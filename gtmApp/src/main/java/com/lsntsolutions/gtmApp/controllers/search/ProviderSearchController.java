package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lsntsolutions.gtmApp.model.Provider;
import com.lsntsolutions.gtmApp.service.ProviderService;

@Controller
public class ProviderSearchController {

	@Autowired
	private ProviderService providerService;

	@RequestMapping(value = "/getProviders", method = RequestMethod.GET)
	public @ResponseBody
	List<Provider> getProviders() {
		return this.providerService.getAll();
	}

}
