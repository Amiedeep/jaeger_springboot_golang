package app

import (
	"encoding/json"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/opentracing/opentracing-go"
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
	span := opentracing.SpanFromContext(ctx)

	var order Order
	customerID := mux.Vars(r)["customerID"]

	span.SetTag("customerID", customerID)
	span.LogKV("event", "Get Orders", "value", "Received get orders request for a customer", "customerID", customerID)

	loggedInUser := span.BaggageItem("loggedinuser")
	span.SetTag("loggedInUser", loggedInUser)

	Find(ctx, customerID, &order)

	json.NewEncoder(w).Encode(order)
}
