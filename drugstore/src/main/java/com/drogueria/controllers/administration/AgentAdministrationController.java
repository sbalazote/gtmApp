package com.drogueria.controllers.administration;

import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.drogueria.dto.AgentDTO;
import com.drogueria.model.Agent;
import com.drogueria.service.AgentService;

@Controller
public class AgentAdministrationController {

	@Autowired
	private AgentService agentService;

	@RequestMapping(value = "/agents", method = RequestMethod.POST)
	public ModelAndView agents() {
		return new ModelAndView("agents", "agents", this.agentService.getAll());
	}

	@RequestMapping(value = "/agentAdministration", method = RequestMethod.GET)
	public String agentAdministration(ModelMap modelMap) throws Exception {
		return "agentAdministration";
	}

	@RequestMapping(value = "/saveAgent", method = RequestMethod.POST)
	public @ResponseBody Agent saveAgent(@RequestBody AgentDTO agentDTO) throws Exception {
		Agent agent = this.buildModel(agentDTO);
		this.agentService.save(agent);
		return agent;
	}

	private Agent buildModel(AgentDTO agentDTO) {
		Agent agent = new Agent();
		if (agentDTO.getId() != null) {
			agent.setId(agentDTO.getId());
		}
		agent.setCode(agentDTO.getCode());

		agent.setDescription(agentDTO.getDescription());
		agent.setActive(agentDTO.isActive());
		return agent;
	}

	@RequestMapping(value = "/readAgent", method = RequestMethod.GET)
	public @ResponseBody Agent readAgent(@RequestParam Integer agentId) throws Exception {
		return this.agentService.get(agentId);
	}

	@RequestMapping(value = "/deleteAgent", method = RequestMethod.POST)
	public @ResponseBody boolean deleteAgent(@RequestParam Integer agentId) throws Exception {
		return this.agentService.delete(agentId);
	}

	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	public @ResponseBody Boolean existsAgent(@RequestParam Integer code) throws Exception {
		return this.agentService.exists(code);
	}

	@RequestMapping(value = "/getMatchedAgents", method = RequestMethod.POST)
	public @ResponseBody String getMatchedAgents(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<Agent> listAgents = null;
		if (searchPhrase.matches("")) {
			listAgents = this.agentService.getPaginated(start, length);
			total = this.agentService.getTotalNumber();
		} else {
			listAgents = this.agentService.getForAutocomplete(searchPhrase, null);
			total = listAgents.size();
			if (total < start + length) {
				listAgents = listAgents.subList(start, (int) total);
			} else {
				listAgents = listAgents.subList(start, start + length);
			}
		}

		for (Agent agent : listAgents) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", agent.getId());
			dataJson.put("code", agent.getCode());
			dataJson.put("description", agent.getDescription());
			dataJson.put("isActive", agent.isActive() == true ? "Si" : "No");
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
