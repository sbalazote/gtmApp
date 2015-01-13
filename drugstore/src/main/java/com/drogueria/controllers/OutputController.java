package com.drogueria.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;

import com.drogueria.constant.AuditState;
import com.drogueria.constant.RoleOperation;
import com.drogueria.dto.OutputDTO;
import com.drogueria.helper.impl.OutputDeliveryNoteSheetPrinter;
import com.drogueria.helper.impl.OutputFakeDeliveryNoteSheetPrinter;
import com.drogueria.model.Output;
import com.drogueria.query.OutputQuery;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.OutputService;
import com.drogueria.service.ProviderService;
import com.drogueria.util.OperationResult;

@Controller
public class OutputController {

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

	@RequestMapping(value = "/output", method = RequestMethod.GET)
	public String output(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());

		modelMap.put("concepts", this.conceptService.getAllActives(false));
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("providers", this.providerService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());

		return "output";
	}

	@RequestMapping(value = "/outputCancellation", method = RequestMethod.GET)
	public String outputCancellation(ModelMap modelMap) throws Exception {
		modelMap.put("cancelleables", this.outputService.getCancelleables());
		return "outputCancellation";
	}

	@RequestMapping(value = "/saveOutput", method = RequestMethod.POST)
	public @ResponseBody
	Output saveOutput(@RequestBody OutputDTO outputDTO, HttpServletRequest request) throws Exception {
		Output output = this.outputService.save(outputDTO);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			this.auditService.addAudit(auth.getName(), RoleOperation.OUTPUT.getId(), AuditState.COMFIRMED, output.getId());
		}
		if (output.getConcept().isPrintDeliveryNote()) {
			List<Integer> outputs = new ArrayList<Integer>();
			outputs.add(output.getId());
			List<Integer> deliveryNotes = this.outputDeliveryNoteSheetPrinter.print(outputs);
			for (Integer deliveryNote : deliveryNotes) {
				this.auditService.addAudit(auth.getName(), RoleOperation.DELIVERY_NOTE_PRINT.getId(), AuditState.COMFIRMED, deliveryNote);
			}
		} else {
			Integer deliveryNote = this.outputFakeDeliveryNoteSheetPrinter.print(output);
			this.auditService.addAudit(auth.getName(), RoleOperation.DELIVERY_NOTE_PRINT.getId(), AuditState.COMFIRMED, deliveryNote);
		}
		return output;
	}

	@RequestMapping(value = "/getOutput", method = RequestMethod.GET)
	public @ResponseBody
	Output getOutput(@RequestParam Integer outputId) throws Exception {
		Output output = this.outputService.get(outputId);
		return output;
	}

	@RequestMapping(value = "/getOutputForSearch", method = RequestMethod.POST)
	public @ResponseBody
	List<Output> getOutputForSearch(@RequestBody OutputQuery outputQuery) throws Exception {
		return this.outputService.getOutputForSearch(outputQuery);
	}

	@RequestMapping(value = "/getCountOutputSearch", method = RequestMethod.POST)
	public @ResponseBody
	boolean getCountOutputSearch(@RequestBody OutputQuery outputQuery) throws Exception {
		return this.outputService.getCountOutputSearch(outputQuery);
	}

	@RequestMapping(value = "/cancelOutputs", method = RequestMethod.POST)
	public @ResponseBody
	void cancelOutputs(@RequestBody List<Integer> outputIds) throws Exception {
		// this.outputService.cancelOutputs(outputIds);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			for (Integer outputId : outputIds) {
				this.auditService.addAudit(auth.getName(), RoleOperation.OUTPUT_CANCELLATION.getId(), AuditState.CANCELLED, outputId);
			}
		}
	}

	@RequestMapping(value = "/confirmPendingOutputs", method = RequestMethod.POST)
	public @ResponseBody
	List<OperationResult> confirmPendingOutputs(@RequestBody List<Integer> outputIds) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<OperationResult> operationResults = new ArrayList<OperationResult>();
		if (auth != null) {
			for (Integer outputId : outputIds) {
				Output output = this.outputService.get(outputId);
				OperationResult operationResult = null;// this.outputService.saveAndInform(output);
				this.auditService.addAudit(auth.getName(), RoleOperation.OUTPUT.getId(), AuditState.COMFIRMED, output.getId());
				operationResults.add(operationResult);
			}
		}
		return operationResults;
	}

	// @RequestMapping(value = "/authorizeOutputWithoutInform", method = RequestMethod.POST)
	// public @ResponseBody
	// List<Integer> authorizeOutputWithoutInform(@RequestBody List<Integer> outputIds, HttpServletRequest request) throws Exception {
	// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// if (auth != null) {
	// this.outputService.authorizeWithoutInform(outputIds, auth.getName());
	// }
	// return outputIds;
	// }

	@RequestMapping(value = "/outputs", method = RequestMethod.POST)
	public ModelAndView inputs(HttpServletRequest request) {
		OutputQuery outputQuery = this.getOutputQuery(request);
		return new ModelAndView("outputs", "outputs", this.outputService.getOutputForSearch(outputQuery));
	}

	private OutputQuery getOutputQuery(HttpServletRequest request) {
		Integer id = null;
		if (!(request.getParameterValues("id")[0]).equals("null")) {
			id = Integer.valueOf(request.getParameterValues("id")[0]);
		}
		Integer conceptId = null;
		if (!(request.getParameterValues("conceptId")[0]).equals("null")) {
			conceptId = Integer.valueOf(request.getParameterValues("conceptId")[0]);
		}
		Integer providerId = null;
		if (!(request.getParameterValues("providerId")[0]).equals("null")) {
			providerId = Integer.valueOf(request.getParameterValues("providerId")[0]);
		}
		Integer deliveryLocationId = null;
		if (!(request.getParameterValues("deliveryLocationId")[0]).equals("null")) {
			deliveryLocationId = Integer.valueOf(request.getParameterValues("deliveryLocationId")[0]);
		}
		Integer agreementId = null;
		if (!(request.getParameterValues("agreementId")[0]).equals("null")) {
			agreementId = Integer.valueOf(request.getParameterValues("agreementId")[0]);
		}

		OutputQuery outputQuery = OutputQuery.createFromParameters(id, request.getParameterValues("dateFrom")[0], request.getParameterValues("dateTo")[0],
				conceptId, providerId, deliveryLocationId, agreementId);

		return outputQuery;
	}
}
