from pydantic import BaseModel
class RGB(BaseModel):
    r: int
    g: int
    b: int