from shared.repository.image_repository import ImageRepository
from fastapi import UploadFile, HTTPException
from typing import List
from shared.data_classes import ColorDTO, ImageData


class ImageService:
    def __init__(self,
                 repository: ImageRepository) -> None:
        self.repository = repository

    async def upload_and_process_image(self, uid: str, file: UploadFile, colors: List[ColorDTO]):
        # validate that file is image
        if not file.content_type.startswith('image'):
            raise HTTPException(status_code=422, detail=f"Expected image file but received: {file.content_type}")

        image_hash = await self.repository.upload_unprocessed_image(uid, file)
        
        to_process = []
        processed_images = []

        # go through RGB
        # check if image exists
        for dto in colors:
            image_response = self.repository.check_image_exists_for_id(uid, image_hash, dto)
            if image_response is not None:
                response = {
                    "uid": uid,
                    "color": {
                        "paintid": image_response.paintId,
                        "color": image_response.rgb
                    },
                    "processed_image_hash": image_response.image_hash
                }
                processed_images.append(response)
            else:
                to_process.append(dto)
                # TODO: figure out best way to send data to image pipeline

                # call fetch image
                image_response = await self.repository.upload_processed_image(uid, file, dto)
                response = {
                    "uid": uid,
                    "rgb": image_response.rgb,
                    "paintId": image_response.paintId,
                    "processed_image_hash": image_response.image_hash
                }
                processed_images.append(response)
        
        image_data = ImageData(uid=uid, colors=to_process, raw_image_hash=image_hash)
        # call image pipeline with above image data as a payload
        # below is mock response
        processed_responses = [
            {
                "uid": uid,
                "color": {
                    "paintid": image_response.paintId,
                    "color": image_response.rgb
                },
                "processed_image_hash": image_response.image_hash
            }
        ]
        
        processed_images.extend(processed_responses)
        
        return {
            "original_image": image_hash,
            "processed_images": processed_images
        }

    def get_image_by_hash(self, uid: str, hash: str):
        is_raw_image = len(hash.split("-")) == 1
        if is_raw_image:
            image = self.repository.get_raw_image_by_hash(uid, hash, True)
            if image is None or image.image_data is None:
                raise HTTPException(status_code=404)
            return image.image_data
        else:
            image = self.repository.get_processed_image_by_hash(uid, hash, True)
            if image is None or image.image_data is None:
                raise HTTPException(status_code=404)
            return image.image_data

    def get_image_summary_by_hash(self, uid: str, hash: str):
        raw_image = self.repository.get_raw_image_by_hash(uid, hash)
        if raw_image is None:
            raise HTTPException(status_code=404)

        processed_files = self.repository.get_all_processed_images(uid, hash)
        return {
            "original_image": raw_image.image_hash,
            "processed_images": processed_files
        }
