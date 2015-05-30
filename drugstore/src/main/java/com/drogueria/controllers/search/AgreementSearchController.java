package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.model.Agreement;
import com.drogueria.service.AgreementService;

@Controller
public class AgreementSearchController {

	@Autowired
	private AgreementService agreementService;

	@RequestMapping(value = "/getAgreements", method = RequestMethod.GET)
	public @ResponseBody
	List<Agreement> getAgreements() {
		return this.agreementService.getAll();
	}

}
