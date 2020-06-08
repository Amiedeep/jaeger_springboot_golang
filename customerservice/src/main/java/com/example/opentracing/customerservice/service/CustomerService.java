package com.example.opentracing.customerservice.service;

import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.repository.CustomerRepository;
import com.example.opentracing.customerservice.utils.Tracing;
import com.google.common.collect.ImmutableMap;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.example.opentracing.customerservice.utils.http.getHttp;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    Tracer postgresTracer = Tracing.init("postgres");

    public Map<String, Object> findCustomer(long customerID) {

        Map<String, Object> response = new HashMap<String, Object>();

        Span span = postgresTracer.buildSpan("postgres").asChildOf(GlobalTracer.get().activeSpan()).start();

        try (Scope scope = postgresTracer.scopeManager().activate(span)) {
            span.log(ImmutableMap.of("event", "Searching customer", "id", customerID));
            Customer customer = customerRepository.findByid(customerID);
            if(customer == null) {
                span.log(ImmutableMap.of("event", "customer not found", "id", customerID));
                Tags.ERROR.set(span, true);
                response.put("Error", "Customer not found");
                return response;
            }
            span.log(ImmutableMap.of("event", "found customer", "name", customer.name));
            response.put("Customer", customer);
            String orders = getHttp(8081, "orders", customerID);
            response.put("Orders", orders);
        }
        finally{
            span.finish();
        }
        return response;
    }

    public Map<String, Object> compareCustomer(long customerID) {

        getHttp(8081, "orders", customerID);
        getHttp(8081, "orders", customerID);
        
        Map<String, Object> response = new HashMap<String, Object>();

        Span span = postgresTracer.buildSpan("postgres").asChildOf(GlobalTracer.get().activeSpan()).start();

        try (Scope scope = postgresTracer.scopeManager().activate(span)) {
            span.log(ImmutableMap.of("event", "Searching customer", "id", customerID));
            Customer customer = customerRepository.findByid(customerID);
            if(customer == null) {
                span.log(ImmutableMap.of("event", "customer not found", "id", customerID));
                Tags.ERROR.set(span, true);
                response.put("Error", "Customer not found");
                return response;
            }
            span.log(ImmutableMap.of("event", "found customer", "name", customer.name));
            response.put("Customer", customer);
            String orders = getHttp(8081, "orders", customerID);
            response.put("Orders", orders);
        }
        finally{
            span.finish();
        }
        return response;
    }
}
