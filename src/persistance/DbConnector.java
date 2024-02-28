package persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.Account;
import utilities.BankingException;
import utilities.Branch;
import utilities.Customer;
import utilities.Employee;
import utilities.InvalidUserException;
import utilities.Transaction;
import utilities.User;

public class DbConnector implements Connector{
	
	private String url = "jdbc:mysql://localhost:3306/ZBank";
	private String userName = "root";
	private String password = "";
	private final int CREDIT = 1;
	private final int DEBIT = 2;
	
	public String getPassword(int userId) throws BankingException {
		String query = "select PASSWORD from USER_DETAILS where  USER_ID = ? " ;
		String passWord = null;
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1,userId);
			try(ResultSet resultSet = statement.executeQuery()){
				while(resultSet.next()) {
					passWord = resultSet.getString(1);
				}
				return passWord;
			}
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}
	
	public String getRole(int userId) throws BankingException,InvalidUserException {
		String query = "select ROLE from USER_DETAILS where USER_ID = ? " ;
		String role = null;
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1,userId);
			try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next()) {
					role = resultSet.getString(1);
					System.out.println(role);
				}else {
					throw new InvalidUserException("No such user");
				}
				
			}
		
		    return role;
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}
		
		
	
	public void addEmployee(Employee employee,String passWord) throws BankingException {
		addUser(employee,passWord);

		String query = "insert into EMPLOYEE_DETAILS  VALUES(?,?,?)";

		try(Connection connection = DriverManager.getConnection(url, userName, password)){

			PreparedStatement empStatement = connection.prepareStatement(query);
			
			empStatement.setInt(1,getEmpId(employee.getMobile()));
			empStatement.setInt(2, employee.getBranchId());
			empStatement.setString(3, employee.getRole());
			
			empStatement.execute();
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}
	
	
	public void addBranch(Branch branch) throws BankingException {
		String query = "insert into BRANCH_DETAILS (BRANCH_NAME,IFSC_CODE,ADDRESS) values(?,?,?)";
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, branch.getBranchName());
			statement.setLong(2, branch.getIfsc());
			statement.setString(3, branch.getAddress());
			statement.execute();
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		
	}
	
	public void addCustomer(Customer customer,String passWord) throws BankingException {
		addUser(customer,passWord);
		String quary = "insert into CUSTOMER_DETAILS VALUES (?,?,?,?)";
		try(Connection connection = DriverManager.getConnection(url, userName, password)){

			PreparedStatement statement = connection.prepareStatement(quary);
			
			statement.setInt(1,getEmpId(customer.getMobile()));
			statement.setLong(2, customer.getAadhar());
			statement.setString(3, customer.getPan());
			statement.setString(4, customer.getAddress());
			statement.execute();
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		
		
	}

	public void addAccount(Account account) throws BankingException  {
		String query = "insert into ACCOUNT_DETAILS (USER_ID,ACCOUNT_TYPE,BRANCH_ID) VALUES(?,?,?)";
		
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setInt(1,account.getUserId());
			statement.setString(2,account.getAccountType());
			statement.setInt(3,account.getBranchId());
			statement.execute();
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}
	
	public void changePassword(int userId,long passWord) throws BankingException {
		String query = "update USER_DETAILS SET PASSWORD = ? where USER_ID = ?";
		
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1,passWord);
			statement.setInt(2, userId);
			statement.execute();
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}
	
	public long getBalance(long accountNumber) throws BankingException {
		String query = "select BALANCE from ACCOUNT_DETAILS where ACCOUNT_NUMBER = ?";
		long balance = 0;
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1,accountNumber);
			try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next()) {
					balance = resultSet.getLong(1);
				}
				
			}
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		return balance;
		
	}
	
    public Map<Integer,Branch>  getAllBranches() throws BankingException {
		String query = "select * from BRANCH_DETAILS";
		
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			try(ResultSet resultSet = statement.executeQuery()){
				return setBranchInMap(resultSet);
				
			}
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}
    
    

	public Map<Integer, Customer> getCustomersDetails(int limit) throws BankingException {
		String query ="select USER_DETAILS.USER_ID, USER_DETAILS.NAME,USER_DETAILS.MOBILE,USER_DETAILS.EMAIL,USER_DETAILS.AGE,USER_DETAILS.GENDER,USER_DETAILS.STATUS,"
				+ "CUSTOMER_DETAILS.AADHAR_NUMBER,CUSTOMER_DETAILS.PAN_NUMBER,CUSTOMER_DETAILS.ADDRESS\n"
				+ " from USER_DETAILS join CUSTOMER_DETAILS ON USER_DETAILS.USER_ID = CUSTOMER_DETAILS.USER_ID limit ?";
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, limit);
			try(ResultSet resultSet = statement.executeQuery()){
				return setCustomerInMap(resultSet);
			}
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		
		
	}

	public List<Account> getAccountDetails(int userId) throws BankingException {
		String query = "select * from ACCOUNT_DETAILS where STATUS = 'ACTIVE' and USER_ID = ?";
		
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);
			try(ResultSet resultSet = statement.executeQuery()){
			    return	setAccount(resultSet);
			}
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		
	}

	public void transferMoney(Transaction transaction) throws BankingException {
		
		long accountNo = transaction.getAccountNo();
		    System.out.println("transfer money");
			updateTransaction(transaction,DEBIT);
			
			long transactionAcc = transaction.getTransactionAccNo();
			if(getAccountDetail(transactionAcc) != null) {
			    transaction.setAccountNo(transactionAcc);	
			    transaction.setTransactionAccNo(accountNo);
			    updateTransaction(transaction,CREDIT);
			}
		
	}
	
	private void updateTransaction(Transaction transaction,int type) throws BankingException {
		
		String query = "insert into TRANSACTION_DETAILS (DATE_TIME,USER_ID, ACCOUNT_NUMBER ,TRANSACTION_ACCOUNT_N0,"
				+ "AMOUNT,TYPE, DESCRIPTION , OPENING_BALANCE,CLOSING_BALANCE)VALUES(?,?,?,?,?,?,?,?,?)";
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1,transaction.getDateTime());
			System.out.println(transaction.getAccountNo());
			statement.setInt(2,transaction.getUserId());
			long accountNumber = transaction.getAccountNo();
			statement.setLong(3, accountNumber);
			statement.setLong(4, transaction.getTransactionAccNo());
			int transactionAmount = transaction.getAmount();
			statement.setInt(5,transactionAmount);
			
			long balance =  getBalance(accountNumber);
			
			long closingBalance = balance+transactionAmount;
			
			String tranactionType = "CREDIT";
			
			if(type == 2 ) {
				tranactionType = "DEBIT";
				closingBalance = balance-transactionAmount;
				
			}
			statement.setString(6, tranactionType);
			
			statement.setString(7, transaction.getDescription());
			
			statement.setLong(8,balance);
			statement.setLong(9, closingBalance);
			statement.execute();
			updateBalance(accountNumber,closingBalance);
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		
	}
	
	public Account getAccountDetail(long accountNo) throws BankingException {
		String query = "select * from ACCOUNT_DETAILS where ACCOUNT_NUMBER = ? ";
		Account account = new Account();
		
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1,accountNo);
			try(ResultSet resultSet =  statement.executeQuery()){
				if(resultSet.next()) {
					account.setUserId(resultSet.getInt(1));
					account.setAccountNo(resultSet.getLong(2));
					account.setBranchId(resultSet.getInt(3));
					account.setBalance(resultSet.getInt(4));
					account.setAccountType(resultSet.getString(5));
				}
				return account;
			}
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}
	public boolean isActive(long accountNumber) throws BankingException {
		String query = "select STATUS from ACCOUNT_DETAILS where ACCOUNT_NUMBER = ?";
		boolean result = false;
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, accountNumber);
			try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next()) {
					if(resultSet.getString(1) == "ACTIVE") {
						result = true;
					}
				}else {
					throw new BankingException("Account not found ");
				}
				
			}
			return result;
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}
	public void deactivateAccount(long accountNumber) throws BankingException{
		String query = "update ACCOUNT_DETAILS set STATUS = 'INACTIVE' where ACCOUNT_NUMBER = ?";

		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, accountNumber);
			statement.execute();
				
			}catch(SQLException e) {
				throw new BankingException(e.getMessage(),e);
			}
	}
	
	
	private void updateBalance(long accountNumber,long balance) throws BankingException {
		String query ="update ACCOUNT_DETAILS set BALANCE = ? where ACCOUNT_NUMBER = ?";
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, balance);
			statement.setLong(2, accountNumber);
			statement.execute();
				
			}catch(SQLException e) {
				throw new BankingException(e.getMessage(),e);
			}
		}
	
	private List<Account> setAccount(ResultSet resultSet) throws BankingException {
		List<Account> accountList = new ArrayList<Account>();
		try {
			while(resultSet.next()) {
				Account account = new Account();
				int userId = resultSet.getInt(1);
				account.setUserId(userId);
				account.setAccountNo(resultSet.getLong(2));
				account.setBranchId(resultSet.getInt(3));
				account.setBalance(resultSet.getInt(4));
				account.setAccountType(resultSet.getString(5));
			
				accountList.add(account);
			}
			return accountList;
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		
	}
	
	private Map<Integer, Customer> setCustomerInMap(ResultSet resultset) throws BankingException {
		Map<Integer,Customer> customerMap = new HashMap<Integer,Customer>();
		
		try {
			while(resultset.next()) {
				Customer customer = new Customer();
	            int userId = resultset.getInt(1);
	            customer.setName(resultset.getString(2));
	            customer.setMobile(resultset.getLong(3));
	            customer.setEmail(resultset.getString(4));
	            customer.setAge(resultset.getInt(5));
	            customer.setGender(resultset.getString(6));
	            customer.setStatus(resultset.getString(7));
	            customer.setAadhar(resultset.getLong(8));
	            customer.setPan(resultset.getString(9));
	            customer.setAddress(resultset.getString(10));
	            
	            customerMap.put(userId, customer);
	         }
			return customerMap;
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		
		
	}
	
	private Map<Integer,Branch> setBranchInMap(ResultSet resultSet) throws BankingException {
		Map<Integer,Branch> accountMap = new HashMap<Integer,Branch>();
		try {
			while(resultSet.next()) {
				Branch branch = new Branch();
				int branchId = resultSet.getInt(1);
				branch.setBranchId(branchId);
				branch.setBranchName(resultSet.getString(2));
				branch.setIfsc(resultSet.getLong(3));
				branch.setAddress(resultSet.getString(4));
				accountMap.put(branchId,branch);
			}
			return accountMap;
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		
	}
	
	private void addUser(User user,String passWord) throws BankingException {
		
		String query = "insert into USER_DETAILS(NAME,MOBILE,EMAIL,AGE,GENDER,ROLE,PASSWORD) VALUES(?,?,?,?,?,?,?) " ;
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1,user.getName());
			statement.setLong(2,user.getMobile());
			statement.setString(3,user.getEmail());
			statement.setInt(4,user.getAge());
			statement.setString(5,user.getGender());
			statement.setString(6,user.getRole());
			statement.setString(7, passWord);
			
			statement.execute();
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		
		
	}
	
	private int getEmpId(long mobile)  throws BankingException{
		String sql = "select USER_ID from USER_DETAILS where  MOBILE = ? " ;
		int empId = 0;
		try(Connection connection = DriverManager.getConnection(url, userName, password)){
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1,mobile);
			try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next()) {
					empId = resultSet.getInt(1);
				}	
			}
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
		return empId;
	}

}
