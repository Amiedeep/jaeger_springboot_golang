package com.example.opentracing.customerservice.service;

import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.repository.CustomerRepository;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.opentracing.customerservice.utils.http.getHttp;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    Tracer tracer = GlobalTracer.get();

    public List<Customer> findCustomers() {

        String output = getHttp(8081, "orders");

        Span span = tracer.buildSpan("Postgres-service").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            return customerRepository.findAll();
        } finally{
            span.finish();
        }

    }
}
