package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import com.lsntsolutions.gtmApp.service.DeliveryLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lsntsolutions.gtmApp.model.DeliveryLocation;

@Controller
public class DeliveryLocationSearchController {

	@Autowired
	private DeliveryLocationService deliveryLocationService;

	@RequestMapping(value = "/getDeliveryLocations", method = RequestMethod.GET)
	public @ResponseBody
	List<DeliveryLocation> getDeliveryLocations() {
		return this.deliveryLocationService.getAll();
	}
}
