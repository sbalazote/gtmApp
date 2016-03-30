package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.DeliveryNoteService;
import com.lsntsolutions.gtmApp.service.PropertyService;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FakeDeliveryNoteSheetPrinter {

	private static final Logger logger = Logger.getLogger(FakeDeliveryNoteSheetPrinter.class);

	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private PropertyService propertyService;

	public void print(String userName, Egress egress) {
		Date date = new Date();

		List<Detail> outputDetails = egress.getDetails();
		Integer conceptId = getConceptId(egress);
		Concept concept = this.conceptService.getAndUpdateDeliveryNote(conceptId, 1);
		Integer deliveryNoteNumber = concept.getDeliveryNoteEnumerator().getDeliveryNoteNumber();

		// Hago el corte de remitos por la cantidad items por pagina que se indique por parametro.

		DeliveryNote deliveryNote = new DeliveryNote();
		List<DeliveryNoteDetail> deliveryNoteDetails = new ArrayList<DeliveryNoteDetail>();
		String deliveryNoteComplete = StringUtility.addLeadingZeros(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4) + "-"
				+ StringUtility.addLeadingZeros(deliveryNoteNumber, 8);
		deliveryNote.setNumber(deliveryNoteComplete);
		for (Detail detail : outputDetails) {
			DeliveryNoteDetail deliveryNoteDetail = new DeliveryNoteDetail();
			if(detail.getClass().equals(SupplyingDetail.class)){
				deliveryNoteDetail.setSupplyingDetail((SupplyingDetail) detail);
			}
			if(detail.getClass().equals(OutputDetail.class)) {
				deliveryNoteDetail.setOutputDetail((OutputDetail) detail);
			}
			deliveryNoteDetails.add(deliveryNoteDetail);

			// Guardo el Remito en la base de datos
		}
		deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
		try {
			if (egress.hasToInformANMAT() && concept.isInformAnmat() && propertyService.get().isInformAnmat()) {
				deliveryNote.setInformAnmat(true);
			} else {
				deliveryNote.setInformAnmat(false);
			}
			deliveryNote.setDate(date);
			deliveryNote.setFake(true);
			this.deliveryNoteService.save(deliveryNote);
			this.deliveryNoteService.sendTrasactionAsync(deliveryNote);
			logger.info("Se ha impreso el remito numero: " + deliveryNoteComplete);
		} catch (Exception e1) {
			logger.info("No se ha podido guardar el remito numero: " + deliveryNoteComplete);
		}
	}

	private Integer getConceptId(Egress egress){
		if(egress.getClass().equals(Output.class)){
			return ((Output)egress).getConcept().getId();
		}
		if (egress.getClass().equals(Supplying.class)){
			return this.propertyService.get().getSupplyingConcept().getId();
		}
		return null;
	}
}
