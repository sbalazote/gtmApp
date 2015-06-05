package com.drogueria.dto;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ClientDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer code;
	private String name;
	private String taxId;
	private String corporateName;
	private Integer provinceId;
	private Integer VATLiabilityId;
	private String locality;
	private String address;
	private String zipCode;
	private String phone;
	private List<Integer> deliveryLocations;
	private boolean active;
	private Integer medicalInsuranceCode;

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

	@JsonProperty("VATLiabilityId")
	public Integer getVATLiabilityId() {
		return this.VATLiabilityId;
	}

	@JsonProperty("VATLiabilityId")
	public void setVATLiabilityId(Integer VATLiabilityId) {
		this.VATLiabilityId = VATLiabilityId;
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

	public List<Integer> getDeliveryLocations() {
		return this.deliveryLocations;
	}

	public void setDeliveryLocations(List<Integer> deliveryLocations) {
		this.deliveryLocations = deliveryLocations;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getMedicalInsuranceCode() {
		return this.medicalInsuranceCode;
	}

	public void setMedicalInsuranceCode(Integer medicalInsuranceCode) {
		this.medicalInsuranceCode = medicalInsuranceCode;
	}

}
