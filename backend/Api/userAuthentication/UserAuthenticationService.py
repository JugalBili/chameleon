from fastapi import Depends
from .UserAuthenticationRepository import UserAuthenticationRepository
from typing import Annotated
from .CreateUserDto import CreateUserDTO
class UserAuthenticationService(object):
    def __init__(self,
                 repository: Annotated[UserAuthenticationRepository, Depends(UserAuthenticationRepository)]) -> None:
        self.repository = repository

    def test(self):
        return self.repository.test()
    
    def createUser(self, createUserDto: CreateUserDTO):
        return self.repository.createUser(createUserDto)
