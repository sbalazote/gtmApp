package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProductDrugCategory;
import com.lsntsolutions.gtmApp.service.ProductDrugCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductDrugCategorySearchController {

	@Autowired
	private ProductDrugCategoryService productDrugCategoryService;

	@RequestMapping(value = "/getProductDrugCaregories", method = RequestMethod.GET)
	public @ResponseBody
	List<ProductDrugCategory> getProductDrugCaregories(@RequestParam String term) {
		return this.productDrugCategoryService.getForAutocomplete(term);
	}
}
