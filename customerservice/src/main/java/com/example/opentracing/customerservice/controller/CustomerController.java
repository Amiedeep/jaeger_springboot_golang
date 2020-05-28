package com.example.opentracing.customerservice.controller;


import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.service.CustomerService;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    Tracer tracer = GlobalTracer.get();

    @RequestMapping("/customers")
    public ResponseEntity<Object> findAll() {
        Span span = tracer.buildSpan("controller").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            List<Customer> customers =  customerService.findCustomers();
            return ResponseEntity.status(HttpStatus.OK).body(customers);
        } finally{
            span.finish();
        }
    }
}
