from functools import lru_cache
from .config import Settings
from fastapi import Request


@lru_cache()
def getEnv():
    return Settings()


def get_auth_token(request: Request):
    token = request.headers.get('Authorization')
    return token
