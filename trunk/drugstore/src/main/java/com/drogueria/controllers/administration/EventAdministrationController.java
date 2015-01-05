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

import com.drogueria.dto.EventDTO;
import com.drogueria.model.Event;
import com.drogueria.service.AgentService;
import com.drogueria.service.EventService;

@Controller
public class EventAdministrationController {

	@Autowired
	private EventService eventService;

	@Autowired
	private AgentService agentService;

	@RequestMapping(value = "/events", method = RequestMethod.POST)
	public ModelAndView events() {
		return new ModelAndView("events", "events", this.eventService.getAll());
	}

	@RequestMapping(value = "/eventAdministration", method = RequestMethod.GET)
	public String eventAdministration(ModelMap modelMap) throws Exception {
		modelMap.put("agents", this.agentService.getAll());
		return "eventAdministration";
	}

	@RequestMapping(value = "/saveEvent", method = RequestMethod.POST)
	public @ResponseBody Event saveEvent(@RequestBody EventDTO eventDTO) throws Exception {
		Event event = this.buildModel(eventDTO);
		this.eventService.save(event);
		return event;
	}

	private Event buildModel(EventDTO eventDTO) {
		Event event = new Event();

		if (eventDTO.getId() != null) {
			event.setId(eventDTO.getId());
		}
		event.setCode(eventDTO.getCode());
		event.setDescription(eventDTO.getDescription());
		event.setOriginAgent(this.agentService.get(eventDTO.getOriginAgentId()));
		event.setDestinationAgent(this.agentService.get(eventDTO.getDestinationAgentId()));
		event.setActive(eventDTO.isActive());

		return event;
	}

	@RequestMapping(value = "/readEvent", method = RequestMethod.GET)
	public @ResponseBody Event readEvent(@RequestParam Integer eventId) throws Exception {
		return this.eventService.get(eventId);
	}

	@RequestMapping(value = "/deleteEvent", method = RequestMethod.POST)
	public @ResponseBody boolean deleteEvent(@RequestParam Integer eventId) throws Exception {
		return this.eventService.delete(eventId);
	}

	@RequestMapping(value = "/existsEvent", method = RequestMethod.GET)
	public @ResponseBody Boolean exists(@RequestParam Integer code) throws Exception {
		return this.eventService.exists(code);
	}

	@RequestMapping(value = "/getMatchedEvents", method = RequestMethod.POST)
	public @ResponseBody String getMatchedEvents(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<Event> listEvents = null;
		if (searchPhrase.matches("")) {
			listEvents = this.eventService.getPaginated(start, length);
			total = this.eventService.getTotalNumber();
		} else {
			listEvents = this.eventService.getForAutocomplete(searchPhrase, null);
			total = listEvents.size();
			if (total < start + length) {
				listEvents = listEvents.subList(start, (int) total);
			} else {
				listEvents = listEvents.subList(start, start + length);
			}
		}

		for (Event event : listEvents) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", event.getId());
			dataJson.put("code", event.getCode());
			dataJson.put("description", event.getDescription());
			dataJson.put("originAgent", event.getOriginAgent().getDescription());
			dataJson.put("destinationAgent", event.getDestinationAgent().getDescription());
			dataJson.put("isActive", event.isActive() == true ? "Si" : "No");
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
