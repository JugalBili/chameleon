from pydantic import BaseModel
from .RGB import RGB
from .imageData import Image
class GetImageResponse(BaseModel):
    image_hash: str
    rgb: RGB | None
    paintId: str | None
    image_data: Image | None = None