package com.lsntsolutions.gtmApp.dto;

/**
 * Created by a983060 on 19/11/2015.
 */
public class ImportStockDTO {
    private Integer agreementId;
    private Integer conceptId;
    private Integer providerId;
    private Integer firstRow;
    private Integer typeColumn;
    private Integer gtinColumn;
    private Integer batchColumn;
    private Integer expirationColumn;
    private Integer serialColumn;
    private Integer amountColumn;

    public Integer getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Integer agreementId) {
        this.agreementId = agreementId;
    }

    public Integer getTypeColumn() {
        return typeColumn;
    }

    public void setTypeColumn(Integer typeColumn) {
        this.typeColumn = typeColumn;
    }

    public Integer getConceptId() {
        return conceptId;
    }

    public void setConceptId(Integer conceptId) {
        this.conceptId = conceptId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(Integer firstRow) {
        this.firstRow = firstRow;
    }

    public Integer getGtinColumn() {
        return gtinColumn;
    }

    public void setGtinColumn(Integer gtinColumn) {
        this.gtinColumn = gtinColumn;
    }

    public Integer getBatchColumn() {
        return batchColumn;
    }

    public void setBatchColumn(Integer batchColumn) {
        this.batchColumn = batchColumn;
    }

    public Integer getExpirationColumn() {
        return expirationColumn;
    }

    public void setExpirationColumn(Integer expirationColumn) {
        this.expirationColumn = expirationColumn;
    }

    public Integer getSerialColumn() {
        return serialColumn;
    }

    public void setSerialColumn(Integer serialColumn) {
        this.serialColumn = serialColumn;
    }

    public Integer getAmountColumn() {
        return amountColumn;
    }

    public void setAmountColumn(Integer amountColumn) {
        this.amountColumn = amountColumn;
    }
}
