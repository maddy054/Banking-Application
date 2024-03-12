package com.zbank.models;

public class Customer extends User {
	
	private long aadhar;
	private String pan;
	private String address;
	
	public void setAadhar(long aadhar) {
		this.aadhar = aadhar;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	
	public long getAadhar() {
		return aadhar ;
	}
	public String getPan() {
		return pan ;
	}
	public String getAddress() {
		return address;
	}
}
