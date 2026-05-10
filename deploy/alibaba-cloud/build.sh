#!/bin/bash
set -e

DEPLOY_DIR=/opt/mall

echo "=== Building Docker images ==="

# Build gateway image
cd $DEPLOY_DIR
docker build -t mall-gateway:latest -f Dockerfile.gateway .
docker build -t mall-auth:latest -f Dockerfile.auth .
docker build -t mall-product:latest -f Dockerfile.product .

echo "=== Images built successfully ==="
docker images | grep mall

echo "=== Starting services ==="
cd $DEPLOY_DIR
docker compose up -d

echo "=== Waiting for services to start ==="
sleep 10
docker compose ps

echo "=== Deployment complete ==="
