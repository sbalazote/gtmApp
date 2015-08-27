package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Product;
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

public class ProductsPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(13); // 13 columnas
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
				cb.showText("Listado de Productos");
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
				PdfPCell productIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell productCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell productDescriptionHeader = new PdfPCell(new Paragraph("DESCRIPCIÓN", fontHeader));
				PdfPCell productBrandHeader = new PdfPCell(new Paragraph("MARCA", fontHeader));
				PdfPCell productMonodrugHeader = new PdfPCell(new Paragraph("MONODR.", fontHeader));
				PdfPCell productGroupHeader = new PdfPCell(new Paragraph("AGRUP.", fontHeader));
				PdfPCell productDrugCategoryHeader = new PdfPCell(new Paragraph("A.FAR.", fontHeader));
				PdfPCell productColdHeader = new PdfPCell(new Paragraph("FRÍO", fontHeader));
				PdfPCell productInformAnmatHeader = new PdfPCell(new Paragraph("ANM.", fontHeader));
				PdfPCell productTypeHeader = new PdfPCell(new Paragraph("TIPO", fontHeader));
				PdfPCell productActiveHeader = new PdfPCell(new Paragraph("ACT", fontHeader));
				PdfPCell productGtinHeader = new PdfPCell(new Paragraph("GTIN", fontHeader));
				PdfPCell productPriceHeader = new PdfPCell(new Paragraph("PRECIO", fontHeader));

				productIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productBrandHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productMonodrugHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productGroupHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productDrugCategoryHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productColdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productInformAnmatHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productTypeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productGtinHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				productPriceHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(productIdHeader);
				table.addCell(productCodeHeader);
				table.addCell(productDescriptionHeader);
				table.addCell(productBrandHeader);
				table.addCell(productMonodrugHeader);
				table.addCell(productGroupHeader);
				table.addCell(productDrugCategoryHeader);
				table.addCell(productColdHeader);
				table.addCell(productInformAnmatHeader);
				table.addCell(productTypeHeader);
				table.addCell(productActiveHeader);
				table.addCell(productGtinHeader);
				table.addCell(productPriceHeader);
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
		float[] columnWidths = {1f, 1f, 4f, 2f, 2f, 2f, 2f, 1f, 1f, 2f, 1f, 2f, 2f};
		table.setWidths(columnWidths);
		document.open();
		@SuppressWarnings("unchecked")
		ArrayList<Product> products = (ArrayList<Product>) model.get("products");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (Product product : products) {

			PdfPCell productIdDetail = new PdfPCell(new Paragraph(product.getId().toString(), fontDetails));
			PdfPCell productCodeDetail = new PdfPCell(new Paragraph(product.getCode().toString(), fontDetails));
			PdfPCell productDescriptionDetail = new PdfPCell(new Paragraph(product.getDescription(), fontDetails));
			PdfPCell productBrandDetail = (new PdfPCell(new Paragraph(product.getBrand().getDescription(), fontDetails)));
			PdfPCell productMonodrugDetail = (new PdfPCell(new Paragraph(product.getMonodrug().getDescription(), fontDetails)));
			PdfPCell productGroupDetail = new PdfPCell(new Paragraph(product.getGroup().getDescription(), fontDetails));
			PdfPCell productDrugCategoryDetail = new PdfPCell(new Paragraph(product.getDrugCategory().getDescription(), fontDetails));
			PdfPCell productColdDetail = new PdfPCell(new Paragraph(product.isCold() ? "SI" : "NO", fontDetails));
			PdfPCell productInformAnmatDetail = (new PdfPCell(new Paragraph(product.isInformAnmat() ? "SI" : "NO", fontDetails)));
			PdfPCell productTypeDetail = (new PdfPCell(new Paragraph(product.getType().equalsIgnoreCase("BE") ? "LTE/VTO" : product.getType().equalsIgnoreCase("PS") ? "ORIGEN"
					: "PROPIO", fontDetails)));
			PdfPCell productActiveDetail = new PdfPCell(new Paragraph(product.isActive() ? "SI" : "NO", fontDetails));
			PdfPCell productGtinDetail = (new PdfPCell(new Paragraph((product.getLastGtin() != null) ? product.getLastGtin() : "-", fontDetails)));
			PdfPCell productPriceDetail = (new PdfPCell(new Paragraph((product.getLastPrice() != null) ? product.getLastPrice().toString() : "-", fontDetails)));

			productIdDetail.setBorder(Rectangle.NO_BORDER);
			productCodeDetail.setBorder(Rectangle.NO_BORDER);
			productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
			productBrandDetail.setBorder(Rectangle.NO_BORDER);
			productMonodrugDetail.setBorder(Rectangle.NO_BORDER);
			productGroupDetail.setBorder(Rectangle.NO_BORDER);
			productDrugCategoryDetail.setBorder(Rectangle.NO_BORDER);
			productColdDetail.setBorder(Rectangle.NO_BORDER);
			productInformAnmatDetail.setBorder(Rectangle.NO_BORDER);
			productTypeDetail.setBorder(Rectangle.NO_BORDER);
			productActiveDetail.setBorder(Rectangle.NO_BORDER);
			productGtinDetail.setBorder(Rectangle.NO_BORDER);
			productPriceDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(productIdDetail);
			table.addCell(productCodeDetail);
			table.addCell(productDescriptionDetail);
			table.addCell(productBrandDetail);
			table.addCell(productMonodrugDetail);
			table.addCell(productGroupDetail);
			table.addCell(productDrugCategoryDetail);
			table.addCell(productColdDetail);
			table.addCell(productInformAnmatDetail);
			table.addCell(productTypeDetail);
			table.addCell(productActiveDetail);
			table.addCell(productGtinDetail);
			table.addCell(productPriceDetail);
			document.add(table);
			table.flushContent();
		}
	}
}