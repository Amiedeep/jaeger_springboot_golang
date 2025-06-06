package pkg

import (
	"github.com/opentracing/opentracing-go"
	jaeger "github.com/uber/jaeger-client-go"
	"github.com/uber/jaeger-client-go/config"
)

// Init creates a new instance of Jaeger tracer.
func Init(serviceName string) opentracing.Tracer {
	cfg, err := config.FromEnv()
	if err != nil {
		panic(err)
	}
	cfg.Reporter.LogSpans = true
	cfg.ServiceName = serviceName
	cfg.Sampler.Type = "const"
	cfg.Sampler.Param = 1

	tracer, _, err := cfg.NewTracer(config.Logger(jaeger.StdLogger))
	if err != nil {
		panic(err)
	}
	return tracer
}
