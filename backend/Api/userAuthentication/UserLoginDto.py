from pydantic import BaseModel


class UserLoginDto(BaseModel):
    email: str
    password: str
