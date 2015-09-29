package com.lsntsolutions.gtmApp.exceptions;

@SuppressWarnings("serial")
public class NullPurchaseOrderNumberException extends Exception {

	public NullPurchaseOrderNumberException() {
	}

	public NullPurchaseOrderNumberException(String message) {
		super(message);
	}

	public NullPurchaseOrderNumberException(Throwable cause) {
		super(cause);
	}

	public NullPurchaseOrderNumberException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullPurchaseOrderNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
