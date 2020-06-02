FROM golang:1.14.3

ENV JAEGER_AGENT_HOST jaeger
WORKDIR /app
COPY main.go go.mod go.sum /app/

CMD go run main.go
