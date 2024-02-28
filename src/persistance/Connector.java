package persistance;


import java.util.List;
import java.util.Map;

import utilities.Account;
import utilities.BankingException;
import utilities.Branch;
import utilities.Customer;
import utilities.Employee;
import utilities.InvalidUserException;
import utilities.Transaction;
import utilities.Type;
import utilities.User;

public interface Connector {

	public  String getPassword(int userId) throws BankingException;
	
	public String getRole(int userId) throws BankingException,InvalidUserException ;
	
	public void addEmployee(Employee employee,String password) throws BankingException;
	
	public void addBranch(Branch branch) throws BankingException;
	
	public void addCustomer(Customer customer,String passWord) throws BankingException;
	
	public void addAccount(Account account) throws BankingException  ;
	
	public long getBalance(long accountNumber) throws BankingException ;
	
	public void changePassword(int userId,long password) throws BankingException;
	
	public Map<Integer,Branch>  getAllBranches() throws BankingException;
	
	public Map<Integer, Customer> getCustomersDetails(int limit) throws BankingException;
	
	public List<Account> getAccountDetails(int userId) throws BankingException;
	
	public boolean isActive(long accountNumber) throws BankingException ;
	
	public void transferMoney(Transaction transaction) throws  BankingException;
	
	public void deactivateAccount(long accountNumber) throws BankingException;

}