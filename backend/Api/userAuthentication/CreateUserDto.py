from .UserLoginDto import UserLoginDto


class CreateUserDTO(UserLoginDto):
    firstname: str
    lastname: str
