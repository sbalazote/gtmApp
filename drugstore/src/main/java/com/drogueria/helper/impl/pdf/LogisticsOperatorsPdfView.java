package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.LogisticsOperator;
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

public class LogisticsOperatorsPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(11); // 11 columnas
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
				cb.showText("Listado de Operadores Logisticos");
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
				PdfPCell logisticsOperatorIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell logisticsOperatorCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell logisticsOperatorNameHeader = new PdfPCell(new Paragraph("NOMBRE", fontHeader));
				PdfPCell logisticsOperatorTaxIdHeader = new PdfPCell(new Paragraph("CUIT", fontHeader));
				PdfPCell logisticsOperatorCorporateNameHeader = new PdfPCell(new Paragraph("RAZ.SOC.", fontHeader));
				PdfPCell logisticsOperatorProvinceHeader = new PdfPCell(new Paragraph("PROVINCIA", fontHeader));
				PdfPCell logisticsOperatorLocalityHeader = new PdfPCell(new Paragraph("LOCALIDAD", fontHeader));
				PdfPCell logisticsOperatorAddressHeader = new PdfPCell(new Paragraph("DIRECCION", fontHeader));
				PdfPCell logisticsOperatorZipCodeHeader = new PdfPCell(new Paragraph("COD. POSTAL", fontHeader));
				PdfPCell logisticsOperatorPhoneHeader = new PdfPCell(new Paragraph("TELEFONO", fontHeader));
				PdfPCell logisticsOperatorActiveHeader = new PdfPCell(new Paragraph("ACTIVO", fontHeader));

				logisticsOperatorIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorNameHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorTaxIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorCorporateNameHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorProvinceHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorLocalityHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorAddressHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorZipCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorPhoneHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				logisticsOperatorActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(logisticsOperatorIdHeader);
				table.addCell(logisticsOperatorCodeHeader);
				table.addCell(logisticsOperatorNameHeader);
				table.addCell(logisticsOperatorTaxIdHeader);
				table.addCell(logisticsOperatorCorporateNameHeader);
				table.addCell(logisticsOperatorProvinceHeader);
				table.addCell(logisticsOperatorLocalityHeader);
				table.addCell(logisticsOperatorAddressHeader);
				table.addCell(logisticsOperatorZipCodeHeader);
				table.addCell(logisticsOperatorPhoneHeader);
				table.addCell(logisticsOperatorActiveHeader);
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
		float[] columnWidths = {1f, 1f, 3f, 3f, 2f, 2f, 2f, 2f, 1f, 1f, 1f};
		table.setWidths(columnWidths);
		document.open();
		@SuppressWarnings("unchecked")
		List<LogisticsOperator> logisticsOperators = (List<LogisticsOperator>) model.get("logisticsOperators");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (LogisticsOperator logisticsOperator : logisticsOperators) {

			PdfPCell logisticsOperatorIdDetail = new PdfPCell(new Paragraph(logisticsOperator.getId().toString(), fontDetails));
			PdfPCell logisticsOperatorCodeDetail = new PdfPCell(new Paragraph(logisticsOperator.getCode().toString(), fontDetails));
			PdfPCell logisticsOperatorNameDetail = new PdfPCell(new Paragraph(logisticsOperator.getName(), fontDetails));
			PdfPCell logisticsOperatorTaxIdDetail = new PdfPCell(new Paragraph(logisticsOperator.getTaxId(), fontDetails));
			PdfPCell logisticsOperatorCorporateNameDetail = new PdfPCell(new Paragraph(logisticsOperator.getCorporateName(), fontDetails));
			PdfPCell logisticsOperatorProvinceDetail = new PdfPCell(new Paragraph(logisticsOperator.getCorporateName(), fontDetails));
			PdfPCell logisticsOperatorLocalityDetail = new PdfPCell(new Paragraph(logisticsOperator.getProvince().getName(), fontDetails));
			PdfPCell logisticsOperatorAddressDetail = new PdfPCell(new Paragraph(logisticsOperator.getAddress(), fontDetails));
			PdfPCell logisticsOperatorZipCodeDetail = new PdfPCell(new Paragraph(logisticsOperator.getZipCode(), fontDetails));
			PdfPCell logisticsOperatorPhoneDetail = new PdfPCell(new Paragraph(logisticsOperator.getPhone() == null ? "-" : logisticsOperator.getPhone(), fontDetails));
			PdfPCell logisticsOperatorActiveDetail = new PdfPCell(new Paragraph(logisticsOperator.isActive() ? "SI" : "NO", fontDetails));

			logisticsOperatorIdDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorCodeDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorNameDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorTaxIdDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorCorporateNameDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorProvinceDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorLocalityDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorAddressDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorZipCodeDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorPhoneDetail.setBorder(Rectangle.NO_BORDER);
			logisticsOperatorActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(logisticsOperatorIdDetail);
			table.addCell(logisticsOperatorCodeDetail);
			table.addCell(logisticsOperatorNameDetail);
			table.addCell(logisticsOperatorTaxIdDetail);
			table.addCell(logisticsOperatorCorporateNameDetail);
			table.addCell(logisticsOperatorProvinceDetail);
			table.addCell(logisticsOperatorLocalityDetail);
			table.addCell(logisticsOperatorAddressDetail);
			table.addCell(logisticsOperatorZipCodeDetail);
			table.addCell(logisticsOperatorPhoneDetail);
			table.addCell(logisticsOperatorActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}