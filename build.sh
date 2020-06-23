#!/usr/bin/env sh

eval $(minikube docker-env)

docker build --no-cache -t slinkydeveloper/flink-statefun-python-fn -f Dockerfile.python-fn .
docker build --no-cache -t slinkydeveloper/flink-statefun-python-engine -f Dockerfile.statefun .
docker build --no-cache -t slinkydeveloper/flink-statefun-python-message-sender -f Dockerfile.python-message-sender .
docker build --no-cache -t slinkydeveloper/flink-statefun-python-message-receiver -f Dockerfile.python-message-receiver .
