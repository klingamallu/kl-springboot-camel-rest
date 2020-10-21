package com.krisnal.demo.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.krisnal.demo.beans.OrderResponse;

@Component("orderService")
public class MockOrderService implements OrderService {

    private final AtomicInteger idGen = new AtomicInteger();
	
    @Override
    public OrderResponse createOrder(String custOrderXml) {
    	OrderResponse resp = new OrderResponse();
    	resp.setId(idGen.incrementAndGet());
    	resp.setMessage("Success");
    	return resp;
    }

}


