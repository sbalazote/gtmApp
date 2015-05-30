package com.drogueria.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

	private static final Logger logger = Logger.getLogger(ExceptionController.class);

	@ExceptionHandler(Exception.class)
	public String handleException(HttpServletRequest request, Exception ex) throws Exception {
		if (this.isAjaxRequest(request)) {
			throw ex;
		} else {
			logger.error("Logging exception...", ex);
			return "error";
		}
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

}