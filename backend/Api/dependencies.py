from functools import lru_cache
from .config import Settings
from fastapi import Request, HTTPException


@lru_cache()
def getEnv():
    return Settings()


def get_auth_token(request: Request):
    token = request.headers.get('Authorization')
    if token is None:
        raise HTTPException(status_code=401)
    return token
