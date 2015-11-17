package com.lsntsolutions.gtmApp.dto;

import java.util.ArrayList;
import java.util.List;

public class PrinterResultDTO {

    private String operationId;

    private List<String> deliveryNoteNumbers;

    private List<String> errorMessages;
    private List<String> successMessages;

    public PrinterResultDTO() {
        deliveryNoteNumbers = new ArrayList<String>();
        errorMessages = new ArrayList<String>();
        successMessages = new ArrayList<String>();
    }

    public PrinterResultDTO(String operationId) {
        this.operationId = operationId;
        deliveryNoteNumbers = new ArrayList<String>();
        errorMessages = new ArrayList<String>();
        successMessages = new ArrayList<String>();
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<String> getDeliveryNoteNumbers() {
        return deliveryNoteNumbers;
    }

    public void setDeliveryNoteNumbers(List<String> deliveryNoteNumbers) {
        this.deliveryNoteNumbers = deliveryNoteNumbers;
    }

    public List<String> getSuccessMessages() {
        return successMessages;
    }

    public void setSuccessMessages(List<String> successMessages) {
        this.successMessages = successMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void addErrorMessage(String errorMessage) {
        errorMessages.add(errorMessage);
    }

    public void addSuccessMessage(String successMessage) {
        successMessages.add(successMessage);
    }
}