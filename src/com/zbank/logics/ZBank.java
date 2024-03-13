package com.zbank.logics;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.zbank.enums.Status;
import com.zbank.enums.TransactionDescription;
import com.zbank.enums.TransactionStatus;
import com.zbank.enums.TransactionType;
import com.zbank.enums.UserType;
import com.zbank.exceptions.BankingException;
import com.zbank.exceptions.InvalidUserException;
import com.zbank.exceptions.WrongPasswordException;
import com.zbank.models.Account;
import com.zbank.models.Branch;
import com.zbank.models.Customer;
import com.zbank.models.Employee;
import com.zbank.models.Transaction;
import com.zbank.models.TransactionReq;
import com.zbank.persistence.Connector;
import com.zbank.persistence.DbConnector;
import com.zbank.utilities.SHAHash;
import com.zbank.utilities.Validation;

public class ZBank {
	
	private Connector dbConnector = new DbConnector();
	
	public UserType getUser(int userId) throws BankingException,InvalidUserException {
		return dbConnector.getRole(userId);
	}
	
	public void checkPassword(JSONObject json) throws BankingException, WrongPasswordException {
		int userId = json.getInt("user_id");
		String password = (String) json.get("password");
		

		String originalPassword = dbConnector.getPassword(userId);
		String enteredPassword = SHAHash.getHash(password);
			
		boolean isCorrect = originalPassword.equals(enteredPassword);
		if(!isCorrect) {
			throw new WrongPasswordException("Incorrect password!! try again ");
		}
	}

	public void addEmployees(Employee emploee) throws BankingException{
		
		String password =  SHAHash.getHash(emploee.getPassword());
	    emploee.setPassword(password);
		dbConnector.addEmployee(emploee);	
	}
	
	public void addBranch(Branch branch) throws BankingException {

        dbConnector.addBranch(branch);	
	}
	
	public void addCustomer(Customer customer,String password) throws BankingException {
		password =  SHAHash.getHash(password);
		customer.setPassword(password);
		dbConnector.addCustomer(customer);
	}
	
	public void addAccount(Account account) throws BankingException{

		dbConnector.addAccount(account);
	}
	public void changePassword(int userId,String newPassword) throws BankingException {
		
		Validation.isvalidPassword(newPassword);
		dbConnector.changePassword(userId, SHAHash.getHash(newPassword));
	}
	
	public List<Long> getAccountNumbers(int userId) throws BankingException {
		return (dbConnector.getAccountNumbers(userId));
		
	}
	
	public void transferMoney(Transaction transaction) throws BankingException {
		
		TransactionDescription description = transaction.getDescription();
		
		long accountNumber = transaction.getAccountNo();
		dbConnector.verifyAccount(transaction.getUserId(),accountNumber);
		
		boolean state = dbConnector.isActive(accountNumber);
		
		if(!state) {
			
			throw new BankingException("Your account is inactive ");
		}
		
		
		long balance = dbConnector.getBalance(accountNumber);
		int amount = transaction.getAmount();
		
		long closingBalance = balance - amount;
		TransactionType transactionType = TransactionType.DEBIT ;
		
		transaction.setStatus(TransactionStatus.SUCCESS);
		transaction.setOpenBalance(balance);
		transaction.setDateTime(System.currentTimeMillis());
	
		if(description != TransactionDescription.DEPOSIT) {
			if(balance < amount) {	
				
				transaction.setType(transactionType);
		        transaction.setCloseBalance(balance);
		        transaction.setStatus(TransactionStatus.FAILED);
		        dbConnector.updateTransaction(transaction);
		        
    			throw new BankingException("Insufficient balance");
   			}
		}
        switch(description) {

        case DEPOSIT:
        	transactionType = TransactionType.CREDIT;
        	closingBalance = balance + amount;
   		  	break;   
   		  	
        case INTRA_BANK:
        	
        	transaction.setType(transactionType);
            transaction.setCloseBalance(closingBalance);
        	dbConnector.updateTransaction(transaction);
        	
        	long receiverAccount = transaction.getTransactionAccNo();
        	
        	transaction.setUserId(dbConnector.getUserId(receiverAccount));
        	
        	long receiverBalance = dbConnector.getBalance(receiverAccount);
        	transaction.setOpenBalance(receiverBalance);
        	
        	transaction.setTransactionAccNo(accountNumber);
        	transaction.setAccountNo(receiverAccount);
        	
        	transactionType = TransactionType.CREDIT;
        	closingBalance = receiverBalance + amount;
        	
        	break;
		default:
			break;
        	
   		}
        transaction.setType(transactionType);
        transaction.setCloseBalance(closingBalance);
        dbConnector.updateTransaction(transaction);
   }
		

	
	public void accountDeactivate(long accountNumber,Status status) throws BankingException {
		dbConnector.setAccountStatus(accountNumber, status);
	}
	
	public void userDeactivate(int userId,Status status) throws BankingException {
		dbConnector.setUserStatus(userId, status);
	}
	
	public JSONObject getAllBranch() throws BankingException {
	
		return JSONConverter.getJson(dbConnector.getAllBranches());
	}
	
	public  JSONObject getCustomerDetails(int userId) throws BankingException {
		
		return JSONConverter.getCustomerJson( dbConnector.getCustomerDetails(userId));
		
	}
	
	public JSONObject getEmployeeDetails(int userId) throws BankingException {
		return JSONConverter.getEmployeeJson(dbConnector.getEmployeeDetails(userId));
	}
	public  JSONObject getAccountDetails(int userId) throws BankingException {
		return JSONConverter.getJson (dbConnector.getAccountDetails(userId));
	}
	
	public JSONObject getAllAccounts(int branchId,int limit,int offset) throws BankingException{
		return JSONConverter.getJson( dbConnector.getAllAccounts(branchId,limit, offset));
	}
	
   public JSONArray getAccountTransaction(TransactionReq requirement) throws BankingException{
	   
	   long accountNumber = requirement.getAccountNumber();
	   dbConnector.verifyAccount(requirement.getUserId(),accountNumber);
	   
	  
	   return JSONConverter.getJsonArray( dbConnector.getTransactionDetail(requirement));
   }
  
   public JSONObject getAccountBalance(int userId,long accountNumber) throws BankingException {
	   dbConnector.verifyAccount(userId,accountNumber);
	   
	   return new JSONObject().put("Balance", dbConnector.getBalance(accountNumber));
   }
   public JSONObject getOverAllBalance(int userId) throws BankingException {
	   return new JSONObject().put("Balance", dbConnector.getOverAllbalance(userId));
	 
   }
		
}
