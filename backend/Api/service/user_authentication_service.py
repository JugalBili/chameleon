from fastapi import HTTPException
from Api.repository.user_authentication_repository import UserAuthenticationRepository
from Api.data_classes import CreateUserDto, UserLoginDto
from pydantic import BaseModel
from email_validator import validate_email, EmailNotValidError


class AuthToken(BaseModel):
    token: str
    token_type: str


class UserAuthenticationService(object):
    def __init__(self,
                 repository: UserAuthenticationRepository) -> None:
        self.repository = repository

    # realistically, this method should be a middleware,
    # but I can't figure out how to do that properly with this library
    async def authenticate_user(self, token: str):
        jwt_token = token.split(' ')[1]
        return await self.repository.authenticate_access_token_async(jwt_token)

    async def get_user_token(self, loginDto: UserLoginDto):
        user = await self.repository.get_user_from_login(loginDto)
        if user is None:
            raise HTTPException(status_code=401, detail="Invalid Username and/or password")
        return AuthToken(token=self.repository.create_access_token(user), token_type="Bearer")

    async def create_user(self, createUserDto: CreateUserDto):
        user = await self.repository.get_user_from_email(createUserDto.email)
        if user is not None:
            raise HTTPException(status_code=409, detail="User exists")
        try:
            validate_email(createUserDto.email, check_deliverability=False)
        except EmailNotValidError as e:
            raise HTTPException(status_code=409, detail=str(e))
        user = await self.repository.create_user_async(createUserDto)
        return AuthToken(token=self.repository.create_access_token(user), token_type="Bearer")
