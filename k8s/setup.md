### Setup Kubernetes Cluster

```
kubectl create namespace [z.B polcloud/development/prod]
kubectl apply -f https://raw.githubusercontent.com/HttpMarco/polocloud/refs/heads/development/k8s/polocloud-init.yml
```

### Optional: For development environment
```
kubectl create namespace [z.B polcloud/development/prod]
kubectl apply -f https://raw.githubusercontent.com/HttpMarco/polocloud/refs/heads/development/k8s/polocloud-init-development.yml
```