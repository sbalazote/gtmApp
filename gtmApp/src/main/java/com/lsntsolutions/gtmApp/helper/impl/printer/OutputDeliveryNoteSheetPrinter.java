package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.helper.DeliveryNoteSheetPrinter;
import com.lsntsolutions.gtmApp.helper.PrintOnPrinter;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.DeliveryNoteConfig;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OutputDeliveryNoteSheetPrinter implements DeliveryNoteSheetPrinter {

	private static final Logger logger = Logger.getLogger(OutputDeliveryNoteSheetPrinter.class);

	@Autowired
	private PropertyService PropertyService;
	@Autowired
	private OutputService outputService;
	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private PrintOnPrinter printOnPrinter;
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private DeliveryNoteConfig deliveryNoteConfig;
	@Autowired
	private AuditService auditService;

	private Output output;
	private Property property;

	private Document document;
	private ByteArrayOutputStream out;
	private PdfWriter writer;
	private PdfContentByte overContent;
	private BaseFont bf;
	private int totalItems;
	private Integer deliveryNoteNumber;
	private int offsetY;
	private String POS;
	private Date currentDate;
	private DeliveryNote deliveryNote;
	private String deliveryNoteComplete;
	private List<DeliveryNoteDetail> deliveryNoteDetails;
	private DeliveryNoteDetail deliveryNoteDetail;
	private List<String> printsNumbers;
	private String userName;
    private boolean printHeader;

	@Override
	public void print(String userName, List<Integer> outputsIds, PrinterResultDTO printerResultDTO) {
		this.userName = userName;
		printsNumbers = new ArrayList<>();
		currentDate = new Date();
		for (Integer id : outputsIds) {
			output = this.outputService.get(id);
			Integer numberOfDeliveryNoteDetailsPerPage = output.getAgreement().getNumberOfDeliveryNoteDetailsPerPage();

			// agrupo lista de productos por id de producto + lote.
			TreeMap<String, List<OutputDetail>> outputMap = groupByProductAndBatch(output);
			// calculo cuantas lineas de detalles de productos voy a necesitar.
			int numberOfLinesNeeded = numberOfLinesNeeded(outputMap);
			// calculo cuantos remitos voy a necesitar en base a la cantidad de detalles de productos.
			int deliveryNoteNumbersRequired = (int)Math.ceil((float)numberOfLinesNeeded / numberOfDeliveryNoteDetailsPerPage);

			Integer conceptId = output.getAgreement().getDeliveryNoteConcept().getId();
			Concept concept = this.conceptService.getAndUpdateDeliveryNote(conceptId, deliveryNoteNumbersRequired);
			POS = StringUtility.addLeadingZeros(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4);
			property = this.PropertyService.get();
			deliveryNoteNumber = concept.getDeliveryNoteEnumerator().getLastDeliveryNoteNumber() - deliveryNoteNumbersRequired + 1;

			document = new Document(PageSize.A4);
			out = new ByteArrayOutputStream();
			try {
				writer = PdfWriter.getInstance(document, out);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			document.addAuthor("REMITO-" + deliveryNoteNumber);
			document.addTitle("LS&T Solutions");
			document.open();
			overContent = writer.getDirectContent();

			printHeader(deliveryNoteNumber);

			deliveryNote = new DeliveryNote();
			deliveryNoteComplete = POS + "-" + StringUtility.addLeadingZeros(deliveryNoteNumber, 8);
			deliveryNote.setNumber(deliveryNoteComplete);

			deliveryNoteDetails = new ArrayList<DeliveryNoteDetail>();

			// numero de linea de detalle de producto actual.
			int currentLine = 0;
			// offset coordenada vertical a partir de donde se indico el detalle de productos.
			offsetY = 0;
			// cantidad de items totales para esta pagina de remito.
			totalItems = 0;
            // id de producto en la iteracion anterior.
            String previousProductId = "";
			// recorro el mapa de detalle de productos a imprimir.
			for (Map.Entry<String, List<OutputDetail>> entry : outputMap.entrySet()) {
				String key = entry.getKey();
				List<OutputDetail> outputDetails = entry.getValue();
				String[] parts = key.split(",");
                String currentProductId = parts[0];
				String productType = outputDetails.get(0).getProduct().getType();

				// si ya esta lleno el remito, sigo en uno nuevo
				if (currentLine == numberOfDeliveryNoteDetailsPerPage) {
					savePage();
					newPage();
				}

                String description = outputDetails.get(0).getProduct().getDescription();
                String monodrug = outputDetails.get(0).getProduct().getMonodrug().getDescription();
                String brand = outputDetails.get(0).getProduct().getBrand().getDescription();
                int totalAmount = outputDetails.get(0).getAmount();
                String batch = outputDetails.get(0).getBatch();
                String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(outputDetails.get(0).getExpirationDate());
                String batchAmount = Integer.toString(productType.equals("BE") ? totalAmount : outputDetails.size());
                if (!currentProductId.equals(previousProductId)) {
                    printProductDetailHeader(description, monodrug, brand, getProductTotalAmount(Integer.parseInt(currentProductId)));
                }
                printProductBatchExpirationDateHeader(batch, expirationDate, batchAmount);

				// si es de tipo lote/ vto ocupa 1 (una) sola linea.
                DeliveryNoteDetail deliveryNoteDetail;
                if (productType.equals("BE")) {

					deliveryNoteDetail = new DeliveryNoteDetail();
					deliveryNoteDetail.setOutputDetail(outputDetails.get(0));
					deliveryNoteDetails.add(deliveryNoteDetail);

					currentLine++;
					totalItems += totalAmount;
					// si es de tipo trazado ocupa 1 (una) sola linea por cada cuatro series.
				} else {
                    List<OutputDetail> outputDetailAux = new ArrayList<>();
                    Iterator<OutputDetail> it = outputDetails.iterator();
					int serialIdx = 0;
					while (it.hasNext()) {
						OutputDetail od = it.next();
						outputDetailAux.add(od);

						deliveryNoteDetail = new DeliveryNoteDetail();
						deliveryNoteDetail.setOutputDetail(od);
						deliveryNoteDetails.add(deliveryNoteDetail);

						serialIdx++;

						if ((serialIdx == 4) || (!it.hasNext())) {
							totalItems += outputDetailAux.size();
							printSerialDetails(outputDetailAux);
							currentLine++;
							serialIdx = 0;
							outputDetailAux = new ArrayList<>();
						}
					}
				}
                previousProductId = currentProductId;
			}

			printFooter(totalItems);

			savePage();

			document.close();

			ByteArrayInputStream pdfDocument = new ByteArrayInputStream(out.toByteArray());

			this.printOnPrinter.sendPDFToSpool(output.getAgreement().getDeliveryNotePrinter(), "REMITO NRO-" + deliveryNoteNumber + ".pdf", pdfDocument, printerResultDTO);

			try {
				pdfDocument.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		printerResultDTO.setDeliveryNoteNumbers(printsNumbers);
	}

	private void newPage() {
		printFooter(totalItems);
		document.newPage();
		printHeader(deliveryNoteNumber);

		offsetY = 0;
		totalItems = 0;

		deliveryNote = new DeliveryNote();
		deliveryNoteComplete = POS + "-" + StringUtility.addLeadingZeros(deliveryNoteNumber, 8);
		deliveryNote.setNumber(deliveryNoteComplete);

		deliveryNoteDetails = new ArrayList<DeliveryNoteDetail>();
	}

	private void savePage() {
		// Guardo el Remito en la base de datos
		deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
		deliveryNote.setDate(currentDate);
		deliveryNote.setFake(false);
		try {
			if (output.hasToInform() && output.getAgreement().getDeliveryNoteConcept().isInformAnmat() && PropertyService.get().isInformAnmat()) {
				deliveryNote.setInformAnmat(true);
			} else {
				deliveryNote.setInformAnmat(false);
			}
			this.deliveryNoteService.save(deliveryNote);
			this.deliveryNoteService.sendTrasactionAsync(deliveryNote);
		} catch (Exception e1) {
			logger.error("No se ha podido guardar el remito: " + deliveryNoteNumber + " para el Egreso número: " + output.getId());
		}

		logger.info("Se ha guardado el remito numero: " + deliveryNoteNumber + " para el Egreso número: " + output.getId());
		this.auditService.addAudit(this.userName, RoleOperation.DELIVERY_NOTE_PRINT.getId(), AuditState.COMFIRMED, deliveryNote.getId());


		printsNumbers.add(deliveryNote.getNumber());
		deliveryNoteNumber++;
	}

	private void printHeader(Integer deliveryNoteNumber) {
		overContent.saveState();

		// seteo el tipo de fuente.
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
			overContent.setFontAndSize(bf, deliveryNoteConfig.getFontSize());
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// imprimo numero de remito.
		overContent.setTextMatrix(deliveryNoteConfig.getNumberX(), deliveryNoteConfig.getNumberY());
		overContent.showText(deliveryNoteConfig.isNumberPrint() ? StringUtility.addLeadingZeros(deliveryNoteNumber, 8): "");

		// imprimo fecha.
		overContent.setTextMatrix(deliveryNoteConfig.getDateX(), deliveryNoteConfig.getDateY());
		overContent.showText(deliveryNoteConfig.isDatePrint() ? new SimpleDateFormat("dd/MM/yyyy").format(output.getDate()) : "");

		// imprimo cliente.
		overContent.setTextMatrix(deliveryNoteConfig.getIssuerCorporateNameX(), deliveryNoteConfig.getIssuerCorporateNameY());
		overContent.showText(deliveryNoteConfig.isIssuerCorporateNamePrint() ? property.getCorporateName() : "");

		// imprimo domicilio.
		overContent.setTextMatrix(deliveryNoteConfig.getIssuerAddressX(), deliveryNoteConfig.getIssuerAddressY());
		overContent.showText(deliveryNoteConfig.isIssuerCorporateNamePrint() ? property.getAddress() : "");

		// imprimo localidad.
		overContent.setTextMatrix(deliveryNoteConfig.getIssuerLocalityX(), deliveryNoteConfig.getIssuerLocalityY());
		overContent.showText(deliveryNoteConfig.isIssuerLocalityPrint() ? property.getLocality() : "");

		// imprimo cod. postal.
		overContent.setTextMatrix(deliveryNoteConfig.getIssuerZipcodeX(), deliveryNoteConfig.getIssuerZipcodeY());
		overContent.showText(deliveryNoteConfig.isIssuerZipcodePrint() ? ("(" + property.getZipCode() + ")") : "");

		// imprimo provincia.
		overContent.setTextMatrix(deliveryNoteConfig.getIssuerProvinceX(), deliveryNoteConfig.getIssuerProvinceY());
		overContent.showText(deliveryNoteConfig.isIssuerProvincePrint() ? property.getProvince().getName() : "");

		// imprimo condicion IVA.
		overContent.setTextMatrix(deliveryNoteConfig.getIssuerVatliabilityX(), deliveryNoteConfig.getIssuerVatliabilityY());
		overContent.showText(deliveryNoteConfig.isIssuerVatliabilityPrint() ? property.getVATLiability().getDescription().toUpperCase() : "");

		// imprimo CUIT.
		overContent.setTextMatrix(deliveryNoteConfig.getIssuerTaxX(), deliveryNoteConfig.getIssuerTaxY());
		overContent.showText(deliveryNoteConfig.isIssuerTaxPrint() ? property.getTaxId() : "");

		// imprimo egreso.
		overContent.setTextMatrix(deliveryNoteConfig.getOrderX(), deliveryNoteConfig.getOrderY());
		String outputId = StringUtility.addLeadingZeros(output.getId().toString(), 8);
		String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(output.getAgreement().getCode()),5) + " - " + output.getAgreement().getDescription();
		overContent.showText(deliveryNoteConfig.isOrderPrint() ? ("Egreso Nro.: " + outputId + "     Convenio: " + codeAgreement) : "");

		// imprimo cliente entrega.
		overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationCorporateNameX(), deliveryNoteConfig.getDeliveryLocationCorporateNameY());
		if (output.getDeliveryLocation() != null) {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationCorporateNamePrint() ? output.getDeliveryLocation().getCorporateName() : "");
		} else {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationCorporateNamePrint() ? output.getProvider().getCorporateName() : "");
		}

		// imprimo domicilio entrega.
		overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationAddressX(), deliveryNoteConfig.getDeliveryLocationAddressY());
		if (output.getDeliveryLocation() != null) {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationAddressPrint() ? output.getDeliveryLocation().getCorporateName() : "");
		} else {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationAddressPrint() ? output.getProvider().getCorporateName() : "");
		}

		// imprimo localidad entrega.
		overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationLocalityX(), deliveryNoteConfig.getDeliveryLocationLocalityY());
		if (output.getDeliveryLocation() != null) {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationLocalityPrint() ? output.getDeliveryLocation().getLocality() : "");
		} else {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationLocalityPrint() ? output.getProvider().getLocality() : "");
		}

		// imprimo cod. postal entrega.
		overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationZipcodeX(), deliveryNoteConfig.getDeliveryLocationZipcodeY());
		if (output.getDeliveryLocation() != null) {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationZipcodePrint() ? ("(" + output.getDeliveryLocation().getZipCode() + ")") : "");
		} else {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationZipcodePrint() ? ("(" + output.getProvider().getZipCode() + ")") : "");
		}

		// imprimo provincia entrega.
		overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationProvinceX(), deliveryNoteConfig.getDeliveryLocationProvinceY());
		if (output.getDeliveryLocation() != null) {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationProvincePrint() ? output.getDeliveryLocation().getProvince().getName() : "");
		} else {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationProvincePrint() ? output.getProvider().getProvince().getName() : "");
		}

		// imprimo condicion IVA entrega.
		overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationVatliabilityX(), deliveryNoteConfig.getDeliveryLocationVatliabilityY());
		if (output.getDeliveryLocation() != null) {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationVatliabilityPrint() ? output.getDeliveryLocation().getVATLiability().getDescription().toUpperCase() : "");
		} else {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationVatliabilityPrint() ? output.getProvider().getVATLiability().getDescription().toUpperCase() : "");
		}

		// imprimo CUIT entrega.
		overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationTaxX(), deliveryNoteConfig.getDeliveryLocationTaxY());
		if (output.getDeliveryLocation() != null) {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationTaxPrint() ? output.getDeliveryLocation().getTaxId() : "");
		} else {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationTaxPrint() ? output.getProvider().getTaxId() : "");
		}

		// imprimo GLN Origen
		overContent.setTextMatrix(deliveryNoteConfig.getIssuerGlnX(), deliveryNoteConfig.getIssuerGlnY());
		overContent.showText(deliveryNoteConfig.isIssuerGlnPrint() ? ("GLN Origen: " + property.getGln()) : "");

		// imprimo GLN Destino
		overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationGlnX(), deliveryNoteConfig.getDeliveryLocationGlnY());
		if (output.getDeliveryLocation() != null) {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationGlnPrint() ? ("GLN Destino: " + output.getDeliveryLocation().getGln()) : "");
		} else {
			overContent.showText(deliveryNoteConfig.isDeliveryLocationGlnPrint() ? ("GLN Destino: " + output.getProvider().getGln()) : "");
		}

		overContent.restoreState();
	}

	private void printProductDetailHeader(String description, String monodrug, String brand, int totalAmount) {

        // offset con respecto a la linea anterior.
        int PRODUCT_DETAIL_HEADER_LINE_OFFSET_Y = 30;
        offsetY += (printHeader ? 0 : PRODUCT_DETAIL_HEADER_LINE_OFFSET_Y);

		overContent.saveState();

		// seteo el tipo de fuente.
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
			overContent.setFontAndSize(bf, deliveryNoteConfig.getFontSize());
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// imprimo descripcion.
		overContent.setTextMatrix(deliveryNoteConfig.getProductDescriptionX(), deliveryNoteConfig.getProductDetailsY() - offsetY);
		overContent.showText(deliveryNoteConfig.isProductDescriptionPrint() ? description : "");

		// imprimo monodraga.
		overContent.setTextMatrix(deliveryNoteConfig.getProductMonodrugX(), deliveryNoteConfig.getProductDetailsY() - offsetY);
		overContent.showText(deliveryNoteConfig.isProductMonodrugPrint() ? monodrug : "");

		// imprimo marca.
		overContent.setTextMatrix(deliveryNoteConfig.getProductBrandX(), deliveryNoteConfig.getProductDetailsY() - offsetY);
		overContent.showText(deliveryNoteConfig.isProductBrandPrint() ? brand : "");

		// imprimo cantidad total.
		overContent.setTextMatrix(deliveryNoteConfig.getProductAmountX(), deliveryNoteConfig.getProductDetailsY() - offsetY);
		overContent.showText(deliveryNoteConfig.isProductAmountPrint() ? Integer.toString(totalAmount) : "");

		overContent.restoreState();

        printHeader = false;
	}

    private void printProductBatchExpirationDateHeader(String batch, String expirationDate, String batchAmount) {

		// offset con respecto a la linea anterior.
        int PRODUCT_BATCH_EXPIRATIONDATE_HEADER_LINE_OFFSET_Y = 20;
		offsetY += PRODUCT_BATCH_EXPIRATIONDATE_HEADER_LINE_OFFSET_Y;

        overContent.saveState();

		// seteo el tipo de fuente.
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false);
			overContent.setFontAndSize(bf, deliveryNoteConfig.getFontSize());
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		// imprimo lote.
		overContent.setTextMatrix(deliveryNoteConfig.getProductBatchExpirationdateX(), deliveryNoteConfig.getProductDetailsY() - offsetY);
		overContent.showText(deliveryNoteConfig.isProductBatchExpirationdatePrint() ? ("Lote: " + batch) : "");

		// imprimo vencimiento.
		overContent.setTextMatrix(deliveryNoteConfig.getProductBatchExpirationdateX() + 120, deliveryNoteConfig.getProductDetailsY() - offsetY);
		overContent.showText(deliveryNoteConfig.isProductBatchExpirationdatePrint() ? ("Vto.: " + expirationDate) : "");

        // imprimo cantidad total del lote.
        overContent.setTextMatrix(deliveryNoteConfig.getProductAmountX(), deliveryNoteConfig.getProductDetailsY() - offsetY);
        overContent.showText(deliveryNoteConfig.isProductAmountPrint() ? batchAmount : "");

		overContent.restoreState();
	}

	private void printSerialDetails(List<OutputDetail> outputDetails) {

		// offset con respecto a la linea anterior.
		int SERIAL_DETAIL_LINE_OFFSET_Y = 10;
		offsetY += SERIAL_DETAIL_LINE_OFFSET_Y;

		overContent.saveState();

		// seteo el tipo de fuente.
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC, BaseFont.WINANSI, false);
			overContent.setFontAndSize(bf, deliveryNoteConfig.getFontSize());
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (outputDetails.size()) {
			case 1: {
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn1X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn1Print() ? outputDetails.get(0).getSerialNumber() : "");
				break;
			}
			case 2: {
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn1X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn1Print() ? outputDetails.get(0).getSerialNumber() : "");
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn2X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn2Print() ? outputDetails.get(1).getSerialNumber() : "");
				break;
			}
			case 3: {
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn1X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn1Print() ? outputDetails.get(0).getSerialNumber() : "");
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn2X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn2Print() ? outputDetails.get(1).getSerialNumber() : "");
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn3X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn3Print() ? outputDetails.get(2).getSerialNumber() : "");
				break;
			}
			case 4: {
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn1X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn1Print() ? outputDetails.get(0).getSerialNumber() : "");
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn2X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn2Print() ? outputDetails.get(1).getSerialNumber() : "");
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn3X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn3Print() ? outputDetails.get(2).getSerialNumber() : "");
				overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn4X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
				overContent.showText(deliveryNoteConfig.isSerialColumn4Print() ? outputDetails.get(3).getSerialNumber() : "");
				break;
			}
			default: {
				break;
			}
		}

		overContent.restoreState();
	}

	private void printFooter(int amount) {
		overContent.saveState();

		// seteo el tipo de fuente.
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
			overContent.setFontAndSize(bf, deliveryNoteConfig.getFontSize());
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// imprimo Cantidad de Items para el remito.
		overContent.setTextMatrix(deliveryNoteConfig.getNumberOfItemsX(), deliveryNoteConfig.getNumberOfItemsY());
		overContent.showText(deliveryNoteConfig.isNumberOfItemsPrint() ? ("Items: " + amount) : "");

		overContent.restoreState();
	}

	private TreeMap<String, List<OutputDetail>> groupByProductAndBatch(Output output) {
		TreeMap<String, List<OutputDetail>> details = new TreeMap<>();

		for(OutputDetail outputDetail: output.getOutputDetails()){
			String id = Integer.toString(outputDetail.getProduct().getId());
			String batch = outputDetail.getBatch();
			String key = id + "," + batch;

			List<OutputDetail> list = details.get(key);
			if(list == null) {
				list = new ArrayList<>();
			}
			list.add(outputDetail);
			details.put(key, list);
		}

		return details;
	}

    private int getProductTotalAmount(int productId) {
        HashMap<Integer, Integer> details = new HashMap<>();

        for(OutputDetail outputDetail: output.getOutputDetails()){
            Integer id = outputDetail.getProduct().getId();

            Integer currentAmount = details.get(id);
            if(currentAmount == null) {
                currentAmount = new Integer(0);
            }
            currentAmount += outputDetail.getAmount();
            details.put(id, currentAmount);
        }

        return details.get(productId);
    }

    private int numberOfLinesNeeded(TreeMap<String, List<OutputDetail>> orderMap) {
		int numberOfLines = 0;

		for(List<OutputDetail> outputDetail: orderMap.values()){
			String type = outputDetail.get(0).getProduct().getType();
			if (type.compareTo("BE") == 0) {
				numberOfLines += outputDetail.size();
			} else {
				numberOfLines += Math.ceil((double)outputDetail.size() / 4);
			}
		}

		return numberOfLines;
	}
}