from typing import Annotated

from fastapi import APIRouter, Depends, Header

from ..userAuthentication.CreateUserDto import CreateUserDTO
from ..userAuthentication.UserLoginDto import UserLoginDto
from ..userAuthentication.UserAuthenticationService import UserAuthenticationService

router = APIRouter(
    # specify subroute. All routes in this file will be in the form of /login/{whatever}
    prefix="/user",
    # tags are stricly for metadata (helps with openAPI specifications)
    tags=["login"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)


@router.post("/login")
async def login_user(loginDto: UserLoginDto,
                     AuthenticationService: Annotated[UserAuthenticationService, Depends(UserAuthenticationService)]):
    return await AuthenticationService.getUserToken(loginDto)


@router.post("/register")
async def login_user(newUser: CreateUserDTO,
                     AuthenticationService: Annotated[UserAuthenticationService, Depends(UserAuthenticationService)]):
    return await AuthenticationService.createUser(newUser)
