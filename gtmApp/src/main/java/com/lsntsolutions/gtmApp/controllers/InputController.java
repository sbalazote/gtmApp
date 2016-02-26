package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.InputDTO;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.Stock;
import com.lsntsolutions.gtmApp.query.InputQuery;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class InputController {

	@Autowired
	private InputService inputService;
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private ProviderService providerService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private StockService stockService;

	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());

		modelMap.put("concepts", this.conceptService.getConceptForInput());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("providers", this.providerService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());

		return "input";
	}

	@RequestMapping(value = "/saveInput", method = RequestMethod.POST)
	public @ResponseBody
	Input saveInput(@RequestBody InputDTO inputDTO, HttpServletRequest request) throws Exception {
		Input input = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			input = this.inputService.save(inputDTO, false, auth.getName());
			if (input.isInformAnmat()) {
				this.inputService.sendAsyncTransaction(input);
			}
		}
		return input;
	}

	@RequestMapping(value = "/saveRefundInput", method = RequestMethod.POST)
	public @ResponseBody
	OperationResult saveRefundInput(@RequestBody InputDTO inputDTO, HttpServletRequest request) throws Exception {
		Input input;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OperationResult result = new OperationResult();
		result.setResultado(true);
		if (auth != null) {
			input = this.inputService.save(inputDTO, true, auth.getName());
			if (input.isInformAnmat()) {
				result = this.inputService.sendTransaction(input, true);
			}else{
				result.setOperationId(String.valueOf(input.getId()));
			}
		}
		return result;
	}


	@RequestMapping(value = "/updateInput", method = RequestMethod.POST)
	public @ResponseBody
	OperationResult updateInput(@RequestBody InputDTO inputDTO, HttpServletRequest request) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OperationResult result = null;
		if (auth != null) {
			result = this.inputService.updateInput(inputDTO, auth.getName());
		}

		return result;
	}

    @RequestMapping(value = "/updateForcedInput", method = RequestMethod.POST)
    public @ResponseBody
    OperationResult updateForcedInput(@RequestBody Integer inputId, HttpServletRequest request) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Input input = this.inputService.get(inputId);
        OperationResult result = null;
        if (auth != null) {
            result = this.inputService.sendTransaction(input, false);
			this.auditService.addAudit(auth.getName(), RoleOperation.FORCED_INPUT_UPDATE.getId(), input.getId());
        }

        return result;
    }

    @RequestMapping(value = "/existsSerial", method = RequestMethod.GET)
	public @ResponseBody
	Boolean existsSerial(@RequestParam Map<String, String> parameters) {
		Integer productId = Integer.valueOf(parameters.get("productId"));
		String serialNumber = String.valueOf(parameters.get("serialNumber"));
		String gtin = String.valueOf(parameters.get("gtin"));
		return this.stockService.existsSerial(productId, gtin, null, serialNumber);
	}

	@RequestMapping(value = "/getInput", method = RequestMethod.GET)
	public @ResponseBody
	Input getInput(@RequestParam Integer inputId) throws Exception {
		return this.inputService.get(inputId);
	}

	@RequestMapping(value = "/getInputForSearch", method = RequestMethod.POST)
	public @ResponseBody
	List<Input> getInputForSearch(@RequestBody InputQuery inputQuery) throws Exception {
		return this.inputService.getInputForSearch(inputQuery);
	}

	@RequestMapping(value = "/getCountInputSearch", method = RequestMethod.POST)
	public @ResponseBody
	boolean getCountInputSearch(@RequestBody InputQuery inputQuery) throws Exception {
		return this.inputService.getCountInputSearch(inputQuery);
	}

	@RequestMapping(value = "/searchInputToUpdate", method = RequestMethod.GET)
	public String searchInputToUpdate(ModelMap modelMap) throws Exception {
		modelMap.put("inputs", this.inputService.getInputToAuthorize());
		return "searchInputToUpdate";
	}

	@RequestMapping(value = "/updateInput", method = RequestMethod.GET)
	public String updateInput(ModelMap modelMap, @RequestParam Map<String, String> parameters) throws Exception {
		Integer inputId = Integer.valueOf(parameters.get("id"));
		Input input = this.inputService.get(inputId);

		Assert.notNull(input, "Recep. de Mercadería con identificador = " + inputId + " no encontrada!");

		modelMap.put("concepts", this.conceptService.getAllActives(true));
		modelMap.put("providers", this.providerService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("date", input.getDate());
		if(input.getDeliveryNoteNumber() != null && input.getDeliveryNoteNumber() != "" && input.getDeliveryNoteNumber().length() == 12) {
			modelMap.put("deliveryNotePOSInput", input.getDeliveryNoteNumber().substring(0, 4));
			modelMap.put("deliveryNoteNumberInput", input.getDeliveryNoteNumber().substring(4, 12));
		}

		modelMap.put("purchaseOrderNumber", input.getPurchaseOrderNumber());

		modelMap.put("inputId", input.getId());
		modelMap.put("conceptId", input.getConcept().getId());
		modelMap.put("agreementId", input.getAgreement().getId());

		if (input.getDeliveryLocation() != null) {
			modelMap.put("deliveryLocationId", input.getDeliveryLocation().getId());
		}
		if (input.getProvider() != null) {
			modelMap.put("providerId", input.getProvider().getId());
			modelMap.put("logisticsOperations", input.getProvider().getLogisticsOperators());
			if(input.getLogisticsOperator() != null) {
				modelMap.put("logisticsOperationId", input.getLogisticsOperator().getId());
			}
		}

		modelMap.put("products", input.getInputDetails());

		return "input";
	}

	@RequestMapping(value = "/cancelInputWithoutStock", method = RequestMethod.GET)
	public @ResponseBody
	boolean cancelInputWithoutStock(@RequestParam Integer inputId) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Input input = this.inputService.get(inputId);
		input.setCancelled(true);
		this.inputService.save(input);
		if (auth != null) {
			this.auditService.addAudit(auth.getName(), RoleOperation.INPUT_CANCELLATION.getId(), input.getId());
		}
		return true;
	}

	@RequestMapping(value = "/inputCancellation", method = RequestMethod.GET)
	public String inputCancellation(ModelMap modelMap) throws Exception {
		modelMap.put("concepts", this.conceptService.getConceptForInput());
		modelMap.put("providers", this.providerService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		return "inputCancellation";
	}

	@RequestMapping(value = "/cancelInput", method = RequestMethod.POST)
	public @ResponseBody
	boolean cancelInputs(@RequestBody Integer inputId) throws Exception {
		boolean result = this.inputService.cancelInput(inputId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && result) {
			this.auditService.addAudit(auth.getName(), RoleOperation.INPUT_CANCELLATION.getId(), inputId);
		}
		return  result;
	}

	@RequestMapping(value = "/pendingInputs", method = RequestMethod.GET)
	public String pendingInputs(ModelMap modelMap) throws Exception {
		modelMap.put("inputs", this.inputService.getInputToAuthorize());
		return "pendingInputs";
	}

    @RequestMapping(value = "/informForcedInputs", method = RequestMethod.GET)
    public String informForcedInputs(ModelMap modelMap) throws Exception {
        modelMap.put("inputs", this.inputService.getForcedInputs());
        return "informForcedInputs";
    }

	@RequestMapping(value = "/forceInputDefinitely", method = RequestMethod.POST)
	public @ResponseBody
	void forceInputDefinitely(@RequestParam Integer inputId, String transactionCode, String selfSerializedTransactionCode) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Input input = this.inputService.get(inputId);
		input.setForcedInput(true);
        transactionCode = (transactionCode == "") ? null : transactionCode;
		selfSerializedTransactionCode = (selfSerializedTransactionCode == "") ? null : selfSerializedTransactionCode;
		input.setTransactionCodeANMAT(transactionCode);
		input.setSelfSerializedTransactionCodeANMAT(selfSerializedTransactionCode);
		this.inputService.save(input);
		if (auth != null) {
			this.auditService.addAudit(auth.getName(), RoleOperation.FORCED_INPUT.getId(), inputId);
		}
	}

	@RequestMapping(value = "/getCancelables", method = RequestMethod.POST)
	public @ResponseBody
	List<Input> getCancelables(@RequestBody InputQuery inputQuery) throws Exception {
		return this.inputService.getCancelables(inputQuery);
	}

	@RequestMapping(value = "/authorizeInputWithoutInform", method = RequestMethod.POST)
	public @ResponseBody
	Input authorizeInputWithoutInform(@RequestBody InputDTO inputDTO, HttpServletRequest request) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			return this.inputService.authorizeWithoutInform(inputDTO, auth.getName());
		}
		return null;
	}

	@RequestMapping(value = "/inputs", method = RequestMethod.POST)
	public ModelAndView inputs(HttpServletRequest request) {
		InputQuery inputQuery = this.getInputQuery(request);
		ModelAndView modelAndView = new ModelAndView("inputs", "inputs", this.inputService.getInputForSearch(inputQuery));
		modelAndView.addObject("destinationGln",this.propertyService.get().getGln());
		return modelAndView;
	}

	private InputQuery getInputQuery(HttpServletRequest request) {
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
		String deliveryNoteNumber = null;
		if (!(request.getParameterValues("deliveryNoteNumber")[0]).equals("")) {
			deliveryNoteNumber = request.getParameterValues("deliveryNoteNumber")[0];
		}
		String purchaseOrderNumber = null;
		if (!(request.getParameterValues("purchaseOrderNumber")[0]).equals("")) {
			purchaseOrderNumber = request.getParameterValues("purchaseOrderNumber")[0];
		}
		Boolean cancelled = null;
		if (!(request.getParameterValues("cancelled")[0]).equals("")) {
			cancelled = Boolean.valueOf(request.getParameterValues("cancelled")[0]);
		}
		Integer productId = null;
		if (!(request.getParameterValues("productId")[0]).equals("null")) {
			productId = Integer.valueOf(request.getParameterValues("productId")[0]);
		}
		Integer productMonodrugId = null;
		if (!(request.getParameterValues("productMonodrugId")[0]).equals("null")) {
			productId = Integer.valueOf(request.getParameterValues("productMonodrugId")[0]);
		}

		InputQuery inputQuery = InputQuery.createFromParameters(id, request.getParameterValues("dateFrom")[0], request.getParameterValues("dateTo")[0],
				conceptId, providerId, deliveryLocationId, agreementId, deliveryNoteNumber, purchaseOrderNumber, cancelled, productId, productMonodrugId);

		return inputQuery;
	}
}
