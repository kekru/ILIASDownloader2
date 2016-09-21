package de.whiledo.iliasdownloader2.exception;

public class IliasHTTPSException extends IliasException {

	private static final long serialVersionUID = 1L;

	public IliasHTTPSException(String message){
		super(message);
	}
	
	public IliasHTTPSException(String message, Throwable t){
		super(message, t);
	}
	
	public IliasHTTPSException(Throwable t){
		super(t);
	}
}
