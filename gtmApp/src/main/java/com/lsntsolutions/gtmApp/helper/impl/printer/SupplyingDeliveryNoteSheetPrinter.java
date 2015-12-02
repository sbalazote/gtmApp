package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.DeliveryNoteConfigParam;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.helper.DeliveryNoteSheetPrinter;
import com.lsntsolutions.gtmApp.helper.PrintOnPrinter;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Lucho on 05/11/2015.
 */
@Service
public class SupplyingDeliveryNoteSheetPrinter implements DeliveryNoteSheetPrinter {

    private static final Logger logger = Logger.getLogger(SupplyingDeliveryNoteSheetPrinter.class);

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private SupplyingService supplyingService;
    @Autowired
    private DeliveryNoteService deliveryNoteService;
    @Autowired
    private PrintOnPrinter printOnPrinter;
    @Autowired
    private ConceptService conceptService;
    @Autowired
    private DeliveryNoteConfigService deliveryNoteConfigService;
    @Autowired
    private AuditService auditService;

    private Supplying supplying;
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
    private List<String> printsNumbers;
    private String userName;
    private boolean printHeader;
    private Map<String, Float> dnConfigMap;

    @Override
    public void print(String userName, List<Integer> supplyingsIds, PrinterResultDTO printerResultDTO) {
        dnConfigMap = deliveryNoteConfigService.getAllInMillimiters();
        this.userName = userName;
        printsNumbers = new ArrayList<>();
        currentDate = new Date();
        for (Integer id : supplyingsIds) {
            supplying = this.supplyingService.get(id);
            Integer numberOfDeliveryNoteDetailsPerPage = supplying.getAgreement().getNumberOfDeliveryNoteDetailsPerPage();

            // agrupo lista de productos por id de producto + lote.
            TreeMap<String, List<SupplyingDetail>> orderMap = groupByProductAndBatch(supplying);
            // calculo cuantas lineas de detalles de productos voy a necesitar.
            int numberOfLinesNeeded = numberOfLinesNeeded(orderMap);
            // calculo cuantos remitos voy a necesitar en base a la cantidad de detalles de productos.
            int deliveryNoteNumbersRequired = (int)Math.ceil((float)numberOfLinesNeeded / numberOfDeliveryNoteDetailsPerPage);

            property = this.propertyService.get();
            Concept concept = this.conceptService.getAndUpdateDeliveryNote(property.getSupplyingConcept().getId(), deliveryNoteNumbersRequired);
            POS = StringUtility.addLeadingZeros(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4);
            deliveryNoteNumber = concept.getDeliveryNoteEnumerator().getDeliveryNoteNumber() - deliveryNoteNumbersRequired;

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

            deliveryNoteDetails = new ArrayList<>();

            // numero de linea de detalle de producto actual.
            int currentLine = 0;
            // offset coordenada vertical a partir de donde se indico el detalle de productos.
            offsetY = 0;
            // cantidad de items totales para esta pagina de remito.
            totalItems = 0;
            // id de producto en la iteracion anterior.
            String previousProductId = "";
            // recorro el mapa de detalle de productos a imprimir.
            for (Map.Entry<String, List<SupplyingDetail>> entry : orderMap.entrySet()) {
                String key = entry.getKey();
                List<SupplyingDetail> supplyingDetails = entry.getValue();
                String[] parts = key.split(",");
                String currentProductId = parts[0];
                String productType = supplyingDetails.get(0).getProduct().getType();

                // si ya esta lleno el remito, sigo en uno nuevo
                if (currentLine == numberOfDeliveryNoteDetailsPerPage) {
                    savePage();
                    newPage();
                }

                String description = supplyingDetails.get(0).getProduct().getDescription();
                String monodrug = supplyingDetails.get(0).getProduct().getMonodrug().getDescription();
                String brand = supplyingDetails.get(0).getProduct().getBrand().getDescription();
                int totalAmount = supplyingDetails.get(0).getAmount();
                String batch = supplyingDetails.get(0).getBatch();
                String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(supplyingDetails.get(0).getExpirationDate());
                String batchAmount = Integer.toString(productType.equals("BE") ? totalAmount : supplyingDetails.size());
                if (!currentProductId.equals(previousProductId)) {
                    totalItems++;
                    printProductDetailHeader(description, monodrug, brand, getProductTotalAmount(Integer.parseInt(currentProductId)));
                }
                printProductBatchExpirationDateHeader(batch, expirationDate, batchAmount);

                // si es de tipo lote/ vto ocupa 1 (una) sola linea.
                DeliveryNoteDetail deliveryNoteDetail;
                if (productType.equals("BE")) {

                    deliveryNoteDetail = new DeliveryNoteDetail();
                    deliveryNoteDetail.setSupplyingDetail(supplyingDetails.get(0));
                    deliveryNoteDetails.add(deliveryNoteDetail);

                    currentLine++;
                    // si es de tipo trazado ocupa 1 (una) sola linea por cada cuatro series.
                } else {
                    List<SupplyingDetail> supplyingDetailAux = new ArrayList<>();
                    Iterator<SupplyingDetail> it = supplyingDetails.iterator();
                    int serialIdx = 0;
                    while (it.hasNext()) {
                        SupplyingDetail od = it.next();
                        supplyingDetailAux.add(od);

                        deliveryNoteDetail = new DeliveryNoteDetail();
                        deliveryNoteDetail.setSupplyingDetail(od);
                        deliveryNoteDetails.add(deliveryNoteDetail);

                        serialIdx++;

                        if ((serialIdx == 4) || (!it.hasNext())) {
                            printSerialDetails(supplyingDetailAux);
                            currentLine++;
                            serialIdx = 0;
                            supplyingDetailAux = new ArrayList<>();
                        }
                    }
                }
                previousProductId = currentProductId;
            }

            printFooter(totalItems);

            savePage();

            document.close();

            for(int i = 0; i < concept.getDeliveryNoteCopies(); i++) {
                ByteArrayInputStream pdfDocument = new ByteArrayInputStream(out.toByteArray());
                this.printOnPrinter.sendPDFToSpool(supplying.getAgreement().getDeliveryNotePrinter(), "REMITO NRO-" + deliveryNoteNumber + ".pdf", pdfDocument, printerResultDTO);
                try {
                    pdfDocument.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        deliveryNoteDetails = new ArrayList<>();
    }

    private void savePage() {
        // Guardo el Remito en la base de datos
        deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
        deliveryNote.setDate(currentDate);
        deliveryNote.setFake(false);
        try {
            if (supplying.hasToInform() && supplying.getAgreement().getDeliveryNoteConcept().isInformAnmat() && propertyService.get().isInformAnmat()) {
                deliveryNote.setInformAnmat(true);
            } else {
                deliveryNote.setInformAnmat(false);
            }
            this.deliveryNoteService.save(deliveryNote);
            this.deliveryNoteService.sendTrasactionAsync(deliveryNote);
        } catch (Exception e1) {
            logger.error("No se ha podido guardar el remito: " + deliveryNoteNumber + " para el Egreso número: " + supplying.getId());
        }

        logger.info("Se ha guardado el remito numero: " + deliveryNoteNumber + " para el Egreso número: " + supplying.getId());
        this.auditService.addAudit(this.userName, RoleOperation.DELIVERY_NOTE_PRINT.getId(), AuditState.COMFIRMED, deliveryNote.getId());


        printsNumbers.add(deliveryNote.getNumber());
        deliveryNoteNumber++;
    }

    private void printHeader(Integer deliveryNoteNumber) {
        overContent.saveState();

        // seteo el tipo de fuente.
        try {
            bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
            overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // imprimo numero de remito.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.NUMBER_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.NUMBER_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.NUMBER_PRINT.name()) == 1 ? StringUtility.addLeadingZeros(deliveryNoteNumber, 8): "");

        // imprimo fecha.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DATE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DATE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DATE_PRINT.name()) == 1 ? new SimpleDateFormat("dd/MM/yyyy").format(supplying.getDate()) : "");

        // imprimo cliente.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_PRINT.name()) == 1 ? property.getCorporateName() : "");

        // imprimo domicilio.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ADDRESS_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ADDRESS_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ADDRESS_PRINT.name()) == 1 ? property.getAddress() : "");

        // imprimo localidad.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_LOCALITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_LOCALITY_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_LOCALITY_PRINT.name()) == 1 ? property.getLocality() : "");

        // imprimo cod. postal.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ZIPCODE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ZIPCODE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ZIPCODE_PRINT.name()) == 1 ? ("(" + property.getZipCode() + ")") : "");

        // imprimo provincia.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_PROVINCE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_PROVINCE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_PROVINCE_PRINT.name()) == 1 ? property.getProvince().getName() : "");

        // imprimo condicion IVA.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_PRINT.name()) == 1 ? property.getVATLiability().getDescription().toUpperCase() : "");

        // imprimo CUIT.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_TAX_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_TAX_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_TAX_PRINT.name()) == 1 ? property.getTaxId() : "");

        // imprimo egreso.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ORDER_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ORDER_Y.name()));
        String supplyingId = StringUtility.addLeadingZeros(supplying.getId().toString(), 8);
        String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(supplying.getAgreement().getCode()),5) + " - " + supplying.getAgreement().getDescription();
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ORDER_PRINT.name()) == 1 ? ("Dispensa Nro.: " + supplyingId + "     Convenio: " + codeAgreement) : "");
/*
        // imprimo cliente entrega.
        overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationCorporateNameX(), deliveryNoteConfig.getDeliveryLocationCorporateNameY());
        if (supplying.getDeliveryLocation() != null) {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationCorporateNamePrint() ? supplying.getDeliveryLocation().getCorporateName() : "");
        } else {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationCorporateNamePrint() ? supplying.getProvider().getCorporateName() : "");
        }

        // imprimo domicilio entrega.
        overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationAddressX(), deliveryNoteConfig.getDeliveryLocationAddressY());
        if (supplying.getDeliveryLocation() != null) {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationAddressPrint() ? supplying.getDeliveryLocation().getCorporateName() : "");
        } else {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationAddressPrint() ? supplying.getProvider().getCorporateName() : "");
        }

        // imprimo localidad entrega.
        overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationLocalityX(), deliveryNoteConfig.getDeliveryLocationLocalityY());
        if (supplying.getDeliveryLocation() != null) {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationLocalityPrint() ? supplying.getDeliveryLocation().getLocality() : "");
        } else {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationLocalityPrint() ? supplying.getProvider().getLocality() : "");
        }

        // imprimo cod. postal entrega.
        overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationZipcodeX(), deliveryNoteConfig.getDeliveryLocationZipcodeY());
        if (supplying.getDeliveryLocation() != null) {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationZipcodePrint() ? ("(" + supplying.getDeliveryLocation().getZipCode() + ")") : "");
        } else {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationZipcodePrint() ? ("(" + supplying.getProvider().getZipCode() + ")") : "");
        }

        // imprimo provincia entrega.
        overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationProvinceX(), deliveryNoteConfig.getDeliveryLocationProvinceY());
        if (supplying.getDeliveryLocation() != null) {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationProvincePrint() ? supplying.getDeliveryLocation().getProvince().getName() : "");
        } else {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationProvincePrint() ? supplying.getProvider().getProvince().getName() : "");
        }

        // imprimo condicion IVA entrega.
        overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationVatliabilityX(), deliveryNoteConfig.getDeliveryLocationVatliabilityY());
        if (supplying.getDeliveryLocation() != null) {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationVatliabilityPrint() ? supplying.getDeliveryLocation().getVATLiability().getDescription().toUpperCase() : "");
        } else {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationVatliabilityPrint() ? supplying.getProvider().getVATLiability().getDescription().toUpperCase() : "");
        }

        // imprimo CUIT entrega.
        overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationTaxX(), deliveryNoteConfig.getDeliveryLocationTaxY());
        if (supplying.getDeliveryLocation() != null) {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationTaxPrint() ? supplying.getDeliveryLocation().getTaxId() : "");
        } else {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationTaxPrint() ? supplying.getProvider().getTaxId() : "");
        }

        // imprimo GLN Origen
        overContent.setTextMatrix(deliveryNoteConfig.getIssuerGlnX(), deliveryNoteConfig.getIssuerGlnY());
        overContent.showText(deliveryNoteConfig.isIssuerGlnPrint() ? ("GLN Origen: " + property.getGln()) : "");

        // imprimo GLN Destino
        overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationGlnX(), deliveryNoteConfig.getDeliveryLocationGlnY());
        if (supplying.getDeliveryLocation() != null) {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationGlnPrint() ? ("GLN Destino: " + supplying.getDeliveryLocation().getGln()) : "");
        } else {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationGlnPrint() ? ("GLN Destino: " + supplying.getProvider().getGln()) : "");
        }
*/
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
            overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // imprimo descripcion.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DESCRIPTION_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DESCRIPTION_PRINT.name()) == 1 ? description : "");

        // imprimo monodraga.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_MONODRUG_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_MONODRUG_PRINT.name()) == 1 ? monodrug : "");

        // imprimo marca.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BRAND_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BRAND_PRINT.name()) == 1 ? brand : "");

        // imprimo cantidad total.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_PRINT.name()) == 1 ? Integer.toString(totalAmount) : "");

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
            overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // imprimo lote.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_PRINT.name()) == 1 ? ("Lote: " + batch) : "");

        // imprimo vencimiento.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_X.name()) + 120, dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_PRINT.name()) == 1 ? ("Vto.: " + expirationDate) : "");

        // imprimo cantidad total del lote.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_PRINT.name()) == 1 ? batchAmount : "");

        overContent.restoreState();
    }

    private void printSerialDetails(List<SupplyingDetail> supplyingDetails) {

        // offset con respecto a la linea anterior.
        int SERIAL_DETAIL_LINE_OFFSET_Y = 10;
        offsetY += SERIAL_DETAIL_LINE_OFFSET_Y;

        overContent.saveState();

        // seteo el tipo de fuente.
        try {
            bf = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC, BaseFont.WINANSI, false);
            overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (supplyingDetails.size()) {
            case 1: {
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? supplyingDetails.get(0).getSerialNumber() : "");
                break;
            }
            case 2: {
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? supplyingDetails.get(0).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()) == 1 ? supplyingDetails.get(1).getSerialNumber() : "");
                break;
            }
            case 3: {
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? supplyingDetails.get(0).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()) == 1 ? supplyingDetails.get(1).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_PRINT.name()) == 1 ? supplyingDetails.get(2).getSerialNumber() : "");
                break;
            }
            case 4: {
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? supplyingDetails.get(0).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()) == 1 ? supplyingDetails.get(1).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_PRINT.name()) == 1 ? supplyingDetails.get(2).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN4_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN4_PRINT.name()) == 1 ? supplyingDetails.get(3).getSerialNumber() : "");
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
            overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // imprimo Cantidad de Items para el remito.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.NUMBEROFITEMS_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.NUMBEROFITEMS_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.NUMBEROFITEMS_PRINT.name()) == 1 ? ("Items: " + amount) : "");

        overContent.restoreState();
    }

    private TreeMap<String, List<SupplyingDetail>> groupByProductAndBatch(Supplying supplying) {
        TreeMap<String, List<SupplyingDetail>> details = new TreeMap<>();

        for(SupplyingDetail supplyingDetail : supplying.getSupplyingDetails()){
            String id = Integer.toString(supplyingDetail.getProduct().getId());
            String batch = supplyingDetail.getBatch();
            String key = id + "," + batch;

            List<SupplyingDetail> list = details.get(key);
            if(list == null) {
                list = new ArrayList<>();
            }
            list.add(supplyingDetail);
            details.put(key, list);
        }

        return details;
    }

    private int getProductTotalAmount(int productId) {
        HashMap<Integer, Integer> details = new HashMap<>();

        for(SupplyingDetail supplyingDetail: supplying.getSupplyingDetails()){
            Integer id = supplyingDetail.getProduct().getId();

            Integer currentAmount = details.get(id);
            if(currentAmount == null) {
                currentAmount = new Integer(0);
            }
            currentAmount += supplyingDetail.getAmount();
            details.put(id, currentAmount);
        }

        return details.get(productId);
    }

    private int numberOfLinesNeeded(TreeMap<String, List<SupplyingDetail>> orderMap) {
        int numberOfLines = 0;

        for(List<SupplyingDetail> supplyingDetail : orderMap.values()){
            String type = supplyingDetail.get(0).getProduct().getType();
            if (type.compareTo("BE") == 0) {
                numberOfLines += supplyingDetail.size();
            } else {
                numberOfLines += Math.ceil((double) supplyingDetail.size() / 4);
            }
        }

        return numberOfLines;
    }
}

