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

import com.drogueria.dto.ProductGroupDTO;
import com.drogueria.model.ProductGroup;
import com.drogueria.service.ProductGroupService;

@Controller
public class ProductGroupAdministrationController {

	@Autowired
	private ProductGroupService productGroupService;

	@RequestMapping(value = "/productGroups", method = RequestMethod.POST)
	public ModelAndView productGroups() {
		return new ModelAndView("productGroups", "productGroups", this.productGroupService.getAll());
	}

	@RequestMapping(value = "/saveProductGroup", method = RequestMethod.POST)
	public @ResponseBody ProductGroup saveProductGroup(@RequestBody ProductGroupDTO productGroupDTO) throws Exception {
		ProductGroup productGroup = this.buildModel(productGroupDTO);
		this.productGroupService.save(productGroup);
		return productGroup;
	}

	private ProductGroup buildModel(ProductGroupDTO productGroupDTO) {
		ProductGroup productGroup = new ProductGroup();
		if (productGroupDTO.getId() != null) {
			productGroup.setId(productGroupDTO.getId());
		}
		productGroup.setCode(productGroupDTO.getCode());

		productGroup.setDescription(productGroupDTO.getDescription());
		productGroup.setActive(productGroupDTO.isActive());
		return productGroup;
	}

	@RequestMapping(value = "/readProductGroup", method = RequestMethod.GET)
	public @ResponseBody ProductGroup readProductGroup(@RequestParam Integer productGroupId) throws Exception {
		return this.productGroupService.get(productGroupId);
	}

	@RequestMapping(value = "/deleteProductGroup", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProductGroup(@RequestParam Integer productGroupId) throws Exception {
		return this.productGroupService.delete(productGroupId);
	}

	@RequestMapping(value = "/existsProductGroup", method = RequestMethod.POST)
	public @ResponseBody Boolean existsProductGroup(@RequestParam Integer code) throws Exception {
		return this.productGroupService.exists(code);
	}

	@RequestMapping(value = "/getMatchedProductGroups", method = RequestMethod.POST)
	public @ResponseBody String getMatchedProductGroups(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<ProductGroup> listProductGroups = null;
		if (searchPhrase.matches("")) {
			listProductGroups = this.productGroupService.getPaginated(start, length);
			total = this.productGroupService.getTotalNumber();
		} else {
			listProductGroups = this.productGroupService.getForAutocomplete(searchPhrase, null);
			total = listProductGroups.size();
			if (total < start + length) {
				listProductGroups = listProductGroups.subList(start, (int) total);
			} else {
				listProductGroups = listProductGroups.subList(start, start + length);
			}
		}

		for (ProductGroup productGroup : listProductGroups) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", productGroup.getId());
			dataJson.put("code", productGroup.getCode());
			dataJson.put("description", productGroup.getDescription());
			dataJson.put("isActive", productGroup.isActive() == true ? "Si" : "No");
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
