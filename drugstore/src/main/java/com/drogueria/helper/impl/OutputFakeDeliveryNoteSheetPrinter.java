package com.drogueria.helper.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drogueria.model.Concept;
import com.drogueria.model.DeliveryNote;
import com.drogueria.model.DeliveryNoteDetail;
import com.drogueria.model.Output;
import com.drogueria.model.OutputDetail;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryNoteService;

@Service
public class OutputFakeDeliveryNoteSheetPrinter {

	private static final Logger logger = Logger.getLogger(OutputDeliveryNoteSheetPrinter.class);

	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private ConceptService conceptService;

	public Integer print(Output output) {
		Date date = new Date();

		List<OutputDetail> outputDetails = output.getOutputDetails();
		Integer conceptId = output.getAgreement().getDeliveryNoteConcept().getId();
		Concept concept = this.conceptService.getAndUpdateDeliveryNote(conceptId, 1);
		Integer deliveryNoteNumber = concept.getLastDeliveryNoteNumber() + 1;

		// Hago el corte de remitos por la cantidad items por pagina que se indique por parametro.

		for (OutputDetail outputDetail : outputDetails) {
			DeliveryNote deliveryNote = new DeliveryNote();
			deliveryNote.setNumber(Integer.toString(deliveryNoteNumber));

			List<DeliveryNoteDetail> deliveryNoteDetails = new ArrayList<DeliveryNoteDetail>();

			DeliveryNoteDetail deliveryNoteDetail = new DeliveryNoteDetail();
			deliveryNoteDetail.setOutputDetail(outputDetail);
			deliveryNoteDetails.add(deliveryNoteDetail);

			// Guardo el Remito en la base de datos
			deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
			deliveryNote.setDate(date);
			deliveryNote.setFake(true);
			try {
				if (output.hasProductThatInform() && output.getConcept().isInformAnmat()) {
					deliveryNote.setInformAnmat(true);
				} else {
					deliveryNote.setInformAnmat(false);
				}
				this.deliveryNoteService.save(deliveryNote);
				this.deliveryNoteService.sendTrasactionAsync(deliveryNote);
			} catch (Exception e1) {
				logger.info("No se ha podido imprimir el remito numero: " + deliveryNoteNumber);
			}

			logger.info("Se ha impreso el remito numero: " + deliveryNoteNumber);

		}
		return deliveryNoteNumber;
	}
}
