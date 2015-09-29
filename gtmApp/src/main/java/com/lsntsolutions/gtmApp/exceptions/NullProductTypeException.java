package com.lsntsolutions.gtmApp.exceptions;

@SuppressWarnings("serial")
public class NullProductTypeException extends Exception {

	public NullProductTypeException() {
	}

	public NullProductTypeException(String message) {
		super(message);
	}

	public NullProductTypeException(Throwable cause) {
		super(cause);
	}

	public NullProductTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullProductTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
