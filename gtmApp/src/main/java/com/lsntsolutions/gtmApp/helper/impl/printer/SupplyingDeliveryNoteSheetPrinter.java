package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.constant.State;
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

/**
 * Created by Lucho on 05/11/2015.
 */
@Service
public class SupplyingDeliveryNoteSheetPrinter implements DeliveryNoteSheetPrinter{

    private static final Logger logger = Logger.getLogger(SupplyingDeliveryNoteSheetPrinter.class);

    private static int PRODUCT_DETAIL_HEADER_LINE_OFFSET_Y = 30;
    private static int PRODUCT_BATCH_EXPIRATIONDATE_HEADER_LINE_OFFSET_Y = 20;
    private static int SERIAL_DETAIL_LINE_OFFSET_Y = 20;

    @Autowired
    private com.lsntsolutions.gtmApp.service.PropertyService PropertyService;
    @Autowired
    private SupplyingService supplyingService;
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
    private DeliveryNoteDetail deliveryNoteDetail;
    private List<String> printsNumbers;
    private String userName;

    @Override
    public List<String> print(String userName, List<Integer> supplyingsIds) {
        this.userName = userName;
        printsNumbers = new ArrayList<>();
        currentDate = new Date();
        for (Integer id : supplyingsIds) {
            supplying = this.supplyingService.get(id);
            Integer numberOfDeliveryNoteDetailsPerPage = supplying.getAgreement().getNumberOfDeliveryNoteDetailsPerPage();

            // agrupo lista de productos por id de producto + lote.
            HashMap<String, List<SupplyingDetail>> orderMap = groupByProduct(supplying);
            // calculo cuantas lineas de detalles de productos voy a necesitar.
            int numberOfLinesNeeded = numberOfLinesNeeded(orderMap);
            // calculo cuantos remitos voy a necesitar en base a la cantidad de detalles de productos.
            int deliveryNoteNumbersRequired = (int)Math.ceil((float)numberOfLinesNeeded / numberOfDeliveryNoteDetailsPerPage);

            Integer conceptId = supplying.getAgreement().getDeliveryNoteConcept().getId();
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
            // recorro el mapa de detalle de productos a imprimir.
            for (Map.Entry<String, List<SupplyingDetail>> entry : orderMap.entrySet()) {
                String key = entry.getKey();
                List<SupplyingDetail> supplyingDetails = entry.getValue();
                String[] parts = key.split(",");
            /*String productId = parts[0];
            String batch = parts[1];*/
                String type = supplyingDetails.get(0).getProduct().getType();

                // si ya esta lleno el remito, sigo en uno nuevo
                if (currentLine == numberOfDeliveryNoteDetailsPerPage) {
                    savePage();
                    newPage();
                }

                // si es de tipo lote/ vto ocupa 1 (una) sola linea.
                if (type.compareTo("BE") == 0) {
                    String description = supplyingDetails.get(0).getProduct().getDescription();
                    String monodrug = supplyingDetails.get(0).getProduct().getMonodrug().getDescription();
                    String brand = supplyingDetails.get(0).getProduct().getBrand().getDescription();
                    int totalAmount = supplyingDetails.get(0).getAmount();

                    printProductDetailHeader(description, monodrug, brand, totalAmount);
                    printProductBatchExpirationDateHeader(supplyingDetails.get(0));

                    deliveryNoteDetail = new DeliveryNoteDetail();
                    deliveryNoteDetail.setSupplyingDetail(supplyingDetails.get(0));
                    deliveryNoteDetails.add(deliveryNoteDetail);

                    currentLine++;
                    totalItems += totalAmount;
                    // si es de tipo trazado ocupa 1 (una) sola linea por cada cuatro series.
                } else {
                    if (currentLine == numberOfDeliveryNoteDetailsPerPage) {
                        savePage();
                        newPage();
                    }
                    List<SupplyingDetail> temp = new ArrayList<>();
                    Iterator<SupplyingDetail> it = supplyingDetails.iterator();
                    int serialIdx = 0;
                    String description = supplyingDetails.get(0).getProduct().getDescription();
                    String monodrug = supplyingDetails.get(0).getProduct().getMonodrug().getDescription();
                    String brand = supplyingDetails.get(0).getProduct().getBrand().getDescription();
                    int totalAmount = supplyingDetails.size();
                    printProductDetailHeader(description, monodrug, brand, totalAmount);
                    printProductBatchExpirationDateHeader(supplyingDetails.get(0));
                    while (it.hasNext()) {
                        SupplyingDetail od = it.next();
                        temp.add(od);

                        deliveryNoteDetail = new DeliveryNoteDetail();
                        deliveryNoteDetail.setSupplyingDetail(od);
                        deliveryNoteDetails.add(deliveryNoteDetail);

                        serialIdx++;

                        if ((serialIdx == 4) || (!it.hasNext())) {
                            totalItems += temp.size();
                            printSerialDetails(temp);
                            currentLine++;
                            serialIdx = 0;
                            temp = new ArrayList<>();
                        }
                    }
                }
            }

            printFooter(totalItems);

            savePage();

            document.close();

            ByteArrayInputStream pdfDocument = new ByteArrayInputStream(out.toByteArray());

            this.printOnPrinter.sendPDFToSpool(supplying.getAgreement().getDeliveryNoteFilepath(), "REMITO NRO-" + deliveryNoteNumber + ".pdf", pdfDocument);

            try {
                pdfDocument.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return printsNumbers;
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
            if (supplying.hasToInform() && supplying.getAgreement().getDeliveryNoteConcept().isInformAnmat()) {
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
        overContent.showText(deliveryNoteConfig.isDatePrint() ? new SimpleDateFormat("dd/MM/yyyy").format(supplying.getDate()) : "");

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
        // TODO FALTA ESTE DATO
    /*overContent.setTextMatrix(deliveryNoteConfig.getIssuerVatliabilityX(), deliveryNoteConfig.getIssuerVatliabilityY());
    overContent.showText(deliveryNoteConfig.isIssuerVatliabilityPrint() ? property.getVATLiability().getDescription().toUpperCase() : "");*/

        // imprimo CUIT.
        overContent.setTextMatrix(deliveryNoteConfig.getIssuerTaxX(), deliveryNoteConfig.getIssuerTaxY());
        overContent.showText(deliveryNoteConfig.isIssuerTaxPrint() ? property.getTaxId() : "");

        // imprimo egreso.
        overContent.setTextMatrix(deliveryNoteConfig.getOrderX(), deliveryNoteConfig.getOrderY());
        overContent.showText(deliveryNoteConfig.isOrderPrint() ? ("Pedido: " + deliveryNoteNumber + "  Egreso: " + supplying.getAgreement().getCode() + "-" + supplying.getAgreement().getDescription()) : "");
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
            overContent.showText(deliveryNoteConfig.isDeliveryLocationVatliabilityPrint() ? supplying.getDeliveryLocation().getVATLiability().getDescription() : "");
        } else {
            overContent.showText(deliveryNoteConfig.isDeliveryLocationVatliabilityPrint() ? supplying.getProvider().getVATLiability().getDescription() : "");
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
        overContent.saveState();

        // offset con respecto a la linea anterior.
        offsetY += PRODUCT_DETAIL_HEADER_LINE_OFFSET_Y;

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
    }

    private void printProductBatchExpirationDateHeader(SupplyingDetail supplyingDetail) {
        overContent.saveState();

        // offset con respecto a la linea anterior.
        offsetY += PRODUCT_BATCH_EXPIRATIONDATE_HEADER_LINE_OFFSET_Y;

        // seteo el tipo de fuente.
        try {
            bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false);
            overContent.setFontAndSize(bf, deliveryNoteConfig.getFontSize());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String batch = supplyingDetail.getBatch();
        String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(supplyingDetail.getExpirationDate());

        // imprimo lote.
        overContent.setTextMatrix(deliveryNoteConfig.getProductBatchExpirationdateX(), deliveryNoteConfig.getProductDetailsY() - offsetY);
        overContent.showText(deliveryNoteConfig.isProductBatchExpirationdatePrint() ? ("Lote: " + batch) : "");

        // imprimo vencimiento.
        overContent.setTextMatrix(deliveryNoteConfig.getProductBatchExpirationdateX() + 120, deliveryNoteConfig.getProductDetailsY() - offsetY);
        overContent.showText(deliveryNoteConfig.isProductBatchExpirationdatePrint() ? ("Vto.: " + expirationDate) : "");

        overContent.restoreState();
    }

    private void printSerialDetails(List<SupplyingDetail> supplyingDetail) {
        overContent.saveState();

        // offset con respecto a la linea anterior.
        offsetY += SERIAL_DETAIL_LINE_OFFSET_Y;

        // seteo el tipo de fuente.
        try {
            bf = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC, BaseFont.WINANSI, false);
            overContent.setFontAndSize(bf, deliveryNoteConfig.getFontSize());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (supplyingDetail.size()) {
            case 1: {
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn1X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn1Print() ? supplyingDetail.get(0).getSerialNumber() : "");
                break;
            }
            case 2: {
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn1X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn1Print() ? supplyingDetail.get(0).getSerialNumber() : "");
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn2X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn2Print() ? supplyingDetail.get(1).getSerialNumber() : "");
                break;
            }
            case 3: {
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn1X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn1Print() ? supplyingDetail.get(0).getSerialNumber() : "");
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn2X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn2Print() ? supplyingDetail.get(1).getSerialNumber() : "");
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn3X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn3Print() ? supplyingDetail.get(2).getSerialNumber() : "");
                break;
            }
            case 4: {
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn1X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn1Print() ? supplyingDetail.get(0).getSerialNumber() : "");
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn2X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn2Print() ? supplyingDetail.get(1).getSerialNumber() : "");
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn3X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn3Print() ? supplyingDetail.get(2).getSerialNumber() : "");
                overContent.setTextMatrix(deliveryNoteConfig.getSerialColumn4X(), deliveryNoteConfig.getProductDetailsY() - offsetY);
                overContent.showText(deliveryNoteConfig.isSerialColumn4Print() ? supplyingDetail.get(3).getSerialNumber() : "");
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

    private HashMap<String, List<SupplyingDetail>> groupByProduct(Supplying supplying) {
        HashMap<String, List<SupplyingDetail>> details = new HashMap<>();

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

    private int numberOfLinesNeeded(HashMap<String, List<SupplyingDetail>> orderMap) {
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

