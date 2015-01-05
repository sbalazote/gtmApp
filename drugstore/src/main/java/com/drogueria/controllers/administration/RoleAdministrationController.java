package com.drogueria.controllers.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RoleAdministrationController {

	@RequestMapping(value = "/addRole", method = RequestMethod.GET)
	public String addUser(ModelMap modelMap) throws Exception {
		return "addRole";
	}

	@RequestMapping(value = "/roleAdministration", method = RequestMethod.GET)
	public String roleAdministration(ModelMap modelMap) throws Exception {
		return "roleAdministration";
	}

}
