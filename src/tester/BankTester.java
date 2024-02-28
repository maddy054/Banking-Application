package tester;

import java.util.List;
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
import utilities.Type;

public class BankTester {
	Logger logger = Logger.getLogger(BankingTester.class.getName());
	Scanner bankScanner = new Scanner(System.in);
	ZBank zBank = new ZBank();
	
	public void adminAccess() throws BankingException {
		int select = 0;
		
		do {
				
			
		logger.log(Level.INFO,"1. Add a new employee \n 2. Add a new customer \n 3.Create a account for customer \n 4.Get the customers details in particular Branch"
					+ "\n 5.Add new branch details \n 6.Get all the branch details \n 7. Get all account details of a customer \n 8. Deactivate account \n"
					+ " 9. Deactivate user \n 10.Logout ");
			
			
			select = bankScanner.nextInt();
			bankScanner.nextLine();
			switch(select) {
			
			case 1:
				Employee employee = new Employee();
				
				logger.log(Level.INFO, "Enter the name ");
				String name = bankScanner.nextLine();
				
				
				logger.log(Level.INFO, "Enter the email ");
				String email = bankScanner.nextLine();
				
				
				logger.log(Level.INFO, "Enter the mobile no ");
				long mobile = bankScanner.nextLong();
				bankScanner.nextLine();
				
				
				logger.log(Level.INFO, "Enter the age ");
				int age = bankScanner.nextInt();
				bankScanner.nextLine();
			
				
				
				logger.log(Level.INFO, "Enter the gender ");
				String gender = bankScanner.nextLine();
			
				
				logger.log(Level.INFO, "Enter the role ");
				String role = bankScanner.nextLine();
				
					
					employee.setName(name);
					employee.setEmail(email);
					employee.setMobile(mobile);
					employee.setAge(age);
					employee.setGender(gender);
					employee.setRole(role);
					
					logger.log(Level.INFO, "Enter the branch id ");
					
					int branchId = bankScanner.nextInt();
					bankScanner.nextLine();
					employee.setBranchId(branchId);
					
					logger.log(Level.INFO,"create the password ");
					
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
				System.out.println(zBank.getCustomersDetail(12));
				break;
			case 5:
				Branch branch = new Branch();
				
				logger.log(Level.INFO,"Enter the Branch name ");
				String branchName = bankScanner.nextLine();
				branch.setBranchName(branchName);
				
				logger.log(Level.INFO,"Enter the IFSC Code ");
				long ifsc = bankScanner.nextLong();
				bankScanner.nextLine();
				branch.setIfsc(ifsc);
				
				logger.log(Level.INFO,"Enter the address ");
				String address = bankScanner.nextLine();
				branch.setAddress(address);
				
				zBank.addBranch(branch);
				break;
				
			case 6:
				System.out.println(zBank.getAllBranch());
				break;
				
			case 7:
				getAccounts();

				break;
			case 8:
				logger.log(Level.INFO, "Enter the account number ");
				long accNo = bankScanner.nextLong();
				bankScanner.nextLine();
				zBank.accountDeactivate(accNo);
				
				break;
			case 9:
				
				logger.log(Level.INFO,"Enter the userId");
				int userId = bankScanner.nextInt();
				zBank.userDeactivate(userId);
				break;
			}
		  }while(select != 10);

	}
	
	
	public void employeeAccess() throws BankingException {
		int select = 0;
		
		
		do{
				
			
			logger.log(Level.INFO,"1. Add a new customer \n 2. Create a account for customer \n 3. Get all the customers details "
					+ "\n 4. Get all account details of a customer \n 5. Deactivate user \n 6. Deposit cash \n 7. Withdraw cash \n 0.Logout ");
			
			
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
				System.out.println(zBank.getCustomersDetail(12));
				break;
			case 4:
				getAccounts();
				break;
			case 5:
				logger.log(Level.INFO, "Enter the account number ");
				long accNo = bankScanner.nextLong();
				zBank.accountDeactivate(accNo);
				break;
			case 6:
			case 7:
				
				Transaction transaction = new Transaction();
				
				String description = "Cash deposit";
				Type type = Type.DEPOSIT;
			
				if(select == 7) {
					description = "Cash Withdrawl";
					 type = Type.WITHDRAW;
				}
				
				logger.log(Level.INFO,"Enter the userId ");
				 int userId = bankScanner.nextInt();
				 bankScanner.nextLine();
				 transaction.setUserId(userId);
				 
				logger.log(Level.INFO,"Enter the account number ");
				 accNo = bankScanner.nextLong();
				 bankScanner.nextLine();
				 transaction.setAccountNo(accNo);
				 
				 logger.log(Level.INFO,"Enter the amount");
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
		  
		}while(select != 0);
	}
	
	
    public void customerAccess() throws BankingException {
    	int select = 0;
		
    	try (Scanner bankScanner = new Scanner(System.in)){
		do{

		logger.log(Level.INFO,"1. get personal details \n 2.get all account details\n 3.Get transaction details "
					+ "\n 3. Get transaction details of particular account \n 5. get account balance \n 6. money transfer \n 0.Logout ");
			
			
			select = bankScanner.nextInt();
			bankScanner.nextLine();
			switch(select) {
			
			case 6:
			Transaction transaction = new Transaction();
			
			logger.log(Level.INFO,"Enter your account number ");
			transaction.setAccountNo(bankScanner.nextLong());
			bankScanner.nextLine();
			
			logger.log(Level.INFO,"Enter receiver account number ");
			transaction.setTransactionAccNo(bankScanner.nextLong());
			bankScanner.nextLine();
			
			logger.log(Level.INFO,"Enter the amount you want to send ");
			transaction.setAmount(bankScanner.nextInt());
			bankScanner.nextLine();
			
			transaction.setDescription("With in bank transfer ");
			
			zBank.transferMoney(transaction,Type.WITHIN_BANK);
			break;
			
			}
			}while(select != 0);
		}
    	
    }
    private void createAccount() throws BankingException {
    		Account account = new Account();
		
		logger.log(Level.INFO,"Enter the user id ");
		int userId = bankScanner.nextInt();
		bankScanner.nextLine();
		account.setUserId(userId);
		
		logger.log(Level.INFO,"Enter the branch id ");
		int branchId = bankScanner.nextInt();
		bankScanner.nextLine();
		account.setBranchId(branchId);
		
		logger.log(Level.INFO,"Enter the aaccount type ");
		String accountType = bankScanner.nextLine();
		account.setAccountType(accountType);
	    
		zBank.addAccount(account);
 
    }
    
    private void getAccounts() throws BankingException {
    	logger.log(Level.INFO,"Enter the user id ");
		int userId = bankScanner.nextInt();
		bankScanner.nextLine();
		List<Account> accountList = zBank.getAccountDetails(userId);
		for(Account account : accountList) {
			logger.log(Level.INFO,"Account Number is "+account.getAccountNo()); 
			logger.log(Level.INFO,"Account type is "+account.getAccountType()); 
			logger.log(Level.INFO,"Account balance is "+account.getBalance()); 
			logger.log(Level.INFO,"Account branch is "+account.getBranchId());
			logger.log(Level.INFO,"Account type is "+account.getAccountType()); 
			
		}
    }
    
    private void addCoustomer() throws BankingException {
    	
    		
    		Customer customer = new Customer();
    		logger.log(Level.INFO, "Enter the name ");
			String name = bankScanner.nextLine();
			
			
			logger.log(Level.INFO, "Enter the email ");
			String email = bankScanner.nextLine();
			
			
			logger.log(Level.INFO, "Enter the mobile no ");
			long mobile = bankScanner.nextLong();
			bankScanner.nextLine();
			
			
			logger.log(Level.INFO, "Enter the age ");
			int age = bankScanner.nextInt();
			bankScanner.nextLine();
		
			logger.log(Level.INFO, "Enter the gender ");
			String gender = bankScanner.nextLine();
			
			customer.setRole("CUSTOMER");
			customer.setName(name);
			customer.setEmail(email);
			customer.setMobile(mobile);
			customer.setAge(age);
			customer.setGender(gender);
			
			logger.log(Level.INFO, "Enter the Aadhar no ");
			
			long aadhar = bankScanner.nextLong();
			bankScanner.nextLine();
			customer.setAadhar(aadhar);
			
            logger.log(Level.INFO,"Enter the PAN number ");
			
			String panNo = bankScanner.nextLine();
			customer.setPan(panNo);
			

            logger.log(Level.INFO,"Enter the address ");
			
			String address = bankScanner.nextLine();
			customer.setAddress(address);
			logger.log(Level.INFO,"create the password ");
			
			String pass = bankScanner.nextLine();
		   
			zBank.addCustomer(customer,pass);
			
    		
    }
    
}
