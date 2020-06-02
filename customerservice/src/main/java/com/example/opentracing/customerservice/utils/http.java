package com.example.opentracing.customerservice.utils;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class http {

    static OkHttpClient client = new OkHttpClient();

    public static TextMap requestBuilderCarrier(final Request.Builder builder) {
        return new TextMap() {
            @Override
            public Iterator<Map.Entry<String, String>> iterator() {
                throw new UnsupportedOperationException("carrier is write-only");
            }

            @Override
            public void put(String key, String value) {
                builder.addHeader(key, value);
            }
        };
    }

    public static String getHttp(int port, String path, int customerID) {
        Tracer tracer = GlobalTracer.get();
        Span span = tracer.buildSpan("order-service").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {


            HttpUrl url = new HttpUrl.Builder().scheme("http").host("localhost").port(port).addPathSegment(path)
                    .addQueryParameter("customerID", String.valueOf(customerID)).build();
            Request.Builder requestBuilder = new Request.Builder().url(url);

            Tags.SPAN_KIND.set(tracer.activeSpan(), Tags.SPAN_KIND_CLIENT);
            Tags.HTTP_METHOD.set(tracer.activeSpan(), "GET");
            Tags.PEER_SERVICE.set(span, "postgres");
            Tags.HTTP_URL.set(tracer.activeSpan(), url.toString());
            tracer.inject(tracer.activeSpan().context(), Format.Builtin.HTTP_HEADERS, requestBuilderCarrier(requestBuilder));

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                throw new RuntimeException("Bad HTTP result: " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            span.finish();
        }
    }
}
