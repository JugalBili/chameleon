from functools import lru_cache
from Api.config import Settings
from shared.repository.image_repository import ImageRepository


@lru_cache()
def getEnv():
    return Settings()

def get_image_repository():
    return ImageRepository()