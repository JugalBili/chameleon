import hashlib
import io
import json
from firebase_admin import storage
from google.cloud.storage import Blob
from fastapi import UploadFile, HTTPException
from shared.data_classes import RGB, GetImageResponse, ColorDTO, Image, GetJSONResponse


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
    def _create_metadata(upload_request: ColorDTO):
        return {
            "r": upload_request.color.r,
            "g": upload_request.color.g,
            "b": upload_request.color.b,
            "paint_id": upload_request.paint_id
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

    async def upload_processed_image(self, uid: str, raw_image_hash: str, image_bytes, dto: ColorDTO):
        base_path = f"{uid}/{raw_image_hash}/{self.processed_image_path}"
        processed_image_hash = f"{raw_image_hash}-{dto.paintId}"
        image_path = f"{base_path}/{processed_image_hash}"
        blob = self.bucket.blob(image_path)
        if blob.exists():
            r, g, b, paintId = self._parse_metadata_from_blob(blob)
            if not r or not g or not b or not paintId:
                raise self.metadata_exception
            return GetImageResponse(image_hash=processed_image_hash, rgb=RGB(r=r, g=g, b=b), paintId=paintId)
        blob.metadata = self._create_metadata(dto)
        blob.upload_from_string(image_bytes, content_type="image/jpg")

        # return GetImageResponse(image_hash=file_name, rgb=dto.color, paintId=dto.paintId)
        return processed_image_hash

    async def upload_json(self, uid: str, raw_image_hash: str, json_dict: dict):
        base_path = f"{uid}/{raw_image_hash}"
        file_name = f"{raw_image_hash}.json"
        image_path = f"{base_path}/{file_name}"

        json_str = json.dumps(json_dict)
        blob = self.bucket.blob(image_path)

        blob.upload_from_string(json_str, content_type="application/json")
        return GetJSONResponse(image_hash=raw_image_hash)
    
    @staticmethod
    def _get_json_from_blob(self, blob: Blob) -> dict:
        json_string = blob.download_as_text()
        dict_obj = json.loads(json_string)

        return dict_obj
    
    def get_json_by_hash(self, uid: str, image_hash: str) -> None | GetJSONResponse:
        base_path = f"{uid}/{image_hash}/"
        file_name = f"{image_hash}.json"
        base_path += file_name
        blob = self.bucket.blob(base_path)
        if not blob.exists():
            return None
        obj = self._get_json_from_blob(blob)

        return GetJSONResponse(image_hash=image_hash, json_data=obj)
    
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

    def check_image_exists_for_id(self, uid: str, image_hash: str, dto: ColorDTO):
        file_name = f"{image_hash}-{dto.paint_id}"
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
