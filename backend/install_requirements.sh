#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd ) 
source "$SCRIPT_DIR/venv/Scripts/activate"
which pip

echo "== Installing from requirements.txt =="
pip install -r requirements.txt

cd "$SCRIPT_DIR/image_pipeline"

echo "== Installing from segment_anything =="
pip install -e ./segment_anything

echo "== Installing from GroundingDino =="
pip install --no-build-isolation -e ./GroundingDINO


echo "== Downloading Models =="
cd "$SCRIPT_DIR/image_pipeline/models"
wget https://dl.fbaipublicfiles.com/segment_anything/sam_vit_h_4b8939.pth
wget https://github.com/IDEA-Research/GroundingDINO/releases/download/v0.1.0-alpha/groundingdino_swint_ogc.pth