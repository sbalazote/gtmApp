package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.dto.DeliveryNoteResultDTO;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.helper.DeliveryNoteSheetPrinter;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.CancelDeliveryNoteResult;
import com.lsntsolutions.gtmApp.util.OperationResult;
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
	private DeliveryNoteSheetPrinter deliveryNoteSheetPrinter;

	@RequestMapping(value = "/deliveryNoteSheet", method = RequestMethod.GET)
	public String deliveryNoteSheet(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.orderService.getAgreementForOrderToPrint());
		modelMap.put("deliveryLocations", this.orderService.getDeliveryLocationsForOrderToPrint());
		modelMap.put("logisticsOperators", this.orderService.getLogisticsOperatorsForOrderToPrint());
		modelMap.put("clients", this.orderService.getClientForOrderToPrint());

		return "deliveryNoteSheet";
	}

	@RequestMapping(value = "/printDeliveryNotes", method = RequestMethod.POST)
	@ResponseBody
	public synchronized PrinterResultDTO printDeliveryNotesFromOrders(@RequestBody List<Integer> ordersToPrint) throws Exception {
		Set<Integer> hs = new HashSet<>();
		hs.addAll(ordersToPrint);
		ordersToPrint.clear();
		ordersToPrint.addAll(hs);
		PrinterResultDTO printerResultDTO = new PrinterResultDTO();
		ordersToPrint = this.orderService.filterAlreadyPrinted(ordersToPrint, printerResultDTO);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		this.deliveryNoteSheetPrinter.print(auth.getName(), ordersToPrint, printerResultDTO,false,false,true);
		return printerResultDTO;
	}

	@RequestMapping(value = "/deliveryNoteCancellation", method = RequestMethod.GET)
	public String deliveryNoteCancellation() throws Exception {
		return "deliveryNoteCancellation";
	}

	@RequestMapping(value = "/cancelDeliveryNotes", method = RequestMethod.POST)
	public @ResponseBody
	List<CancelDeliveryNoteResult> cancelDeliveryNotes(@RequestBody List<String> deliveryNoteNumbers) throws Exception {
		Set<String> hs = new HashSet<>();
		hs.addAll(deliveryNoteNumbers);
		deliveryNoteNumbers.clear();
		deliveryNoteNumbers.addAll(hs);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			return this.deliveryNoteService.cancelDeliveryNotes(deliveryNoteNumbers, auth.getName());
		}
		return new ArrayList<>();
	}

	@RequestMapping(value = "/getCancelableDeliveryNotes", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, List<DeliveryNote>> getCancelableDeliveryNotes(@RequestParam String deliveryNoteNumber) throws Exception {
		return this.deliveryNoteService.getDeliveryNotes(deliveryNoteNumber);
	}

	@RequestMapping(value = "/pendingTransactions", method = RequestMethod.GET)
	public String pendingTransactions(ModelMap modelMap) throws Exception {
		Map<String, List<DeliveryNote>> orderDeliveryNotes = this.deliveryNoteService.getAssociatedOrders(true, "");
		modelMap.put("orderDeliveryNotes", orderDeliveryNotes);

		Map<String, List<DeliveryNote>> outputDeliveryNotes = this.deliveryNoteService.getAssociatedOutputs(true, "");
		modelMap.put("outputDeliveryNotes", outputDeliveryNotes);

		Map<String, List<DeliveryNote>> supplyingDeliveryNotes = this.deliveryNoteService.getAssociatedSupplyings(true, "");
		modelMap.put("supplyingDeliveryNotes", supplyingDeliveryNotes);

		return "pendingTransactions";
	}

	@RequestMapping(value = "/confirmPendingDeliveryNotes", method = RequestMethod.POST)
	public @ResponseBody
	List<OperationResult> confirmPendingDeliveryNotes(@RequestBody List<String> deliveryNoteNumbers) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<OperationResult> operationResults = new ArrayList<>();
		if (auth != null) {
			for (String deliveryNoteNumber : deliveryNoteNumbers) {
				DeliveryNote deliveryNote = this.deliveryNoteService.getDeliveryNoteFromNumber(deliveryNoteNumber);
				OperationResult operationResult = this.deliveryNoteService.saveAndInform(deliveryNote);
				this.auditService.addAudit(auth.getName(), RoleOperation.PENDING_TRANSACTIONS.getId(), deliveryNote.getId());
				operationResults.add(operationResult);
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

	@RequestMapping(value = "/cancellableDeliveryNoteCount", method = RequestMethod.POST)
	public @ResponseBody
	Integer cancellableDeliveryNoteCount(@RequestBody List<String> deliveryNoteNumbers) {
		Integer numberOfCancellableDeliveryNotes = 0;
		for (String deliveryNoteNumber : deliveryNoteNumbers) {
			if (!this.deliveryNoteService.isCancelled(deliveryNoteNumber)) {
				numberOfCancellableDeliveryNotes++;
			}
		}

		return numberOfCancellableDeliveryNotes;
	}
}