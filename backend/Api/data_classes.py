from pydantic import BaseModel
from typing import List
from shared.data_classes import GetProcessedResponse

class UserLoginDto(BaseModel):
    email: str
    password: str


class CreateUserDto(UserLoginDto):
    firstname: str
    lastname: str

class ImageRequestListResponse(BaseModel):
    original_image: str
    processed_images: List[GetProcessedResponse]
