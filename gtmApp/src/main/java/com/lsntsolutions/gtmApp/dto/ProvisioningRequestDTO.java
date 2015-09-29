package com.lsntsolutions.gtmApp.dto;

import java.io.Serializable;
import java.util.List;

public class ProvisioningRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer agreementId;
	private Integer clientId;
	private Integer deliveryLocationId;
	private Integer logisticsOperatorId;
	private Integer affiliateId;
	private String deliveryDate;
	private String comment;
	private List<ProvisioningRequestDetailDTO> products;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAgreementId() {
		return this.agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

	public Integer getClientId() {
		return this.clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getDeliveryLocationId() {
		return this.deliveryLocationId;
	}

	public void setDeliveryLocationId(Integer deliveryLocationId) {
		this.deliveryLocationId = deliveryLocationId;
	}

	public Integer getLogisticsOperatorId() {
		return this.logisticsOperatorId;
	}

	public void setLogisticsOperatorId(Integer logisticsOperatorId) {
		this.logisticsOperatorId = logisticsOperatorId;
	}

	public Integer getAffiliateId() {
		return this.affiliateId;
	}

	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}

	public String getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<ProvisioningRequestDetailDTO> getProducts() {
		return this.products;
	}

	public void setProducts(List<ProvisioningRequestDetailDTO> products) {
		this.products = products;
	}

}
