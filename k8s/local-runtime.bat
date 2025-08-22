@echo off
cd ..\

call gradlew :agent:build

docker build -f docker/Dockerfile -t polocloud-launcher:latest .

kubectl apply -f k8s/local-polocloud-init.yml
pause