from fastapi import APIRouter, Depends
from ..userAuthentication.UserAuthenticationService import UserAuthenticationService
from typing import Annotated
from ..userAuthentication.CreateUserDto import CreateUserDTO
import os

router = APIRouter(
    # specify subroute. All routes in this file will be in the form of /login/{whatever}
    prefix="/user",
    # tags are stricly for metadata (helps with openAPI specifications)
    tags=["login"],
    #also metadata
    responses={401: {"description": "Incorrect Login information"}},
)


@router.get("/test")
def test():
    return os.getenv("FIREBASE_EMULATOR_HOST")


@router.get("/register")
def login_user(AuthenticationService: Annotated[UserAuthenticationService, Depends(UserAuthenticationService)]):
    newUser = CreateUserDTO(email="test@test.com", password="password")
    return AuthenticationService.createUser(newUser)

