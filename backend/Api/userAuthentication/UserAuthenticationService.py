from fastapi import Depends
from .UserAuthenticationRepository import UserAuthenticationRepository
from typing import Annotated
from .CreateUserDto import CreateUserDTO


class UserAuthenticationService(object):
    def __init__(self,
                 repository: Annotated[UserAuthenticationRepository, Depends(UserAuthenticationRepository)]) -> None:
        self.repository = repository

    async def createUser(self, createUserDto: CreateUserDTO):
        return await self.repository.createUserAsync(createUserDto)
