package com.lsntsolutions.gtmApp.helper.impl.printer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.DeliveryNoteDetail;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.DeliveryNoteService;
import com.lsntsolutions.gtmApp.service.PropertyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.model.OutputDetail;
import com.lsntsolutions.gtmApp.util.StringUtility;

@Service
public class OutputFakeDeliveryNoteSheetPrinter {

	private static final Logger logger = Logger.getLogger(OutputDeliveryNoteSheetPrinter.class);

	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private PropertyService propertyService;

	public Integer print(Output output) {
		Date date = new Date();

		List<OutputDetail> outputDetails = output.getOutputDetails();
		Integer conceptId = output.getConcept().getId();
		Concept concept = this.conceptService.getAndUpdateDeliveryNote(conceptId, 1);
		Integer deliveryNoteNumber = concept.getDeliveryNoteEnumerator().getLastDeliveryNoteNumber() + 1;

		// Hago el corte de remitos por la cantidad items por pagina que se indique por parametro.

		DeliveryNote deliveryNote = new DeliveryNote();
		List<DeliveryNoteDetail> deliveryNoteDetails = new ArrayList<DeliveryNoteDetail>();
		String deliveryNoteComplete = StringUtility.addLeadingZeros(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4) + "-"
				+ StringUtility.addLeadingZeros(deliveryNoteNumber, 8);
		deliveryNote.setNumber(deliveryNoteComplete);
		for (OutputDetail outputDetail : outputDetails) {

			DeliveryNoteDetail deliveryNoteDetail = new DeliveryNoteDetail();
			deliveryNoteDetail.setOutputDetail(outputDetail);
			deliveryNoteDetails.add(deliveryNoteDetail);

			// Guardo el Remito en la base de datos
		}
		deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
		try {
			if (output.hasProductThatInform() && output.getConcept().isInformAnmat() && propertyService.get().isInformAnmat()) {
				deliveryNote.setInformAnmat(true);
			} else {
				deliveryNote.setInformAnmat(false);
			}
			deliveryNote.setDate(date);
			deliveryNote.setFake(true);
			this.deliveryNoteService.save(deliveryNote);
			this.deliveryNoteService.sendTrasactionAsync(deliveryNote);
			logger.info("Se ha impreso el remito numero: " + deliveryNoteNumber);
		} catch (Exception e1) {
			logger.info("No se ha podido imprimir el remito numero: " + deliveryNoteNumber);
		}

		return deliveryNote.getId();
	}
}
