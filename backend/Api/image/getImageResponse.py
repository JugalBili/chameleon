from pydantic import BaseModel
from .RGB import RGB

class GetImageResponse(BaseModel):
    image_hash: str
    rgb: RGB | None
    paintId: str | None