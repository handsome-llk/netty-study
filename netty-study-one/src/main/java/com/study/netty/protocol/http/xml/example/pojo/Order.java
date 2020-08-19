package com.study.netty.protocol.http.xml.example.pojo;

public class Order {

	/** 订单数量*/
	private long orderNumber;
	
	/** 客户信息*/
	private Customer customer;
	
	/** 账单地址*/
	private Address billTo;
	
	/** 寄送方式*/
	private Shipping shipping;
	
	/** 送货地址*/
	private Address shipTo;
	
	/** 商品总价*/
	private Float total;

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getBillTo() {
		return billTo;
	}

	public void setBillTo(Address billTo) {
		this.billTo = billTo;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public Address getShipTo() {
		return shipTo;
	}

	public void setShipTo(Address shipTo) {
		this.shipTo = shipTo;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}
	
	
	
}
