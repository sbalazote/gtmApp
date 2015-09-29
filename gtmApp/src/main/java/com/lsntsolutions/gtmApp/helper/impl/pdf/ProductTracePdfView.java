package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.dto.AuditDTO;
import com.lsntsolutions.gtmApp.dto.AuditResultDTO;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductTracePdfView extends AbstractPdfView {

    PdfPTable table = new PdfPTable(6); // 6 columnas
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
                PdfPCell auditActionHeader = new PdfPCell(new Paragraph("Accion"));
                PdfPCell auditDateHeader = new PdfPCell(new Paragraph("Fecha"));
                PdfPCell auditUserHeader = new PdfPCell(new Paragraph("Usuario"));

                auditIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                auditRoleHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                auditOperationIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                auditActionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                auditDateHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                auditUserHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

                table.addCell(auditIdHeader);
                table.addCell(auditRoleHeader);
                table.addCell(auditOperationIdHeader);
                table.addCell(auditActionHeader);
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
        float[] columnWidths = {1f, 4f, 4f, 4f, 6f, 4f};
        table.setWidths(columnWidths);
        document.open();
        @SuppressWarnings("unchecked")
        AuditResultDTO auditResultDTO = (AuditResultDTO) model.get("auditResultDTO");

        List<AuditDTO> inputs = auditResultDTO.getInputs();
        List<AuditDTO> outputs = auditResultDTO.getOutputs();
        List<AuditDTO> orders = auditResultDTO.getOrders();
        List<AuditDTO> deliveryNotes = auditResultDTO.getDeliveryNotes();
        List<AuditDTO> supplyings = auditResultDTO.getSupplyings();

        List<AuditDTO> allAudits = new ArrayList<AuditDTO>();
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
        Font fontHeader = new Font(Font.TIMES_ROMAN, 11f, Font.NORMAL, Color.BLACK);
        Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

        for (AuditDTO auditDTO : allAudits) {

            PdfPCell auditIdDetail = new PdfPCell(new Paragraph(auditDTO.getId().toString(), fontDetails));
            PdfPCell auditRoleDetail = new PdfPCell(new Paragraph(auditDTO.getRole(), fontDetails));
            PdfPCell auditOperationIdDetail = (new PdfPCell(new Paragraph(auditDTO.getOperationId().toString(), fontDetails)));
            PdfPCell auditActionDetail = new PdfPCell(new Paragraph(auditDTO.getAuditAction(), fontDetails));
            PdfPCell auditDateDetail = (new PdfPCell(new Paragraph(auditDTO.getDate(), fontDetails)));
            PdfPCell auditUserDetail = (new PdfPCell(new Paragraph(auditDTO.getUsername(), fontDetails)));

            auditIdDetail.setBorder(Rectangle.NO_BORDER);
            auditRoleDetail.setBorder(Rectangle.NO_BORDER);
            auditOperationIdDetail.setBorder(Rectangle.NO_BORDER);
            auditActionDetail.setBorder(Rectangle.NO_BORDER);
            auditDateDetail.setBorder(Rectangle.NO_BORDER);
            auditUserDetail.setBorder(Rectangle.NO_BORDER);

            table.addCell(auditIdDetail);
            table.addCell(auditRoleDetail);
            table.addCell(auditOperationIdDetail);
            table.addCell(auditActionDetail);
            table.addCell(auditDateDetail);
            table.addCell(auditUserDetail);
            document.add(table);
            table.flushContent();
        }
    }
}
