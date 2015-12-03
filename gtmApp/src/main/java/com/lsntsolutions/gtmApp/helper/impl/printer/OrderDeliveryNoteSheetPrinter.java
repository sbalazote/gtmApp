package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.DeliveryNoteConfigParam;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.constant.State;
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

@Service
public class OrderDeliveryNoteSheetPrinter extends DeliveryNoteSheetPrinter {

	private static final Logger logger = Logger.getLogger(OutputDeliveryNoteSheetPrinter.class);

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
    @Autowired
    private DeliveryNoteConfigService deliveryNoteConfigService;
    @Autowired
    private AuditService auditService;

    private Order order;
    private ProvisioningRequest provisioningRequest;
    private String drugstoreGln;
    private Document document;
    private ByteArrayOutputStream out;
    private PdfWriter writer;
    private int totalItems;
    private Integer deliveryNoteNumber;
    private String POS;
    private Date currentDate;
    private DeliveryNote deliveryNote;
    private String deliveryNoteComplete;
    private List<DeliveryNoteDetail> deliveryNoteDetails;
    private List<String> printsNumbers;
    private String userName;

    @Override
    public void print(String userName, List<Integer> ordersIds, PrinterResultDTO printerResultDTO) {
        dnConfigMap = deliveryNoteConfigService.getAllInMillimiters();
        this.userName = userName;
        printsNumbers = new ArrayList<>();
        ProvisioningRequestState state = this.provisioningRequestStateService.get(State.DELIVERY_NOTE_PRINTED.getId());
        currentDate = new Date();
        for (Integer id : ordersIds) {
            order = this.orderService.get(id);
            provisioningRequest = this.provisioningRequestService.get(order.getProvisioningRequest().getId());
            Integer numberOfDeliveryNoteDetailsPerPage = provisioningRequest.getAgreement().getNumberOfDeliveryNoteDetailsPerPage();
            provisioningRequest.setState(state);
            this.provisioningRequestService.save(provisioningRequest);

            // agrupo lista de productos por id de producto + lote.
            TreeMap<String, List<? extends Detail>> orderMap = groupByProductAndBatch(order.getOrderDetails());
            // calculo cuantas lineas de detalles de productos voy a necesitar.
            int numberOfLinesNeeded = numberOfLinesNeeded(orderMap);
            // calculo cuantos remitos voy a necesitar en base a la cantidad de detalles de productos.
            int deliveryNoteNumbersRequired = (int)Math.ceil((float)numberOfLinesNeeded / numberOfDeliveryNoteDetailsPerPage);

            Integer conceptId = order.getProvisioningRequest().getAgreement().getDeliveryNoteConcept().getId();
            Concept concept = this.conceptService.getAndUpdateDeliveryNote(conceptId, deliveryNoteNumbersRequired);
            POS = StringUtility.addLeadingZeros(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4);
            drugstoreGln = this.PropertyService.get().getGln();
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
            for (Map.Entry<String, List<? extends Detail>> entry : orderMap.entrySet()) {
                String key = entry.getKey();
                List<? extends Detail> orderDetails = entry.getValue();
                String[] parts = key.split(",");
                String currentProductId = parts[0];
                String productType = orderDetails.get(0).getProduct().getType();

                // si ya esta lleno el remito, sigo en uno nuevo
                if (currentLine == numberOfDeliveryNoteDetailsPerPage) {
                    savePage();
                    newPage();
                }

                String description = orderDetails.get(0).getProduct().getDescription();
                String monodrug = orderDetails.get(0).getProduct().getMonodrug().getDescription();
                String brand = orderDetails.get(0).getProduct().getBrand().getDescription();
                int totalAmount = orderDetails.get(0).getAmount();
                String batch = orderDetails.get(0).getBatch();
                String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(orderDetails.get(0).getExpirationDate());
                String batchAmount = Integer.toString(productType.equals("BE") ? totalAmount : orderDetails.size());
                if (!currentProductId.equals(previousProductId)) {
                    totalItems++;
                    printProductDetailHeader(description, monodrug, brand, getProductTotalAmount(Integer.parseInt(currentProductId), orderDetails));
                }
                printProductBatchExpirationDateHeader(batch, expirationDate, batchAmount);

                // si es de tipo lote/ vto ocupa 1 (una) sola linea.
                DeliveryNoteDetail deliveryNoteDetail;
                if (productType.equals("BE")) {

                    deliveryNoteDetail = new DeliveryNoteDetail();
                    deliveryNoteDetail.setOrderDetail((OrderDetail) orderDetails.get(0));
                    deliveryNoteDetails.add(deliveryNoteDetail);

                    currentLine++;
                    // si es de tipo trazado ocupa 1 (una) sola linea por cada cuatro series.
                } else {
                    List<OrderDetail> orderDetailAux = new ArrayList<>();
                    Iterator<? extends Detail> it = orderDetails.iterator();
                    int serialIdx = 0;
                    while (it.hasNext()) {
                        OrderDetail od = (OrderDetail) it.next();
                        orderDetailAux.add(od);

                        deliveryNoteDetail = new DeliveryNoteDetail();
                        deliveryNoteDetail.setOrderDetail(od);
                        deliveryNoteDetails.add(deliveryNoteDetail);

                        serialIdx++;

                        if ((serialIdx == 4) || (!it.hasNext())) {
                            printSerialDetails(orderDetailAux);
                            currentLine++;
                            serialIdx = 0;
                            orderDetailAux = new ArrayList<>();
                        }
                    }
                }
                previousProductId = currentProductId;
            }
            printFooter(totalItems);

            savePage();

            document.close();
            ByteArrayInputStream pdfDocument = new ByteArrayInputStream(out.toByteArray());

            this.printOnPrinter.sendPDFToSpool(provisioningRequest.getAgreement().getDeliveryNotePrinter(), "REMITO NRO-" + deliveryNoteNumber + ".pdf", pdfDocument, printerResultDTO, concept.getDeliveryNoteCopies());

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

        deliveryNoteDetails = new ArrayList<>();
    }

    private void savePage() {
        // Guardo el Remito en la base de datos
        deliveryNote.setDeliveryNoteDetails(deliveryNoteDetails);
        deliveryNote.setDate(currentDate);
        deliveryNote.setFake(false);
        try {
            if (order.hasToInform() && provisioningRequest.getAgreement().getDeliveryNoteConcept().isInformAnmat() && PropertyService.get().isInformAnmat()) {
                deliveryNote.setInformAnmat(true);
            } else {
                deliveryNote.setInformAnmat(false);
            }
            this.deliveryNoteService.save(deliveryNote);
            this.deliveryNoteService.sendTrasactionAsync(deliveryNote);
        } catch (Exception e1) {
            logger.error("No se ha podido guardar el remito: " + deliveryNoteNumber + " para el Armado número: " + order.getId());
        }

        logger.info("Se ha guardado el remito numero: " + deliveryNoteNumber + " para el Armado número: " + order.getId());
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
        overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.DATE_PRINT.name()) == 1 ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : "");

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