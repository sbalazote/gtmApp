package com.drogueria.controllers.administration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.drogueria.dto.ProductDTO;
import com.drogueria.model.Product;
import com.drogueria.model.ProductGtin;
import com.drogueria.model.ProductPrice;
import com.drogueria.service.ProductBrandService;
import com.drogueria.service.ProductDrugCategoryService;
import com.drogueria.service.ProductGroupService;
import com.drogueria.service.ProductMonodrugService;
import com.drogueria.service.ProductService;

@Controller
public class ProductAdministrationController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductBrandService productBrandService;

	@Autowired
	private ProductMonodrugService productMonodrugService;

	@Autowired
	private ProductGroupService productGroupService;

	@Autowired
	private ProductDrugCategoryService productDrugCategoryService;

	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ModelAndView products() {
		return new ModelAndView("products", "products", this.productService.getAll());
	}

	@RequestMapping(value = "/productAdministration", method = RequestMethod.GET)
	public String productAdministration(ModelMap modelMap) throws Exception {
		this.setModelMaps(modelMap);
		return "productAdministration";
	}

	@RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
	public @ResponseBody Product saveProduct(@RequestBody ProductDTO productDTO) throws Exception {
		Product product = this.buildModel(productDTO);
		this.productService.save(product);
		return product;
	}

	private Product buildModel(ProductDTO productDTO) {
		Product product;
		if (productDTO.getId() != null) {
			product = this.productService.get(productDTO.getId());
		} else {
			product = new Product();
		}

		product.setCode(productDTO.getCode());
		product.setDescription(productDTO.getDescription());
		if (!product.existsGtin(productDTO.getGtin())) {
			ProductGtin productGtin = new ProductGtin();
			productGtin.setDate(new Date());
			productGtin.setNumber(productDTO.getGtin());
			if (product.getGtins() == null) {
				product.setGtins(new ArrayList<ProductGtin>());
			}
			product.getGtins().add(productGtin);
		}

		product.setBrand(this.productBrandService.get(productDTO.getBrandId()));
		product.setMonodrug(this.productMonodrugService.get(productDTO.getMonodrugId()));
		product.setGroup(this.productGroupService.get(productDTO.getGroupId()));
		product.setDrugCategory(this.productDrugCategoryService.get(productDTO.getDrugCategoryId()));

		if (!product.existsPrice(productDTO.getPrice())) {
			ProductPrice productPrice = new ProductPrice();
			productPrice.setDate(new Date());
			productPrice.setPrice(productDTO.getPrice());
			if (product.getPrices() == null) {
				product.setPrices(new ArrayList<ProductPrice>());
			}
			product.getPrices().add(productPrice);
		}

		product.setCold(productDTO.isCold());
		product.setInformAnmat(productDTO.isInformAnmat());
		product.setType(productDTO.getType());
		product.setActive(productDTO.isActive());

		return product;
	}

	@RequestMapping(value = "/alfabetaUpdateProducts", method = RequestMethod.GET)
	public String alfabetaUpdateProducts(ModelMap modelMap) throws Exception {
		return "alfabetaUpdateProducts";
	}

	public void setModelMaps(ModelMap modelMap) {
		modelMap.put("brands", this.productBrandService.getAll());
		modelMap.put("monodrugs", this.productMonodrugService.getAll());
		modelMap.put("productGroups", this.productGroupService.getAll());
		modelMap.put("drugCategories", this.productDrugCategoryService.getAll());
	}

	@RequestMapping(value = "/readProduct", method = RequestMethod.GET)
	public @ResponseBody ProductDTO readProduct(@RequestParam Integer productId) throws Exception {
		Product product = this.productService.get(productId);
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setCode(product.getCode());
		productDTO.setDescription(product.getDescription());
		productDTO.setGtin(product.getLastGtin());
		productDTO.setBrandId(product.getBrand().getId());
		productDTO.setMonodrugId(product.getMonodrug().getId());
		productDTO.setGroupId(product.getGroup().getId());
		productDTO.setDrugCategoryId(product.getDrugCategory().getId());
		productDTO.setPrice(product.getLastPrice());
		productDTO.setCold(product.isCold());
		productDTO.setInformAnmat(product.isInformAnmat());
		productDTO.setType(product.getType());
		productDTO.setActive(product.isActive());

		return productDTO;
	}

	@RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProduct(@RequestParam Integer productId) throws Exception {
		return this.productService.delete(productId);
	}

	@RequestMapping(value = "/existsProduct", method = RequestMethod.GET)
	public @ResponseBody Boolean existsProduct(@RequestParam Integer code) throws Exception {
		return this.productService.exists(code);
	}

	@RequestMapping(value = "/getMatchedProducts", method = RequestMethod.POST)
	public @ResponseBody String getMatchedProducts(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<Product> listProducts = null;
		if (searchPhrase.matches("")) {
			listProducts = this.productService.getPaginated(start, length);
			total = this.productService.getTotalNumber();
		} else {
			listProducts = this.productService.getForAutocomplete(searchPhrase, null);
			total = listProducts.size();
			if (total < start + length) {
				listProducts = listProducts.subList(start, (int) total);
			} else {
				listProducts = listProducts.subList(start, start + length);
			}
		}

		for (Product product : listProducts) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", product.getId());
			dataJson.put("code", product.getCode());
			dataJson.put("description", product.getDescription());
			dataJson.put("gtin", product.getLastGtin());
			dataJson.put("brand", product.getBrand().getDescription());
			dataJson.put("monodrug", product.getMonodrug().getDescription());
			dataJson.put("group", product.getGroup().getDescription());
			dataJson.put("drugCategory", product.getDrugCategory().getDescription());
			dataJson.put("price", product.getLastPrice());
			dataJson.put("isCold", product.isCold() == true ? "Si" : "No");
			dataJson.put("isInformAnmat", product.isInformAnmat() == true ? "Si" : "No");
			dataJson.put("type", product.getType() == "BE" ? "Lote/Vto." : (product.getType() == "PS") ? "Origen" : "Propio");
			dataJson.put("isActive", product.isActive() == true ? "Si" : "No");
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
