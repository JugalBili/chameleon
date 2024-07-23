from fastapi import APIRouter, Depends, Response, UploadFile, HTTPException, File, Form
from typing import Annotated
from pydantic import ValidationError
import json

from Api.dependencies import get_user, get_gallery_service
from Api.repository.user_authentication_repository import User
from Api.data_classes import ReviewImageDto, ReviewDto
from Api.service.gallery_service import GalleryService

router = APIRouter(
    # specify sub-route. All routes in this file will be in the form of /gallery/{whatever}
    prefix="/gallery",
    # tags are strictly for metadata (helps with openAPI specifications)
    tags=["gallery"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)


@router.get("/review/{paint_id}")
async def get_user_review(
    gallery_service: Annotated["GalleryService", Depends(get_gallery_service)],
    user: Annotated["User", Depends(get_user)],
    paint_id: str,
):

    review = await gallery_service.get_paint_review_by_user(paint_id, user)

    return review


@router.get("/review/all/{paint_id}")
async def get_all_reviews(
    gallery_service: Annotated["GalleryService", Depends(get_gallery_service)],
    user: Annotated["User", Depends(get_user)],
    paint_id: str,
):

    reviews = await gallery_service.get_all_reviews_by_paint(paint_id)
    return {"reviews": reviews.reviews}


@router.post("/get-image")
def get_image_by_hash(
    gallery_service: Annotated["GalleryService", Depends(get_gallery_service)],
    user: Annotated["User", Depends(get_user)],
    review_dto: ReviewImageDto,
):
    print("Review DTO: ", review_dto.model_dump())
    image = gallery_service.get_image_by_hash(
        review_dto.paint_id, review_dto.image_hash
    )

    return Response(content=image.image_bytes, media_type=image.contentType)


@router.post("/create-review")
async def upload_file(
    gallery_service: Annotated["GalleryService", Depends(get_gallery_service)],
    user: Annotated["User", Depends(get_user)],
    files: list[UploadFile] = File(None),
    review: str = Form(...),
):
    print("Received Review: ", review)
    try:
        review_dto = ReviewDto(**json.loads(review))
    except (SyntaxError, ValidationError, TypeError) as e:
        raise HTTPException(status_code=422, detail=f"Invalid 'image_info' input: {e}")
    except Exception as e:
        raise HTTPException(status_code=422, detail=f"invalid image_info input: {e}")
    if files:
        for file in files: 
            image_hash = await gallery_service.upload_image(user, file, review_dto.paint_id)
            review_dto.image_hashes.append(image_hash)
    await gallery_service.add_paint_review(review_dto, user)
    return Response(status_code=204)
