import os, sys

sys.path.append(os.path.join(os.getcwd(), "GroundingDINO"))

import argparse
import copy

# from IPython.display import display
from PIL import Image, ImageDraw, ImageFont
import torch
import torchvision
from torchvision.ops import box_convert

# Grounding DINO
import GroundingDINO.groundingdino.datasets.transforms as T
from GroundingDINO.groundingdino.models import build_model
from GroundingDINO.groundingdino.util import box_ops
from GroundingDINO.groundingdino.util.slconfig import SLConfig
from GroundingDINO.groundingdino.util.utils import (
    clean_state_dict,
    get_phrases_from_posmap,
)
from GroundingDINO.groundingdino.util.inference import (
    annotate,
    load_image,
    predict,
    Model,
)

import supervision as sv

# segment anything
from segment_anything import sam_hq_model_registry, sam_model_registry, SamPredictor
import cv2
import numpy as np
import matplotlib.pyplot as plt

from huggingface_hub import hf_hub_download


DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")
DEVICE = "cpu"
print(DEVICE)


ckpt_filename = "./models/groundingdino_swint_ogc.pth"
ckpt_config_filename = "./models/GroundingDINO_SwinT_OGC.py"


def load_model(model_config, model_ckpt, device):
    args = SLConfig.fromfile(model_config)
    args.device = device
    model = build_model(args)
    checkpoint = torch.load(model_ckpt, map_location="cpu")
    load_res = model.load_state_dict(
        clean_state_dict(checkpoint["model"]), strict=False
    )
    print(load_res)
    _ = model.eval()
    return model


grounding_dino_model = load_model(ckpt_config_filename, ckpt_filename, device=DEVICE)


print("DINO activated")

sam_checkpoint = "./models/sam_vit_h_4b8939.pth"
sam_type = "vit_h"

sam_predictor = SamPredictor(
    sam_model_registry[sam_type](checkpoint=sam_checkpoint).to(device=DEVICE)
)
print("SAM activated")

# Predict classes and hyper-param for GroundingDINO
SOURCE_IMAGE_PATH = "img.jpg"
CLASSES = ["walls"]
BOX_THRESHOLD = 0.15
TEXT_THRESHOLD = 0.25
NMS_THRESHOLD = 0.8


# load image
# image = cv2.imread(SOURCE_IMAGE_PATH)


def load_image(image_path):
    image_pil = Image.open(image_path).convert("RGB")  # load image

    transform = T.Compose(
        [
            T.RandomResize([800], max_size=1333),
            T.ToTensor(),
            T.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225]),
        ]
    )
    image, _ = transform(image_pil, None)  # 3, h, w
    return image_pil, image


image_pil, image = load_image(SOURCE_IMAGE_PATH)

# detect objects


def get_grounding_output(
    model,
    image,
    caption,
    box_threshold,
    text_threshold,
    with_logits=True,
    device=DEVICE,
):
    caption = caption.lower()
    caption = caption.strip()
    if not caption.endswith("."):
        caption = caption + "."
    model = model.to(device)
    image = image.to(device)
    with torch.no_grad():
        outputs = model(image[None], captions=[caption])
    logits = outputs["pred_logits"].cpu().sigmoid()[0]  # (nq, 256)
    boxes = outputs["pred_boxes"].cpu()[0]  # (nq, 4)
    logits.shape[0]

    # filter output
    logits_filt = logits.clone()
    boxes_filt = boxes.clone()
    filt_mask = logits_filt.max(dim=1)[0] > box_threshold
    logits_filt = logits_filt[filt_mask]  # num_filt, 256
    boxes_filt = boxes_filt[filt_mask]  # num_filt, 4
    logits_filt.shape[0]

    # get phrase
    tokenlizer = model.tokenizer
    tokenized = tokenlizer(caption)
    # build pred
    pred_phrases = []
    for logit, box in zip(logits_filt, boxes_filt):
        pred_phrase = get_phrases_from_posmap(
            logit > text_threshold, tokenized, tokenlizer
        )
        if with_logits:
            pred_phrases.append(pred_phrase + f"({str(logit.max().item())[:4]})")
        else:
            pred_phrases.append(pred_phrase)

    return boxes_filt, pred_phrases


boxes_filt, pred_phrases = get_grounding_output(
    grounding_dino_model,
    image,
    CLASSES[0],
    BOX_THRESHOLD,
    TEXT_THRESHOLD,
    device=DEVICE,
)

size = image_pil.size
H, W = size[1], size[0]

pred_dict = {
    "boxes": boxes_filt,
    "size": [size[1], size[0]],  # H,W
    "labels": pred_phrases,
}


def plot_boxes_to_image(image_pil, tgt):
    H, W = tgt["size"]
    boxes = tgt["boxes"]
    labels = tgt["labels"]
    assert len(boxes) == len(labels), "boxes and labels must have same length"

    draw = ImageDraw.Draw(image_pil)
    mask = Image.new("L", image_pil.size, 0)
    mask_draw = ImageDraw.Draw(mask)

    # draw boxes and masks
    for box, label in zip(boxes, labels):
        # from 0..1 to 0..W, 0..H
        box = box * torch.Tensor([W, H, W, H])
        # from xywh to xyxy
        box[:2] -= box[2:] / 2
        box[2:] += box[:2]
        # random color
        color = tuple(np.random.randint(0, 255, size=3).tolist())
        # draw
        x0, y0, x1, y1 = box
        x0, y0, x1, y1 = int(x0), int(y0), int(x1), int(y1)

        draw.rectangle([x0, y0, x1, y1], outline=color, width=6)
        # draw.text((x0, y0), str(label), fill=color)

        font = ImageFont.truetype("arial.ttf", 40)
        if hasattr(font, "getbbox"):
            bbox = draw.textbbox((x0, y0), str(label), font)
        else:
            w, h = draw.textsize(str(label), font)
            bbox = (x0, y0, w + x0, y0 + h)
        # bbox = draw.textbbox((x0, y0), str(label))
        draw.rectangle(bbox, fill=color)
        draw.text((x0, y0), str(label), fill="white", font=font)

        mask_draw.rectangle([x0, y0, x1, y1], fill=255, width=6)

    return image_pil, mask


image_with_box = plot_boxes_to_image(copy.deepcopy(image_pil), pred_dict)[0]
image_with_box.save("groundingdino_annotated_image.jpg")


# Prompting SAM with detected boxes
print(f"image pil size = {image_pil.size}")
sam_image = np.array(image_pil)
print(f"sam image size = {sam_image.shape}")
sam_predictor.set_image(sam_image)

for i in range(boxes_filt.size(0)):
    boxes_filt[i] = boxes_filt[i] * torch.Tensor([W, H, W, H])
    boxes_filt[i][:2] -= boxes_filt[i][2:] / 2
    boxes_filt[i][2:] += boxes_filt[i][:2]

boxes_filt = boxes_filt.to(DEVICE)

# from FastSAM.fastsam.model import FastSAM
# from FastSAM.fastsam.prompt import FastSAMPrompt

# fast_sam_file = "FastSAM-x.pt"
# fast_sam = FastSAM(fast_sam_file)
# everything_results = fast_sam( image_pil,
#   device=DEVICE,
#   retina_masks=True,
#   imgsz=1024,
#   conf=0.3,
#   iou=0.8
# )
# prompt_process = FastSAMPrompt(image_pil, everything_results, device=DEVICE)
# boxes = [box.tolist() for box in boxes_filt]
# ann = prompt_process.box_prompt(bboxes=boxes)
# prompt_process.plot(annotations=ann if ann.any() else None, output_path="grounded_sam_annotated_image_test.jpg")

transformed_boxes = sam_predictor.transform.apply_boxes_torch(
    boxes_filt, sam_image.shape[:2]
)
masks, _, _ = sam_predictor.predict_torch(
    point_coords=None,
    point_labels=None,
    boxes=transformed_boxes,
    multimask_output=False,
)
# print(masks)

# def show_box(box, ax, label):
#     x0, y0 = box[0], box[1]
#     w, h = box[2] - box[0], box[3] - box[1]
#     ax.add_patch(plt.Rectangle((x0, y0), w, h, edgecolor='green', facecolor=(0,0,0,0), lw=2))
#     ax.text(x0, y0, label)


def apply_masks(image, masks):
    for mask in masks:
        # print(mask.cpu().numpy())
        mask = mask.cpu().numpy()[0]
        # Ensure the mask is binary
        mask = mask > 0
        # Generate a random color
        color = tuple(
            np.concatenate([np.random.randint(0, 256, 3), np.array([127])], axis=0)
        )

        # Create an image from the mask
        mask_image = Image.fromarray((mask * 255).astype(np.uint8), mode="L")

        # Apply the mask with the random color
        colored_mask = Image.new("RGBA", image.size, color)
        image = Image.composite(colored_mask, image, mask_image)

    return image


masked_image = apply_masks(copy.deepcopy(image_pil).convert("RGBA"), masks).convert(
    "RGB"
)
masked_image.save("grounded_sam_annotated_image.jpg")
