package com.zbank.models;

import com.zbank.enums.TransactionDetail;
import com.zbank.enums.TransactionPeriod;

public class TransactionReq {
	
	private TransactionDetail type;
	private TransactionPeriod time;
	private int userId;
	private long accountNumber;
	private int limit = 50;
	
	public int getLimit() {
		return this.limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public TransactionDetail getType() {
		return type;
	}
	public void setType(TransactionDetail type) {
		this.type = type;
	}
	public TransactionPeriod getTime() {
		return time;
	}
	 
	
	public void setTime(TransactionPeriod time) {
		this.time = time;
	}

}
