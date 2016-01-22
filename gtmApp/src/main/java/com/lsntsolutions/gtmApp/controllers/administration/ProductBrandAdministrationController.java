package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.ProductBrandDTO;
import com.lsntsolutions.gtmApp.model.ProductBrand;
import com.lsntsolutions.gtmApp.service.ProductBrandService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class ProductBrandAdministrationController {

	@Autowired
	private ProductBrandService productBrandService;

	@RequestMapping(value = "/productBrands", method = RequestMethod.POST)
	public ModelAndView productBrands(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("productBrands", "productBrands", this.productBrandService.getAll());
		} else {
			return new ModelAndView("productBrands", "productBrands", this.productBrandService.getForAutocomplete(searchPhrase, null, null, null, null, null));
		}
	}

	@RequestMapping(value = "/saveProductBrand", method = RequestMethod.POST)
	public @ResponseBody
	ProductBrand saveProductBrand(@RequestBody ProductBrandDTO productBrandDTO) throws Exception {
		ProductBrand productBrand = this.buildModel(productBrandDTO);
		this.productBrandService.save(productBrand);
		return productBrand;
	}

	private ProductBrand buildModel(ProductBrandDTO productBrandDTO) {
		ProductBrand productBrand = new ProductBrand();

		if (productBrandDTO.getId() != null) {
			productBrand.setId(productBrandDTO.getId());
		}
		productBrand.setCode(productBrandDTO.getCode());

		productBrand.setDescription(productBrandDTO.getDescription());
		productBrand.setActive(productBrandDTO.isActive());

		return productBrand;
	}

	@RequestMapping(value = "/readProductBrand", method = RequestMethod.GET)
	public @ResponseBody ProductBrand readProductBrand(@RequestParam Integer productBrandId) throws Exception {
		return this.productBrandService.get(productBrandId);
	}

	@RequestMapping(value = "/deleteProductBrand", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProductBrand(@RequestParam Integer productBrandId) throws Exception {
		return this.productBrandService.delete(productBrandId);
	}

	@RequestMapping(value = "/existsProductBrand", method = RequestMethod.GET)
	public @ResponseBody Boolean existsProductBrand(@RequestParam Integer code) throws Exception {
		return this.productBrandService.exists(code);
	}

	@RequestMapping(value = "/getMatchedProductBrands", method = RequestMethod.POST)
	public @ResponseBody String getMatchedProductBrands(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		String sortId = parametersMap.get("sort[id]");
		String sortCode = parametersMap.get("sort[code]");
		String sortDescription = parametersMap.get("sort[description]");
		String sortIsActive = parametersMap.get("sort[isActive]");

		List<ProductBrand> listProductBrands = null;
		listProductBrands = this.productBrandService.getForAutocomplete(searchPhrase, null, sortId, sortCode, sortDescription, sortIsActive);
		total = listProductBrands.size();
		if (total < start + length) {
			listProductBrands = listProductBrands.subList(start, (int) total);
		} else {
			if(length > 0) {
				listProductBrands = listProductBrands.subList(start, start + length);
			}
		}

		for (ProductBrand productBrand : listProductBrands) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", productBrand.getId());
			dataJson.put("code", productBrand.getCode());
			dataJson.put("description", productBrand.getDescription());
			dataJson.put("isActive", productBrand.isActive() ? "Si" : "No");
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