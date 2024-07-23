from Api.config import Settings
from shared.data_classes import ImageData, GetProcessedResponse
from fastapi import HTTPException
from pydantic import ValidationError
import http3
class ImageServerClient:
    def __init__(self, env: Settings):
        self.base_url = env.image_server_url
        self.client = http3.AsyncClient()

    async def send_image_process_request(self, image_data: ImageData):
        json_data = image_data.model_dump_json()
        url = self.base_url + "/image/generate"
        try:
            print("sending request to: ", url)
            print("sending data: ", json_data)
            resp  = await self.client.post(url, data=json_data, headers={"Content-Type": "application/json"}, timeout=60)
            resp.raise_for_status()
            processed_data = resp.json()
            print("received data from server: ", processed_data)
            processed_resp = [GetProcessedResponse(**image_response) for image_response in processed_data]
            return processed_resp
        except ValidationError as e:
            HTTPException(status_code=500, detail=f"Internal server error when calling Image Processor: {e}")
        except http3.exceptions.HttpError as e:
            HTTPException(status_code=500, detail=f"Internal server error when calling Image Processor: {e}")
        except Exception as e:
            HTTPException(status_code=500, detail=f"Internal server error when calling Image Processor: {e}")



