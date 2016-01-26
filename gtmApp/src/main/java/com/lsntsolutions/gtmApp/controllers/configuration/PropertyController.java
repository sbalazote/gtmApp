package com.lsntsolutions.gtmApp.controllers.configuration;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.constant.DeliveryNoteConfigParam;
import com.lsntsolutions.gtmApp.dto.PropertyDTO;
import com.lsntsolutions.gtmApp.helper.EncryptionHelper;
import com.lsntsolutions.gtmApp.model.Agent;
import com.lsntsolutions.gtmApp.model.Property;
import com.lsntsolutions.gtmApp.model.Province;
import com.lsntsolutions.gtmApp.model.VATLiability;
import com.lsntsolutions.gtmApp.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class PropertyController {
	@Autowired
	private DeliveryNoteConfigService deliveryNoteConfigService; ;

	@Autowired
	private PropertyService PropertyService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private VATLiabilityService VATLiabilityService;

	@Autowired
	private ConceptService conceptService;

	@Autowired
	private AgreementService agreementService;

	@Autowired
	private ProviderService providerService;

	private static final Logger logger = Logger.getLogger(PropertyController.class);

	@RequestMapping(value = "/saveProperty", method = RequestMethod.POST)
	public @ResponseBody void saveProperty(@RequestBody PropertyDTO propertyDTO) throws Exception {
		this.PropertyService.save(this.buildModel(propertyDTO));
		this.updateDeliveryNoteConfig(propertyDTO);
		PropertyProvider.getInstance().setProp(PropertyProvider.NAME, propertyDTO.getName());
	}

	private void updateDeliveryNoteConfig(PropertyDTO propertyDTO) {
		Map<String, Integer> deliveryNoteConfigServiceAll = deliveryNoteConfigService.getAll();

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.FONT_SIZE.name(), propertyDTO.getFontSize());

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.NUMBER_X.name(), propertyDTO.getNumberX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.NUMBER_Y.name(), propertyDTO.getNumberY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.NUMBER_PRINT.name(), propertyDTO.getNumberPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DATE_X.name(), propertyDTO.getDateX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DATE_Y.name(), propertyDTO.getDateY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DATE_PRINT.name(), propertyDTO.getDatePrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_X.name(), propertyDTO.getIssuerCorporateNameX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_Y.name(), propertyDTO.getIssuerCorporateNameY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_PRINT.name(), propertyDTO.getIssuerCorporateNamePrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_ADDRESS_X.name(), propertyDTO.getIssuerAddressX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_ADDRESS_Y.name(), propertyDTO.getIssuerAddressY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_ADDRESS_PRINT.name(), propertyDTO.getIssuerAddressPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_LOCALITY_X.name(), propertyDTO.getIssuerLocalityX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_LOCALITY_Y.name(), propertyDTO.getIssuerLocalityY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_LOCALITY_PRINT.name(), propertyDTO.getIssuerLocalityPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_ZIPCODE_X.name(), propertyDTO.getIssuerZipcodeX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_ZIPCODE_Y.name(), propertyDTO.getIssuerZipcodeY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_ZIPCODE_PRINT.name(), propertyDTO.getIssuerZipcodePrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_PROVINCE_X.name(), propertyDTO.getIssuerProvinceX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_PROVINCE_Y.name(), propertyDTO.getIssuerProvinceY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_PROVINCE_PRINT.name(), propertyDTO.getIssuerProvincePrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_X.name(), propertyDTO.getIssuerVatliabilityX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_Y.name(), propertyDTO.getIssuerVatliabilityY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_PRINT.name(), propertyDTO.getIssuerVatliabilityPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_TAX_X.name(), propertyDTO.getIssuerTaxX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_TAX_Y.name(), propertyDTO.getIssuerTaxY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_TAX_PRINT.name(), propertyDTO.getIssuerTaxPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_GLN_X.name(), propertyDTO.getIssuerGlnX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_GLN_Y.name(), propertyDTO.getIssuerGlnY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ISSUER_GLN_PRINT.name(), propertyDTO.getIssuerGlnPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_X.name(), propertyDTO.getDeliveryLocationCorporateNameX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_Y.name(), propertyDTO.getDeliveryLocationCorporateNameY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_PRINT.name(), propertyDTO.getDeliveryLocationCorporateNamePrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_X.name(), propertyDTO.getDeliveryLocationAddressX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_Y.name(), propertyDTO.getDeliveryLocationAddressY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_PRINT.name(), propertyDTO.getDeliveryLocationAddressPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_X.name(), propertyDTO.getDeliveryLocationLocalityX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_Y.name(), propertyDTO.getDeliveryLocationLocalityY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_PRINT.name(), propertyDTO.getDeliveryLocationLocalityPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_X.name(), propertyDTO.getDeliveryLocationZipcodeX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_Y.name(), propertyDTO.getDeliveryLocationZipcodeY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_PRINT.name(), propertyDTO.getDeliveryLocationZipcodePrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_X.name(), propertyDTO.getDeliveryLocationProvinceX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_Y.name(), propertyDTO.getDeliveryLocationProvinceY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_PRINT.name(), propertyDTO.getDeliveryLocationProvincePrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_X.name(), propertyDTO.getDeliveryLocationVatliabilityX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_Y.name(), propertyDTO.getDeliveryLocationVatliabilityY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_PRINT.name(), propertyDTO.getDeliveryLocationVatliabilityPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_X.name(), propertyDTO.getDeliveryLocationTaxX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_Y.name(), propertyDTO.getDeliveryLocationTaxY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_PRINT.name(), propertyDTO.getDeliveryLocationTaxPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_X.name(), propertyDTO.getDeliveryLocationGlnX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_Y.name(), propertyDTO.getDeliveryLocationGlnY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_PRINT.name(), propertyDTO.getDeliveryLocationGlnPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.AFFILIATE_X.name(), propertyDTO.getAffiliateX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.AFFILIATE_Y.name(), propertyDTO.getAffiliateY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.AFFILIATE_PRINT.name(), propertyDTO.getAffiliatePrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ORDER_X.name(), propertyDTO.getOrderX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ORDER_Y.name(), propertyDTO.getOrderY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.ORDER_PRINT.name(), propertyDTO.getOrderPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name(), propertyDTO.getProductDetailsY());

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_DESCRIPTION_X.name(), propertyDTO.getProductDescriptionX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_DESCRIPTION_PRINT.name(), propertyDTO.getProductDescriptionPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_MONODRUG_X.name(), propertyDTO.getProductMonodrugX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_MONODRUG_PRINT.name(), propertyDTO.getProductMonodrugPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_BRAND_X.name(), propertyDTO.getProductBrandX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_BRAND_PRINT.name(), propertyDTO.getProductBrandPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_AMOUNT_X.name(), propertyDTO.getProductAmountX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_AMOUNT_PRINT.name(), propertyDTO.getProductAmountPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_X.name(), propertyDTO.getProductBatchExpirationdateX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_PRINT.name(), propertyDTO.getProductBatchExpirationdatePrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name(), propertyDTO.getSerialColumn1X());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name(), propertyDTO.getSerialColumn1Print() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name(), propertyDTO.getSerialColumn2X());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name(), propertyDTO.getSerialColumn2Print() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.SERIAL_COLUMN3_X.name(), propertyDTO.getSerialColumn3X());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.SERIAL_COLUMN3_PRINT.name(), propertyDTO.getSerialColumn3Print() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.SERIAL_COLUMN4_X.name(), propertyDTO.getSerialColumn4X());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.SERIAL_COLUMN4_PRINT.name(), propertyDTO.getSerialColumn4Print() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.NUMBEROFITEMS_X.name(), propertyDTO.getNumberOfItemsX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.NUMBEROFITEMS_Y.name(), propertyDTO.getNumberOfItemsY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.NUMBEROFITEMS_PRINT.name(), propertyDTO.getNumberOfItemsPrint() ? 1 : 0);

		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.LOGISTICS_OPERATOR_X.name(), propertyDTO.getLogisticsOperatorX());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.LOGISTICS_OPERATOR_Y.name(), propertyDTO.getLogisticsOperatorY());
		deliveryNoteConfigServiceAll.put(DeliveryNoteConfigParam.LOGISTICS_OPERATOR_PRINT.name(), propertyDTO.getLogisticsOperatorPrint() ? 1 : 0);

		deliveryNoteConfigService.saveAll(deliveryNoteConfigServiceAll);
	}
	
	private Property buildModel(PropertyDTO propertyDTO) {
		Property property = new Property();
		if (propertyDTO.getId() != null) {
			property.setId(propertyDTO.getId());
		}

		property.setCode(propertyDTO.getCode());
		property.setName(propertyDTO.getName());
		property.setTaxId(propertyDTO.getTaxId());
		property.setCorporateName(propertyDTO.getCorporateName());
		property.setProvince(this.provinceService.get(propertyDTO.getProvinceId()));
		property.setLocality(propertyDTO.getLocality());
		property.setAddress(propertyDTO.getAddress());
		property.setZipCode(propertyDTO.getZipCode());
		property.setPhone(propertyDTO.getPhone());
		property.setMail(propertyDTO.getMail());
		property.setGln(propertyDTO.getGln());
		property.setAgent(this.agentService.get(propertyDTO.getAgentId()));
		property.setDaysAgoPendingTransactions(propertyDTO.getDaysAgoPendingTransactions());
		property.setSelfSerializedTagFilepath(propertyDTO.getSelfSerializedTagFilepath());
		property.setANMATName(propertyDTO.getANMATName());

		Property Property = this.PropertyService.get();

		if (!propertyDTO.getANMATPassword().isEmpty()) {
			property.setANMATPassword(EncryptionHelper.AESEncrypt(propertyDTO.getANMATPassword()));
		} else {
			property.setANMATPassword(Property.getANMATPassword());
		}

		if (propertyDTO.isInformProxy()) {
			property.setProxy(propertyDTO.getProxy());
			property.setProxyPort(propertyDTO.getProxyNumber());
			property.setHasProxy(propertyDTO.isInformProxy());
		} else {
			property.setProxy("");
			property.setProxyPort("");
			property.setHasProxy(false);
		}

		if (propertyDTO.getStartTraceConceptSelectId() != null) {
			property.setStartTraceConcept(this.conceptService.get(propertyDTO.getStartTraceConceptSelectId()));
		}
		if (propertyDTO.getSupplyingConceptSelectId() != null) {
			property.setSupplyingConcept(this.conceptService.get(propertyDTO.getSupplyingConceptSelectId()));
		}
		property.setLastTag(Property.getLastTag());

		property.setInformAnmat(propertyDTO.isInformAnmat());
		property.setVATLiability(this.VATLiabilityService.get(propertyDTO.getVATLiabilityId()));
		property.setProvisioningRequireAuthorization(propertyDTO.isProvisioningRequireAuthorization());
		property.setPrintPickingList(propertyDTO.isPrintPickingList());
		property.setPrintPickingCoordinateXStart(propertyDTO.getPickingListX());
		property.setPrintPickingCoordinateYStart(propertyDTO.getPickingListY());

		return property;
	}

	@RequestMapping(value = "/updateProperty", method = RequestMethod.GET)
	public String updateProperty(ModelMap modelMap, @RequestParam Map<String, String> parameters) throws Exception {
		Property property = this.PropertyService.get();

		modelMap.put("id", property.getId());
		modelMap.put("code", property.getCode());
		modelMap.put("name", property.getName());
		modelMap.put("taxId", property.getTaxId());
		modelMap.put("corporateName", property.getCorporateName());

		Province selectedProvince = property.getProvince();
		List<Province> allProvinces = this.provinceService.getAll();
		allProvinces.remove(selectedProvince);
		modelMap.put("provinces", allProvinces);
		modelMap.put("selectedProvince", selectedProvince);

		modelMap.put("locality", property.getLocality());
		modelMap.put("address", property.getAddress());
		modelMap.put("zipCode", property.getZipCode());
		modelMap.put("phone", property.getPhone());
		modelMap.put("mail", property.getMail());
		modelMap.put("gln", property.getGln());

		Agent selectedAgent = property.getAgent();
		List<Agent> allAgents = this.agentService.getAll();
		allAgents.remove(selectedAgent);
		modelMap.put("agents", allAgents);
		modelMap.put("selectedAgent", selectedAgent);

		modelMap.put("daysAgoPendingTransactions", property.getDaysAgoPendingTransactions());

		modelMap.put("selfSerializedTagFilepath", property.getSelfSerializedTagFilepath());

		modelMap.put("ANMATName", property.getANMATName());

		if (property.getStartTraceConcept() != null) {
			modelMap.put("selectedStartTraceConcept", property.getStartTraceConcept().getId());
		}
		modelMap.put("concepts", this.conceptService.getAllActives(true));

		if (property.getSupplyingConcept() != null) {
			modelMap.put("selectedSupplyingConcept", property.getSupplyingConcept().getId());
		}

		modelMap.put("deliveryNoteconcepts", this.conceptService.getAllActives(false));

		modelMap.put("proxy", property.getProxy());
		modelMap.put("proxyPort", property.getProxyPort());
		modelMap.put("informProxy", property.isHasProxy());
		modelMap.put("informAnmat", property.isInformAnmat());
		modelMap.put("provisioningRequireAuthorization", property.isProvisioningRequireAuthorization());
		modelMap.put("printPickingList", property.isPrintPickingList());

		modelMap.put("pickingListX", property.getPrintPickingCoordinateXStart());
		modelMap.put("pickingListY", property.getPrintPickingCoordinateYStart());
		modelMap.put("printPickingList",  property.isPrintPickingList() ? 1 : 0);

		VATLiability selectedVATLiability = property.getVATLiability();
		List<VATLiability> allVATLiabilities = this.VATLiabilityService.getAll();
		allVATLiabilities.remove(selectedVATLiability);
		modelMap.put("VATLiabilities", allVATLiabilities);
		modelMap.put("selectedVATLiability", selectedVATLiability);

		Map<String, Integer> deliveryNoteConfigServiceAll = deliveryNoteConfigService.getAll();

		modelMap.put("fontSize", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.FONT_SIZE.name()));

		modelMap.put("numberX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.NUMBER_X.name()));
		modelMap.put("numberY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.NUMBER_Y.name()));
		modelMap.put("numberPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.NUMBER_PRINT.name()));

		modelMap.put("dateX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DATE_X.name()));
		modelMap.put("dateY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DATE_Y.name()));
		modelMap.put("datePrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DATE_PRINT.name()));

		modelMap.put("issuerCorporateNameX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_X.name()));
		modelMap.put("issuerCorporateNameY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_Y.name()));
		modelMap.put("issuerCorporateNamePrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_PRINT.name()));

		modelMap.put("issuerAddressX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_ADDRESS_X.name()));
		modelMap.put("issuerAddressY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_ADDRESS_Y.name()));
		modelMap.put("issuerAddressPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_ADDRESS_PRINT.name()));

		modelMap.put("issuerLocalityX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_LOCALITY_X.name()));
		modelMap.put("issuerLocalityY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_LOCALITY_Y.name()));
		modelMap.put("issuerLocalityPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_LOCALITY_PRINT.name()));

		modelMap.put("issuerZipcodeX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_ZIPCODE_X.name()));
		modelMap.put("issuerZipcodeY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_ZIPCODE_Y.name()));
		modelMap.put("issuerZipcodePrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_ZIPCODE_PRINT.name()));

		modelMap.put("issuerProvinceX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_PROVINCE_X.name()));
		modelMap.put("issuerProvinceY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_PROVINCE_Y.name()));
		modelMap.put("issuerProvincePrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_PROVINCE_PRINT.name()));

		modelMap.put("issuerVatliabilityX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_X.name()));
		modelMap.put("issuerVatliabilityY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_Y.name()));
		modelMap.put("issuerVatliabilityPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_PRINT.name()));

		modelMap.put("issuerTaxX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_TAX_X.name()));
		modelMap.put("issuerTaxY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_TAX_Y.name()));
		modelMap.put("issuerTaxPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_TAX_PRINT.name()));

		modelMap.put("issuerGlnX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_GLN_X.name()));
		modelMap.put("issuerGlnY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_GLN_Y.name()));
		modelMap.put("issuerGlnPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ISSUER_GLN_PRINT.name()));

		modelMap.put("deliveryLocationCorporateNameX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_X.name()));
		modelMap.put("deliveryLocationCorporateNameY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_Y.name()));
		modelMap.put("deliveryLocationCorporateNamePrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_PRINT.name()));

		modelMap.put("deliveryLocationAddressX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_X.name()));
		modelMap.put("deliveryLocationAddressY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_Y.name()));
		modelMap.put("deliveryLocationAddressPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_PRINT.name()));

		modelMap.put("deliveryLocationLocalityX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_X.name()));
		modelMap.put("deliveryLocationLocalityY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_Y.name()));
		modelMap.put("deliveryLocationLocalityPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_PRINT.name()));

		modelMap.put("deliveryLocationZipcodeX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_X.name()));
		modelMap.put("deliveryLocationZipcodeY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_Y.name()));
		modelMap.put("deliveryLocationZipcodePrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_PRINT.name()));

		modelMap.put("deliveryLocationProvinceX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_X.name()));
		modelMap.put("deliveryLocationProvinceY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_Y.name()));
		modelMap.put("deliveryLocationProvincePrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_PRINT.name()));

		modelMap.put("deliveryLocationVatliabilityX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_X.name()));
		modelMap.put("deliveryLocationVatliabilityY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_Y.name()));
		modelMap.put("deliveryLocationVatliabilityPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_PRINT.name()));

		modelMap.put("deliveryLocationTaxX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_X.name()));
		modelMap.put("deliveryLocationTaxY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_Y.name()));
		modelMap.put("deliveryLocationTaxPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_PRINT.name()));

		modelMap.put("deliveryLocationGlnX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_X.name()));
		modelMap.put("deliveryLocationGlnY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_Y.name()));
		modelMap.put("deliveryLocationGlnPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_PRINT.name()));

		modelMap.put("affiliateX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.AFFILIATE_X.name()));
		modelMap.put("affiliateY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.AFFILIATE_Y.name()));
		modelMap.put("affiliatePrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.AFFILIATE_PRINT.name()));

		modelMap.put("orderX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ORDER_X.name()));
		modelMap.put("orderY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ORDER_Y.name()));
		modelMap.put("orderPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.ORDER_PRINT.name()));

		modelMap.put("productDetailsY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()));

		modelMap.put("productDescriptionX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_DESCRIPTION_X.name()));
		modelMap.put("productDescriptionPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_DESCRIPTION_PRINT.name()));
		
		modelMap.put("productMonodrugX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_MONODRUG_X.name()));
		modelMap.put("productMonodrugPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_MONODRUG_PRINT.name()));

		modelMap.put("productBrandX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_BRAND_X.name()));
		modelMap.put("productBrandPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_BRAND_PRINT.name()));

		modelMap.put("productAmountX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_X.name()));
		modelMap.put("productAmountPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_PRINT.name()));

		modelMap.put("productBatchExpirationdateX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_X.name()));
		modelMap.put("productBatchExpirationdatePrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_PRINT.name()));

		modelMap.put("serialColumn1X", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()));
		modelMap.put("serialColumn1Print", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()));

		modelMap.put("serialColumn2X", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()));
		modelMap.put("serialColumn2Print", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()));

		modelMap.put("serialColumn3X", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_X.name()));
		modelMap.put("serialColumn3Print", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_PRINT.name()));

		modelMap.put("serialColumn4X", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.SERIAL_COLUMN4_X.name()));
		modelMap.put("serialColumn4Print", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.SERIAL_COLUMN4_PRINT.name()));

		modelMap.put("numberOfItemsX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.NUMBEROFITEMS_X.name()));
		modelMap.put("numberOfItemsY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.NUMBEROFITEMS_Y.name()));
		modelMap.put("numberOfItemsPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.NUMBEROFITEMS_PRINT.name()));

		modelMap.put("logisticsOperatorX", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.LOGISTICS_OPERATOR_X.name()));
		modelMap.put("logisticsOperatorY", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.LOGISTICS_OPERATOR_Y.name()));
		modelMap.put("logisticsOperatorPrint", deliveryNoteConfigServiceAll.get(DeliveryNoteConfigParam.LOGISTICS_OPERATOR_PRINT.name()));

		return "updateProperty";
	}

	@RequestMapping(value = "/getProperties", method = RequestMethod.GET)
	@ResponseBody
	public Property getProperties() throws IOException {
		return this.PropertyService.get();
	}


	@RequestMapping(value = "/importStock", method = RequestMethod.GET)
	public String importStock(ModelMap modelMap, @RequestParam Map<String, String> parameters) throws Exception {
		modelMap.put("concepts",this.conceptService.getAll());
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("providers", this.providerService.getAll());
		return "importStock";
	}
}
