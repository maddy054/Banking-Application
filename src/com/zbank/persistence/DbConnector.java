package com.zbank.persistence;

import java.sql.SQLException;

import java.time.Year;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zbank.enums.Table;
import com.zbank.enums.TransactionDetail;
import com.zbank.enums.TransactionPeriod;
import com.zbank.exceptions.BankingException;
import com.zbank.models.Account;
import com.zbank.models.Branch;
import com.zbank.models.Customer;
import com.zbank.models.Employee;
import com.zbank.models.Transaction;
import com.zbank.models.TransactionReq;
import com.zbank.models.User;
import com.zbank.utilities.InvalidUserException;
import com.zbank.utilities.QueryBuilder;

public class DbConnector implements Connector {

	public String getPassword(int userId) throws BankingException {
		try {
			
			QueryBuilder queryBuilder = new QueryBuilder(Table.USER.get());
			String query =  queryBuilder.column(2).where(1).buildSelect();
			
			List<Map<String, Object>> values = queryBuilder.executeQuery(query, userId);
		
			return (String) values.get(0).get("PASSWORD");
		
		}catch(SQLException e) {
			throw new BankingException(e.getMessage());
		
		}
	}

	public String getRole(int userId) throws InvalidUserException, BankingException {
		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.USER.get());
			String query =  queryBuilder.column(9).where(1).buildSelect();
			
			List<Map<String, Object>> values = queryBuilder.executeQuery(query, userId);
			return (String) values.get(0).get("ROLE");
		}catch (SQLException e) {	
			throw new BankingException(e.getMessage());
		}
	}

	public void addEmployee(Employee employee) throws BankingException {
		addUser(employee);
		
		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.EMPLOYEE.get());
			String query =  queryBuilder.buildInsert();
		    queryBuilder.execute(query,	getUsersId(employee.getMobile()),employee.getBranchId(),employee.getRole());
		    
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}
	
	public void addCustomer(Customer customer) throws BankingException {
		addUser(customer);
		
		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.CUSTOMER.get());
			String query =  queryBuilder.buildInsert();
			queryBuilder.execute(query, getUsersId(customer.getMobile()),customer.getAadhar(),customer.getPan(),customer.getAddress());
			
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public void addBranch(Branch branch) throws BankingException {

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.BRANCH.get());
			String query =  queryBuilder.column(2,3,4).buildInsert();
			List<Object> values = new ArrayList<Object>();
			values.add(branch.getBranchName());
			values.add(branch.getIfsc());
			values.add(branch.getAddress());
			
			queryBuilder.execute(query,values.toArray());
			
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}
	
	public void addAccount(Account account) throws BankingException {

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query =  queryBuilder.column(1,3,6).buildInsert();
			
			queryBuilder.execute(query, account.getUserId(),account.getBranchId(),account.getAccountType());
			
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}
	

	public int getUserId(long accNo) throws BankingException {

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query =  queryBuilder.column(1).where(2).buildSelect();
			
			List<Map<String, Object>> values =queryBuilder.executeQuery(query, accNo);
			
			if(values.isEmpty()) {
				throw new BankingException("No such account number");
			}
			return (int) values.get(0).get("USER_ID");
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	
	public List<Long> getAccountNumbers(int userId) throws BankingException {
		List<Long> accountNumbers = new ArrayList<>();
		
		try{
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query =  queryBuilder.column(2).where(1).buildSelect();
			List<Map<String, Object>> values =queryBuilder.executeQuery(query, userId);
			
			if(values.isEmpty()) {
				throw new BankingException("No account found !! ");
			}
			for(Map<String,Object> map : values) {
				accountNumbers.add((Long) map.get("ACCOUNT_NUMBER"));
			}
			
			return accountNumbers;
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public void changePassword(int userId, String passWord) throws BankingException {

		try{
			QueryBuilder queryBuilder = new QueryBuilder(Table.USER.get());
			String query =  queryBuilder.column(2).where(1).buildUpdate();
			queryBuilder.execute(query, passWord,userId);

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public long getBalance(long accountNumber) throws BankingException {

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query =  queryBuilder.column(4).where(2).buildSelect();
			
			List<Map<String, Object>> values =queryBuilder.executeQuery(query, accountNumber);
			
			if(values.isEmpty()) {
				throw new BankingException("Account number not found!!");
			}
			
			return (long) values.get(0).get("BALANCE");
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public long getOverAllbalance(int userId) throws BankingException {

		try  {
			long balance =0;
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query = queryBuilder.column(4).where(1).buildSelect();
			
			List<Map<String,Object>> resultList = queryBuilder.executeQuery(query, userId);
			for(Map<String,Object> map:resultList) {
				balance = balance + (long)map.get("BALANCE");
			}
			return balance;
			
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public Map<Integer, Branch> getAllBranches() throws BankingException {
		Map<Integer, Branch> branchMap = new HashMap<>();
		
		try {
			
			QueryBuilder queryBuilder = new QueryBuilder(Table.BRANCH.get());
			String query =  queryBuilder.buildSelect();
			List<Map<String, Object>> values =queryBuilder.executeQuery(query);
			
			for(int i =0;i<values.size();i++) {
				
				Branch branch = new Branch();
				
				setBranch(branch,values.get(i));
				branchMap.put(branch.getBranchId(), branch);
			}
			
			return branchMap;
			
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	private void setBranch(Branch branch,Map<String, Object> map) {
		branch.setBranchId((int) map.get("BRANCH_ID"));
		branch.setBranchName((String) map.get("BRANCH_NAME"));
		branch.setIfsc((long) map.get("IFSC_CODE"));
		branch.setAddress((String) map.get("ADDRESS"));
		
	}

	public Customer getCustomerDetails(int userId) throws BankingException {

		Customer customer = new Customer();
		getUser(customer, userId);

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.CUSTOMER.get());
			String query = queryBuilder.where(1).buildSelect();
			
			List<Map<String, Object>> customerList = queryBuilder.executeQuery(query,userId);
			setCustomer(customer,customerList.get(0));
			
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
		return customer;

	}
	
	private void setCustomer(Customer customer, Map<String, Object> map) {
		customer.setAadhar((long) map.get("AADHAR_NUMBER"));
		customer.setPan((String) map.get("PAN_NUMBER"));
		customer.setAddress((String) map.get("ADDRESS"));
		
	}

	public Employee getEmployeeDetails(int userId) throws BankingException{
		Employee employee = new Employee();
		getUser(employee, userId);
		
		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.EMPLOYEE.get());
			String query = queryBuilder.where(1).buildSelect();
			int branchId = (int) queryBuilder.executeQuery(query, userId).get(0).get("BRANCH_ID");
			employee.setBranchId(branchId);
			
			return employee;
		}catch(SQLException e) {
			throw new BankingException(e.getMessage(),e);
		}
	}


	public Map<Long, Account> getAccountDetails(int userId) throws BankingException {
		
		Map<Long,Account> accountMap = new HashMap<>();
		
		try{
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			
			String query = queryBuilder.where(1).buildSelect();
			List<Map<String, Object>> values = queryBuilder.executeQuery(query, userId);
			
			for(Map<String,Object> value :values) {
				Account account = new Account();
				setAccounts(account,value);
				accountMap.put(account.getAccountNo(), account);
			}
			return accountMap;
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}
	
	private void setAccounts(Account account, Map<String, Object> map) {
		account.setUserId((int) map.get("USER_ID"));
		account.setAccountNo((long) map.get("ACCOUNT_NUMBER"));
		account.setBranchId((int) map.get("BRANCH_ID"));
		account.setBalance((long) map.get("BALANCE"));
		account.setAccountStatus((String) map.get("STATUS"));
		account.setAccountType((String) map.get("ACCOUNT_TYPE"));
		
	}

	public Map<Integer, Map<Long, Account>> getAllAccounts(int limit, int offset) throws BankingException {

		Map<Integer, Map<Long, Account>> accountMap = new HashMap<>();
		Map<Long,Account> individualAccount = new HashMap<>();
		
		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query = queryBuilder.limit().offset().buildSelect();
		
			for(Map<String, Object> map : queryBuilder.executeQuery(query, limit,offset)) {
				Account account = new Account();
				setAccounts(account,map);
			
				individualAccount = accountMap.get(account.getBranchId());
				
				if(individualAccount == null) {
					individualAccount = new HashMap<>();
					accountMap.put(account.getBranchId(), individualAccount);
				}
				individualAccount.put(account.getAccountNo(), account);
			}
				return accountMap;
			
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public void updateTransaction(Transaction transaction) throws BankingException {
		
		try {
	
			QueryBuilder queryBuilder = new QueryBuilder(Table.TRANSACTION.get());
			String query = queryBuilder.column(1,2,3,5,6,7,8,9,10,11).buildInsert();
			
			queryBuilder.execute(query,getTransactionValues(transaction).toArray());
			
			updateBalance(transaction.getAccountNo(), transaction.getCloseBalance());

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	private List<Object> getTransactionValues(Transaction transaction) {
		List<Object> values = new ArrayList<Object>();
		values.add(transaction.getDateTime());
		values.add(transaction.getUserId());
		values.add(transaction.getAccountNo());
		values.add(transaction.getTransactionAccNo());
		values.add(transaction.getAmount());
		values.add(transaction.getType());
		values.add(transaction.getDescription());
		values.add(transaction.getOpenBalance());
		values.add(transaction.getCloseBalance());
		values.add(transaction.getStatus());
		
		return values;
	}

	private void updateBalance(long accNo,long balance) throws SQLException, BankingException {
		QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
		String query = queryBuilder.column(4).where(2).buildUpdate();
		
		queryBuilder.execute(query, balance, accNo);
	}
	
	public boolean isActive(long accountNumber) throws BankingException {

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query = queryBuilder.column(5).where(2).buildSelect();
			
			List<Map<String, Object>> resultList = queryBuilder.executeQuery(query, accountNumber);
			return resultList.get(0).get("STATUS").equals("ACTIVE");
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public void verifyAccount(int userId, long accountNumber) throws BankingException {
		
		try {
			
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query = queryBuilder.column(2).where(1,2).buildSelect();
			
			if (queryBuilder.executeQuery(query, userId,accountNumber).isEmpty()) {
				throw new BankingException("Wrong userid or account number!! ");
			}
		
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public void deactivateAccount(long accountNumber) throws BankingException {

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query =  queryBuilder.column(5).where(2).buildUpdate();
			queryBuilder.execute(query, "INACTIVE",accountNumber);

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	
	
	public Map<Long, List<Transaction>> getTransactionDetail(TransactionReq req) throws BankingException {

		

		Map<Long, List<Transaction>> transactionMap = new HashMap<>();
		List<Transaction> transactionList = new ArrayList<>();
		Transaction transaction = new Transaction();
		
		try  {
			String query = new String();
			
			QueryBuilder queryBuilder = new QueryBuilder(Table.TRANSACTION.get());
			
			List<Map<String,Object>> resultList = new ArrayList<>();
			long[] time = getTime(req.getTime());
			
			if(req.isForAllAccount()) {
				query = getTransactionQuery(req);
				resultList = queryBuilder.executeQuery(query, req.getUserId(),req.getType().get(),time[0],time[1],req.getLimit(),0);
			}
			else {
				query = getAccTransactionQuery(req);
				resultList = queryBuilder.executeQuery(query, req.getUserId(),req.getAccountNumber(),req.getType().get(),time[0],time[1],req.getLimit(),0);
			}
			
			for(Map<String,Object> transactions : resultList) {
				setTransaction(transaction,transactions);
				transactionList = transactionMap.get(transaction.getAccountNo());
				
				if(transactionList == null) {
					transactionList = new ArrayList<>();
				}
				
				transactionList.add(transaction);
				transactionMap.put(transaction.getAccountNo(),transactionList);
			}
		
			return transactionMap;

		} catch (SQLException e) {

			throw new BankingException(e.getMessage(), e);
		}
	}
	private void setTransaction(Transaction transaction, Map<String, Object> transactions) {
		transaction.setDateTime((long) transactions.get("DATE_TIME"));
		transaction.setUserId((int) transactions.get("USER_ID"));
		transaction.setAccountNo((long) transactions.get("ACCOUNT_NUMBER"));
		transaction.setTransactionId((long) transactions.get("TRANSACTION_ID"));
		transaction.setTransactionAccNo((long) transactions.get("TRANSACTION_ACCOUNT_N0"));
		transaction.setAmount((int) transactions.get("AMOUNT"));
		transaction.setType((String) transactions.get("TYPE"));
		transaction.setDescription((String) transactions.get("DESCRIPTION"));
		transaction.setOpenBalance((long) transactions.get("OPENING_BALANCE"));
		transaction.setCloseBalance((long) transactions.get("CLOSING_BALANCE"));
		transaction.setStatus((String) transactions.get("STATUS"));
		
	}

	private String getTransactionQuery(TransactionReq requirement) throws SQLException {
		
		QueryBuilder queryBuilder = new QueryBuilder(Table.TRANSACTION.get());
		int column = 7;
		
		if(requirement.getType() == TransactionDetail.SUCCESS || requirement.getType() == TransactionDetail.FAILED) {
			 column =11;	
			}
		return queryBuilder.where(2,column).between(1).limit().offset().buildSelect();
		
	}

	private String getAccTransactionQuery(TransactionReq requirement) throws SQLException {
		QueryBuilder queryBuilder = new QueryBuilder(Table.TRANSACTION.get());
		int column = 7;

		if(requirement.getType() == TransactionDetail.SUCCESS || requirement.getType() == TransactionDetail.FAILED) {
		 column =11;	
		}

		return queryBuilder.where(2,3,column).between(1).limit().offset().buildSelect();
		

	}

	private long[] getTime(TransactionPeriod time) throws SQLException {
		ZonedDateTime fromTime = ZonedDateTime.now();
		ZonedDateTime toTime = ZonedDateTime.now();

		switch (time) {
		case LAST_30_DAYS:
			fromTime = fromTime.minusMonths(1);
			break;
			
		case THIS_MONTH:
			fromTime = fromTime.minusDays(toTime.getDayOfMonth());
			break;
			
		case PREVIOUS_MONTH:
			fromTime = fromTime.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
			boolean isLeap = Year.of(fromTime.getYear()).isLeap();

			int totalDays = fromTime.getMonth().length(isLeap);
			toTime = toTime.minusDays(totalDays);
			break;
			
		case PAST_THREE_MONTH:
			fromTime = fromTime.minusMonths(3);
			break;
			
		case PAST_SIX_MONTH:
			fromTime = fromTime.minusMonths(6);
			break;
		}
		
		long[] result = {fromTime.toInstant().toEpochMilli(),toTime.toInstant().toEpochMilli()};
		return  result;

	}
	
	private void addUser(User user) throws BankingException {
		
		try  {
			QueryBuilder queryBuilder = new QueryBuilder(Table.USER.get());
			
			String query = queryBuilder.column(2,3,4,5,6,7,8,9).buildInsert();
			List<Object> userObj = getUserDetails(user);
			userObj.remove(0);
			System.out.println(queryBuilder.executeQuery(query, userObj.toArray()).get(0).get("LAST_INSERT_ID()"));
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}
	private int getUsersId(long mobile) throws BankingException {

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.USER.get());
			String query = queryBuilder.column(1).where(4).buildSelect();
			List<Map<String,Object>> resultList = queryBuilder.executeQuery(query, mobile);
			
			if(resultList.isEmpty()) {
				throw new BankingException("User not found");
			}
			return (int) resultList.get(0).get("USER_ID");
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}
	private void getUser(User user, int userId) throws BankingException {

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.USER.get());
			String query = queryBuilder.where(1).buildSelect();
			List<Map<String, Object>> resultList = queryBuilder.executeQuery(query, userId);
	
			setUser(user,resultList.get(0));
			
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	private void setUser(User user, Map<String, Object> map) {
		user.setUserId((int) map.get("USER_ID"));
		user.setName((String) map.get("NAME"));
		user.setMobile((long) map.get("MOBILE"));
		user.setEmail((String) map.get("EMAIL"));
		user.setAge((int) map.get("AGE"));
		user.setGender((String) map.get("GENDER"));
		user.setStatus((String) map.get("STATUS"));
		
	}
	public List<Object> getUserDetails(User user) {
	List<Object> customer = new ArrayList<Object>();
		
		customer.add(user.getUserId());
		customer.add(user.getPassword());
		customer.add(user.getName());
		customer.add(user.getMobile());
		customer.add(user.getEmail());
		customer.add(user.getAge());
		customer.add(user.getGender());
		customer.add(user.getStatus());
		customer.add(user.getRole());
		return customer; 
		
	}


	
}

