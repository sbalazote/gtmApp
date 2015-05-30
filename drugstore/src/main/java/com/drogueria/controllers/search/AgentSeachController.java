package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.model.Agent;
import com.drogueria.service.AgentService;

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
