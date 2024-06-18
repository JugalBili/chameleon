import os
from typing import Annotated

from fastapi import APIRouter, Depends

from ..userAuthentication.CreateUserDto import CreateUserDTO
from ..userAuthentication.UserAuthenticationService import UserAuthenticationService

router = APIRouter(
    # specify subroute. All routes in this file will be in the form of /login/{whatever}
    prefix="/user",
    # tags are stricly for metadata (helps with openAPI specifications)
    tags=["login"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)


@router.get("/test")
def test():
    return os.getenv("FIREBASE_EMULATOR_HOST")


@router.post("/register")
async def login_user(newUser: CreateUserDTO,
                     AuthenticationService: Annotated[UserAuthenticationService, Depends(UserAuthenticationService)]):
    return await AuthenticationService.createUser(newUser)
