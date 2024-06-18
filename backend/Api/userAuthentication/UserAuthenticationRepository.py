from fastapi import HTTPException, Depends
from firebase_admin import auth
from firebase_admin import firestore_async
from firebase_admin.auth import UserRecord
from .CreateUserDto import CreateUserDTO
from ..dependencies import getEnv
from ..config import Settings
from typing import Annotated
from pydantic import BaseModel
from datetime import datetime, timedelta, timezone
import jwt


class User:
    def __init__(self, email: str, firstname: str, lastname: str, uid: str):
        self.email: str = email
        self.firstname: str = firstname
        self.lastname: str = lastname
        self.uid: str = uid

    def from_dict(self, data):
        self.email = data.get("email", "")
        self.firstname = data.get("firstname", "")
        self.lastname = data.get("lastname", "")
        self.uid = data.get("uid", "")

    def to_dict(self):
        return {
            "email": self.email,
            "firstname": self.firstname,
            "lastname": self.lastname,
            "uid": self.uid
        }


class AuthToken(BaseModel):
    token: str
    token_type: str


class UserAuthenticationRepository:
    def __init__(self, env: Annotated[Settings, Depends(getEnv)]) -> None:
        self.db = firestore_async.client()
        self.collectionRef = self.db.collection('users')
        self.signingKey = env.jwt_signing_key
        self.signingAlgorithm = "HS256"

    def __createAccessToken(self, data: dict) -> str:
        to_encode = data.copy()
        expiration_date = datetime.now(timezone.utc) + timedelta(days=10)
        to_encode.update({"exp": expiration_date})
        token = jwt.encode(to_encode, self.signingKey, self.signingAlgorithm)
        return token

    async def createUserAsync(self, createUserDTO: CreateUserDTO) -> AuthToken:
        try:
            user = auth.create_user(
                email=createUserDTO.email,
                password=createUserDTO.password
            )
        except Exception as e:
            raise HTTPException(status_code=500, detail=str(e))
        uid = user._data.get('localId')
        newUser = User(createUserDTO.email, createUserDTO.firstname, createUserDTO.lastname, uid)
        try:
            await self.collectionRef.document(uid).set(newUser.to_dict())
        except Exception as e:
            auth.delete_user(uid)
            raise HTTPException(status_code=500, detail=str(e))
        jtw_token = self.__createAccessToken({"usr": newUser.email})
        return AuthToken(token=jtw_token, token_type="bearer")
