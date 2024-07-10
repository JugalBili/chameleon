from typing import Annotated

from fastapi import APIRouter, Depends

from Api.data_classes import CreateUserDTO, UserLoginDto
from Api.service.user_authentication_service import UserAuthenticationService
from Api.dependencies import get_user, get_authentication_service
from Api.repository.user_authentication_repository import User

router = APIRouter(
    # specify sub-route. All routes in this file will be in the form of /login/{whatever}
    prefix="/user",
    # tags are strictly for metadata (helps with openAPI specifications)
    tags=["login"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)


@router.get("/login-token")
async def get_user(user: Annotated['User', Depends(get_user)]):
    return user


@router.post("/login")
async def login_user(loginDto: UserLoginDto,
                     AuthenticationService: Annotated[UserAuthenticationService, Depends(get_authentication_service)]):
    token = await AuthenticationService.get_user_token(loginDto)
    # TODO: Make better later
    user = await AuthenticationService.authenticate_user(f'Bearer {token.token}')
    return {
        "user": user,
        "token": token
    }


@router.post("/register")
async def login_user(newUser: CreateUserDTO,
                     AuthenticationService: Annotated[UserAuthenticationService, Depends(get_authentication_service)]):
    return await AuthenticationService.create_user(newUser)
