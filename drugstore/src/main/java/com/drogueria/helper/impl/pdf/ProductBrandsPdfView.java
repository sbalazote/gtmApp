package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.ProductBrand;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ProductBrandsPdfView extends AbstractPdfView {
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
				cb.showText("Listado de Marcas de Producto");
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
				PdfPCell productBrandIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell productBrandCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell productBrandDescriptionHeader = new PdfPCell(new Paragraph("DESCRIPCIÓN", fontHeader));
				PdfPCell productBrandActiveHeader = new PdfPCell(new Paragraph("ACT", fontHeader));

				productBrandIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productBrandCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productBrandDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productBrandActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(productBrandIdHeader);
				table.addCell(productBrandCodeHeader);
				table.addCell(productBrandDescriptionHeader);
				table.addCell(productBrandActiveHeader);
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
		ArrayList<ProductBrand> productBrands = (ArrayList<ProductBrand>) model.get("productBrands");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (ProductBrand productBrand : productBrands) {

			PdfPCell productBrandIdDetail = new PdfPCell(new Paragraph(productBrand.getId().toString(), fontDetails));
			PdfPCell productBrandCodeDetail = new PdfPCell(new Paragraph(productBrand.getCode().toString(), fontDetails));
			PdfPCell productBrandDescriptionDetail = new PdfPCell(new Paragraph(productBrand.getDescription(), fontDetails));
			PdfPCell productBrandActiveDetail = new PdfPCell(new Paragraph(productBrand.isActive() ? "SI" : "NO", fontDetails));

			productBrandIdDetail.setBorder(Rectangle.NO_BORDER);
			productBrandCodeDetail.setBorder(Rectangle.NO_BORDER);
			productBrandDescriptionDetail.setBorder(Rectangle.NO_BORDER);
			productBrandActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(productBrandIdDetail);
			table.addCell(productBrandCodeDetail);
			table.addCell(productBrandDescriptionDetail);
			table.addCell(productBrandActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}