package com.lsntsolutions.gtmApp.exceptions;

public class ExistingStockException extends Exception {

    public ExistingStockException() {
    }

    public ExistingStockException(String message) {
        super(message);
    }

    public ExistingStockException(Throwable cause) {
        super(cause);
    }

    public ExistingStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
