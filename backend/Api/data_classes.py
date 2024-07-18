from pydantic import BaseModel
from typing import List
from shared.data_classes import GetProcessedResponse
from datetime import datetime
from shared.data_classes import ColorDTO, Image


class UserLoginDto(BaseModel):
    email: str
    password: str


class CreateUserDto(UserLoginDto):
    firstname: str
    lastname: str


class ImageRequestListResponse(BaseModel):
    original_image: str
    processed_images: List[GetProcessedResponse]


class History(BaseModel):
    base_image: str
    last_accessed: datetime
    colors: List[ColorDTO] = []


class HistoryList(BaseModel):
    history: List[History]

class Favorites(BaseModel):
    favorites: List[ColorDTO] = []


class UploadReviewImageDto(BaseModel):
    paint_id: str


class ReviewImageDto(BaseModel):
    paint_id: str
    image_hash: str


class ReviewDto(BaseModel):
    paint_id: str
    review: str
    image_hashes: List[str] = []


class Review(BaseModel):
    paint_id: str
    uid: str
    review: str
    timestamp: datetime
    image_hashes: List[str]


class ReviewList(BaseModel):
    reviews: List[Review]


class GetReviewImageResponse(BaseModel):
    image_hash: str
    paint_id: str
    image_data: Image | None = None
