import numpy as np
import cv2
import asyncio
import time
from typing import Annotated
from pydantic import BaseModel
from fastapi import APIRouter, Depends, Header

from image_server.dependencies import get_image_repository
from shared.data_classes import Image, GetImageResponse, GetJSONResponse, ColorDTO, RGB, ImageData, GetProcessedResponse
from image_server.image_pipeline.dino_sam_singleton import DinoSAMSingleton


router = APIRouter(
    # specify subroute. All routes in this file will be in the form of /login/{whatever}
    prefix="/image",
    # tags are stricly for metadata (helps with openAPI specifications)
    tags=["image processing"],
    # also metadata
    responses={401: {"description": "Improper image or metadata"}},
)

   
pipeline_lock = asyncio.Lock()


@router.post("/generate", response_model = list[GetImageResponse])
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
    image_cv = cv2.imdecode(np.asarray(image_bytes, dtype="uint8") , cv2.IMREAD_COLOR)
    
    colored_images = []
    masks = []
    
    pipeline_lock.acquire()
    
    ds_instance = DinoSAMSingleton.instance()
        
    if json_data and json_data["masks"]:
        masks = json_data["masks"]
        for color in image_data.colors:
            rgb = [color.rgb.r, color.rgb.g, color.rgb.b]
            recolored_image = ds_instance.recolor(image_cv, rgb, masks)
            colored_images.append(recolored_image)
    else:
        rgb_colors = [[color.rgb.r, color.rgb.g, color.rgb.b] for color in image_data.colors]
        masks, colored_images = ds_instance.run_pipeline(image_cv, image_data.raw_image_hash, rgb_colors)
    
    pipeline_lock.release()
  
    if not json_data:
        json_data["masks"] = masks
        json_data["uid"] = image_data.uid
        json_data["raw_image_hash"] = image_data.raw_image_hash
        json_data["processed"] = []
    
    for i in range(len(image_data.colors)):
        color_item = image_data.colors[i]
        rgb = [color_item.color.r, color_item.color.g, color_item.color.b]
        json_data["processed"].append({
            "paint_id" :  color_item.paint_id,
            "rgb" : rgb,
            "timestamp": time.time(),
        }) 
    
    image_repository.upload_json(image_data.uid, image_data.raw_image_hash, json_data)
    response = []

    for i in range(len(colored_images)):
        _, image_bytes = cv2.imencode('.jpg', colored_images[i])
        processed_image_hash = await image_repository.upload_processed_image(image_data.uid, image_data.raw_image_hash, image_bytes.tobytes(), image_data.colors[i])
        response.append(GetProcessedResponse(uid=image_data.uid, processed_image_hash=processed_image_hash, color=image_data.colors[i]))
    
    return response