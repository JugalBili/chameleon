from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    jwt_signing_key: str
    model_config = SettingsConfigDict(env_file=".env")
