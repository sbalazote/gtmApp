package com.lsntsolutions.gtmApp.helper.impl.printer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.DeliveryNoteDetail;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.PropertyService;
import com.lsntsolutions.gtmApp.util.StringUtility;
import com.lsntsolutions.gtmApp.model.SupplyingDetail;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.Supplying;
import com.lsntsolutions.gtmApp.service.DeliveryNoteService;

@Service
public class SupplyingFakeDeliveryNoteSheetPrinter {

	private static final Logger logger = Logger.getLogger(OutputDeliveryNoteSheetPrinter.class);

	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private PropertyService propertyService;

	public Integer print(Supplying supplying) {
		Date date = new Date();

		List<SupplyingDetail> supplyingDetails = supplying.getSupplyingDetails();
		Integer conceptId = this.propertyService.get().getSupplyingConcept().getId();
		Concept concept = this.conceptService.getAndUpdateDeliveryNote(conceptId, 1);
		Integer deliveryNoteNumber = concept.getDeliveryNoteEnumerator().getLastDeliveryNoteNumber() + 1;

		// Hago el corte de remitos por la cantidad items por pagina que se indique por parametro.

		DeliveryNote deliveryNote = new DeliveryNote();
		List<DeliveryNoteDetail> deliveryNoteDetails = new ArrayList<DeliveryNoteDetail>();
		String deliveryNoteComplete = StringUtility.addLeadingZeros(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4) + "-"
				+ StringUtility.addLeadingZeros(deliveryNoteNumber, 8);
		deliveryNote.setNumber(deliveryNoteComplete);
		for (SupplyingDetail supplyingDetail : supplyingDetails) {

			DeliveryNoteDetail deliveryNoteDetail = new DeliveryNoteDetail();
			deliveryNoteDetail.setSupplyingDetail(supplyingDetail);
			deliveryNoteDetails.add(deliveryNoteDetail);
		}
		deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
		try {
			if (supplying.hasProductThatInform() && this.propertyService.get().getSupplyingConcept().isInformAnmat()) {
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
		return deliveryNote.getId();
	}
}