package com.drogueria.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drogueria.dto.AuditDTO;
import com.drogueria.dto.AuditResultDTO;
import com.drogueria.dto.OutputOrderResultDTO;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.InputService;
import com.drogueria.service.OrderService;
import com.drogueria.service.OutputService;
import com.drogueria.service.ProviderService;
import com.drogueria.service.StockService;

@Controller
public class SerializedReturnsController {

	@Autowired
	private AuditService auditService;
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private ProviderService providerService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private StockService stockService;
	@Autowired
	private InputService inputService;
	@Autowired
	private OutputService outputService;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/serializedReturns", method = RequestMethod.GET)
	public String serializedReturns(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());

		modelMap.put("concepts", this.conceptService.getAllReturnConcepts());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("providers", this.providerService.getAllActives());
		modelMap.put("agreements", this.agreementService.getAllActives());

		return "serializedReturns";
	}

	@RequestMapping(value = "/searchSerialRecords", method = RequestMethod.GET)
	public @ResponseBody
	OutputOrderResultDTO searchSerialRecords(@RequestParam(required = false) Integer productId, @RequestParam String serialNumber) throws Exception {
		OutputOrderResultDTO outputOrderResultDTO = new OutputOrderResultDTO();
		AuditResultDTO auditResultDTO = this.auditService.getAudit(productId, serialNumber);
		List<AuditDTO> orders = auditResultDTO.getOrders();
		List<AuditDTO> inputs = auditResultDTO.getInputs();
		// List<AuditDTO> deliveryNotes = auditResultDTO.getDeliveryNotes();
		List<AuditDTO> outputs = auditResultDTO.getOutputs();

		// Si egresos y armados no existen, no hay devolucion.
		if (outputs.isEmpty() && orders.isEmpty()) {
			outputOrderResultDTO.setOutputId(null);
			outputOrderResultDTO.setOrderId(null);
		}
		// Si egresos existen, pero no armados verifico que sea lo ultimo y devuelvo los egresos.
		// EGRESO.
		else if (!outputs.isEmpty() && orders.isEmpty()) {
			if (inputs.isEmpty()) {
				outputOrderResultDTO.setOutputId(outputs.get(0).getOperationId());
				outputOrderResultDTO.setOrderId(null);
			} else {
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date outputDate = dateFormatter.parse(outputs.get(0).getDate());
				Date inputDate = dateFormatter.parse(inputs.get(0).getDate());
				if (outputDate.after(inputDate)) {
					outputOrderResultDTO.setOutputId(outputs.get(0).getOperationId());
					outputOrderResultDTO.setOrderId(null);
				}
			}
		}
		// Si armados existen, pero no egresos verifico que sea lo ultimo y devuelvo los armados.
		// ARMADO.
		else if (outputs.isEmpty() && !orders.isEmpty()) {
			if (inputs.isEmpty()) {
				outputOrderResultDTO.setOutputId(null);
				outputOrderResultDTO.setOrderId(orders.get(0).getOperationId());
			} else {
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date orderDate = dateFormatter.parse(orders.get(0).getDate());
				Date inputDate = dateFormatter.parse(inputs.get(0).getDate());
				if (orderDate.after(inputDate)) {
					outputOrderResultDTO.setOutputId(null);
					outputOrderResultDTO.setOrderId(orders.get(0).getOperationId());
				}
			}
		}
		// Si ambos egresos y armados existen, verifico cual es el ultimo y lo devuelvo.
		// EGRESO o ARMADO.
		else {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			if (inputs.isEmpty()) {
				Date outputDate = dateFormatter.parse(outputs.get(0).getDate());
				Date orderDate = dateFormatter.parse(orders.get(0).getDate());
				if (outputDate.after(orderDate)) {
					outputOrderResultDTO.setOutputId(outputs.get(0).getOperationId());
					outputOrderResultDTO.setOrderId(null);
				} else {
					outputOrderResultDTO.setOutputId(null);
					outputOrderResultDTO.setOrderId(orders.get(0).getOperationId());
				}
			} else {
				Date outputDate = dateFormatter.parse(outputs.get(0).getDate());
				Date orderDate = dateFormatter.parse(orders.get(0).getDate());
				Date inputDate = dateFormatter.parse(inputs.get(0).getDate());
				if (outputDate.after(inputDate) && outputDate.after(orderDate)) {
					outputOrderResultDTO.setOutputId(outputs.get(0).getOperationId());
					outputOrderResultDTO.setOrderId(null);
				}
				if (orderDate.after(inputDate) && orderDate.after(outputDate)) {
					outputOrderResultDTO.setOutputId(null);
					outputOrderResultDTO.setOrderId(orders.get(0).getOperationId());
				}
			}
		}
		return outputOrderResultDTO;
	}
}
