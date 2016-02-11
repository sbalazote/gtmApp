package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.LogisticsOperatorDTO;
import com.lsntsolutions.gtmApp.model.LogisticsOperator;
import com.lsntsolutions.gtmApp.model.Province;
import com.lsntsolutions.gtmApp.service.AgentService;
import com.lsntsolutions.gtmApp.service.LogisticsOperatorService;
import com.lsntsolutions.gtmApp.service.ProvinceService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class LogisticsOperatorAdministrationController {

	@Autowired
	private LogisticsOperatorService logisticsOperatorService;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private AgentService agentService;

	@RequestMapping(value = "/logisticsOperators", method = RequestMethod.POST)
	public ModelAndView logisticsOperators(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("logisticsOperators", "logisticsOperators", this.logisticsOperatorService.getAll());
		} else {
			return new ModelAndView("logisticsOperators", "logisticsOperators", this.logisticsOperatorService.getForAutocomplete(searchPhrase, null, null, null, null, null, null, null, null));
		}
	}

	@RequestMapping(value = "/logisticsOperatorAdministration", method = RequestMethod.GET)
	public String logisticsOperatorAdministration(ModelMap modelMap) throws Exception {
		modelMap.put("provinces", this.provinceService.getAll());
		modelMap.put("agents", this.agentService.getAll());
		return "logisticsOperatorAdministration";
	}

	@RequestMapping(value = "/getLogisticsOperatorsOutput", method = RequestMethod.GET)
	public @ResponseBody
	List<LogisticsOperator> getLogisticsOperatorsOutput() throws Exception {
		return this.logisticsOperatorService.getAllActives(false);
	}

	@RequestMapping(value = "/saveLogisticsOperator", method = RequestMethod.POST)
	public @ResponseBody
	LogisticsOperator saveLogisticsOperator(@RequestBody LogisticsOperatorDTO logisticsOperatorDTO) throws Exception {
		LogisticsOperator logisticsOperator = this.buildModel(logisticsOperatorDTO);
		this.logisticsOperatorService.save(logisticsOperator);
		return logisticsOperator;
	}

	private LogisticsOperator buildModel(LogisticsOperatorDTO logisticsOperatorDTO) {
		LogisticsOperator logisticsOperator = new LogisticsOperator();
		if (logisticsOperatorDTO.getId() != null) {
			logisticsOperator.setId(logisticsOperatorDTO.getId());
		}
		logisticsOperator.setCode(logisticsOperatorDTO.getCode());
		logisticsOperator.setName(logisticsOperatorDTO.getName());
		logisticsOperator.setTaxId(logisticsOperatorDTO.getTaxId());
		logisticsOperator.setCorporateName(logisticsOperatorDTO.getCorporateName());
		Province province = this.provinceService.get(logisticsOperatorDTO.getProvinceId());
		logisticsOperator.setProvince(province);
		logisticsOperator.setLocality(logisticsOperatorDTO.getLocality());
		logisticsOperator.setAddress(logisticsOperatorDTO.getAddress());
		logisticsOperator.setZipCode(logisticsOperatorDTO.getZipCode());
		logisticsOperator.setPhone(logisticsOperatorDTO.getPhone());
		logisticsOperator.setActive(logisticsOperatorDTO.isActive());
		logisticsOperator.setGln(logisticsOperatorDTO.getGln());
		logisticsOperator.setInput(logisticsOperatorDTO.isInput());
		logisticsOperator.setAgent(this.agentService.get(logisticsOperatorDTO.getAgentId()));

		return logisticsOperator;
	}

	@RequestMapping(value = "/readLogisticsOperator", method = RequestMethod.GET)
	public @ResponseBody LogisticsOperator readLogisticsOperator(@RequestParam Integer logisticsOperatorId) throws Exception {
		return this.logisticsOperatorService.get(logisticsOperatorId);
	}

	@RequestMapping(value = "/deleteLogisticsOperator", method = RequestMethod.POST)
	public @ResponseBody boolean deleteLogisticsOperator(@RequestParam Integer logisticsOperatorId) throws Exception {
		return this.logisticsOperatorService.delete(logisticsOperatorId);
	}

	@RequestMapping(value = "/existsLogisticsOperator", method = RequestMethod.GET)
	public @ResponseBody Boolean existsLogisticsOperator(@RequestParam Integer code) throws Exception {
		return this.logisticsOperatorService.exists(code);
	}

	@RequestMapping(value = "/getMatchedLogisticsOperators", method = RequestMethod.POST)
	public @ResponseBody String getMatchedLogisticsOperators(@RequestParam Map<String, String> parametersMap) throws JSONException {

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
		String sortIsInput = parametersMap.get("sort[isInput]");

		List<LogisticsOperator> listLogisticsOperators = this.logisticsOperatorService.getForAutocomplete(searchPhrase, null, sortId, sortCode, sortName, sortCorporateName, sortTaxId, sortIsActive, sortIsInput);
		total = listLogisticsOperators.size();
		if (total < start + length) {
			listLogisticsOperators = listLogisticsOperators.subList(start, (int) total);
		} else {
			if(length > 0) {
				listLogisticsOperators = listLogisticsOperators.subList(start, start + length);
			}
		}

		for (LogisticsOperator logisticsOperator : listLogisticsOperators) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", logisticsOperator.getId());
			dataJson.put("code", logisticsOperator.getCode());
			dataJson.put("name", logisticsOperator.getName());
			dataJson.put("taxId", logisticsOperator.getTaxId());
			dataJson.put("corporateName", logisticsOperator.getCorporateName());
			dataJson.put("province", logisticsOperator.getProvince().getName());
			dataJson.put("locality", logisticsOperator.getLocality());
			dataJson.put("address", logisticsOperator.getAddress());
			dataJson.put("zipCode", logisticsOperator.getZipCode());
			dataJson.put("phone", logisticsOperator.getPhone());
			dataJson.put("isActive", logisticsOperator.isActive() ? "Si" : "No");
			dataJson.put("isInput", logisticsOperator.isInput() ? "Si" : "No");
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
