package com.drogueria.helper.impl;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drogueria.constant.State;
import com.drogueria.helper.DeliveryNoteConfigFile;
import com.drogueria.helper.DeliveryNoteSheetPrinter;
import com.drogueria.helper.PrintOnPrinter;
import com.drogueria.model.Concept;
import com.drogueria.model.DeliveryNote;
import com.drogueria.model.DeliveryNoteDetail;
import com.drogueria.model.Order;
import com.drogueria.model.OrderDetail;
import com.drogueria.model.Product;
import com.drogueria.model.ProvisioningRequest;
import com.drogueria.model.ProvisioningRequestState;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryNoteService;
import com.drogueria.service.PropertyService;
import com.drogueria.service.OrderService;
import com.drogueria.service.ProvisioningRequestService;
import com.drogueria.service.ProvisioningRequestStateService;
import com.drogueria.util.StringUtility;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class OrderDeliveryNoteSheetPrinter implements DeliveryNoteSheetPrinter {

	private static final Logger logger = Logger.getLogger(OrderDeliveryNoteSheetPrinter.class);

	@Autowired
	private ProvisioningRequestService provisioningRequestService;
	@Autowired
	private PropertyService PropertyService;
	@Autowired
	private ProvisioningRequestStateService provisioningRequestStateService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private PrintOnPrinter printOnPrinter;
	@Autowired
	private ConceptService conceptService;

	@Override
	public List<Integer> print(List<Integer> ordersIds) {
		List<Integer> printsNumbers = new ArrayList<>();
		ProvisioningRequestState state = this.provisioningRequestStateService.get(State.DELIVERY_NOTE_PRINTED.getId());
		Date date = new Date();

		for (Integer id : ordersIds) {
			Order order = this.orderService.get(id);
			ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(order.getProvisioningRequest().getId());
			Integer numberOfDeliveryNoteDetailsPerPage = provisioningRequest.getAgreement().getNumberOfDeliveryNoteDetailsPerPage();
			provisioningRequest.setState(state);
			this.provisioningRequestService.save(provisioningRequest);

			List<OrderDetail> orderDetails = order.getOrderDetails();
			Integer deliveryNoteNumbersRequired = (orderDetails.size() / numberOfDeliveryNoteDetailsPerPage) + 1;
			Integer conceptId = order.getProvisioningRequest().getAgreement().getDeliveryNoteConcept().getId();
			Concept concept = this.conceptService.getAndUpdateDeliveryNote(conceptId, deliveryNoteNumbersRequired);
			DeliveryNoteConfigFile deliveryNoteConfigFile = new DeliveryNoteConfigFile(provisioningRequest.getAgreement().getDeliveryNoteFilepath());
			String drugstoreGln = this.PropertyService.get().getGln();
			Integer deliveryNoteNumber = concept.getLastDeliveryNoteNumber() - deliveryNoteNumbersRequired + 1;

			// Hago el corte de remitos por la cantidad items por pagina que se indique por parametro.

			int remaining = orderDetails.size();
			int idx = 0;
			while (remaining > 0) {
				DeliveryNote deliveryNote = new DeliveryNote();
				deliveryNote.setNumber(Integer.toString(deliveryNoteNumber));

				List<DeliveryNoteDetail> deliveryNoteDetails = new ArrayList<DeliveryNoteDetail>();
				List<OrderDetail> tempOrderDetails = new ArrayList<OrderDetail>();

				if (remaining > numberOfDeliveryNoteDetailsPerPage) {
					for (int i = 0; i < numberOfDeliveryNoteDetailsPerPage; i++) {
						DeliveryNoteDetail deliveryNoteDetail = new DeliveryNoteDetail();
						tempOrderDetails.add(orderDetails.get(idx));
						deliveryNoteDetail.setOrderDetail(orderDetails.get(idx));
						deliveryNoteDetails.add(deliveryNoteDetail);
						remaining--;
						idx++;
					}
				} else {
					tempOrderDetails = new ArrayList<OrderDetail>();
					for (int j = 0; j < remaining; j++) {
						DeliveryNoteDetail deliveryNoteDetail = new DeliveryNoteDetail();
						tempOrderDetails.add(orderDetails.get(idx));
						deliveryNoteDetail.setOrderDetail(orderDetails.get(idx));
						deliveryNoteDetails.add(deliveryNoteDetail);
						idx++;
					}
					remaining = 0;
				}

				// Imprimo el pdf de Remito
				this.generateDeliveryNoteSheet(provisioningRequest, deliveryNoteNumber, deliveryNoteConfigFile, provisioningRequest.getAgreement()
						.getDeliveryNoteFilepath(), drugstoreGln, tempOrderDetails);
				// Guardo el Remito en la base de datos
				deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
				deliveryNote.setDate(date);
				deliveryNote.setFake(false);
				try {
					if (order.hasToInform() && provisioningRequest.getAgreement().getDeliveryNoteConcept().isInformAnmat()) {
						deliveryNote.setInformAnmat(true);
					} else {
						deliveryNote.setInformAnmat(false);
					}
					this.deliveryNoteService.save(deliveryNote);
					this.deliveryNoteService.sendTrasactionAsync(deliveryNote);
				} catch (Exception e1) {
					logger.info("No se ha podido imprimir el remito: " + deliveryNoteNumber + " para la Solicitud de Abastecimiento número: " + id);
				}

				printsNumbers.add(deliveryNote.getId());
				deliveryNoteNumber++;
			}
		}
		return printsNumbers;
	}

	// The coordinates are measured in points. 1 inch is divided into 72 points
	// so that 1 Millimeter equals 2.8346 points.
	private void generateDeliveryNoteSheet(ProvisioningRequest provisioningRequest, Integer deliveryNoteNumber, DeliveryNoteConfigFile deliveryNoteConfigFile,
			String pdfPath, String drugstoreGln, List<OrderDetail> orderDetails) {
		try {
			/*PdfReader reader = new PdfReader(pdfPath + "Estimate_035931_blank.pdf");
			PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream(pdfPath + "deliveryNoteNumber-" + deliveryNoteNumber + ".pdf"));*/

			Document document = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath + "deliveryNoteNumber-" + deliveryNoteNumber + ".pdf"));
			document.addAuthor("REMITO-" + deliveryNoteNumber);
			document.addTitle("LS&T Solutions");
			document.open();
			
			//PdfContentByte overContent = pdfStamper.getOverContent(1);
			PdfContentByte overContent = writer.getDirectContent();

			BaseFont bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);

			overContent.saveState();
			overContent.beginText();
			overContent.setFontAndSize(bf, 10.0f);

			// imprimo numero de remito.
			overContent.setTextMatrix(164.0f * 2.8346f, (293.60f - 16.0f) * 2.8346f);
			overContent.showText(StringUtility.addLeadingZeros(deliveryNoteNumber, 8));

			// imprimo fecha.
			overContent.setTextMatrix(deliveryNoteConfigFile.getDateCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile.getDateCoordinate()
					.getPosY()) * 2.8346f);
			overContent.showText(new SimpleDateFormat("dd/MM/yyyy").format(provisioningRequest.getDeliveryDate()));

			// imprimo cliente.
			overContent.setTextMatrix(deliveryNoteConfigFile.getClientCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile.getClientCoordinate()
					.getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getClient().getCorporateName());

			// imprimo domicilio.
			overContent.setTextMatrix(deliveryNoteConfigFile.getAddressCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getAddressCoordinate().getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getClient().getAddress());

			// imprimo localidad.
			overContent.setTextMatrix(deliveryNoteConfigFile.getLocalityCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getLocalityCoordinate().getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getClient().getLocality() + "          (" + provisioningRequest.getClient().getZipCode() + ") "
					+ provisioningRequest.getClient().getLocality());

			// imprimo condicion IVA.
			overContent.setTextMatrix(deliveryNoteConfigFile.getTaxConditionCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getTaxConditionCoordinate().getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getClient().getVATLiability().getDescription().toUpperCase());

			// imprimo CUIT.
			overContent.setTextMatrix(deliveryNoteConfigFile.getTaxIdCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile.getTaxIdCoordinate()
					.getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getClient().getTaxId());

			// imprimo OBS1.
			overContent.setTextMatrix(deliveryNoteConfigFile.getDeliveryObservation1Coordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getDeliveryObservation1Coordinate().getPosY()) * 2.8346f);
			overContent.showText("AF: " + provisioningRequest.getAffiliate().getCode() + "    - " + provisioningRequest.getAffiliate().getSurname() + "  "
					+ provisioningRequest.getAffiliate().getName());

			// imprimo pedido.
			overContent.setTextMatrix(deliveryNoteConfigFile.getOrderCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile.getOrderCoordinate()
					.getPosY()) * 2.8346f);
			overContent.showText("Pedido: " + deliveryNoteNumber + "  Convenio: " + provisioningRequest.getAgreement().getCode() + "-"
					+ provisioningRequest.getAgreement().getDescription());

			// imprimo cliente entrega.
			overContent.setTextMatrix(deliveryNoteConfigFile.getClientDeliveryCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getClientDeliveryCoordinate().getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getDeliveryLocation().getCorporateName());

			// imprimo domicilio entrega.
			overContent.setTextMatrix(deliveryNoteConfigFile.getDeliveryAddressCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getDeliveryAddressCoordinate().getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getDeliveryLocation().getAddress());

			// imprimo localidad entrega.
			overContent.setTextMatrix(deliveryNoteConfigFile.getDeliveryLocalityCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getDeliveryLocalityCoordinate().getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getDeliveryLocation().getLocality() + "          ("
					+ provisioningRequest.getDeliveryLocation().getZipCode() + ") " + provisioningRequest.getDeliveryLocation().getLocality());

			// imprimo condicion IVA entrega.
			overContent.setTextMatrix(deliveryNoteConfigFile.getDeliveryTaxConditionCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getDeliveryTaxConditionCoordinate().getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getDeliveryLocation().getVATLiability().getDescription().toUpperCase());

			// imprimo CUIT entrega.
			overContent.setTextMatrix(deliveryNoteConfigFile.getDeliveryTaxIdCoordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getDeliveryTaxIdCoordinate().getPosY()) * 2.8346f);
			overContent.showText(provisioningRequest.getDeliveryLocation().getTaxId());

			// imprimo OBS 1
			overContent.setTextMatrix(deliveryNoteConfigFile.getObservation1Coordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getObservation1Coordinate().getPosY()) * 2.8346f);
			String details = "GLN Origen: " + drugstoreGln + "  GLN Destino: " + provisioningRequest.getDeliveryLocation().getGln();
			overContent.showText(details);
			int amount = 0;
			Product product = new Product();
			product.setId(-1);
			for (OrderDetail orderDetail : orderDetails) {
				if (orderDetail.getProduct().getId() != product.getId()) {
					product = orderDetail.getProduct();

					details = product.getDescription() + "      " + product.getMonodrug().getDescription() + "      " + product.getBrand().getDescription();
					overContent.setLeading(8);
					overContent.newlineShowText(details);
				}

				details = "LOTE: " + orderDetail.getBatch() + "      " + "VTO: " + new SimpleDateFormat("dd/MM/yyyy").format(orderDetail.getExpirationDate());

				if (orderDetail.getSerialNumber() != null) {
					details += "      " + "SERIE: " + orderDetail.getSerialNumber();
				}
				overContent.setLeading(8);
				overContent.newlineShowText(details);

				overContent.saveState();
				overContent.setTextMatrix(184f * 2.8346f, overContent.getYTLM());
				overContent.showText(String.format("%s,000", orderDetail.getAmount()));
				overContent.restoreState();

				overContent.newlineShowText("");

				amount += orderDetail.getAmount();
			}

			// imprimo OBS 2
			overContent.setTextMatrix(deliveryNoteConfigFile.getObservation2Coordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getObservation2Coordinate().getPosY()) * 2.8346f);
			overContent.showText("Cant. de Items: " + amount);

			// imprimo OBS 3
			overContent.setTextMatrix(deliveryNoteConfigFile.getObservation3Coordinate().getPosX() * 2.8346f, (293.60f - deliveryNoteConfigFile
					.getObservation3Coordinate().getPosY()) * 2.8346f);
			if (provisioningRequest.getLogisticsOperator() != null) {
				overContent.showText(provisioningRequest.getLogisticsOperator().getCode() + "   "
						+ provisioningRequest.getLogisticsOperator().getCorporateName() + "- " + provisioningRequest.getLogisticsOperator().getTaxId());
			}

			overContent.endText();
			overContent.restoreState();

			//pdfStamper.close();
			document.close();

			// TODO obtener IP desde el concepto o drugstore property, el archivo se guardaria en una carpeta del servidor.
			// this.printOnPrinter.sendToPrint("//10.80.38.149/Lexmark T632", pdfPath + "deliveryNoteNumber-" + deliveryNoteNumber + ".pdf");

		} catch (Exception e) {
			throw new RuntimeException("No se ha podido generar el Remito Nro.: " + deliveryNoteNumber, e);
		}
	}
}
