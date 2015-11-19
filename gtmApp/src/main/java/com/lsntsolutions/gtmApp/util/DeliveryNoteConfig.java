package com.lsntsolutions.gtmApp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeliveryNoteConfig {

	// The coordinates are measured in points. 1 inch is divided into 72 points so that 1 Millimeter equals 2.8346 points.
	private float MILLIMITER_TO_POINTS_FACTOR = 2.8346f;

	@Value("${font.size}")
	private Integer fontSize;

	@Value("${number.x}")
	private Integer numberX;
	@Value("${number.y}")
	private Integer numberY;
	@Value("${number.print}")
	private Boolean numberPrint;

	@Value("${date.x}")
	private Integer dateX;
	@Value("${date.y}")
	private Integer dateY;
	@Value("${date.print}")
	private Boolean datePrint;

	@Value("${issuer.corporateName.x}")
	private Integer issuerCorporateNameX;
	@Value("${issuer.corporateName.y}")
	private Integer issuerCorporateNameY;
	@Value("${issuer.corporateName.print}")
	private Boolean issuerCorporateNamePrint;

	@Value("${issuer.address.x}")
	private Integer issuerAddressX;
	@Value("${issuer.address.y}")
	private Integer issuerAddressY;
	@Value("${issuer.address.print}")
	private Boolean issuerAddressPrint;

	@Value("${issuer.locality.x}")
	private Integer issuerLocalityX;
	@Value("${issuer.locality.y}")
	private Integer issuerLocalityY;
	@Value("${issuer.locality.print}")
	private Boolean issuerLocalityPrint;

	@Value("${issuer.zipcode.x}")
	private Integer issuerZipcodeX;
	@Value("${issuer.zipcode.y}")
	private Integer issuerZipcodeY;
	@Value("${issuer.zipcode.print}")
	private Boolean issuerZipcodePrint;

	@Value("${issuer.province.x}")
	private Integer issuerProvinceX;
	@Value("${issuer.province.y}")
	private Integer issuerProvinceY;
	@Value("${issuer.province.print}")
	private Boolean issuerProvincePrint;

	@Value("${issuer.vatliability.x}")
	private Integer issuerVatliabilityX;
	@Value("${issuer.vatliability.y}")
	private Integer issuerVatliabilityY;
	@Value("${issuer.vatliability.print}")
	private Boolean issuerVatliabilityPrint;

	@Value("${issuer.tax.x}")
	private Integer issuerTaxX;
	@Value("${issuer.tax.y}")
	private Integer issuerTaxY;
	@Value("${issuer.tax.print}")
	private Boolean issuerTaxPrint;

	@Value("${deliveryLocation.corporateName.x}")
	private Integer deliveryLocationCorporateNameX;
	@Value("${deliveryLocation.corporateName.y}")
	private Integer deliveryLocationCorporateNameY;
	@Value("${deliveryLocation.corporateName.print}")
	private Boolean deliveryLocationCorporateNamePrint;

	@Value("${deliveryLocation.address.x}")
	private Integer deliveryLocationAddressX;
	@Value("${deliveryLocation.address.y}")
	private Integer deliveryLocationAddressY;
	@Value("${deliveryLocation.address.print}")
	private Boolean deliveryLocationAddressPrint;

	@Value("${deliveryLocation.locality.x}")
	private Integer deliveryLocationLocalityX;
	@Value("${deliveryLocation.locality.y}")
	private Integer deliveryLocationLocalityY;
	@Value("${deliveryLocation.locality.print}")
	private Boolean deliveryLocationLocalityPrint;

	@Value("${deliveryLocation.zipcode.x}")
	private Integer deliveryLocationZipcodeX;
	@Value("${deliveryLocation.zipcode.y}")
	private Integer deliveryLocationZipcodeY;
	@Value("${deliveryLocation.zipcode.print}")
	private Boolean deliveryLocationZipcodePrint;

	@Value("${deliveryLocation.province.x}")
	private Integer deliveryLocationProvinceX;
	@Value("${deliveryLocation.province.y}")
	private Integer deliveryLocationProvinceY;
	@Value("${deliveryLocation.province.print}")
	private Boolean deliveryLocationProvincePrint;

	@Value("${deliveryLocation.vatliability.x}")
	private Integer deliveryLocationVatliabilityX;
	@Value("${deliveryLocation.vatliability.y}")
	private Integer deliveryLocationVatliabilityY;
	@Value("${deliveryLocation.vatliability.print}")
	private Boolean deliveryLocationVatliabilityPrint;

	@Value("${deliveryLocation.tax.x}")
	private Integer deliveryLocationTaxX;
	@Value("${deliveryLocation.tax.y}")
	private Integer deliveryLocationTaxY;
	@Value("${deliveryLocation.tax.print}")
	private Boolean deliveryLocationTaxPrint;

	@Value("${affiliate.x}")
	private Integer affiliateX;
	@Value("${affiliate.y}")
	private Integer affiliateY;
	@Value("${affiliate.print}")
	private Boolean affiliatePrint;

	@Value("${order.x}")
	private Integer orderX;
	@Value("${order.y}")
	private Integer orderY;
	@Value("${order.print}")
	private Boolean orderPrint;

	@Value("${issuer.gln.x}")
	private Integer issuerGlnX;
	@Value("${issuer.gln.y}")
	private Integer issuerGlnY;
	@Value("${issuer.gln.print}")
	private Boolean issuerGlnPrint;

	@Value("${deliveryLocation.gln.x}")
	private Integer deliveryLocationGlnX;
	@Value("${deliveryLocation.gln.y}")
	private Integer deliveryLocationGlnY;
	@Value("${deliveryLocation.gln.print}")
	private Boolean deliveryLocationGlnPrint;

	@Value("${product.details.y}")
	private Integer productDetailsY;

	@Value("${product.description.x}")
	private Integer productDescriptionX;
	@Value("${product.description.print}")
	private Boolean productDescriptionPrint;

	@Value("${product.monodrug.x}")
	private Integer productMonodrugX;
	@Value("${product.monodrug.print}")
	private Boolean productMonodrugPrint;

	@Value("${product.brand.x}")
	private Integer productBrandX;
	@Value("${product.brand.print}")
	private Boolean productBrandPrint;

	@Value("${product.amount.x}")
	private Integer productAmountX;
	@Value("${product.amount.print}")
	private Boolean productAmountPrint;

	@Value("${product.batchExpirationdate.x}")
	private Integer productBatchExpirationdateX;
	@Value("${product.batchExpirationdate.print}")
	private Boolean productBatchExpirationdatePrint;

	@Value("${serial.column1.x}")
	private Integer serialColumn1X;
	@Value("${serial.column2.x}")
	private Integer serialColumn2X;
	@Value("${serial.column3.x}")
	private Integer serialColumn3X;
	@Value("${serial.column4.x}")
	private Integer serialColumn4X;
	@Value("${serial.column1.print}")
	private Boolean serialColumn1Print;
	@Value("${serial.column2.print}")
	private Boolean serialColumn2Print;
	@Value("${serial.column3.print}")
	private Boolean serialColumn3Print;
	@Value("${serial.column4.print}")
	private Boolean serialColumn4Print;

	@Value("${numberOfItems.x}")
	private Integer numberOfItemsX;
	@Value("${numberOfItems.y}")
	private Integer numberOfItemsY;
	@Value("${numberOfItems.print}")
	private Boolean numberOfItemsPrint;

	public Integer getFontSize() {
		return fontSize;
	}

	public float getNumberX() {
		return numberX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getNumberY() {
		return (297.0f - numberY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDateX() {
		return dateX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDateY() {
		return (297.0f - dateY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerCorporateNameX() {
		return issuerCorporateNameX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerCorporateNameY() {
		return (297.0f - issuerCorporateNameY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerAddressX() {
		return issuerAddressX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerAddressY() {
		return (297.0f - issuerAddressY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerLocalityX() {
		return issuerLocalityX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerLocalityY() {
		return (297.0f - issuerLocalityY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerZipcodeX() {
		return issuerZipcodeX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerZipcodeY() {
		return (297.0f - issuerZipcodeY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerProvinceX() {
		return issuerProvinceX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerProvinceY() {
		return (297.0f - issuerProvinceY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerVatliabilityX() {
		return issuerVatliabilityX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerVatliabilityY() {
		return (297.0f - issuerVatliabilityY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerTaxX() {
		return issuerTaxX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerTaxY() {
		return (297.0f - issuerTaxY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationCorporateNameX() {
		return deliveryLocationCorporateNameX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationCorporateNameY() {
		return (297.0f - deliveryLocationCorporateNameY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationAddressX() {
		return deliveryLocationAddressX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationAddressY() {
		return (297.0f - deliveryLocationAddressY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationLocalityX() {
		return deliveryLocationLocalityX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationLocalityY() {
		return (297.0f - deliveryLocationLocalityY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationZipcodeX() {
		return deliveryLocationZipcodeX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationZipcodeY() {
		return (297.0f - deliveryLocationZipcodeY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationProvinceX() {
		return deliveryLocationProvinceX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationProvinceY() {
		return (297.0f - deliveryLocationProvinceY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationVatliabilityX() {
		return deliveryLocationVatliabilityX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationVatliabilityY() {
		return (297.0f - deliveryLocationVatliabilityY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationTaxX() {
		return deliveryLocationTaxX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationTaxY() {
		return (297.0f - deliveryLocationTaxY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getAffiliateX() {
		return affiliateX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getAffiliateY() {
		return (297.0f - affiliateY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getOrderX() {
		return orderX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getOrderY() {
		return (297.0f - orderY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerGlnX() {
		return issuerGlnX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getIssuerGlnY() {
		return (297.0f - issuerGlnY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationGlnX() {
		return deliveryLocationGlnX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getDeliveryLocationGlnY() {
		return (297.0f - deliveryLocationGlnY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getProductDetailsY() {
		return (297.0f - productDetailsY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getProductDescriptionX() {
		return productDescriptionX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getProductMonodrugX() {
		return productMonodrugX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getProductBrandX() {
		return productBrandX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getProductAmountX() {
		return productAmountX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getProductBatchExpirationdateX() {
		return productBatchExpirationdateX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getSerialColumn1X() {
		return serialColumn1X * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getSerialColumn2X() {
		return serialColumn2X * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getSerialColumn3X() {
		return serialColumn3X * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getSerialColumn4X() {
		return serialColumn4X * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getNumberOfItemsX() {
		return numberOfItemsX * MILLIMITER_TO_POINTS_FACTOR;
	}

	public float getNumberOfItemsY() {
		return (297.0f - numberOfItemsY) * MILLIMITER_TO_POINTS_FACTOR;
	}

	public Boolean isNumberPrint() {
		return numberPrint;
	}

	public Boolean isDatePrint() {
		return datePrint;
	}

	public Boolean isIssuerCorporateNamePrint() {
		return issuerCorporateNamePrint;
	}

	public Boolean isIssuerAddressPrint() {
		return issuerAddressPrint;
	}

	public Boolean isIssuerLocalityPrint() {
		return issuerLocalityPrint;
	}

	public Boolean isIssuerZipcodePrint() {
		return issuerZipcodePrint;
	}

	public Boolean isIssuerProvincePrint() {
		return issuerProvincePrint;
	}

	public Boolean isIssuerVatliabilityPrint() {
		return issuerVatliabilityPrint;
	}

	public Boolean isIssuerTaxPrint() {
		return issuerTaxPrint;
	}

	public Boolean isDeliveryLocationCorporateNamePrint() {
		return deliveryLocationCorporateNamePrint;
	}

	public Boolean isDeliveryLocationAddressPrint() {
		return deliveryLocationAddressPrint;
	}

	public Boolean isDeliveryLocationLocalityPrint() {
		return deliveryLocationLocalityPrint;
	}

	public Boolean isDeliveryLocationZipcodePrint() {
		return deliveryLocationZipcodePrint;
	}

	public Boolean isDeliveryLocationProvincePrint() {
		return deliveryLocationProvincePrint;
	}

	public Boolean isDeliveryLocationVatliabilityPrint() {
		return deliveryLocationVatliabilityPrint;
	}

	public Boolean isDeliveryLocationTaxPrint() {
		return deliveryLocationTaxPrint;
	}

	public Boolean isAffiliatePrint() {
		return affiliatePrint;
	}

	public Boolean isOrderPrint() {
		return orderPrint;
	}

	public Boolean isIssuerGlnPrint() {
		return issuerGlnPrint;
	}

	public Boolean isDeliveryLocationGlnPrint() {
		return deliveryLocationGlnPrint;
	}

	public Boolean isProductDescriptionPrint() {
		return productDescriptionPrint;
	}

	public Boolean isProductMonodrugPrint() {
		return productMonodrugPrint;
	}

	public Boolean isProductBrandPrint() {
		return productBrandPrint;
	}

	public Boolean isProductAmountPrint() {
		return productAmountPrint;
	}

	public Boolean isProductBatchExpirationdatePrint() {
		return productBatchExpirationdatePrint;
	}

	public Boolean isSerialColumn1Print() {
		return serialColumn1Print;
	}

	public Boolean isSerialColumn2Print() {
		return serialColumn2Print;
	}

	public Boolean isSerialColumn3Print() {
		return serialColumn3Print;
	}

	public Boolean isSerialColumn4Print() {
		return serialColumn4Print;
	}

	public Boolean isNumberOfItemsPrint() {
		return numberOfItemsPrint;
	}

	public Integer getNumberXInMillimeters() {
		return numberX;
	}

	public Integer getNumberYInMillimeters() {
		return numberY;
	}

	public Integer getDateXInMillimeters() {
		return dateX;
	}

	public Integer getDateYInMillimeters() {
		return dateY;
	}

	public Integer getIssuerCorporateNameXInMillimeters() {
		return issuerCorporateNameX;
	}

	public Integer getIssuerCorporateNameYInMillimeters() {
		return issuerCorporateNameY;
	}

	public Integer getIssuerAddressXInMillimeters() {
		return issuerAddressX;
	}

	public Integer getIssuerAddressYInMillimeters() {
		return issuerAddressY;
	}

	public Integer getIssuerLocalityXInMillimeters() {
		return issuerLocalityX;
	}

	public Integer getIssuerLocalityYInMillimeters() {
		return issuerLocalityY;
	}

	public Integer getIssuerZipcodeXInMillimeters() {
		return issuerZipcodeX;
	}

	public Integer getIssuerZipcodeYInMillimeters() {
		return issuerZipcodeY;
	}

	public Integer getIssuerProvinceXInMillimeters() {
		return issuerProvinceX;
	}

	public Integer getIssuerProvinceYInMillimeters() {
		return issuerProvinceY;
	}

	public Integer getIssuerVatliabilityXInMillimeters() {
		return issuerVatliabilityX;
	}

	public Integer getIssuerVatliabilityYInMillimeters() {
		return issuerVatliabilityY;
	}

	public Integer getIssuerTaxXInMillimeters() {
		return issuerTaxX;
	}

	public Integer getIssuerTaxYInMillimeters() {
		return issuerTaxY;
	}

	public Integer getDeliveryLocationCorporateNameXInMillimeters() {
		return deliveryLocationCorporateNameX;
	}

	public Integer getDeliveryLocationCorporateNameYInMillimeters() {
		return deliveryLocationCorporateNameY;
	}

	public Integer getDeliveryLocationAddressXInMillimeters() {
		return deliveryLocationAddressX;
	}

	public Integer getDeliveryLocationAddressYInMillimeters() {
		return deliveryLocationAddressY;
	}

	public Integer getDeliveryLocationLocalityXInMillimeters() {
		return deliveryLocationLocalityX;
	}

	public Integer getDeliveryLocationLocalityYInMillimeters() {
		return deliveryLocationLocalityY;
	}

	public Integer getDeliveryLocationZipcodeXInMillimeters() {
		return deliveryLocationZipcodeX;
	}

	public Integer getDeliveryLocationZipcodeYInMillimeters() {
		return deliveryLocationZipcodeY;
	}

	public Integer getDeliveryLocationProvinceXInMillimeters() {
		return deliveryLocationProvinceX;
	}

	public Integer getDeliveryLocationProvinceYInMillimeters() {
		return deliveryLocationProvinceY;
	}

	public Integer getDeliveryLocationVatliabilityXInMillimeters() {
		return deliveryLocationVatliabilityX;
	}

	public Integer getDeliveryLocationVatliabilityYInMillimeters() {
		return deliveryLocationVatliabilityY;
	}

	public Integer getDeliveryLocationTaxXInMillimeters() {
		return deliveryLocationTaxX;
	}

	public Integer getDeliveryLocationTaxYInMillimeters() {
		return deliveryLocationTaxY;
	}

	public Integer getAffiliateXInMillimeters() {
		return affiliateX;
	}

	public Integer getAffiliateYInMillimeters() {
		return affiliateY;
	}

	public Integer getOrderXInMillimeters() {
		return orderX;
	}

	public Integer getOrderYInMillimeters() {
		return orderY;
	}

	public Integer getIssuerGlnXInMillimeters() {
		return issuerGlnX;
	}

	public Integer getIssuerGlnYInMillimeters() {
		return issuerGlnY;
	}

	public Integer getDeliveryLocationGlnXInMillimeters() {
		return deliveryLocationGlnX;
	}

	public Integer getDeliveryLocationGlnYInMillimeters() {
		return deliveryLocationGlnY;
	}

	public Integer getProductDetailsYInMillimeters() {
		return productDetailsY;
	}

	public Integer getProductDescriptionXInMillimeters() {
		return productDescriptionX;
	}

	public Integer getProductMonodrugXInMillimeters() {
		return productMonodrugX;
	}

	public Integer getProductBrandXInMillimeters() {
		return productBrandX;
	}

	public Integer getProductAmountXInMillimeters() {
		return productAmountX;
	}

	public Integer getProductBatchExpirationdateXInMillimeters() {
		return productBatchExpirationdateX;
	}

	public Integer getSerialColumn1XInMillimeters() {
		return serialColumn1X;
	}

	public Integer getSerialColumn2XInMillimeters() {
		return serialColumn2X;
	}

	public Integer getSerialColumn3XInMillimeters() {
		return serialColumn3X;
	}

	public Integer getSerialColumn4XInMillimeters() {
		return serialColumn4X;
	}

	public Integer getNumberOfItemsXInMillimeters() {
		return numberOfItemsX;
	}

	public Integer getNumberOfItemsYInMillimeters() {
		return numberOfItemsY;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public void setNumberXInMillimeters(Integer numberX) {
		this.numberX = numberX;
	}

	public void setNumberYInMillimeters(Integer numberY) {
		this.numberY = numberY;
	}

	public void setNumberPrint(Boolean numberPrint) {
		this.numberPrint = numberPrint;
	}

	public void setDateXInMillimeters(Integer dateX) {
		this.dateX = dateX;
	}

	public void setDateYInMillimeters(Integer dateY) {
		this.dateY = dateY;
	}

	public void setDatePrint(Boolean datePrint) {
		this.datePrint = datePrint;
	}

	public void setIssuerCorporateNameXInMillimeters(Integer issuerCorporateNameX) {
		this.issuerCorporateNameX = issuerCorporateNameX;
	}

	public void setIssuerCorporateNameYInMillimeters(Integer issuerCorporateNameY) {
		this.issuerCorporateNameY = issuerCorporateNameY;
	}

	public void setIssuerCorporateNamePrint(Boolean issuerCorporateNamePrint) {
		this.issuerCorporateNamePrint = issuerCorporateNamePrint;
	}

	public void setIssuerAddressXInMillimeters(Integer issuerAddressX) {
		this.issuerAddressX = issuerAddressX;
	}

	public void setIssuerAddressYInMillimeters(Integer issuerAddressY) {
		this.issuerAddressY = issuerAddressY;
	}

	public void setIssuerAddressPrint(Boolean issuerAddressPrint) {
		this.issuerAddressPrint = issuerAddressPrint;
	}

	public void setIssuerLocalityXInMillimeters(Integer issuerLocalityX) {
		this.issuerLocalityX = issuerLocalityX;
	}

	public void setIssuerLocalityYInMillimeters(Integer issuerLocalityY) {
		this.issuerLocalityY = issuerLocalityY;
	}

	public void setIssuerLocalityPrint(Boolean issuerLocalityPrint) {
		this.issuerLocalityPrint = issuerLocalityPrint;
	}

	public void setIssuerZipcodeXInMillimeters(Integer issuerZipcodeX) {
		this.issuerZipcodeX = issuerZipcodeX;
	}

	public void setIssuerZipcodeYInMillimeters(Integer issuerZipcodeY) {
		this.issuerZipcodeY = issuerZipcodeY;
	}

	public void setIssuerZipcodePrint(Boolean issuerZipcodePrint) {
		this.issuerZipcodePrint = issuerZipcodePrint;
	}

	public void setIssuerProvinceXInMillimeters(Integer issuerProvinceX) {
		this.issuerProvinceX = issuerProvinceX;
	}

	public void setIssuerProvinceYInMillimeters(Integer issuerProvinceY) {
		this.issuerProvinceY = issuerProvinceY;
	}

	public void setIssuerProvincePrint(Boolean issuerProvincePrint) {
		this.issuerProvincePrint = issuerProvincePrint;
	}

	public void setIssuerVatliabilityXInMillimeters(Integer issuerVatliabilityX) {
		this.issuerVatliabilityX = issuerVatliabilityX;
	}

	public void setIssuerVatliabilityYInMillimeters(Integer issuerVatliabilityY) {
		this.issuerVatliabilityY = issuerVatliabilityY;
	}

	public void setIssuerVatliabilityPrint(Boolean issuerVatliabilityPrint) {
		this.issuerVatliabilityPrint = issuerVatliabilityPrint;
	}

	public void setIssuerTaxXInMillimeters(Integer issuerTaxX) {
		this.issuerTaxX = issuerTaxX;
	}

	public void setIssuerTaxYInMillimeters(Integer issuerTaxY) {
		this.issuerTaxY = issuerTaxY;
	}

	public void setIssuerTaxPrint(Boolean issuerTaxPrint) {
		this.issuerTaxPrint = issuerTaxPrint;
	}

	public void setDeliveryLocationCorporateNameXInMillimeters(Integer deliveryLocationCorporateNameX) {
		this.deliveryLocationCorporateNameX = deliveryLocationCorporateNameX;
	}

	public void setDeliveryLocationCorporateNameYInMillimeters(Integer deliveryLocationCorporateNameY) {
		this.deliveryLocationCorporateNameY = deliveryLocationCorporateNameY;
	}

	public void setDeliveryLocationCorporateNamePrint(Boolean deliveryLocationCorporateNamePrint) {
		this.deliveryLocationCorporateNamePrint = deliveryLocationCorporateNamePrint;
	}

	public void setDeliveryLocationAddressXInMillimeters(Integer deliveryLocationAddressX) {
		this.deliveryLocationAddressX = deliveryLocationAddressX;
	}

	public void setDeliveryLocationAddressYInMillimeters(Integer deliveryLocationAddressY) {
		this.deliveryLocationAddressY = deliveryLocationAddressY;
	}

	public void setDeliveryLocationAddressPrint(Boolean deliveryLocationAddressPrint) {
		this.deliveryLocationAddressPrint = deliveryLocationAddressPrint;
	}

	public void setDeliveryLocationLocalityXInMillimeters(Integer deliveryLocationLocalityX) {
		this.deliveryLocationLocalityX = deliveryLocationLocalityX;
	}

	public void setDeliveryLocationLocalityYInMillimeters(Integer deliveryLocationLocalityY) {
		this.deliveryLocationLocalityY = deliveryLocationLocalityY;
	}

	public void setDeliveryLocationLocalityPrint(Boolean deliveryLocationLocalityPrint) {
		this.deliveryLocationLocalityPrint = deliveryLocationLocalityPrint;
	}

	public void setDeliveryLocationZipcodeXInMillimeters(Integer deliveryLocationZipcodeX) {
		this.deliveryLocationZipcodeX = deliveryLocationZipcodeX;
	}

	public void setDeliveryLocationZipcodeYInMillimeters(Integer deliveryLocationZipcodeY) {
		this.deliveryLocationZipcodeY = deliveryLocationZipcodeY;
	}

	public void setDeliveryLocationZipcodePrint(Boolean deliveryLocationZipcodePrint) {
		this.deliveryLocationZipcodePrint = deliveryLocationZipcodePrint;
	}

	public void setDeliveryLocationProvinceXInMillimeters(Integer deliveryLocationProvinceX) {
		this.deliveryLocationProvinceX = deliveryLocationProvinceX;
	}

	public void setDeliveryLocationProvinceYInMillimeters(Integer deliveryLocationProvinceY) {
		this.deliveryLocationProvinceY = deliveryLocationProvinceY;
	}

	public void setDeliveryLocationProvincePrint(Boolean deliveryLocationProvincePrint) {
		this.deliveryLocationProvincePrint = deliveryLocationProvincePrint;
	}

	public void setDeliveryLocationVatliabilityXInMillimeters(Integer deliveryLocationVatliabilityX) {
		this.deliveryLocationVatliabilityX = deliveryLocationVatliabilityX;
	}

	public void setDeliveryLocationVatliabilityYInMillimeters(Integer deliveryLocationVatliabilityY) {
		this.deliveryLocationVatliabilityY = deliveryLocationVatliabilityY;
	}

	public void setDeliveryLocationVatliabilityPrint(Boolean deliveryLocationVatliabilityPrint) {
		this.deliveryLocationVatliabilityPrint = deliveryLocationVatliabilityPrint;
	}

	public void setDeliveryLocationTaxXInMillimeters(Integer deliveryLocationTaxX) {
		this.deliveryLocationTaxX = deliveryLocationTaxX;
	}

	public void setDeliveryLocationTaxYInMillimeters(Integer deliveryLocationTaxY) {
		this.deliveryLocationTaxY = deliveryLocationTaxY;
	}

	public void setDeliveryLocationTaxPrint(Boolean deliveryLocationTaxPrint) {
		this.deliveryLocationTaxPrint = deliveryLocationTaxPrint;
	}

	public void setAffiliateXInMillimeters(Integer affiliateX) {
		this.affiliateX = affiliateX;
	}

	public void setAffiliateYInMillimeters(Integer affiliateY) {
		this.affiliateY = affiliateY;
	}

	public void setAffiliatePrint(Boolean affiliatePrint) {
		this.affiliatePrint = affiliatePrint;
	}

	public void setOrderXInMillimeters(Integer orderX) {
		this.orderX = orderX;
	}

	public void setOrderYInMillimeters(Integer orderY) {
		this.orderY = orderY;
	}

	public void setOrderPrint(Boolean orderPrint) {
		this.orderPrint = orderPrint;
	}

	public void setIssuerGlnXInMillimeters(Integer issuerGlnX) {
		this.issuerGlnX = issuerGlnX;
	}

	public void setIssuerGlnYInMillimeters(Integer issuerGlnY) {
		this.issuerGlnY = issuerGlnY;
	}

	public void setIssuerGlnPrint(Boolean issuerGlnPrint) {
		this.issuerGlnPrint = issuerGlnPrint;
	}

	public void setDeliveryLocationGlnXInMillimeters(Integer deliveryLocationGlnX) {
		this.deliveryLocationGlnX = deliveryLocationGlnX;
	}

	public void setDeliveryLocationGlnYInMillimeters(Integer deliveryLocationGlnY) {
		this.deliveryLocationGlnY = deliveryLocationGlnY;
	}

	public void setDeliveryLocationGlnPrint(Boolean deliveryLocationGlnPrint) {
		this.deliveryLocationGlnPrint = deliveryLocationGlnPrint;
	}

	public void setProductDetailsYInMillimeters(Integer productDetailsY) {
		this.productDetailsY = productDetailsY;
	}

	public void setProductDescriptionXInMillimeters(Integer productDescriptionX) {
		this.productDescriptionX = productDescriptionX;
	}

	public void setProductDescriptionPrint(Boolean productDescriptionPrint) {
		this.productDescriptionPrint = productDescriptionPrint;
	}

	public void setProductMonodrugXInMillimeters(Integer productMonodrugX) {
		this.productMonodrugX = productMonodrugX;
	}

	public void setProductMonodrugPrint(Boolean productMonodrugPrint) {
		this.productMonodrugPrint = productMonodrugPrint;
	}

	public void setProductBrandXInMillimeters(Integer productBrandX) {
		this.productBrandX = productBrandX;
	}

	public void setProductBrandPrint(Boolean productBrandPrint) {
		this.productBrandPrint = productBrandPrint;
	}

	public void setProductAmountXInMillimeters(Integer productAmountX) {
		this.productAmountX = productAmountX;
	}

	public void setProductAmountPrint(Boolean productAmountPrint) {
		this.productAmountPrint = productAmountPrint;
	}

	public void setProductBatchExpirationdateXInMillimeters(Integer productBatchExpirationdateX) {
		this.productBatchExpirationdateX = productBatchExpirationdateX;
	}

	public void setProductBatchExpirationdatePrint(Boolean productBatchExpirationdatePrint) {
		this.productBatchExpirationdatePrint = productBatchExpirationdatePrint;
	}

	public void setSerialColumn1XInMillimeters(Integer serialColumn1X) {
		this.serialColumn1X = serialColumn1X;
	}

	public void setSerialColumn2XInMillimeters(Integer serialColumn2X) {
		this.serialColumn2X = serialColumn2X;
	}

	public void setSerialColumn3XInMillimeters(Integer serialColumn3X) {
		this.serialColumn3X = serialColumn3X;
	}

	public void setSerialColumn4XInMillimeters(Integer serialColumn4X) {
		this.serialColumn4X = serialColumn4X;
	}

	public void setSerialColumn1Print(Boolean serialColumn1Print) {
		this.serialColumn1Print = serialColumn1Print;
	}

	public void setSerialColumn2Print(Boolean serialColumn2Print) {
		this.serialColumn2Print = serialColumn2Print;
	}

	public void setSerialColumn3Print(Boolean serialColumn3Print) {
		this.serialColumn3Print = serialColumn3Print;
	}

	public void setSerialColumn4Print(Boolean serialColumn4Print) {
		this.serialColumn4Print = serialColumn4Print;
	}

	public void setNumberOfItemsXInMillimeters(Integer numberOfItemsX) {
		this.numberOfItemsX = numberOfItemsX;
	}

	public void setNumberOfItemsYInMillimeters(Integer numberOfItemsY) {
		this.numberOfItemsY = numberOfItemsY;
	}

	public void setNumberOfItemsPrint(Boolean numberOfItemsPrint) {
		this.numberOfItemsPrint = numberOfItemsPrint;
	}
}