from shared.repository.image_repository import ImageRepository
from fastapi import UploadFile, HTTPException
from typing import List
from shared.data_classes import ColorDTO, ImageData, GetProcessedResponse, RGB
from Api.data_classes import ImageRequestListResponse
from Api.client.image_server_client import ImageServerClient
from shared.data_classes import GetImageResponse, GetProcessedResponse, ColorDTO


class ImageService:
    def __init__(
        self, repository: ImageRepository, image_server_client: ImageServerClient
    ) -> None:
        self.repository = repository
        self.client = image_server_client

    async def upload_and_process_image(
        self, uid: str, file: UploadFile, colors: List[ColorDTO]
    ):
        # validate that file is image
        if not file.content_type.startswith("image"):
            raise HTTPException(
                status_code=422,
                detail=f"Expected image file but received: {file.content_type}",
            )

        image_hash = await self.repository.upload_unprocessed_image(uid, file)

        to_process = []
        processed_images = []

        # go through RGB
        # check if image exists
        for dto in colors:
            image_response = self.repository.check_image_exists_for_id(
                uid, image_hash, dto
            )
            if image_response is not None:
                response = GetProcessedResponse(
                    uid=uid,
                    processed_image_hash=image_response.image_hash,
                    color=ColorDTO(
                        paint_id=image_response.paintId,
                        rgb=RGB(
                            r=image_response.rgb.r,
                            g=image_response.rgb.g,
                            b=image_response.rgb.b,
                        ),
                    ),
                )
                processed_images.append(response)
            else:
                to_process.append(dto)
        if len(to_process) > 0:
            image_data = ImageData(
                uid=uid, colors=to_process, raw_image_hash=image_hash
            )
            resp = self.client.send_image_process_request(image_data)
            processed_images.extend(resp)

        return ImageRequestListResponse(
            original_image=image_hash, processed_images=processed_images
        )

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

    @staticmethod
    def _get_image_response_to_get_processed_response(
        uid: str, get_image_responses: List[GetImageResponse]
    ):
        ret: List[GetProcessedResponse] = []
        for image_response in get_image_responses:
            processed_response = GetProcessedResponse(
                uid=uid,
                processed_image_hash=image_response.image_hash,
                color=ColorDTO(paint_id=image_response.paintId, rgb=image_response.rgb),
            )
            ret.append(processed_response)
        return ret

    def get_image_summary_by_hash(self, uid: str, hash: str):
        raw_image = self.repository.get_raw_image_by_hash(uid, hash)
        if raw_image is None:
            raise HTTPException(status_code=404)

        processed_files = self.repository.get_all_processed_images(uid, hash)
        processed_response = self._get_image_response_to_get_processed_response(
            uid, processed_files
        )
        return ImageRequestListResponse(
            original_image=raw_image.image_hash, processed_images=processed_response
        )
