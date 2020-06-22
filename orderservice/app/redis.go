package app

import (
	"context"
	"os"

	"github.com/gomodule/redigo/redis"
	"github.com/opentracing/opentracing-go"
	tags "github.com/opentracing/opentracing-go/ext"
	"github.com/yurishkuro/opentracing-tutorial/go/lib/tracing"
)

var (
	redisTracer, _ = tracing.Init("redis")
	redisURL       = getEnvValue("redis_url", "localhost")
)

func getEnvValue(field string, fallback string) string {
	value := os.Getenv(field)

	if len(value) != 0 {
		return value
	}
	return fallback
}

//CreateSeeddata creates initial orders
func CreateSeeddata() {
	conn := GetConn()

	conn.Do("HMSET", 1, "OrderID", 21, "Name", "Burger", "CustomerID", 1)
	conn.Do("HMSET", 2, "OrderID", 31, "Name", "Pizza", "CustomerID", 2)

	conn.Close()
}

//GetConn return redis connection
func GetConn() redis.Conn {
	conn, err := redis.Dial("tcp", redisURL+":6379")

	if err != nil {
		panic(err.Error())
	}
	return conn
}

//Find serach redis and return an object
func Find(ctx context.Context, param string, object interface{}) {

	span, _ := opentracing.StartSpanFromContextWithTracer(ctx, redisTracer, "HMETALL")
	defer span.Finish()

	tags.SpanKindRPCClient.Set(span)
	tags.PeerService.Set(span, "redis")

	conn := GetConn()

	defer conn.Close()

	span.LogKV("event", "HGETALL", "value", "Finding orders in redis")
	value, err := redis.Values(conn.Do("HGETALL", param))
	if err != nil {
		panic(err.Error())
	}
	err = redis.ScanStruct(value, object)

	span.LogKV("event", "HGETALL", "value", "Found orders in redis", "order", object.(*Order).Name)
}
