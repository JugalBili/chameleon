import firebase_admin
import os
from firebase_admin import credentials
from fastapi import FastAPI
from contextlib import asynccontextmanager

from routes import image_processing
from dependencies import getEnv
from image_pipeline.dino_sam_singleton import DinoSAMSingleton


@asynccontextmanager
async def lifespan(app: FastAPI):
    env = getEnv()
    cred = credentials.Certificate('./firebase-auth-image-server.json')

    firebase_admin.initialize_app(cred, {
        'storageBucket': env.firebase_storage_bucket_url
    })
    yield
    print("good bye")


# Initialize Pipeline Models
ds_instance = DinoSAMSingleton.instance()

# initialize fastAPI
app = FastAPI(lifespan=lifespan)
app.include_router(image_processing.router)
