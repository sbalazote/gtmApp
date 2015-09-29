package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import com.lsntsolutions.gtmApp.service.ProductGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lsntsolutions.gtmApp.model.ProductGroup;

@Controller
public class ProductGroupSearchController {

	@Autowired
	private ProductGroupService productGroupService;

	@RequestMapping(value = "/getProductGroups", method = RequestMethod.GET)
	public @ResponseBody
	List<ProductGroup> getProductGroups() {
		return this.productGroupService.getAll();
	}
}
