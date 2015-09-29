package com.lsntsolutions.gtmApp.exceptions;

@SuppressWarnings("serial")
public class NullDeliveryLocationIdException extends Exception {

	public NullDeliveryLocationIdException() {
	}

	public NullDeliveryLocationIdException(String message) {
		super(message);
	}

	public NullDeliveryLocationIdException(Throwable cause) {
		super(cause);
	}

	public NullDeliveryLocationIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullDeliveryLocationIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
