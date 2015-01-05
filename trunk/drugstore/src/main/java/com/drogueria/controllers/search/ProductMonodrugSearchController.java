package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.model.ProductMonodrug;
import com.drogueria.service.ProductMonodrugService;

@Controller
public class ProductMonodrugSearchController {

	@Autowired
	private ProductMonodrugService productMonodrugService;

	@RequestMapping(value = "/getProductMonodrugs", method = RequestMethod.GET)
	public @ResponseBody
	List<ProductMonodrug> getProductMonodrugs(@RequestParam String term) {
		return this.productMonodrugService.getForAutocomplete(term);
	}
}
