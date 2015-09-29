package com.lsntsolutions.gtmApp.exceptions;

@SuppressWarnings("serial")
public class NullProviderIdException extends Exception {

	public NullProviderIdException() {
	}

	public NullProviderIdException(String message) {
		super(message);
	}

	public NullProviderIdException(Throwable cause) {
		super(cause);
	}

	public NullProviderIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullProviderIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
