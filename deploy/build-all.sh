#!/bin/bash
set -e

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEPLOY_DIR="$PROJECT_ROOT/deploy"

echo "=== Mall-HJ Build Script ==="

# Step 1: Maven build
echo "[1/4] Maven build..."
cd "$PROJECT_ROOT"
mvn clean package -DskipTests -T 2C

# Step 2: Copy JARs to deploy directory
echo "[2/4] Copying JARs..."
SERVICES="mall-gateway mall-auth mall-member mall-product mall-order mall-seckill mall-ware mall-search mall-third-party mall-cart"
for svc in $SERVICES; do
  cp "$PROJECT_ROOT/$svc/target/$svc-2.0.0.jar" "$DEPLOY_DIR/$svc.jar"
  echo "  $svc.jar"
done

# Step 3: Build frontends (if directories exist)
echo "[3/4] Building frontends..."
if [ -d "$PROJECT_ROOT/mall-website" ]; then
  cd "$PROJECT_ROOT/mall-website"
  npm install && npm run build
  rm -rf "$DEPLOY_DIR/alibaba-cloud/mall-website-dist"
  cp -r dist "$DEPLOY_DIR/alibaba-cloud/mall-website-dist"
  echo "  mall-website → deploy/alibaba-cloud/mall-website-dist/"
fi

if [ -d "$PROJECT_ROOT/mall-admin" ]; then
  cd "$PROJECT_ROOT/mall-admin"
  npm install && npm run build
  rm -rf "$DEPLOY_DIR/houlang-cloud/mall-admin-dist"
  cp -r dist "$DEPLOY_DIR/houlang-cloud/mall-admin-dist"
  echo "  mall-admin → deploy/houlang-cloud/mall-admin-dist/"
fi

# Step 4: Build Docker images
echo "[4/4] Building Docker images..."
cd "$DEPLOY_DIR"

declare -A SERVICE_PORTS=(
  [mall-gateway]=8080 [mall-auth]=8081 [mall-member]=8082
  [mall-product]=8083 [mall-order]=8084 [mall-seckill]=8085
  [mall-ware]=8086 [mall-search]=8087 [mall-third-party]=8088
  [mall-cart]=8089
)

for svc in "${!SERVICE_PORTS[@]}"; do
  name="${svc#mall-}"
  echo "  Building $svc..."
  docker build -t "$svc:latest" -f "Dockerfile.$name" .
done

echo ""
echo "=== Build Complete ==="
echo "docker images | grep mall:"
docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}" | grep mall
echo ""
echo "Next: copy deploy/ to target machines and run docker compose up -d"
