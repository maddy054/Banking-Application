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

import utilities.Account;
import utilities.BankingException;
import utilities.Branch;
import utilities.Customer;
import utilities.Employee;
import utilities.InvalidUserException;
import utilities.Transaction;
import utilities.TransactionPeriod;
import utilities.TransactionReq;
import utilities.User;

public class DbConnector implements Connector {

	private String url = "jdbc:mysql://localhost:3306/ZBank";
	private String userName = "root";
	private String password = "";

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, userName, password);
	}

	
	public String getPassword(int userId) throws BankingException {
		try {
			String query = "select PASSWORD from USER_DETAILS where USER_ID = ?";
			String passWord = null;

			try (Connection connection = getConnection()) {
				PreparedStatement statement = connection.prepareStatement(query);

				statement.setInt(1, userId);
				try (ResultSet resultSet = statement.executeQuery()) {

					if (resultSet.next()) {

						passWord = resultSet.getString(1);
					}
					return passWord;
				}
			}
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}
	

	public String getRole(int userId) throws BankingException, InvalidUserException {

		String query = "select ROLE from USER_DETAILS where USER_ID = ? ";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new InvalidUserException("No such user");
				}
				return resultSet.getString(1);

			}
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public void addEmployee(Employee employee, String passWord) throws BankingException {
		addUser(employee, passWord);

		String query = "insert into EMPLOYEE_DETAILS  VALUES(?,?,?)";

		try (Connection connection = getConnection()) {

			PreparedStatement empStatement = connection.prepareStatement(query);

			empStatement.setInt(1, getEmpId(employee.getMobile()));
			empStatement.setInt(2, employee.getBranchId());

			empStatement.setString(3, employee.getRole());
			empStatement.execute();

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public void addBranch(Branch branch) throws BankingException {
		String query = "insert into BRANCH_DETAILS (BRANCH_NAME,IFSC_CODE,ADDRESS) values(?,?,?)";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, branch.getBranchName());
			statement.setLong(2, branch.getIfsc());

			statement.setString(3, branch.getAddress());
			statement.execute();

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public void addCustomer(Customer customer, String passWord) throws BankingException {
		addUser(customer, passWord);
		String quary = "insert into CUSTOMER_DETAILS VALUES (?,?,?,?)";

		try (Connection connection = getConnection()) {

			PreparedStatement statement = connection.prepareStatement(quary);

			statement.setInt(1, getEmpId(customer.getMobile()));
			statement.setLong(2, customer.getAadhar());

			statement.setString(3, customer.getPan());
			statement.setString(4, customer.getAddress());
			statement.execute();

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public void addAccount(Account account) throws BankingException {
		String query = "insert into ACCOUNT_DETAILS (USER_ID,ACCOUNT_TYPE,BRANCH_ID) VALUES(?,?,?)";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setInt(1, account.getUserId());
			statement.setString(2, account.getAccountType());

			statement.setInt(3, account.getBranchId());
			statement.execute();

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}
	

	public int getUserId(long accNo) throws BankingException {
		String query = "select USER_ID from ACCOUNT_DETAILS where ACCOUNT_NUMBER = ?";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, accNo);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new BankingException("No such account number ");
				}
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	
	public List<Long> getAccountNumbers(int userId) throws BankingException {
		List<Long> accountNumbers = new ArrayList<>();

		String query = "select ACCOUNT_NUMBER from ACCOUNT_DETAILS where USER_ID = ?";
		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, userId);
			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {
					accountNumbers.add(resultSet.getLong(1));
				}
				return accountNumbers;
			}
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	


	public void changePassword(int userId, String passWord) throws BankingException {
		String query = "update USER_DETAILS SET PASSWORD = ? where USER_ID = ?";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, passWord);
			statement.setInt(2, userId);

			statement.execute();

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public long getBalance(long accountNumber) throws BankingException {
		String query = "select BALANCE from ACCOUNT_DETAILS where ACCOUNT_NUMBER = ?";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setLong(1, accountNumber);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new BankingException("Account not found !!");
				}
				return resultSet.getLong(1);
			}
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
		String query = "select * from BRANCH_DETAILS";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			try (ResultSet resultSet = statement.executeQuery()) {
				return setBranchInMap(resultSet);

			}
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public Customer getCustomerDetails(int userId) throws BankingException {

		Customer customer = new Customer();
		getUser(customer, userId);

		String query = "select * from CUSTOMER_DETAILS where USER_ID = ?";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setInt(1, userId);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					customer.setAadhar(resultSet.getLong(2));
					customer.setPan(resultSet.getString(3));
					customer.setAddress(resultSet.getString(4));
				}

			}
			return customer;
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public Map<Long, Account> getAccountDetails(int userId) throws BankingException {
		String query = "select * from ACCOUNT_DETAILS where USER_ID = ?";
		Map<Long,Account> accountMap = new HashMap<>();
		
		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);

			try (ResultSet resultSet = statement.executeQuery()) {
				while(resultSet.next()) {
					 setAccount(accountMap,resultSet);
				}
				return accountMap;
			}

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}
	
	public Map<Integer, Map<Long, Account>> getAllAccounts(int limit, int offset) throws BankingException {
		String query = "select * from ACCOUNT_DETAILS limit ? offset ?";

		Map<Integer, Map<Long, Account>> accountMap = new HashMap<>();
		Map<Long,Account> individualAccount = new HashMap<>();
		
		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, limit);
			statement.setInt(2, offset);
			try (ResultSet resultSet = statement.executeQuery()) {
				if(!resultSet.next()) {
					throw new BankingException("You reached the end !!");
				}
				
				while (resultSet.next()) {
					int userId = resultSet.getInt(1);
					individualAccount = accountMap.get(userId);	
								
					if(individualAccount == null ) {
						individualAccount = new HashMap<>();
					}
					
					setAccount(individualAccount, resultSet);
					accountMap.put(userId, individualAccount);
					
				}
				return accountMap;
			}
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public void updateTransaction(Transaction transaction) throws BankingException {

		String query = "insert into TRANSACTION_DETAILS (DATE_TIME,USER_ID, ACCOUNT_NUMBER ,TRANSACTION_ACCOUNT_N0,"
				+ "AMOUNT,TYPE, DESCRIPTION , OPENING_BALANCE,CLOSING_BALANCE,STATUS)VALUES(?,?,?,?,?,?,?,?,?,?)";
		
		try (Connection connection = getConnection()) {

			PreparedStatement statement = connection.prepareStatement(query);

			long accountNumber = transaction.getAccountNo();
			long closingBalance = transaction.getCloseBalance();

			statement.setLong(1, transaction.getDateTime());

			statement.setInt(2, transaction.getUserId());
			statement.setLong(3, accountNumber);

			statement.setLong(4, transaction.getTransactionAccNo());
			statement.setInt(5, transaction.getAmount());

			statement.setString(6, transaction.getType());
			statement.setString(7, transaction.getDescription());

			statement.setLong(8, transaction.getOpenBalance());
			statement.setLong(9, closingBalance);
			statement.setString(10, transaction.getStatus());

			statement.execute();
			updateBalance(accountNumber, closingBalance);

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public boolean isActive(long accountNumber) throws BankingException {

		String query = "select STATUS from ACCOUNT_DETAILS where ACCOUNT_NUMBER = ?";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setLong(1, accountNumber);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (!resultSet.next()) {
					throw new BankingException("Account not found ");
				}
				return resultSet.getString(1).equals("ACTIVE");
			}
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public void verifyAccount(int userId, long accountNumber) throws BankingException {
		String query = "select ACCOUNT_NUMBER from ACCOUNT_DETAILS where USER_ID = ? and ACCOUNT_NUMBER = ?";
		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);
			statement.setLong(2, accountNumber);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (!resultSet.next()) {
					throw new BankingException("Wrong userid or account number!! ");
				}
			}
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	public void deactivateAccount(long accountNumber) throws BankingException {
		String query = "update ACCOUNT_DETAILS set STATUS = 'INACTIVE' where ACCOUNT_NUMBER = ?";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setLong(1, accountNumber);
			statement.execute();

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	public Map<Long, List<Transaction>> getTransactionDetail(TransactionReq requirement) throws BankingException {

		String query = getTransactionQuery(requirement);

		Map<Long, List<Transaction>> transactionMap = new HashMap<>();

		try (Connection connection = getConnection()) {
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
	

	private String getTransactionQuery(TransactionReq requirement) {
		String query = "select * from TRANSACTION_DETAILS where USER_ID = ? ";
		String condition = "";

		if (!requirement.isForAllAccount()) {
			query = query + "and ACCOUNT_NUMBER = " + requirement.getAccountNumber();
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

	private void updateBalance(long accountNumber, long balance) throws BankingException {
		String query = "update ACCOUNT_DETAILS set BALANCE = ? where ACCOUNT_NUMBER = ?";

		try (Connection connection = getConnection()) {

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, balance);

			statement.setLong(2, accountNumber);

			statement.execute();

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	private void setAccount(Map<Long,Account> accountMap,ResultSet resultSet) throws SQLException {

			Account account = new Account();

			account.setUserId(resultSet.getInt(1));
			account.setAccountNo(resultSet.getLong(2));
			account.setBranchId(resultSet.getInt(3));
			long accountNumber = resultSet.getInt(4);
			account.setBalance(accountNumber);
			account.setAccountStatus(resultSet.getString(5));
			account.setAccountType(resultSet.getString(6));
			accountMap.put(accountNumber, account);
	}
	

	private Map<Integer, Branch> setBranchInMap(ResultSet resultSet) throws BankingException {
		Map<Integer, Branch> accountMap = new HashMap<Integer, Branch>();
		try {
			
			while (resultSet.next()) {
				Branch branch = new Branch();
				int branchId = resultSet.getInt(1);

				branch.setBranchId(branchId);
				branch.setBranchName(resultSet.getString(2));

				branch.setIfsc(resultSet.getLong(3));
				branch.setAddress(resultSet.getString(4));

				accountMap.put(branchId, branch);
			}
			return accountMap;

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	private void addUser(User user, String passWord) throws BankingException {

		String query = "insert into USER_DETAILS(NAME,MOBILE,EMAIL,AGE,GENDER,ROLE,PASSWORD) VALUES(?,?,?,?,?,?,?) ";
		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, user.getName());
			statement.setLong(2, user.getMobile());

			statement.setString(3, user.getEmail());
			statement.setInt(4, user.getAge());

			statement.setString(5, user.getGender());
			statement.setString(6, user.getRole());

			statement.setString(7, passWord);

			statement.execute();
		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
	}

	private void getUser(User user, int userId) throws BankingException {
		String query = "select * from USER_DETAILS WHERE USER_ID = ?";

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, userId);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {

					user.setUserId(resultSet.getInt(1));
					user.setName(resultSet.getString(3));

					user.setMobile(resultSet.getLong(4));
					user.setEmail(resultSet.getString(5));

					user.setAge(resultSet.getInt(6));
					user.setGender(resultSet.getString(7));

					user.setStatus(resultSet.getString(8));
				}
			}

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}

	}

	private int getEmpId(long mobile) throws BankingException {
		String sql = "select USER_ID from USER_DETAILS where  MOBILE = ? ";
		int empId = 0;

		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, mobile);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					empId = resultSet.getInt(1);
				}
			}

		} catch (SQLException e) {
			throw new BankingException(e.getMessage(), e);
		}
		return empId;
	}
}
