from fastapi import APIRouter, Depends, UploadFile, Form, File, HTTPException, Response
from ..image.RGB import ImageUploadDTO
from pydantic import ValidationError
from ..image.imageService import ImageService
from typing import Annotated, List
from ..dependencies import get_image_service, get_user
from ..userAuthentication.UserAuthenticationRepository import User
from ..image.getImageResponse import GetImageResponse
router = APIRouter(
    # specify subroute. All routes in this file will be in the form of /login/{whatever}
    prefix="/image",
    # tags are stricly for metadata (helps with openAPI specifications)
    tags=["image"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)

@router.get("/{image_hash}")
def get_image_by_hash(image_service: Annotated['ImageService',Depends(get_image_service)],
                    user: Annotated['User', Depends(get_user)], 
                    image_hash: str
                    ):
    image = image_service.get_image_by_hash(user, image_hash)
    return Response(content=image.image_bytes, media_type=image.contentType)
    
@router.get("/list/{image_hash}")
def get_image_by_hash(image_service: Annotated['ImageService',Depends(get_image_service)],
                    # user: Annotated['User', Depends(get_user)], 
                    image_hash: str
                    ):
    user = User(email="test", firstname="Test", lastname="Test", uid="Test")
    
    return image_service.get_image_summary_by_hash(user, image_hash)

@router.post("/")
async def upload_file(image_service: Annotated['ImageService',Depends(get_image_service)],
                    user: Annotated['User', Depends(get_user)], 
                    file: UploadFile = File(...), colors: str = Form(...)):
    try:
        color_list = [ImageUploadDTO(**color) for color in eval(colors)]
    except (SyntaxError, ValidationError, TypeError) as e:
        raise HTTPException(status_code=422, detail=f"Invalid 'colors' input: {e}")
    
    return await image_service.upload_and_process_image(user, file, color_list)