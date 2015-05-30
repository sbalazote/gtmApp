package com.drogueria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap modelMap, @RequestParam(value = "error", required = false) String error) throws Exception {
		if (error != null) {
			modelMap.put("error", "Usuario / Contrasenia incorrecta, vuelva a intentar");
		}
		return "login";
	}

}