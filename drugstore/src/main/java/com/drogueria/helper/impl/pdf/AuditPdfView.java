package com.drogueria.helper.impl.pdf;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Audit;
import com.drogueria.util.StringUtility;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

public class AuditPdfView extends AbstractPdfView {
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
				String absoluteDiskPath = getServletContext().getRealPath("/images/logo.png");
				Image logo = Image.getInstance(absoluteDiskPath);
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
				PdfPCell auditDateTimeHeader = new PdfPCell(new Paragraph("FECHA."));
				PdfPCell auditRoleHeader = new PdfPCell(new Paragraph("Rol"));
				PdfPCell auditOperationNumberHeader = new PdfPCell(new Paragraph("Nro. de Operación"));
				PdfPCell auditActionHeader = new PdfPCell(new Paragraph("Acción"));
				PdfPCell auditUserHeader = new PdfPCell(new Paragraph("Usuario"));

				auditDateTimeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				auditRoleHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				auditOperationNumberHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				auditActionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				auditUserHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(auditDateTimeHeader);
				table.addCell(auditRoleHeader);
				table.addCell(auditOperationNumberHeader);
				table.addCell(auditActionHeader);
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
		table.setSpacingBefore(10f);

		table.setSpacingAfter(10f);
		float[] columnWidths = {4f, 4f, 4f, 2f, 2f};

		table.setWidths(columnWidths);
		document.open();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		@SuppressWarnings("unchecked")
		List<Audit> audits = (List<Audit>) model.get("audits");

		// Fuentes
		Font fontHeader = new Font(Font.TIMES_ROMAN, 11f, Font.NORMAL, Color.BLACK);
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (Audit audit : audits) {

			PdfPCell auditDateTimeDetail = new PdfPCell(new Paragraph(dateFormatter.format(audit.getDate()), fontDetails));
			PdfPCell auditRoleDetail = new PdfPCell(new Paragraph(audit.getRole().getDescription(), fontDetails));
			PdfPCell auditOperationNumberDetail = new PdfPCell(new Paragraph(StringUtility.addLeadingZeros(audit.getOperationId().toString(), 8), fontDetails));
			PdfPCell auditActionDetail = (new PdfPCell(new Paragraph(audit.getAuditAction().getDescription(), fontDetails)));
			PdfPCell auditUserDetail = (new PdfPCell(new Paragraph(audit.getUser().getName(), fontDetails)));

			auditDateTimeDetail.setBorder(Rectangle.NO_BORDER);
			auditRoleDetail.setBorder(Rectangle.NO_BORDER);
			auditOperationNumberDetail.setBorder(Rectangle.NO_BORDER);
			auditActionDetail.setBorder(Rectangle.NO_BORDER);
			auditUserDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(auditDateTimeDetail);
			table.addCell(auditRoleDetail);
			table.addCell(auditOperationNumberDetail);
			table.addCell(auditActionDetail);
			table.addCell(auditUserDetail);
			document.add(table);
			table.flushContent();
		}
		//document.add(table);
	}

}