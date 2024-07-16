from firebase_admin import firestore_async
from google.cloud.firestore_v1 import DocumentSnapshot, FieldFilter
import datetime
from Api.repository.user_authentication_repository import User
from shared.data_classes import ColorDTO
from typing import List
from pydantic import ValidationError
from fastapi import HTTPException
from Api.data_classes import History, HistoryList
class HistoryRepository:
    def __init__(self):
        self.collectionRef = firestore_async.client().collection("history")

    @staticmethod
    def _add_colors_to_history(history: History, colors: List[ColorDTO]):
        # pre-processing
        existing_ids = {color.paint_id for color in history.colors}

        for new_color in colors:
            if new_color.paint_id not in existing_ids:
                history.colors.append(new_color)
                existing_ids.add(new_color.paint_id)

    async def updateHistory(self, user: User, image_hash: str, colors: List[ColorDTO]):
        history_ref = (
            self.collectionRef.document(user.uid)
            .collection("history")
            .document(image_hash)
        )
        history_doc = await history_ref.get()
        if history_doc.exists:
            try:
                data = history_doc.to_dict()
                history = History(**data)
            except (SyntaxError, ValidationError, TypeError) as e:
                raise HTTPException(status_code=500, detail=f"Error parsing user history: {e}")
        else:
            history = History(last_accessed=datetime.datetime.now(), base_image=image_hash)
        
        #Modifies history since python is pass by reference
        self._add_colors_to_history(history, colors)
        data_to_send = history.model_dump()
        # Have to do this way to set timestamp to current
        data_to_send['last_accessed'] = firestore_async.SERVER_TIMESTAMP
        await history_ref.set(data_to_send)

    async def getHistory(self, user: User):
        collection_ref = (
            self.collectionRef.document(user.uid).collection("history")
        )
        query = collection_ref.order_by("last_accessed",
                                         direction=firestore_async.Query.DESCENDING
                                         )

        history_docs = query.stream()
        history_list: List[History] = []
        async for history_doc in history_docs:
            try:
                data = history_doc.to_dict()
                history_list.append(History(**data))
            except (SyntaxError, ValidationError, TypeError) as e:
                raise HTTPException(status_code=500, detail=f"Error parsing user history: {e}")   
        return HistoryList(history=history_list)