package com.drogueria.controllers;

import java.util.ArrayList;
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

import com.drogueria.constant.AuditState;
import com.drogueria.constant.RoleOperation;
import com.drogueria.constant.State;
import com.drogueria.dto.DeliveryNoteResultDTO;
import com.drogueria.helper.impl.OrderDeliveryNoteSheetPrinter;
import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Order;
import com.drogueria.model.ProvisioningRequest;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ClientService;
import com.drogueria.service.DeliveryNoteService;
import com.drogueria.service.OrderService;
import com.drogueria.service.ProvisioningRequestService;
import com.drogueria.service.ProvisioningRequestStateService;
import com.drogueria.util.OperationResult;

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
	public void printDeliveryNotesFromOrders(@RequestBody List<Integer> ordersToPrint) throws Exception {
		List<Integer> deliveryNotes = this.orderDeliveryNoteSheetPrinter.print(ordersToPrint);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			for (Integer deliveryNote : deliveryNotes) {
				this.auditService.addAudit(auth.getName(), RoleOperation.DELIVERY_NOTE_PRINT.getId(), AuditState.COMFIRMED, deliveryNote);
			}
		}
	}

	@RequestMapping(value = "/deliveryNoteCancellation", method = RequestMethod.GET)
	public String deliveryNoteCancellation(ModelMap modelMap) throws Exception {
		Map<Integer, List<String>> orderDeliveryNotes = this.deliveryNoteService.getAssociatedOrders(false);
		modelMap.put("orderDeliveryNotes", orderDeliveryNotes);

		Map<Integer, List<String>> outputDeliveryNotes = this.deliveryNoteService.getAssociatedOutputs(false);
		modelMap.put("outputDeliveryNotes", outputDeliveryNotes);

		return "deliveryNoteCancellation";
	}

	@RequestMapping(value = "/cancelDeliveryNotes", method = RequestMethod.POST)
	public @ResponseBody
	void cancelDeliveryNotes(@RequestBody List<String> deliveryNoteNumbers) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			this.deliveryNoteService.cancelDeliveryNotes(deliveryNoteNumbers, auth.getName());
		}
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
		Map<Integer, List<String>> orderDeliveryNotes = this.deliveryNoteService.getAssociatedOrders(true);
		modelMap.put("orderDeliveryNotes", orderDeliveryNotes);

		Map<Integer, List<String>> outputDeliveryNotes = this.deliveryNoteService.getAssociatedOutputs(true);
		modelMap.put("outputDeliveryNotes", outputDeliveryNotes);

		return "pendingTransactions";
	}

	@RequestMapping(value = "/confirmPendingDeliveryNotes", method = RequestMethod.POST)
	public @ResponseBody
	List<OperationResult> confirmPendingDeliveryNotes(@RequestBody List<String> deliveryNoteNumbers) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<OperationResult> operationResults = new ArrayList<OperationResult>();
		if (auth != null) {
			for (String deliveryNoteNumber : deliveryNoteNumbers) {
				DeliveryNote deliveryNote = this.deliveryNoteService.getDeliveryNoteFromNumber(deliveryNoteNumber);
				OperationResult operationResult = this.deliveryNoteService.saveAndInform(deliveryNote);
				this.auditService.addAudit(auth.getName(), RoleOperation.DELIVERY_NOTE_PRINT.getId(), AuditState.AUTHORITED, deliveryNote.getId());
				operationResults.add(operationResult);
			}
		}
		return operationResults;
	}

	@RequestMapping(value = "/getDeliveryNote", method = RequestMethod.GET)
	public @ResponseBody
	DeliveryNoteResultDTO getInput(@RequestParam Integer deliveryNoteId) throws Exception {
		DeliveryNote deliveryNote = this.deliveryNoteService.get(deliveryNoteId);
		DeliveryNoteResultDTO deliveryNoteResultDTO = new DeliveryNoteResultDTO();
		deliveryNoteResultDTO.setFromDeliveryNote(deliveryNote, this.deliveryNoteService.getOrder(deliveryNote),
				this.deliveryNoteService.getOutput(deliveryNote));
		return deliveryNoteResultDTO;
	}

	@RequestMapping(value = "/authorizeDeliveryNotesWithoutInform", method = RequestMethod.POST)
	public @ResponseBody
	List<Integer> authorizeDeliveryNotesWithoutInform(@RequestBody List<Integer> deliveryNoteIds, HttpServletRequest request) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			this.deliveryNoteService.authorizeWithoutInform(deliveryNoteIds, auth.getName());
		}
		return deliveryNoteIds;
	}
}
