import os, sys

sys.path.append(os.path.join(os.path.dirname(__file__), "FastSAM"))

import copy
from PIL import Image
import torch
import numpy as np

from FastSAM.fastsam import FastSAM, FastSAMPrompt


class FSAM:
    def __init__(self, model_file, device, mask_threshold, iou_threshold):
        self.model_file = model_file
        self.device = device
        self.mask_threshold = mask_threshold
        self.iou_threshold = iou_threshold
        self.fsam_model = self._load_fsam_model()
        self.prompt_process = None

    def _load_fsam_model(self):
        return FastSAM(self.model_file)

    def apply_mask_to_image(self, image_pil, masks):
        image = copy.deepcopy(image_pil)

        image = self.prompt_process.plot_to_result(
            annotations=masks,
            better_quality=True,
            withContours=True,
        )

        image = image[:, :, ::-1]

        return image

    def run_inference(self, image_pil, pred_dict):
        size = image_pil.size
        H, W = size[1], size[0]

        boxes_filt = copy.deepcopy(pred_dict["boxes"])

        fsam_image = image_pil

        for i in range(boxes_filt.size(0)):
            boxes_filt[i] = boxes_filt[i] * torch.Tensor([W, H, W, H])
            boxes_filt[i][:2] -= boxes_filt[i][2:] / 2
            boxes_filt[i][2:] += boxes_filt[i][:2]

        boxes_filt = boxes_filt.tolist()

        everything_results = self.fsam_model(
            fsam_image,
            device=self.device,
            retina_masks=True,
            imgsz=1024,
            conf=self.mask_threshold,
            iou=self.iou_threshold,
        )

        self.prompt_process = FastSAMPrompt(
            fsam_image, everything_results, device=self.device
        )
        masks = self.prompt_process.box_prompt(bboxes=boxes_filt)

        return masks
