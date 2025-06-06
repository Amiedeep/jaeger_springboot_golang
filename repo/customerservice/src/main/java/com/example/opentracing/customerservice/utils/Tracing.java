package com.example.opentracing.customerservice.utils;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;

public class Tracing {

    public static JaegerTracer init(String serviceName) {
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
                .withType(ConstSampler.TYPE)
                .withParam(1);

        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(true);

        Configuration config = new Configuration(serviceName)
                .withSampler(samplerConfig)
                .withReporter(reporterConfig);

        return config.getTracer();
    }
}
