package com.drogueria.controllers.administration;

import java.util.List;
import java.util.Map;

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

import com.drogueria.dto.ProductBrandDTO;
import com.drogueria.model.ProductBrand;
import com.drogueria.service.ProductBrandService;

@Controller
public class ProductBrandAdministrationController {

	@Autowired
	private ProductBrandService productBrandService;

	@RequestMapping(value = "/productBrands", method = RequestMethod.POST)
	public ModelAndView productBrands() {
		return new ModelAndView("productBrands", "productBrands", this.productBrandService.getAll());
	}

	@RequestMapping(value = "/saveProductBrand", method = RequestMethod.POST)
	public @ResponseBody ProductBrand saveProductBrand(@RequestBody ProductBrandDTO productBrandDTO) throws Exception {
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

		List<ProductBrand> listProductBrands = null;
		if (searchPhrase.matches("")) {
			listProductBrands = this.productBrandService.getPaginated(start, length);
			total = this.productBrandService.getTotalNumber();
		} else {
			listProductBrands = this.productBrandService.getForAutocomplete(searchPhrase, null);
			total = listProductBrands.size();
			if (total < start + length) {
				listProductBrands = listProductBrands.subList(start, (int) total);
			} else {
				listProductBrands = listProductBrands.subList(start, start + length);
			}
		}

		for (ProductBrand productBrand : listProductBrands) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", productBrand.getId());
			dataJson.put("code", productBrand.getCode());
			dataJson.put("description", productBrand.getDescription());
			dataJson.put("isActive", productBrand.isActive() == true ? "Si" : "No");
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