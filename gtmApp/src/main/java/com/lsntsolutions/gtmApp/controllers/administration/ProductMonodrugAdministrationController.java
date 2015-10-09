package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.ProductMonodrugDTO;
import com.lsntsolutions.gtmApp.model.ProductMonodrug;
import com.lsntsolutions.gtmApp.service.ProductMonodrugService;
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
public class ProductMonodrugAdministrationController {

	@Autowired
	private ProductMonodrugService productMonodrugService;

	@RequestMapping(value = "/productMonodrugs", method = RequestMethod.POST)
	public ModelAndView productMonodrugs(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("productMonodrugs", "productMonodrugs", this.productMonodrugService.getAll());
		} else {
			return new ModelAndView("productMonodrugs", "productMonodrugs", this.productMonodrugService.getForAutocomplete(searchPhrase, null));
		}
	}

	@RequestMapping(value = "/saveProductMonodrug", method = RequestMethod.POST)
	public @ResponseBody
	ProductMonodrug saveProductMonodrug(@RequestBody ProductMonodrugDTO productMonodrugDTO) throws Exception {
		ProductMonodrug productMonodrug = this.buildModel(productMonodrugDTO);
		this.productMonodrugService.save(productMonodrug);
		return productMonodrug;
	}

	private ProductMonodrug buildModel(ProductMonodrugDTO productMonodrugDTO) {
		ProductMonodrug productMonodrug = new ProductMonodrug();
		if (productMonodrugDTO.getId() != null) {
			productMonodrug.setId(productMonodrugDTO.getId());
		}
		productMonodrug.setCode(productMonodrugDTO.getCode());

		productMonodrug.setDescription(productMonodrugDTO.getDescription());
		productMonodrug.setActive(productMonodrugDTO.isActive());
		return productMonodrug;
	}

	@RequestMapping(value = "/readProductMonodrug", method = RequestMethod.GET)
	public @ResponseBody ProductMonodrug readProductMonodrug(Integer productMonodrugId) throws Exception {
		return this.productMonodrugService.get(productMonodrugId);
	}

	@RequestMapping(value = "/deleteProductMonodrug", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProductMonodrug(@RequestParam Integer productMonodrugId) throws Exception {
		return this.productMonodrugService.delete(productMonodrugId);
	}

	@RequestMapping(value = "/existsProductMonodrug", method = RequestMethod.GET)
	public @ResponseBody Boolean existsProductMonodrug(@RequestParam Integer code) throws Exception {
		return this.productMonodrugService.exists(code);
	}

	@RequestMapping(value = "/getMatchedProductMonodrugs", method = RequestMethod.POST)
	public @ResponseBody String getMatchedProductMonodrugs(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<ProductMonodrug> listProductMonodrugs = null;
		if (searchPhrase.matches("")) {
			listProductMonodrugs = this.productMonodrugService.getPaginated(start, length);
			total = this.productMonodrugService.getTotalNumber();
		} else {
			listProductMonodrugs = this.productMonodrugService.getForAutocomplete(searchPhrase, null);
			total = listProductMonodrugs.size();
			if (total < start + length) {
				listProductMonodrugs = listProductMonodrugs.subList(start, (int) total);
			} else {
				listProductMonodrugs = listProductMonodrugs.subList(start, start + length);
			}
		}

		for (ProductMonodrug productMonodrug : listProductMonodrugs) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", productMonodrug.getId());
			dataJson.put("code", productMonodrug.getCode());
			dataJson.put("description", productMonodrug.getDescription());
			dataJson.put("isActive", productMonodrug.isActive() == true ? "Si" : "No");
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
