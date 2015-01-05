package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class NullConceptIdException extends Exception {

	public NullConceptIdException() {
	}

	public NullConceptIdException(String message) {
		super(message);
	}

	public NullConceptIdException(Throwable cause) {
		super(cause);
	}

	public NullConceptIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullConceptIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
