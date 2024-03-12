package com.zbank.exceptions;

public class UnAuthorizedUserException extends Exception{
	private static final long serialVersionUID = 1L;

	public UnAuthorizedUserException() {
		super("You dont have access to this!! ");
	}
}
