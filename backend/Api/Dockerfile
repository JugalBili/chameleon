FROM python:3.10-slim

WORKDIR /app
COPY requirements.txt /app/

RUN pip install --no-cache-dir -r requirements.txt

COPY . /app/Api
COPY ./shared /app/shared

WORKDIR /app/Api

EXPOSE 8000

CMD ["fastapi", "run", "main.py"]