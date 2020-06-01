package main

import (
	"encoding/json"
	"net/http"

	"github.com/gorilla/mux"
)

//Order model
type Order struct {
	OrderID    int
	CustomerID int
	Name       string
}

var (
	orders []Order
)

//ReturnOrder find order of customer
func ReturnOrder(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	var order Order
	customerID := mux.Vars(r)["customerID"]

	Find(ctx, customerID, &order)

	json.NewEncoder(w).Encode(order)
}
