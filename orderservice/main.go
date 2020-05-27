package main

import (
	"encoding/json"
	"fmt"
	"html"
	"log"
	"net/http"
	"strconv"

	"github.com/gomodule/redigo/redis"
	"github.com/gorilla/mux"
)

type Order struct {
	OrderID    int
	CustomerID int
	Name       string
}

var (
	orders []Order
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
	// conn.Do("HMSET", 1, "CustomerID", 2, "OrderID", 2)
	// conn.Do("SET", "Amandeep", "some")

	// value, err := redis.Values(conn.Do("HGETALL", 1))
	// if err != nil {
	// 	return
	// }

	// var object Order

	// fmt.Println("Before ", object)
	// err = redis.ScanStruct(value, &object)

	// fmt.Println(object)
	// if err != nil {
	// 	return
	// }
}

func handleRequests() {
	myRouter := mux.NewRouter().StrictSlash(true)

	myRouter.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "Hello, %q", html.EscapeString(r.URL.Path))
	})
	myRouter.HandleFunc("/orders", returnAllOrders)
	myRouter.HandleFunc("/orders/{customerId}", returnOrder)

	log.Println("Listening on localhost:8081")
	log.Fatal(http.ListenAndServe(":8081", myRouter))
}

func initOrders() {
	conn := redisInit()

	conn.Do("HMSET", 1, "OrderID", 21, "Name", "Burger")
	conn.Do("HMSET", 2, "OrderID", 31, "Name", "Pizza")

	conn.Close()
}

func returnAllOrders(w http.ResponseWriter, r *http.Request) {
	fmt.Println("Endpoint Hit: returnAllOrders")
	json.NewEncoder(w).Encode(orders)
}

func returnOrder(w http.ResponseWriter, r *http.Request) {
	conn := redisInit()

	vars := mux.Vars(r)
	key, _ := strconv.Atoi(vars["customerId"])

	value, err := redis.Values(conn.Do("HGETALL", key))
	if err != nil {
		panic(err.Error())
	}

	var object Order

	err = redis.ScanStruct(value, &object)
	json.NewEncoder(w).Encode(object)
}
