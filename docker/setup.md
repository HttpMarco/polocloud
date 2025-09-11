### Setup Kubernetes Cluster

FIRST CHANGE PATH IN docker-compose.yml FILE
```
docker swarm init  
docker stack deploy -c docker-compose.yml polocloud
```

