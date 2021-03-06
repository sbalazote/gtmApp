package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.OutputDTO;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.helper.DeliveryNoteSheetPrinter;
import com.lsntsolutions.gtmApp.helper.impl.printer.FakeDeliveryNoteSheetPrinter;
import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.query.OutputQuery;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

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
	private DeliveryNoteSheetPrinter deliveryNoteSheetPrinter;
	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private FakeDeliveryNoteSheetPrinter fakeDeliveryNoteSheetPrinter;
    @Autowired
    private PropertyService propertyService;

	@RequestMapping(value = "/output", method = RequestMethod.GET)
	public String output(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());

        List<Concept> concepts = this.conceptService.getAllActives(false);
        Concept supplyingConcept = propertyService.get().getSupplyingConcept();
        if(supplyingConcept != null) {
            concepts.remove(supplyingConcept);
        }
		modelMap.put("concepts", concepts);
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("providers", this.providerService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());

		return "output";
	}

	@RequestMapping(value = "/saveOutput", method = RequestMethod.POST)
	public @ResponseBody
	PrinterResultDTO saveOutput(@RequestBody OutputDTO outputDTO, HttpServletRequest request) throws Exception {
		Output output = this.outputService.save(outputDTO);
		PrinterResultDTO printerResultDTO = new PrinterResultDTO(output.getFormatId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			if (output.getConcept().isDestruction()) {
				this.auditService.addAudit(auth.getName(), RoleOperation.PRODUCT_DESTRUCTION.getId(), output.getId());
			} else {
				this.auditService.addAudit(auth.getName(), RoleOperation.OUTPUT.getId(), output.getId());
			}
		}
		if (output.getConcept().isPrintDeliveryNote()) {
			List<Integer> outputs = new ArrayList<Integer>();
			outputs.add(output.getId());
			this.deliveryNoteSheetPrinter.print(auth.getName(), outputs, printerResultDTO,false,true,false);
		} else {
			this.fakeDeliveryNoteSheetPrinter.print(auth.getName(), output);
		}
		return printerResultDTO;
	}

	@RequestMapping(value = "/getOutput", method = RequestMethod.GET)
	public @ResponseBody
	Output getOutput(@RequestParam Integer outputId) throws Exception {
		Output output = this.outputService.get(outputId);
		return output;
	}

	@RequestMapping(value = "/getOutputsDeliveriesNoteNumbers", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getOutputsDeliveriesNoteNumbers(@RequestParam Integer outputId) {
		return this.deliveryNoteService.getOutputsDeliveriesNoteNumbers(outputId);
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

	@RequestMapping(value = "/confirmPendingOutputs", method = RequestMethod.POST)
	public @ResponseBody
	List<OperationResult> confirmPendingOutputs(@RequestBody List<Integer> outputIds) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<OperationResult> operationResults = new ArrayList<OperationResult>();
		if (auth != null) {
			for (Integer outputId : outputIds) {
				Output output = this.outputService.get(outputId);
				OperationResult operationResult = null;// this.outputService.saveAndInform(output);
				this.auditService.addAudit(auth.getName(), RoleOperation.OUTPUT.getId(), output.getId());
				operationResults.add(operationResult);
			}
		}
		return operationResults;
	}

	@RequestMapping(value = "/outputs", method = RequestMethod.POST)
	public ModelAndView outputs(HttpServletRequest request) {
		OutputQuery outputQuery = this.getOutputQuery(request);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<Integer, List<DeliveryNote>> outputDeliveryNotes = this.deliveryNoteService.getAssociatedOutputs();
		List<Output> outputs = this.outputService.getOutputForSearch(outputQuery);
		map.put("associatedOutputs", outputDeliveryNotes);
		map.put("outputs", outputs);
		return new ModelAndView("outputs", map);
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

		Integer productId = null;
		if (!(request.getParameterValues("productId")[0]).equals("null")) {
			productId = Integer.valueOf(request.getParameterValues("productId")[0]);
		}

		Boolean cancelled = null;
		if (!(request.getParameterValues("cancelled")[0]).equals("null")) {
			cancelled = Boolean.valueOf(request.getParameterValues("cancelled")[0]);
		}

		OutputQuery outputQuery = OutputQuery.createFromParameters(id, request.getParameterValues("dateFrom")[0], request.getParameterValues("dateTo")[0],
				conceptId, providerId, deliveryLocationId, agreementId, productId,cancelled );

		return outputQuery;
	}
}
