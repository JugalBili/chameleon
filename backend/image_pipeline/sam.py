import os, sys

sys.path.append(os.path.join(os.getcwd(), "segment_anything"))

import copy
from PIL import Image
import torch
import numpy as np

from segment_anything import sam_model_registry, SamPredictor
import numpy as np


class SAM:
    def __init__(self, model_file, model_type, device):
        self.model_file = model_file
        self.model_type = model_type
        self.device = device
        self.sam_model = self._load_sam_model()

    def _load_sam_model(self):
        return SamPredictor(
            sam_model_registry[self.model_type](checkpoint=self.model_file).to(
                device=self.device
            )
        )

    def apply_mask_to_image(self, image_pil, masks):
        image = copy.deepcopy(image_pil)

        for mask in masks:
            # ensure mask is binary
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

    def run_inference(self, image_pil, pred_dict):
        size = image_pil.size
        H, W = size[1], size[0]

        boxes_filt = copy.deepcopy(pred_dict["boxes"])

        sam_image = np.array(image_pil)
        self.sam_model.set_image(sam_image)

        for i in range(boxes_filt.size(0)):
            boxes_filt[i] = boxes_filt[i] * torch.Tensor([W, H, W, H])
            boxes_filt[i][:2] -= boxes_filt[i][2:] / 2
            boxes_filt[i][2:] += boxes_filt[i][:2]

        boxes_filt = boxes_filt.to(self.device)

        transformed_boxes = self.sam_model.transform.apply_boxes_torch(
            boxes_filt, sam_image.shape[:2]
        )
        masks, _, _ = self.sam_model.predict_torch(
            point_coords=None,
            point_labels=None,
            boxes=transformed_boxes,
            multimask_output=False,
        )

        masks = masks.cpu().numpy()
        masks = np.squeeze(masks, axis=1)

        return masks
