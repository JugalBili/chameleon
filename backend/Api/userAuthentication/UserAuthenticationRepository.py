from fastapi import HTTPException, Depends
from firebase_admin import firestore_async
from google.cloud.firestore_v1 import DocumentSnapshot

from .CreateUserDto import CreateUserDTO
from .UserLoginDto import UserLoginDto
from ..dependencies import getEnv
from ..config import Settings
from typing import Annotated, Union
from pydantic import BaseModel
from datetime import datetime, timedelta, timezone
import jwt
from passlib.context import CryptContext


class User:
    def __init__(self, email: str, firstname: str, lastname: str, uid: str):
        self.email: str = email
        self.firstname: str = firstname
        self.lastname: str = lastname
        self.uid: str = uid

    @classmethod
    def from_firestore_document(cls, doc: DocumentSnapshot) -> 'User':
        data = doc.to_dict()
        return cls(
            email=data.get("email", ""),
            firstname=data.get("firstname", ""),
            lastname=data.get("lastname", ""),
            uid=doc.id
        )

    def to_dict(self):
        return {
            "email": self.email,
            "firstname": self.firstname,
            "lastname": self.lastname,
            "uid": self.uid
        }


class UserAuthenticationRepository:
    def __init__(self, env: Annotated[Settings, Depends(getEnv)]) -> None:
        self.collectionRef = firestore_async.client().collection('users')
        self.signingKey = env.jwt_signing_key
        self.signingAlgorithm = "HS256"
        self.pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

    def __verify_password(self, plaintext, hashed_password):
        return self.pwd_context.verify(plaintext, hashed_password)

    def __hash_password(self, plaintext):
        return self.pwd_context.hash(plaintext)

    def createAccessToken(self, user: User) -> str:
        data: dict = {"usr": user.email}
        to_encode = data.copy()
        expiration_date = datetime.now(timezone.utc) + timedelta(days=10)
        to_encode.update({"exp": expiration_date})
        token = jwt.encode(to_encode, self.signingKey, self.signingAlgorithm)
        return token

    async def getUserFromLogin(self, loginDto: UserLoginDto) -> Union[User, None]:
        try:
            query = self.collectionRef.where("email", "==", loginDto.email)
            users = [user async for user in query.stream()]
            if len(users) == 0:
                return None
            user_document = users[0]
            if self.__verify_password(loginDto.password, user_document.get("password")):
                return User.from_firestore_document(user_document)
            else:
                return None
        except Exception as e:
            raise HTTPException(status_code=500, detail=str(e))

    async def getUserFromEmail(self, email: str):
        try:
            query = self.collectionRef.where("email", "==", email)
            users = [user async for user in query.stream()]
            if len(users) == 0:
                return None
            user_document = users[0]
            return User.from_firestore_document(user_document)
        except Exception as e:
            raise HTTPException(status_code=500, detail=str(e))

    def __create_user_snapshot(self, createuserDto: CreateUserDTO):
        return {
            "email": createuserDto.email,
            "firstname": createuserDto.firstname,
            "lastname": createuserDto.lastname,
            "password": self.__hash_password(createuserDto.password)
        }

    async def createUserAsync(self, createUserDTO: CreateUserDTO) -> User:
        new_user_document_dict = self.__create_user_snapshot(createUserDTO)
        try:
            new_user_document = await self.collectionRef.add(new_user_document_dict)
            uid = new_user_document[1].id  # this gets the document ID that was just created
        except Exception as e:
            raise HTTPException(status_code=500, detail=str(e))
        newUser = User(createUserDTO.email, createUserDTO.firstname, createUserDTO.lastname, uid)
        return newUser
