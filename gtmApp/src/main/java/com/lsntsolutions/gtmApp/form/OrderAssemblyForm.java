package com.lsntsolutions.gtmApp.form;

/**
 * Created by a983060 on 23/12/2015.
 */
public class OrderAssemblyForm {

    String stockInput;
    String stockAmountInput;
    Integer amount;
    Integer assignAmount;
    Integer provisioningRequestDetailId;

    public String getStockInput() {
        return stockInput;
    }

    public void setStockInput(String stockInput) {
        this.stockInput = stockInput;
    }

    public String getStockAmountInput() {
        return stockAmountInput;
    }

    public void setStockAmountInput(String stockAmountInput) {
        this.stockAmountInput = stockAmountInput;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAssignAmount() {
        return assignAmount;
    }

    public void setAssignAmount(Integer assignAmount) {
        this.assignAmount = assignAmount;
    }

    public Integer getProvisioningRequestDetailId() {
        return provisioningRequestDetailId;
    }

    public void setProvisioningRequestDetailId(Integer provisioningRequestDetailId) {
        this.provisioningRequestDetailId = provisioningRequestDetailId;
    }
}
