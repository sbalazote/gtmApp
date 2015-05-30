package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class NullSerialNumberException extends Exception {

	public NullSerialNumberException() {
	}

	public NullSerialNumberException(String message) {
		super(message);
	}

	public NullSerialNumberException(Throwable cause) {
		super(cause);
	}

	public NullSerialNumberException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullSerialNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
