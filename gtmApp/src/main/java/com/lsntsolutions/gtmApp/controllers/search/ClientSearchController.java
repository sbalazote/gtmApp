package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lsntsolutions.gtmApp.service.ClientService;

@Controller
public class ClientSearchController {

	@Autowired
	private ClientService clientService;

	@RequestMapping(value = "/getClients", method = RequestMethod.GET)
	public @ResponseBody
	List<Client> getClients() {
		return this.clientService.getAll();
	}

}