package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.ProductDTO;
import com.lsntsolutions.gtmApp.dto.ProductGtinDTO;
import com.lsntsolutions.gtmApp.dto.ProductPriceDTO;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.ProductGtin;
import com.lsntsolutions.gtmApp.model.ProductPrice;
import com.lsntsolutions.gtmApp.service.*;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class ProductAdministrationController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductGtinService productGtinService;

	@Autowired
	private ProductBrandService productBrandService;

	@Autowired
	private ProductMonodrugService productMonodrugService;

	@Autowired
	private ProductGroupService productGroupService;

	@Autowired
	private ProductDrugCategoryService productDrugCategoryService;

	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ModelAndView products(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("products", "products", this.productService.getAll());
		} else {
			return new ModelAndView("products", "products", this.productService.getForAutocomplete(searchPhrase, null, null, null, null, null, null, null));
		}
	}

	@RequestMapping(value = "/productAdministration", method = RequestMethod.GET)
	public String productAdministration(ModelMap modelMap) throws Exception {
		this.setModelMaps(modelMap);
		return "productAdministration";
	}

	@RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
	public @ResponseBody
	Product saveProduct(@RequestBody ProductDTO productDTO) throws Exception {
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
		product.setBrand(this.productBrandService.get(productDTO.getBrandId()));
		product.setMonodrug(this.productMonodrugService.get(productDTO.getMonodrugId()));
		product.setGroup(this.productGroupService.get(productDTO.getGroupId()));
		product.setDrugCategory(this.productDrugCategoryService.get(productDTO.getDrugCategoryId()));
		product.setCold(productDTO.isCold());
		product.setInformAnmat(productDTO.isInformAnmat());
		product.setType(productDTO.getType());
		product.setActive(productDTO.isActive());

		List<ProductGtin> productGtins = null;
		if (product.getGtins() == null) {
			productGtins = new ArrayList<ProductGtin>();
		} else {
			productGtins = product.getGtins();
		}
		Iterator<ProductGtinDTO> productGtinDTOIterator = productDTO.getGtins().iterator();
		// Itero sobre los gtins que vienen de la UI.
		while (productGtinDTOIterator.hasNext()) {
			ProductGtinDTO productGtinDTO = productGtinDTOIterator.next();
			ProductGtin productGtin = new ProductGtin();
			productGtin.setId(productGtinDTO.getId());
			productGtin.setNumber(productGtinDTO.getNumber());
			productGtin.setDate(productGtinDTO.getDate());
			// Si no existía el gtin en la db se agrega; caso contrario se actualizan unicamente sus valores.
			if (!product.existsGtin(productGtinDTO.getNumber())) {
				productGtins.add(productGtin);
			} else {
				productGtin = product.getGtinByNumber(productGtinDTO.getNumber());
				productGtin.setNumber(productGtinDTO.getNumber());
				productGtin.setDate(productGtinDTO.getDate());
			}
		}
		// Se eliminan los gtins que estaban en la db pero fueron borrados.
		Iterator<ProductGtin> productGtinIterator = productGtins.iterator();
		while (productGtinIterator.hasNext()) {
			ProductGtin productGtin = productGtinIterator.next();
			if (!productDTO.existsGtin(productGtin.getNumber())) {
				productGtinIterator.remove();
			}
		}

		product.setGtins(productGtins);

		List<ProductPrice> productPrices = null;
		if (product.getPrices() == null) {
			productPrices = new ArrayList<ProductPrice>();
		} else {
			productPrices = product.getPrices();
		}
		Iterator<ProductPriceDTO> productPriceDTOIterator = productDTO.getPrices().iterator();
		// Itero sobre los precios que vienen de la UI.
		while (productPriceDTOIterator.hasNext()) {
			ProductPriceDTO productPriceDTO = productPriceDTOIterator.next();
			ProductPrice productPrice = new ProductPrice();
			productPrice.setId(productPriceDTO.getId());
			productPrice.setPrice(productPriceDTO.getPrice());
			productPrice.setDate(productPriceDTO.getDate());
			// Si no existía el precio en la db se agrega; caso contrario se actualizan unicamente sus valores.
			if (!product.existsPrice(productPriceDTO.getPrice())) {
				productPrices.add(productPrice);
			} else {
				productPrice = product.getPrice(productPriceDTO.getPrice());
				productPrice.setPrice(productPriceDTO.getPrice());
				productPrice.setDate(productPriceDTO.getDate());
			}
		}
		// Se eliminan los precios que estaban en la db pero fueron borrados.
		Iterator<ProductPrice> productPriceIterator = productPrices.iterator();
		while (productPriceIterator.hasNext()) {
			ProductPrice productPrice = productPriceIterator.next();
			if (!productDTO.existsPrice(productPrice.getPrice())) {
				productPriceIterator.remove();
			}
		}

		product.setPrices(productPrices);

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
		Iterator<ProductGtin> productGtinIterator = product.getGtins().iterator();
		List<ProductGtinDTO> productGtinDTOs = new ArrayList<ProductGtinDTO>();
		while (productGtinIterator.hasNext()) {
			ProductGtin productGtin = productGtinIterator.next();
			ProductGtinDTO productGtinDTO = new ProductGtinDTO();
			productGtinDTO.setId(productGtin.getId());
			productGtinDTO.setNumber(productGtin.getNumber());
			productGtinDTO.setDate(productGtin.getDate());
			productGtinDTOs.add(productGtinDTO);
		}
		productDTO.setGtins(productGtinDTOs);
		Iterator<ProductPrice> productPriceIterator = product.getPrices().iterator();
		List<ProductPriceDTO> productPriceDTOs = new ArrayList<ProductPriceDTO>();
		while (productPriceIterator.hasNext()) {
			ProductPrice productPrice = productPriceIterator.next();
			ProductPriceDTO productPriceDTO = new ProductPriceDTO();
			productPriceDTO.setId(productPrice.getId());
			productPriceDTO.setPrice(productPrice.getPrice());
			productPriceDTO.setDate(productPrice.getDate());
			productPriceDTOs.add(productPriceDTO);
		}
		productDTO.setPrices(productPriceDTOs);

		return productDTO;
	}

	@RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProduct(@RequestParam Integer productId) throws Exception {
		return this.productService.delete(productId);
	}

	@RequestMapping(value = "/isGtinUsed", method = RequestMethod.POST)
	public @ResponseBody boolean isGtinUsed(@RequestParam String gtinNumber) throws Exception {
		return this.productGtinService.isGtinUsed(gtinNumber);
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

		String sortId = parametersMap.get("sort[id]");
		String sortCode = parametersMap.get("sort[code]");
		String sortDescription = parametersMap.get("sort[description]");
		String sortGtin = parametersMap.get("sort[gtin]");
		String sortPrice = parametersMap.get("sort[price]");
		String sortIsCold = parametersMap.get("sort[isCold]");

		List<Product> listProducts = null;
		listProducts = this.productService.getForAutocomplete(searchPhrase, null, sortId, sortCode, sortDescription, sortGtin, sortPrice, sortIsCold);
		total = listProducts.size();
		if (total < start + length) {
			listProducts = listProducts.subList(start, (int) total);
		} else {
			if(length > 0) {
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
			dataJson.put("isCold", product.isCold() ? "Si" : "No");
			dataJson.put("isInformAnmat", product.isInformAnmat() ? "Si" : "No");
			dataJson.put("type", product.getType() == "BE" ? "Lote/Vto." : (product.getType() == "PS") ? "Origen" : "Propio");
			dataJson.put("isActive", product.isActive() ? "Si" : "No");
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
