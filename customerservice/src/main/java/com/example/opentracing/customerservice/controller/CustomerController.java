package com.example.opentracing.customerservice.controller;


import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.service.CustomerService;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
public class CustomerController {


    @Autowired
    private JaegerTracer jaegerTracer;

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/customers")
    public ResponseEntity findAll() {
        Span span = jaegerTracer.buildSpan("Hello").start();

        List<Customer> customers =  customerService.findCustomers();
        span.finish();

        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }
}
