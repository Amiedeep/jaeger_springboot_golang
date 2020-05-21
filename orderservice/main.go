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
	redisInit()

}

func redisInit() {
	_, err := redis.Dial("tcp", "localhost:6379")

	if err != nil {
		panic(err)
	}
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

	log.Println("Listening on localhost:8080")
	log.Fatal(http.ListenAndServe(":8080", myRouter))
}

func initOrders() {
	orders = []Order{
		Order{OrderID: 21, CustomerID: 1, Name: "Jam"},
		Order{OrderID: 31, CustomerID: 2, Name: "burger"},
	}
}

func returnAllOrders(w http.ResponseWriter, r *http.Request) {
	fmt.Println("Endpoint Hit: returnAllOrders")
	json.NewEncoder(w).Encode(orders)
}

func returnOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key, _ := strconv.Atoi(vars["customerId"])

	for _, article := range orders {
		if article.CustomerID == key {
			json.NewEncoder(w).Encode(article)
			break
		}
	}
}
