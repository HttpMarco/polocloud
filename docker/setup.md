### Setup Kubernetes Cluster

FIRST CHANGE PATH IN docker-compose.yml FILE
```
docker run -d --name agent -p 8080:8080 -v C:\Users\mirco\Desktop\te\:/cloud/local -v /var/run/docker.sock:/var/run/docker.sock polocloud:development
```

