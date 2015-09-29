package com.lsntsolutions.gtmApp.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lsntsolutions.gtmApp.constant.DocumentType;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.DocumentType;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.SupplyingDTO;
import com.lsntsolutions.gtmApp.helper.impl.printer.SupplyingFakeDeliveryNoteSheetPrinter;
import com.lsntsolutions.gtmApp.model.Supplying;
import com.lsntsolutions.gtmApp.query.SupplyingQuery;
import com.lsntsolutions.gtmApp.constant.DocumentType;
import com.lsntsolutions.gtmApp.service.*;
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
import org.springframework.web.servlet.ModelAndView;

import com.lsntsolutions.gtmApp.service.AgreementService;
import com.lsntsolutions.gtmApp.service.SupplyingService;

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
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private SupplyingFakeDeliveryNoteSheetPrinter supplyingFakeDeliveryNoteSheetPrinter;

	@RequestMapping(value = "/supplying", method = RequestMethod.GET)
	public String supplying(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());
		modelMap.put("clients", this.clientService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("documentTypes", DocumentType.types);
		return "supplying";
	}

	@RequestMapping(value = "/supplyingCancellation", method = RequestMethod.GET)
	public String supplyingCancellation(ModelMap modelMap) throws Exception {
		modelMap.put("cancelleables", this.supplyingService.getCancelleables());
		return "supplyingCancellation";
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

	@RequestMapping(value = "/getSupplyingForSearch", method = RequestMethod.POST)
	public @ResponseBody
	List<Supplying> getSupplyingForSearch(@RequestBody SupplyingQuery supplyingQuery) throws Exception {
		return this.supplyingService.getSupplyingForSearch(supplyingQuery);
	}

	@RequestMapping(value = "/getCountSupplyingSearch", method = RequestMethod.POST)
	public @ResponseBody
	boolean getCountSupplyingSearch(@RequestBody SupplyingQuery supplyingQuery) throws Exception {
		return this.supplyingService.getCountSupplyingSearch(supplyingQuery);
	}

	@RequestMapping(value = "/getSupplyingsDeliveriesNoteNumbers", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getSupplyingsDeliveriesNoteNumbers(@RequestParam Integer supplyingId) {
		return this.deliveryNoteService.getSupplyingsDeliveriesNoteNumbers(supplyingId);
	}

	@RequestMapping(value = "/cancelSupplyings", method = RequestMethod.POST)
	public @ResponseBody
	void cancelSupplyings(@RequestBody List<Integer> supplyingIds) throws Exception {
		// this.supplyingService.cancelSupplyings(supplyingIds);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			for (Integer supplyingId : supplyingIds) {
				this.auditService.addAudit(auth.getName(), RoleOperation.SUPPLYING_CANCELLATION.getId(), AuditState.CANCELLED, supplyingId);
			}
		}
	}

	@RequestMapping(value = "/supplyings", method = RequestMethod.POST)
	public ModelAndView supplyings(HttpServletRequest request) {
		SupplyingQuery supplyingQuery = this.getSupplyingQuery(request);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<Integer, List<DeliveryNote>> supplyingDeliveryNotes = this.deliveryNoteService.getAssociatedSupplyings();
		List<Supplying> supplyings = this.supplyingService.getSupplyingForSearch(supplyingQuery);
		map.put("associatedSupplyings", supplyingDeliveryNotes);
		map.put("supplyings", supplyings);
		return new ModelAndView("supplyings", map);

	}

	private SupplyingQuery getSupplyingQuery(HttpServletRequest request) {
		Integer id = null;
		if (!(request.getParameterValues("id")[0]).equals("null")) {
			id = Integer.valueOf(request.getParameterValues("id")[0]);
		}
		Integer affiliateId = null;
		if (!(request.getParameterValues("affiliateId")[0]).equals("null")) {
			affiliateId = Integer.valueOf(request.getParameterValues("affiliateId")[0]);
		}
		Integer agreementId = null;
		if (!(request.getParameterValues("agreementId")[0]).equals("null")) {
			agreementId = Integer.valueOf(request.getParameterValues("agreementId")[0]);
		}

		Integer productId = null;
		if (!(request.getParameterValues("productId")[0]).equals("null")) {
			productId = Integer.valueOf(request.getParameterValues("productId")[0]);
		}

		Integer clientId = null;
		if (!(request.getParameterValues("clientId")[0]).equals("null")) {
			clientId = Integer.valueOf(request.getParameterValues("clientId")[0]);
		}

        Boolean cancelled = null;
        if (!(request.getParameterValues("cancelled")[0]).equals("null")) {
            cancelled = Boolean.valueOf(request.getParameterValues("cancelled")[0]);
        }

		SupplyingQuery outputQuery = SupplyingQuery.createFromParameters(id, request.getParameterValues("dateFrom")[0],
				request.getParameterValues("dateTo")[0],affiliateId, agreementId,cancelled,productId,clientId);

		return outputQuery;
	}
}
