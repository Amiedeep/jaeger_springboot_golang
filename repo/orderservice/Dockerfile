FROM golang:1.14.3 as builder
WORKDIR /orderservice

# Fetch dependencies
COPY go.mod ./
RUN go mod download

# Build
COPY . ./
RUN CGO_ENABLED=0 go build

FROM golang:1.14.3

WORKDIR /app

COPY --from=builder /orderservice/orderservice .

CMD ["./orderservice"]
# CMD go run main.go
