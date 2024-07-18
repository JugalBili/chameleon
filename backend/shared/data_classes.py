from pydantic import BaseModel, ConfigDict
import io

class RGB(BaseModel):
    r: int
    g: int
    b: int


class ColorDTO(BaseModel):
    paint_id: str
    rgb: RGB


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

class GetMaskResponse(BaseModel):
    model_config = ConfigDict(arbitrary_types_allowed=True)
    
    image_hash: str
    mask_data: io.BytesIO | None = None

    
class ImageData(BaseModel):
    uid: str
    colors: list[ColorDTO]
    raw_image_hash: str