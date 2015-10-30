package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lsntsolutions.gtmApp.util.DeliveryNoteConfig;
import com.lsntsolutions.gtmApp.helper.DeliveryNoteSheetPrinter;
import com.lsntsolutions.gtmApp.helper.PrintOnPrinter;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.DeliveryNoteService;
import com.lsntsolutions.gtmApp.service.OutputService;
import com.lsntsolutions.gtmApp.service.PropertyService;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OutputDeliveryNoteSheetPrinter implements DeliveryNoteSheetPrinter {

	private static final Logger logger = Logger.getLogger(OutputDeliveryNoteSheetPrinter.class);

	@Autowired
	private PropertyService PropertyService;
	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private OutputService outputService;
	@Autowired
	private PrintOnPrinter printOnPrinter;
	@Autowired
	private ConceptService conceptService;

	@Override
	public List<Integer> print(List<Integer> outputsIds) {
		List<Integer> printsNumbers = new ArrayList<>();
		Date date = new Date();

		for (Integer id : outputsIds) {
			Output output = this.outputService.get(id);
			Integer numberOfDeliveryNoteDetailsPerPage = output.getAgreement().getNumberOfDeliveryNoteDetailsPerPage();
			List<OutputDetail> outputDetails = output.getOutputDetails();
			Integer deliveryNoteNumbersRequired = (outputDetails.size() / numberOfDeliveryNoteDetailsPerPage) + 1;
			Integer conceptId = output.getAgreement().getDeliveryNoteConcept().getId();
			Concept concept = this.conceptService.getAndUpdateDeliveryNote(conceptId, deliveryNoteNumbersRequired);
			DeliveryNoteConfig deliveryNoteConfig = new DeliveryNoteConfig();
			String drugstoreGln = this.PropertyService.get().getGln();
			Integer deliveryNoteNumber = concept.getDeliveryNoteEnumerator().getLastDeliveryNoteNumber() - deliveryNoteNumbersRequired + 1;

			// Hago el corte de remitos por la cantidad items por pagina que se indique por parametro.

			int remaining = outputDetails.size();
			int idx = 0;
			while (remaining > 0) {
				DeliveryNote deliveryNote = new DeliveryNote();
				String deliveryNoteComplete = StringUtility.addLeadingZeros(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4) + "-"
						+ StringUtility.addLeadingZeros(deliveryNoteNumber, 8);
				deliveryNote.setNumber(deliveryNoteComplete);

				List<DeliveryNoteDetail> deliveryNoteDetails = new ArrayList<DeliveryNoteDetail>();
				List<OutputDetail> tempOutputDetails = new ArrayList<OutputDetail>();

				if (remaining > numberOfDeliveryNoteDetailsPerPage) {
					for (int i = 0; i < numberOfDeliveryNoteDetailsPerPage; i++) {
						DeliveryNoteDetail deliveryNoteDetail = new DeliveryNoteDetail();
						tempOutputDetails.add(outputDetails.get(idx));
						deliveryNoteDetail.setOutputDetail(outputDetails.get(idx));
						deliveryNoteDetails.add(deliveryNoteDetail);
						remaining--;
						idx++;
					}
				} else {
					tempOutputDetails = new ArrayList<OutputDetail>();
					for (int j = 0; j < remaining; j++) {
						DeliveryNoteDetail deliveryNoteDetail = new DeliveryNoteDetail();
						tempOutputDetails.add(outputDetails.get(idx));
						deliveryNoteDetail.setOutputDetail(outputDetails.get(idx));
						deliveryNoteDetails.add(deliveryNoteDetail);
						idx++;
					}
					remaining = 0;
				}

				// Imprimo el pdf de Remito
				this.generateDeliveryNoteSheet(output, deliveryNoteNumber, deliveryNoteConfig, drugstoreGln, tempOutputDetails);
				// Guardo el Remito en la base de datos
				deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
				deliveryNote.setDate(date);
				deliveryNote.setFake(false);
				try {
					if (output.hasProductThatInform() && output.getAgreement().getDeliveryNoteConcept().isInformAnmat()) {
						deliveryNote.setInformAnmat(true);
					} else {
						deliveryNote.setInformAnmat(false);
					}
					this.deliveryNoteService.save(deliveryNote);
					this.deliveryNoteService.sendTrasactionAsync(deliveryNote);
				} catch (Exception e1) {
					logger.error("No se ha podido guardar el remito numero: " + deliveryNoteNumber + " para el Egreso numero: " + id);
				}

				logger.info("Se ha guardado el remito numero: " + deliveryNoteNumber + " para el Egreso número: " + id);

				printsNumbers.add(deliveryNote.getId());
				deliveryNoteNumber++;
			}
		}
		return printsNumbers;
	}

	// The coordinates are measured in points. 1 inch is divided into 72 points
	// so that 1 Millimeter equals 2.8346 points.
	private void generateDeliveryNoteSheet(Output output, Integer deliveryNoteNumber, DeliveryNoteConfig deliveryNoteConfig,
			String drugstoreGln, List<OutputDetail> outputDetails) {
		try {
			/* PdfReader reader = new PdfReader(pdfPath + "Estimate_035931_blank.pdf"); PdfStamper pdfStamper = new PdfStamper(reader, new
			 * FileOutputStream(pdfPath + "deliveryNoteNumber-" + deliveryNoteNumber + ".pdf")); */

			Document document = new Document(PageSize.A4);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			//new File(pdfPath).mkdirs();
			//PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath + "deliveryNoteNumber-" + deliveryNoteNumber + ".pdf"));
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.addAuthor("REMITO-" + deliveryNoteNumber);
			document.addTitle("LS&T Solutions");
			document.open();

			// PdfContentByte overContent = pdfStamper.getOverContent(1);
			PdfContentByte overContent = writer.getDirectContent();

			BaseFont bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);

			overContent.saveState();
			overContent.beginText();
			overContent.setFontAndSize(bf, 10.0f);

			// imprimo numero de remito.
			overContent.setTextMatrix(164.0f * 2.8346f, (297.0f - 16.0f) * 2.8346f);
			overContent.showText(StringUtility.addLeadingZeros(deliveryNoteNumber, 8));
/*
			// imprimo fecha.
			overContent.setTextMatrix(deliveryNoteConfig.getDateCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig.getDateCoordinate()
					.getPosY()) * 2.8346f);
			overContent.showText(new SimpleDateFormat("dd/MM/yyyy").format(output.getDate()));

			// imprimo cliente.
			overContent.setTextMatrix(deliveryNoteConfig.getClientCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig.getClientCoordinate()
					.getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getCorporateName());
			} else {
				overContent.showText(output.getProvider().getCorporateName());
			}

			// imprimo domicilio.
			overContent.setTextMatrix(deliveryNoteConfig.getAddressCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getAddressCoordinate().getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getAddress());
			} else {
				overContent.showText(output.getProvider().getAddress());
			}

			// imprimo localidad.
			overContent.setTextMatrix(deliveryNoteConfig.getLocalityCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getLocalityCoordinate().getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getLocality() + "          (" + output.getDeliveryLocation().getZipCode() + ") "
						+ output.getDeliveryLocation().getLocality());
			} else {
				overContent.showText(output.getProvider().getLocality() + "          (" + output.getProvider().getZipCode() + ") "
						+ output.getProvider().getLocality());
			}

			// imprimo condicion IVA.
			overContent.setTextMatrix(deliveryNoteConfig.getTaxConditionCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getTaxConditionCoordinate().getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getVATLiability().getDescription().toUpperCase());
			} else {
				overContent.showText(output.getProvider().getVATLiability().getDescription().toUpperCase());
			}

			// imprimo CUIT.
			overContent.setTextMatrix(deliveryNoteConfig.getTaxIdCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig.getTaxIdCoordinate()
					.getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getTaxId());
			} else {
				overContent.showText(output.getProvider().getTaxId());
			}

			// imprimo OBS1.
			// overContent.setTextMatrix(deliveryNoteConfig.getDeliveryObservation1Coordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
			// .getDeliveryObservation1Coordinate().getPosY()) * 2.8346f);
			// overContent.showText("AF: " + provisioningRequest.getAffiliate().getCode() + "    - " + provisioningRequest.getAffiliate().getSurname() + "  "
			// + provisioningRequest.getAffiliate().getName());

			// imprimo pedido.
			overContent.setTextMatrix(deliveryNoteConfig.getOrderCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig.getOrderCoordinate()
					.getPosY()) * 2.8346f);
			overContent.showText("Pedido: " + deliveryNoteNumber + "  Convenio: " + output.getAgreement().getCode() + "-"
					+ output.getAgreement().getDescription());

			// imprimo cliente entrega.
			overContent.setTextMatrix(deliveryNoteConfig.getClientDeliveryCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getClientDeliveryCoordinate().getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getCorporateName());
			} else {
				overContent.showText(output.getProvider().getCorporateName());
			}

			// imprimo domicilio entrega.
			overContent.setTextMatrix(deliveryNoteConfig.getDeliveryAddressCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getDeliveryAddressCoordinate().getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getAddress());
			} else {
				overContent.showText(output.getProvider().getAddress());
			}

			// imprimo localidad entrega.
			overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocalityCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getDeliveryLocalityCoordinate().getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getLocality() + "          (" + output.getDeliveryLocation().getZipCode() + ") "
						+ output.getDeliveryLocation().getLocality());
			} else {
				overContent.showText(output.getProvider().getLocality() + "          (" + output.getProvider().getZipCode() + ") "
						+ output.getProvider().getLocality());
			}

			// imprimo condicion IVA entrega.
			overContent.setTextMatrix(deliveryNoteConfig.getDeliveryTaxConditionCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getDeliveryTaxConditionCoordinate().getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getVATLiability().getDescription().toUpperCase());
			} else {
				overContent.showText(output.getProvider().getVATLiability().getDescription().toUpperCase());
			}

			// imprimo CUIT entrega.
			overContent.setTextMatrix(deliveryNoteConfig.getDeliveryTaxIdCoordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getDeliveryTaxIdCoordinate().getPosY()) * 2.8346f);
			if (output.getDeliveryLocation() != null) {
				overContent.showText(output.getDeliveryLocation().getTaxId());
			} else {
				overContent.showText(output.getProvider().getTaxId());
			}

			// imprimo OBS 1
			overContent.setTextMatrix(deliveryNoteConfig.getObservation1Coordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getObservation1Coordinate().getPosY()) * 2.8346f);
			String gln;
			if (output.getDeliveryLocation() != null) {
				gln = output.getDeliveryLocation().getGln();
			} else {
				gln = output.getProvider().getGln();
			}
			String details = "GLN Origen: " + drugstoreGln + "  GLN Destino: " + gln;
			overContent.showText(details);
			int amount = 0;
			Product product = new Product();
			product.setId(-1);
			for (OutputDetail outputDetail : outputDetails) {
				if (outputDetail.getProduct().getId() != product.getId()) {
					product = outputDetail.getProduct();

					details = product.getDescription() + "      " + product.getMonodrug().getDescription() + "      " + product.getBrand().getDescription();
					overContent.setLeading(8);
					overContent.newlineShowText(details);
				}

				details = "LOTE: " + outputDetail.getBatch() + "      " + "VTO: " + new SimpleDateFormat("dd/MM/yyyy").format(outputDetail.getExpirationDate());

				if (outputDetail.getSerialNumber() != null) {
					details += "      " + "SERIE: " + outputDetail.getSerialNumber();
				}
				overContent.setLeading(8);
				overContent.newlineShowText(details);

				overContent.saveState();
				overContent.setTextMatrix(184f * 2.8346f, overContent.getYTLM());
				overContent.showText(String.format("%s,000", outputDetail.getAmount()));
				overContent.restoreState();

				overContent.newlineShowText("");

				amount += outputDetail.getAmount();
			}

			// imprimo OBS 2
			overContent.setTextMatrix(deliveryNoteConfig.getObservation2Coordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getObservation2Coordinate().getPosY()) * 2.8346f);
			overContent.showText("Cant. de Items: " + amount);

			// imprimo OBS 3
			overContent.setTextMatrix(deliveryNoteConfig.getObservation3Coordinate().getPosX() * 2.8346f, (297.0f - deliveryNoteConfig
					.getObservation3Coordinate().getPosY()) * 2.8346f);
			// if (provisioningRequest.getLogisticsOperator() != null) {
			// overContent.showText(provisioningRequest.getLogisticsOperator().getCode() + "   "
			// + provisioningRequest.getLogisticsOperator().getCorporateName() + "- " + provisioningRequest.getLogisticsOperator().getTaxId());
			// }
*/
			overContent.endText();
			overContent.restoreState();

			document.close();

			ByteArrayInputStream pdfDocument = new ByteArrayInputStream(out.toByteArray());

			this.printOnPrinter.sendPDFToSpool(output.getAgreement().getDeliveryNoteFilepath(), "REMITO NRO-" + deliveryNoteNumber + ".pdf", pdfDocument);

			pdfDocument.close();

		} catch (Exception e) {
			throw new RuntimeException("No se ha podido generar el Remito Nro.: " + deliveryNoteNumber, e);
		}
	}
}
