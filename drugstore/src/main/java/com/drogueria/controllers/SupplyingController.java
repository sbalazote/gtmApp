package com.drogueria.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.drogueria.helper.impl.OutputDeliveryNoteSheetPrinter;
import com.drogueria.helper.impl.OutputFakeDeliveryNoteSheetPrinter;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ClientService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.DeliveryNoteService;
import com.drogueria.service.OutputService;
import com.drogueria.service.ProviderService;

@Controller
public class SupplyingController {

	@Autowired
	private ConceptService conceptService;
	@Autowired
	private ProviderService providerService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private OutputService outputService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private OutputDeliveryNoteSheetPrinter outputDeliveryNoteSheetPrinter;
	@Autowired
	private OutputFakeDeliveryNoteSheetPrinter outputFakeDeliveryNoteSheetPrinter;
	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private ClientService clientService;

	@RequestMapping(value = "/supplying", method = RequestMethod.GET)
	public String supplying(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());
		modelMap.put("clients", this.clientService.getAllActives());

		return "supplying";
	}
}
