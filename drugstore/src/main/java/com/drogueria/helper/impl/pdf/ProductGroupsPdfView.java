package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.ProductGroup;
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
import java.util.Map;

public class ProductGroupsPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(4); // 4 columnas
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

				// NOMBRE LISTADO
				cb.beginText();
				cb.setFontAndSize(bf_times, 12f);
				cb.setTextMatrix(40f * 2.8346f, 190f * 2.8346f);
				cb.showText("Listado de Agrupaciones");
				cb.endText();

				document.add(logo);

				document.add(Chunk.NEWLINE);

				LineSeparator ls = new LineSeparator();
				document.add(new Chunk(ls));

				// Fuente
				Font fontHeader = new Font(Font.TIMES_ROMAN, 9f, Font.NORMAL, Color.BLACK);

				//Encabezado
				PdfPCell productGroupIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell productGroupCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell productGroupDescriptionHeader = new PdfPCell(new Paragraph("DESCRIPCIÓN", fontHeader));
				PdfPCell productGroupActiveHeader = new PdfPCell(new Paragraph("ACT", fontHeader));

				productGroupIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productGroupCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productGroupDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productGroupActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(productGroupIdHeader);
				table.addCell(productGroupCodeHeader);
				table.addCell(productGroupDescriptionHeader);
				table.addCell(productGroupActiveHeader);
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
		ArrayList<ProductGroup> productGroups = (ArrayList<ProductGroup>) model.get("productGroups");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (ProductGroup productGroup : productGroups) {

			PdfPCell productGroupIdDetail = new PdfPCell(new Paragraph(productGroup.getId().toString(), fontDetails));
			PdfPCell productGroupCodeDetail = new PdfPCell(new Paragraph(productGroup.getCode().toString(), fontDetails));
			PdfPCell productGroupDescriptionDetail = new PdfPCell(new Paragraph(productGroup.getDescription(), fontDetails));
			PdfPCell productGroupActiveDetail = new PdfPCell(new Paragraph(productGroup.isActive() ? "SI" : "NO", fontDetails));

			productGroupIdDetail.setBorder(Rectangle.NO_BORDER);
			productGroupCodeDetail.setBorder(Rectangle.NO_BORDER);
			productGroupDescriptionDetail.setBorder(Rectangle.NO_BORDER);
			productGroupActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(productGroupIdDetail);
			table.addCell(productGroupCodeDetail);
			table.addCell(productGroupDescriptionDetail);
			table.addCell(productGroupActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}