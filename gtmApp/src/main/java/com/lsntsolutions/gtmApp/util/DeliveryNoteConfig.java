package com.lsntsolutions.gtmApp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeliveryNoteConfig {

	@Value("${font.size}")
	private Integer fontSize;

	@Value("${number.x}")
	private Integer numberX;
	@Value("${number.y}")
	private Integer numberY;

	@Value("${date.x}")
	private Integer dateX;
	@Value("${date.y}")
	private Integer dateY;

	@Value("${issuer.corporateName.x}")
	private Integer issuerCorporateNameX;
	@Value("${issuer.corporateName.y}")
	private Integer issuerCorporateNameY;

	@Value("${issuer.address.x}")
	private Integer issuerAddressX;
	@Value("${issuer.address.y}")
	private Integer issuerAddressY;

	@Value("${issuer.locality.x}")
	private Integer issuerLocalityX;
	@Value("${issuer.locality.y}")
	private Integer issuerLocalityY;

	@Value("${issuer.zipcode.x}")
	private Integer issuerZipcodeX;
	@Value("${issuer.zipcode.y}")
	private Integer issuerZipcodeY;

	@Value("${issuer.province.x}")
	private Integer issuerProvinceX;
	@Value("${issuer.province.y}")
	private Integer issuerProvinceY;

	@Value("${issuer.vatliability.x}")
	private Integer issuerVatliabilityX;
	@Value("${issuer.vatliability.y}")
	private Integer issuerVatliabilityY;

	@Value("${issuer.tax.x}")
	private Integer issuerTaxX;
	@Value("${issuer.tax.y}")
	private Integer issuerTaxY;

	@Value("${deliveryLocation.corporateName.x}")
	private Integer deliveryLocationCorporateNameX;
	@Value("${deliveryLocation.corporateName.y}")
	private Integer deliveryLocationCorporateNameY;

	@Value("${deliveryLocation.address.x}")
	private Integer deliveryLocationAddressX;
	@Value("${deliveryLocation.address.y}")
	private Integer deliveryLocationAddressY;

	@Value("${deliveryLocation.locality.x}")
	private Integer deliveryLocationLocalityX;
	@Value("${deliveryLocation.locality.y}")
	private Integer deliveryLocationLocalityY;

	@Value("${deliveryLocation.zipcode.x}")
	private Integer deliveryLocationZipcodeX;
	@Value("${deliveryLocation.zipcode.y}")
	private Integer deliveryLocationZipcodeY;

	@Value("${deliveryLocation.province.x}")
	private Integer deliveryLocationProvinceX;
	@Value("${deliveryLocation.province.y}")
	private Integer deliveryLocationProvinceY;

	@Value("${deliveryLocation.vatliability.x}")
	private Integer deliveryLocationVatliabilityX;
	@Value("${deliveryLocation.vatliability.y}")
	private Integer deliveryLocationVatliabilityY;

	@Value("${deliveryLocation.tax.x}")
	private Integer deliveryLocationTaxX;
	@Value("${deliveryLocation.tax.y}")
	private Integer deliveryLocationTaxY;

	@Value("${affiliate.x}")
	private Integer affiliateX;
	@Value("${affiliate.y}")
	private Integer affiliateY;

	@Value("${order.x}")
	private Integer orderX;
	@Value("${order.y}")
	private Integer orderY;

	@Value("${issuer.gln.x}")
	private Integer issuerGlnX;
	@Value("${issuer.gln.y}")
	private Integer issuerGlnY;

	@Value("${deliveryLocation.gln.x}")
	private Integer deliveryLocationGlnX;
	@Value("${deliveryLocation.gln.y}")
	private Integer deliveryLocationGlnY;

	@Value("${product.details.y}")
	private Integer productDetailsY;

	@Value("${product.description.x}")
	private Integer productDescriptionX;

	@Value("${product.monodrug.x}")
	private Integer productMonodrugX;

	@Value("${product.brand.x}")
	private Integer productBrandX;

	@Value("${product.amount.x}")
	private Integer productAmountX;

	@Value("${product.batchExpirationdate.x}")
	private Integer productBatchExpirationdateX;

	@Value("${serial.column1.x}")
	private Integer serialColumn1X;
	@Value("${serial.column2.x}")
	private Integer serialColumn2X;
	@Value("${serial.column3.x}")
	private Integer serialColumn3X;
	@Value("${serial.column4.x}")
	private Integer serialColumn4X;

	@Value("${numberOfItems.x}")
	private Integer numberOfItemsX;
	@Value("${numberOfItems.y}")
	private Integer numberOfItemsY;

	public Integer getFontSize() {
		return fontSize;
	}

	public float getNumberX() {
		return numberX * 2.8346f;
	}

	public float getNumberY() {
		return (297.0f - numberY) * 2.8346f;
	}

	public float getDateX() {
		return dateX * 2.8346f;
	}

	public float getDateY() {
		return (297.0f - dateY) * 2.8346f;
	}

	public float getIssuerCorporateNameX() {
		return issuerCorporateNameX * 2.8346f;
	}

	public float getIssuerCorporateNameY() {
		return (297.0f - issuerCorporateNameY) * 2.8346f;
	}

	public float getIssuerAddressX() {
		return issuerAddressX * 2.8346f;
	}

	public float getIssuerAddressY() {
		return (297.0f - issuerAddressY) * 2.8346f;
	}

	public float getIssuerLocalityX() {
		return issuerLocalityX * 2.8346f;
	}

	public float getIssuerLocalityY() {
		return (297.0f - issuerLocalityY) * 2.8346f;
	}

	public float getIssuerZipcodeX() {
		return issuerZipcodeX * 2.8346f;
	}

	public float getIssuerZipcodeY() {
		return (297.0f - issuerZipcodeY) * 2.8346f;
	}

	public float getIssuerProvinceX() {
		return issuerProvinceX * 2.8346f;
	}

	public float getIssuerProvinceY() {
		return (297.0f - issuerProvinceY) * 2.8346f;
	}

	public float getIssuerVatliabilityX() {
		return issuerVatliabilityX * 2.8346f;
	}

	public float getIssuerVatliabilityY() {
		return (297.0f - issuerVatliabilityY) * 2.8346f;
	}

	public float getIssuerTaxX() {
		return issuerTaxX * 2.8346f;
	}

	public float getIssuerTaxY() {
		return (297.0f - issuerTaxY) * 2.8346f;
	}

	public float getDeliveryLocationCorporateNameX() {
		return deliveryLocationCorporateNameX * 2.8346f;
	}

	public float getDeliveryLocationCorporateNameY() {
		return (297.0f - deliveryLocationCorporateNameY) * 2.8346f;
	}

	public float getDeliveryLocationAddressX() {
		return deliveryLocationAddressX * 2.8346f;
	}

	public float getDeliveryLocationAddressY() {
		return (297.0f - deliveryLocationAddressY) * 2.8346f;
	}

	public float getDeliveryLocationLocalityX() {
		return deliveryLocationLocalityX * 2.8346f;
	}

	public float getDeliveryLocationLocalityY() {
		return (297.0f - deliveryLocationLocalityY) * 2.8346f;
	}

	public float getDeliveryLocationZipcodeX() {
		return deliveryLocationZipcodeX * 2.8346f;
	}

	public float getDeliveryLocationZipcodeY() {
		return (297.0f - deliveryLocationZipcodeY) * 2.8346f;
	}

	public float getDeliveryLocationProvinceX() {
		return deliveryLocationProvinceX * 2.8346f;
	}

	public float getDeliveryLocationProvinceY() {
		return (297.0f - deliveryLocationProvinceY) * 2.8346f;
	}

	public float getDeliveryLocationVatliabilityX() {
		return deliveryLocationVatliabilityX * 2.8346f;
	}

	public float getDeliveryLocationVatliabilityY() {
		return (297.0f - deliveryLocationVatliabilityY) * 2.8346f;
	}

	public float getDeliveryLocationTaxX() {
		return deliveryLocationTaxX * 2.8346f;
	}

	public float getDeliveryLocationTaxY() {
		return (297.0f - deliveryLocationTaxY) * 2.8346f;
	}

	public float getAffiliateX() {
		return affiliateX * 2.8346f;
	}

	public float getAffiliateY() {
		return (297.0f - affiliateY) * 2.8346f;
	}

	public float getOrderX() {
		return orderX * 2.8346f;
	}

	public float getOrderY() {
		return (297.0f - orderY) * 2.8346f;
	}

	public float getIssuerGlnX() {
		return issuerGlnX * 2.8346f;
	}

	public float getIssuerGlnY() {
		return (297.0f - issuerGlnY) * 2.8346f;
	}

	public float getDeliveryLocationGlnX() {
		return deliveryLocationGlnX * 2.8346f;
	}

	public float getDeliveryLocationGlnY() {
		return (297.0f - deliveryLocationGlnY) * 2.8346f;
	}

	public float getProductDetailsY() {
		return (297.0f - productDetailsY) * 2.8346f;
	}

	public float getProductDescriptionX() {
		return productDescriptionX * 2.8346f;
	}

	public float getProductMonodrugX() {
		return productMonodrugX * 2.8346f;
	}

	public float getProductBrandX() {
		return productBrandX * 2.8346f;
	}

	public float getProductAmountX() {
		return productAmountX * 2.8346f;
	}

	public float getProductBatchExpirationdateX() {
		return productBatchExpirationdateX * 2.8346f;
	}

	public float getSerialColumn1X() {
		return serialColumn1X * 2.8346f;
	}

	public float getSerialColumn2X() {
		return serialColumn2X * 2.8346f;
	}

	public float getSerialColumn3X() {
		return serialColumn3X * 2.8346f;
	}

	public float getSerialColumn4X() {
		return serialColumn4X * 2.8346f;
	}

	public float getNumberOfItemsX() {
		return numberOfItemsX * 2.8346f;
	}

	public float getNumberOfItemsY() {
		return (297.0f - numberOfItemsY) * 2.8346f;
	}
}