import hashlib
import io
from firebase_admin import storage
from google.cloud.storage import Blob
from fastapi import UploadFile, HTTPException
from Api.data_classes import RGB, GetImageResponse, ImageUploadDTO, Image


class ImageRepository:
    def __init__(self) -> None:
        self.bucket = storage.bucket()
        self.processed_image_path = "processed"
        self.metadata_exception = HTTPException(status_code=500, detail="Error encountered: invalid metadata")

    async def upload_unprocessed_image(self, uid: str, file: UploadFile):
        await file.seek(0)
        content = await file.read()
        image_hash = hashlib.sha256(content).hexdigest()
        base_path = f"{uid}/{image_hash}"
        image_path = f"{base_path}/{image_hash}"
        blob = self.bucket.blob(image_path)

        # TODO Update History here (Add New entry or update existing one)
        if blob.exists():
            return image_hash
        content_file = io.BytesIO(content)
        blob.upload_from_file(content_file, content_type=file.content_type)
        return image_hash

    @staticmethod
    def _create_metadata(upload_request: ImageUploadDTO):
        return {
            "r": upload_request.color.r,
            "g": upload_request.color.g,
            "b": upload_request.color.b,
            "paintId": upload_request.paintId
        }

    @staticmethod
    def _parse_metadata_from_blob(blob: Blob):
        blob.reload()
        metadata = blob.metadata
        if metadata is None:
            return None, None, None, None
        r = metadata.get("r", None)
        g = metadata.get("g", None)
        b = metadata.get("b", None)
        paintId = metadata.get("paintId", None)
        return r, g, b, paintId

    # temp function for testing while image pipeline is getting developed.
    # Replace calls to this function with Image Pipeline Service after completion
    async def upload_processed_image(self, uid: str, file: UploadFile, dto: ImageUploadDTO):
        await file.seek(0)
        content = await file.read()
        image_hash = hashlib.sha256(content).hexdigest()
        base_path = f"{uid}/{image_hash}/{self.processed_image_path}"
        file_name = f"{image_hash}-{dto.paintId}"
        image_path = f"{base_path}/{file_name}"
        blob = self.bucket.blob(image_path)
        if blob.exists():
            r, g, b, paintId = self._parse_metadata_from_blob(blob)
            if not r or not g or not b or not paintId:
                raise self.metadata_exception
            return GetImageResponse(image_hash=file_name, rgb=RGB(r=r, g=g, b=b), paintId=paintId)
        content_file = io.BytesIO(content)
        blob.metadata = self._create_metadata(dto)
        blob.upload_from_file(content_file, content_type=file.content_type)
        return GetImageResponse(image_hash=file_name, rgb=dto.color, paintId=dto.paintId)

    @staticmethod
    def _get_image_from_blob(blob: Blob) -> Image:
        image_bytes = blob.download_as_bytes()
        # image_bytes = io.BytesIO()
        # blob.download_as_string(image_bytes)
        # image_bytes.seek(0)
        return Image(image_bytes=image_bytes, contentType=blob.content_type)

    def get_raw_image_by_hash(self, uid: str, image_hash: str,
                              download_image: bool = False) -> None | GetImageResponse:
        base_path = f"{uid}/{image_hash}/"
        file_name = f"{image_hash}"
        base_path += file_name
        blob = self.bucket.blob(base_path)
        if not blob.exists():
            return None
        if download_image:
            raw_image = self._get_image_from_blob(blob)
        else:
            raw_image = None
        return GetImageResponse(image_hash=image_hash, rgb=None, paintId=None, image_data=raw_image)

    def get_processed_image_by_hash(self, uid: str, image_hash: str,
                                    download_image: bool = False) -> None | GetImageResponse:
        raw_hash = image_hash.split("-")[0]
        path = f"{uid}/{raw_hash}/{self.processed_image_path}/{image_hash}"
        blob = self.bucket.blob(path)
        if not blob.exists():
            return None
        # If Image exists with no metadata, what do we do? (probably wouldn't even)
        # Get data we can?
        # Cancel the request?
        # Dont return images without metadata?
        r, g, b, paintId = self._parse_metadata_from_blob(blob)
        if not r or not g or not b or not paintId:
            raise self.metadata_exception
        if download_image:
            raw_image = self._get_image_from_blob(blob)
        else:
            raw_image = None
        return GetImageResponse(image_hash=image_hash, rgb=RGB(r=r, g=g, b=b), paintId=paintId, image_data=raw_image)

    def check_image_exists_for_id(self, uid: str, image_hash: str, dto: ImageUploadDTO):
        file_name = f"{image_hash}-{dto.paintId}"
        return self.get_processed_image_by_hash(uid, file_name)

    def get_all_processed_images(self, uid: str, raw_hash: str):
        path = f"{uid}/{raw_hash}/{self.processed_image_path}"
        blobs = self.bucket.list_blobs(prefix=path)
        if not blobs:
            return []
        ret = []
        for blob in blobs:
            filename = blob.name.split("/")[-1]
            r, g, b, paintId = self._parse_metadata_from_blob(blob)
            ret.append(GetImageResponse(image_hash=filename, rgb=RGB(r=r, g=g, b=b), paintId=paintId))
        return ret
