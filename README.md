# Flink StateFun 2.0 + Knative example

This example demonstrates how to deploy a stateful function application
written in Python to Knative.

Example took from: https://github.com/apache/flink-statefun/tree/master/statefun-examples/statefun-python-k8s-example

## Overview

This examples create a stateful function application,
that consumes `LoginEvent`s from a `logins` Kafka topic,
and produces `seen` count per user, into the `seen` Kafka topic.

The main example components contains:
- [main.py](main.py) - A StateFun python function that implements the main logic
- [module.yaml](module.yaml) - defines the ingress, egress and the remote function specification.
- [k8s](k8s) - a list of yamls to apply to deploy flink and the function to a k8s cluster. 
- [build.sh](build.sh) - Builds StateFun Docker images. 

## Setup

### Install a minikube installation

Use the scripts in https://github.com/matzew/kn-box to start minikube, install Knative Serving and Strimzi

### Build project

Use `build.sh` to build the images

## Deploy

* `kubectl apply -f k8s/1-kafka-topics.yaml`
* `kubectl apply -f k8s/2-python-fn.yaml`
* `kubectl apply -f k8s/3-engine.yaml`
 
## Generate events

Run:

```
kubectl run kafka-producer -ti --image=docker.io/slinkydeveloper/flink-statefun-python-message-sender --rm=true --restart=Never -- python3 event-generator.py --address my-cluster-kafka-bootstrap.kafka.svc:9092 --events 1000
```

This would generate 1,000 login events into the `logins` topic

## Check the output

You can look at the pushed messages with:

```
kubectl run kafka-consumer-in -ti --image=strimzi/kafka:latest-kafka-2.3.1  --rm=true --restart=Never -- bin/kafka-console-consumer.sh --bootstrap-server my-cluster-kafka-bootstrap.kafka.svc:9092 --topic logins --from-beginning
```

You can look at the processed messages:

```
kubectl run kafka-consumer -ti --image=docker.io/slinkydeveloper/flink-statefun-python-message-receiver --rm=true --restart=Never -- python3 event-consumer.py --address my-cluster-kafka-bootstrap.kafka.svc:9092
```
