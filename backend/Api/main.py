import firebase_admin
import sys
import os
from firebase_admin import credentials
from fastapi import FastAPI
from contextlib import asynccontextmanager

sys.path.append(os.path.join("\\".join(os.path.dirname(__file__).split("\\")[:-1])))
from Api.routes import login, image, history
from Api.dependencies import getEnv


@asynccontextmanager
async def lifespan(app: FastAPI):
    env = getEnv()
    use_emulator = env.use_firebase_emulator
    cred = credentials.Certificate("./firebase-auth.json")
    if use_emulator.lower() == "true":
        os.environ["FIRESTORE_EMULATOR_HOST"] = "localhost:8080"
        print("using emulators")
    firebase_admin.initialize_app(
        cred, {"storageBucket": env.firebase_storage_bucket_url}
    )
    yield
    print("good bye")


# initialize fastAPI
app = FastAPI(lifespan=lifespan)
app.include_router(login.router)
app.include_router(image.router)
app.include_router(history.router)
