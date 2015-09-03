package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Agreement;
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

public class AgreementsPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(10); // 10 columnas
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
				cb.showText("Listado de Agentes");
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
				PdfPCell agreementIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell agreementCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell agreementDescriptionHeader = new PdfPCell(new Paragraph("DESCRIPCIÓN", fontHeader));
				PdfPCell agreementNumberOfDeliveryNoteDetailsPerPageHeader = new PdfPCell(new Paragraph("NRO.DETALLE REM.", fontHeader));
				PdfPCell agreementOrderLabelFilepathHeader = new PdfPCell(new Paragraph("RUTA PICKING", fontHeader));
				PdfPCell agreementDeliveryNoteFilepathHeader = new PdfPCell(new Paragraph("RUTA REMITOS", fontHeader));
				PdfPCell agreementPickingFilepathHeader = new PdfPCell(new Paragraph("RUTA ROTULOS", fontHeader));
				PdfPCell agreementDeliveryNoteConceptHeader = new PdfPCell(new Paragraph("CONC. REMITO", fontHeader));
				PdfPCell agreementDestructionConceptHeader = new PdfPCell(new Paragraph("CONC. DEVOL.", fontHeader));
				PdfPCell agreementActiveHeader = new PdfPCell(new Paragraph("ACT", fontHeader));

				agreementIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				agreementCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				agreementDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				agreementNumberOfDeliveryNoteDetailsPerPageHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				agreementOrderLabelFilepathHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				agreementDeliveryNoteFilepathHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				agreementPickingFilepathHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				agreementDeliveryNoteConceptHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				agreementDestructionConceptHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				agreementActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(agreementIdHeader);
				table.addCell(agreementCodeHeader);
				table.addCell(agreementDescriptionHeader);
				table.addCell(agreementNumberOfDeliveryNoteDetailsPerPageHeader);
				table.addCell(agreementOrderLabelFilepathHeader);
				table.addCell(agreementDeliveryNoteFilepathHeader);
				table.addCell(agreementPickingFilepathHeader);
				table.addCell(agreementDeliveryNoteConceptHeader);
				table.addCell(agreementDestructionConceptHeader);
				table.addCell(agreementActiveHeader);
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
		float[] columnWidths = {1f, 1f, 2f, 1f, 3f, 3f, 3f, 2f, 2f, 2f};
		table.setWidths(columnWidths);
		document.open();
		@SuppressWarnings("unchecked")
		List<Agreement> agreements = (List<Agreement>) model.get("agreements");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (Agreement agreement : agreements) {

			PdfPCell agreementIdDetail = new PdfPCell(new Paragraph(agreement.getId().toString(), fontDetails));
			PdfPCell agreementCodeDetail = new PdfPCell(new Paragraph(agreement.getCode().toString(), fontDetails));
			PdfPCell agreementDescriptionDetail = new PdfPCell(new Paragraph(agreement.getDescription(), fontDetails));
			PdfPCell agreementNumberOfDeliveryNoteDetailsPerPageDetail = new PdfPCell(new Paragraph(agreement.getNumberOfDeliveryNoteDetailsPerPage().toString(), fontDetails));
			PdfPCell agreementOrderLabelFilepathDetail = new PdfPCell(new Paragraph(agreement.getOrderLabelFilepath(), fontDetails));
			PdfPCell agreementDeliveryNoteFilepathDetail = new PdfPCell(new Paragraph(agreement.getDeliveryNoteFilepath(), fontDetails));
			PdfPCell agreementPickingFilepathDetail = new PdfPCell(new Paragraph(agreement.getPickingFilepath(), fontDetails));
			PdfPCell agreementDeliveryNoteConceptDetail = new PdfPCell(new Paragraph(agreement.getDeliveryNoteConcept().getDescription(), fontDetails));
			PdfPCell agreementDestructionConceptDetail = new PdfPCell(new Paragraph(agreement.getDestructionConcept().getDescription(), fontDetails));
			PdfPCell agreementActiveDetail = new PdfPCell(new Paragraph(agreement.isActive() ? "SI" : "NO", fontDetails));

			agreementIdDetail.setBorder(Rectangle.NO_BORDER);
			agreementCodeDetail.setBorder(Rectangle.NO_BORDER);
			agreementDescriptionDetail.setBorder(Rectangle.NO_BORDER);
			agreementNumberOfDeliveryNoteDetailsPerPageDetail.setBorder(Rectangle.NO_BORDER);
			agreementOrderLabelFilepathDetail.setBorder(Rectangle.NO_BORDER);
			agreementDeliveryNoteFilepathDetail.setBorder(Rectangle.NO_BORDER);
			agreementPickingFilepathDetail.setBorder(Rectangle.NO_BORDER);
			agreementDeliveryNoteConceptDetail.setBorder(Rectangle.NO_BORDER);
			agreementDestructionConceptDetail.setBorder(Rectangle.NO_BORDER);
			agreementActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(agreementIdDetail);
			table.addCell(agreementCodeDetail);
			table.addCell(agreementDescriptionDetail);
			table.addCell(agreementNumberOfDeliveryNoteDetailsPerPageDetail);
			table.addCell(agreementOrderLabelFilepathDetail);
			table.addCell(agreementDeliveryNoteFilepathDetail);
			table.addCell(agreementPickingFilepathDetail);
			table.addCell(agreementDeliveryNoteConceptDetail);
			table.addCell(agreementDestructionConceptDetail);
			table.addCell(agreementActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}