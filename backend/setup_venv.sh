#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd ) 
PYTHON_VERSION=$(python -V 2>&1 | grep -Po '(?<=Python )(.+)')

if [[ "$PYTHON_VERSION" == "3.10.6" ]]; then
    echo -e "Correct python version\n"

    echo "=== Creating venv ==="
    cd $SCRIPT_DIR
    pip install virtualenv
    python -m venv venv
    echo "=== venv created ==="

    source "$SCRIPT_DIR/venv/Scripts/activate"

    echo "=== venv activated ==="

else
  echo "Please install Python 3.10.6 and set as default"
fi
