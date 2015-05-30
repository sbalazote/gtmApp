package com.drogueria.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.constant.AuditState;
import com.drogueria.constant.RoleOperation;
import com.drogueria.dto.SupplyingDTO;
import com.drogueria.helper.impl.SupplyingFakeDeliveryNoteSheetPrinter;
import com.drogueria.model.Supplying;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ClientService;
import com.drogueria.service.SupplyingService;

@Controller
public class SupplyingController {

	@Autowired
	private SupplyingService supplyingService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private SupplyingFakeDeliveryNoteSheetPrinter supplyingFakeDeliveryNoteSheetPrinter;

	@RequestMapping(value = "/supplying", method = RequestMethod.GET)
	public String supplying(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());
		modelMap.put("clients", this.clientService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());

		return "supplying";
	}

	@RequestMapping(value = "/saveSupplying", method = RequestMethod.POST)
	public @ResponseBody
	Supplying saveSupplying(@RequestBody SupplyingDTO supplyingDTO, HttpServletRequest request) throws Exception {
		Supplying supplying = this.supplyingService.save(supplyingDTO);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			this.auditService.addAudit(auth.getName(), RoleOperation.SUPPLYING.getId(), AuditState.COMFIRMED, supplying.getId());
		}
		Integer deliveryNote = this.supplyingFakeDeliveryNoteSheetPrinter.print(supplying);
		this.auditService.addAudit(auth.getName(), RoleOperation.DELIVERY_NOTE_PRINT.getId(), AuditState.COMFIRMED, deliveryNote);

		return supplying;
	}

	@RequestMapping(value = "/getSupplying", method = RequestMethod.GET)
	public @ResponseBody
	Supplying getSupplying(@RequestParam Integer supplyingId) {
		return this.supplyingService.get(supplyingId);
	}
}
