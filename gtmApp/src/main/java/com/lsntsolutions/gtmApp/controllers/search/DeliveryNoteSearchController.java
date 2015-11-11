package com.lsntsolutions.gtmApp.controllers.search;

import com.lsntsolutions.gtmApp.dto.DeliveryNoteFromOrderDTO;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;
import com.lsntsolutions.gtmApp.service.DeliveryNoteService;
import com.lsntsolutions.gtmApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class DeliveryNoteSearchController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private DeliveryNoteService deliveryNoteService;

	@RequestMapping(value = "/getCountDeliveryNoteSearch", method = RequestMethod.POST)
	public @ResponseBody
	boolean getCountDeliveryNoteSearch(@RequestBody DeliveryNoteQuery deliveryNoteQuery) throws Exception {
		return this.orderService.getCountDeliveryNoteSearch(deliveryNoteQuery);
	}

	@RequestMapping(value = "/getDeliveryNoteFromOrderForSearch", method = RequestMethod.POST)
	public @ResponseBody
	List<DeliveryNoteFromOrderDTO> getDeliveryNoteFromOrderForSearch(@RequestBody DeliveryNoteQuery deliveryNoteQuery) throws Exception {
		List<DeliveryNoteFromOrderDTO> deliveryNoteFromOrderDTOList = new ArrayList<DeliveryNoteFromOrderDTO>();
		Iterator<DeliveryNote> deliveryNotesFromOrderIterator = deliveryNoteService.getDeliveryNoteFromOrderForSearch(deliveryNoteQuery).iterator();
		while (deliveryNotesFromOrderIterator.hasNext()) {
			DeliveryNote currentDeliveryNote = deliveryNotesFromOrderIterator.next();
			Order currentOrder = deliveryNoteService.getOrder(currentDeliveryNote);
			deliveryNoteFromOrderDTOList.add(new DeliveryNoteFromOrderDTO(currentDeliveryNote, currentOrder));
		}
		return deliveryNoteFromOrderDTOList;
	}

	@RequestMapping(value = "/getDeliveryNoteFromOutputForSearch", method = RequestMethod.POST)
	public @ResponseBody
	List<DeliveryNote> getDeliveryNoteFromOutputForSearch(@RequestBody DeliveryNoteQuery deliveryNoteQuery) throws Exception {
		return this.deliveryNoteService.getDeliveryNoteFromOutputForSearch(deliveryNoteQuery);
	}

    @RequestMapping(value = "/getDeliveryNoteFromSupplyingForSearch", method = RequestMethod.POST)
    public @ResponseBody
    List<DeliveryNote> getDeliveryNoteFromSupplyingForSearch(@RequestBody DeliveryNoteQuery deliveryNoteQuery) throws Exception {
        return this.deliveryNoteService.getDeliveryNoteFromSupplyingForSearch(deliveryNoteQuery);
    }
}
