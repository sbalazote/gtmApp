package com.lsntsolutions.gtmApp.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lsntsolutions.gtmApp.dto.OutputOrderResultDTO;
import com.lsntsolutions.gtmApp.service.AgreementService;
import com.lsntsolutions.gtmApp.service.AuditService;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.DeliveryLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lsntsolutions.gtmApp.service.ProviderService;

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

	@RequestMapping(value = "/serializedReturns", method = RequestMethod.GET)
	public String serializedReturns(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());

		//Supuestamente no hay devoluciones de provedores, para no modificar el jsp por las dudas solo se muestran los conceptos que sean de devolucion de clientes.
		modelMap.put("concepts", this.conceptService.getAllReturnFromClientConcepts());
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