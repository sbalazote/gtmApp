package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class NullProductIdException extends Exception {

	public NullProductIdException() {
	}

	public NullProductIdException(String message) {
		super(message);
	}

	public NullProductIdException(Throwable cause) {
		super(cause);
	}

	public NullProductIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullProductIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
