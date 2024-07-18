import PIL.Image
import numpy as np
import cv2
import asyncio
import time
from typing import Annotated
from pydantic import BaseModel
from fastapi import APIRouter, Depends, Header
import sys 
import os
import io
import PIL

# print(os.path.join(os.getcwd()))
sys.path.append(os.path.join(os.getcwd()))

from dependencies import get_image_repository
from shared.data_classes import Image, GetImageResponse, GetJSONResponse, ColorDTO, RGB, ImageData, GetProcessedResponse, GetMaskResponse
from image_pipeline.dino_sam_singleton import DinoSAMSingleton
from shared.repository.image_repository import ImageRepository


router = APIRouter(
    # specify subroute. All routes in this file will be in the form of /login/{whatever}
    prefix="/image",
    # tags are stricly for metadata (helps with openAPI specifications)
    tags=["image processing"],
    # also metadata
    responses={401: {"description": "Improper image or metadata"}},
)

   
pipeline_lock = asyncio.Lock()


@router.post("/generate", response_model = list[GetProcessedResponse])
async def generate_image(image_data: ImageData,  
                     image_repository: Annotated['ImageRepository',Depends(get_image_repository)]):
    global pipeline_lock
    
    image_response : GetImageResponse = image_repository.get_raw_image_by_hash(image_data.uid, image_data.raw_image_hash, True)
    if image_response == None:
        pass
        
    json_data = {}
    image_json_response : GetJSONResponse = image_repository.get_json_by_hash(image_data.uid, image_data.raw_image_hash)
    if image_json_response:
        json_data = image_json_response.json_data

    image_bytes = image_response.image_data.image_bytes
    image_cv = cv2.imdecode(np.frombuffer(image_bytes, dtype="uint8") , cv2.IMREAD_COLOR)
    
    colored_images = []
    masks = []
    
    await pipeline_lock.acquire()
    
    ds_instance = DinoSAMSingleton.instance()
        
    if json_data and json_data["masks"]:
        mask_responses : list[GetMaskResponse] = image_repository.get_masks_by_hash(image_data.uid, image_data.raw_image_hash, json_data["masks"])
        
        for reponse in mask_responses:
            pil_mask = PIL.Image.open(reponse.mask_data)
            mask = np.array(pil_mask)
            mask = (mask > 0).astype(np.uint8)
            masks.append(mask)
            
        for color in image_data.colors:
            rgb = [color.rgb.r, color.rgb.g, color.rgb.b]
            recolored_image = ds_instance.recolor(image_cv, rgb, masks)
            colored_images.append(recolored_image)
    else:
        rgb_colors = [[color.rgb.r, color.rgb.g, color.rgb.b] for color in image_data.colors]
        masks, colored_images = ds_instance.run_pipeline(image_cv, image_data.raw_image_hash, rgb_colors)
    
    if pipeline_lock.locked():
        pipeline_lock.release()
    # First time processing this image
    bmp_buffers = []
    if not json_data:
        for i in range(len(masks)):
            masks[i][masks[i] > 0] = 1
            masks[i] = masks[i] * 255
            pil_mask = PIL.Image.fromarray(masks[i])
            masks[i] = pil_mask.convert('1')
            buffer = io.BytesIO()
            masks[i].save(buffer, format="BMP")
            bmp_buffers.append(buffer)
        
        mask_hashes = await image_repository.upload_masks(image_data.uid, image_data.raw_image_hash, bmp_buffers)
        
        json_data["masks"] = mask_hashes
        json_data["uid"] = image_data.uid
        json_data["raw_image_hash"] = image_data.raw_image_hash
        json_data["processed"] = []
    
    for i in range(len(image_data.colors)):
        color_item = image_data.colors[i]
        rgb = [color_item.rgb.r, color_item.rgb.g, color_item.rgb.b]
        json_data["processed"].append({
            "paint_id" :  color_item.paint_id,
            "rgb" : rgb,
            "timestamp": time.time(),
        }) 
    
    await image_repository.upload_json(image_data.uid, image_data.raw_image_hash, json_data)
    response = []

    for i in range(len(colored_images)):
        _, image_bytes = cv2.imencode('.jpg', colored_images[i])
        processed_image_hash = await image_repository.upload_processed_image(image_data.uid, image_data.raw_image_hash, image_bytes.tobytes(), image_data.colors[i])
        response.append(GetProcessedResponse(uid=image_data.uid, processed_image_hash=processed_image_hash, color=image_data.colors[i]))
    
    return response
