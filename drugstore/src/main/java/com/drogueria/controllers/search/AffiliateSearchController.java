package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.model.Affiliate;
import com.drogueria.service.AffiliateService;

@Controller
public class AffiliateSearchController {

	@Autowired
	private AffiliateService affiliateService;

	@RequestMapping(value = "/getAffiliates", method = RequestMethod.GET)
	public @ResponseBody
	List<Affiliate> getAffiliates(@RequestParam String term, Integer clientId, Boolean active, @RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize) {
		return this.affiliateService.getForAutocomplete(term, clientId, active, pageNumber, pageSize);
	}

	@RequestMapping(value = "/getAllAffiliatesByClient", method = RequestMethod.GET)
	public @ResponseBody
	List<Affiliate> getAffiliates(@RequestParam Integer clientId, Boolean active) {
		return this.affiliateService.getAllAffiliatesByClient(clientId, active);
	}
}
