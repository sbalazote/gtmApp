package com.lsntsolutions.gtmApp.helper;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.*;
import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.DeliveryNoteConfigParam;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
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

@Service
public class DeliveryNoteSheetPrinterImpl implements DeliveryNoteSheetPrinter{

    private static final Logger logger = Logger.getLogger(DeliveryNoteSheetPrinterImpl.class);

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private OutputService outputService;
    @Autowired
    private OrderService orderService;
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
    @Autowired
    private SupplyingService supplyingService;
    @Autowired
    private ProvisioningRequestStateService provisioningRequestStateService;
    @Autowired
    private ProvisioningRequestService provisioningRequestService;

    private Document document;
    private ByteArrayOutputStream out;
    private PdfWriter writer;
    private int totalItems;
    private Date currentDate;
    private List<DeliveryNote> deliveryNoteList;
    private DeliveryNote deliveryNote;
    private List<DeliveryNoteDetail> deliveryNoteDetails;
    private List<String> printsNumbers;
    private String userName;
    private Property property;
    private int offsetY;
    private PdfContentByte overContent;
    private BaseFont bf;
    private Map<String, Float> dnConfigMap;
    private boolean printHeader;
    private Concept deliveryNoteConcept;
    private Integer deliveryNoteNumbersRequired;
    int PRODUCT_DETAIL_HEADER_LINE_OFFSET_Y = 10;
    int SERIAL_DETAIL_LINE_OFFSET_Y = 10;
    int PRODUCT_BATCH_EXPIRATIONDATE_HEADER_LINE_OFFSET_Y = 10;
    private boolean printSupplyings;
    private boolean printOutputs;
    private boolean printOrders;

    public TreeMap<String, List<? extends Detail>> groupByProductAndBatch(List<?extends Detail> details) {
        TreeMap<String, List<? extends Detail>> detailsMap = new TreeMap<>();

        for(Detail detail : details){
            String id = Integer.toString(detail.getProduct().getId());
            String batch = detail.getBatch();
            String key = id + "," + batch;

            List<Detail> list = (List<Detail>) detailsMap.get(key);
            if(list == null) {
                list = new ArrayList<>();
            }
            list.add(detail);
            detailsMap.put(key, list);
        }

        return detailsMap;
    }

    public TreeMap<String, Integer> countByProduct(List<?extends Detail> details) {
        TreeMap<String, Integer> detailsMap = new TreeMap<>();

        for(Detail detail : details){
            String id = Integer.toString(detail.getProduct().getId());

            Integer amount = detailsMap.get(id);
            if(amount == null) {
                amount = new Integer(0);
            }
            amount += detail.getAmount();
            detailsMap.put(id, amount);
        }

        return detailsMap;
    }

    public void printFooter(int amount) {
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

    public void printSerialDetails(List<? extends Detail> orderDetails) {

        // offset con respecto a la linea anterior.
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

        switch (orderDetails.size()) {
            case 1: {
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? orderDetails.get(0).getSerialNumber() : "");
                break;
            }
            case 2: {
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? orderDetails.get(0).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()) == 1 ? orderDetails.get(1).getSerialNumber() : "");
                break;
            }
            case 3: {
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? orderDetails.get(0).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()) == 1 ? orderDetails.get(1).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_PRINT.name()) == 1 ? orderDetails.get(2).getSerialNumber() : "");
                break;
            }
            case 4: {
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? orderDetails.get(0).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()) == 1 ? orderDetails.get(1).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_PRINT.name()) == 1 ? orderDetails.get(2).getSerialNumber() : "");
                overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN4_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
                overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN4_PRINT.name()) == 1 ? orderDetails.get(3).getSerialNumber() : "");
                break;
            }
            default: {
                break;
            }
        }

        overContent.restoreState();
    }

    public void printProductDetailHeader(String description, String monodrug, String brand, int totalAmount) {

        // offset con respecto a la linea anterior.
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

    public void printProductGtinBatchExpirationDateHeader(String gtin, String batch, String expirationDate, String batchAmount) {

        // offset con respecto a la linea anterior.
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

        // imprimo GTIN.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_PRINT.name()) == 1 ? ("GTIN: " + gtin) : "");

        // imprimo lote.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_X.name()) + 120, dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_PRINT.name()) == 1 ? ("Lote: " + batch) : "");

        // imprimo vencimiento.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_X.name()) + 240, dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_PRINT.name()) == 1 ? ("Vto.: " + expirationDate) : "");

        // imprimo cantidad total del lote.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_PRINT.name()) == 1 ? batchAmount : "");

        overContent.restoreState();
    }


    @Override
    public void print(String userName, List<Integer> egressIds, PrinterResultDTO printerResultDTO, boolean printSupplyings, boolean printOutputs, boolean printOrders) {
        this.printSupplyings = printSupplyings;
        this.printOutputs = printOutputs;
        this.printOrders = printOrders;
        deliveryNoteList = new ArrayList<DeliveryNote>();
        dnConfigMap = deliveryNoteConfigService.getAllInMillimiters();
        property = this.propertyService.get();
        this.userName = userName;
        printsNumbers = new ArrayList<>();
        ProvisioningRequestState state = this.provisioningRequestStateService.get(State.DELIVERY_NOTE_PRINTED.getId());
        currentDate = new Date();
        for (Integer id : egressIds) {
            Egress egress = getEgress(id, printSupplyings, printOutputs, printOrders);
            Integer numberOfDeliveryNoteDetailsPerPage = egress.getAgreement().getNumberOfDeliveryNoteDetailsPerPage();

            // agrupo lista de productos por id de producto + lote.
            TreeMap<String, List<? extends Detail>> productMap = groupByProductAndBatch(egress.getDetails());

            TreeMap<String, Integer> productsCount = countByProduct(egress.getDetails());

            document = new Document(PageSize.A4);
            out = new ByteArrayOutputStream();
            try {
                writer = PdfWriter.getInstance(document, out);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            document.addCreationDate();
            document.addCreator("LS&T Solutions");
            document.open();
            overContent = writer.getDirectContent();
            overContent.setLeading(0f);

            printHeader(egress);

            deliveryNote = new DeliveryNote();
            deliveryNoteDetails = new ArrayList<>();
            // numero de remitos requeridos
            deliveryNoteNumbersRequired = 0;
            // numero de linea de detalle de producto actual.
            int currentLine = 0;
            // offset coordenada vertical a partir de donde se indico el detalle de productos.
            offsetY = 0;
            // cantidad de items totales para esta pagina de remito.
            totalItems = 0;
            // id de producto en la iteracion anterior.
            String previousProductId = "";
            // recorro el mapa de detalle de productos a imprimir.
            for (Map.Entry<String, List<? extends Detail>> entry : productMap.entrySet()) {
                String key = entry.getKey();
                List<? extends Detail> details = entry.getValue();
                String[] parts = key.split(",");
                String currentProductId = parts[0];
                String productType = details.get(0).getProduct().getType();

                String description = details.get(0).getProduct().getDescription();
                String monodrug = details.get(0).getProduct().getMonodrug().getDescription();
                String brand = details.get(0).getProduct().getBrand().getDescription();
                int totalAmount = details.get(0).getAmount();
                ProductGtin productGtin = details.get(0).getGtin();
                String gtin = (productGtin != null) ? productGtin.getNumber() : "";
                String batch = details.get(0).getBatch();
                String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(details.get(0).getExpirationDate());
                String batchAmount = Integer.toString(productType.equals("BE") ? totalAmount : details.size());

                // si el id de producto actual es distinto del anterior//sino, significa que es el mismo producto pero otro lote
                if (!currentProductId.equals(previousProductId)) {
                    currentLine = checkNewPage(egress, numberOfDeliveryNoteDetailsPerPage, currentLine, false, productType.equals("BE") ? true : false);
                    totalItems++;
                    printProductDetailHeader(description, monodrug, brand, productsCount.get(currentProductId));
                    currentLine++;

                } else {
                    currentLine = checkNewPage(egress, numberOfDeliveryNoteDetailsPerPage, currentLine, true, productType.equals("BE") ? true : false);
                    // si es el mismo producto con otro lote, pero hubo corte de hoja, tengo que poner de vuelta el detalle del producto en la nueva.
                    if (currentLine == 0) {
                        totalItems++;
                        printProductDetailHeader(description, monodrug, brand, productsCount.get(currentProductId));
                        currentLine++;
                    }
                }
                printProductGtinBatchExpirationDateHeader(gtin, batch, expirationDate, batchAmount);
                // si es de tipo lote/ vto voy a necesitar 2 (dos) lineas. 1 para el nombre del producto y otro para el lote.
                currentLine++;
                DeliveryNoteDetail deliveryNoteDetail;
                if (productType.equals("BE")) {
                    deliveryNoteDetail = new DeliveryNoteDetail();
                    setDeliveryNoteDetail(details.get(0), deliveryNoteDetail);
                    deliveryNoteDetails.add(deliveryNoteDetail);
                    // si es de tipo trazado ocupa 1 (una) sola linea por cada cuatro series.
                } else {
                    List<Detail> detailAux = new ArrayList<>();
                    Iterator<? extends Detail> it = details.iterator();
                    int serialIdx = 0;
                    while (it.hasNext()) {
                        Detail od = it.next();
                        detailAux.add(od);

                        deliveryNoteDetail = new DeliveryNoteDetail();
                        setDeliveryNoteDetail(od, deliveryNoteDetail);
                        deliveryNoteDetails.add(deliveryNoteDetail);

                        serialIdx++;

                        if ((serialIdx == 4) || (!it.hasNext())) {
                            printSerialDetails(detailAux);
                            currentLine++;
                            serialIdx = 0;
                            detailAux = new ArrayList<>();
                            currentLine = checkNewPageMoreSerials(egress, numberOfDeliveryNoteDetailsPerPage, currentLine);
                        }
                    }
                }
                previousProductId = currentProductId;
            }

            printFooter(totalItems);
            // pido los numeros necesarios a la base, los asigno e imprimo los remitos.
            try {
                deliveryNoteConcept = getConcept(egress, deliveryNoteNumbersRequired);
            } catch (Exception e) {
                logger.error("No se ha podido obtener el Concepto");
                e.printStackTrace();
            }
            savePage(egress);

            String POS = StringUtility.addLeadingZeros(deliveryNoteConcept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4);
            Integer currentDeliveryNoteNumber, currentDeliveryNoteNumberCopy;
            currentDeliveryNoteNumber = currentDeliveryNoteNumberCopy = deliveryNoteConcept.getDeliveryNoteEnumerator().getDeliveryNoteNumber() - deliveryNoteNumbersRequired + 1;
            String deliveryNoteComplete;
            for (int i = 0; i < deliveryNoteList.size(); i++) {
                DeliveryNote dn = deliveryNoteList.get(i);
                deliveryNoteComplete = POS + "-" + StringUtility.addLeadingZeros(currentDeliveryNoteNumber, 8);
                dn.setNumber(deliveryNoteComplete);

                try {
                    this.deliveryNoteService.save(dn);
                    this.deliveryNoteService.sendTrasactionAsync(dn);
                } catch (Exception e) {
                    logger.error("No se ha podido guardar el remito: " + currentDeliveryNoteNumber + " para " + egress.getName() + " con Id: " + egress.getId());
                }

                logger.info("Se ha guardado el remito numero: " + currentDeliveryNoteNumber + " para " + egress.getName() + " con Id: " + egress.getId());
                this.auditService.addAudit(this.userName, RoleOperation.DELIVERY_NOTE_PRINT.getId(), AuditState.COMFIRMED, dn.getId());

                currentDeliveryNoteNumber++;
                printsNumbers.add(dn.getNumber());
            }

            document.close();

            // estampo los numeros recien generados en cada pagina.
            PdfReader reader = null;
            PdfStamper stamper = null;
            ByteArrayOutputStream finishedDeliveryNotes = new ByteArrayOutputStream();
            try {
                // creo un lector de los remitos que aun no tienen los numeros estampados.
                reader = new PdfReader(out.toByteArray());
                // creo la estampadora de numeros.
                stamper = new PdfStamper(reader, finishedDeliveryNotes);

                int numberOfPages = reader.getNumberOfPages();
                bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
                for (int i = 1; i <= numberOfPages; i++) {
                    PdfContentByte canvas = stamper.getOverContent(i);
                    bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
                    canvas.beginText();
                    canvas.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
                    canvas.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.NUMBER_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.NUMBER_Y.name()));
                    canvas.showText(dnConfigMap.get(DeliveryNoteConfigParam.NUMBER_PRINT.name()) == 1 ? StringUtility.addLeadingZeros(currentDeliveryNoteNumberCopy, 8) : "");
                    canvas.endText();
                    currentDeliveryNoteNumberCopy++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } finally {
                try {
                    stamper.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader.close();
            }


            ByteArrayInputStream pdfDocument = new ByteArrayInputStream(finishedDeliveryNotes.toByteArray());
            this.printOnPrinter.sendPDFToSpool(egress.getAgreement().getDeliveryNotePrinter(), "remito-" + new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(new Date()), pdfDocument, printerResultDTO, deliveryNoteConcept.getDeliveryNoteCopies());

            try {
                pdfDocument.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // solo si se pudo imprimir correctamente seteo como impreso la solicitud asociada al armado.
            if (printOrders) {
                ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(((Order) egress).getProvisioningRequest().getId());
                provisioningRequest.setState(state);
                this.provisioningRequestService.save(provisioningRequest);
            }
        }
        printerResultDTO.setDeliveryNoteNumbers(printsNumbers);
    }

    public Concept getConcept(Egress egress, Integer deliveryNoteNumbersRequired) throws Exception {
        logger.error("Se procede a obtener el concepto para "  + egress.getClass());
        if(this.printSupplyings){
            logger.error("Se obtiene el concepto de egreso");
            Integer conceptId = ((Output)egress).getConcept().getId();
            return this.conceptService.getAndUpdateDeliveryNote(conceptId, deliveryNoteNumbersRequired);
        }
        if(this.printSupplyings){
            logger.error("Se obtiene el concepto de dispensa");
            return this.conceptService.getAndUpdateDeliveryNote(property.getSupplyingConcept().getId(), deliveryNoteNumbersRequired);
        }
        if(this.printOrders) {
            logger.error("Se obtiene el concepto de armado");
            Integer conceptId = egress.getAgreement().getDeliveryNoteConcept().getId();
            return this.conceptService.getAndUpdateDeliveryNote(conceptId, deliveryNoteNumbersRequired);
        }
        logger.error("No se obtuvo el concepto");
        throw new Exception();
    }

    // si ya esta lleno el remito, sigo en uno nuevo
    private int checkNewPage(Egress egress, Integer numberOfDeliveryNoteDetailsPerPage, int currentLine, boolean sameSucessiveProduct, boolean isBatchExpirationDateProduct) {
        int neededLines;
        /* si el producto anterior es el mismo del actual y es lote/vto necesito 1 linea sola el lote nuevo, sino 2
         sino si es serializado necesito minimo 2 lineas si es el mismo producto q el anterior, sino minimo voy a necesitar 3 */
        if (isBatchExpirationDateProduct) {
            if (sameSucessiveProduct) {
                neededLines = 1;
            } else {
                neededLines = 2;
            }
        } else {
            if (sameSucessiveProduct) {
                neededLines = 2;
            } else {
                neededLines = 3;
            }
        }
        if ((currentLine + neededLines) > numberOfDeliveryNoteDetailsPerPage) {
            savePage(egress);
            newPage(egress);
            currentLine = 0;
        }
        return currentLine;
    }

    private int checkNewPageMoreSerials(Egress egress, Integer numberOfDeliveryNoteDetailsPerPage, int currentLine) {
        if (currentLine > numberOfDeliveryNoteDetailsPerPage) {
            savePage(egress);
            newPage(egress);
            currentLine = 0;
        }
        return currentLine;
    }

    private Egress getEgress(Integer id, boolean printSupplyings, boolean printOutputs, boolean printOrders) {
        if(printSupplyings){
            logger.error("Se obtiene la dispensa");
            return this.supplyingService.get(id);
        }
        if(printOutputs){
            logger.error("Se obtiene el egreso");
            return this.outputService.get(id);
        }
        if(printOrders){
            logger.error("Se obtiene el armado");
            return this.orderService.get(id);
        }
        return null;
    }

    private void newPage(Egress egress) {
        printFooter(totalItems);
        document.newPage();
        printHeader(egress);
        offsetY = 0;
        totalItems = 0;

        deliveryNote = new DeliveryNote();
        deliveryNoteDetails = new ArrayList<>();
    }

    public void setDeliveryNoteDetail(Detail detail, DeliveryNoteDetail deliveryNoteDetail) {
        if(printSupplyings){
            deliveryNoteDetail.setSupplyingDetail((SupplyingDetail) detail);
        }
        if(printOutputs){
            deliveryNoteDetail.setOutputDetail((OutputDetail) detail);
        }
        if(printOrders){
            deliveryNoteDetail.setOrderDetail((OrderDetail) detail);
        }
    }

    private void printHeader(Egress egress){
        if(printSupplyings){
            printHeaderSupplying((Supplying) egress);
        }
        if(printOutputs){
            printHeaderOutput((Output) egress);
        }
        if(printOrders){
            printHeaderOrder((Order) egress);
        }
    }

    private void savePage(Egress egress) {
        deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
        deliveryNote.setDate(currentDate);
        deliveryNote.setFake(false);
        logger.error("El egreso que se esta por guardar es: " + egress);
        logger.error("El concepto de remito es: " + deliveryNoteConcept);
        logger.error("La configuracion del sistema es: " + property);

        if (egress.hasToInformANMAT() && deliveryNoteConcept.isInformAnmat() && property.isInformAnmat()) {
            deliveryNote.setInformAnmat(true);
        } else {
            deliveryNote.setInformAnmat(false);
        }
        deliveryNoteNumbersRequired++;
        deliveryNoteList.add(deliveryNote);
    }

    private void printHeaderSupplying(Supplying supplying) {
        overContent.saveState();
        String drugstoreGln = this.propertyService.get().getGln();
        // seteo el tipo de fuente.
        try {
            bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
            overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // imprimo fecha.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DATE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DATE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DATE_PRINT.name()) == 1 ? new SimpleDateFormat("dd/MM/yyyy").format(currentDate) : "");

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

        // imprimo afiliado.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.AFFILIATE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.AFFILIATE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.AFFILIATE_PRINT.name()) == 1 ? ("AF: " + supplying.getAffiliate().getCode() + "    - " + supplying.getAffiliate().getSurname() + "  " + supplying.getAffiliate().getName()) : "");

        // imprimo dispensa.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ORDER_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ORDER_Y.name()));
        String supplyingId = StringUtility.addLeadingZeros(supplying.getId().toString(), 8);
        String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(supplying.getAgreement().getCode()),5) + " - " + supplying.getAgreement().getDescription();
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ORDER_PRINT.name()) == 1 ? ("Dispensa Nro.: " + supplyingId + "     Convenio: " + codeAgreement) : "");

        // imprimo cliente entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_PRINT.name()) == 1 ? supplying.getClient().getCorporateName() : "");

        // imprimo domicilio entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_PRINT.name()) == 1 ? supplying.getClient().getAddress() : "");

        // imprimo localidad entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_PRINT.name()) == 1 ? supplying.getClient().getLocality() : "");

        // imprimo cod. postal entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_PRINT.name()) == 1 ? ("(" + supplying.getClient().getZipCode() + ")") : "");

        // imprimo provincia entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_PRINT.name()) == 1 ? supplying.getClient().getProvince().getName() : "");

        // imprimo condicion IVA entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_PRINT.name()) == 1 ? supplying.getClient().getVATLiability().getDescription().toUpperCase() : "");

        // imprimo CUIT entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_PRINT.name()) == 1 ? supplying.getClient().getTaxId() : "");

        // imprimo GLN Origen
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_GLN_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_GLN_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_GLN_PRINT.name()) == 1 ? ("GLN Origen: " + drugstoreGln) : "");

        // TODO no falta GLN Destino en Cliente??
        /*overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_PRINT.name()) == 1 ? ("GLN Destino: " + supplying.getClient().getGln()) : "");*/

        overContent.restoreState();

        printHeader = true;
    }

    private void printHeaderOutput(Output output) {
        overContent.saveState();

        Property property = this.propertyService.get();

        // seteo el tipo de fuente.
        try {
            bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
            overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // imprimo fecha.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DATE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DATE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DATE_PRINT.name()) == 1 ? new SimpleDateFormat("dd/MM/yyyy").format(currentDate) : "");

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
        String outputId = StringUtility.addLeadingZeros(output.getId().toString(), 8);
        String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(output.getAgreement().getCode()),5) + " - " + output.getAgreement().getDescription();
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ORDER_PRINT.name()) == 1 ? ("Egreso Nro.: " + outputId + "     Convenio: " + codeAgreement) : "");

        // imprimo cliente entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_Y.name()));
        if (output.getDeliveryLocation() != null) {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_PRINT.name()) == 1 ? output.getDeliveryLocation().getCorporateName() : "");
        } else {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_PRINT.name()) == 1 ? output.getProvider().getCorporateName() : "");
        }

        // imprimo domicilio entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_Y.name()));
        if (output.getDeliveryLocation() != null) {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_PRINT.name()) == 1 ? output.getDeliveryLocation().getAddress() : "");
        } else {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_PRINT.name()) == 1 ? output.getProvider().getAddress() : "");
        }

        // imprimo localidad entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_Y.name()));
        if (output.getDeliveryLocation() != null) {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_PRINT.name()) == 1 ? output.getDeliveryLocation().getLocality() : "");
        } else {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_PRINT.name()) == 1 ? output.getProvider().getLocality() : "");
        }

        // imprimo cod. postal entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_Y.name()));
        if (output.getDeliveryLocation() != null) {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_PRINT.name()) == 1 ? ("(" + output.getDeliveryLocation().getZipCode() + ")") : "");
        } else {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_PRINT.name()) == 1 ? ("(" + output.getProvider().getZipCode() + ")") : "");
        }

        // imprimo provincia entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_Y.name()));
        if (output.getDeliveryLocation() != null) {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_PRINT.name()) == 1 ? output.getDeliveryLocation().getProvince().getName() : "");
        } else {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_PRINT.name()) == 1 ? output.getProvider().getProvince().getName() : "");
        }

        // imprimo condicion IVA entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_Y.name()));
        if (output.getDeliveryLocation() != null) {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_PRINT.name()) == 1 ? output.getDeliveryLocation().getVATLiability().getDescription().toUpperCase() : "");
        } else {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_PRINT.name()) == 1 ? output.getProvider().getVATLiability().getDescription().toUpperCase() : "");
        }

        // imprimo CUIT entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_Y.name()));
        if (output.getDeliveryLocation() != null) {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_PRINT.name()) == 1 ? output.getDeliveryLocation().getTaxId() : "");
        } else {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_PRINT.name()) == 1 ? output.getProvider().getTaxId() : "");
        }

        // imprimo GLN Origen
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_GLN_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_GLN_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_GLN_PRINT.name()) == 1 ? ("GLN Origen: " + property.getGln()) : "");

        // imprimo GLN Destino
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_Y.name()));
        if (output.getDeliveryLocation() != null) {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_PRINT.name()) == 1 ? ("GLN Destino: " + output.getDeliveryLocation().getGln()) : "");
        } else {
            overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_PRINT.name()) == 1 ? ("GLN Destino: " + output.getProvider().getGln()) : "");
        }

        overContent.restoreState();
    }

    private void printHeaderOrder(Order order) {
        overContent.saveState();
        ProvisioningRequest provisioningRequest = order.getProvisioningRequest();
        String drugstoreGln = this.propertyService.get().getGln();
        // seteo el tipo de fuente.
        try {
            bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
            overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // imprimo fecha.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DATE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DATE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DATE_PRINT.name()) == 1 ? new SimpleDateFormat("dd/MM/yyyy").format(currentDate) : "");

        // imprimo cliente.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_CORPORATENAME_PRINT.name()) == 1 ? provisioningRequest.getClient().getCorporateName() : "");

        // imprimo domicilio.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ADDRESS_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ADDRESS_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ADDRESS_PRINT.name()) == 1 ? provisioningRequest.getClient().getAddress() : "");

        // imprimo localidad.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_LOCALITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_LOCALITY_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_LOCALITY_PRINT.name()) == 1 ? provisioningRequest.getClient().getLocality() : "");

        // imprimo cod. postal.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ZIPCODE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ZIPCODE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_ZIPCODE_PRINT.name()) == 1 ? ("(" + provisioningRequest.getClient().getZipCode() + ")") : "");

        // imprimo provincia.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_PROVINCE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_PROVINCE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_PROVINCE_PRINT.name()) == 1 ? provisioningRequest.getClient().getProvince().getName() : "");

        // imprimo condicion IVA.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_VATLIABILITY_PRINT.name()) == 1 ? provisioningRequest.getClient().getVATLiability().getDescription().toUpperCase() : "");

        // imprimo CUIT.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_TAX_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_TAX_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_TAX_PRINT.name()) == 1 ? provisioningRequest.getClient().getTaxId() : "");

        // imprimo afiliado.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.AFFILIATE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.AFFILIATE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.AFFILIATE_PRINT.name()) == 1 ? ("AF: " + provisioningRequest.getAffiliate().getCode() + "    - " + provisioningRequest.getAffiliate().getSurname() + "  " + provisioningRequest.getAffiliate().getName()) : "");

        // imprimo pedido.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ORDER_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ORDER_Y.name()));
        String provisioningId = StringUtility.addLeadingZeros(provisioningRequest.getId().toString(), 8);
        String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(provisioningRequest.getAgreement().getCode()),5) + " - " + provisioningRequest.getAgreement().getDescription();
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ORDER_PRINT.name()) == 1 ? ("Pedido Nro.: " + provisioningId + "     Convenio: " + codeAgreement) : "");

        // imprimo cliente entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_CORPORATENAME_PRINT.name()) == 1 ? provisioningRequest.getDeliveryLocation().getCorporateName() : "");

        // imprimo domicilio entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ADDRESS_PRINT.name()) == 1 ? provisioningRequest.getDeliveryLocation().getAddress() : "");

        // imprimo localidad entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_LOCALITY_PRINT.name()) == 1 ? provisioningRequest.getDeliveryLocation().getLocality() : "");

        // imprimo cod. postal entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_ZIPCODE_PRINT.name()) == 1 ? ("(" + provisioningRequest.getDeliveryLocation().getZipCode() + ")") : "");

        // imprimo provincia entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_PROVINCE_PRINT.name()) == 1 ? provisioningRequest.getDeliveryLocation().getProvince().getName() : "");

        // imprimo condicion IVA entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_VATLIABILITY_PRINT.name()) == 1 ? provisioningRequest.getDeliveryLocation().getVATLiability().getDescription().toUpperCase() : "");

        // imprimo CUIT entrega.
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_TAX_PRINT.name()) == 1 ? provisioningRequest.getDeliveryLocation().getTaxId() : "");

        // imprimo GLN Origen
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_GLN_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_GLN_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.ISSUER_GLN_PRINT.name()) == 1 ? ("GLN Origen: " + drugstoreGln) : "");

        // imprimo GLN Destino
        overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_Y.name()));
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DELIVERYLOCATION_GLN_PRINT.name()) == 1 ? ("GLN Destino: " + provisioningRequest.getDeliveryLocation().getGln()) : "");

        overContent.restoreState();

        printHeader = true;
    }
}