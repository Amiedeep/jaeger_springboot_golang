package main

import (
	"encoding/json"
	"log"
	"net/http"

	"github.com/gomodule/redigo/redis"
	"github.com/gorilla/mux"
	"github.com/opentracing-contrib/go-stdlib/nethttp"
	"github.com/yurishkuro/opentracing-tutorial/go/lib/tracing"
)

type Order struct {
	OrderID    int
	CustomerID int
	Name       string
}

var (
	orders    []Order
	tracer, _ = tracing.Init("order-service")
)

func main() {
	initOrders()
	handleRequests()
}

func redisInit() redis.Conn {
	conn, err := redis.Dial("tcp", "localhost:6379")

	if err != nil {
		panic(err.Error())
	}
	return conn
}

func handleRequests() {
	myRouter := mux.NewRouter().StrictSlash(true)
	myRouter.Path("/orders").Queries("customerID", "{customerID}").HandlerFunc(returnOrder)

	log.Println("Listening on localhost:8081")
	log.Fatal(http.ListenAndServe(":8081", nethttp.Middleware(tracer, myRouter)))
}

func initOrders() {
	conn := redisInit()

	conn.Do("HMSET", 1, "OrderID", 21, "Name", "Burger")
	conn.Do("HMSET", 2, "OrderID", 31, "Name", "Pizza")

	conn.Close()
}

// func returnAllOrders(w http.ResponseWriter, r *http.Request) {
// 	fmt.Println("Endpoint Hit: returnAllOrders")
// 	json.NewEncoder(w).Encode(orders)
// }

func returnOrder(w http.ResponseWriter, r *http.Request) {
	conn := redisInit()

	vars := mux.Vars(r)
	value, err := redis.Values(conn.Do("HGETALL", vars["customerID"]))
	if err != nil {
		panic(err.Error())
	}

	var object Order

	err = redis.ScanStruct(value, &object)
	json.NewEncoder(w).Encode(object)
}
