from fastapi import APIRouter, Depends, Response, UploadFile, HTTPException, File, Form
from typing import Annotated, Union
from pydantic import ValidationError
import json

from Api.dependencies import get_user, get_gallery_service
from Api.repository.user_authentication_repository import User
from Api.data_classes import ReviewList, ReviewImageDTO, ReviewDTO, UploadReviewImageDTO


router = APIRouter(
    # specify sub-route. All routes in this file will be in the form of /gallery/{whatever}
    prefix="/gallery",
    # tags are strictly for metadata (helps with openAPI specifications)
    tags=["gallery"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)

@router.get("/getReviews/{paint_id}")
async def get_reviews(
    history_service: Annotated["GalleryService", Depends(get_gallery_service)],
    user: Annotated['User', Depends(get_user)],
    paint_id: str,
    by_user: Union[bool, None] = None
    
):    
    reviews: ReviewList = await history_service.get_reviews_by_paint(paint_id, user if by_user else None)
    
    return reviews.reviews

@router.get("/getImage")
def get_image_by_hash(gallery_service: Annotated['GalleryService', Depends(get_gallery_service)],
                      user: Annotated['User', Depends(get_user)],
                      review_dto: ReviewImageDTO
                      ):
    image = gallery_service.get_image_by_hash(review_dto.paint_id, review_dto.image_hash)
    return Response(content=image.image_bytes, media_type=image.contentType)


@router.post("/uploadImage")
async def upload_file(gallery_service: Annotated['GalleryService', Depends(get_gallery_service)],
                      user: Annotated['User', Depends(get_user)],
                      file: UploadFile = File(...),
                      image_info: str = Form(...)):
    try:
        image_info = UploadReviewImageDTO(**json.loads(image_info))
    except (SyntaxError, ValidationError, TypeError) as e:
        raise HTTPException(status_code=422, detail=f"Invalid 'image_info' input: {e}")
    except Exception as e:
        raise HTTPException(status_code=422, detail=f"invalid image_info input: {e}")

    image_hash = await gallery_service.upload_image(user, file, image_info)
    
    return image_hash

@router.post("/addReview")
async def add_review(gallery_service: Annotated['GalleryService', Depends(get_gallery_service)],
                      user: Annotated['User', Depends(get_user)],
                      review_dto: ReviewDTO):
    
    return await gallery_service.add_paint_review(review_dto, user)