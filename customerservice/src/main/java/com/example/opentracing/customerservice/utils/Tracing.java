package com.example.opentracing.customerservice.utils;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.util.GlobalTracer;

public class Tracing {

    public static void init() {
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
                .withType(ConstSampler.TYPE)
                .withParam(1);

        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(true);

        Configuration config = new Configuration("customer-service")
                .withSampler(samplerConfig)
                .withReporter(reporterConfig);

        GlobalTracer.registerIfAbsent(config.getTracer());
    }
}
