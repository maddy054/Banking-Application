package utilities;

public class Account {
	private int userId;
	private long accountNo;
	private long balance;
	private String accountType;
	private int BranchId;
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
}
