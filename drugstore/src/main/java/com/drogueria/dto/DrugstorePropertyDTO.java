package com.drogueria.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class DrugstorePropertyDTO implements Serializable {
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
	private Integer daysAgoPendingTransactions;
	private Integer numberOfDeliveryNoteDetailsPerPage;
	private String orderLabelFilepath;
	private String selfSerializedTagFilepath;
	private String deliveryNoteFilepath;
	private String pickingFilepath;
	private String ANMATName;
	private String ANMATPassword;
	private Integer printDeliveryNoteConceptId;
	private Integer startTraceConceptSelectId;
	private String proxy;
	private String proxyNumber;
	private boolean informProxy;
	private Integer supplyingConceptSelectId;

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

	public Integer getDaysAgoPendingTransactions() {
		return this.daysAgoPendingTransactions;
	}

	public void setDaysAgoPendingTransactions(Integer daysAgoPendingTransactions) {
		this.daysAgoPendingTransactions = daysAgoPendingTransactions;
	}

	public Integer getNumberOfDeliveryNoteDetailsPerPage() {
		return this.numberOfDeliveryNoteDetailsPerPage;
	}

	public void setNumberOfDeliveryNoteDetailsPerPage(Integer numberOfDeliveryNoteDetailsPerPage) {
		this.numberOfDeliveryNoteDetailsPerPage = numberOfDeliveryNoteDetailsPerPage;
	}

	public String getOrderLabelFilepath() {
		return this.orderLabelFilepath;
	}

	public void setOrderLabelFilepath(String orderLabelFilepath) {
		this.orderLabelFilepath = orderLabelFilepath;
	}

	public String getSelfSerializedTagFilepath() {
		return this.selfSerializedTagFilepath;
	}

	public void setSelfSerializedTagFilepath(String selfSerializedTagFilepath) {
		this.selfSerializedTagFilepath = selfSerializedTagFilepath;
	}

	public String getDeliveryNoteFilepath() {
		return this.deliveryNoteFilepath;
	}

	public void setDeliveryNoteFilepath(String deliveryNoteFilepath) {
		this.deliveryNoteFilepath = deliveryNoteFilepath;
	}

	public String getPickingFilepath() {
		return this.pickingFilepath;
	}

	public void setPickingFilepath(String pickingFilepath) {
		this.pickingFilepath = pickingFilepath;
	}

	@JsonProperty("ANMATName")
	public String getANMATName() {
		return this.ANMATName;
	}

	@JsonProperty("ANMATName")
	public void setANMATName(String aNMATName) {
		this.ANMATName = aNMATName;
	}

	@JsonProperty("ANMATPassword")
	public String getANMATPassword() {
		return this.ANMATPassword;
	}

	@JsonProperty("ANMATPassword")
	public void setANMATPassword(String aNMATPassword) {
		this.ANMATPassword = aNMATPassword;
	}

	public Integer getPrintDeliveryNoteConceptId() {
		return this.printDeliveryNoteConceptId;
	}

	public void setPrintDeliveryNoteConceptId(Integer printDeliveryNoteConceptId) {
		this.printDeliveryNoteConceptId = printDeliveryNoteConceptId;
	}

	public Integer getStartTraceConceptSelectId() {
		return this.startTraceConceptSelectId;
	}

	public void setStartTraceConceptSelectId(Integer startTraceConceptSelectId) {
		this.startTraceConceptSelectId = startTraceConceptSelectId;
	}

	public String getProxy() {
		return this.proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getProxyNumber() {
		return this.proxyNumber;
	}

	public void setProxyNumber(String proxyNumber) {
		this.proxyNumber = proxyNumber;
	}

	public boolean isInformProxy() {
		return this.informProxy;
	}

	public void setInformProxy(boolean informProxy) {
		this.informProxy = informProxy;
	}

	public Integer getSupplyingConceptSelectId() {
		return this.supplyingConceptSelectId;
	}

	public void setSupplyingConceptSelectId(Integer supplyingConceptSelectId) {
		this.supplyingConceptSelectId = supplyingConceptSelectId;
	}

}
