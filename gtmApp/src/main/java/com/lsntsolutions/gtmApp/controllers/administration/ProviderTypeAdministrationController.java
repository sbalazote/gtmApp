package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.ProviderTypeDTO;
import com.lsntsolutions.gtmApp.model.ProviderType;
import com.lsntsolutions.gtmApp.service.ProviderTypeService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class ProviderTypeAdministrationController {

	@Autowired
	private ProviderTypeService providerTypeService;

	@RequestMapping(value = "/providerTypes", method = RequestMethod.POST)
	public ModelAndView providerTypes(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("providerTypes", "providerTypes", this.providerTypeService.getAll());
		} else {
			return new ModelAndView("providerTypes", "providerTypes", this.providerTypeService.getForAutocomplete(searchPhrase, null));
		}
	}

	@RequestMapping(value = "/saveProviderType", method = RequestMethod.POST)
	public @ResponseBody
	ProviderType saveProviderType(@RequestBody ProviderTypeDTO providerTypeDTO) throws Exception {
		ProviderType providerType = this.buildModel(providerTypeDTO);
		this.providerTypeService.save(providerType);
		return providerType;
	}

	private ProviderType buildModel(ProviderTypeDTO providerTypeDTO) {
		ProviderType providerType = new ProviderType();

		if (providerTypeDTO.getId() != null) {
			providerType.setId(providerTypeDTO.getId());
		}
		providerType.setCode(providerTypeDTO.getCode());
		providerType.setDescription(providerTypeDTO.getDescription());
		providerType.setActive(providerTypeDTO.isActive());

		return providerType;
	}

	@RequestMapping(value = "/readProviderType", method = RequestMethod.GET)
	public @ResponseBody ProviderType readProviderType(Integer providerTypeId) throws Exception {
		return this.providerTypeService.get(providerTypeId);
	}

	@RequestMapping(value = "/deleteProviderType", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProviderType(@RequestParam Integer providerTypeId) throws Exception {
		return this.providerTypeService.delete(providerTypeId);
	}

	@RequestMapping(value = "/existsProviderType", method = RequestMethod.GET)
	public @ResponseBody Boolean existsProviderType(@RequestParam Integer code) throws Exception {
		return this.providerTypeService.exists(code);
	}

	@RequestMapping(value = "/getMatchedProviderTypes", method = RequestMethod.POST)
	public @ResponseBody String getMatchedProviderTypes(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<ProviderType> listProviderTypes = null;
		if (searchPhrase.matches("")) {
			listProviderTypes = this.providerTypeService.getPaginated(start, length);
			total = this.providerTypeService.getTotalNumber();
		} else {
			listProviderTypes = this.providerTypeService.getForAutocomplete(searchPhrase, null);
			total = listProviderTypes.size();
			if (total < start + length) {
				listProviderTypes = listProviderTypes.subList(start, (int) total);
			} else {
				listProviderTypes = listProviderTypes.subList(start, start + length);
			}
		}

		for (ProviderType providerType : listProviderTypes) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", providerType.getId());
			dataJson.put("code", providerType.getCode());
			dataJson.put("description", providerType.getDescription());
			dataJson.put("isActive", providerType.isActive() == true ? "Si" : "No");
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