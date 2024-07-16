from fastapi import APIRouter, Depends
from shared.data_classes import ColorDTO
from Api.service.history_service import HistoryService
from typing import Annotated
from Api.dependencies import get_history_service, get_user
from Api.repository.user_authentication_repository import User
from shared.data_classes import ColorDTO, RGB

router = APIRouter(
    # specify sub-route. All routes in this file will be in the form of /login/{whatever}
    prefix="/history",
    # tags are strictly for metadata (helps with openAPI specifications)
    tags=["history"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)

@router.post("/")
async def test(
    history_service: Annotated["HistoryService", Depends(get_history_service)],
    user: Annotated['User', Depends(get_user)]
):
    colors = []
    colors.append(ColorDTO(paint_id="something", color= RGB(r=0, g=0, b=0)))
    colors.append(ColorDTO(paint_id="something_else", color= RGB(r=1, g=2, b=3)))
    return await history_service.update_history(user, "aaa", colors)
@router.get("/")
async def get_user_history(
    history_service: Annotated["HistoryService", Depends(get_history_service)],
    user: Annotated['User', Depends(get_user)]
):
    return await history_service.get_history(user)
