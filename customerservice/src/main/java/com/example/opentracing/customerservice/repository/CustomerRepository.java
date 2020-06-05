package com.example.opentracing.customerservice.repository;

import com.example.opentracing.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAll();
    List<Customer> findByid(long id);
}
