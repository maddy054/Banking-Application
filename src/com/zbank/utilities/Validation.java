package com.zbank.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	
	public boolean isvalidPassword(String password) {
		
		 Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,16}$");
		  Matcher match = pattern.matcher(password);
		  return match.matches();	
	}
	public boolean isValidNumber(String number) {
		Pattern pattern = getPattern("^[7-9]\\d{9}$");
		Matcher match = getMatcher(pattern,number);
		return match.matches();
	}
	public boolean checkLength(String values) {
	  Pattern pattern = Pattern.compile("^.{1,30}$");
	  Matcher match = pattern.matcher(values);
	  return match.matches();
	  }
	  
	private Pattern getPattern(String regEx) {
		return Pattern.compile(regEx);
	}
	private Matcher getMatcher(Pattern pattern, String match) {
		return pattern.matcher(match);
	}
}
