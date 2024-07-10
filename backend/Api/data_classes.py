from pydantic import BaseModel
from typing import List


class UserLoginDto(BaseModel):
    email: str
    password: str


class CreateUserDTO(UserLoginDto):
    firstname: str
    lastname: str


class RGB(BaseModel):
    r: int
    g: int
    b: int


class ImageUploadDTO(BaseModel):
    paintId: str
    color: RGB


class Image(BaseModel):
    image_bytes: bytes
    contentType: str


class GetImageResponse(BaseModel):
    image_hash: str
    rgb: RGB | None
    paintId: str | None
    image_data: Image | None = None
