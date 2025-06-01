# Java Spring Boot Demo Project

## Minikube

- Way to locally run Kubernetes

### Steps to run:

- To start: `minikube start --vm-driver=hyperv` (make sure minikube is installed)
    - Ensure Minikube is on: `kubectl get nodes`

- Build Docker image: `docker build --no-cache -t myapp:latest .`
    - Load the image into Minikube: `minikube image load myapp:latest`

- Apply changes in all kubernetes files: `kubectl apply -f kubernetes/`
- Full restart: `kubectl rollout restart deployment/myapp-deployment`

- Get Minikube IP Address: `minikube ip`
- Testing: `http://<MINIKUBE_IP>:30007`
- Logs:
  `kubectl logs -l app=myapp`
  `kubectl logs <pod name from get pods>`
