package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.model.ProductBrand;
import com.drogueria.service.ProductBrandService;

@Controller
public class ProductBrandSearchController {

	@Autowired
	private ProductBrandService productBrandService;

	@RequestMapping(value = "/getProductBrands", method = RequestMethod.GET)
	public @ResponseBody
	List<ProductBrand> getProductBrands(@RequestParam String term) {
		return this.productBrandService.getForAutocomplete(term);
	}
}
