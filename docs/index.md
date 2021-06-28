# [Backstage](https://backstage.io)

This is your newly scaffolded Backstage App, Good Luck!

To start the app, run:

```sh
yarn install
yarn dev     
```

When the command finishes running, it should open up a browser window displaying your app. If not, you can open a browser and directly navigate to the frontend at http://localhost:3000.

***Important note:** You will only be able to log in as a Guest User, Google authentication method won't work*

## Deployment

### Using Docker and PostgreSQL

The first steps in the host build are to install dependencies with yarn install, generate type definitions using yarn tsc, and build all packages with yarn build.

```sh
yarn install --frozen-lockfile
yarn tsc
yarn build
```

Once the app is created, a Dockerfile is included in **/packages/backend/Dockerfile**. You need to execute it from the root of the repo as the build context, in order to get access to the root yarn.lock and package.json, along with any other files that might be needed.

Execute the build:

```sh
docker image build . -f packages/backend/Dockerfile --tag backstage
```

To be able to execute Backstage using postgreSQL as database, it`s necesary use a docker-compose to execute an instance of PostgreSQL. In the root directory of the project there is a docker-compose.yaml ready to be executed.

```sh
docker-compose up
```

You should then start to get logs in your terminal, and then you can open your browser at http://localhost:7000.

***Important note:** You will only be able to log in as a Guest User, Google authentication method won't work*

### Using Kubernetes and PostgreSQL

For deployment with kubernetes, you will need to have minikube / kind installed. In this tutorial minikube will be used.

```sh
brew install minikube
```

Create the cluster:

```sh
minikube start
```

Deployments in Kubernetes are commonly assigned to their own namespace to isolate services in a multi-tenant environment.

```sh
kubectl create namespace backstage
```

or use the kubernetes/namespace.yaml file:

```sh
kubectl apply -f kubernetes/namespace.yaml
```

First, create a Kubernetes Secret for the PostgreSQL username and password

```sh
kubectl apply -f kubernetes/postgres-secrets.yaml
```

PostgreSQL needs a persistent volume to store data and we define the kubernetes/postgres-storage.yaml.

(Note: to be able to execute the postgres service, you have to do this changes in the app.conf file and ensure that the bases urls of the app and the backend doesn´t contains the port number.)

```sh
app:
  title: Scaffolded Backstage App
  baseUrl: http://localhost:3000

organization:
  name: My Company

backend:
  baseUrl: http://localhost:7000
```

```sh
host: ${POSTGRES_SERVICE_HOST}
port: ${POSTGRES_SERVICE_PORT}
```

Apply the storage volume and claim to the Kubernetes cluster:

```sh
kubectl apply -f kubernetes/postgres-storage.yaml
```

Now we can create a Kubernetes Deployment descriptor for the PostgreSQL database deployment itself in the file kubernetes/postgres.yaml. Then, apply PostgreSQL deployment to the Kubernetes cluster:

```sh
kubectl apply -f kubernetes/postgres.yaml
```

The final step for our database is to create the service descriptor and apply the service to the Kubernetes cluster:

```sh
kubectl apply -f kubernetes/postgres-service.yaml
```

Then, create a Kubernetes Secret for the Backstage app.

```sh
kubectl apply -f kubernetes/backstage-secrets.yaml
```

For testing locally with minikube, point the local Docker daemon to the minikube internal Docker registry and then rebuild the image to install it:

```sh
eval $(minikube docker-env)
yarn build-image --tag backstage:1.0.0
```

Apply this Deployment to the Kubernetes cluster:

```sh
kubectl apply -f kubernetes/backstage.yaml
```

Check if the deployment and pods are running correctly:

```sh
kubectl get deployments --namespace=backstage
kubectl get pods --namespace=backstage
```

Apply the backstage service to the Kubernetes cluster:

```sh 
kubectl apply -f kubernetes/backstage-service.yaml

```

The Backstage deployment is now fully operational. You can forward a local port to the service:

```sh
sudo kubectl port-forward --namespace=backstage svc/backstage 80:80
```

Finally, you can open a browser and directly navigate to the frontend at http://localhost.

***Important note:** You will only be able to log in as a Guest User, Google authentication method won't work*

### Custom Helm Chart & Kind

1. Install Kind:

```sh
brew install kind
```

2. Create cluster and deploy nginx ingress:

```sh
kind create cluster --config=kind/cluster-conf.yaml

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/kind/deploy.yaml

kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=90s
```

4. Build the Backstage image and load it to kind:

```sh
yarn build-image --tag backstage:1.0.0
kind load docker-image backstage:1.0.0
```

5. Install custom Helm Chart:

```sh
helm install backstage ./helm
```

6. Check if the deployment and pods are running correctly:

```sh
kubectl get deployments --namespace=backstage
kubectl get pods --namespace=backstage
```

Finally, you can open a browser and directly navigate to the frontend at http://backstage.localhost.

***Important note:** You will only be able to log in as a Guest User, Google authentication method won't work*
