package utilities;

public class WrongPasswordException extends BankingException{

	private static final long serialVersionUID = 1L;
	
	public WrongPasswordException(String message) {
		super(message);
	}

}
