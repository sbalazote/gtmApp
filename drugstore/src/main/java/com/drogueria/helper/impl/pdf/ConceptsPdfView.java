package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Concept;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.List;

public class ConceptsPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(11); // 11 columnas
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	Date now = new Date();

	/**
	 * Inner class to add a table as header.
	 */
	class HeaderFooterPageEvent extends PdfPageEventHelper {
		/**
		 * Current page number (will be reset for every chapter).
		 */
		int pagenumber = 1;

		public void onStartPage(PdfWriter writer, Document document) {
			table.flushContent();
			Rectangle rect = writer.getBoxSize("art");

			try {
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
				cb.showText("Listado de Conceptos");
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
				Font fontHeader = new Font(Font.TIMES_ROMAN, 9f, Font.NORMAL, Color.BLACK);

				//Encabezado
				PdfPCell conceptIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell conceptCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell conceptDescriptionHeader = new PdfPCell(new Paragraph("DESCRIPCION", fontHeader));
				PdfPCell conceptInputHeader = new PdfPCell(new Paragraph("ES INGRESO", fontHeader));
				PdfPCell conceptPrintDeliveryNoteHeader = new PdfPCell(new Paragraph("IMPRIME REMITO", fontHeader));
				PdfPCell conceptDeliveryNoteCopiesHeader = new PdfPCell(new Paragraph("NRO. COPIAS REMITO", fontHeader));
				PdfPCell conceptRefundHeader = new PdfPCell(new Paragraph("ES DEVOLUCION", fontHeader));
				PdfPCell conceptInformAnmatHeader = new PdfPCell(new Paragraph("INFORMA ANMAT", fontHeader));
				PdfPCell conceptActiveHeader = new PdfPCell(new Paragraph("ACTIVO", fontHeader));
				PdfPCell conceptClientHeader = new PdfPCell(new Paragraph("ES CLIENTE", fontHeader));
				PdfPCell conceptDeliveryNoteEnumeratorHeader = new PdfPCell(new Paragraph("REMITO ENUM", fontHeader));

				conceptIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptInputHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptPrintDeliveryNoteHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptDeliveryNoteCopiesHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptRefundHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptInformAnmatHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptClientHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				conceptDeliveryNoteEnumeratorHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(conceptIdHeader);
				table.addCell(conceptCodeHeader);
				table.addCell(conceptDescriptionHeader);
				table.addCell(conceptInputHeader);
				table.addCell(conceptPrintDeliveryNoteHeader);
				table.addCell(conceptDeliveryNoteCopiesHeader);
				table.addCell(conceptRefundHeader);
				table.addCell(conceptInformAnmatHeader);
				table.addCell(conceptActiveHeader);
				table.addCell(conceptClientHeader);
				table.addCell(conceptDeliveryNoteEnumeratorHeader);
				document.add(table);
				table.flushContent();
			} catch (Exception e) {

			}

		}

		public void onEndPage(PdfWriter writer, Document document) {
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
		float[] columnWidths = {1f, 1f, 3f, 3f, 2f, 2f, 2f, 2f, 1f, 1f, 2f};
		table.setWidths(columnWidths);
		document.open();
		@SuppressWarnings("unchecked")
		List<Concept> concepts = (List<Concept>) model.get("concepts");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (Concept concept : concepts) {

			PdfPCell conceptIdDetail = new PdfPCell(new Paragraph(concept.getId().toString(), fontDetails));
			PdfPCell conceptCodeDetail = new PdfPCell(new Paragraph(concept.getCode().toString(), fontDetails));
			PdfPCell conceptDescriptionDetail = new PdfPCell(new Paragraph(concept.getDescription(), fontDetails));
			PdfPCell conceptInputDetail = new PdfPCell(new Paragraph(concept.isInput() ? "SI" : "NO", fontDetails));
			PdfPCell conceptPrintDeliveryNoteDetail = new PdfPCell(new Paragraph(concept.isPrintDeliveryNote() ? "SI" : "NO", fontDetails));
			PdfPCell conceptDeliveryNoteCopiesDetail = new PdfPCell(new Paragraph(concept.getDeliveryNoteCopies().toString(), fontDetails));
			PdfPCell conceptRefundDetail = new PdfPCell(new Paragraph(concept.isRefund() ? "SI" : "NO", fontDetails));
			PdfPCell conceptInformAnmatDetail = new PdfPCell(new Paragraph(concept.isInformAnmat() ? "SI" : "NO", fontDetails));
			PdfPCell conceptActiveDetail = new PdfPCell(new Paragraph(concept.isActive() ? "SI" : "NO", fontDetails));
			PdfPCell conceptClientDetail = new PdfPCell(new Paragraph(concept.isClient() ? "SI" : "NO", fontDetails));
			PdfPCell conceptDeliveryNoteEnumeratorDetail = new PdfPCell(new Paragraph(concept.getDeliveryNoteEnumerator().getDeliveryNotePOS() + "-" + concept.getDeliveryNoteEnumerator().getLastDeliveryNoteNumber(), fontDetails));

			conceptIdDetail.setBorder(Rectangle.NO_BORDER);
			conceptCodeDetail.setBorder(Rectangle.NO_BORDER);
			conceptDescriptionDetail.setBorder(Rectangle.NO_BORDER);
			conceptInputDetail.setBorder(Rectangle.NO_BORDER);
			conceptPrintDeliveryNoteDetail.setBorder(Rectangle.NO_BORDER);
			conceptDeliveryNoteCopiesDetail.setBorder(Rectangle.NO_BORDER);
			conceptRefundDetail.setBorder(Rectangle.NO_BORDER);
			conceptInformAnmatDetail.setBorder(Rectangle.NO_BORDER);
			conceptActiveDetail.setBorder(Rectangle.NO_BORDER);
			conceptClientDetail.setBorder(Rectangle.NO_BORDER);
			conceptDeliveryNoteEnumeratorDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(conceptIdDetail);
			table.addCell(conceptCodeDetail);
			table.addCell(conceptDescriptionDetail);
			table.addCell(conceptInputDetail);
			table.addCell(conceptPrintDeliveryNoteDetail);
			table.addCell(conceptDeliveryNoteCopiesDetail);
			table.addCell(conceptRefundDetail);
			table.addCell(conceptInformAnmatDetail);
			table.addCell(conceptActiveDetail);
			table.addCell(conceptClientDetail);
			table.addCell(conceptDeliveryNoteEnumeratorDetail);
			document.add(table);
			table.flushContent();
		}
	}
}