package com.example.opentracing.customerservice.controller;


import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.service.CustomerService;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    Tracer tracer = GlobalTracer.get();

    @RequestMapping("/customers/{customerID}")
    public ResponseEntity<Object> findAll(@PathVariable int customerID) {
        Span span = tracer.buildSpan("controller").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            List<Customer> customers =  customerService.findCustomers(customerID);
            return ResponseEntity.status(HttpStatus.OK).body(customers);
        } finally{
            span.finish();
        }
    }
}
