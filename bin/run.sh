#!/bin/bash

# Get the directory where the script is located (now in /bin)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && cd .. && pwd)"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Docker could not be found. Please install Docker first."
    exit 1
fi

# Check if the correct number of arguments is provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <input file path> <output file path>"
    exit 1
fi

# Convert input and output paths to absolute paths
INPUT_FILE="$(cd "$(dirname "$1")" && pwd)/$(basename "$1")"
OUTPUT_FILE="$(cd "$(dirname "$2")" && pwd)/$(basename "$2")"

# Extract directories of each file
INPUT_DIR="$(dirname "$INPUT_FILE")"
OUTPUT_DIR="$(dirname "$OUTPUT_FILE")"

# Define the image name
IMAGE_NAME="team-rank:1.0.0"

# Check if the Docker image exists, build it if it doesn’t
if [[ "$(docker images -q $IMAGE_NAME 2> /dev/null)" == "" ]]; then
    echo "Docker image '$IMAGE_NAME' not found. Building the image..."
    docker build -t $IMAGE_NAME "$SCRIPT_DIR"
else
    echo "Docker image '$IMAGE_NAME' already exists. Skipping build."
fi

# Run the application inside Docker, mounting each directory
docker run --rm \
    -v "$SCRIPT_DIR":/app \
    -v "$INPUT_DIR":/input_dir \
    -v "$OUTPUT_DIR":/output_dir \
    $IMAGE_NAME gradle run -Pargs="/input_dir/$(basename "$INPUT_FILE"),/output_dir/$(basename "$OUTPUT_FILE")"
