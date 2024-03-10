package persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.Year;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Account;
import models.BankingException;
import models.Branch;
import models.Customer;
import models.Employee;
import models.Table;
import models.Transaction;
import models.TransactionPeriod;
import models.TransactionReq;
import models.User;
import utilities.InvalidUserException;

public class DbConnector implements Connector {

	private String url = "jdbc:mysql://localhost:3306/ZBank";
	private String userName = "root";
	private String password = "";
	
	
	private Connection getConnection() throws SQLException {

		return DriverManager.getConnection(url, userName, password);
	}	

	
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
			System.out.println(values);
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
			
			queryBuilder.execute(query,branch.getAll().toArray());

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
			QueryBuilder queryBuilder = new QueryBuilder(Table.USER.get());
			String query =  queryBuilder.column(1).buildSelect();
			
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
			String query =  queryBuilder.column(2).buildSelect();
			List<Map<String, Object>> values =queryBuilder.executeQuery(query, userId);
			
			if(values.isEmpty()) {
				throw new BankingException("No account found !! ");
			}
			
			accountNumbers.add((Long) values.get(0).get("ACCOUNT_NUMBER"));
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
		String query = "select SUM(BALANCE) from ACCOUNT_DETAILS where USER_ID = ? ";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new BankingException("You entered a wrong UserId");
				}
				return resultSet.getLong(1);
			}
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public Map<Integer, Branch> getAllBranches() throws BankingException {
		Map<Integer, Branch> branchMap = new HashMap<>();
		
		try (Connection connection = getConnection()) {
			
			QueryBuilder queryBuilder = new QueryBuilder(Table.BRANCH.get());
			String query =  queryBuilder.buildSelect();
			List<Map<String, Object>> values =queryBuilder.executeQuery(query);
			
			for(int i =0;i<values.size();i++) {
				
				Branch branch = new Branch();
				branch.setAll(values.get(i));
				branchMap.put(branch.getBranchId(), branch);
			}
			
			return branchMap;
			
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public Customer getCustomerDetails(int userId) throws BankingException {

		Customer customer = new Customer();
		getUser(customer, userId);

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.CUSTOMER.get());
			String query = queryBuilder.where(1).buildSelect();
			
			List<Map<String, Object>> customerList = queryBuilder.executeQuery(query,userId);
			customer.setAll(customerList.get(0));
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
		return customer;

	}

	public Map<Long, Account> getAccountDetails(int userId) throws BankingException {
		
		Map<Long,Account> accountMap = new HashMap<>();
		
		try (Connection connection = getConnection()) {
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			
			String query = queryBuilder.where(1).buildSelect();
			List<Map<String, Object>> values = queryBuilder.executeQuery(query, userId);
			
			for(Map<String,Object> accounts :values) {
				Account account = new Account();
				account.setAll(accounts);
				accountMap.put(account.getAccountNo(), account);
			}
			return accountMap;
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}
	
	public Map<Integer, Map<Long, Account>> getAllAccounts(int limit, int offset) throws BankingException {

		Map<Integer, Map<Long, Account>> accountMap = new HashMap<>();
		Map<Long,Account> individualAccount = new HashMap<>();
		
		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.ACCOUNTS.get());
			String query = queryBuilder.limit().offset().buildSelect();
		
			for(Map<String, Object> map : queryBuilder.executeQuery(query, limit,offset)) {
				Account account = new Account();
				account.setAll(map);
			
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
			
			queryBuilder.execute(query,transaction.getAll().toArray());
			updateBalance(transaction.getAccountNo(), transaction.getCloseBalance());

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

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
		
		try (Connection connection = getConnection()) {
			
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

	
	
	public Map<Long, List<Transaction>> getTransactionDetail(TransactionReq requirement) throws BankingException {

		

		Map<Long, List<Transaction>> transactionMap = new HashMap<>();

		try (Connection connection = getConnection()) {
			String query = getTransactionQuery(requirement);
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setInt(1, requirement.getUserId());
			setTime(statement, requirement.getTime());
			statement.setInt(4, requirement.getLimit());

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new BankingException("No transaction found!!");
				}
				while (resultSet.next()) {

					long accNo = resultSet.getLong(3);

					Transaction transaction = setTransactionList(resultSet);

					List<Transaction> transactionList = transactionMap.get(accNo);

					if (transactionList == null) {
						transactionList = new ArrayList<>();
					}
					transactionList.add(transaction);
					transactionMap.put(accNo, transactionList);
				}

			}
			return transactionMap;

		} catch (SQLException e) {

			throw new BankingException(e.getMessage(), e);
		}
	}
	

	private String getTransactionQuery(TransactionReq requirement) throws SQLException {
		QueryBuilder queryBuilder = new QueryBuilder(Table.TRANSACTION.get());
		
		
		int column = 7;
		String query1 = "select * from TRANSACTION_DETAILS where USER_ID = ? ";
		String condition = "";

		if (!requirement.isForAllAccount()) {
			query1 = query1 + "and ACCOUNT_NUMBER = " + requirement.getAccountNumber();
		}

		switch (requirement.getType()) {

		case SUCCESS:
			condition = " and STATUS = 'SUCCESS' ";
			break;
		case FAILED:
			condition = " and STATUS = 'FAILED' ";
			break;

		case DEBIT:
			condition = " and TYPE = 'DEBIT' ";
			break;
		case CREDIT:
			condition = " and TYPE = 'CREDIT' ";
			break;

		default:
			break;

		}
		String query = queryBuilder.where(1,column).buildSelect();
		return query + condition + " and DATE_TIME BETWEEN ? and ? limit ?";

	}

	private void setTime(PreparedStatement statement, TransactionPeriod time) throws SQLException {
		ZonedDateTime now = ZonedDateTime.now();
		switch (time) {
		case PAST_MONTH:

			statement.setLong(3, now.toInstant().toEpochMilli());

			statement.setLong(2, now.minusMonths(1).toInstant().toEpochMilli());
			break;

		case THIS_MONTH:

			statement.setLong(3, now.toInstant().toEpochMilli());
			int noOfDays = now.getDayOfMonth();
			statement.setLong(2, now.minusDays(noOfDays).toInstant().toEpochMilli());
			break;

		case PREVIOUS_MONTH:

			ZonedDateTime firstDay = now.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

			boolean isLeap = Year.of(now.getYear()).isLeap();

			int totalDays = firstDay.getMonth().length(isLeap);

			statement.setLong(2, firstDay.toInstant().toEpochMilli());

			statement.setLong(3, now.plusDays(totalDays).toInstant().toEpochMilli());
			break;
		case PAST_THREE_MONTH:
			statement.setLong(3, now.toInstant().toEpochMilli());
			statement.setLong(2, now.minusMonths(3).toInstant().toEpochMilli());
			break;
		case PAST_SIX_MONTH:
			statement.setLong(3, now.toInstant().toEpochMilli());
			statement.setLong(2, now.minusMonths(6).toInstant().toEpochMilli());

			break;
		default:
			break;
		}
	}

	private Transaction setTransactionList(ResultSet resultSet) throws SQLException {

		Transaction transaction = new Transaction();
		transaction.setDateTime(resultSet.getLong(1));
		transaction.setUserId(resultSet.getInt(2));

		transaction.setAccountNo(resultSet.getLong(3));
		transaction.setTransactionId(resultSet.getInt(4));

		transaction.setTransactionAccNo(resultSet.getLong(5));
		transaction.setAmount(resultSet.getInt(6));
		transaction.setType(resultSet.getString(7));

		transaction.setDescription(resultSet.getString(8));
		transaction.setOpenBalance(resultSet.getLong(9));

		transaction.setCloseBalance(resultSet.getLong(10));
		transaction.setStatus(resultSet.getString(11));
		return transaction;

	}


	private void addUser(User user) throws BankingException {
		
		try  {
			QueryBuilder queryBuilder = new QueryBuilder(Table.USER.get());
			
			String query = queryBuilder.column(2,3,4,5,6,7,8,9).buildInsert();
			List<Object> userObj = user.getAll();
			userObj.remove(0);
			queryBuilder.execute(query, userObj.toArray());
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	private void getUser(User user, int userId) throws BankingException {

		try {
			QueryBuilder queryBuilder = new QueryBuilder(Table.USER.get());
			String query = queryBuilder.where(1).buildSelect();
			List<Map<String, Object>> resultList = queryBuilder.executeQuery(query, userId);
			System.out.println(resultList.get(0));
			user.setAllUser(resultList.get(0));
			
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
	
	}

