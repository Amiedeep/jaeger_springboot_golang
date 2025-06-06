package com.example.opentracing.customerservice.service;

import static com.example.opentracing.customerservice.utils.http.getHttp;

import java.util.HashMap;
import java.util.Map;

import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${orderservice.host}")
    private String orderServiceHost;

    @Value("${orderservice.port}")
    private int orderServicePort;

    public Map<String, Object> findCustomer(long customerID) {

        Map<String, Object> response = new HashMap<String, Object>();

        Customer customer = customerRepository.findByid(customerID);

        if (customer == null) {
            Tags.ERROR.set(GlobalTracer.get().activeSpan(), true);
            response.put("error", "Customer not found");
            return response;
        }

        response.put("Customer", customer);
        String orders = getHttp(orderServiceHost, orderServicePort, "orders", customerID);
        response.put("Orders", orders);
        return response;
    }

    public Map<String, Object> compareCustomer(long customerID) {

        getHttp(orderServiceHost, orderServicePort, "orders", customerID);
        getHttp(orderServiceHost, orderServicePort, "orders", customerID);
        
        Map<String, Object> response = new HashMap<String, Object>();

        Customer customer = customerRepository.findByid(customerID);


        if (customer == null) {
            Tags.ERROR.set(GlobalTracer.get().activeSpan(), true);
        }

        response.put("Customer", customer);
        String orders = getHttp(orderServiceHost, orderServicePort, "orders", customerID);

        response.put("Orders", orders);
        return response;
    }
}
