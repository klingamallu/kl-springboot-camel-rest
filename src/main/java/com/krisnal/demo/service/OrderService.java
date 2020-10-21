package com.krisnal.demo.service;

import com.krisnal.demo.beans.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(String custOrderXml);

}
