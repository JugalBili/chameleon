from fastapi import Depends, FastApi
from .routes import login

app = FastApi()
app.include_router(login.router)