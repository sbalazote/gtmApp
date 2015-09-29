package com.lsntsolutions.gtmApp.exceptions;

@SuppressWarnings("serial")
public class NullDeliveryNoteNumberException extends Exception {

	public NullDeliveryNoteNumberException() {
	}

	public NullDeliveryNoteNumberException(String message) {
		super(message);
	}

	public NullDeliveryNoteNumberException(Throwable cause) {
		super(cause);
	}

	public NullDeliveryNoteNumberException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullDeliveryNoteNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
