from firebase_admin import firestore_async
from shared.data_classes import ColorDTO
from Api.repository.user_authentication_repository import User
from Api.data_classes import Favorites
from fastapi import HTTPException
from pydantic import ValidationError
from google.cloud.firestore_v1 import DocumentSnapshot

class FavoritesRepository():
    def __init__(self) -> None:
        self.collection_ref = firestore_async.client().collection("favorites")
    
    @staticmethod
    def _get_favorites_from_doc(doc: DocumentSnapshot):
        if doc.exists:
            data = doc.to_dict()
            try:
                favorites = Favorites(**data)
            except (SyntaxError, ValidationError, TypeError) as e:
                raise HTTPException(status_code=500, detail=f"Error parsing user favorites: {e}")
        else:
            favorites = Favorites()
        return favorites
    
    async def get_favorites(self, user: User):
        favorites_ref = self.collection_ref.document(user.uid)
        favorites_doc = await favorites_ref.get()
        favorites = self._get_favorites_from_doc(favorites_doc)
        return favorites
        
    async def add_to_favorites(self, user: User, color: ColorDTO):
        favorites_ref = self.collection_ref.document(user.uid)
        favorites_doc = await favorites_ref.get()
        favorites = self._get_favorites_from_doc(favorites_doc)
        color_favorited = False
        for f_color in favorites.favorites:
            if f_color.paint_id == color.paint_id:
                color_favorited = True
                break
        if not color_favorited:
            favorites.favorites.append(color)
            await favorites_ref.set(favorites.model_dump())
        return color

    async def remove_from_favorites(self, user: User, color_id: str):
        favorites_ref = self.collection_ref.document(user.uid)
        favorites_doc = await favorites_ref.get()
        if not favorites_doc.exists:
            return
        favorites = self._get_favorites_from_doc(favorites_doc)
        item_removed = False
        for i, f_color in enumerate(favorites.favorites):
            if f_color.paint_id == color_id:
                favorites.favorites.pop(i)
                item_removed = True
                break
        if item_removed:
            await favorites_ref.set(favorites.model_dump())
        return 
