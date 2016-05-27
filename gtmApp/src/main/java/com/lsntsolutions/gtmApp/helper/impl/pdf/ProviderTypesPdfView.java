package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.ProviderType;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ProviderTypesPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(4); // 4 columnas
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
				cb.showText("Listado de Tipos de Proveedor");
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
				PdfPCell providerTypeIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell providerTypeCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell providerTypeDescriptionHeader = new PdfPCell(new Paragraph("DESCRIPCIÓN", fontHeader));
				PdfPCell providerTypeActiveHeader = new PdfPCell(new Paragraph("ACT", fontHeader));

				providerTypeIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				providerTypeCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				providerTypeDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				providerTypeActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(providerTypeIdHeader);
				table.addCell(providerTypeCodeHeader);
				table.addCell(providerTypeDescriptionHeader);
				table.addCell(providerTypeActiveHeader);
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
		float[] columnWidths = {1f, 1f, 4f, 1f};
		table.setWidths(columnWidths);
		document.open();
		@SuppressWarnings("unchecked")
		ArrayList<ProviderType> providerTypes = (ArrayList<ProviderType>) model.get("providerTypes");

		// Fuente
		Font fontDetails = new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 8f, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);

		for (ProviderType providerType : providerTypes) {

			PdfPCell providerTypeIdDetail = new PdfPCell(new Paragraph(providerType.getId().toString(), fontDetails));
			PdfPCell providerTypeCodeDetail = new PdfPCell(new Paragraph(providerType.getCode().toString(), fontDetails));
			PdfPCell providerTypeDescriptionDetail = new PdfPCell(new Paragraph(providerType.getDescription(), fontDetails));
			PdfPCell providerTypeActiveDetail = new PdfPCell(new Paragraph(providerType.isActive() ? "SI" : "NO", fontDetails));

			providerTypeIdDetail.setBorder(Rectangle.NO_BORDER);
			providerTypeCodeDetail.setBorder(Rectangle.NO_BORDER);
			providerTypeDescriptionDetail.setBorder(Rectangle.NO_BORDER);
			providerTypeActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(providerTypeIdDetail);
			table.addCell(providerTypeCodeDetail);
			table.addCell(providerTypeDescriptionDetail);
			table.addCell(providerTypeActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}