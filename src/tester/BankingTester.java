package tester;


import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import banklogicals.ZBank;
import utilities.BankingException;

import utilities.InvalidUserException;

import utilities.WrongPasswordException;


public class BankingTester {
	Logger logger = Logger.getLogger(BankingTester.class.getName());
	
	public static void main(String... args) {

		Logger logger = Logger.getLogger(BankingTester.class.getName());
		try(Scanner bankScanner = new Scanner(System.in)){
			
		
			boolean loop = true;
			while(loop) {

				try{
	
					ZBank zBank = new ZBank();
					
					logger.log(Level.INFO, "Welcome to Z Bank");
				
					
					logger.log(Level.INFO, "Enter the user id: ");
					int username = bankScanner.nextInt();
					bankScanner.nextLine();
					    
					String user =  zBank.getUser(username);
					boolean passwordLoop = true;
					do {
						try {
							logger.log(Level.INFO, "Enter the password ");
							String password = bankScanner.nextLine();

							 zBank.checkPassword(username, password);
							 passwordLoop = false;
						}catch(WrongPasswordException e) {
							e.getMessage();					
						}
					
					}while(passwordLoop);
					
					logger.log(Level.INFO,"welcome to ZBank.\nYou successfully logged in ");
					BankTester bankTester = new BankTester();
					switch(user) {
					
					case "ADMIN":
						 bankTester.adminAccess();
						break;
						
					case "EMPLOYEE":
						bankTester.employeeAccess();
						break;
					
					case "CUSTOMER":
						break;
					}
					
				
				  } catch (BankingException e) {
					logger.log(Level.WARNING,e.getMessage());
				  } catch (InvalidUserException e) {
				
					e.printStackTrace();
				}
			}
			
		}
	}
}


