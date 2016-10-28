package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.dto.OutputOrderResultDTO;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private InputService inputService;
	@Autowired
	private OutputService outputService;
    @Autowired
    private DeliveryNoteService deliveryNoteService;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/serializedReturns", method = RequestMethod.GET)
	public String serializedReturns(ModelMap modelMap) throws Exception {
		modelMap.put("currentDate", (new SimpleDateFormat("dd/MM/yyyy").format(new Date())).toString());

		//Supuestamente no hay devoluciones de provedores, para no modificar el jsp por las dudas solo se muestran los conceptos que sean de devolucion de clientes.
		modelMap.put("concepts", this.conceptService.getAllReturnFromClientConcepts());
		modelMap.put("destructionConcepts", this.conceptService.getDestructionConcept());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAll());
		modelMap.put("providers", this.providerService.getAll());
		modelMap.put("agreements", this.agreementService.getAllActives());

		return "serializedReturns";
	}

	@RequestMapping(value = "/searchSerialRecords", method = RequestMethod.GET)
	public @ResponseBody
	OutputOrderResultDTO searchSerialRecords(@RequestParam(required = false) Integer productId, @RequestParam String serialNumber) throws Exception {
		OutputOrderResultDTO outputOrderResultDTO = this.auditService.getOutputOrOrder(productId, serialNumber);
		return outputOrderResultDTO;
	}

	@RequestMapping(value = "/searchSerialNumberOnEgress", method = RequestMethod.GET)
	public @ResponseBody
	String searchSerialNumberOnEgress(@RequestParam String serialNumber, @RequestParam Integer outputId, @RequestParam Integer orderId) throws Exception {
		if(outputId != null){
			return this.outputService.searchSerialNumberOnOutput(outputId, serialNumber);
		}
		if(orderId != null){
			return this.orderService.searchSerialNumberOnOutput(orderId, serialNumber);
		}
		return "";
	}

	@RequestMapping(value = "/serializedReturns", method = RequestMethod.POST)
	public ModelAndView serializedReturns(@RequestParam String inputId, @RequestParam String outputId) {
        Input input = this.inputService.get(Integer.parseInt(inputId));
        Output output = null;
        Map<String, Object> map = new HashMap<>();

        if (!outputId.isEmpty()) {
            output = this.outputService.get(Integer.parseInt(outputId));
            Map<Integer, List<DeliveryNote>> outputDeliveryNotes = this.deliveryNoteService.getAssociatedOutputs();
            map.put("associatedOutputs", outputDeliveryNotes);
        }

        map.put("input", input);
        map.put("output", output);

		return new ModelAndView("serializedReturns", map);
	}
}