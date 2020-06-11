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

        Customer customer = customerRepository.findByid(customerID);
        response.put("Customer", customer);
        String orders = getHttp(8081, "orders", customerID);
        response.put("Orders", orders);
        return response;
    }

    public Map<String, Object> compareCustomer(long customerID) {

        getHttp(8081, "orders", customerID);
        getHttp(8081, "orders", customerID);

        Map<String, Object> response = new HashMap<String, Object>();

        Customer customer = customerRepository.findByid(customerID);
        response.put("Customer", customer);
        String orders = getHttp(8081, "orders", customerID);
        response.put("Orders", orders);
        return response;
    }
}
