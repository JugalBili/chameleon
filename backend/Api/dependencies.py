from functools import lru_cache
from Api.config import Settings
from fastapi import Request, HTTPException, Depends
from typing import Annotated
from Api.repository.user_authentication_repository import UserAuthenticationRepository
from Api.service.user_authentication_service import UserAuthenticationService
from shared.repository.imageRepository import ImageRepository
from Api.service.image_service import ImageService


@lru_cache()
def getEnv():
    return Settings()


def get_authentication_repository(env: Annotated[Settings, Depends(getEnv)]):
    return UserAuthenticationRepository(env)


def get_authentication_service(
        repository: Annotated['UserAuthenticationRepository', Depends(get_authentication_repository)]):
    return UserAuthenticationService(repository)


def get_image_repository():
    return ImageRepository()


def get_image_service(repository: Annotated['ImageRepository', Depends(get_image_repository)]):
    return ImageService(repository)


def get_auth_token(request: Request):
    token = request.headers.get('Authorization')
    if token is None:
        raise HTTPException(status_code=401)
    return token


async def get_user(token: Annotated[str, Depends(get_auth_token)],
                   authentication_service: Annotated['UserAuthenticationService', Depends(get_authentication_service)]):
    return await authentication_service.authenticate_user(token)
