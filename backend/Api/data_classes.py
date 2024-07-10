from pydantic import BaseModel
from typing import List


class UserLoginDto(BaseModel):
    email: str
    password: str


class CreateUserDto(UserLoginDto):
    firstname: str
    lastname: str
