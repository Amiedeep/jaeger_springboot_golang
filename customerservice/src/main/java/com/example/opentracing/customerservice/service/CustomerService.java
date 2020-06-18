package com.example.opentracing.customerservice.service;

import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.repository.CustomerRepository;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

import java.util.HashMap;
import java.util.Map;

import static com.example.opentracing.customerservice.utils.http.getHttp;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${orderservice.host}")
    private String orderServiceHost;

    @Value("${orderservice.port}")
    private int orderServicePort;

    @Autowired
    private Producer producer;

    public Map<String, Object> findCustomer(long customerID) {

        Map<String, Object> response = new HashMap<String, Object>();

        Customer customer = customerRepository.findByid(customerID);

        if (customer == null) {
            Tags.ERROR.set(GlobalTracer.get().activeSpan(), true);
            response.put("error", "Customer not found");
            return response;
        }

        producer.sendMessage("Found customer: " + customer.name);
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

        producer.sendMessage("Found customer: " + customer.name);

        response.put("Customer", customer);
        String orders = getHttp(orderServiceHost, orderServicePort, "orders", customerID);
        producer.sendMessage("Found customer: " + customer.name);

        response.put("Orders", orders);
        return response;
    }
}
