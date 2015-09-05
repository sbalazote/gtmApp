package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.DeliveryLocation;
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

public class DeliveryLocationsPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(15); // 15 columnas
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
				cb.showText("Listado de Lugares de Entrega");
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
				PdfPCell deliveryLocationIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell deliveryLocationCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell deliveryLocationNameHeader = new PdfPCell(new Paragraph("NOMBRE", fontHeader));
				PdfPCell deliveryLocationTaxIdHeader = new PdfPCell(new Paragraph("CUIT", fontHeader));
				PdfPCell deliveryLocationCorporateNameHeader = new PdfPCell(new Paragraph("RAZ.SOC.", fontHeader));
				PdfPCell deliveryLocationProvinceHeader = new PdfPCell(new Paragraph("PROVINCIA", fontHeader));
				PdfPCell deliveryLocationLocalityHeader = new PdfPCell(new Paragraph("LOCALIDAD", fontHeader));
				PdfPCell deliveryLocationAddressHeader = new PdfPCell(new Paragraph("DIRECCION", fontHeader));
				PdfPCell deliveryLocationZipCodeHeader = new PdfPCell(new Paragraph("COD. POSTAL", fontHeader));
				PdfPCell deliveryLocationVATLiabilityHeader = new PdfPCell(new Paragraph("TIPO DE IVA", fontHeader));
				PdfPCell deliveryLocationPhoneHeader = new PdfPCell(new Paragraph("TELEFONO", fontHeader));
				PdfPCell deliveryLocationMailHeader = new PdfPCell(new Paragraph("MAIL", fontHeader));
				PdfPCell deliveryLocationGlnHeader = new PdfPCell(new Paragraph("GLN", fontHeader));
				PdfPCell deliveryLocationAgentHeader = new PdfPCell(new Paragraph("AGENTE", fontHeader));
				PdfPCell deliveryLocationActiveHeader = new PdfPCell(new Paragraph("ACTIVO", fontHeader));

				deliveryLocationIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationNameHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationTaxIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationCorporateNameHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationProvinceHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationLocalityHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationAddressHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationZipCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationVATLiabilityHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationPhoneHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationMailHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationGlnHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationAgentHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				deliveryLocationActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(deliveryLocationIdHeader);
				table.addCell(deliveryLocationCodeHeader);
				table.addCell(deliveryLocationNameHeader);
				table.addCell(deliveryLocationTaxIdHeader);
				table.addCell(deliveryLocationCorporateNameHeader);
				table.addCell(deliveryLocationProvinceHeader);
				table.addCell(deliveryLocationLocalityHeader);
				table.addCell(deliveryLocationAddressHeader);
				table.addCell(deliveryLocationZipCodeHeader);
				table.addCell(deliveryLocationVATLiabilityHeader);
				table.addCell(deliveryLocationPhoneHeader);
				table.addCell(deliveryLocationMailHeader);
				table.addCell(deliveryLocationGlnHeader);
				table.addCell(deliveryLocationAgentHeader);
				table.addCell(deliveryLocationActiveHeader);
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
		float[] columnWidths = {1f, 1f, 3f, 3f, 2f, 2f, 2f, 2f, 1f, 1f, 1f, 1f, 1f, 2f, 1f};
		table.setWidths(columnWidths);
		document.open();
		@SuppressWarnings("unchecked")
		List<DeliveryLocation> deliveryLocations = (List<DeliveryLocation>) model.get("deliveryLocations");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (DeliveryLocation deliveryLocation : deliveryLocations) {

			PdfPCell deliveryLocationIdDetail = new PdfPCell(new Paragraph(deliveryLocation.getId().toString(), fontDetails));
			PdfPCell deliveryLocationCodeDetail = new PdfPCell(new Paragraph(deliveryLocation.getCode().toString(), fontDetails));
			PdfPCell deliveryLocationNameDetail = new PdfPCell(new Paragraph(deliveryLocation.getName(), fontDetails));
			PdfPCell deliveryLocationTaxIdDetail = new PdfPCell(new Paragraph(deliveryLocation.getTaxId(), fontDetails));
			PdfPCell deliveryLocationCorporateNameDetail = new PdfPCell(new Paragraph(deliveryLocation.getCorporateName(), fontDetails));
			PdfPCell deliveryLocationProvinceDetail = new PdfPCell(new Paragraph(deliveryLocation.getCorporateName(), fontDetails));
			PdfPCell deliveryLocationLocalityDetail = new PdfPCell(new Paragraph(deliveryLocation.getProvince().getName(), fontDetails));
			PdfPCell deliveryLocationAddressDetail = new PdfPCell(new Paragraph(deliveryLocation.getAddress(), fontDetails));
			PdfPCell deliveryLocationZipCodeDetail = new PdfPCell(new Paragraph(deliveryLocation.getZipCode(), fontDetails));
			PdfPCell deliveryLocationVATLiabilityDetail = new PdfPCell(new Paragraph(deliveryLocation.getVATLiability().getDescription(), fontDetails));
			PdfPCell deliveryLocationPhoneDetail = new PdfPCell(new Paragraph(deliveryLocation.getPhone() == null ? "-" : deliveryLocation.getPhone(), fontDetails));
			PdfPCell deliveryLocationMailDetail = new PdfPCell(new Paragraph(deliveryLocation.getMail() == null ? "-" : deliveryLocation.getMail(), fontDetails));
			PdfPCell deliveryLocationGlnDetail = new PdfPCell(new Paragraph(deliveryLocation.getGln(), fontDetails));
			PdfPCell deliveryLocationAgentDetail = new PdfPCell(new Paragraph(deliveryLocation.getAgent().getDescription(), fontDetails));
			PdfPCell deliveryLocationActiveDetail = new PdfPCell(new Paragraph(deliveryLocation.isActive() ? "SI" : "NO", fontDetails));

			deliveryLocationIdDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationCodeDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationNameDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationTaxIdDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationCorporateNameDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationProvinceDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationLocalityDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationAddressDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationZipCodeDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationVATLiabilityDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationPhoneDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationMailDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationGlnDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationAgentDetail.setBorder(Rectangle.NO_BORDER);
			deliveryLocationActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(deliveryLocationIdDetail);
			table.addCell(deliveryLocationCodeDetail);
			table.addCell(deliveryLocationNameDetail);
			table.addCell(deliveryLocationTaxIdDetail);
			table.addCell(deliveryLocationCorporateNameDetail);
			table.addCell(deliveryLocationProvinceDetail);
			table.addCell(deliveryLocationLocalityDetail);
			table.addCell(deliveryLocationAddressDetail);
			table.addCell(deliveryLocationZipCodeDetail);
			table.addCell(deliveryLocationVATLiabilityDetail);
			table.addCell(deliveryLocationPhoneDetail);
			table.addCell(deliveryLocationMailDetail);
			table.addCell(deliveryLocationGlnDetail);
			table.addCell(deliveryLocationAgentDetail);
			table.addCell(deliveryLocationActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}