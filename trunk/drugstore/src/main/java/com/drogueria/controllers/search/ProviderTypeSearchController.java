package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.model.ProviderType;
import com.drogueria.service.ProviderTypeService;

@Controller
public class ProviderTypeSearchController {

	@Autowired
	private ProviderTypeService providerTypeService;

	@RequestMapping(value = "/getProviderTypes", method = RequestMethod.GET)
	public @ResponseBody
	List<ProviderType> getProviderTypes() {
		return this.providerTypeService.getAll();
	}

}