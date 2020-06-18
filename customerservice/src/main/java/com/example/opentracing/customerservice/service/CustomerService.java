package com.example.opentracing.customerservice.service;

import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.repository.CustomerRepository;
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
        String orders = getHttp(9090, "orders", customerID);
        response.put("Orders", orders);
        return response;
    }

    public Map<String, Object> compareCustomer(long customerID) {

        getHttp(9090, "orders", customerID);
        getHttp(9090, "orders", customerID);

        Map<String, Object> response = new HashMap<String, Object>();

        Customer customer = customerRepository.findByid(customerID);

        if (customer == null) {
            Tags.ERROR.set(GlobalTracer.get().activeSpan(), true);
        }

        producer.sendMessage("Found customer: " + customer.name);

        response.put("Customer", customer);
        String orders = getHttp(9090, "orders", customerID);
        response.put("Orders", orders);
        return response;
    }
}
