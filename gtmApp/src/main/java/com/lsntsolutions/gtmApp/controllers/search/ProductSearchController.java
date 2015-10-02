package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import com.lsntsolutions.gtmApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.dto.ProviderSerializedProductDTO;
import com.lsntsolutions.gtmApp.helper.SerialParser;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.ProductGtin;

@Controller
public class ProductSearchController {

	@Autowired
	private ProductService productService;
	@Autowired
	private SerialParser serialParser;

	@RequestMapping(value = "/getProducts", method = RequestMethod.GET)
	public @ResponseBody
	List<Product> getProducts(@RequestParam String term, Boolean active) {
		return this.productService.getForAutocomplete(term, active);
	}

	@RequestMapping(value = "/getProduct", method = RequestMethod.GET)
	public @ResponseBody
	Product getProduct(@RequestParam Integer productId) {
		return this.productService.get(productId);
	}

	@RequestMapping(value = "/getProductBySerialOrGtin", method = RequestMethod.GET)
	public @ResponseBody
	Product getProductBySerialOrGtin(@RequestParam String serial) {
		if (serial != null && serial.length() > Constants.GTIN_LENGTH) {
			ProviderSerializedProductDTO productDTO = this.serialParser.parse(serial);
			if (productDTO != null && productDTO.getGtin() != null) {
				return this.productService.getByGtin(productDTO.getGtin());
			}
		} else {
			return this.productService.getByGtin(serial);
		}
		return null;
	}

	@RequestMapping(value = "/parseSerial", method = RequestMethod.GET)
	public @ResponseBody
	ProviderSerializedProductDTO parseSerial(@RequestParam String serial) {
		if(this.serialParser.isParseSelfSerial(serial)){
            return this.serialParser.parseSelfSerial(serial);
        }else {
            return this.serialParser.parse(serial);
        }
	}

	@RequestMapping(value = "/isParseSelfSerial", method = RequestMethod.GET)
	public @ResponseBody
	boolean parseSelfSerial(@RequestParam String serial) {
		return this.serialParser.isParseSelfSerial(serial);
	}

	@RequestMapping(value = "/getGtins", method = RequestMethod.GET)
	public @ResponseBody
	List<ProductGtin> getGtins(@RequestParam Integer productId) {
		return this.productService.get(productId).getGtins();
	}

}