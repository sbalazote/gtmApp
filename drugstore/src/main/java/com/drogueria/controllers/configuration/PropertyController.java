package com.drogueria.controllers.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.dto.DrugstorePropertyDTO;
import com.drogueria.helper.EncryptionHelper;
import com.drogueria.model.Agent;
import com.drogueria.model.DrugstoreProperty;
import com.drogueria.model.Province;
import com.drogueria.service.AgentService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DrugstorePropertyService;
import com.drogueria.service.ProviderTypeService;
import com.drogueria.service.ProvinceService;

@Controller
public class PropertyController {
	@Autowired
	private DrugstorePropertyService drugstorePropertyService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private ProviderTypeService providerTypeService;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	ConceptService conceptService;

	private static final Logger logger = Logger.getLogger(PropertyController.class);

	@RequestMapping(value = "/saveProperty", method = RequestMethod.POST)
	public @ResponseBody void saveProperty(@RequestBody DrugstorePropertyDTO propertyDTO) throws Exception {
		this.drugstorePropertyService.save(this.buildModel(propertyDTO));
	}

	private DrugstoreProperty buildModel(DrugstorePropertyDTO propertyDTO) {
		DrugstoreProperty property = new DrugstoreProperty();
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

		DrugstoreProperty drugstoreProperty = this.drugstorePropertyService.get();

		if (!propertyDTO.getANMATPassword().isEmpty()) {
			property.setANMATPassword(EncryptionHelper.AESEncrypt(propertyDTO.getANMATPassword()));
		} else {
			property.setANMATPassword(drugstoreProperty.getANMATPassword());
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

		property.setStartTraceConcept(this.conceptService.get(propertyDTO.getStartTraceConceptSelectId()));

		property.setLastTag(drugstoreProperty.getLastTag());

		return property;
	}

	@RequestMapping(value = "/updateProperty", method = RequestMethod.GET)
	public String updateProperty(ModelMap modelMap, @RequestParam Map<String, String> parameters) throws Exception {
		DrugstoreProperty property = this.drugstorePropertyService.get();

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

		modelMap.put("selectedStartTraceConcept", property.getStartTraceConcept().getId());
		modelMap.put("startTraceConcepts", this.conceptService.getAllActives(true));

		modelMap.put("proxy", property.getProxy());
		modelMap.put("proxyPort", property.getProxyPort());
		modelMap.put("informProxy", property.isHasProxy());

		return "updateProperty";
	}

	@RequestMapping(value = "/getProperties", method = RequestMethod.GET)
	@ResponseBody
	public DrugstoreProperty getProperties() throws IOException {
		return this.drugstorePropertyService.get();
	}
}
