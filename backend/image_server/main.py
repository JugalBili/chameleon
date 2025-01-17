import firebase_admin
import os
from firebase_admin import credentials
from fastapi import FastAPI
from contextlib import asynccontextmanager
import sys

sys.path.append(os.path.join(os.sep.join(os.path.dirname(__file__).split(os.sep)[:-1])))
sys.path.append(os.path.join(os.path.dirname(__file__)))
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
    # Initialize Pipeline Models
    ds_instance = DinoSAMSingleton.instance()
    yield
    print("good bye")

# initialize fastAPI
app = FastAPI(lifespan=lifespan)
app.include_router(image_processing.router)
