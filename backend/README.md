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

## Backend Requirements:
A .env file (placed in the API directory) containing the following: `JWT_SIGNING_KEY="{secret_key}"`

To obtain the secret key for local testing, run the following (tested with git bash)

`openssl rand -hex 32`

You will also require authorization for firebase. To obtain:

- in the chamelon firebase console, **go to project settings** (click on the gear)
- go to service accounts
- click **generate new private key**
- paste the contents of the downloaded file into file called **firebase-auth.json**
- place firebase-auth.json into the root of the **Api** directory


# Local Testing

Firebase offers a firebase emulator that can be used to debug code locally.

## emulator installation
[Install the CLI through here](https://firebase.google.com/docs/cli#install_the_firebase_cli)

[download the emulator here](https://firebase.google.com/docs/emulator-suite/connect_and_prototype)

- when choosing option, select emulator -> firestore

## enable emulator for local build

To enable emulator, set the following environmental variables using the following:

### For Windows:

```powershell
$env:FIRESTORE_EMULATOR_HOST = "localhost:8080" 
```
