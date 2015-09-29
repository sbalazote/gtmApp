package com.lsntsolutions.gtmApp.dto;

import java.io.Serializable;
import java.util.List;

public class SupplyingDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer clientId;
	private Integer affiliateId;
	private Integer agreementId;
	private String date;
	private List<SupplyingDetailDTO> supplyingDetails;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClientId() {
		return this.clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getAffiliateId() {
		return this.affiliateId;
	}

	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}

	public Integer getAgreementId() {
		return this.agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<SupplyingDetailDTO> getSupplyingDetails() {
		return this.supplyingDetails;
	}

	public void setSupplyingDetails(List<SupplyingDetailDTO> supplyingDetails) {
		this.supplyingDetails = supplyingDetails;
	}

}