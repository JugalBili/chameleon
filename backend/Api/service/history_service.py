from Api.repository.history_repository import HistoryRepository
from Api.repository.user_authentication_repository import User
from shared.data_classes import ColorDTO


class HistoryService:
    def __init__(self, repository: HistoryRepository) -> None:
        self.repository = repository

    async def update_history(
        self, user: User, image_hash: str, colors: ColorDTO
    ) -> None:
        return await self.repository.updateHistory(user, image_hash)
