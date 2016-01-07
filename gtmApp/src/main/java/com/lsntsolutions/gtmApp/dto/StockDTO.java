package com.lsntsolutions.gtmApp.dto;

import java.io.Serializable;

/**
 * Created by Sebastian on 06/01/2016.
 */
public class StockDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer productId;
    private Integer agreementId;
    private Integer productCode;
    private String productDescription;
    private String agreementDescription;
    private String gtinNumber;
    private String serialNumber;
    private Integer amount;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Integer agreementId) {
        this.agreementId = agreementId;
    }

    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getAgreementDescription() {
        return agreementDescription;
    }

    public void setAgreementDescription(String agreementDescription) {
        this.agreementDescription = agreementDescription;
    }

    public String getGtinNumber() {
        return gtinNumber;
    }

    public void setGtinNumber(String gtinNumber) {
        this.gtinNumber = gtinNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}