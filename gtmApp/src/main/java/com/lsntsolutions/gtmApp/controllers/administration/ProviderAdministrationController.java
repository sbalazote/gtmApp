package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.ProviderDTO;
import com.lsntsolutions.gtmApp.model.LogisticsOperator;
import com.lsntsolutions.gtmApp.model.Provider;
import com.lsntsolutions.gtmApp.model.Province;
import com.lsntsolutions.gtmApp.service.*;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ProviderAdministrationController {

	@Autowired
	private ProviderService providerService;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private ProviderTypeService providerTypeService;

	@Autowired
	private LogisticsOperatorService logisticsOperatorService;

	@Autowired
	private com.lsntsolutions.gtmApp.service.VATLiabilityService VATLiabilityService;

	@RequestMapping(value = "/providers", method = RequestMethod.POST)
	public ModelAndView providers(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("providers", "providers", this.providerService.getAll());
		} else {
			return new ModelAndView("providers", "providers", this.providerService.getForAutocomplete(searchPhrase, null, null, null, null, null, null, null));
		}
	}

	@RequestMapping(value = "/saveProvider", method = RequestMethod.POST)
	public @ResponseBody
	Provider saveProvider(@RequestBody ProviderDTO providerDTO) throws Exception {
		Provider provider = this.buildModel(providerDTO);
		this.providerService.save(provider);
		return provider;
	}

	@RequestMapping(value = "/providerAdministration", method = RequestMethod.GET)
	public String providerAdministration(ModelMap modelMap) throws Exception {
		this.setModelMaps(modelMap);
		return "providerAdministration";
	}

	@RequestMapping(value = "/getProvidersLogisticsOperators", method = RequestMethod.GET)
	public @ResponseBody List<LogisticsOperator> getProvidersLogisticsOperators(@RequestParam Integer providerId, boolean input) throws Exception {
		return this.providerService.get(providerId).getLogisticsOperators(input);
	}

	private Provider buildModel(ProviderDTO providerDTO) {
		Provider provider = new Provider();

		if (providerDTO.getId() != null) {
			provider.setId(providerDTO.getId());
		}

		provider.setCode(providerDTO.getCode());
		provider.setName(providerDTO.getName());
		provider.setTaxId(providerDTO.getTaxId());
		provider.setCorporateName(providerDTO.getCorporateName());
		Province province = this.provinceService.get(providerDTO.getProvinceId());
		provider.setProvince(province);
		provider.setLocality(providerDTO.getLocality());
		provider.setAddress(providerDTO.getAddress());
		provider.setZipCode(providerDTO.getZipCode());
		provider.setPhone(providerDTO.getPhone());
		provider.setMail(providerDTO.getMail());
		provider.setGln(providerDTO.getGln());
		provider.setActive(providerDTO.isActive());
		provider.setAgent(this.agentService.get(providerDTO.getAgentId()));
		provider.setType(this.providerTypeService.get(providerDTO.getTypeId()));
		provider.setVATLiability(this.VATLiabilityService.get(providerDTO.getVATLiabilityId()));

		List<Integer> deliveryLocationsId = providerDTO.getLogisticsOperators();

		List<LogisticsOperator> logisticsOperators = new ArrayList<>();
		for (Integer deliveryLocationId : deliveryLocationsId) {
			logisticsOperators.add(this.logisticsOperatorService.get(deliveryLocationId));
		}
		provider.setLogisticsOperators(logisticsOperators);

		return provider;
	}

	public void setModelMaps(ModelMap modelMap) {
		modelMap.put("provinces", this.provinceService.getAll());
		modelMap.put("types", this.providerTypeService.getAll());
		modelMap.put("agents", this.agentService.getAll());
		modelMap.put("VATLiabilities", this.VATLiabilityService.getAll());
		modelMap.put("logisticsOperators", this.logisticsOperatorService.getAllActives(true));
	}

	@RequestMapping(value = "/readProvider", method = RequestMethod.GET)
	public @ResponseBody Provider readProvider(Integer providerId) throws Exception {
		return this.providerService.get(providerId);
	}

	@RequestMapping(value = "/deleteProvider", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProvider(@RequestParam Integer providerId) throws Exception {
		return this.providerService.delete(providerId);
	}

	@RequestMapping(value = "/existsProvider", method = RequestMethod.GET)
	public @ResponseBody Boolean existsProvider(@RequestParam Integer code) throws Exception {
		return this.providerService.exists(code);
	}

	@RequestMapping(value = "/getMatchedProviders", method = RequestMethod.POST)
	public @ResponseBody String getMatchedProviders(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		String sortId = parametersMap.get("sort[id]");
		String sortCode = parametersMap.get("sort[code]");
		String sortName = parametersMap.get("sort[name]");
		String sortCorporateName = parametersMap.get("sort[corporateName]");
		String sortTaxId = parametersMap.get("sort[taxId]");
		String sortIsActive = parametersMap.get("sort[isActive]");

		List<Provider> listProviders = this.providerService.getForAutocomplete(searchPhrase, null, sortId, sortCode, sortName, sortCorporateName, sortTaxId, sortIsActive);
		total = listProviders.size();
		if (total < start + length) {
			listProviders = listProviders.subList(start, (int) total);
		} else {
			if(length > 0) {
				listProviders = listProviders.subList(start, start + length);
			}
		}

		for (Provider provider : listProviders) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", provider.getId());
			dataJson.put("code", provider.getCode());
			dataJson.put("name", provider.getName());
			dataJson.put("taxId", provider.getTaxId());
			dataJson.put("corporateName", provider.getCorporateName());
			dataJson.put("province", provider.getProvince().getName());
			dataJson.put("locality", provider.getLocality());
			dataJson.put("address", provider.getAddress());
			dataJson.put("zipCode", provider.getZipCode());
			dataJson.put("vatLiability", provider.getVATLiability().getAcronym());
			dataJson.put("phone", provider.getPhone());
			dataJson.put("mail", provider.getMail());
			dataJson.put("gln", provider.getGln());
			dataJson.put("agent", provider.getAgent().getDescription());
			dataJson.put("type", provider.getType().getDescription());
			dataJson.put("isActive", provider.isActive() ? "Si" : "No");
			jsonArray.put(dataJson);
		}

		JSONObject responseJson = new JSONObject();
		responseJson.put("current", current);
		responseJson.put("rowCount", (total < (start + length)) ? (total - length) : length);
		responseJson.put("rows", jsonArray);
		responseJson.put("total", total);

		return responseJson.toString();
	}
}