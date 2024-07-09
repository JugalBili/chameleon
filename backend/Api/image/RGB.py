from pydantic import BaseModel
from typing import List
class RGB(BaseModel):
    r: int
    g: int
    b: int

class ImageUploadDTO(BaseModel):
    paintId: str
    color: RGB