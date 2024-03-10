package models;


import java.util.Map;

public class Account {
	private int userId;
	private long accountNo;
	private long balance;
	private String accountType;
	private int BranchId;
	private String accountStatus = "ACTIVE";
	
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
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
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public int getBranchId() {
		return BranchId;
	}
	public void setBranchId(int branchId) {
		BranchId = branchId;
	}
	
	public void setAll(Map<String,Object> account) {
		setUserId((int) account.get("USER_ID"));
		setAccountNo((long) account.get("ACCOUNT_NUMBER"));
		setBranchId((int) account.get("BRANCH_ID"));
		setBalance((long) account.get("BALANCE"));
		setAccountStatus((String) account.get("STATUS"));
		setAccountType((String) account.get("ACCOUNT_TYPE"));
	}
}
