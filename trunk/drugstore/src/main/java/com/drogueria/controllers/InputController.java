package com.drogueria.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.drogueria.dto.InputDTO;
import com.drogueria.model.Input;
import com.drogueria.query.InputQuery;
import com.drogueria.service.AgreementService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.InputService;
import com.drogueria.service.ProviderService;
import com.drogueria.util.OperationResult;

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

	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());

		modelMap.put("concepts", this.conceptService.getAllActives(true));
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("providers", this.providerService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());

		return "input";
	}

	@RequestMapping(value = "/saveInput", method = RequestMethod.POST)
	public @ResponseBody
	Input saveInput(@RequestBody InputDTO inputDTO, @RequestParam Boolean isSerializedReturn, HttpServletRequest request) throws Exception {
		Input input = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			input = this.inputService.save(inputDTO, isSerializedReturn, auth.getName());
			if (input.isInformAnmat()) {
				this.inputService.sendAsyncTransaction(input);
			}
		}
		return input;
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

	@RequestMapping(value = "/existsSerial", method = RequestMethod.GET)
	public @ResponseBody
	Boolean existsSerial(@RequestParam Map<String, String> parameters) {
		Integer productId = Integer.valueOf(parameters.get("productId"));
		String serialNumber = String.valueOf(parameters.get("serialNumber"));
		String gtin = String.valueOf(parameters.get("gtin"));
		boolean toReturn = this.inputService.existsSerial(serialNumber, productId, gtin);
		return toReturn;
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

		modelMap.put("concepts", this.conceptService.getAllActives(true));
		modelMap.put("providers", this.providerService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("date", input.getDate());
		modelMap.put("deliveryNotePOSInput", input.getDeliveryNoteNumber().substring(0, 4));
		modelMap.put("deliveryNoteNumberInput", input.getDeliveryNoteNumber().substring(4, 12));

		modelMap.put("purchaseOrderNumber", input.getPurchaseOrderNumber());

		modelMap.put("inputId", input.getId());
		modelMap.put("conceptId", input.getConcept().getId());
		modelMap.put("agreementId", input.getAgreement().getId());

		if (input.getDeliveryLocation() != null) {
			modelMap.put("deliveryLocationId", input.getDeliveryLocation().getId());
		}
		if (input.getProvider() != null) {
			modelMap.put("providerId", input.getProvider().getId());
		}

		modelMap.put("products", input.getInputDetails());

		return "input";
	}

	@RequestMapping(value = "/cancelInputWithoutStock", method = RequestMethod.GET)
	public @ResponseBody
	boolean cancelInputWithoutStock(@RequestParam Integer inputId) throws Exception {
		Input input = this.inputService.get(inputId);
		input.setCancelled(true);
		this.inputService.save(input);
		return true;
	}

	@RequestMapping(value = "/inputCancellation", method = RequestMethod.GET)
	public String inputCancellation(ModelMap modelMap) throws Exception {
		modelMap.put("concepts", this.conceptService.getAll());
		modelMap.put("providers", this.providerService.getAll());
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAll());
		return "inputCancellation";
	}

	@RequestMapping(value = "/cancelInputs", method = RequestMethod.POST)
	public @ResponseBody
	void cancelInputs(@RequestBody List<Integer> inputIds) throws Exception {
		this.inputService.cancelInputs(inputIds);
	}

	@RequestMapping(value = "/pendingInputs", method = RequestMethod.GET)
	public String pendingInputs(ModelMap modelMap) throws Exception {
		modelMap.put("inputs", this.inputService.getInputToAuthorize());
		return "pendingInputs";
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
		return new ModelAndView("inputs", "inputs", this.inputService.getInputForSearch(inputQuery));
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
		if ((request.getParameterValues("deliveryNoteNumber")) != null) {
			deliveryNoteNumber = request.getParameterValues("deliveryNoteNumber")[0];
		}
		String purchaseOrderNumber = null;
		if (request.getParameterValues("purchaseOrderNumber") != null) {
			purchaseOrderNumber = request.getParameterValues("purchaseOrderNumber")[0];
		}

		InputQuery inputQuery = InputQuery.createFromParameters(id, request.getParameterValues("dateFrom")[0], request.getParameterValues("dateTo")[0],
				conceptId, providerId, deliveryLocationId, agreementId, deliveryNoteNumber, purchaseOrderNumber, null);

		return inputQuery;
	}
}
