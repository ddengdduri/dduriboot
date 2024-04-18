package com.example.myrestfulservice.exception;

public class UserNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8479420742412116180L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
