import hashlib
import io
from firebase_admin import storage
from fastapi import UploadFile
from ..userAuthentication.UserAuthenticationRepository import User
from .RGB import RGB
class ImageRepository:
    def __init__(self) -> None:
        self.bucket = storage.bucket()
        self.processed_image_path = "processed"
    
    async def upload_unprocessed_image(self, user: User, file: UploadFile):
        await file.seek(0)
        content = await file.read()
        image_hash = hashlib.sha256(content).hexdigest()
        base_path = f"{user.uid}/{image_hash}"
        image_path = f"{base_path}/{image_hash}"
        blob = self.bucket.blob(image_path)

        # TODO Update History here (Add New entry or update existing one)
        if blob.exists():
            return blob.generate_signed_url(version="v4", expiration=100), image_hash
        content_file = io.BytesIO(content)
        blob.upload_from_file(content_file, content_type=file.content_type)
        return blob.generate_signed_url(version="v4", expiration=100), image_hash
    
    # temp function for testing while image pipeline is getting developed. 
    # Replace calls to this function with Image Pipeline Service after completion
    async def upload_processed_image(self, user: User, file: UploadFile, color: RGB):
        await file.seek(0)
        content = await file.read()
        image_hash = hashlib.sha256(content).hexdigest()
        base_path = f"{user.uid}/{image_hash}/{self.processed_image_path}"
        image_path = f"{base_path}/{image_hash}-{color.r}-{color.g}-{color.b}"
        blob = self.bucket.blob(image_path)
        if blob.exists():
            return blob.generate_signed_url(version="v4", expiration=100)
        content_file = io.BytesIO(content)
        blob.upload_from_file(content_file, content_type=file.content_type)
        return blob.generate_signed_url(version="v4", expiration=100)
    
    def get_image_url(self, user: User, image_hash: str, color: RGB):
        base_path = f"{user.uid}/{image_hash}/{self.processed_image_path}/"
        file_name = f"{image_hash}-{color.r}-{color.g}-{color.b}"
        base_path += file_name
        blob = self.bucket.blob(base_path)
        return blob.generate_signed_url(version="v4", expiration=100) if blob.exists() else None

    def get_image_url(self, user: User, image_hash: str):
        base_path = f"{user.uid}/{image_hash}/"
        file_name = f"{image_hash}"
        base_path += file_name
        blob = self.bucket.blob(base_path)
        return blob.generate_signed_url(version="v4", expiration=100) if blob.exists() else None
