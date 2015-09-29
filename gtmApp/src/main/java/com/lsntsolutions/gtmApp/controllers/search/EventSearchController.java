package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Event;
import com.lsntsolutions.gtmApp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EventSearchController {

	@Autowired
	private EventService eventService;

	@RequestMapping(value = "/getEvents", method = RequestMethod.GET)
	public @ResponseBody
	List<Event> getEvents() {
		return this.eventService.getAll();
	}

	@RequestMapping(value = "/getEventsIO", method = RequestMethod.GET)
	public @ResponseBody
	List<Event> getEventsIO(@RequestParam boolean input) {
		return this.eventService.getInputOutput(input);
	}
}
