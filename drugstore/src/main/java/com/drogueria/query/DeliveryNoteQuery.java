package com.drogueria.query;

public class DeliveryNoteQuery {

	private Integer affiliateId;
	private String deliveryNoteNumber;
	private Integer provisioningRequestId;
	private Integer deliveryLocationId;
	private Integer providerId;
	private Integer agreementId;
    private Integer clientId;

	public Integer getAffiliateId() {
		return this.affiliateId;
	}

	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}

	public String getDeliveryNoteNumber() {
		return this.deliveryNoteNumber;
	}

	public void setDeliveryNoteNumber(String deliveryNoteNumber) {
		this.deliveryNoteNumber = deliveryNoteNumber;
	}

	public Integer getProvisioningRequestId() {
		return this.provisioningRequestId;
	}

	public void setProvisioningRequestId(Integer provisioningRequestId) {
		this.provisioningRequestId = provisioningRequestId;
	}

	public Integer getDeliveryLocationId() {
		return this.deliveryLocationId;
	}

	public void setDeliveryLocationId(Integer deliveryLocationId) {
		this.deliveryLocationId = deliveryLocationId;
	}

	public Integer getProviderId() {
		return this.providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

	public Integer getAgreementId() {
		return this.agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}
