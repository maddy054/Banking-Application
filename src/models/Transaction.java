package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	public List<Object> getAll(){
		List<Object> transaction = new ArrayList<>();
		transaction.add(getDateTime());
		transaction.add(getUserId());
		transaction.add(getAccountNo());
		transaction.add(getTransactionAccNo());
		transaction.add(getAmount());
		transaction.add(getType());
		transaction.add(getDescription());
		transaction.add(getOpenBalance());
		transaction.add(getCloseBalance());
		transaction.add(getStatus());
		return transaction;

	}
	public void setAll(Map<String,Object> transaction) {
		setDateTime((long) transaction.get("DATE_TIME"));
		setUserId((int) transaction.get("USER_ID"));
		setAccountNo((long) transaction.get("ACCOUNT_NUMBER"));
		setTransactionId((long) transaction.get("TRANSACTION_ID"));
		setTransactionAccNo((long) transaction.get("TRASACTION_ACCOUNT_NO"));
		setAmount((int) transaction.get("AMOUNT"));
		setType((String) transaction.get("TYPE"));
		setDescription((String) transaction.get("DESCRIPTION"));
		setOpenBalance((long) transaction.get("OPENING_BALANCE"));
		setCloseBalance((long) transaction.get("CLOSING_BALANCE"));
		setStatus((String) transaction.get("STATUS"));
	}
}
