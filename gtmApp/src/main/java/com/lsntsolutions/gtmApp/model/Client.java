package com.lsntsolutions.gtmApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lsntsolutions.gtmApp.util.StringUtility;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "client")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "code", unique = true, nullable = false)
	private Integer code;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "tax_id", nullable = false)
	private String taxId;

	@Column(name = "corporate_name", nullable = false)
	private String corporateName;

	@ManyToOne
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;

	@Column(name = "locality", nullable = false)
	private String locality;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "zip_code", nullable = false)
	private String zipCode;

	@ManyToOne
	@JoinColumn(name = "VAT_liability_id", nullable = false)
	private VATLiability VATLiability;

	@Column(name = "phone")
	private String phone;

	@Column(name = "active", nullable = false)
	private boolean active;

	@Column(name = "medical_insurance_code", unique = true)
	private Integer medicalInsuranceCode;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "client_affiliate", joinColumns = @JoinColumn(name = "client_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "affiliate_id", nullable = false))
	@JsonManagedReference
	private List<Affiliate> affiliates;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "client_delivery_location", joinColumns = @JoinColumn(name = "client_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "delivery_location_id", nullable = false))
	private List<DeliveryLocation> deliveryLocations;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getFormatCode() {
		return StringUtility.addLeadingZeros(this.getCode().toString(), 5);
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

	public Province getProvince() {
		return this.province;
	}

	public void setProvince(Province province) {
		this.province = province;
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

	public VATLiability getVATLiability() {
		return this.VATLiability;
	}

	public void setVATLiability(VATLiability VATLiability) {
		this.VATLiability = VATLiability;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public List<DeliveryLocation> getDeliveryLocations() {
		return this.deliveryLocations;
	}

	public void setDeliveryLocations(List<DeliveryLocation> deliveryLocations) {
		this.deliveryLocations = deliveryLocations;
	}

	public List<Affiliate> getAffiliates() { return affiliates; }

	public void setAffiliates(List<Affiliate> affiliates) { this.affiliates = affiliates; }
}