package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Affiliate;
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

public class AffiliatesPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(8); // 8 columnas
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
				cb.showText("Listado de Afiliados");
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
				PdfPCell affiliateIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell affiliateCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell affiliateNameHeader = new PdfPCell(new Paragraph("NOMBRE", fontHeader));
				PdfPCell affiliateSurnameHeader = new PdfPCell(new Paragraph("APELLIDO", fontHeader));
				PdfPCell affiliateDocumentTypeHeader = new PdfPCell(new Paragraph("TIPO DE DOCUMENTO", fontHeader));
				PdfPCell affiliateDocumentHeader = new PdfPCell(new Paragraph("NRO, DE DOCUMENTO", fontHeader));
				PdfPCell affiliateClientHeader = new PdfPCell(new Paragraph("CLIENTE", fontHeader));
				PdfPCell affiliateActiveHeader = new PdfPCell(new Paragraph("ACTIVO", fontHeader));

				affiliateIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				affiliateCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				affiliateNameHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				affiliateSurnameHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				affiliateDocumentTypeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				affiliateDocumentHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				affiliateClientHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				affiliateActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(affiliateIdHeader);
				table.addCell(affiliateCodeHeader);
				table.addCell(affiliateNameHeader);
				table.addCell(affiliateSurnameHeader);
				table.addCell(affiliateDocumentTypeHeader);
				table.addCell(affiliateDocumentHeader);
				table.addCell(affiliateClientHeader);
				table.addCell(affiliateActiveHeader);
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
		float[] columnWidths = {1f, 1f, 4f, 4f, 2f, 2f, 2f, 1f};
		table.setWidths(columnWidths);
		document.open();
		@SuppressWarnings("unchecked")
		ArrayList<Affiliate> affiliates = (ArrayList<Affiliate>) model.get("affiliates");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (Affiliate affiliate : affiliates) {

			PdfPCell affiliateIdDetail = new PdfPCell(new Paragraph(affiliate.getId().toString(), fontDetails));
			PdfPCell affiliateCodeDetail = new PdfPCell(new Paragraph(affiliate.getCode().toString(), fontDetails));
			PdfPCell affiliateNameDetail = new PdfPCell(new Paragraph(affiliate.getName(), fontDetails));
			PdfPCell affiliateSurnameDetail = new PdfPCell(new Paragraph(affiliate.getSurname(), fontDetails));
			PdfPCell affiliateDocumentTypeDetail = new PdfPCell(new Paragraph(affiliate.getDocumentType() == null ? "-" : affiliate.getDocumentType(), fontDetails));
			PdfPCell affiliateDocumentDetail = new PdfPCell(new Paragraph(affiliate.getDocument() == null ? "-" : affiliate.getDocument(), fontDetails));
			PdfPCell affiliateClientDetail = new PdfPCell(new Paragraph(affiliate.getClient().getName(), fontDetails));
			PdfPCell affiliateActiveDetail = new PdfPCell(new Paragraph(affiliate.isActive() ? "SI" : "NO", fontDetails));

			affiliateIdDetail.setBorder(Rectangle.NO_BORDER);
			affiliateCodeDetail.setBorder(Rectangle.NO_BORDER);
			affiliateNameDetail.setBorder(Rectangle.NO_BORDER);
			affiliateSurnameDetail.setBorder(Rectangle.NO_BORDER);
			affiliateDocumentTypeDetail.setBorder(Rectangle.NO_BORDER);
			affiliateDocumentDetail.setBorder(Rectangle.NO_BORDER);
			affiliateClientDetail.setBorder(Rectangle.NO_BORDER);
			affiliateActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(affiliateIdDetail);
			table.addCell(affiliateCodeDetail);
			table.addCell(affiliateNameDetail);
			table.addCell(affiliateSurnameDetail);
			table.addCell(affiliateDocumentTypeDetail);
			table.addCell(affiliateDocumentDetail);
			table.addCell(affiliateClientDetail);
			table.addCell(affiliateActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}