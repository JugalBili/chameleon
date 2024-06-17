from firebase_admin import firestore_async
from firebase_admin import auth
from firebase_admin.auth import UserRecord
from .CreateUserDto import CreateUserDTO
from fastapi import HTTPException


class UserAuthenticationRepository:
    def __init__(self) -> None:
        self.db = firestore_async.client()

    def test(self) -> str:
        return "Hello World"

    def createUser(self, createUserDTO: CreateUserDTO) -> UserRecord:
        try:
            user = auth.create_user(
                email=createUserDTO.email,
                password=createUserDTO.password
            )
        except Exception as e:
            raise HTTPException(status_code=500, detail=str(e))
        return user
