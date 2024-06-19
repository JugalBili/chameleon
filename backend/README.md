# Dependencies

This project uses the following libraries. Please install them using

`pip install {dependency}`

## Dependencies:

- fastapi
- pyjwt
- httpx

Or similarly, if you are in the root directory, you can install everything using

`pip install -r requirements.txt`

# Running the backend

To run the backend, if you are in the `Api` directory, use

```
fastapi dev main.py
```

The app will be running by default on `localhost:8000`
API documentations can be found at `localhost:8000/docs`

# Local Debugging

Firebase offers a firebase emulator that can be used to debug code locally.
To enable emulator, set the following environmental variables using the following:

## For Windows:

```powershell
$env:FIRESTORE_EMULATOR_HOST = "localhost:8080" 
$env:FIREBASE_AUTH_EMULATOR_HOST = "localhost:9099"  
```
