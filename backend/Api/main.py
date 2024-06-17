import firebase_admin
from firebase_admin import credentials


cred = credentials.Certificate('./firebase-auth.json')
firebase_admin.initialize_app(credential=cred)

from fastapi import FastAPI
from .routes import login
# initialize fastAPI
app = FastAPI()
app.include_router(login.router)