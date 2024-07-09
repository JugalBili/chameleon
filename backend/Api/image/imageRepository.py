import hashlib
import io
from firebase_admin import storage
from fastapi import UploadFile, HTTPException
from ..userAuthentication.UserAuthenticationRepository import User
from .getImageResponse import GetImageResponse
from .RGB import RGB, ImageUploadDTO

class ImageRepository:
    def __init__(self) -> None:
        self.bucket = storage.bucket()
        self.processed_image_path = "processed"
        self.metadata_exception = HTTPException(status_code=500, detail="Error encountered: invalid metadata")
        
    async def upload_unprocessed_image(self, user: User, file: UploadFile):
        await file.seek(0)
        content = await file.read()
        image_hash = hashlib.sha256(content).hexdigest()
        base_path = f"{user.uid}/{image_hash}"
        image_path = f"{base_path}/{image_hash}"
        blob = self.bucket.blob(image_path)

        # TODO Update History here (Add New entry or update existing one)
        if blob.exists():
            return image_hash
        content_file = io.BytesIO(content)
        blob.upload_from_file(content_file, content_type=file.content_type)
        return image_hash
    
    def _create_metadata(self, upload_request: ImageUploadDTO):
        return {
            "r": upload_request.color.r,
            "g": upload_request.color.g,
            "b": upload_request.color.b,
            "paintId": upload_request.paintId
        }
    
    def _parse_metadata_from_blob(self, blob):
        blob.reload()
        metadata = blob.metadata
        print(metadata)
        if metadata is None:
            return None, None, None, None
        r = metadata.get("r", None)
        g = metadata.get("g", None)
        b = metadata.get("b", None)
        paintId = metadata.get("paintId", None)
        return r,g,b,paintId
    # temp function for testing while image pipeline is getting developed. 
    # Replace calls to this function with Image Pipeline Service after completion
    async def upload_processed_image(self, user: User, file: UploadFile, dto: ImageUploadDTO):
        await file.seek(0)
        content = await file.read()
        image_hash = hashlib.sha256(content).hexdigest()
        base_path = f"{user.uid}/{image_hash}/{self.processed_image_path}"
        file_name = f"{image_hash}-{dto.paintId}"
        image_path = f"{base_path}/{file_name}"
        blob = self.bucket.blob(image_path)
        if blob.exists():
            r,g,b,paintId = self._parse_metadata_from_blob(blob)
            if not r or not g or not b or not paintId:
                raise self.metadata_exception
            return GetImageResponse(image_hash=file_name, rgb=RGB(r=r,g=g,b=b), paintId=paintId)
        content_file = io.BytesIO(content)
        blob.metadata = self._create_metadata(dto)
        blob.upload_from_file(content_file, content_type=file.content_type)
        return GetImageResponse(image_hash=file_name, rgb=dto.color, paintId=dto.paintId)
    
    def get_raw_image_by_hash(self, user: User, image_hash: str) -> None | GetImageResponse:
        base_path = f"{user.uid}/{image_hash}/"
        file_name = f"{image_hash}"
        base_path += file_name
        blob = self.bucket.blob(base_path)
        return GetImageResponse(image_hash=image_hash, rgb=None, paintId=None) if blob.exists() else None
    
    def get_processed_image_by_hash(self, user: User,image_hash: str) -> None | GetImageResponse:
        raw_hash = image_hash.split("-")[0]
        path = f"{user.uid}/{raw_hash}/{self.processed_image_path}/{image_hash}"
        blob = self.bucket.blob(path)
        if not blob.exists():
            return None
        # If Image exists with no metadata, what do we do? (probably wouldnt even)
        # Get data we can?
        # Cancel the request?
        # Dont return images without metadata? 
        r,g,b,paintId = self._parse_metadata_from_blob(blob)
        if not r or not g or not b or not paintId:
            raise self.metadata_exception
        return GetImageResponse(image_hash=image_hash, rgb=RGB(r=r,g=g,b=b), paintId=paintId)

    def check_image_exists_for_id(self, user: User, image_hash: str, dto: ImageUploadDTO):
        file_name = f"{image_hash}-{dto.paintId}"
        return self.get_processed_image_by_hash(user, file_name)
