package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class NullAgreementIdException extends Exception {

	public NullAgreementIdException() {
	}

	public NullAgreementIdException(String message) {
		super(message);
	}

	public NullAgreementIdException(Throwable cause) {
		super(cause);
	}

	public NullAgreementIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullAgreementIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
