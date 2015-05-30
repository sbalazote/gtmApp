package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class NullAmountException extends Exception {

	public NullAmountException() {
	}

	public NullAmountException(String message) {
		super(message);
	}

	public NullAmountException(Throwable cause) {
		super(cause);
	}

	public NullAmountException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullAmountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
