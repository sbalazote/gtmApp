package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.model.Provider;
import com.drogueria.service.ProviderService;

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
