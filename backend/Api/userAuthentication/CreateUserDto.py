from pydantic import BaseModel


class CreateUserDTO(BaseModel):
    email: str
    password: str
    firstname: str
    lastname: str
