package utilities;

public class Transaction {
	private long dateTime;
	private int userId;
	private long accountNo;
	private long transactionId;
	private long transactionAccNo;
	private int amount;
	private String type;
	private String description;
	private long openBalance;
	private long closeBalance;
	private String status;
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
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
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public long getTransactionAccNo() {
		return transactionAccNo;
	}
	public void setTransactionAccNo(long transactionAccNo) {
		this.transactionAccNo = transactionAccNo;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getOpenBalance() {
		return openBalance;
	}
	public void setOpenBalance(long openBalance) {
		this.openBalance = openBalance;
	}
	public long getCloseBalance() {
		return closeBalance;
	}
	public void setCloseBalance(long closeBalance) {
		this.closeBalance = closeBalance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
