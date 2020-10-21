package com.krisnal.demo.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="customerDetails")
public class Customer {
	
	@XmlElement(name="customerID")
	private int customerID;
	
	@XmlElement(name="customerName")
	private String customerName;
	
	@XmlElement(name="address")
	private String address;
	
	@XmlElement(name="orderNo")
	private int orderNo;
	
	@XmlElement(name="orderAmount")
	private double orderAmount;

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
}
