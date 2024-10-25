#!/bin/bash

# Get the directory where the script is located (now in /bin)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && cd .. && pwd)"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Docker could not be found. Please install Docker first."
    exit 1
fi

# Define the image name
IMAGE_NAME="team-rank:1.0.0"

# Check if the Docker image exists, build it if it doesn’t
if [[ "$(docker images -q $IMAGE_NAME 2> /dev/null)" == "" ]]; then
    echo "Docker image '$IMAGE_NAME' not found. Building the image..."
    docker build -t $IMAGE_NAME "$SCRIPT_DIR"
else
    echo "Docker image '$IMAGE_NAME' already exists. Skipping build."
fi

# Run the tests inside the Docker container
docker run --rm -v "$SCRIPT_DIR":/app $IMAGE_NAME gradle clean test --no-daemon
