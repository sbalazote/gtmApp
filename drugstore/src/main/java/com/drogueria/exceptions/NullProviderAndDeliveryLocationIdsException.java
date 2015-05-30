package com.drogueria.exceptions;

@SuppressWarnings("serial")
public class NullProviderAndDeliveryLocationIdsException extends Exception {

	public NullProviderAndDeliveryLocationIdsException() {
	}

	public NullProviderAndDeliveryLocationIdsException(String message) {
		super(message);
	}

	public NullProviderAndDeliveryLocationIdsException(Throwable cause) {
		super(cause);
	}

	public NullProviderAndDeliveryLocationIdsException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullProviderAndDeliveryLocationIdsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
