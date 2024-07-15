#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd ) 
source "$SCRIPT_DIR/venv/Scripts/activate"
which pip

export AM_I_DOCKER=False
export BUILD_WITH_CUDA=True
export CUDA_HOME="/c/Program Files/NVIDIA GPU Computing Toolkit/CUDA/v11.8"

echo "== Installing from requirements_gpu.txt =="
pip install -r requirements_gpu.txt
pip uninstall torch torchvision
pip install torch torchvision --index-url https://download.pytorch.org/whl/cu118

cd "$SCRIPT_DIR/image_pipeline"

echo "== Installing from segment_anything =="
pip install -e ./segment_anything

echo "== Installing from GroundingDino =="
LINK_DIR="/c/temp/GroundingDINO"
rm -rf $LINK_DIR
ln -s "$SCRIPT_DIR/image_pipeline/GroundingDINO" "/c/temp/"
cd "$LINK_DIR"
pip install -v -e .
cp "$LINK_DIR/groundingdino/_C.cp310-win_amd64.pyd" "$SCRIPT_DIR/image_pipeline/GroundingDINO/groundingdino/_C.cp310-win_amd64.pyd"
rm -rf "/c/temp/GroundingDINO"

echo "== Downloading Models =="
cd "$SCRIPT_DIR/image_pipeline/models"
wget https://dl.fbaipublicfiles.com/segment_anything/sam_vit_h_4b8939.pth
wget https://github.com/IDEA-Research/GroundingDINO/releases/download/v0.1.0-alpha/groundingdino_swint_ogc.pthwhi