from typing import Annotated

from fastapi import APIRouter, Depends, Header

from ..userAuthentication.CreateUserDto import CreateUserDTO
from ..userAuthentication.UserLoginDto import UserLoginDto
from ..userAuthentication.UserAuthenticationService import UserAuthenticationService
from ..dependencies import get_auth_token

router = APIRouter(
    # specify subroute. All routes in this file will be in the form of /login/{whatever}
    prefix="/user",
    # tags are stricly for metadata (helps with openAPI specifications)
    tags=["login"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)


@router.get("/login")
async def get_user(auth_token: Annotated[str, Depends(get_auth_token)],
                   AuthenticationService: Annotated[UserAuthenticationService, Depends(UserAuthenticationService)]):
    return await AuthenticationService.authenticateUser(auth_token)


@router.post("/get-token")
async def login_user(loginDto: UserLoginDto,
                     AuthenticationService: Annotated[UserAuthenticationService, Depends(UserAuthenticationService)]):
    return await AuthenticationService.getUserToken(loginDto)


@router.post("/register")
async def login_user(newUser: CreateUserDTO,
                     AuthenticationService: Annotated[UserAuthenticationService, Depends(UserAuthenticationService)]):
    return await AuthenticationService.createUser(newUser)
