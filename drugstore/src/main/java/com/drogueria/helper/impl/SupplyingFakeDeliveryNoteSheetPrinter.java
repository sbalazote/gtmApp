package com.drogueria.helper.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.drogueria.util.StringUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drogueria.model.Concept;
import com.drogueria.model.DeliveryNote;
import com.drogueria.model.DeliveryNoteDetail;
import com.drogueria.model.Supplying;
import com.drogueria.model.SupplyingDetail;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryNoteService;
import com.drogueria.service.PropertyService;

@Service
public class SupplyingFakeDeliveryNoteSheetPrinter {

	private static final Logger logger = Logger.getLogger(OutputDeliveryNoteSheetPrinter.class);

	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private PropertyService PropertyService;

	public Integer print(Supplying supplying) {
		Date date = new Date();

		List<SupplyingDetail> supplyingDetails = supplying.getSupplyingDetails();
		Integer conceptId = supplying.getAgreement().getDeliveryNoteConcept().getId();
		Concept concept = this.conceptService.getAndUpdateDeliveryNote(conceptId, 1);
		Integer deliveryNoteNumber = concept.getLastDeliveryNoteNumber() + 1;

		// Hago el corte de remitos por la cantidad items por pagina que se indique por parametro.

		DeliveryNote deliveryNote = new DeliveryNote();
		List<DeliveryNoteDetail> deliveryNoteDetails = new ArrayList<DeliveryNoteDetail>();
        String deliveryNoteComplete = concept.getDeliveryNotePOS() + "-" + StringUtility.addLeadingZeros(deliveryNoteNumber, 8);
        deliveryNote.setNumber(deliveryNoteComplete);
        for (SupplyingDetail supplyingDetail : supplyingDetails) {

			DeliveryNoteDetail deliveryNoteDetail = new DeliveryNoteDetail();
			deliveryNoteDetail.setSupplyingDetail(supplyingDetail);
			deliveryNoteDetails.add(deliveryNoteDetail);
		}
		deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
		try {
			if (supplying.hasProductThatInform() && this.PropertyService.get().getSupplyingConcept().isInformAnmat()) {
				deliveryNote.setInformAnmat(true);
			} else {
				deliveryNote.setInformAnmat(false);
			}
			deliveryNote.setDate(date);
			deliveryNote.setFake(true);
			this.deliveryNoteService.save(deliveryNote);
			this.deliveryNoteService.sendTrasactionAsync(deliveryNote);
			logger.info("Se ha impreso el remito numero: " + deliveryNoteNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deliveryNoteNumber;
	}
}