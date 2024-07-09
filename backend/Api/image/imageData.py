from pydantic import BaseModel
class Image(BaseModel):
    image_bytes: bytes
    contentType: str