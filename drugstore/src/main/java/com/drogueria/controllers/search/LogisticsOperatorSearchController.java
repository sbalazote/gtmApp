package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.model.LogisticsOperator;
import com.drogueria.service.LogisticsOperatorService;

@Controller
public class LogisticsOperatorSearchController {

	@Autowired
	private LogisticsOperatorService logisticsOperatorService;

	@RequestMapping(value = "/getLogisticsOperators", method = RequestMethod.GET)
	public @ResponseBody
	List<LogisticsOperator> getLogisticsOperators() {
		return this.logisticsOperatorService.getAll();
	}
}