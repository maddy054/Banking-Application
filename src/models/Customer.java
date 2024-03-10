package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Customer extends User {
	
	private long aadhar;
	private String pan;
	private String address;
	private List<String> columnName ;
	
	

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

	public void setAll(Map<String,Object> customer) {
		
		setAadhar((long) customer.get("AADHAR_NUMBER"));
		setPan((String) customer.get("PAN_NUMBER"));
		setAddress((String) customer.get("ADDRESS"));
	}

}
