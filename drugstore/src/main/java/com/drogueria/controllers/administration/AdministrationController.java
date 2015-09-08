package com.drogueria.controllers.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdministrationController {

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home() throws Exception {
		return "home";
	}

}
