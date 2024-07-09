from .imageRepository import ImageRepository
from fastapi import UploadFile, HTTPException
from typing import List
from ..userAuthentication.UserAuthenticationRepository import User
import hashlib
from .RGB import RGB
class ImageService:
    def __init__(self,
                 repository: ImageRepository) -> None:
        self.repository = repository
    
    async def upload_and_process_image(self, user: User, file: UploadFile, colors: List[RGB]):
        # validate that file is image
        if not file.content_type.startswith('image'):
            raise HTTPException(status_code=422, detail=f"Expected image file but received: {file.content_type}")
        
        raw_image_url, image_hash = await self.repository.upload_unprocessed_image(user,  file)
        processed_images = []
        #go through RGB
        # check if image exists
        # if it doesn't make call to image pipeline
        for color in colors:
            image_url = self.repository.get_image_url(user, image_hash, color)
            if image_url is not None:
                response = {
                    "uid": user.uid,
                    "color": color,
                    "url": image_url
                }
                processed_images.append(response)
            else:
                # call fetch image
                image_url = await self.repository.upload_processed_image(user, file, color)
                response = {
                    "uid": user.uid,
                    "color": color,
                    "url": image_url
                }
                processed_images.append(response)
        return {
            "original_image": raw_image_url,
            "processed_images": processed_images
        }
    