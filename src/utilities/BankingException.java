package utilities;

public class BankingException extends Exception{

	private static final long serialVersionUID = 1L;
	public BankingException(String message,Throwable throwable) {
		super(message,throwable);
	}
	public BankingException(String message) {
		super(message);
	}
}
