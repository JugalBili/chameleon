from fastapi import HTTPException, UploadFile

from Api.repository.gallery_repository import GalleryRepository
from Api.repository.user_authentication_repository import User
from Api.data_classes import UploadReviewImageDto, ReviewDto


class GalleryService:
    def __init__(self, repository: GalleryRepository) -> None:
        self.repository = repository

    async def get_all_reviews_by_paint(self, paint_id: str):
        return await self.repository.get_paint_reviews(paint_id)

    async def get_paint_review_by_user(self, paint_id: str, user: User):
        return await self.repository.get_paint_user_reviews_or_throw(paint_id, user)

    def get_image_by_hash(self, paint_id: str, hash: str):
        image = self.repository.get_review_image(hash, paint_id, True)
        if image is None or image.image_data is None:
            raise HTTPException(status_code=404, detail="could not fetch review image")

        return image.image_data

    async def upload_image(self, user: User, file: UploadFile, paint_id: str):
        # validate that file is image
        if not file.content_type.startswith("image"):
            raise HTTPException(
                status_code=422,
                detail=f"Expected image file but received: {file.content_type}",
            )

        return await self.repository.upload_review_image(user, file, paint_id)

    async def add_paint_review(self, review_dto: ReviewDto, user: User):
        return await self.repository.add_paint_review(review_dto, user)
