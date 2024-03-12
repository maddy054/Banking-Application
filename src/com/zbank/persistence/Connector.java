package com.zbank.persistence;



import java.util.List;
import java.util.Map;

import com.zbank.exceptions.BankingException;
import com.zbank.models.Account;
import com.zbank.models.Branch;
import com.zbank.models.Customer;
import com.zbank.models.Employee;
import com.zbank.models.Transaction;
import com.zbank.models.TransactionReq;
import com.zbank.utilities.InvalidUserException;


public interface Connector {

	public  String getPassword(int userId) throws  BankingException;
	
	public String getRole(int userId) throws InvalidUserException, BankingException ;
	
	public void addEmployee(Employee employee) throws BankingException;
	
	public void addBranch(Branch branch) throws BankingException;
	
	public void addCustomer(Customer customer) throws BankingException;
	
	public void addAccount(Account account) throws BankingException  ;
	
	public int getUserId(long accNo) throws BankingException;
	
	public List<Long> getAccountNumbers(int userId) throws BankingException;
	
	public long getBalance(long accountNumber) throws BankingException ;
	
	public void changePassword(int userId,String password) throws BankingException;
	
	public Map<Integer,Branch> getAllBranches() throws BankingException;
	
	public Customer getCustomerDetails(int userId) throws BankingException;
	
	public  Map<Long,Account> getAccountDetails(int userId) throws BankingException;
	
	public Map<Integer,Map<Long,Account>> getAllAccounts(int limit,int offset) throws BankingException;
	
	public boolean isActive(long accountNumber) throws BankingException ;
	
	public Map<Long, List<Transaction>> getTransactionDetail(TransactionReq requirement) throws BankingException;

	
	public void updateTransaction(Transaction transaction) throws BankingException;
	
	public void deactivateAccount(long accountNumber) throws BankingException;
	
	public void verifyAccount(int userId,long accountNumber) throws BankingException ;

	public long getOverAllbalance(int userId) throws BankingException;

	public Employee getEmployeeDetails(int userId) throws BankingException; 

}
