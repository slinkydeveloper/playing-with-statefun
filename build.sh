#!/bin/zsh

set -e

function build_image() {
  cd "./$1"
  docker build -t "slinkydeveloper/$1" .
  cd ..
}

eval $(minikube docker-env)
mvn package

build_image mapper
build_image greeter
build_image inbound
build_image outbound
