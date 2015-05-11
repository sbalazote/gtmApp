package com.drogueria.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView forbidden(Principal user) {
		ModelAndView model = new ModelAndView();
		model.addObject("error", 403);
		if (user != null) {
			model.addObject("msg", "Error!. El usuario: " + user.getName() + " no tiene permiso para acceder a este recurso.");
		} else {
			model.addObject("msg", "Error!. No tiene permiso para acceder a este recurso.");
		}
		model.setViewName("error");
		return model;

	}

	@RequestMapping(value = "/400")
	public ModelAndView badRequest() {
		ModelAndView model = new ModelAndView();
		model.addObject("error", 400);
		model.addObject("msg", "Error!. Petición incorrecta.");
		model.setViewName("error");
		return model;
	}

	@RequestMapping(value = "/404")
	public ModelAndView notFound() {
		ModelAndView model = new ModelAndView();
		model.addObject("error", 404);
		model.addObject("msg", "Error!. Página no encontrada.");
		model.setViewName("error");
		return model;
	}

	@RequestMapping(value = "/500")
	public ModelAndView internalServerError() {
		ModelAndView model = new ModelAndView();
		model.addObject("error", 500);
		model.addObject("msg", "Error interno del servidor.");
		model.setViewName("error");
		return model;
	}

}