from pydantic import BaseModel
from typing import List
from shared.data_classes import GetProcessedResponse
from datetime import datetime
from shared.data_classes import ColorDTO
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
    last_accessed: datetime
    colors: List[ColorDTO] = []
