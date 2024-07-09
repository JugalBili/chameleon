from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    jwt_signing_key: str
    use_firebase_emulator: str
    firebase_storage_bucket_url: str
    model_config = SettingsConfigDict(env_file=".env")
