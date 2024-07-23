from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    firebase_storage_bucket_url: str
    model_config = SettingsConfigDict(env_file=".env")
