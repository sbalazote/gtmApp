package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.ConceptDTO;
import com.lsntsolutions.gtmApp.dto.EventDTO;
import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.Event;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.DeliveryNoteEnumeratorService;
import com.lsntsolutions.gtmApp.service.EventService;
import com.lsntsolutions.gtmApp.util.StringUtility;
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
public class ConceptAdministrationController {

	@Autowired
	private ConceptService conceptService;

	@Autowired
	private EventService eventService;

	@Autowired
	private DeliveryNoteEnumeratorService deliveryNoteEnumeratorService;

	@RequestMapping(value = "/concepts", method = RequestMethod.POST)
	public ModelAndView concepts(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("concepts", "concepts", this.conceptService.getAll());
		} else {
			return new ModelAndView("concepts", "concepts", this.conceptService.getForAutocomplete(searchPhrase, null));
		}
	}

	@RequestMapping(value = "/conceptAdministration", method = RequestMethod.GET)
	public String conceptAdministration(ModelMap modelMap) throws Exception {
		modelMap.put("events", this.eventService.getAll());
		modelMap.put("deliveryNoteEnumerators", this.deliveryNoteEnumeratorService.getAll());
		return "conceptAdministration";
	}

	@RequestMapping(value = "/saveConcept", method = RequestMethod.POST)
	public @ResponseBody
	Concept saveConcept(@RequestBody ConceptDTO conceptDTO) throws Exception {
		Concept concept = this.buildModel(conceptDTO);
		this.conceptService.save(concept);
		return concept;
	}

	private Concept buildModel(ConceptDTO conceptDTO) {
		Concept concept = new Concept();

		if (conceptDTO.getId() != null) {
			concept.setId(conceptDTO.getId());
		}
		concept.setCode(conceptDTO.getCode());
		concept.setDescription(conceptDTO.getDescription());
		concept.setDeliveryNoteEnumerator(this.deliveryNoteEnumeratorService.get(conceptDTO.getDeliveryNoteEnumeratorId()));
		concept.setInput(conceptDTO.isInput());
		concept.setPrintDeliveryNote(conceptDTO.isPrintDeliveryNote());
		concept.setRefund(conceptDTO.isRefund());
		concept.setInformAnmat(conceptDTO.isInformAnmat());
		concept.setDeliveryNoteCopies(conceptDTO.getDeliveryNotesCopies());
		concept.setActive(conceptDTO.isActive());
		concept.setClient(conceptDTO.isClient());

		List<Integer> eventIds = conceptDTO.getEvents();

		List<Event> events = new ArrayList<Event>();
		if (!eventIds.isEmpty()) {
			for (Integer eventId : eventIds) {
				events.add(this.eventService.get(eventId));
			}
		}
		concept.setEvents(events);

		return concept;
	}

	@RequestMapping(value = "/readConcept", method = RequestMethod.GET)
	public @ResponseBody
	Concept readConcept(ModelMap modelMap, @RequestParam Integer conceptId) throws Exception {
		Concept concept = this.conceptService.get(conceptId);

		modelMap.put("events", this.getSelectedEvents(concept));

		return concept;
	}

	@RequestMapping(value = "/isConceptInUse", method = RequestMethod.GET)
	public @ResponseBody
	boolean isConceptInUse(@RequestParam Integer conceptId) throws Exception {
		return this.conceptService.isAlreadyInUse(conceptId);
	}

	private List<EventDTO> getSelectedEvents(Concept concept) {
		List<Event> selectedEvents = concept.getEvents();
		List<Event> allEvents = this.eventService.getAll();

		List<EventDTO> eventsDTO = new ArrayList<EventDTO>();

		for (Event event : allEvents) {
			if (selectedEvents.contains(event)) {
				eventsDTO.add(this.newEventDTO(event, true));
			} else {
				eventsDTO.add(this.newEventDTO(event, false));
			}
		}
		return eventsDTO;
	}

	private EventDTO newEventDTO(Event event, boolean selected) {
		EventDTO eventDTO = new EventDTO();

		eventDTO.setCode(event.getCode());
		eventDTO.setDescription(event.getDescription());
		eventDTO.setDestinationAgentId(event.getDestinationAgent().getId());
		eventDTO.setOriginAgentId(event.getOriginAgent().getId());
		eventDTO.setId(event.getId());
		eventDTO.setSelected(selected);

		return eventDTO;
	}

	@RequestMapping(value = "/deleteConcept", method = RequestMethod.POST)
	public @ResponseBody
	boolean deleteConcept(@RequestParam Integer conceptId) throws Exception {
		return this.conceptService.delete(conceptId);
	}

	@RequestMapping(value = "/existsConcept", method = RequestMethod.GET)
	public @ResponseBody
	Boolean existsConcept(@RequestParam Integer code) throws Exception {
		return this.conceptService.exists(code);
	}

	@RequestMapping(value = "/isClientConcept", method = RequestMethod.GET)
	public @ResponseBody
	Boolean isClientConcept(@RequestParam Integer conceptId) throws Exception {
		return this.conceptService.get(conceptId).isClient();
	}

	@RequestMapping(value = "/getMatchedConcepts", method = RequestMethod.POST)
	public @ResponseBody
	String getMatchedConcepts(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<Concept> listConcepts;
		if (searchPhrase.matches("")) {
			listConcepts = this.conceptService.getPaginated(start, length);
			total = this.conceptService.getTotalNumber();
		} else {
			listConcepts = this.conceptService.getForAutocomplete(searchPhrase, null);
			total = listConcepts.size();
			if (total < start + length) {
				listConcepts = listConcepts.subList(start, (int) total);
			} else {
				listConcepts = listConcepts.subList(start, start + length);
			}
		}

		for (Concept concept : listConcepts) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", concept.getId());
			dataJson.put("code", concept.getCode());
			dataJson.put("description", concept.getDescription());
			String deliveryNoteEnumerator;
			if(concept.getDeliveryNoteEnumerator() != null) {
				 deliveryNoteEnumerator = StringUtility.addLeadingZeros(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4);
			}else{
				deliveryNoteEnumerator = "No definido";
			}

			dataJson.put("deliveryNotePOS", deliveryNoteEnumerator);
			dataJson.put("isClient", concept.isClient() == true ? "Si" : "No");
			dataJson.put("isPrintDeliveryNote", concept.isPrintDeliveryNote() == true ? "Si" : "No");
			dataJson.put("deliveryNoteCopies", concept.getDeliveryNoteCopies());
			dataJson.put("isRefund", concept.isRefund() == true ? "Si" : "No");
			dataJson.put("isInformAnmat", concept.isInformAnmat() == true ? "Si" : "No");
			dataJson.put("isActive", concept.isActive() == true ? "Si" : "No");
			dataJson.put("isClient", concept.isClient() == true ? "Si" : "No");
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
