package com.lsntsolutions.gtmApp.controllers.configuration;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
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
		PropertyProvider.getInstance().setProp(PropertyProvider.NAME, propertyDTO.getName());
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
