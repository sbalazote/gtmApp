package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.service.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConceptSearchController {

	@Autowired
	private ConceptService conceptService;

	@RequestMapping(value = "/getConcepts", method = RequestMethod.GET)
	public @ResponseBody
	List<Concept> getConcepts() {
		return this.conceptService.getAll();
	}

	@RequestMapping(value = "/getConcept", method = RequestMethod.GET)
	public @ResponseBody
	Concept getConcept(Integer conceptId) {
		return this.conceptService.get(conceptId);
	}

}
