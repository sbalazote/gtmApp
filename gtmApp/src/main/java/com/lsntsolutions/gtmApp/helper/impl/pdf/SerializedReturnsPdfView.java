package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.util.StringUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializedReturnsPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        document.open();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressWarnings("unchecked")
        Input input = (Input) model.get("input");
        Output output = (Output) model.get("output");
        Map<Integer, List<DeliveryNote>> associatedOutputs = (Map<Integer, List<DeliveryNote>>) model.get("associatedOutputs");

        // Logo
        String realPath = getServletContext().getRealPath("/images/uploadedLogo.png");

        File file = new File(realPath);

        Image logo;
        if (file.exists()) {
            logo = Image.getInstance(realPath);
        } else {
            realPath = getServletContext().getRealPath("/images/logo.png");
            logo = Image.getInstance(realPath);
        }
        logo.scaleToFit(50f, 50f);
        logo.setAbsolutePosition(10f * 2.8346f, 190f * 2.8346f);

        String name = PropertyProvider.getInstance().getProp("name");


        HashMap<Integer, List<InputDetail>> groupByProduct = groupByProduct(input);

        PdfPTable table = new PdfPTable(6); // 6 columnas
        table.setWidthPercentage(95);
        table.setSpacingBefore(10f);

        table.setSpacingAfter(10f);

        table.setWidths(PdfConstants.columnWidths);

        //Encabezado

        PdfPCell productCodeHeader = new PdfPCell(new Paragraph("GTIN."));
        PdfPCell productDescriptionHeader = new PdfPCell(new Paragraph("Descripcion (Cod.)"));
        PdfPCell productBatchHeader = new PdfPCell(new Paragraph("Lote"));
        PdfPCell productExpirationDateHeader = new PdfPCell(new Paragraph("Vto."));
        PdfPCell productSerialNumberHeader = new PdfPCell(new Paragraph("Serie"));
        PdfPCell productAmountHeader = new PdfPCell(new Paragraph("Cant."));

        productCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        productDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        productBatchHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        productExpirationDateHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        productSerialNumberHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        productAmountHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

        table.addCell(productCodeHeader);
        table.addCell(productDescriptionHeader);
        table.addCell(productBatchHeader);
        table.addCell(productExpirationDateHeader);
        table.addCell(productSerialNumberHeader);
        table.addCell(productAmountHeader);

        // add text at an absolute position
        PdfContentByte cb = writer.getDirectContent();
        BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
        // NOMBRE MEMBRETE
        cb.beginText();
        cb.setFontAndSize(bf_times, 16f);
        cb.setTextMatrix(40f * 2.8346f, 195f * 2.8346f);
        cb.showText(name);
        cb.endText();

        // FECHA
        cb.beginText();
        cb.setFontAndSize(bf_times, 11f);
        cb.setTextMatrix(230 * 2.8346f, 200 * 2.8346f);
        cb.showText("Fecha: " + dateFormatter.format(input.getDate()));
        cb.endText();

        // INGRESO NRO
        cb.beginText();
        cb.setTextMatrix(230 * 2.8346f, 195 * 2.8346f);
        cb.showText("Ingreso Nro.: " + StringUtility.addLeadingZeros(input.getId().toString(), 8));
        cb.endText();

        // DOC. NRO
        cb.beginText();
        cb.setTextMatrix(230 * 2.8346f, 190 * 2.8346f);
        String deliveryNoteNumber = input.getDeliveryNoteNumber();
        if (!deliveryNoteNumber.isEmpty()) {
            String POS = input.getDeliveryNoteNumber().substring(0, 4);
            String number = input.getDeliveryNoteNumber().substring(4, 12);
            cb.showText("Doc. Nro.: R" + POS + "-" + number);
        } else {
            cb.showText("Doc. Nro.: -");
        }
        cb.endText();

        document.add(logo);

        document.add(Chunk.NEWLINE);


        LineSeparator ls = new LineSeparator();
        document.add(new Chunk(ls));

        document.add(Chunk.NEWLINE);

        document.add(new Chunk("Convenio: ", PdfConstants.fontHeader));
        String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(input.getAgreement().getCode()), 5);
        Chunk description = new Chunk(codeAgreement + " - " + input.getAgreement().getDescription(), PdfConstants.fontHeader);
        document.add(description);
        document.add(Chunk.NEWLINE);

        document.add(new Chunk("Cliente/Proveedor: ", PdfConstants.fontHeader));
        Chunk active = new Chunk(input.getClientOrProviderDescription(), PdfConstants.fontHeader);
        document.add(active);
        document.add(Chunk.NEWLINE);

        document.add(new Chunk("Concepto: ", PdfConstants.fontHeader));
        String conceptCode = StringUtility.addLeadingZeros(input.getConcept().getCode(), 4);
        Chunk code = new Chunk(conceptCode + " - " + input.getConcept().getDescription(), PdfConstants.fontHeader);
        document.add(code);
        document.add(Chunk.NEWLINE);

        document.add(new Chunk("Operador Logístico: ", PdfConstants.fontHeader));
        LogisticsOperator logisticsOperator = input.getLogisticsOperator();
        String logisticsOperatorString;
        if (logisticsOperator == null) {
            logisticsOperatorString = " - ";
        } else {
            logisticsOperatorString = StringUtility.addLeadingZeros(logisticsOperator.getCode(), 4) + " - " + logisticsOperator.getName();

        }
        Chunk logisticsOperatorChunk = new Chunk(logisticsOperatorString, PdfConstants.fontHeader);
        document.add(logisticsOperatorChunk);
        document.add(Chunk.NEWLINE);

        document.add(new Chunk("Orden de Compra: ", PdfConstants.fontHeader));
        String purchaseOrderNumber = input.getPurchaseOrderNumber();
        if (purchaseOrderNumber == null) {
            purchaseOrderNumber = " - ";
        }
        Chunk purchaseNumber = new Chunk(purchaseOrderNumber, PdfConstants.fontHeader);
        document.add(purchaseNumber);
        document.add(Chunk.NEWLINE);

        Chunk tab = new Chunk(new VerticalPositionMark(), 150, true);

        document.add(new Chunk("GLN Origen: ", PdfConstants.fontHeader));
        String originGLNString = input.getOriginGln();
        if (originGLNString == null) {
            originGLNString = " - ";
        }
        Chunk originGLN = new Chunk(originGLNString, PdfConstants.fontHeader);
        document.add(originGLN);

        document.add(tab);

        document.add(new Chunk("GLN Destino: ", PdfConstants.fontHeader));
        String destinationGLNString = (String) model.get("destinationGln");
        if (destinationGLNString == null) {
            destinationGLNString = " - ";
        }
        Chunk destinationGLN = new Chunk(destinationGLNString, PdfConstants.fontHeader);
        document.add(destinationGLN);
        document.add(Chunk.NEWLINE);

        for (Integer productId : groupByProduct.keySet()) {
            String gtin = "-";
            InputDetail id = groupByProduct.get(productId).get(0);
            if (id.getGtin() != null) {
                gtin = id.getGtin().getNumber();
            }
            PdfPCell productCodeDetail;
            PdfPCell productDescriptionDetail;
            PdfPCell productBatchDetail;
            PdfPCell productExpirationDateDetail;
            PdfPCell productSerialNumberDetail;
            PdfPCell productAmountDetail;

            boolean isGroup = false;
            if (groupByProduct.get(productId).size() > 1) {
                isGroup = true;
                productCodeDetail = new PdfPCell(new Paragraph(gtin, PdfConstants.fontDetails));
                productDescriptionDetail = new PdfPCell(new Paragraph(id.getProduct().getDescription() + " (" + String.valueOf(id.getProduct().getCode()) + ")", PdfConstants.fontDetails));
                productBatchDetail = new PdfPCell(new Paragraph("", PdfConstants.fontDetails));
                productExpirationDateDetail = (new PdfPCell(new Paragraph("", PdfConstants.fontDetails)));
                productSerialNumberDetail = new PdfPCell(new Paragraph("", PdfConstants.fontDetails));
                Integer total = 0;
                for (InputDetail inputDetail : groupByProduct.get(productId)) {
                    total += inputDetail.getAmount();
                }
                productAmountDetail = new PdfPCell(new Paragraph(String.valueOf(total), PdfConstants.fontDetails));

                productCodeDetail.setBorder(Rectangle.NO_BORDER);
                productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
                productBatchDetail.setBorder(Rectangle.NO_BORDER);
                productExpirationDateDetail.setBorder(Rectangle.NO_BORDER);
                productSerialNumberDetail.setBorder(Rectangle.NO_BORDER);
                productAmountDetail.setBorder(Rectangle.NO_BORDER);

                table.addCell(productCodeDetail);
                table.addCell(productDescriptionDetail);
                table.addCell(productBatchDetail);
                table.addCell(productExpirationDateDetail);
                table.addCell(productSerialNumberDetail);
                table.addCell(productAmountDetail);
            }

            for (InputDetail inputDetail : groupByProduct.get(productId)) {
                gtin = "-";
                if (inputDetail.getGtin() != null) {
                    gtin = inputDetail.getGtin().getNumber();
                }
                productCodeDetail = new PdfPCell(new Paragraph(gtin, PdfConstants.fontDetails));
                String productDescription = "";
                if (!isGroup) {
                    productDescription = inputDetail.getProduct().getDescription() + " (" + String.valueOf(inputDetail.getProduct().getCode()) + ")";
                }
                productDescriptionDetail = new PdfPCell(new Paragraph(productDescription, PdfConstants.fontDetails));
                productBatchDetail = new PdfPCell(new Paragraph(inputDetail.getBatch(), PdfConstants.fontDetails));
                productExpirationDateDetail = (new PdfPCell(new Paragraph(dateFormatter.format(inputDetail.getExpirationDate()), PdfConstants.fontDetails)));

                String serialNumber = "-";
                if (inputDetail.getSerialNumber() != null) {
                    serialNumber = inputDetail.getSerialNumber();
                }
                productSerialNumberDetail = new PdfPCell(new Paragraph(serialNumber, PdfConstants.fontDetails));
                productAmountDetail = new PdfPCell(new Paragraph(String.valueOf(inputDetail.getAmount()), PdfConstants.fontDetails));

                productCodeDetail.setBorder(Rectangle.NO_BORDER);
                productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
                productBatchDetail.setBorder(Rectangle.NO_BORDER);
                productExpirationDateDetail.setBorder(Rectangle.NO_BORDER);
                productSerialNumberDetail.setBorder(Rectangle.NO_BORDER);
                productAmountDetail.setBorder(Rectangle.NO_BORDER);

                table.addCell(productCodeDetail);
                table.addCell(productDescriptionDetail);
                table.addCell(productBatchDetail);
                table.addCell(productExpirationDateDetail);
                table.addCell(productSerialNumberDetail);
                table.addCell(productAmountDetail);
            }
        }
        document.add(table);
        document.newPage();

        // Si hay egreso lo imprimo
        if (output != null) {
            HashMap<Integer, List<OutputDetail>> groupByProduct2 = groupByProduct(output);

            table = new PdfPTable(6); // 6 columnas
            table.setWidthPercentage(95);
            table.setSpacingBefore(10f);

            table.setSpacingAfter(10f);

            table.setWidths(PdfConstants.columnWidths);

            //Encabezado
            productCodeHeader = new PdfPCell(new Paragraph("GTIN."));
            productDescriptionHeader = new PdfPCell(new Paragraph("Descripcion (Cod.)"));
            productBatchHeader = new PdfPCell(new Paragraph("Lote"));
            productExpirationDateHeader = new PdfPCell(new Paragraph("Vto."));
            productSerialNumberHeader = new PdfPCell(new Paragraph("Serie"));
            productAmountHeader = new PdfPCell(new Paragraph("Cant."));

            productCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            productDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            productBatchHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            productExpirationDateHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            productSerialNumberHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            productAmountHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

            table.addCell(productCodeHeader);
            table.addCell(productDescriptionHeader);
            table.addCell(productBatchHeader);
            table.addCell(productExpirationDateHeader);
            table.addCell(productSerialNumberHeader);
            table.addCell(productAmountHeader);

            // add text at an absolute position
            cb = writer.getDirectContent();
            bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
            // NOMBRE MEMBRETE
            cb.beginText();
            cb.setFontAndSize(bf_times, 16f);
            cb.setTextMatrix(40f * 2.8346f, 195f * 2.8346f);
            cb.showText(name);
            cb.endText();

            // FECHA
            cb.beginText();
            cb.setFontAndSize(bf_times, 11f);
            cb.setTextMatrix(230 * 2.8346f, 200 * 2.8346f);
            cb.showText("Fecha: " + dateFormatter.format(output.getDate()));
            cb.endText();

            // INGRESO NRO
            cb.beginText();
            cb.setTextMatrix(230 * 2.8346f, 195 * 2.8346f);
            cb.showText("Egreso Nro.: " + StringUtility.addLeadingZeros(output.getId().toString(), 8));
            cb.endText();

            // DOC. NRO
            List<DeliveryNote> outputDeliveryNotes = associatedOutputs.get(output.getId());
            String dnNumbers = "";
            if (outputDeliveryNotes != null) {
                int offsetY = 190;
                cb.beginText();
                cb.setTextMatrix(230 * 2.8346f, offsetY * 2.8346f);
                cb.showText("Doc. Nro.: " + dnNumbers);
                cb.endText();
                for (DeliveryNote elem : outputDeliveryNotes) {
                    cb.beginText();
                    cb.setTextMatrix(250 * 2.8346f, offsetY * 2.8346f);
                    String pre = elem.isFake() ? "X" : "R";
                    dnNumbers = pre.concat(elem.getNumber());
                    cb.showText(dnNumbers);
                    cb.endText();
                    offsetY -= 5;
                    document.add(Chunk.NEWLINE);
                }
            } else {
                cb.beginText();
                cb.setTextMatrix(230 * 2.8346f, 190 * 2.8346f);
                cb.showText("Doc. Nro.: NO IMPRIME");
                cb.endText();
            }

            document.add(logo);

            document.add(Chunk.NEWLINE);

            ls = new LineSeparator();
            document.add(new Chunk(ls));

            document.add(Chunk.NEWLINE);

            document.add(new Chunk("Convenio: ", PdfConstants.fontHeader));
            codeAgreement = StringUtility.addLeadingZeros(String.valueOf(output.getAgreement().getCode()), 5);
            description = new Chunk(codeAgreement + " - " + output.getAgreement().getDescription(), PdfConstants.fontHeader);
            document.add(description);
            document.add(Chunk.NEWLINE);

            document.add(new Chunk("Cliente/Proveedor: ", PdfConstants.fontHeader));
            active = new Chunk(output.getClientOrProviderDescription(), PdfConstants.fontHeader);
            document.add(active);
            document.add(Chunk.NEWLINE);

            document.add(new Chunk("Concepto: ", PdfConstants.fontHeader));
            conceptCode = StringUtility.addLeadingZeros(output.getConcept().getCode(), 4);
            code = new Chunk(conceptCode + " - " + output.getConcept().getDescription(), PdfConstants.fontHeader);
            document.add(code);
            document.add(Chunk.NEWLINE);

            for (Integer productId : groupByProduct2.keySet()) {
                String gtin = "-";
                OutputDetail id = groupByProduct2.get(productId).get(0);
                if (id.getGtin() != null) {
                    gtin = id.getGtin().getNumber();
                }
                PdfPCell productCodeDetail;
                PdfPCell productDescriptionDetail;
                PdfPCell productBatchDetail;
                PdfPCell productExpirationDateDetail;
                PdfPCell productSerialNumberDetail;
                PdfPCell productAmountDetail;

                boolean isGroup = false;
                if (groupByProduct2.get(productId).size() > 1) {
                    isGroup = true;
                    productCodeDetail = new PdfPCell(new Paragraph(gtin, PdfConstants.fontDetails));
                    productDescriptionDetail = new PdfPCell(new Paragraph(id.getProduct().getDescription() + " (" + String.valueOf(id.getProduct().getCode()) + ")", PdfConstants.fontDetails));
                    productBatchDetail = new PdfPCell(new Paragraph("", PdfConstants.fontDetails));
                    productExpirationDateDetail = (new PdfPCell(new Paragraph("", PdfConstants.fontDetails)));
                    productSerialNumberDetail = new PdfPCell(new Paragraph("", PdfConstants.fontDetails));
                    Integer total = 0;
                    for (OutputDetail outputDetail : groupByProduct2.get(productId)) {
                        total += outputDetail.getAmount();
                    }
                    productAmountDetail = new PdfPCell(new Paragraph(String.valueOf(total), PdfConstants.fontDetails));

                    productCodeDetail.setBorder(Rectangle.NO_BORDER);
                    productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
                    productBatchDetail.setBorder(Rectangle.NO_BORDER);
                    productExpirationDateDetail.setBorder(Rectangle.NO_BORDER);
                    productSerialNumberDetail.setBorder(Rectangle.NO_BORDER);
                    productAmountDetail.setBorder(Rectangle.NO_BORDER);

                    table.addCell(productCodeDetail);
                    table.addCell(productDescriptionDetail);
                    table.addCell(productBatchDetail);
                    table.addCell(productExpirationDateDetail);
                    table.addCell(productSerialNumberDetail);
                    table.addCell(productAmountDetail);
                }

                for (OutputDetail outputDetail : groupByProduct2.get(productId)) {
                    gtin = "-";
                    if (outputDetail.getGtin() != null) {
                        gtin = outputDetail.getGtin().getNumber();
                    }
                    productCodeDetail = new PdfPCell(new Paragraph(gtin, PdfConstants.fontDetails));
                    String productDescription = "";
                    if (!isGroup) {
                        productDescription = outputDetail.getProduct().getDescription() + " (" + String.valueOf(outputDetail.getProduct().getCode()) + ")";
                    }
                    productDescriptionDetail = new PdfPCell(new Paragraph(productDescription, PdfConstants.fontDetails));
                    productBatchDetail = new PdfPCell(new Paragraph(outputDetail.getBatch(), PdfConstants.fontDetails));
                    productExpirationDateDetail = (new PdfPCell(new Paragraph(dateFormatter.format(outputDetail.getExpirationDate()), PdfConstants.fontDetails)));
                    String serialNumber = "-";
                    if (outputDetail.getSerialNumber() != null) {
                        serialNumber = outputDetail.getSerialNumber();
                    }
                    productSerialNumberDetail = new PdfPCell(new Paragraph(serialNumber, PdfConstants.fontDetails));
                    productAmountDetail = new PdfPCell(new Paragraph(String.valueOf(outputDetail.getAmount()), PdfConstants.fontDetails));

                    productCodeDetail.setBorder(Rectangle.NO_BORDER);
                    productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
                    productBatchDetail.setBorder(Rectangle.NO_BORDER);
                    productExpirationDateDetail.setBorder(Rectangle.NO_BORDER);
                    productSerialNumberDetail.setBorder(Rectangle.NO_BORDER);
                    productAmountDetail.setBorder(Rectangle.NO_BORDER);

                    table.addCell(productCodeDetail);
                    table.addCell(productDescriptionDetail);
                    table.addCell(productBatchDetail);
                    table.addCell(productExpirationDateDetail);
                    table.addCell(productSerialNumberDetail);
                    table.addCell(productAmountDetail);
                }
            }
            document.add(table);
        }
    }

    private HashMap<Integer, List<InputDetail>> groupByProduct(Input input) {
        HashMap<Integer, List<InputDetail>> details = new HashMap<>();

        for (InputDetail inputDetail : input.getInputDetails()) {
            List<InputDetail> list = details.get(inputDetail.getProduct().getId());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(inputDetail);
            details.put(inputDetail.getProduct().getId(), list);
        }

        return details;
    }

    private HashMap<Integer, List<OutputDetail>> groupByProduct(Output output) {
        HashMap<Integer, List<OutputDetail>> details = new HashMap<>();

        for (OutputDetail outputDetail : output.getOutputDetails()) {
            List<OutputDetail> list = details.get(outputDetail.getProduct().getId());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(outputDetail);
            details.put(outputDetail.getProduct().getId(), list);
        }

        return details;
    }
}