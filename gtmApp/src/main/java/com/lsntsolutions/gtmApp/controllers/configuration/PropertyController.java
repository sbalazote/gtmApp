package com.lsntsolutions.gtmApp.controllers.configuration;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.dto.PropertyDTO;
import com.lsntsolutions.gtmApp.helper.EncryptionHelper;
import com.lsntsolutions.gtmApp.model.Agent;
import com.lsntsolutions.gtmApp.model.Property;
import com.lsntsolutions.gtmApp.model.Province;
import com.lsntsolutions.gtmApp.model.VATLiability;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.DeliveryNoteConfig;
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
	private PropertyService PropertyService;

	@Autowired
	private DeliveryNoteConfig deliveryNoteConfig;

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
		this.deliveryNoteConfig.setFontSize(propertyDTO.getFontSize());
		this.deliveryNoteConfig.setNumberXInMillimeters(propertyDTO.getNumberX());
		this.deliveryNoteConfig.setNumberYInMillimeters(propertyDTO.getNumberY());
		this.deliveryNoteConfig.setNumberPrint(propertyDTO.getNumberPrint());
		this.deliveryNoteConfig.setDateXInMillimeters(propertyDTO.getDateX());
		this.deliveryNoteConfig.setDateYInMillimeters(propertyDTO.getDateY());
		this.deliveryNoteConfig.setDatePrint(propertyDTO.getDatePrint());
		this.deliveryNoteConfig.setIssuerCorporateNameXInMillimeters(propertyDTO.getIssuerCorporateNameX());
		this.deliveryNoteConfig.setIssuerCorporateNameYInMillimeters(propertyDTO.getIssuerCorporateNameY());
		this.deliveryNoteConfig.setIssuerCorporateNamePrint(propertyDTO.getIssuerCorporateNamePrint());
		this.deliveryNoteConfig.setIssuerAddressXInMillimeters(propertyDTO.getIssuerAddressX());
		this.deliveryNoteConfig.setIssuerAddressYInMillimeters(propertyDTO.getIssuerAddressY());
		this.deliveryNoteConfig.setIssuerAddressPrint(propertyDTO.getIssuerAddressPrint());
		this.deliveryNoteConfig.setIssuerLocalityXInMillimeters(propertyDTO.getIssuerLocalityX());
		this.deliveryNoteConfig.setIssuerLocalityYInMillimeters(propertyDTO.getIssuerLocalityY());
		this.deliveryNoteConfig.setIssuerLocalityPrint(propertyDTO.getIssuerLocalityPrint());
		this.deliveryNoteConfig.setIssuerZipcodeXInMillimeters(propertyDTO.getIssuerZipcodeX());
		this.deliveryNoteConfig.setIssuerZipcodeYInMillimeters(propertyDTO.getIssuerZipcodeY());
		this.deliveryNoteConfig.setIssuerZipcodePrint(propertyDTO.getIssuerZipcodePrint());
		this.deliveryNoteConfig.setIssuerProvinceXInMillimeters(propertyDTO.getIssuerProvinceX());
		this.deliveryNoteConfig.setIssuerProvinceYInMillimeters(propertyDTO.getIssuerProvinceY());
		this.deliveryNoteConfig.setIssuerProvincePrint(propertyDTO.getIssuerProvincePrint());
		this.deliveryNoteConfig.setIssuerVatliabilityXInMillimeters(propertyDTO.getIssuerVatliabilityX());
		this.deliveryNoteConfig.setIssuerVatliabilityYInMillimeters(propertyDTO.getIssuerVatliabilityY());
		this.deliveryNoteConfig.setIssuerVatliabilityPrint(propertyDTO.getIssuerVatliabilityPrint());
		this.deliveryNoteConfig.setIssuerTaxXInMillimeters(propertyDTO.getIssuerTaxX());
		this.deliveryNoteConfig.setIssuerTaxYInMillimeters(propertyDTO.getIssuerTaxY());
		this.deliveryNoteConfig.setIssuerTaxPrint(propertyDTO.getIssuerTaxPrint());
		this.deliveryNoteConfig.setDeliveryLocationCorporateNameXInMillimeters(propertyDTO.getDeliveryLocationCorporateNameX());
		this.deliveryNoteConfig.setDeliveryLocationCorporateNameYInMillimeters(propertyDTO.getDeliveryLocationCorporateNameY());
		this.deliveryNoteConfig.setDeliveryLocationCorporateNamePrint(propertyDTO.getDeliveryLocationCorporateNamePrint());
		this.deliveryNoteConfig.setDeliveryLocationAddressXInMillimeters(propertyDTO.getDeliveryLocationAddressX());
		this.deliveryNoteConfig.setDeliveryLocationAddressYInMillimeters(propertyDTO.getDeliveryLocationAddressY());
		this.deliveryNoteConfig.setDeliveryLocationAddressPrint(propertyDTO.getDeliveryLocationAddressPrint());
		this.deliveryNoteConfig.setDeliveryLocationLocalityXInMillimeters(propertyDTO.getDeliveryLocationLocalityX());
		this.deliveryNoteConfig.setDeliveryLocationLocalityYInMillimeters(propertyDTO.getDeliveryLocationLocalityY());
		this.deliveryNoteConfig.setDeliveryLocationLocalityPrint(propertyDTO.getDeliveryLocationLocalityPrint());
		this.deliveryNoteConfig.setDeliveryLocationZipcodeXInMillimeters(propertyDTO.getDeliveryLocationZipcodeX());
		this.deliveryNoteConfig.setDeliveryLocationZipcodeYInMillimeters(propertyDTO.getDeliveryLocationZipcodeY());
		this.deliveryNoteConfig.setDeliveryLocationZipcodePrint(propertyDTO.getDeliveryLocationZipcodePrint());
		this.deliveryNoteConfig.setDeliveryLocationProvinceXInMillimeters(propertyDTO.getDeliveryLocationProvinceX());
		this.deliveryNoteConfig.setDeliveryLocationProvinceYInMillimeters(propertyDTO.getDeliveryLocationProvinceY());
		this.deliveryNoteConfig.setDeliveryLocationProvincePrint(propertyDTO.getDeliveryLocationProvincePrint());
		this.deliveryNoteConfig.setDeliveryLocationVatliabilityXInMillimeters(propertyDTO.getDeliveryLocationVatliabilityX());
		this.deliveryNoteConfig.setDeliveryLocationVatliabilityYInMillimeters(propertyDTO.getDeliveryLocationVatliabilityY());
		this.deliveryNoteConfig.setDeliveryLocationVatliabilityPrint(propertyDTO.getDeliveryLocationVatliabilityPrint());
		this.deliveryNoteConfig.setDeliveryLocationTaxXInMillimeters(propertyDTO.getDeliveryLocationTaxX());
		this.deliveryNoteConfig.setDeliveryLocationTaxYInMillimeters(propertyDTO.getDeliveryLocationTaxY());
		this.deliveryNoteConfig.setDeliveryLocationTaxPrint(propertyDTO.getDeliveryLocationTaxPrint());
		this.deliveryNoteConfig.setAffiliateXInMillimeters(propertyDTO.getAffiliateX());
		this.deliveryNoteConfig.setAffiliateYInMillimeters(propertyDTO.getAffiliateY());
		this.deliveryNoteConfig.setAffiliatePrint(propertyDTO.getAffiliatePrint());
		this.deliveryNoteConfig.setOrderXInMillimeters(propertyDTO.getOrderX());
		this.deliveryNoteConfig.setOrderYInMillimeters(propertyDTO.getOrderY());
		this.deliveryNoteConfig.setOrderPrint(propertyDTO.getOrderPrint());
		this.deliveryNoteConfig.setIssuerGlnXInMillimeters(propertyDTO.getIssuerGlnX());
		this.deliveryNoteConfig.setIssuerGlnYInMillimeters(propertyDTO.getIssuerGlnY());
		this.deliveryNoteConfig.setIssuerGlnPrint(propertyDTO.getIssuerGlnPrint());
		this.deliveryNoteConfig.setDeliveryLocationGlnXInMillimeters(propertyDTO.getDeliveryLocationGlnX());
		this.deliveryNoteConfig.setDeliveryLocationGlnYInMillimeters(propertyDTO.getDeliveryLocationGlnY());
		this.deliveryNoteConfig.setDeliveryLocationGlnPrint(propertyDTO.getDeliveryLocationGlnPrint());
		this.deliveryNoteConfig.setProductDetailsYInMillimeters(propertyDTO.getProductDetailsY());
		this.deliveryNoteConfig.setProductDescriptionXInMillimeters(propertyDTO.getProductDescriptionX());
		this.deliveryNoteConfig.setProductDescriptionPrint(propertyDTO.getProductDescriptionPrint());
		this.deliveryNoteConfig.setProductMonodrugXInMillimeters(propertyDTO.getProductMonodrugX());
		this.deliveryNoteConfig.setProductMonodrugPrint(propertyDTO.getProductMonodrugPrint());
		this.deliveryNoteConfig.setProductBrandXInMillimeters(propertyDTO.getProductBrandX());
		this.deliveryNoteConfig.setProductBrandPrint(propertyDTO.getProductBrandPrint());
		this.deliveryNoteConfig.setProductAmountXInMillimeters(propertyDTO.getProductAmountX());
		this.deliveryNoteConfig.setProductAmountPrint(propertyDTO.getProductAmountPrint());
		this.deliveryNoteConfig.setProductBatchExpirationdateXInMillimeters(propertyDTO.getProductBatchExpirationdateX());
		this.deliveryNoteConfig.setProductBatchExpirationdatePrint(propertyDTO.getProductBatchExpirationdatePrint());
		this.deliveryNoteConfig.setSerialColumn1XInMillimeters(propertyDTO.getSerialColumn1X());
		this.deliveryNoteConfig.setSerialColumn2XInMillimeters(propertyDTO.getSerialColumn2X());
		this.deliveryNoteConfig.setSerialColumn3XInMillimeters(propertyDTO.getSerialColumn3X());
		this.deliveryNoteConfig.setSerialColumn4XInMillimeters(propertyDTO.getSerialColumn4X());
		this.deliveryNoteConfig.setSerialColumn1Print(propertyDTO.getSerialColumn1Print());
		this.deliveryNoteConfig.setSerialColumn2Print(propertyDTO.getSerialColumn2Print());
		this.deliveryNoteConfig.setSerialColumn3Print(propertyDTO.getSerialColumn3Print());
		this.deliveryNoteConfig.setSerialColumn4Print(propertyDTO.getSerialColumn4Print());
		this.deliveryNoteConfig.setNumberOfItemsXInMillimeters(propertyDTO.getNumberOfItemsX());
		this.deliveryNoteConfig.setNumberOfItemsYInMillimeters(propertyDTO.getNumberOfItemsY());
		this.deliveryNoteConfig.setNumberOfItemsPrint(propertyDTO.getNumberOfItemsPrint());

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

		VATLiability selectedVATLiability = property.getVATLiability();
		List<VATLiability> allVATLiabilities = this.VATLiabilityService.getAll();
		allVATLiabilities.remove(selectedVATLiability);
		modelMap.put("VATLiabilities", allVATLiabilities);
		modelMap.put("selectedVATLiability", selectedVATLiability);

		modelMap.put("fontSize", this.deliveryNoteConfig.getFontSize());

		modelMap.put("numberX", this.deliveryNoteConfig.getNumberXInMillimeters());
		modelMap.put("numberY", this.deliveryNoteConfig.getNumberYInMillimeters());
		modelMap.put("numberPrint", this.deliveryNoteConfig.isNumberPrint());

		modelMap.put("dateX", this.deliveryNoteConfig.getDateXInMillimeters());
		modelMap.put("dateY", this.deliveryNoteConfig.getDateYInMillimeters());
		modelMap.put("datePrint", this.deliveryNoteConfig.isDatePrint());

		modelMap.put("issuerCorporateNameX", this.deliveryNoteConfig.getIssuerCorporateNameXInMillimeters());
		modelMap.put("issuerCorporateNameY", this.deliveryNoteConfig.getIssuerCorporateNameYInMillimeters());
		modelMap.put("issuerCorporateNamePrint", this.deliveryNoteConfig.isIssuerCorporateNamePrint());

		modelMap.put("issuerAddressX", this.deliveryNoteConfig.getIssuerAddressXInMillimeters());
		modelMap.put("issuerAddressY", this.deliveryNoteConfig.getIssuerAddressYInMillimeters());
		modelMap.put("issuerAddressPrint", this.deliveryNoteConfig.isIssuerAddressPrint());

		modelMap.put("issuerLocalityX", this.deliveryNoteConfig.getIssuerLocalityXInMillimeters());
		modelMap.put("issuerLocalityY", this.deliveryNoteConfig.getIssuerLocalityYInMillimeters());
		modelMap.put("issuerLocalityPrint", this.deliveryNoteConfig.isIssuerLocalityPrint());

		modelMap.put("issuerZipcodeX", this.deliveryNoteConfig.getIssuerZipcodeXInMillimeters());
		modelMap.put("issuerZipcodeY", this.deliveryNoteConfig.getIssuerZipcodeYInMillimeters());
		modelMap.put("issuerZipcodePrint", this.deliveryNoteConfig.isIssuerZipcodePrint());

		modelMap.put("issuerProvinceX", this.deliveryNoteConfig.getIssuerProvinceXInMillimeters());
		modelMap.put("issuerProvinceY", this.deliveryNoteConfig.getIssuerProvinceYInMillimeters());
		modelMap.put("issuerProvincePrint", this.deliveryNoteConfig.isIssuerProvincePrint());

		modelMap.put("issuerVatliabilityX", this.deliveryNoteConfig.getIssuerVatliabilityXInMillimeters());
		modelMap.put("issuerVatliabilityY", this.deliveryNoteConfig.getIssuerVatliabilityYInMillimeters());
		modelMap.put("issuerVatliabilityPrint", this.deliveryNoteConfig.isIssuerVatliabilityPrint());

		modelMap.put("issuerTaxX", this.deliveryNoteConfig.getIssuerTaxXInMillimeters());
		modelMap.put("issuerTaxY", this.deliveryNoteConfig.getIssuerTaxYInMillimeters());
		modelMap.put("issuerTaxPrint", this.deliveryNoteConfig.isIssuerTaxPrint());

		modelMap.put("issuerGlnX", this.deliveryNoteConfig.getIssuerGlnXInMillimeters());
		modelMap.put("issuerGlnY", this.deliveryNoteConfig.getIssuerGlnYInMillimeters());
		modelMap.put("issuerGlnPrint", this.deliveryNoteConfig.isIssuerGlnPrint());

		modelMap.put("deliveryLocationCorporateNameX", this.deliveryNoteConfig.getDeliveryLocationCorporateNameXInMillimeters());
		modelMap.put("deliveryLocationCorporateNameY", this.deliveryNoteConfig.getDeliveryLocationCorporateNameYInMillimeters());
		modelMap.put("deliveryLocationCorporateNamePrint", this.deliveryNoteConfig.isDeliveryLocationCorporateNamePrint());

		modelMap.put("deliveryLocationAddressX", this.deliveryNoteConfig.getDeliveryLocationAddressXInMillimeters());
		modelMap.put("deliveryLocationAddressY", this.deliveryNoteConfig.getDeliveryLocationAddressYInMillimeters());
		modelMap.put("deliveryLocationAddressPrint", this.deliveryNoteConfig.isDeliveryLocationAddressPrint());

		modelMap.put("deliveryLocationLocalityX", this.deliveryNoteConfig.getDeliveryLocationLocalityXInMillimeters());
		modelMap.put("deliveryLocationLocalityY", this.deliveryNoteConfig.getDeliveryLocationLocalityYInMillimeters());
		modelMap.put("deliveryLocationLocalityPrint", this.deliveryNoteConfig.isDeliveryLocationLocalityPrint());

		modelMap.put("deliveryLocationZipcodeX", this.deliveryNoteConfig.getDeliveryLocationZipcodeXInMillimeters());
		modelMap.put("deliveryLocationZipcodeY", this.deliveryNoteConfig.getDeliveryLocationZipcodeYInMillimeters());
		modelMap.put("deliveryLocationZipcodePrint", this.deliveryNoteConfig.isDeliveryLocationZipcodePrint());

		modelMap.put("deliveryLocationProvinceX", this.deliveryNoteConfig.getDeliveryLocationProvinceXInMillimeters());
		modelMap.put("deliveryLocationProvinceY", this.deliveryNoteConfig.getDeliveryLocationProvinceYInMillimeters());
		modelMap.put("deliveryLocationProvincePrint", this.deliveryNoteConfig.isDeliveryLocationProvincePrint());

		modelMap.put("deliveryLocationVatliabilityX", this.deliveryNoteConfig.getDeliveryLocationVatliabilityXInMillimeters());
		modelMap.put("deliveryLocationVatliabilityY", this.deliveryNoteConfig.getDeliveryLocationVatliabilityYInMillimeters());
		modelMap.put("deliveryLocationVatliabilityPrint", this.deliveryNoteConfig.isDeliveryLocationVatliabilityPrint());

		modelMap.put("deliveryLocationTaxX", this.deliveryNoteConfig.getDeliveryLocationTaxXInMillimeters());
		modelMap.put("deliveryLocationTaxY", this.deliveryNoteConfig.getDeliveryLocationTaxYInMillimeters());
		modelMap.put("deliveryLocationTaxPrint", this.deliveryNoteConfig.isDeliveryLocationTaxPrint());

		modelMap.put("deliveryLocationGlnX", this.deliveryNoteConfig.getDeliveryLocationGlnXInMillimeters());
		modelMap.put("deliveryLocationGlnY", this.deliveryNoteConfig.getDeliveryLocationGlnYInMillimeters());
		modelMap.put("deliveryLocationGlnPrint", this.deliveryNoteConfig.isDeliveryLocationGlnPrint());

		modelMap.put("affiliateX", this.deliveryNoteConfig.getAffiliateXInMillimeters());
		modelMap.put("affiliateY", this.deliveryNoteConfig.getAffiliateYInMillimeters());
		modelMap.put("affiliatePrint", this.deliveryNoteConfig.isAffiliatePrint());

		modelMap.put("orderX", this.deliveryNoteConfig.getOrderXInMillimeters());
		modelMap.put("orderY", this.deliveryNoteConfig.getOrderYInMillimeters());
		modelMap.put("orderPrint", this.deliveryNoteConfig.isOrderPrint());

		modelMap.put("productDetailsY", this.deliveryNoteConfig.getProductDetailsYInMillimeters());

		modelMap.put("productDescriptionX", this.deliveryNoteConfig.getProductDescriptionXInMillimeters());
		modelMap.put("productDescriptionPrint", this.deliveryNoteConfig.isProductDescriptionPrint());
		
		modelMap.put("productMonodrugX", this.deliveryNoteConfig.getProductMonodrugXInMillimeters());
		modelMap.put("productMonodrugPrint", this.deliveryNoteConfig.isProductMonodrugPrint());

		modelMap.put("productBrandX", this.deliveryNoteConfig.getProductBrandXInMillimeters());
		modelMap.put("productBrandPrint", this.deliveryNoteConfig.isProductBrandPrint());

		modelMap.put("productAmountX", this.deliveryNoteConfig.getProductAmountXInMillimeters());
		modelMap.put("productAmountPrint", this.deliveryNoteConfig.isProductAmountPrint());

		modelMap.put("productBatchExpirationdateX", this.deliveryNoteConfig.getProductBatchExpirationdateXInMillimeters());
		modelMap.put("productBatchExpirationdatePrint", this.deliveryNoteConfig.isProductBatchExpirationdatePrint());

		modelMap.put("serialColumn1X", this.deliveryNoteConfig.getSerialColumn1XInMillimeters());
		modelMap.put("serialColumn1Print", this.deliveryNoteConfig.isSerialColumn1Print());

		modelMap.put("serialColumn2X", this.deliveryNoteConfig.getSerialColumn2XInMillimeters());
		modelMap.put("serialColumn2Print", this.deliveryNoteConfig.isSerialColumn2Print());

		modelMap.put("serialColumn3X", this.deliveryNoteConfig.getSerialColumn3XInMillimeters());
		modelMap.put("serialColumn3Print", this.deliveryNoteConfig.isSerialColumn3Print());

		modelMap.put("serialColumn4X", this.deliveryNoteConfig.getSerialColumn4XInMillimeters());
		modelMap.put("serialColumn4Print", this.deliveryNoteConfig.isSerialColumn4Print());

		modelMap.put("numberOfItemsX", this.deliveryNoteConfig.getNumberOfItemsXInMillimeters());
		modelMap.put("numberOfItemsY", this.deliveryNoteConfig.getNumberOfItemsYInMillimeters());
		modelMap.put("numberOfItemsPrint", this.deliveryNoteConfig.isNumberOfItemsPrint());

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
