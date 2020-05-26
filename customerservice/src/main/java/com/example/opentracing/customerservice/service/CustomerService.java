package com.example.opentracing.customerservice.service;

import com.example.opentracing.customerservice.model.Customer;
import com.example.opentracing.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.opentracing.customerservice.utils.http.getHttp;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findCustomers() {
        String output = getHttp(8081, "orders/1");
        return customerRepository.findAll();
    }
}
