package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.model.ProductDrugCategory;
import com.drogueria.service.ProductDrugCategoryService;

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
