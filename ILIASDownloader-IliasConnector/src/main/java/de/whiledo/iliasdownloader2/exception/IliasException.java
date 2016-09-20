package de.whiledo.iliasdownloader2.exception;

public class IliasException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IliasException(String message){
		super(message);
	}
	
	public IliasException(String message, Throwable t){
		super(message, t);
	}
	
	public IliasException(Throwable t){
		super(t);
	}
}
