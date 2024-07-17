from firebase_admin import firestore_async, storage
from google.cloud.storage import Blob
from typing import List
from pydantic import ValidationError
from fastapi import HTTPException, UploadFile
import hashlib
import io
import datetime

from Api.repository.user_authentication_repository import User
from shared.data_classes import ColorDTO, Image
from Api.data_classes import GetReviewImageResponse, Review, ReviewList, ReviewDTO, UploadReviewImageDTO
from typing import List


class GalleryRepository:
    def __init__(self):
        self.collectionRef = firestore_async.client().collection("gallery")
        self.bucket = storage.bucket()
        self.gallery_folder = "gallery"
        self.metadata_exception = HTTPException(status_code=500, detail="Error encountered: invalid metadata")

    
    async def upload_review_image(self, user: User, file: UploadFile, paint_id: str):
        await file.seek(0)
        content = await file.read()
        image_hash = hashlib.sha256(content).hexdigest()
        base_path = f"{self.gallery_folder}/{paint_id}"
        image_path = f"{base_path}/{image_hash}"
        blob = self.bucket.blob(image_path)

        if blob.exists():
            return image_hash
        content_file = io.BytesIO(content)
        blob.metadata = {"uid": user.uid}
        blob.upload_from_file(content_file, content_type=file.content_type)
        blob.make_private()

        return image_hash

    @staticmethod
    def _get_image_from_blob(blob: Blob) -> Image:
        image_bytes = blob.download_as_bytes()

        return Image(image_bytes=image_bytes, contentType=blob.content_type)

    def get_review_image(self, image_hash: str, paint_id: str,
                            download_image: bool = False) -> None | GetReviewImageResponse:
        base_path = f"{self.gallery_folder}/{paint_id}"
        file_name = f"{image_hash}"
        image_path = f"{base_path}/{file_name}"
        blob = self.bucket.blob(image_path)
        if not blob.exists():
            return None
        if download_image:
            image = self._get_image_from_blob(blob)
        else:
            image = None
        blob.make_private()

        return GetReviewImageResponse(image_hash=image_hash, paint_id=paint_id, image_data=image)

    
    async def get_paint_user_reviews(self, paint_id: str, user: User):
        review_ref = (
            self.collectionRef.document(paint_id).collection("reviews").document(user.uid)
        )
        review_doc = await review_ref.get()
        review_list: List[Review] = []
        if review_doc.exists:
            try:
                data = review_doc.to_dict()
                review = Review(**data)
                review_list.append(review)
            except (SyntaxError, ValidationError, TypeError) as e:
                raise HTTPException(status_code=500, detail=f"Error parsing review: {e}")
            
        return ReviewList(reviews=review_list)

    
    async def get_paint_reviews(self, paint_id: str):
        collection_ref = (
            self.collectionRef.document(paint_id).collection("reviews")
        )
        
        review_docs = collection_ref.stream()
        review_list: List[Review] = []
        
        async for review in review_docs:
            try:
                data = review.to_dict()
                review_list.append(Review(**data))
            except (SyntaxError, ValidationError, TypeError) as e:
                raise HTTPException(status_code=500, detail=f"Error parsing user review: {e}")
 
        return ReviewList(reviews=review_list)

    async def add_paint_review(self, review_dto: ReviewDTO, user: User):
        review_ref = (
            self.collectionRef.document(review_dto.paint_id).collection("reviews").document(user.uid)
        )
        
        review = None

        review_doc = await review_ref.get()
      
        if review_doc.exists:
            try:
                data = review_doc.to_dict()
                review = Review(**data)
                review.review = review_dto.review
                review.image_hashes = review_dto.image_hashes
                review.timestamp = datetime.datetime.now()
                
            except (SyntaxError, ValidationError, TypeError) as e:
                raise HTTPException(status_code=500, detail=f"Error parsing user review: {e}")
        else:
            review = Review(paint_id=review_dto.paint_id, uid=user.uid, review=review_dto.review, image_hashes=review_dto.image_hashes, timestamp=datetime.datetime.now())

        data_to_send = review.model_dump()
        data_to_send["timestamp"] = firestore_async.SERVER_TIMESTAMP
        await review_ref.set(data_to_send)
 
        return review
