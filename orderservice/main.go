package main

import (
	"log"
	"net/http"

	app "github.com/amiedeep/jaeger_springboot_golang/orderservice/app"
	"github.com/gorilla/mux"
	"github.com/opentracing-contrib/go-stdlib/nethttp"
	"github.com/yurishkuro/opentracing-tutorial/go/lib/tracing"
)

var (
	tracer, _ = tracing.Init("order")
)

func main() {
	app.CreateSeeddata()
	handleRequests()
}

func handleRequests() {
	myRouter := mux.NewRouter().StrictSlash(true)
	myRouter.Path("/orders").Queries("customerID", "{customerID}").HandlerFunc(app.ReturnOrder)

	log.Println("Listening on localhost:9090")
	log.Fatal(http.ListenAndServe(":9090", nethttp.Middleware(tracer, myRouter)))
	log.Fatal(http.ListenAndServe(":9090", nethttp.Middleware(tracer, myRouter)))
}
