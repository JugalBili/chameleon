from fastapi import APIRouter, Depends, UploadFile, Form, File, HTTPException
from ..image.RGB import ImageUploadDTO
from pydantic import ValidationError
from ..image.imageService import ImageService
from typing import Annotated
from ..dependencies import get_image_service, get_user
from ..userAuthentication.UserAuthenticationRepository import User
router = APIRouter(
    # specify subroute. All routes in this file will be in the form of /login/{whatever}
    prefix="/image",
    # tags are stricly for metadata (helps with openAPI specifications)
    tags=["image"],
    # also metadata
    responses={401: {"description": "Incorrect Login information"}},
)

@router.post("/",
             responses={
                 200: {
                     "content": {"image/jpeg": {}}
                 }
             })
async def upload_file(repository: Annotated['ImageService',Depends(get_image_service)],
                    #   user: Annotated['User', Depends(get_user)], 
                      file: UploadFile = File(...), colors: str = Form(...)):
    try:
        user = User("test@test.com", "Test", "Test", "Test")
        color_list = [ImageUploadDTO(**color) for color in eval(colors)]
    except (SyntaxError, ValidationError, TypeError) as e:
        raise HTTPException(status_code=422, detail=f"Invalid 'colors' input: {e}")
    
    return await repository.upload_and_process_image(user, file, color_list)