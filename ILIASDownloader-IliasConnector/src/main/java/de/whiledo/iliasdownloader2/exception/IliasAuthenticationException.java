package de.whiledo.iliasdownloader2.exception;

public class IliasAuthenticationException extends IliasException {

	private static final long serialVersionUID = 1L;

	public IliasAuthenticationException(String message){
		super(message);
	}
	
	public IliasAuthenticationException(String message, Throwable t){
		super(message, t);
	}
	
	public IliasAuthenticationException(Throwable t){
		super(t);
	}
}
