package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class NullBatchException extends Exception {

	public NullBatchException() {
	}

	public NullBatchException(String message) {
		super(message);
	}

	public NullBatchException(Throwable cause) {
		super(cause);
	}

	public NullBatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullBatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
