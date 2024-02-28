package banklogicals;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import persistance.Connector;
import persistance.DbConnector;
import utilities.Account;
import utilities.BankingException;
import utilities.Branch;
import utilities.Customer;
import utilities.Employee;
import utilities.InvalidUserException;
import utilities.Transaction;
import utilities.Type;
import utilities.WrongPasswordException;


public class ZBank {
	private Connector dbConnector = new DbConnector();
	private int userId;
	
	public String getUser(int userId) throws BankingException,InvalidUserException {
		return dbConnector.getRole(userId);
        
	}
	
	
	public void checkPassword(int userId, String password) throws BankingException, WrongPasswordException{

			String originalPassword = dbConnector.getPassword(userId);
			String enteredPassword = getHash(password);
			boolean isCorrect = originalPassword.equals(enteredPassword);
			if(!isCorrect) {
				throw new WrongPasswordException("Incorrect password!! try again ");
			}
			this.userId = userId;
		
	}
	/*private void authorizedPerson(String user) throws BankingException, UnAuthorizedUserException {
		if(!role.equals(user)){
			throw new UnAuthorizedUserException();
		}
	}*/
	public void addEmployees(Employee emploee,String password) throws BankingException{
		password = getHash(password);
	
		dbConnector.addEmployee(emploee,password);
		
	}
	
	public void addBranch(Branch branch) throws BankingException {

        dbConnector.addBranch(branch);
		
	}
	
	public void addCustomer(Customer customer,String password) throws BankingException {
		password = getHash(password);
		dbConnector.addCustomer(customer, password);
	}
	
	public void addAccount(Account account) throws BankingException{

		dbConnector.addAccount(account);
	}
	
	public void changePassword(String oldPassword,String newPassword) {
		
	}
	
	public void deposit(Transaction transaction) throws BankingException {
		long accountNumber = transaction.getAccountNo();
		System.out.println("deposit");
		if(dbConnector.isActive(accountNumber)) {
			long balance = dbConnector.getBalance(accountNumber);
			
			if(balance < transaction.getAmount()) {	
				throw new BankingException("Insufficient balance");
			}
			
			transaction.setDateTime(System.currentTimeMillis());
			dbConnector.transferMoney(transaction);
			}
		else {
				throw new BankingException("Your account is inactive ");
			}
	}
	
	public void transferMoney(Transaction transaction) throws BankingException {
		transaction.setDateTime(System.currentTimeMillis());
		
		transaction.setUserId(userId);
		System.out.println(userId);
		dbConnector.transferMoney(transaction);
		
		
	}
	
	public void accountDeactivate(long accountNumber) throws BankingException {
		dbConnector.deactivateAccount(accountNumber);
	}
	public void userDeactivate(int userId) throws BankingException {
		dbConnector.getAccountDetails(userId);
	}
	public Map<Integer, Branch> getAllBranch() throws BankingException {
	
		return dbConnector.getAllBranches();
	}
	
	public Map<Integer, Customer> getCustomersDetail(int limit) throws BankingException {

		return dbConnector.getCustomersDetails(limit);
	}
	public  List<Account> getAccountDetails(int userId) throws BankingException {

		return dbConnector.getAccountDetails(userId);
		
	}

	private String getHash(String password) throws  BankingException {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			 StringBuilder hexString = new StringBuilder(2 * hash.length);
			    for (int i = 0; i < hash.length; i++) {
			        String hex = Integer.toHexString(0xff & hash[i]);
			        if(hex.length() == 1) {
			            hexString.append('0');
			        }
			        hexString.append(hex);
			    }
			    return hexString.toString();
		}catch(NoSuchAlgorithmException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}
		
}
