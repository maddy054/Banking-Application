package tester;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import banklogicals.ZBank;

import utilities.Account;
import utilities.BankingException;
import utilities.Branch;
import utilities.Customer;
import utilities.Employee;
import utilities.Transaction;
import utilities.TransactionDetail;
import utilities.TransactionPeriod;
import utilities.TransactionReq;
import utilities.TransactionType;

public class BankTester {
	private Logger logger = Logger.getLogger(BankingTester.class.getName());
	private Scanner bankScanner = new Scanner(System.in);
	private ZBank zBank = new ZBank();
	private int userId; 
	
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void adminAccess() throws BankingException {

		int select = 0;
		
		do {
				
		try {
			System.out.println(zBank.getHash("raghul"));
		System.out.println("1. Add a new employee \n 2. Add a new customer \n 3.Create a account for customer \n 4.Get the customers details in particular Branch"
					+ "\n 5.Add new branch details \n 6.Get all the branch details \n 7. Get all account details of a customer \n 8. Deactivate account \n"
					+ " 9. Deactivate user \n 0.Logout ");
			
 
			select = bankScanner.nextInt();
			bankScanner.nextLine();
			
			switch(select) {
			
			case 1:
				Employee employee = new Employee();
				
				System.out.println( "Enter the name ");
				String name = bankScanner.nextLine();
				
				
				System.out.println( "Enter the email ");
				String email = bankScanner.nextLine();
				
				
				System.out.println( "Enter the mobile no ");
				long mobile = bankScanner.nextLong();
				bankScanner.nextLine();
				
				
				System.out.println( "Enter the age ");
				int age = bankScanner.nextInt();
				bankScanner.nextLine();
			
				
				
				System.out.println( "Enter the gender ");
				String gender = bankScanner.nextLine();
			
				
				System.out.println( "Enter the role ");
				String role = bankScanner.nextLine();
				
					
					employee.setName(name);
					employee.setEmail(email);
					employee.setMobile(mobile);
					employee.setAge(age);
					employee.setGender(gender);
					employee.setRole(role);
					
					System.out.println( "Enter the branch id ");
					
					int branchId = bankScanner.nextInt();
					bankScanner.nextLine();
					employee.setBranchId(branchId);
					
					System.out.println("create the password ");
					
					String pass = bankScanner.nextLine();
				    zBank.addEmployees(employee,pass);
			 
			    break;
			case 2:
				addCoustomer();
				break;
			case 3:
				createAccount();
				break;
				
			case 4:
				
				System.out.println("Enter the userId");
				int userId = bankScanner.nextInt();
				getCustomer(userId);
				
				break;
			case 5:
				Branch branch = new Branch();
				
				System.out.println("Enter the Branch name ");
				String branchName = bankScanner.nextLine();
				branch.setBranchName(branchName);
				
				System.out.println("Enter the IFSC Code ");
				long ifsc = bankScanner.nextLong();
				bankScanner.nextLine();
				branch.setIfsc(ifsc);
				
				System.out.println("Enter the address ");
				String address = bankScanner.nextLine();
				branch.setAddress(address);
				
				zBank.addBranch(branch);
				break;
				
			case 6:
				System.out.println(zBank.getAllBranch());
				break;
				
			case 7:
				System.out.println("Enter the user id ");
				userId = bankScanner.nextInt();
				bankScanner.nextLine();
				getAccounts(userId);

				break;
			case 8:
				System.out.println( "Enter the account number ");
				long accNo = bankScanner.nextLong();
				bankScanner.nextLine();
				zBank.accountDeactivate(accNo);
				
				break;
			case 9:
				
				System.out.println("Enter the userId");
				 userId = bankScanner.nextInt();
				zBank.userDeactivate(userId);
				break;
			}
		

			}catch(BankingException e) {
				logger.log(Level.INFO,e.getMessage());
	
			}
		 }while(select != 0);
	}
	
	public void employeeAccess() throws BankingException {
		
		int select = 0;
		
		do{
			try {
			System.out.println(" 1. Add a new customer \n 2. Create a account for customer \n 3. Get all the customers details "
					+ "\n 4. Get all account details of a customer \n 5. Deactivate user \n 6. Deposit cash \n 7. Withdraw cash \n 0. Logout ");
			
			
			select = bankScanner.nextInt();
			bankScanner.nextLine();
			switch(select) {
			case 1:
				addCoustomer();
				break;
				
			case 2:
				createAccount();
				break;
				
			case 3:
				System.out.println("Enter the userId");
				int userId = bankScanner.nextInt();
				getCustomer(userId);
				break;
				
			case 4:
				System.out.println("Enter the user id ");
				userId = bankScanner.nextInt();
				bankScanner.nextLine();
				getAccounts(userId);
				break;
				
			case 5:
				System.out.println( "Enter the account number ");
				long accNo = bankScanner.nextLong();
				zBank.accountDeactivate(accNo);
				break;
			case 6:
			case 7:
				
				Transaction transaction = new Transaction();
				
				String description = "Cash deposit";
				TransactionType type = TransactionType.DEPOSIT;
			
				if(select == 7) {
					description = "Cash Withdrawl";
					 type = TransactionType.WITHDRAW;
				}
				
				System.out.println("Enter the userId ");
				 userId = bankScanner.nextInt();
				 bankScanner.nextLine();
				 transaction.setUserId(userId);
				 
				System.out.println("Enter the account number ");
				 accNo = bankScanner.nextLong();
				 bankScanner.nextLine();
				 transaction.setAccountNo(accNo);
				 
				 System.out.println("Enter the amount");
				 int amount = bankScanner.nextInt();
				 bankScanner.nextLine();
				 transaction.setAmount(amount);
				 
				 
				 transaction.setDescription(description);
				 
				 try {
				 zBank.transferMoney(transaction,type);
				 }
				 catch(BankingException e) {
					 System.out.println(e.getMessage());
				 }
				 
				break;
			}
			}catch (BankingException e) {
				logger.log(Level.WARNING,e.getMessage());
			}
		  
		}while(select != 0);
	}
	
	
    public void customerAccess() throws BankingException {
    	int select = 0;
		
    	
		do{
			try {
		System.out.println(" 1. Get personal details \n 2. Get all account details\n 3. Get account balance \n 4. Get"
					+ " overall account balance \n 5. Money transfer within bank \n 6. Money transfer with other bank \n 7. Get transaction details "
					+ "\n 8. Get transaction details of particular  \n 0. Logout ");
			TransactionReq requirement = new TransactionReq();
			
			select = bankScanner.nextInt();
			bankScanner.nextLine();
			switch(select) {
			case 1:
				
				getCustomer(this.userId);
				
				break;
			case 2:
				getAccounts(this.userId);
				break;
			
			case 3:
				System.out.println("Enter the account number ");
				long accNo = bankScanner.nextLong();
				bankScanner.nextLine();
				
				long balance = zBank.getAccountBalance(accNo);
				logger.log(Level.INFO,"The account balance is "+balance);
				
				break;
				
			case 4:
				logger.log(Level.INFO,"The over all account balance is "+zBank.getOverAllBalance(this.userId));
				
				break;
			case 5:
			case 6:
			TransactionType type = TransactionType.WITHIN_BANK;
			Transaction transaction = new Transaction();
			transaction.setUserId(this.userId);
			System.out.println("Enter your account number ");
			transaction.setAccountNo(bankScanner.nextLong());
			bankScanner.nextLine();
			
			System.out.println("Enter receiver account number ");
			transaction.setTransactionAccNo(bankScanner.nextLong());
			bankScanner.nextLine();
			
			System.out.println("Enter the amount you want to send ");
			transaction.setAmount(bankScanner.nextInt());
			bankScanner.nextLine();
			String description = "With in bank transfer ";
		
			if(select == 6) {
				description = "Bank to bank transfer";
				type = TransactionType.BANK_TO_BANK;
			}
			transaction.setDescription(description);
			zBank.transferMoney(transaction,type);
			break;
			
			case 7:
			case 8:
				System.out.println(" 1. Get All transaction \n 2. Get all success transaction \n 3. Get all failed transaction "
						+ "\n 4. get all credited transaction \n 5. Get all debited transaction \n 0. Go back");
				TransactionDetail transactionCondition = TransactionDetail.ALL;
				int transactionSwitch = bankScanner.nextInt();
				bankScanner.nextLine();
				
				switch(transactionSwitch) {
				case 2:
					transactionCondition =TransactionDetail.SUCCESS;
					break;
				case 3:
					transactionCondition =TransactionDetail.FAILED;
					break;
				case 4:
					transactionCondition =TransactionDetail.CREDIT;
					break;
				case 5:
					transactionCondition =TransactionDetail.DEBIT;
					break;
				case 0:
					break;
				}
				
				System.out.println(" 1. For this month \n 2. For past one month \n 3. For previous month \n 4 For last 3 months \n 5. For last 6 month \n 0. go back ");
				int timeCondition = bankScanner.nextInt();
				TransactionPeriod period = TransactionPeriod.THIS_MONTH;
				
				switch(timeCondition) {
				case 2:
					period = TransactionPeriod.PAST_MONTH;
					break;
				case 3:
					period = TransactionPeriod.PREVIOUS_MONTH;
					break;
				case 4:
					period = TransactionPeriod.PAST_THREE_MONTH;
					break;
				case 5:
					period = TransactionPeriod.PAST_SIX_MONTH;
					break;
				}
				
				requirement.setForAllAccount(true);
				
				requirement.setLimit(10);
				requirement.setTime(period);
				
				requirement.setType(transactionCondition);
				requirement.setUserId(this.userId);
				
				
				Map<Long,List<Transaction>> transactionMap = zBank.getTransactionDetails(requirement);
				for(long acNo :transactionMap.keySet()) {
					System.out.println("The transaction detail for account number "+acNo);
					printTransaction(transactionMap.get(acNo));
				}
				
				break;

			case 9:
				System.out.println("Enter the account number");
				accNo = bankScanner.nextLong();
				//printTransaction(zBank.getAccountTransaction(requirement));
				break;
			
			}
			}catch(BankingException e) {
				e.printStackTrace();
				logger.log(Level.WARNING,e.getMessage());
			}
			}while(select != 0);
    	
    }

	private void createAccount() throws BankingException {
    		Account account = new Account();
		
		System.out.println("Enter the user id ");
		int userId = bankScanner.nextInt();
		bankScanner.nextLine();
		account.setUserId(userId);
		
		System.out.println("Enter the branch id ");
		int branchId = bankScanner.nextInt();
		bankScanner.nextLine();
		account.setBranchId(branchId);
		
		System.out.println("Enter the account type ");
		String accountType = bankScanner.nextLine();
		account.setAccountType(accountType);
	    
		zBank.addAccount(account);
 
    }
    
    private void getAccounts(int userId) throws BankingException {
    	
		List<Account> accountList = zBank.getAccountDetails(userId);
		for(Account account : accountList) {
			System.out.println("Account Number -  "+account.getAccountNo()); 
			System.out.println("Account type -    "+account.getAccountType()); 
			System.out.println("Account balance - "+account.getBalance()); 
			System.out.println("Account branch -  "+account.getBranchId());
			System.out.println();
			
		}
    }
    
    private void addCoustomer() throws BankingException {
    	
    		
    		Customer customer = new Customer();
    		System.out.println( "Enter the name ");
			String name = bankScanner.nextLine();
			
			
			System.out.println( "Enter the email ");
			String email = bankScanner.nextLine();
			
			
			System.out.println( "Enter the mobile no ");
			long mobile = bankScanner.nextLong();
			bankScanner.nextLine();
			
			
			System.out.println( "Enter the age ");
			int age = bankScanner.nextInt();
			bankScanner.nextLine();
		
			System.out.println( "Enter the gender ");
			String gender = bankScanner.nextLine();
			
			customer.setRole("CUSTOMER");
			customer.setName(name);
			customer.setEmail(email);
			customer.setMobile(mobile);
			customer.setAge(age);
			customer.setGender(gender);
			
			System.out.println( "Enter the Aadhar no ");
			
			long aadhar = bankScanner.nextLong();
			bankScanner.nextLine();
			customer.setAadhar(aadhar);
			
            System.out.println("Enter the PAN number ");
			
			String panNo = bankScanner.nextLine();
			customer.setPan(panNo);
			

            System.out.println("Enter the address ");
			
			String address = bankScanner.nextLine();
			customer.setAddress(address);
			System.out.println("create the password ");
			
			String pass = bankScanner.nextLine();
		   
			zBank.addCustomer(customer,pass);
			
    		
    }
    private void getCustomer(int userId) throws BankingException {
    	Customer customer = zBank.getCustomerDetails(userId);
		System.out.println("User id          - "+customer.getUserId());
		System.out.println("Name of customer - "+customer.getName());
		System.out.println("Mobile number    - "+customer.getMobile());
		System.out.println("Email id         - "+customer.getEmail());
		System.out.println("Age              - "+customer.getAge());
		System.out.println("Gender           - "+customer.getGender());
		System.out.println("Pan number       - "+customer.getPan());
		System.out.println("Aadhar number    - "+customer.getAadhar());
		System.out.println("Address          - "+customer.getAddress());
    }
    
    private void printTransaction(List<Transaction> transactionList) {
    	for(Transaction transaction : transactionList) {
    		System.out.println("User id             "+transaction.getUserId());
    		Instant instant = Instant.ofEpochMilli(transaction.getDateTime());
    		
    		System.out.println("Date & time         "+instant.atZone(ZoneId.systemDefault()));
    		System.out.println("From account number "+transaction.getAccountNo());
    		System.out.println("Transaction id      "+transaction.getTransactionId());
    		System.out.println("Transaction with    "+transaction.getTransactionAccNo());
    		System.out.println("Amount              "+transaction.getAmount());
    		System.out.println("Debit/credit        "+transaction.getType());
    		System.out.println("Description         "+transaction.getDescription());
    		System.out.println("Opening balance     "+transaction.getOpenBalance());
    		System.out.println("Closing balance     "+transaction.getCloseBalance());
    		System.out.println("Status              "+transaction.getStatus()+"\n");
    		
    	}	
    }

}
