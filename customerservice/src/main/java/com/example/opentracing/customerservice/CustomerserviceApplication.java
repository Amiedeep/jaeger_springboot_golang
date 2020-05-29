package com.example.opentracing.customerservice;

import com.example.opentracing.customerservice.utils.Tracing;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.util.GlobalTracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerserviceApplication {

	public static void main(String[] args) {

		JaegerTracer tracer = Tracing.init("customer");
		GlobalTracer.registerIfAbsent(tracer);
		SpringApplication.run(CustomerserviceApplication.class, args);
	}
}
