package com.example.opentracing.customerservice.controller;


import com.example.opentracing.customerservice.model.Person;
import com.example.opentracing.customerservice.repository.PersonRepository;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
public class CustomerController {


    @Autowired
    private JaegerTracer jaegerTracer;

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/customers")
    public String index() {
        Span span = jaegerTracer.buildSpan("Hello").start();

        List<Person> customers =  personRepository.findAll();
        span.finish();
        return "Greetings from Spring Boot!";
    }
}
