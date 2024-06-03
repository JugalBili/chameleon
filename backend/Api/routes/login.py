from fastapi import APIRouter

router = APIRouter(
    prefix="/login",
    tags=["login"],
    responses={401: {"description": "Incorrect Login information"}},
)

@router.post("/")
async def login_user():
    return "Hello World"
