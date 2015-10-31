package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private DeliveryNoteConfig deliveryNoteConfig;

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
            String drugstoreGln = this.PropertyService.get().getGln();
            Integer deliveryNoteNumber = concept.getDeliveryNoteEnumerator().getLastDeliveryNoteNumber() - deliveryNoteNumbersRequired + 1;

            // Hago el corte de remitos por la cantidad items por pagina que se indique por parametro.

            int remaining = orderDetails.size();
            int idx = 0;
            while (remaining > 0) {
                DeliveryNote deliveryNote = new DeliveryNote();
                String deliveryNoteComplete = StringUtility.addLeadingZeros(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS(), 4) + "-"
                        + StringUtility.addLeadingZeros(deliveryNoteNumber, 8);
                deliveryNote.setNumber(deliveryNoteComplete);

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
                this.generateDeliveryNoteSheet(provisioningRequest, deliveryNoteNumber, deliveryNoteConfig, drugstoreGln, tempOrderDetails);
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
                    logger.error("No se ha podido guardar el remito: " + deliveryNoteNumber + " para el Armado número: " + id);
                }

                logger.info("Se ha guardado el remito numero: " + deliveryNoteNumber + " para el Armado número: " + id);

                printsNumbers.add(deliveryNote.getId());
                deliveryNoteNumber++;
            }
        }
        return printsNumbers;
    }

    private void generateDeliveryNoteSheet(ProvisioningRequest provisioningRequest, Integer deliveryNoteNumber, DeliveryNoteConfig deliveryNoteConfig, String drugstoreGln, List<OrderDetail> orderDetails) {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.addAuthor("REMITO-" + deliveryNoteNumber);
            document.addTitle("LS&T Solutions");
            document.open();

            PdfContentByte overContent = writer.getDirectContent();

            BaseFont bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);

            overContent.saveState();
            overContent.beginText();
            overContent.setFontAndSize(bf, deliveryNoteConfig.getFontSize());

            // imprimo numero de remito.
            overContent.setTextMatrix(deliveryNoteConfig.getNumberX(), deliveryNoteConfig.getNumberY());
            overContent.showText(StringUtility.addLeadingZeros(deliveryNoteNumber, 8));

            // imprimo fecha.
            overContent.setTextMatrix(deliveryNoteConfig.getDateX(), deliveryNoteConfig.getDateY());
            overContent.showText(new SimpleDateFormat("dd/MM/yyyy").format(provisioningRequest.getDeliveryDate()));

            // imprimo cliente.
            overContent.setTextMatrix(deliveryNoteConfig.getIssuerCorporateNameX(), deliveryNoteConfig.getIssuerCorporateNameY());
            overContent.showText(provisioningRequest.getClient().getCorporateName());

            // imprimo domicilio.
            overContent.setTextMatrix(deliveryNoteConfig.getIssuerAddressX(), deliveryNoteConfig.getIssuerAddressY());
            overContent.showText(provisioningRequest.getClient().getAddress());

            // imprimo localidad.
            overContent.setTextMatrix(deliveryNoteConfig.getIssuerLocalityX(), deliveryNoteConfig.getIssuerLocalityY());
            overContent.showText(provisioningRequest.getClient().getLocality());

            // imprimo cod. postal.
            overContent.setTextMatrix(deliveryNoteConfig.getIssuerZipcodeX(), deliveryNoteConfig.getIssuerZipcodeY());
            overContent.showText("(" + provisioningRequest.getClient().getZipCode() + ")");

            // imprimo provincia.
            overContent.setTextMatrix(deliveryNoteConfig.getIssuerProvinceX(), deliveryNoteConfig.getIssuerProvinceY());
            overContent.showText(provisioningRequest.getClient().getProvince().getName());

            // imprimo condicion IVA.
            overContent.setTextMatrix(deliveryNoteConfig.getIssuerVatliabilityX(), deliveryNoteConfig.getIssuerVatliabilityY());
            overContent.showText(provisioningRequest.getClient().getVATLiability().getDescription().toUpperCase());

            // imprimo CUIT.
            overContent.setTextMatrix(deliveryNoteConfig.getIssuerTaxX(), deliveryNoteConfig.getIssuerTaxY());
            overContent.showText(provisioningRequest.getClient().getTaxId());

            // imprimo afiliado.
            overContent.setTextMatrix(deliveryNoteConfig.getAffiliateX(), deliveryNoteConfig.getAffiliateY());
            overContent.showText("AF: " + provisioningRequest.getAffiliate().getCode() + "    - " + provisioningRequest.getAffiliate().getSurname() + "  " + provisioningRequest.getAffiliate().getName());

            // imprimo pedido.
            overContent.setTextMatrix(deliveryNoteConfig.getOrderX(), deliveryNoteConfig.getOrderY());
            overContent.showText("Pedido: " + deliveryNoteNumber + "  Convenio: " + provisioningRequest.getAgreement().getCode() + "-" + provisioningRequest.getAgreement().getDescription());

            // imprimo cliente entrega.
            overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationCorporateNameX(), deliveryNoteConfig.getDeliveryLocationCorporateNameY());
            overContent.showText(provisioningRequest.getDeliveryLocation().getCorporateName());

            // imprimo domicilio entrega.
            overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationAddressX(), deliveryNoteConfig.getDeliveryLocationAddressY());
            overContent.showText(provisioningRequest.getDeliveryLocation().getAddress());

            // imprimo localidad entrega.
            overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationLocalityX(), deliveryNoteConfig.getDeliveryLocationLocalityY());
            overContent.showText(provisioningRequest.getDeliveryLocation().getLocality());

            // imprimo cod. postal entrega.
            overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationZipcodeX(), deliveryNoteConfig.getDeliveryLocationZipcodeY());
            overContent.showText("(" + provisioningRequest.getClient().getZipCode() + ")");

            // imprimo provincia entrega.
            overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationProvinceX(), deliveryNoteConfig.getDeliveryLocationProvinceY());
            overContent.showText(provisioningRequest.getClient().getProvince().getName());

            // imprimo condicion IVA entrega.
            overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationVatliabilityX(), deliveryNoteConfig.getDeliveryLocationVatliabilityY());
            overContent.showText(provisioningRequest.getDeliveryLocation().getVATLiability().getDescription().toUpperCase());

            // imprimo CUIT entrega.
            overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationTaxX(), deliveryNoteConfig.getDeliveryLocationTaxY());
            overContent.showText(provisioningRequest.getDeliveryLocation().getTaxId());

            // imprimo GLN Origen
            overContent.setTextMatrix(deliveryNoteConfig.getIssuerGlnX(), deliveryNoteConfig.getIssuerGlnY());
            overContent.showText("GLN Origen: " + drugstoreGln);

            // imprimo GLN Destino
            overContent.setTextMatrix(deliveryNoteConfig.getDeliveryLocationGlnX(), deliveryNoteConfig.getDeliveryLocationGlnY());
            overContent.showText("GLN Destino: " + provisioningRequest.getDeliveryLocation().getGln());

            String details;
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

            // imprimo Cantidad de Items para el remito.
            overContent.setTextMatrix(deliveryNoteConfig.getNumberOfItemsX(), deliveryNoteConfig.getNumberOfItemsY());
            overContent.showText("Items: " + amount);

            overContent.endText();
            overContent.restoreState();

            document.close();

            ByteArrayInputStream pdfDocument = new ByteArrayInputStream(out.toByteArray());

            this.printOnPrinter.sendPDFToSpool(provisioningRequest.getAgreement().getDeliveryNoteFilepath(), "REMITO NRO-" + deliveryNoteNumber + ".pdf", pdfDocument);

            pdfDocument.close();

        } catch (Exception e) {
            throw new RuntimeException("No se ha podido generar el Remito Nro.: " + deliveryNoteNumber, e);
        }
    }
}