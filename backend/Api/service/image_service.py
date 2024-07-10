from shared.repository.imageRepository import ImageRepository
from fastapi import UploadFile, HTTPException
from typing import List
from Api.repository.UserAuthenticationRepository import User
from shared.data_classes import ImageUploadDTO


class ImageService:
    def __init__(self,
                 repository: ImageRepository) -> None:
        self.repository = repository

    async def upload_and_process_image(self, user: User, file: UploadFile, colors: List[ImageUploadDTO]):
        # validate that file is image
        if not file.content_type.startswith('image'):
            raise HTTPException(status_code=422, detail=f"Expected image file but received: {file.content_type}")

        image_hash = await self.repository.upload_unprocessed_image(user.uid, file)
        processed_images = []
        # go through RGB
        # check if image exists
        # if it doesn't make call to image pipeline
        for DTO in colors:
            image_response = self.repository.check_image_exists_for_id(user.uid, image_hash, DTO)
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
                image_response = await self.repository.upload_processed_image(user.uid, file, DTO)
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

    def get_image_by_hash(self, user: User, hash: str):
        is_raw_image = len(hash.split("-")) == 1
        if is_raw_image:
            image = self.repository.get_raw_image_by_hash(user.uid, hash, True)
            if image is None or image.image_data is None:
                raise HTTPException(status_code=404)
            return image.image_data
        else:
            image = self.repository.get_processed_image_by_hash(user.uid, hash, True)
            if image is None or image.image_data is None:
                raise HTTPException(status_code=404)
            return image.image_data

    def get_image_summary_by_hash(self, user: User, hash: str):
        raw_image = self.repository.get_raw_image_by_hash(user.uid, hash)
        if raw_image is None:
            raise HTTPException(status_code=404)

        processed_files = self.repository.get_all_processed_images(user.uid, hash)
        return {
            "original_image": raw_image.image_hash,
            "processed_images": processed_files
        }
