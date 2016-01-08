package com.lsntsolutions.gtmApp.controllers.search;

import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.dto.ProviderSerializedProductDTO;
import com.lsntsolutions.gtmApp.dto.StockDTO;
import com.lsntsolutions.gtmApp.helper.SerialParser;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.Stock;
import com.lsntsolutions.gtmApp.query.StockQuery;
import com.lsntsolutions.gtmApp.service.ProductService;
import com.lsntsolutions.gtmApp.service.StockService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class StockSearchController {

	@Autowired
	private ProductService productService;
	@Autowired
	private StockService stockService;
	@Autowired
	private SerialParser serialParser;

	@RequestMapping(value = "/getProductAmount", method = RequestMethod.GET)
	public @ResponseBody Long getProductAmount(@RequestParam Integer productId, Integer agreementId, Integer provisioningId) {
		return this.stockService.getProductAmount(productId, agreementId, provisioningId);
	}

	@RequestMapping(value = "/getCountStockSearch", method = RequestMethod.POST)
	public @ResponseBody boolean getCountStockSearch(@RequestBody StockQuery stockQuery) throws Exception {
		return this.stockService.getCountStockSearch(stockQuery);
	}

	@RequestMapping(value = "/getStockFromProductAndAgreement", method = RequestMethod.POST)
	public @ResponseBody List<Stock> getStockFromProductAndAgreement(@RequestParam Integer productId, @RequestParam Integer agreementId) throws Exception {
		StockQuery stockQuery = StockQuery.createFromParameters(null, null, productId, agreementId, null, null, null);
		return this.stockService.getStockForSearch(stockQuery);
	}

	@RequestMapping(value = "/getStockForSearch", method = RequestMethod.POST)
	public @ResponseBody String getStockForSearch(@RequestParam Map<String, Object> parametersMap) throws Exception {
		//return this.stockService.getStockForSearch(stockQuery);
		String searchPhrase = (String) parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt((String) parametersMap.get("current"));
		Integer rowCount = Integer.parseInt((String) parametersMap.get("rowCount"));

		String sortCode = (String) parametersMap.get("sort[code]");
		String sortProduct = (String) parametersMap.get("sort[product]");
		String sortAgreement = (String) parametersMap.get("sort[agreement]");
		String sortGtin = (String) parametersMap.get("sort[gtin]");
		String sortAmount = (String) parametersMap.get("sort[amount]");

		String agreementId = (String) parametersMap.get("agreementId");
		String batchNumber = (String) parametersMap.get("batchNumber");
		String expirateDateFrom = (String) parametersMap.get("expirateDateFrom");
		String expirateDateTo = (String) parametersMap.get("expirateDateTo");
		String monodrugId = (String) parametersMap.get("monodrugId");
		String productId = (String) parametersMap.get("productId");
		String serialNumber = (String) parametersMap.get("serialNumber");

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<StockDTO> stockList = null;
			stockList = this.stockService.getForAutocomplete(searchPhrase, sortCode, sortProduct, sortAgreement, sortGtin, sortAmount, agreementId, batchNumber, expirateDateFrom, expirateDateTo, monodrugId, productId, serialNumber);
			total = stockList.size();
			if (total < start + length) {
				stockList = stockList.subList(start, (int) total);
			} else {
				stockList = stockList.subList(start, start + length);
			}

		for (StockDTO stockDTO: stockList) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", stockDTO.getProductId());
			dataJson.put("code", stockDTO.getProductCode());
			dataJson.put("product", stockDTO.getProductDescription());
			dataJson.put("agreementId", stockDTO.getAgreementId());
			dataJson.put("agreement", stockDTO.getAgreementDescription());
			dataJson.put("gtin", stockDTO.getGtinNumber());
			dataJson.put("serialNumber", stockDTO.getSerialNumber());
			dataJson.put("amount", stockDTO.getAmount());
			jsonArray.put(dataJson);
		}

		JSONObject responseJson = new JSONObject();
		responseJson.put("current", current);
		responseJson.put("rowCount", (total < (start + length)) ? (total - length) : length);
		responseJson.put("rows", jsonArray);
		responseJson.put("total", total);

		return responseJson.toString();
	}

	@RequestMapping(value = "/getProductFromStock", method = RequestMethod.GET)
	public @ResponseBody List<Product> getProductFromStock(@RequestParam String term, Integer agreementId) throws Exception {
		return this.stockService.getForAutocomplete(term, agreementId);
	}

	@RequestMapping(value = "/getProductFromStockBySerialOrGtin", method = RequestMethod.GET)
	public @ResponseBody Product getProductFromStockBySerialOrGtin(@RequestParam String serial, Integer agreementId) {
		if (serial != null && serial.length() > Constants.GTIN_LENGTH) {
			ProviderSerializedProductDTO productDTO = this.serialParser.parse(serial);
			if (productDTO != null && productDTO.getGtin() != null) {
				return this.stockService.getByGtin(productDTO.getGtin(), agreementId);
			}
		} else {
			return this.stockService.getByGtin(serial, agreementId);
		}
		return null;
	}

	@RequestMapping(value = "/stocks", method = RequestMethod.POST)
	public @ResponseBody ModelAndView stocks(HttpServletRequest request) throws Exception {
		StockQuery stockQuery = this.getStockQuery(request);
		return new ModelAndView("stocks", "stocks", this.stockService.getStockForSearch(stockQuery));
	}

	private StockQuery getStockQuery(HttpServletRequest request) {
		Integer productId = null;
		if (!(request.getParameterValues("productId")[0]).equals("null")) {
			productId = Integer.valueOf(request.getParameterValues("productId")[0]);
		}
		Integer agreementId = null;
		if (!(request.getParameterValues("agreementId")[0]).equals("null")) {
			agreementId = Integer.valueOf(request.getParameterValues("agreementId")[0]);
		}
		Integer monodrugId = null;
		if (!(request.getParameterValues("monodrugId")[0]).equals("null")) {
			agreementId = Integer.valueOf(request.getParameterValues("monodrugId")[0]);
		}
		StockQuery stockQuery = StockQuery.createFromParameters(request.getParameterValues("expirateDateFrom")[0],
				request.getParameterValues("expirateDateTo")[0], productId, agreementId, request.getParameterValues("serialNumber")[0],
				request.getParameterValues("batchNumber")[0],monodrugId);
		return stockQuery;
	}
}