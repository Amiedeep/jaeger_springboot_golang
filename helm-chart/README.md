# Helm Chart

Helm chart for this project; tested on minikube.


## Pre-requisites

- helm2, v2.16.9 (tested version)
- Please install Jaeger chart from [here](https://github.com/Amiedeep/jaeger_minikube_setup). Follow the instructions of chart nodes for the jaeger service access.

## Running instructions

Deploy the chart using:

```bash
helm install --name demo-app ./
```

## Test setup

Follow the instructions of chart nodes for the services access.