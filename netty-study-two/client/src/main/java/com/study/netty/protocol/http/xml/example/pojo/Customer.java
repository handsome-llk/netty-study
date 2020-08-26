package com.study.netty.protocol.http.xml.example.pojo;

import java.util.List;

public class Customer {

	/** 客户ID*/
	private long customerNumber;
	
	/** 客户姓氏*/
	private String firstName;
	
	/** 客户名字*/
	private String lastName;
	
	/** 客户全称*/
	private List<String> meddleNames;

	public long getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(long customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<String> getMeddleNames() {
		return meddleNames;
	}

	public void setMeddleNames(List<String> meddleNames) {
		this.meddleNames = meddleNames;
	}
	
	
	
}
