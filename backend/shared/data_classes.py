from pydantic import BaseModel


class RGB(BaseModel):
    r: int
    g: int
    b: int


class ColorDTO(BaseModel):
    paint_id: str
    color: RGB


class Image(BaseModel):
    image_bytes: bytes
    contentType: str


class GetImageResponse(BaseModel):
    image_hash: str
    rgb: RGB | None
    paintId: str | None
    image_data: Image | None = None
    
class GetProcessedResponse(BaseModel):
    uid: str
    processed_image_hash: str
    color: ColorDTO


class GetJSONResponse(BaseModel):
    image_hash: str
    json_data: dict | None = None

    
class ImageData(BaseModel):
    uid: str
    colors: list[ColorDTO]
    raw_image_hash: str