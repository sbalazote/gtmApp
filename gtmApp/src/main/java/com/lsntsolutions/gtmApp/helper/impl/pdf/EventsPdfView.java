package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.lsntsolutions.gtmApp.model.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EventsPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(6); // 6 columnas
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	Date now = new Date();

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
				BaseFont bf_times_bold = BaseFont.createFont(BaseFont.TIMES_BOLD, "Cp1252", false);
				BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
				BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER_BOLD, "Cp1252", false);

				// NOMBRE MEMBRETE
				cb.beginText();
				cb.setFontAndSize(bf_times_bold, 16f);
				cb.setTextMatrix(40f * 2.8346f, 200f * 2.8346f);
				cb.showText(name);
				cb.endText();

				// NOMBRE LISTADO
				cb.beginText();
				cb.setFontAndSize(bf_times, 12f);
				cb.setTextMatrix(40f * 2.8346f, 190f * 2.8346f);
				cb.showText("Listado de Eventos");
				cb.endText();

				// TIMESTAMP
				cb.beginText();
				cb.setFontAndSize(bf_courier, 12f);
				cb.setTextMatrix(190f * 2.8346f, 195f * 2.8346f);
				cb.showText("Listado generado el " + sdf.format(now));
				cb.endText();

				document.add(logo);

				document.add(Chunk.NEWLINE);

				LineSeparator ls = new LineSeparator();
				document.add(new Chunk(ls));

				// Fuente
				Font fontHeader = new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 9f, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);

				//Encabezado
				PdfPCell eventIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell eventCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell eventDescriptionHeader = new PdfPCell(new Paragraph("DESCRIPCIÓN", fontHeader));
				PdfPCell eventOriginAgentHeader = new PdfPCell(new Paragraph("AGENTE ORIGEN", fontHeader));
				PdfPCell eventDestinationAgentHeader = new PdfPCell(new Paragraph("AGENTE DEST", fontHeader));
				PdfPCell eventActiveHeader = new PdfPCell(new Paragraph("ACT", fontHeader));

				eventIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				eventCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				eventDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				eventOriginAgentHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				eventDestinationAgentHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				eventActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(eventIdHeader);
				table.addCell(eventCodeHeader);
				table.addCell(eventDescriptionHeader);
				table.addCell(eventOriginAgentHeader);
				table.addCell(eventDestinationAgentHeader);
				table.addCell(eventActiveHeader);
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
		HeaderFooterPageEvent pageEvent = new HeaderFooterPageEvent();
		writer.setPageEvent(pageEvent);
		table.setWidthPercentage(95);
		float[] columnWidths = {1f, 1f, 4f, 2f, 2f, 1f};
		table.setWidths(columnWidths);
		document.open();
		@SuppressWarnings("unchecked")
		List<com.lsntsolutions.gtmApp.model.Event> events = (List<Event>) model.get("events");

		// Fuente
		Font fontDetails = new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 8f, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);

		for (Event event : events) {

			PdfPCell eventIdDetail = new PdfPCell(new Paragraph(event.getId().toString(), fontDetails));
			PdfPCell eventCodeDetail = new PdfPCell(new Paragraph(event.getCode().toString(), fontDetails));
			PdfPCell eventDescriptionDetail = new PdfPCell(new Paragraph(event.getDescription(), fontDetails));
			PdfPCell eventOriginAgentDetail = new PdfPCell(new Paragraph(event.getOriginAgent().getDescription(), fontDetails));
			PdfPCell eventDestinationAgentDetail = new PdfPCell(new Paragraph(event.getDestinationAgent().getDescription(), fontDetails));
			PdfPCell eventActiveDetail = new PdfPCell(new Paragraph(event.isActive() ? "SI" : "NO", fontDetails));

			eventIdDetail.setBorder(Rectangle.NO_BORDER);
			eventCodeDetail.setBorder(Rectangle.NO_BORDER);
			eventDescriptionDetail.setBorder(Rectangle.NO_BORDER);
			eventOriginAgentDetail.setBorder(Rectangle.NO_BORDER);
			eventDestinationAgentDetail.setBorder(Rectangle.NO_BORDER);
			eventActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(eventIdDetail);
			table.addCell(eventCodeDetail);
			table.addCell(eventDescriptionDetail);
			table.addCell(eventOriginAgentDetail);
			table.addCell(eventDestinationAgentDetail);
			table.addCell(eventActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}