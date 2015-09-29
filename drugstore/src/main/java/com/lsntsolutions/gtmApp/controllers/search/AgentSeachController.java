package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Agent;
import com.lsntsolutions.gtmApp.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AgentSeachController {

	@Autowired
	private AgentService agentService;

	@RequestMapping(value = "/getAgents", method = RequestMethod.GET)
	public @ResponseBody
	List<Agent> getAgents() {
		return this.agentService.getAll();
	}
}
