package com.drogueria.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.constant.Constants;
import com.drogueria.dto.ProviderSerializedProductDTO;
import com.drogueria.helper.SerialParser;
import com.drogueria.model.Product;
import com.drogueria.model.ProductGtin;
import com.drogueria.service.ProductService;

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
