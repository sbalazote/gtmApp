package com.lsntsolutions.gtmApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class ProviderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer code;
	private String name;
	private String taxId;
	private String corporateName;
	private Integer provinceId;
	private String locality;
	private String address;
	private String zipCode;
	private String phone;
	private String mail;
	private String gln;
	private Integer agentId;
	private Integer typeId;
	private Integer VATLiabilityId;
	private boolean active;
	private List<Integer> logisticsOperators;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxId() {
		return this.taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getCorporateName() {
		return this.corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getGln() {
		return this.gln;
	}

	public void setGln(String gln) {
		this.gln = gln;
	}

	public Integer getAgentId() {
		return this.agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	@JsonProperty("VATLiabilityId")
	public Integer getVATLiabilityId() {
		return this.VATLiabilityId;
	}

	@JsonProperty("VATLiabilityId")
	public void setVATLiabilityId(Integer VATLiabilityId) {
		this.VATLiabilityId = VATLiabilityId;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Integer> getLogisticsOperators() {
		return logisticsOperators;
	}

	public void setLogisticsOperators(List<Integer> logisticsOperators) {
		this.logisticsOperators = logisticsOperators;
	}
}
