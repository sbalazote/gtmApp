package com.lsntsolutions.gtmApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PropertyDTO implements Serializable {
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
	private String selfSerializedTagFilepath;
	private String ANMATName;
	private String ANMATPassword;
	private Integer printDeliveryNoteConceptId;
	private Integer startTraceConceptSelectId;
	private String proxy;
	private String proxyNumber;
	private boolean informProxy;
	private Integer supplyingConceptSelectId;
	private boolean informAnmat;
	private Integer VATLiabilityId;
	private boolean provisioningRequireAuthorization;
	private boolean	printPickingList;
	private Integer pickingListX;
	private Integer pickingListY;

	private Integer fontSize;
	private Integer numberX;
	private Integer numberY;
	private Boolean numberPrint;
	private Integer dateX;
	private Integer dateY;
	private Boolean datePrint;
	private Integer issuerCorporateNameX;
	private Integer issuerCorporateNameY;
	private Boolean issuerCorporateNamePrint;
	private Integer issuerAddressX;
	private Integer issuerAddressY;
	private Boolean issuerAddressPrint;
	private Integer issuerLocalityX;
	private Integer issuerLocalityY;
	private Boolean issuerLocalityPrint;
	private Integer issuerZipcodeX;
	private Integer issuerZipcodeY;
	private Boolean issuerZipcodePrint;
	private Integer issuerProvinceX;
	private Integer issuerProvinceY;
	private Boolean issuerProvincePrint;
	private Integer issuerVatliabilityX;
	private Integer issuerVatliabilityY;
	private Boolean issuerVatliabilityPrint;
	private Integer issuerTaxX;
	private Integer issuerTaxY;
	private Boolean issuerTaxPrint;
	private Integer deliveryLocationCorporateNameX;
	private Integer deliveryLocationCorporateNameY;
	private Boolean deliveryLocationCorporateNamePrint;
	private Integer deliveryLocationAddressX;
	private Integer deliveryLocationAddressY;
	private Boolean deliveryLocationAddressPrint;
	private Integer deliveryLocationLocalityX;
	private Integer deliveryLocationLocalityY;
	private Boolean deliveryLocationLocalityPrint;
	private Integer deliveryLocationZipcodeX;
	private Integer deliveryLocationZipcodeY;
	private Boolean deliveryLocationZipcodePrint;
	private Integer deliveryLocationProvinceX;
	private Integer deliveryLocationProvinceY;
	private Boolean deliveryLocationProvincePrint;
	private Integer deliveryLocationVatliabilityX;
	private Integer deliveryLocationVatliabilityY;
	private Boolean deliveryLocationVatliabilityPrint;
	private Integer deliveryLocationTaxX;
	private Integer deliveryLocationTaxY;
	private Boolean deliveryLocationTaxPrint;
	private Integer affiliateX;
	private Integer affiliateY;
	private Boolean affiliatePrint;
	private Integer orderX;
	private Integer orderY;
	private Boolean orderPrint;
	private Integer issuerGlnX;
	private Integer issuerGlnY;
	private Boolean issuerGlnPrint;
	private Integer deliveryLocationGlnX;
	private Integer deliveryLocationGlnY;
	private Boolean deliveryLocationGlnPrint;
	private Integer productDetailsY;
	private Integer productDescriptionX;
	private Boolean productDescriptionPrint;
	private Integer productMonodrugX;
	private Boolean productMonodrugPrint;
	private Integer productBrandX;
	private Boolean productBrandPrint;
	private Integer productAmountX;
	private Boolean productAmountPrint;
	private Integer productBatchExpirationdateX;
	private Boolean productBatchExpirationdatePrint;
	private Integer serialColumn1X;
	private Integer serialColumn2X;
	private Integer serialColumn3X;
	private Integer serialColumn4X;
	private Boolean serialColumn1Print;
	private Boolean serialColumn2Print;
	private Boolean serialColumn3Print;
	private Boolean serialColumn4Print;
	private Integer numberOfItemsX;
	private Integer numberOfItemsY;
	private Boolean numberOfItemsPrint;
	private Integer logisticsOperatorX;
	private Integer logisticsOperatorY;
	private Boolean logisticsOperatorPrint;

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

	public String getSelfSerializedTagFilepath() {
		return this.selfSerializedTagFilepath;
	}

	public void setSelfSerializedTagFilepath(String selfSerializedTagFilepath) {
		this.selfSerializedTagFilepath = selfSerializedTagFilepath;
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

	public boolean isInformAnmat() {
		return informAnmat;
	}

	public void setInformAnmat(boolean informAnmat) {
		this.informAnmat = informAnmat;
	}

	@JsonProperty("VATLiabilityId")
	public Integer getVATLiabilityId() {
		return this.VATLiabilityId;
	}

	@JsonProperty("VATLiabilityId")
	public void setVATLiabilityId(Integer VATLiabilityId) {
		this.VATLiabilityId = VATLiabilityId;
	}

	public boolean isProvisioningRequireAuthorization() {
		return provisioningRequireAuthorization;
	}

	public void setProvisioningRequireAuthorization(boolean provisioningRequireAuthorization) {
		this.provisioningRequireAuthorization = provisioningRequireAuthorization;
	}

	public boolean isPrintPickingList() {
		return printPickingList;
	}

	public void setPrintPickingList(boolean printPickingList) {
		this.printPickingList = printPickingList;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public Integer getNumberX() {
		return numberX;
	}

	public void setNumberX(Integer numberX) {
		this.numberX = numberX;
	}

	public Integer getNumberY() {
		return numberY;
	}

	public void setNumberY(Integer numberY) {
		this.numberY = numberY;
	}

	public Boolean getNumberPrint() {
		return numberPrint;
	}

	public void setNumberPrint(Boolean numberPrint) {
		this.numberPrint = numberPrint;
	}

	public Integer getDateX() {
		return dateX;
	}

	public void setDateX(Integer dateX) {
		this.dateX = dateX;
	}

	public Integer getDateY() {
		return dateY;
	}

	public void setDateY(Integer dateY) {
		this.dateY = dateY;
	}

	public Boolean getDatePrint() {
		return datePrint;
	}

	public void setDatePrint(Boolean datePrint) {
		this.datePrint = datePrint;
	}

	public Integer getIssuerCorporateNameX() {
		return issuerCorporateNameX;
	}

	public void setIssuerCorporateNameX(Integer issuerCorporateNameX) {
		this.issuerCorporateNameX = issuerCorporateNameX;
	}

	public Integer getIssuerCorporateNameY() {
		return issuerCorporateNameY;
	}

	public void setIssuerCorporateNameY(Integer issuerCorporateNameY) {
		this.issuerCorporateNameY = issuerCorporateNameY;
	}

	public Boolean getIssuerCorporateNamePrint() {
		return issuerCorporateNamePrint;
	}

	public void setIssuerCorporateNamePrint(Boolean issuerCorporateNamePrint) {
		this.issuerCorporateNamePrint = issuerCorporateNamePrint;
	}

	public Integer getIssuerAddressX() {
		return issuerAddressX;
	}

	public void setIssuerAddressX(Integer issuerAddressX) {
		this.issuerAddressX = issuerAddressX;
	}

	public Integer getIssuerAddressY() {
		return issuerAddressY;
	}

	public void setIssuerAddressY(Integer issuerAddressY) {
		this.issuerAddressY = issuerAddressY;
	}

	public Boolean getIssuerAddressPrint() {
		return issuerAddressPrint;
	}

	public void setIssuerAddressPrint(Boolean issuerAddressPrint) {
		this.issuerAddressPrint = issuerAddressPrint;
	}

	public Integer getIssuerLocalityX() {
		return issuerLocalityX;
	}

	public void setIssuerLocalityX(Integer issuerLocalityX) {
		this.issuerLocalityX = issuerLocalityX;
	}

	public Integer getIssuerLocalityY() {
		return issuerLocalityY;
	}

	public void setIssuerLocalityY(Integer issuerLocalityY) {
		this.issuerLocalityY = issuerLocalityY;
	}

	public Boolean getIssuerLocalityPrint() {
		return issuerLocalityPrint;
	}

	public void setIssuerLocalityPrint(Boolean issuerLocalityPrint) {
		this.issuerLocalityPrint = issuerLocalityPrint;
	}

	public Integer getIssuerZipcodeX() {
		return issuerZipcodeX;
	}

	public void setIssuerZipcodeX(Integer issuerZipcodeX) {
		this.issuerZipcodeX = issuerZipcodeX;
	}

	public Integer getIssuerZipcodeY() {
		return issuerZipcodeY;
	}

	public void setIssuerZipcodeY(Integer issuerZipcodeY) {
		this.issuerZipcodeY = issuerZipcodeY;
	}

	public Boolean getIssuerZipcodePrint() {
		return issuerZipcodePrint;
	}

	public void setIssuerZipcodePrint(Boolean issuerZipcodePrint) {
		this.issuerZipcodePrint = issuerZipcodePrint;
	}

	public Integer getIssuerProvinceX() {
		return issuerProvinceX;
	}

	public void setIssuerProvinceX(Integer issuerProvinceX) {
		this.issuerProvinceX = issuerProvinceX;
	}

	public Integer getIssuerProvinceY() {
		return issuerProvinceY;
	}

	public void setIssuerProvinceY(Integer issuerProvinceY) {
		this.issuerProvinceY = issuerProvinceY;
	}

	public Boolean getIssuerProvincePrint() {
		return issuerProvincePrint;
	}

	public void setIssuerProvincePrint(Boolean issuerProvincePrint) {
		this.issuerProvincePrint = issuerProvincePrint;
	}

	public Integer getIssuerVatliabilityX() {
		return issuerVatliabilityX;
	}

	public void setIssuerVatliabilityX(Integer issuerVatliabilityX) {
		this.issuerVatliabilityX = issuerVatliabilityX;
	}

	public Integer getIssuerVatliabilityY() {
		return issuerVatliabilityY;
	}

	public void setIssuerVatliabilityY(Integer issuerVatliabilityY) {
		this.issuerVatliabilityY = issuerVatliabilityY;
	}

	public Boolean getIssuerVatliabilityPrint() {
		return issuerVatliabilityPrint;
	}

	public void setIssuerVatliabilityPrint(Boolean issuerVatliabilityPrint) {
		this.issuerVatliabilityPrint = issuerVatliabilityPrint;
	}

	public Integer getIssuerTaxX() {
		return issuerTaxX;
	}

	public void setIssuerTaxX(Integer issuerTaxX) {
		this.issuerTaxX = issuerTaxX;
	}

	public Integer getIssuerTaxY() {
		return issuerTaxY;
	}

	public void setIssuerTaxY(Integer issuerTaxY) {
		this.issuerTaxY = issuerTaxY;
	}

	public Boolean getIssuerTaxPrint() {
		return issuerTaxPrint;
	}

	public void setIssuerTaxPrint(Boolean issuerTaxPrint) {
		this.issuerTaxPrint = issuerTaxPrint;
	}

	public Integer getDeliveryLocationCorporateNameX() {
		return deliveryLocationCorporateNameX;
	}

	public void setDeliveryLocationCorporateNameX(Integer deliveryLocationCorporateNameX) {
		this.deliveryLocationCorporateNameX = deliveryLocationCorporateNameX;
	}

	public Integer getDeliveryLocationCorporateNameY() {
		return deliveryLocationCorporateNameY;
	}

	public void setDeliveryLocationCorporateNameY(Integer deliveryLocationCorporateNameY) {
		this.deliveryLocationCorporateNameY = deliveryLocationCorporateNameY;
	}

	public Boolean getDeliveryLocationCorporateNamePrint() {
		return deliveryLocationCorporateNamePrint;
	}

	public void setDeliveryLocationCorporateNamePrint(Boolean deliveryLocationCorporateNamePrint) {
		this.deliveryLocationCorporateNamePrint = deliveryLocationCorporateNamePrint;
	}

	public Integer getDeliveryLocationAddressX() {
		return deliveryLocationAddressX;
	}

	public void setDeliveryLocationAddressX(Integer deliveryLocationAddressX) {
		this.deliveryLocationAddressX = deliveryLocationAddressX;
	}

	public Integer getDeliveryLocationAddressY() {
		return deliveryLocationAddressY;
	}

	public void setDeliveryLocationAddressY(Integer deliveryLocationAddressY) {
		this.deliveryLocationAddressY = deliveryLocationAddressY;
	}

	public Boolean getDeliveryLocationAddressPrint() {
		return deliveryLocationAddressPrint;
	}

	public void setDeliveryLocationAddressPrint(Boolean deliveryLocationAddressPrint) {
		this.deliveryLocationAddressPrint = deliveryLocationAddressPrint;
	}

	public Integer getDeliveryLocationLocalityX() {
		return deliveryLocationLocalityX;
	}

	public void setDeliveryLocationLocalityX(Integer deliveryLocationLocalityX) {
		this.deliveryLocationLocalityX = deliveryLocationLocalityX;
	}

	public Integer getDeliveryLocationLocalityY() {
		return deliveryLocationLocalityY;
	}

	public void setDeliveryLocationLocalityY(Integer deliveryLocationLocalityY) {
		this.deliveryLocationLocalityY = deliveryLocationLocalityY;
	}

	public Boolean getDeliveryLocationLocalityPrint() {
		return deliveryLocationLocalityPrint;
	}

	public void setDeliveryLocationLocalityPrint(Boolean deliveryLocationLocalityPrint) {
		this.deliveryLocationLocalityPrint = deliveryLocationLocalityPrint;
	}

	public Integer getDeliveryLocationZipcodeX() {
		return deliveryLocationZipcodeX;
	}

	public void setDeliveryLocationZipcodeX(Integer deliveryLocationZipcodeX) {
		this.deliveryLocationZipcodeX = deliveryLocationZipcodeX;
	}

	public Integer getDeliveryLocationZipcodeY() {
		return deliveryLocationZipcodeY;
	}

	public void setDeliveryLocationZipcodeY(Integer deliveryLocationZipcodeY) {
		this.deliveryLocationZipcodeY = deliveryLocationZipcodeY;
	}

	public Boolean getDeliveryLocationZipcodePrint() {
		return deliveryLocationZipcodePrint;
	}

	public void setDeliveryLocationZipcodePrint(Boolean deliveryLocationZipcodePrint) {
		this.deliveryLocationZipcodePrint = deliveryLocationZipcodePrint;
	}

	public Integer getDeliveryLocationProvinceX() {
		return deliveryLocationProvinceX;
	}

	public void setDeliveryLocationProvinceX(Integer deliveryLocationProvinceX) {
		this.deliveryLocationProvinceX = deliveryLocationProvinceX;
	}

	public Integer getDeliveryLocationProvinceY() {
		return deliveryLocationProvinceY;
	}

	public void setDeliveryLocationProvinceY(Integer deliveryLocationProvinceY) {
		this.deliveryLocationProvinceY = deliveryLocationProvinceY;
	}

	public Boolean getDeliveryLocationProvincePrint() {
		return deliveryLocationProvincePrint;
	}

	public void setDeliveryLocationProvincePrint(Boolean deliveryLocationProvincePrint) {
		this.deliveryLocationProvincePrint = deliveryLocationProvincePrint;
	}

	public Integer getDeliveryLocationVatliabilityX() {
		return deliveryLocationVatliabilityX;
	}

	public void setDeliveryLocationVatliabilityX(Integer deliveryLocationVatliabilityX) {
		this.deliveryLocationVatliabilityX = deliveryLocationVatliabilityX;
	}

	public Integer getDeliveryLocationVatliabilityY() {
		return deliveryLocationVatliabilityY;
	}

	public void setDeliveryLocationVatliabilityY(Integer deliveryLocationVatliabilityY) {
		this.deliveryLocationVatliabilityY = deliveryLocationVatliabilityY;
	}

	public Boolean getDeliveryLocationVatliabilityPrint() {
		return deliveryLocationVatliabilityPrint;
	}

	public void setDeliveryLocationVatliabilityPrint(Boolean deliveryLocationVatliabilityPrint) {
		this.deliveryLocationVatliabilityPrint = deliveryLocationVatliabilityPrint;
	}

	public Integer getDeliveryLocationTaxX() {
		return deliveryLocationTaxX;
	}

	public void setDeliveryLocationTaxX(Integer deliveryLocationTaxX) {
		this.deliveryLocationTaxX = deliveryLocationTaxX;
	}

	public Integer getDeliveryLocationTaxY() {
		return deliveryLocationTaxY;
	}

	public void setDeliveryLocationTaxY(Integer deliveryLocationTaxY) {
		this.deliveryLocationTaxY = deliveryLocationTaxY;
	}

	public Boolean getDeliveryLocationTaxPrint() {
		return deliveryLocationTaxPrint;
	}

	public void setDeliveryLocationTaxPrint(Boolean deliveryLocationTaxPrint) {
		this.deliveryLocationTaxPrint = deliveryLocationTaxPrint;
	}

	public Integer getAffiliateX() {
		return affiliateX;
	}

	public void setAffiliateX(Integer affiliateX) {
		this.affiliateX = affiliateX;
	}

	public Integer getAffiliateY() {
		return affiliateY;
	}

	public void setAffiliateY(Integer affiliateY) {
		this.affiliateY = affiliateY;
	}

	public Boolean getAffiliatePrint() {
		return affiliatePrint;
	}

	public void setAffiliatePrint(Boolean affiliatePrint) {
		this.affiliatePrint = affiliatePrint;
	}

	public Integer getOrderX() {
		return orderX;
	}

	public void setOrderX(Integer orderX) {
		this.orderX = orderX;
	}

	public Integer getOrderY() {
		return orderY;
	}

	public void setOrderY(Integer orderY) {
		this.orderY = orderY;
	}

	public Boolean getOrderPrint() {
		return orderPrint;
	}

	public void setOrderPrint(Boolean orderPrint) {
		this.orderPrint = orderPrint;
	}

	public Integer getIssuerGlnX() {
		return issuerGlnX;
	}

	public void setIssuerGlnX(Integer issuerGlnX) {
		this.issuerGlnX = issuerGlnX;
	}

	public Integer getIssuerGlnY() {
		return issuerGlnY;
	}

	public void setIssuerGlnY(Integer issuerGlnY) {
		this.issuerGlnY = issuerGlnY;
	}

	public Boolean getIssuerGlnPrint() {
		return issuerGlnPrint;
	}

	public void setIssuerGlnPrint(Boolean issuerGlnPrint) {
		this.issuerGlnPrint = issuerGlnPrint;
	}

	public Integer getDeliveryLocationGlnX() {
		return deliveryLocationGlnX;
	}

	public void setDeliveryLocationGlnX(Integer deliveryLocationGlnX) {
		this.deliveryLocationGlnX = deliveryLocationGlnX;
	}

	public Integer getDeliveryLocationGlnY() {
		return deliveryLocationGlnY;
	}

	public void setDeliveryLocationGlnY(Integer deliveryLocationGlnY) {
		this.deliveryLocationGlnY = deliveryLocationGlnY;
	}

	public Boolean getDeliveryLocationGlnPrint() {
		return deliveryLocationGlnPrint;
	}

	public void setDeliveryLocationGlnPrint(Boolean deliveryLocationGlnPrint) {
		this.deliveryLocationGlnPrint = deliveryLocationGlnPrint;
	}

	public Integer getProductDetailsY() {
		return productDetailsY;
	}

	public void setProductDetailsY(Integer productDetailsY) {
		this.productDetailsY = productDetailsY;
	}

	public Integer getProductDescriptionX() {
		return productDescriptionX;
	}

	public void setProductDescriptionX(Integer productDescriptionX) {
		this.productDescriptionX = productDescriptionX;
	}

	public Boolean getProductDescriptionPrint() {
		return productDescriptionPrint;
	}

	public void setProductDescriptionPrint(Boolean productDescriptionPrint) {
		this.productDescriptionPrint = productDescriptionPrint;
	}

	public Integer getProductMonodrugX() {
		return productMonodrugX;
	}

	public void setProductMonodrugX(Integer productMonodrugX) {
		this.productMonodrugX = productMonodrugX;
	}

	public Boolean getProductMonodrugPrint() {
		return productMonodrugPrint;
	}

	public void setProductMonodrugPrint(Boolean productMonodrugPrint) {
		this.productMonodrugPrint = productMonodrugPrint;
	}

	public Integer getProductBrandX() {
		return productBrandX;
	}

	public void setProductBrandX(Integer productBrandX) {
		this.productBrandX = productBrandX;
	}

	public Boolean getProductBrandPrint() {
		return productBrandPrint;
	}

	public void setProductBrandPrint(Boolean productBrandPrint) {
		this.productBrandPrint = productBrandPrint;
	}

	public Integer getProductAmountX() {
		return productAmountX;
	}

	public void setProductAmountX(Integer productAmountX) {
		this.productAmountX = productAmountX;
	}

	public Boolean getProductAmountPrint() {
		return productAmountPrint;
	}

	public void setProductAmountPrint(Boolean productAmountPrint) {
		this.productAmountPrint = productAmountPrint;
	}

	public Integer getProductBatchExpirationdateX() {
		return productBatchExpirationdateX;
	}

	public void setProductBatchExpirationdateX(Integer productBatchExpirationdateX) {
		this.productBatchExpirationdateX = productBatchExpirationdateX;
	}

	public Boolean getProductBatchExpirationdatePrint() {
		return productBatchExpirationdatePrint;
	}

	public void setProductBatchExpirationdatePrint(Boolean productBatchExpirationdatePrint) {
		this.productBatchExpirationdatePrint = productBatchExpirationdatePrint;
	}

	public Integer getSerialColumn1X() {
		return serialColumn1X;
	}

	public void setSerialColumn1X(Integer serialColumn1X) {
		this.serialColumn1X = serialColumn1X;
	}

	public Integer getSerialColumn2X() {
		return serialColumn2X;
	}

	public void setSerialColumn2X(Integer serialColumn2X) {
		this.serialColumn2X = serialColumn2X;
	}

	public Integer getSerialColumn3X() {
		return serialColumn3X;
	}

	public void setSerialColumn3X(Integer serialColumn3X) {
		this.serialColumn3X = serialColumn3X;
	}

	public Integer getSerialColumn4X() {
		return serialColumn4X;
	}

	public void setSerialColumn4X(Integer serialColumn4X) {
		this.serialColumn4X = serialColumn4X;
	}

	public Boolean getSerialColumn1Print() {
		return serialColumn1Print;
	}

	public void setSerialColumn1Print(Boolean serialColumn1Print) {
		this.serialColumn1Print = serialColumn1Print;
	}

	public Boolean getSerialColumn2Print() {
		return serialColumn2Print;
	}

	public void setSerialColumn2Print(Boolean serialColumn2Print) {
		this.serialColumn2Print = serialColumn2Print;
	}

	public Boolean getSerialColumn3Print() {
		return serialColumn3Print;
	}

	public void setSerialColumn3Print(Boolean serialColumn3Print) {
		this.serialColumn3Print = serialColumn3Print;
	}

	public Boolean getSerialColumn4Print() {
		return serialColumn4Print;
	}

	public void setSerialColumn4Print(Boolean serialColumn4Print) {
		this.serialColumn4Print = serialColumn4Print;
	}

	public Integer getNumberOfItemsX() {
		return numberOfItemsX;
	}

	public void setNumberOfItemsX(Integer numberOfItemsX) {
		this.numberOfItemsX = numberOfItemsX;
	}

	public Integer getNumberOfItemsY() {
		return numberOfItemsY;
	}

	public void setNumberOfItemsY(Integer numberOfItemsY) {
		this.numberOfItemsY = numberOfItemsY;
	}

	public Boolean getNumberOfItemsPrint() {
		return numberOfItemsPrint;
	}

	public void setNumberOfItemsPrint(Boolean numberOfItemsPrint) {
		this.numberOfItemsPrint = numberOfItemsPrint;
	}

	public Integer getLogisticsOperatorX() {
		return logisticsOperatorX;
	}

	public void setLogisticsOperatorX(Integer logisticsOperatorX) {
		this.logisticsOperatorX = logisticsOperatorX;
	}

	public Integer getLogisticsOperatorY() {
		return logisticsOperatorY;
	}

	public void setLogisticsOperatorY(Integer logisticsOperatorY) {
		this.logisticsOperatorY = logisticsOperatorY;
	}

	public Boolean getLogisticsOperatorPrint() {
		return logisticsOperatorPrint;
	}

	public void setLogisticsOperatorPrint(Boolean logisticsOperatorPrint) {
		this.logisticsOperatorPrint = logisticsOperatorPrint;
	}

	public Integer getPickingListX() {
		return pickingListX;
	}

	public void setPickingListX(Integer pickingListX) {
		this.pickingListX = pickingListX;
	}

	public Integer getPickingListY() {
		return pickingListY;
	}

	public void setPickingListY(Integer pickingListY) {
		this.pickingListY = pickingListY;
	}
}