from functools import lru_cache
from .config import Settings


@lru_cache()
def getEnv():
    return Settings()
