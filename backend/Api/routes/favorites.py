from fastapi import APIRouter, Depends, Response
from Api.service.favorites_service import FavoritesService
from typing import Annotated
from Api.dependencies import get_favorites_service, get_user
from Api.repository.user_authentication_repository import User
from shared.data_classes import ColorDTO
router = APIRouter(
    # specify sub-route. All routes in this file will be in the form of /login/{whatever}
    prefix="/favorite",
    # tags are strictly for metadata (helps with openAPI specifications)
    tags=["favorites"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)

@router.get("/")
async def get_user_favorites(
    favorites_service: Annotated["FavoritesService", Depends(get_favorites_service)],
    user: Annotated['User', Depends(get_user)]
):
    return await favorites_service.get_favorites(user)

@router.post("/")
async def add_to_favorites(favorites_service: Annotated["FavoritesService", Depends(get_favorites_service)],
    user: Annotated['User', Depends(get_user)],
    color: ColorDTO):
    await favorites_service.add_to_favorites(user, color)
    # returns 204: no-content
    return Response(status_code=204)


@router.delete("/{paint_id}")
async def remove_from_history(
    favorites_service: Annotated["FavoritesService", Depends(get_favorites_service)],
    user: Annotated['User', Depends(get_user)],
    paint_id: str
):
    await favorites_service.remove_from_favorites(user, paint_id)
    # returns 204: no-content
    return Response(status_code=204)
