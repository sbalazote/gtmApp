package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class NullInputDetailsException extends Exception {

	public NullInputDetailsException() {
	}

	public NullInputDetailsException(String message) {
		super(message);
	}

	public NullInputDetailsException(Throwable cause) {
		super(cause);
	}

	public NullInputDetailsException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullInputDetailsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
