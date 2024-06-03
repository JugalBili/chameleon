from fastapi import APIRouter

router = APIRouter(
    # specify subroute. All routes in this file will be in the form of /login/{whatever}
    prefix="/login",
    # tags are stricly for metadata (helps with openAPI specifications)
    tags=["login"],
    responses={401: {"description": "Incorrect Login information"}},
)

@router.post("/")
async def login_user():
    return "Hello World"
