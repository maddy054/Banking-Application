package com.zbank.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	
	public static boolean isvalidPassword(String password) {
		
		Pattern pattern =getPattern("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,16}$");
		Matcher match = getMatcher(pattern, password);
		return match.matches();	
	}
	public static boolean isValidNumber(String number) {
		Pattern pattern = getPattern("^[7-9]\\d{9}$");
		Matcher match = getMatcher(pattern,number);
		return match.matches();
	}
	public static boolean checkLength(String values,int length) {
	   Pattern pattern = Pattern.compile("^.{1,"+length+"}$");
	   Matcher match = pattern.matcher(values);
	   return match.matches();
	 }
	  
	private static Pattern getPattern(String regEx) {
		return Pattern.compile(regEx);
	}
	private static Matcher getMatcher(Pattern pattern, String match) {
		return pattern.matcher(match);
	}
}
