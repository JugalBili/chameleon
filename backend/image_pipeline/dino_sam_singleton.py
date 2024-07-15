import os
import copy
from PIL import Image
import cv2
import skimage.exposure
import numpy as np
import torch
import time
import sys

cur_path = os.path.join(os.path.dirname(__file__))
sys.path.append(cur_path)

from color_helper import calc_delta_CIEDE2000
from dino import Dino
from sam import SAM


DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")
GD_FILENAME = os.path.join(cur_path, "models/groundingdino_swint_ogc.pth")
GD_CONFIG_FILENAME = os.path.join(cur_path, "models/GroundingDINO_SwinT_OGC.py")
SAM_FILENAME =  os.path.join(cur_path, "models/sam_vit_h_4b8939.pth")
SAM_TYPE = "vit_h"

# hyper-param for GroundingDINO
CAPTION = "wall"
BOX_THRESHOLD = 0.30
TEXT_THRESHOLD = 0.35

# Parameter for Mask Bucketing
MAX_DELTA = 30


class Singleton:
    """
    A non-thread-safe helper class to ease implementing singletons.
    This should be used as a decorator -- not a metaclass -- to the
    class that should be a singleton.

    The decorated class can define one `__init__` function that
    takes only the `self` argument. Also, the decorated class cannot be
    inherited from. Other than that, there are no restrictions that apply
    to the decorated class.

    To get the singleton instance, use the `instance` method. Trying
    to use `__call__` will result in a `TypeError` being raised.

    """

    def __init__(self, decorated):
        self._decorated = decorated

    def instance(self):
        """
        Returns the singleton instance. Upon its first call, it creates a
        new instance of the decorated class and calls its `__init__` method.
        On all subsequent calls, the already created instance is returned.

        """
        try:
            return self._instance
        except AttributeError:
            self._instance = self._decorated()
            return self._instance

    def __call__(self):
        raise TypeError("Singletons must be accessed through `instance()`.")

    def __instancecheck__(self, inst):
        return isinstance(inst, self._decorated)


@Singleton
class DinoSAMSingleton:
    def __init__(self):
        self.gd_predictor = Dino(GD_FILENAME, GD_CONFIG_FILENAME, 'cpu')
        print("GroundingDINO Model Loaded")
        self.sam_predictor = SAM(SAM_FILENAME, SAM_TYPE, DEVICE)
        print("SAM Model Loaded")

    def run_pipeline(self, image_cv, image_name, colors):
        print(f"=== Starting Grounded SAM Pipeline for Image {image_name} ===\n")
        image_pil = Image.fromarray(cv2.cvtColor(image_cv, cv2.COLOR_BGR2RGB)) 

        pred_dict = self.gd_predictor.run_inference(
            image_pil, CAPTION, BOX_THRESHOLD, TEXT_THRESHOLD
        )
        masks = self.sam_predictor.run_inference(image_pil, pred_dict)

        boxed_image = self.gd_predictor.apply_boxes_to_image(image_pil, pred_dict)
        # boxed_image.save(
        #     f"{os.path.splitext(os.path.basename(image_name))[0]}_boxed.jpg"
        # )
        masked_image = self.sam_predictor.apply_mask_to_image(image_pil, masks)
        # masked_image.save(
        #     f"{os.path.splitext(os.path.basename(image_name))[0]}_mask.jpg"
        # )

        print("\n=== Starting Image Recoloring ===\n")
        buckets = self.create_buckets(image_cv, masks)

        masks = self.merge_masks(buckets, masks)
        masked_image = self.sam_predictor.apply_mask_to_image(image_pil, masks)
        # masked_image.save(
        #     f"{os.path.splitext(os.path.basename(image_name))[0]}_mask_merged.jpg"
        # )

        colored_images = []
        
        for color in colors:
            recolored_image = self.recolor(image_cv, color, masks)
            color_string = "-".join([str(val) for val in color])
            # cv2.imwrite(
            #     f"{os.path.splitext(os.path.basename(image_name))[0]}_recolored_{color_string}.png",
            #     recolored_image,
            # )
            colored_images.append(recolored_image)
        
        print("\n=== Pipeline Finished ===\n")
                
        return masks, colored_images

    def create_buckets(self, image_cv, masks):
        buckets = {}

        image_lab = cv2.cvtColor(image_cv, cv2.COLOR_BGR2LAB)

        for i in range(len(masks)):
            mask = (masks[i].astype(np.uint8) * 255).astype(np.uint8)
            ave_color = cv2.mean(image_lab, mask=mask)[:3]

            if not buckets:
                buckets[len(buckets) + 1] = {"avg": list(ave_color), "masks": [i]}
            else:
                smallest_delta = float("inf")
                best_bucket = -1

                for key in buckets.keys():
                    ave_bucket_color = buckets[key]["avg"]
                    distance = calc_delta_CIEDE2000(ave_bucket_color, ave_color)
                    if distance < smallest_delta:
                        smallest_delta = distance
                        best_bucket = key

                if best_bucket != -1:
                    if smallest_delta > MAX_DELTA:
                        buckets[len(buckets) + 1] = {
                            "avg": list(ave_color),
                            "masks": [i],
                        }
                    else:
                        buckets[key]["avg"] = np.mean(
                            np.array([buckets[key]["avg"], ave_color]), axis=0
                        ).tolist()
                        buckets[key]["masks"].append(i)
        return buckets

    def merge_masks(self, buckets, masks):
        new_masks = []

        for key in buckets.keys():
            mask_list = buckets[key]["masks"]
            new_mask = None
            for index in mask_list:
                if new_mask is None:
                    new_mask = masks[index]
                else:
                    new_mask = np.logical_or(new_mask, masks[index])
            new_masks.append(new_mask)
        return new_masks

    def recolor(self, image_cv, color_rgb, masks):
        image = copy.deepcopy(image_cv).astype(np.int16)
        color_bgr = color_rgb[::-1]
        desired_color = np.asarray(color_bgr, dtype=np.float64)

        for wallmask in masks:
            mask = (wallmask.astype(np.uint8) * 255).astype(np.uint8)

            # get average bgr color of masked section
            ave_color = cv2.mean(image, mask=mask)[:3]
            # print(f"Average color = {ave_color}")

            # compute difference colors and make into an image the same size as input
            diff_color = desired_color - ave_color
            diff_color = np.full_like(image, diff_color, dtype=np.int16)

            # shift input image color
            # cv2.add clips automatically
            new_image = cv2.add(image, diff_color)

            # antialias mask, convert to float in range 0 to 1 and make 3-channels
            mask = cv2.GaussianBlur(
                mask, (0, 0), sigmaX=3, sigmaY=3, borderType=cv2.BORDER_DEFAULT
            )
            mask = skimage.exposure.rescale_intensity(
                mask, in_range=(100, 150), out_range=(0, 1)
            ).astype(np.float32)
            mask = cv2.merge([mask, mask, mask])

            # combine img and new_img using mask
            result = image * (1 - mask) + new_image * mask
            result = result.clip(0, 255).astype(np.int16)
            image = result

        return image


if __name__ == "__main__":
    print("Running pipeline test")

    dir_path = os.path.dirname(os.path.realpath(__file__))

    # PPG "Viva La Bleu"
    # "PPG1242-3"
    color = [151, 190, 226]  # rgb

    # # Benjamin Moore "Grape Green"
    # # "2027-40"
    # color = [213, 216, 105]  # rgb

    start = time.time()
    inst1 = DinoSAMSingleton.instance()
    end = time.time()
    print(f"Loading models took {end-start} seconds!")
    # image1 = os.path.join(dir_path, "tests/img.jpg")
    # inst1.run_pipeline(image1, color)

    inst2 = DinoSAMSingleton.instance()
    image2_path = os.path.join(dir_path, "tests/img2.jpg")
    image2_cv = cv2.imread(image2_path, cv2.IMREAD_COLOR)
    start = time.time()
    inst2.run_pipeline(image2_cv, os.path.basename(image2_path), [color])
    end = time.time()
    print(f"Grounded SAM Pipeline took {end-start} seconds!")
