package com.example.opentracing.customerservice;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerserviceApplication.class, args);
	}

	@Bean
	public JaegerTracer getJaegerTracer() {

		Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
				.withType(ConstSampler.TYPE)
				.withParam(1);

		Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
				.withLogSpans(true);

		Configuration config = new Configuration("customer-service")
				.withSampler(samplerConfig)
				.withReporter(reporterConfig);

		return config.getTracer();
	}
}
