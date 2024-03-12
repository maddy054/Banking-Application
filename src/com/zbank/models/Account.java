package com.zbank.models;

import com.zbank.enums.AccountType;
import com.zbank.enums.Status;

public class Account {
	private int userId;
	private long accountNo;
	private long balance;
	private AccountType accountType;
	private int BranchId;
	private Status accountStatus = Status.ACTIVE;
	
	public Status getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(Status accountStatus) {
		this.accountStatus = accountStatus;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public AccountType getAccountType() {
		return accountType;
	}
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	public int getBranchId() {
		return BranchId;
	}
	public void setBranchId(int branchId) {
		BranchId = branchId;
	}
}
