package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.dto.SearchProductDTO;
import com.lsntsolutions.gtmApp.dto.SearchProductResultDTO;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.List;

public class ProductTracePdfView extends AbstractPdfView {

    PdfPTable table = new PdfPTable(5); // 5 columnas
    /**
     * Inner class to add a table as header.
     */
    class HeaderFooterPageEvent extends PdfPageEventHelper {
        /** Current page number (will be reset for every chapter). */
        int pagenumber = 1;

        public void onStartPage(PdfWriter writer,Document document) {
            table.flushContent();
            Rectangle rect = writer.getBoxSize("art");

            try {
                // Logo
                String realPath = getServletContext().getRealPath("/images/uploadedLogo.png");

                File file = new File(realPath);

                Image logo;
                if(file.exists()) {
                    logo = Image.getInstance(realPath);
                } else {
                    realPath = getServletContext().getRealPath("/images/logo.png");
                    logo = Image.getInstance(realPath);
                }
                logo.scaleToFit(50f, 50f);
                logo.setAbsolutePosition(10f * 2.8346f, 190f * 2.8346f);

                String name = PropertyProvider.getInstance().getProp("name");
                // add text at an absolute position
                PdfContentByte cb = writer.getDirectContent();
                BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
                // NOMBRE MEMBRETE
                cb.beginText();
                cb.setFontAndSize(bf_times, 16f);
                cb.setTextMatrix(40f * 2.8346f, 195f * 2.8346f);
                cb.showText(name);
                cb.endText();

                document.add(logo);

                document.add(Chunk.NEWLINE);

                LineSeparator ls = new LineSeparator();
                document.add(new Chunk(ls));

                //Encabezado
                PdfPCell auditIdHeader = new PdfPCell(new Paragraph("Id."));
                PdfPCell auditRoleHeader = new PdfPCell(new Paragraph("Rol"));
                PdfPCell auditOperationIdHeader = new PdfPCell(new Paragraph("Id de Operacion"));
                PdfPCell auditDateHeader = new PdfPCell(new Paragraph("Fecha"));
                PdfPCell auditUserHeader = new PdfPCell(new Paragraph("Usuario"));

                auditIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                auditRoleHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                auditOperationIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                auditDateHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                auditUserHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

                table.addCell(auditIdHeader);
                table.addCell(auditRoleHeader);
                table.addCell(auditOperationIdHeader);
                table.addCell(auditDateHeader);
                table.addCell(auditUserHeader);
                document.add(table);
                table.flushContent();
            } catch (Exception e) {

            }

        }
        public void onEndPage(PdfWriter writer,Document document) {
            Rectangle rect = writer.getBoxSize("art");
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(String.format("página %d", pagenumber)),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() + 15, 0);
            ++pagenumber;
        }
    }

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        writer.setBoxSize("art", new Rectangle(0, 0, 788, 559));
        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        writer.setPageEvent(event);
        table.setWidthPercentage(95);
        float[] columnWidths = {1f, 4f, 4f, 6f, 4f};
        table.setWidths(columnWidths);
        document.open();
        @SuppressWarnings("unchecked")
        SearchProductResultDTO searchProductResultDTO = (SearchProductResultDTO) model.get("searchProductResultDTO");

        List<SearchProductDTO> inputs = searchProductResultDTO.getInputs();
        List<SearchProductDTO> outputs = searchProductResultDTO.getOutputs();
        List<SearchProductDTO> orders = searchProductResultDTO.getOrders();
        List<SearchProductDTO> deliveryNotes = searchProductResultDTO.getDeliveryNotes();
        List<SearchProductDTO> supplyings = searchProductResultDTO.getSupplyings();

        List<SearchProductDTO> allAudits = new ArrayList<>();
        if (!inputs.isEmpty()) {
            allAudits.addAll(inputs);
        }

        if (!outputs.isEmpty()) {
            allAudits.addAll(outputs);
        }

        if (!orders.isEmpty()) {
            allAudits.addAll(orders);
        }

        if (!deliveryNotes.isEmpty()) {
            allAudits.addAll(deliveryNotes);
        }

        if (!supplyings.isEmpty()) {
            allAudits.addAll(supplyings);
        }

        // Fuentes
        Font fontDetails = new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 8f, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);

        for (SearchProductDTO searchProductDTO : allAudits) {

            PdfPCell auditIdDetail = new PdfPCell(new Paragraph(searchProductDTO.getId().toString(), fontDetails));
            PdfPCell auditRoleDetail = new PdfPCell(new Paragraph(searchProductDTO.getRole(), fontDetails));
            PdfPCell auditOperationIdDetail = (new PdfPCell(new Paragraph(searchProductDTO.getOperationId().toString(), fontDetails)));
            PdfPCell auditDateDetail = (new PdfPCell(new Paragraph(searchProductDTO.getDate(), fontDetails)));
            PdfPCell auditUserDetail = (new PdfPCell(new Paragraph(searchProductDTO.getUsername(), fontDetails)));

            auditIdDetail.setBorder(Rectangle.NO_BORDER);
            auditRoleDetail.setBorder(Rectangle.NO_BORDER);
            auditOperationIdDetail.setBorder(Rectangle.NO_BORDER);
            auditDateDetail.setBorder(Rectangle.NO_BORDER);
            auditUserDetail.setBorder(Rectangle.NO_BORDER);

            table.addCell(auditIdDetail);
            table.addCell(auditRoleDetail);
            table.addCell(auditOperationIdDetail);
            table.addCell(auditDateDetail);
            table.addCell(auditUserDetail);
            document.add(table);
            table.flushContent();
        }
    }
}