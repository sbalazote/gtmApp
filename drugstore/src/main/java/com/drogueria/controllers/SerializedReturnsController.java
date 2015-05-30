package com.drogueria.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.dto.OutputOrderResultDTO;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.InputService;
import com.drogueria.service.OrderService;
import com.drogueria.service.OutputService;
import com.drogueria.service.ProviderService;
import com.drogueria.service.StockService;

@Controller
public class SerializedReturnsController {

	@Autowired
	private AuditService auditService;
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private ProviderService providerService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private StockService stockService;
	@Autowired
	private InputService inputService;
	@Autowired
	private OutputService outputService;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/serializedReturns", method = RequestMethod.GET)
	public String serializedReturns(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());

		modelMap.put("concepts", this.conceptService.getAllReturnConcepts());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("providers", this.providerService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());

		return "serializedReturns";
	}

	@RequestMapping(value = "/searchSerialRecords", method = RequestMethod.GET)
	public @ResponseBody
	OutputOrderResultDTO searchSerialRecords(@RequestParam(required = false) Integer productId, @RequestParam String serialNumber) throws Exception {
		OutputOrderResultDTO outputOrderResultDTO = this.auditService.getOutputOrOrder(productId, serialNumber);
		return outputOrderResultDTO;
	}

}