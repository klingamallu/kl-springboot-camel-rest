package com.krisnal.demo.beans;

public class OrderToCustomer {

   

   
    public Customer convertToCustomer(Order order) {
        Customer cust = new Customer();
        cust.setCustomerID(111);
        cust.setCustomerName("TestCustomer");
        cust.setAddress("123 Line1, City, State");
        cust.setOrderNo(order.getOrderId());
        cust.setOrderAmount(order.getOrderAmount());
        return cust;
    }
}