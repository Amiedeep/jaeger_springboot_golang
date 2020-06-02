package com.example.opentracing.customerservice.service;

import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.repository.CustomerRepository;
import com.example.opentracing.customerservice.utils.Tracing;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.opentracing.customerservice.utils.http.getHttp;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    Tracer postgresTracer = Tracing.init("postgres");

    public List<Customer> findCustomers(int customerID) {

        String output = getHttp(8081, "orders", customerID);

        Span span = postgresTracer.buildSpan("postgres").asChildOf(GlobalTracer.get().activeSpan()).start();
        try (Scope scope = postgresTracer.scopeManager().activate(span)) {
            return customerRepository.findAll();
        } finally{
            span.finish();
        }
    }
}
