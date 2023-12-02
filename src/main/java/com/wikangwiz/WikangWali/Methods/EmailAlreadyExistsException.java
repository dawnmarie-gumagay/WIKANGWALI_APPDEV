package com.wikangwiz.WikangWali.Methods;

public class EmailAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4108794688471229284L;

	public EmailAlreadyExistsException(String message) {
        super(message);
    }

}
