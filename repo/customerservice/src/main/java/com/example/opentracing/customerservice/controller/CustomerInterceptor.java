package com.example.opentracing.customerservice.controller;

import com.google.common.collect.ImmutableMap;
import io.opentracing.util.GlobalTracer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        GlobalTracer.get().activeSpan().setBaggageItem("loggedInUser", "Amandeep");
        GlobalTracer.get().activeSpan().setTag("loggedInUser", "Amandeep");

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {


        super.postHandle(request, response, handler, modelAndView);
    }
}