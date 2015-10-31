package com.lsntsolutions.gtmApp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeliveryNoteConfig {

	// The coordinates are measured in points. 1 inch is divided into 72 points so that 1 Millimeter equals 2.8346 points.
	private static float MILLIMITER_TO_POINTS_FACTOR = 2.8346f;

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
}