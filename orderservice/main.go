package main

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/opentracing-contrib/go-stdlib/nethttp"
	"github.com/yurishkuro/opentracing-tutorial/go/lib/tracing"
)

var (
	tracer, _ = tracing.Init("order-service")
)

func main() {
	CreateSeeddata()
	handleRequests()
}

func handleRequests() {
	myRouter := mux.NewRouter().StrictSlash(true)
	myRouter.Path("/orders").Queries("customerID", "{customerID}").HandlerFunc(ReturnOrder)

	log.Println("Listening on localhost:8081")
	log.Fatal(http.ListenAndServe(":8081", nethttp.Middleware(tracer, myRouter)))
}
