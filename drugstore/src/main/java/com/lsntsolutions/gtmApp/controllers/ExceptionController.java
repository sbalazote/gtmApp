package com.lsntsolutions.gtmApp.controllers;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {

	private static final Logger logger = Logger.getLogger(ExceptionController.class);

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(HttpServletRequest request, Exception ex) throws Exception {
		if (this.isAjaxRequest(request)) {
			throw ex;
		} else {
			logger.error("Logging exception...", ex);
			ModelAndView model = new ModelAndView();
			model.addObject("msg", ex.getMessage());
			model.setViewName("error");
			return model;
		}
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

}