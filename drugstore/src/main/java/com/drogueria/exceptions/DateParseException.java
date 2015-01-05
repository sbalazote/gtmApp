package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class DateParseException extends Exception {

	public DateParseException() {
	}

	public DateParseException(String message) {
		super(message);
	}

	public DateParseException(Throwable cause) {
		super(cause);
	}

	public DateParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public DateParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
