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
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @RequestMapping("/customer/{customerID}")
    public ResponseEntity<Object> get(@PathVariable long customerID) {
        Map<String, Object> customer =  customerService.findCustomer(customerID);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @RequestMapping("/compare/customer/{customerID}")
    public ResponseEntity<Object> compare(@PathVariable long customerID) {
        Map<String, Object> customer =  customerService.compareCustomer(customerID);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
