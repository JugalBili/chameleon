# Dependencies

This project requires the following dependencies. Ensure you have them before beginning.

Instructions on installing CUDA and cuDNN are given in installation section. 

Dependencies:
- Python 3.10.6
- [Git Bash](https://git-scm.com/downloads)
- CUDA 11.8 (Optional)
- cuDNN 8.9.4.XX (Optional)

# Installation

> Terminal commands will be taking place in a **git bash** or **linux terminal**

Before proceeding, ensure that you are in the `backend/` directory
```bash
cd ./backend
```

Give executable permission to the bash scripts in this folder
```bash
chmod +x ./setup_venv.sh install_requirements.sh install_requirements_gpu_partial.sh install_requirements_gpu_full.sh
```

## CPU Only

With CPU only insallation, both models used in image_pipeline will strictly run on CPU. Average time to generate a colored image will be ~45 seconds.

### 1. Create Virtual Environment
Create the virtual environment using the `setup_venv.sh` bash script
```bash
./setup_venv.sh
```

### 2. Install Requirements
Use the `install_requirements` bash script to install requirements and download models
```bash
./install_requirements.sh
```

## Partial GPU
With Partial GPU insallation, Only the SAM model will use GPU and the GroundingDINO will still use CPU. Average time to generate a colored image will be ~17 seconds.

Before proceeding, ensure you have a **CUDA-capable Nvidia GPU**

For more details on Steps 1. 2. and 3., please look at the following resources 
- Steps 2-5 in https://medium.com/analytics-vidhya/installing-cuda-and-cudnn-on-windows-d44b8e9876b5
- https://medium.com/geekculture/install-cuda-and-cudnn-on-windows-linux-52d1501a8805

### 1. Download Visual Studio 2022

Go to https://visualstudio.microsoft.com/downloads/ and download Visual Studio 2022 Community.

Choose the `Desktop Development With C++` workload

In the right sidebar, under "Desktop Development With C++" -> "Optional", esnure you have the following selected
- MSCV v143 (or latest)
- C++ ATL for latest v143 build tools (or latest corresponding to MSCV version)
- C++ Build Insights
- C++ Profiling Tools
- C++ CMAKE tools for Windows

Then click `install` and wait

> Visual Studio will most likely get installed in `C:\Program Files (x86)\Microsoft Visual Studio\2022`

### 2. Install CUDA ToolKit 11.8

Visit [Nvidia's Website](https://developer.nvidia.com/cuda-11-8-0-download-archive?target_os=Linux&target_arch=x86_64&Distribution=Ubuntu&target_version=22.04&target_type=deb_local) to download CUDA 11.8 for your machine

Follow the installation tool and use the Express installation option

> CUDA will most likely get installed in `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.8\`


Now, add the following entries to `Path` using the Environmental Variables as both User and System Variables
- `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.8`
- `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.8\bin`
- `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.8\libnvvp`
- `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.8\lib\x64`
- `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.8\include`

Create the following new System Variables
- `CUDA_HOME` -> `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.8`
- `CUDA_PATH` -> `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.8`



### 3. Install cuDNN 8.9.4

Visit [Nvidia Developer Website](https://developer.nvidia.com/rdp/cudnn-archive) to download cuDNN 8.9.4 for CUDA 11.x (Local Installer for Windows (Zip))

You will need to create a developer account to download cuDNN 

After unzipping the downloaded zipped folder, drag and drop the `bin`, `include` and `lib` to the CUDA 11.8 directory (`C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.8\`)  


### 4. Create Virtual Environment
Create the virtual environment using the `setup_venv.sh` bash script
```bash
./setup_venv.sh
```

### 5. Install Requirements
Use the `install_requirements_gpu_partial` bash script to install requirements and download models
```bash
./install_requirements_gpu_partial.sh
```

Before installing requirements, ensure `CUDA_HOME` environmental variable is active and pointed to the folder in which CUDA 11.8 is installed.

> Note that folder must be in linux folder format e.g. `/c/Program Files/NVIDIA GPU Computing Toolkit/CUDA/v11.8`

```bash
export CUDA_HOME='/c/Program Files/NVIDIA GPU Computing Toolkit/CUDA/v11.8'
```

### Full GPU

With Full GPU insallation, both the SAM model and the GroundingDINO model will use GPU. Average time to generate a colored image will be ~8 seconds.

First launch git bash cli with administrator privileages.


### 1. Install Visual Studio 2022, CUDA and cuDNN
- Follow Steps 1-3 from the installation instructions for Partial GPU 

### 2. Add CL Compiler to Path
Next step is to add the C++ CL compiler to PATH

cl.exe compiler was downloaded along with Visual Studio and can be found in directory `C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\VC\Tools\MSVC\14.36.32532\bin\Hostx64\x64` or similar

Copy cl.exe to some other accessible directory, lets say `C:\PATH_Programs\`

Add this directory to Path using the Environmental Variables as both User and System Variables

### 3. Install Ninja Build Toolchain
Ninja build toolchain speeds up the process of C++ builds.

Download Ninja binary from https://github.com/ninja-build/ninja/releases

Extract the zip, and copy `ninja.exe` to the same accessible directory used for the CL compiler such as `C:\PATH_Programs\`

If direcotry is different, add the new directory to Path using the Environmental Variables as both User and System Variables

### 4. Create Virtual Environment
Create the virtual environment using the `setup_venv.sh` bash script
```bash
./setup_venv.sh
```

### 5. Install Requirements
Before installing requirements, ensure `CUDA_HOME` environmental variable is active and pointed to the folder in which CUDA 11.8 is installed.

> Note that folder must be in linux folder format e.g. `/c/Program Files/NVIDIA GPU Computing Toolkit/CUDA/v11.8`

```bash
export CUDA_HOME='/c/Program Files/NVIDIA GPU Computing Toolkit/CUDA/v11.8'
```

Use the `install_requirements_gpu_full` bash script to install requirements and download models

Before executing, modify the CUDA_HOME path in line 9 to match the path above 

```bash
./install_requirements_gpu_full.sh
```

# Linux Installation (WIP)
- need g++ & gcc <= 11.0.0
- need cuda 11.8 and cudnn 8.9.4 (https://gist.github.com/Autonomousanz/5469efa0f91af4a6376ff60eeecf179c) (https://developer.nvidia.com/rdp/cudnn-archive)
  - chaneg scruipt to install drivers 555 
  - manually download cudnn-11.3-linux-x64-v8.2.1.32.tgz file since above script gets html text file instead of .tgz file
- need python 3.10.6 (https://phoenixnap.com/kb/how-to-install-python-3-ubuntu)
  - sudo apt-get install libbz2-dev before compiling
  - 

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
