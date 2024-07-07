from fastapi import HTTPException
from .UserAuthenticationRepository import UserAuthenticationRepository
from .CreateUserDto import CreateUserDTO
from .UserLoginDto import UserLoginDto
from pydantic import BaseModel
from email_validator import validate_email, EmailNotValidError


class AuthToken(BaseModel):
    token: str
    token_type: str


class UserAuthenticationService(object):
    def __init__(self,
                 repository: UserAuthenticationRepository) -> None:
        self.repository = repository

    # realistically, this method should be a middleware but I can't figure out how to do that properly with this library
    async def authenticateUser(self, token: str):
        jwt_token = token.split(' ')[1]
        return await self.repository.authenticateAccessTokenAsync(jwt_token)

    async def getUserToken(self, loginDto: UserLoginDto):
        user = await self.repository.getUserFromLogin(loginDto)
        if user is None:
            raise HTTPException(status_code=401, detail="Invalid Username and/or password")
        return AuthToken(token=self.repository.createAccessToken(user), token_type="Bearer")

    async def createUser(self, createUserDto: CreateUserDTO):
        user = await self.repository.getUserFromEmail(createUserDto.email)
        if user is not None:
            raise HTTPException(status_code=409, detail="User exists")
        try:
            validate_email(createUserDto.email)
        except EmailNotValidError as e:
            raise HTTPException(status_code=409, detail=str(e))
        user = await self.repository.createUserAsync(createUserDto)
        return AuthToken(token=self.repository.createAccessToken(user), token_type="Bearer")
