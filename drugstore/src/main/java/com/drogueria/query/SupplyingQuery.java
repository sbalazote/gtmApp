package com.drogueria.query;

/**
 * Created by a983060 on 03/06/2015.
 */
public class SupplyingQuery {

    private Integer id;
    private String dateFrom;
    private String dateTo;
    private Integer clientId;
    private Integer affiliateId;
    private Integer agreementId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(Integer affiliateId) {
        this.affiliateId = affiliateId;
    }

    public Integer getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Integer agreementId) {
        this.agreementId = agreementId;
    }


    public static SupplyingQuery createFromParameters(Integer id, String dateFrom, String dateTo, Integer clientId, Integer affiliateId,
                                                   Integer agreementId) {
        SupplyingQuery supplyingQuery = new SupplyingQuery();
        supplyingQuery.id = id;
        supplyingQuery.dateFrom = dateFrom;
        supplyingQuery.dateTo = dateTo;
        supplyingQuery.clientId = clientId;
        supplyingQuery.affiliateId = affiliateId;
        supplyingQuery.agreementId = agreementId;
        return supplyingQuery;
    }
}
