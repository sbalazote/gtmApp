package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.DeliveryNoteResultDTO;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.helper.impl.printer.OrderDeliveryNoteSheetPrinter;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.OperationResult;
import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class DeliveryNoteController {

	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private ProvisioningRequestService provisioningRequestService;
	@Autowired
	private ProvisioningRequestStateService provisioningRequestStateService;
	@Autowired
	private OrderDeliveryNoteSheetPrinter orderDeliveryNoteSheetPrinter;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private ClientService clientService;

	@RequestMapping(value = "/deliveryNoteSheet", method = RequestMethod.GET)
	public String deliveryNoteSheet(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("clients", this.clientService.getAllActives());

		return "deliveryNoteSheet";
	}

	@RequestMapping(value = "/printDeliveryNotes", method = RequestMethod.POST)
	@ResponseBody
	public PrinterResultDTO printDeliveryNotesFromOrders(@RequestBody List<Integer> ordersToPrint) throws Exception {
		PrinterResultDTO printerResultDTO = new PrinterResultDTO();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		this.orderDeliveryNoteSheetPrinter.print(auth.getName(), ordersToPrint, printerResultDTO);
		return printerResultDTO;
	}

	@RequestMapping(value = "/deliveryNoteCancellation", method = RequestMethod.GET)
	public String deliveryNoteCancellation() throws Exception {
		return "deliveryNoteCancellation";
	}

	@RequestMapping(value = "/cancelDeliveryNotes", method = RequestMethod.POST, consumes = { "application/json" })
	public @ResponseBody
	void cancelDeliveryNotes(@RequestBody HashMap<String,List<String>> deliveryNotesToCancel) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			this.deliveryNoteService.cancelDeliveryNotes(deliveryNotesToCancel, auth.getName());
		}
	}

	@RequestMapping(value = "/getCancelableDeliveryNotes", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, List<String>> getCancelableDeliveryNotes(@RequestParam String deliveryNoteNumber) throws Exception {
		Map<String, List<String>> canceleablesMap = new HashMap<>();
		Map<String, List<String>> orderDeliveryNotes = this.deliveryNoteService.getAssociatedOrders(false, deliveryNoteNumber);
		Map<String, List<String>> outputDeliveryNotes = this.deliveryNoteService.getAssociatedOutputs(false, deliveryNoteNumber);
		Map<String, List<String>> supplyingDeliveryNotes = this.deliveryNoteService.getAssociatedSupplyings(false, deliveryNoteNumber);

		canceleablesMap.putAll(orderDeliveryNotes);
		canceleablesMap.putAll(outputDeliveryNotes);
		canceleablesMap.putAll(supplyingDeliveryNotes);

		return canceleablesMap;
	}

	@RequestMapping(value = "/reassemblyOrders", method = RequestMethod.POST)
	public @ResponseBody
	void reassemblyOrders(@RequestBody List<Integer> orderIds) throws Exception {
		for (Integer orderId : orderIds) {
			Order order = this.orderService.get(orderId);
			ProvisioningRequest provisioningRequest = order.getProvisioningRequest();
			provisioningRequest.setState(this.provisioningRequestStateService.get(State.ASSEMBLED.getId()));
			this.provisioningRequestService.save(provisioningRequest);
		}

	}

	@RequestMapping(value = "/pendingTransactions", method = RequestMethod.GET)
	public String pendingTransactions(ModelMap modelMap) throws Exception {
		Map<String, List<String>> orderDeliveryNotes = this.deliveryNoteService.getAssociatedOrders(true, "");
		modelMap.put("orderDeliveryNotes", orderDeliveryNotes);

		Map<String, List<String>> outputDeliveryNotes = this.deliveryNoteService.getAssociatedOutputs(true, "");
		modelMap.put("outputDeliveryNotes", outputDeliveryNotes);

		Map<String, List<String>> supplyingDeliveryNotes = this.deliveryNoteService.getAssociatedSupplyings(true, "");
		modelMap.put("supplyingDeliveryNotes", supplyingDeliveryNotes);

		return "pendingTransactions";
	}

	@RequestMapping(value = "/confirmPendingDeliveryNotes", method = RequestMethod.POST, consumes = { "application/json" })
	public @ResponseBody
	List<OperationResult> confirmPendingDeliveryNotes(@RequestBody HashMap<String, List<String>> deliveryNoteNumbers) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<OperationResult> operationResults = new ArrayList<OperationResult>();
		if (auth != null) {
			for (String type : deliveryNoteNumbers.keySet()) {
				for(String deliveryNoteNumber : deliveryNoteNumbers.get(type)) {
					DeliveryNote deliveryNote = this.deliveryNoteService.getDeliveryNoteFromNumber(deliveryNoteNumber, type);
					OperationResult operationResult = this.deliveryNoteService.saveAndInform(deliveryNote);
					this.auditService.addAudit(auth.getName(), RoleOperation.DELIVERY_NOTE_PRINT.getId(), AuditState.AUTHORITED, deliveryNote.getId());
					operationResults.add(operationResult);
				}
			}
		}
		return operationResults;
	}

	@RequestMapping(value = "/getDeliveryNote", method = RequestMethod.GET)
	public @ResponseBody
	DeliveryNoteResultDTO getDeliveryNote(@RequestParam String deliveryNoteNumber) throws Exception {
		DeliveryNote deliveryNote = this.deliveryNoteService.getDeliveryNoteFromNumber(deliveryNoteNumber);
		DeliveryNoteResultDTO deliveryNoteResultDTO = new DeliveryNoteResultDTO();
		deliveryNoteResultDTO.setFromDeliveryNote(deliveryNote, this.deliveryNoteService.getOrder(deliveryNote),
				this.deliveryNoteService.getOutput(deliveryNote),this.deliveryNoteService.getSupplying(deliveryNote));
		return deliveryNoteResultDTO;
	}


    @RequestMapping(value = "/getDeliveryNoteById", method = RequestMethod.GET)
    public @ResponseBody
    DeliveryNoteResultDTO getDeliveryNoteById(@RequestParam Integer deliveryNoteId) throws Exception {
        DeliveryNote deliveryNote = this.deliveryNoteService.get(deliveryNoteId);
        DeliveryNoteResultDTO deliveryNoteResultDTO = new DeliveryNoteResultDTO();
        deliveryNoteResultDTO.setFromDeliveryNote(deliveryNote, this.deliveryNoteService.getOrder(deliveryNote),
                this.deliveryNoteService.getOutput(deliveryNote),this.deliveryNoteService.getSupplying(deliveryNote));
        return deliveryNoteResultDTO;
    }

	@RequestMapping(value = "/authorizeDeliveryNotesWithoutInform", method = RequestMethod.POST)
	public @ResponseBody
	List<String> authorizeDeliveryNotesWithoutInform(@RequestBody Map<String,String> deliveryNoteIds) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			this.deliveryNoteService.authorizeWithoutInform(deliveryNoteIds, auth.getName());
		}

		return null;
	}
}
