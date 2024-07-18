from fastapi import HTTPException, UploadFile

from Api.repository.gallery_repository import GalleryRepository
from Api.repository.user_authentication_repository import User
from Api.data_classes import UploadReviewImageDto, ReviewDto


class GalleryService:
    def __init__(self, repository: GalleryRepository) -> None:
        self.repository = repository

    async def get_reviews_by_paint(self, paint_id: str, user: User | None):
        if user:
            return await self.repository.get_paint_user_reviews(paint_id, user)

        return await self.repository.get_paint_reviews(paint_id)

    def get_image_by_hash(self, paint_id: str, hash: str):
        image = self.repository.get_review_image(hash, paint_id, True)
        if image is None or image.image_data is None:
            raise HTTPException(status_code=404)

        return image.image_data

    async def upload_image(
        self, user: User, file: UploadFile, image_data: UploadReviewImageDto
    ):
        # validate that file is image
        if not file.content_type.startswith("image"):
            raise HTTPException(
                status_code=422,
                detail=f"Expected image file but received: {file.content_type}",
            )

        return await self.repository.upload_review_image(
            user, file, image_data.paint_id
        )

    async def add_paint_review(self, review_dto: ReviewDto, user: User):
        return await self.repository.add_paint_review(review_dto, user)
