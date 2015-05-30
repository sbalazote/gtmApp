package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class NullDateException extends Exception {

	public NullDateException() {
	}

	public NullDateException(String message) {
		super(message);
	}

	public NullDateException(Throwable cause) {
		super(cause);
	}

	public NullDateException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullDateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
