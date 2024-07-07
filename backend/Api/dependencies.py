from functools import lru_cache
from .config import Settings
from fastapi import Request, HTTPException,Depends
from typing import Annotated
from .userAuthentication.UserAuthenticationRepository import UserAuthenticationRepository
from .userAuthentication.UserAuthenticationService import UserAuthenticationService

@lru_cache()
def getEnv():
    return Settings()

def get_authentication_repository(env: Annotated[Settings, Depends(getEnv)]):
    return UserAuthenticationRepository(env)

def get_authentication_service(repository: Annotated['UserAuthenticationRepository', Depends(get_authentication_repository)]):
    return UserAuthenticationService(repository)

def get_auth_token(request: Request):
    token = request.headers.get('Authorization')
    if token is None:
        raise HTTPException(status_code=401)
    return token

async def get_user(token: Annotated[str, Depends(get_auth_token)], authentication_service: Annotated['UserAuthenticationService', Depends(get_authentication_service)]):
    return await authentication_service.authenticateUser(token)
