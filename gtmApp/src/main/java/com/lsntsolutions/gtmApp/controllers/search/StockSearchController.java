package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.dto.ProviderSerializedProductDTO;
import com.lsntsolutions.gtmApp.helper.SerialParser;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.query.StockQuery;
import com.lsntsolutions.gtmApp.service.ProductService;
import com.lsntsolutions.gtmApp.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lsntsolutions.gtmApp.model.Stock;

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

	@RequestMapping(value = "/getStockForSearch", method = RequestMethod.POST)
	public @ResponseBody List<Stock> getStockForSearch(@RequestBody StockQuery stockQuery) throws Exception {
		return this.stockService.getStockForSearch(stockQuery);
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