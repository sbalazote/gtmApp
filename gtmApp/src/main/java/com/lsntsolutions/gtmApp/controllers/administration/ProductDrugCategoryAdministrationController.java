package com.lsntsolutions.gtmApp.controllers.administration;

import java.util.List;
import java.util.Map;

import com.lsntsolutions.gtmApp.model.ProductDrugCategory;
import com.lsntsolutions.gtmApp.service.ProductDrugCategoryService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lsntsolutions.gtmApp.dto.ProductDrugCategoryDTO;

@Controller
public class ProductDrugCategoryAdministrationController {

	@Autowired
	private ProductDrugCategoryService productDrugCategoryService;

	@RequestMapping(value = "/productDrugCategories", method = RequestMethod.POST)
	public ModelAndView productDrugCategories() {
		return new ModelAndView("productDrugCategories", "productDrugCategories", this.productDrugCategoryService.getAll());
	}

	@RequestMapping(value = "/saveProductDrugCategory", method = RequestMethod.POST)
	public @ResponseBody
	ProductDrugCategory saveProductBrand(@RequestBody ProductDrugCategoryDTO productDrugCategoryDTO) throws Exception {
		ProductDrugCategory productDrugCategory = this.buildModel(productDrugCategoryDTO);
		this.productDrugCategoryService.save(productDrugCategory);
		return productDrugCategory;
	}

	private ProductDrugCategory buildModel(ProductDrugCategoryDTO productDrugCategoryDTO) {
		ProductDrugCategory productDrugCategory = new ProductDrugCategory();

		if (productDrugCategoryDTO.getId() != null) {
			productDrugCategory.setId(productDrugCategoryDTO.getId());
		}
		productDrugCategory.setCode(productDrugCategoryDTO.getCode());

		productDrugCategory.setDescription(productDrugCategoryDTO.getDescription());
		productDrugCategory.setActive(productDrugCategoryDTO.isActive());
		return productDrugCategory;
	}

	@RequestMapping(value = "/readProductDrugCategory", method = RequestMethod.GET)
	public @ResponseBody ProductDrugCategory readProductDrugCategory(@RequestParam Integer productDrugCategoryId) throws Exception {
		return this.productDrugCategoryService.get(productDrugCategoryId);
	}

	@RequestMapping(value = "/deleteProductDrugCategory", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProductDrugCategory(@RequestParam Integer productDrugCategoryId) throws Exception {
		return this.productDrugCategoryService.delete(productDrugCategoryId);
	}

	@RequestMapping(value = "/existsProductDrugCategory", method = RequestMethod.GET)
	public @ResponseBody Boolean existsProductDrugCategory(@RequestParam Integer code) throws Exception {
		return this.productDrugCategoryService.exists(code);
	}

	@RequestMapping(value = "/getMatchedProductDrugCategories", method = RequestMethod.POST)
	public @ResponseBody String getMatchedProductDrugCategories(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<ProductDrugCategory> listProductDrugCategories = null;
		if (searchPhrase.matches("")) {
			listProductDrugCategories = this.productDrugCategoryService.getPaginated(start, length);
			total = this.productDrugCategoryService.getTotalNumber();
		} else {
			listProductDrugCategories = this.productDrugCategoryService.getForAutocomplete(searchPhrase, null);
			total = listProductDrugCategories.size();
			if (total < start + length) {
				listProductDrugCategories = listProductDrugCategories.subList(start, (int) total);
			} else {
				listProductDrugCategories = listProductDrugCategories.subList(start, start + length);
			}
		}

		for (ProductDrugCategory productDrugCategory : listProductDrugCategories) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", productDrugCategory.getId());
			dataJson.put("code", productDrugCategory.getCode());
			dataJson.put("description", productDrugCategory.getDescription());
			dataJson.put("isActive", productDrugCategory.isActive() == true ? "Si" : "No");
			jsonArray.put(dataJson);
		}

		JSONObject responseJson = new JSONObject();
		responseJson.put("current", current);
		responseJson.put("rowCount", (total < (start + length)) ? (total - length) : length);
		responseJson.put("rows", jsonArray);
		responseJson.put("total", total);

		return responseJson.toString();
	}
}
