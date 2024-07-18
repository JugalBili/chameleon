from Api.repository.favorites_repository import FavoritesRepository
from Api.repository.user_authentication_repository import User
from shared.data_classes import ColorDTO
class FavoritesService():
    def __init__(self, repository: FavoritesRepository) -> None:
        self.repository = repository

    async def get_favorites(self, user: User):
        return await self.repository.get_favorites(user)
    
    async def add_to_favorites(self, user: User, color: ColorDTO ):
        return await self.repository.add_to_favorites(user, color)
    
    async def remove_from_favorites(self, user:User, color_id: str):
        return await self.repository.remove_from_favorites(user, color_id)