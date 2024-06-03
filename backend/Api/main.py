from fastapi import Depends, FastAPI
from .routes import login

app = FastAPI()
app.include_router(login.router)