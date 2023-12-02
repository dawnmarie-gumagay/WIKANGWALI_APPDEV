package com.wikangwiz.WikangWali.Methods;

public class UsernameAlreadyExistsException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1360539542897627502L;

	public UsernameAlreadyExistsException(String message) {
        super(message);
    }
    
}
