from .imageRepository import ImageRepository
from fastapi import UploadFile, HTTPException
from typing import List
from ..userAuthentication.UserAuthenticationRepository import User
from .RGB import ImageUploadDTO
class ImageService:
    def __init__(self,
                 repository: ImageRepository) -> None:
        self.repository = repository
    
    async def upload_and_process_image(self, user: User, file: UploadFile, colors: List[ImageUploadDTO]):
        # validate that file is image
        if not file.content_type.startswith('image'):
            raise HTTPException(status_code=422, detail=f"Expected image file but received: {file.content_type}")
        
        image_hash = await self.repository.upload_unprocessed_image(user,  file)
        processed_images = []
        #go through RGB
        # check if image exists
        # if it doesn't make call to image pipeline
        for DTO in colors:
            image_response = self.repository.check_image_exists_for_id(user, image_hash, DTO)
            if image_response is not None:
                response = {
                    "uid": user.uid,
                    "rgb": image_response.rgb,
                    "paintId": image_response.paintId,
                    "imageHash": image_response.image_hash
                }
                processed_images.append(response)
            else:
                # TODO: figure out best way to send data to image pipeline

                # call fetch image
                image_response = await self.repository.upload_processed_image(user, file, DTO)
                response = {
                    "uid": user.uid,
                    "rgb": image_response.rgb,
                    "paintId": image_response.paintId,
                    "imageHash": image_response.image_hash
                }
                processed_images.append(response)
        return {
            "original_image": image_hash,
            "processed_images": processed_images
        }
    