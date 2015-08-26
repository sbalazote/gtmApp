package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.ProductDrugCategory;
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

public class ProductDrugCategoriesPdfView extends AbstractPdfView {
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
				cb.showText("Listado de Acciones Farmacológicas");
				cb.endText();

				document.add(logo);

				document.add(Chunk.NEWLINE);

				LineSeparator ls = new LineSeparator();
				document.add(new Chunk(ls));

				// Fuente
				Font fontHeader = new Font(Font.TIMES_ROMAN, 9f, Font.NORMAL, Color.BLACK);

				//Encabezado
				PdfPCell productDrugCategoryIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell productDrugCategoryCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell productDrugCategoryDescriptionHeader = new PdfPCell(new Paragraph("DESCRIPCIÓN", fontHeader));
				PdfPCell productDrugCategoryActiveHeader = new PdfPCell(new Paragraph("ACT", fontHeader));

				productDrugCategoryIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productDrugCategoryCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productDrugCategoryDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productDrugCategoryActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(productDrugCategoryIdHeader);
				table.addCell(productDrugCategoryCodeHeader);
				table.addCell(productDrugCategoryDescriptionHeader);
				table.addCell(productDrugCategoryActiveHeader);
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
		ArrayList<ProductDrugCategory> productDrugCategories = (ArrayList<ProductDrugCategory>) model.get("productDrugCategories");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (ProductDrugCategory productDrugCategory : productDrugCategories) {

			PdfPCell productDrugCategoryIdDetail = new PdfPCell(new Paragraph(productDrugCategory.getId().toString(), fontDetails));
			PdfPCell productDrugCategoryCodeDetail = new PdfPCell(new Paragraph(productDrugCategory.getCode().toString(), fontDetails));
			PdfPCell productDrugCategoryDescriptionDetail = new PdfPCell(new Paragraph(productDrugCategory.getDescription(), fontDetails));
			PdfPCell productDrugCategoryActiveDetail = new PdfPCell(new Paragraph(productDrugCategory.isActive() ? "SI" : "NO", fontDetails));

			productDrugCategoryIdDetail.setBorder(Rectangle.NO_BORDER);
			productDrugCategoryCodeDetail.setBorder(Rectangle.NO_BORDER);
			productDrugCategoryDescriptionDetail.setBorder(Rectangle.NO_BORDER);
			productDrugCategoryActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(productDrugCategoryIdDetail);
			table.addCell(productDrugCategoryCodeDetail);
			table.addCell(productDrugCategoryDescriptionDetail);
			table.addCell(productDrugCategoryActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}