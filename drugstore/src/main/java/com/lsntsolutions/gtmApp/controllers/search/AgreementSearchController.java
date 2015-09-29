package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Agreement;
import com.lsntsolutions.gtmApp.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
