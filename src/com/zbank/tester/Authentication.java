package com.zbank.tester;


import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.zbank.enums.UserType;
import com.zbank.exceptions.BankingException;
import com.zbank.exceptions.InvalidUserException;
import com.zbank.exceptions.WrongPasswordException;
import com.zbank.logics.ZBank;


public class Authentication {

	
	public static void main(String... args) throws SQLException {

		Logger logger = Logger.getLogger(Authentication.class.getName());
		try(Scanner bankScanner = new Scanner(System.in)){
			
		
			boolean loop = true;
			while(loop) {

				try{
				
					ZBank zBank = new ZBank();

					System.out.println( "Welcome to Z Bank");

					System.out.println( "Enter the user id: ");
					int userId = bankScanner.nextInt();
					bankScanner.nextLine();

					UserType user = zBank.getUser(userId);
					boolean passwordLoop = true;
					do {
						try {
							System.out.println( "Enter the password ");
							String password = bankScanner.nextLine();
							JSONObject json = new JSONObject();
							json.put("user_id", userId);
							json.put("password", password);
							
							zBank.checkPassword(json);
							
							passwordLoop = false;
						} catch (WrongPasswordException e) {
							logger.log(Level.WARNING,e.getMessage());
						}

					} while (passwordLoop);

					System.out.println( "welcome to ZBank.\nYou successfully logged in ");
					Authorization bankTester = new Authorization();
					bankTester.setUserId(userId);
					switch (user.ordinal()) {

					case 0:
						 bankTester.adminAccess();
						break;
						
					case 1:
						bankTester.employeeAccess();
						break;
					
					case 2:
						bankTester.customerAccess();
						break;
					}
					
				
				  } catch (BankingException e) {
					logger.log(Level.WARNING,e.getMessage());
				
				  } catch (InvalidUserException e) {
					  logger.log(Level.INFO,e.getMessage());
				} catch (WrongPasswordException e) {
					  logger.log(Level.INFO,e.getMessage());
				}
			}
			
		}
	}
}


