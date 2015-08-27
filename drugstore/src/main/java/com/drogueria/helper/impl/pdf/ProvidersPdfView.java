package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Provider;
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

public class ProvidersPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(16); // 16 columnas
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
				cb.showText("Listado de Proveedores");
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
				PdfPCell providerIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell providerCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell providerNameHeader = new PdfPCell(new Paragraph("NOMBRE", fontHeader));
				PdfPCell providerTaxIdHeader = new PdfPCell(new Paragraph("CUIT", fontHeader));
				PdfPCell providerCorporateNameHeader = new PdfPCell(new Paragraph("RAZ.SOC.", fontHeader));
				PdfPCell providerProvinceHeader = new PdfPCell(new Paragraph("PROV.", fontHeader));
				PdfPCell providerLocalityHeader = new PdfPCell(new Paragraph("LOC.", fontHeader));
				PdfPCell providerAddressHeader = new PdfPCell(new Paragraph("DIR.", fontHeader));
				PdfPCell providerZipCodeHeader = new PdfPCell(new Paragraph("COD.POS.", fontHeader));
				PdfPCell providerVATLiabilityHeader = new PdfPCell(new Paragraph("IVA", fontHeader));
				PdfPCell providerPhoneHeader = new PdfPCell(new Paragraph("TEL.", fontHeader));
				PdfPCell providerMailHeader = new PdfPCell(new Paragraph("MAIL", fontHeader));
				PdfPCell providerGlnHeader = new PdfPCell(new Paragraph("GLN", fontHeader));
				PdfPCell providerAgentHeader = new PdfPCell(new Paragraph("AG.", fontHeader));
				PdfPCell providerTypeHeader = new PdfPCell(new Paragraph("TIPO", fontHeader));
				PdfPCell providerActiveHeader = new PdfPCell(new Paragraph("ACT.", fontHeader));

				providerIdHeader.setBorder(Rectangle.NO_BORDER);
				providerCodeHeader.setBorder(Rectangle.NO_BORDER);
				providerNameHeader.setBorder(Rectangle.NO_BORDER);
				providerTaxIdHeader.setBorder(Rectangle.NO_BORDER);
				providerCorporateNameHeader.setBorder(Rectangle.NO_BORDER);
				providerProvinceHeader.setBorder(Rectangle.NO_BORDER);
				providerLocalityHeader.setBorder(Rectangle.NO_BORDER);
				providerAddressHeader.setBorder(Rectangle.NO_BORDER);
				providerZipCodeHeader.setBorder(Rectangle.NO_BORDER);
				providerVATLiabilityHeader.setBorder(Rectangle.NO_BORDER);
				providerPhoneHeader.setBorder(Rectangle.NO_BORDER);
				providerMailHeader.setBorder(Rectangle.NO_BORDER);
				providerGlnHeader.setBorder(Rectangle.NO_BORDER);
				providerAgentHeader.setBorder(Rectangle.NO_BORDER);
				providerTypeHeader.setBorder(Rectangle.NO_BORDER);
				providerActiveHeader.setBorder(Rectangle.NO_BORDER);

				table.addCell(providerIdHeader);
				table.addCell(providerCodeHeader);
				table.addCell(providerNameHeader);
				table.addCell(providerTaxIdHeader);
				table.addCell(providerCorporateNameHeader);
				table.addCell(providerProvinceHeader);
				table.addCell(providerLocalityHeader);
				table.addCell(providerAddressHeader);
				table.addCell(providerZipCodeHeader);
				table.addCell(providerVATLiabilityHeader);
				table.addCell(providerPhoneHeader);
				table.addCell(providerMailHeader);
				table.addCell(providerGlnHeader);
				table.addCell(providerAgentHeader);
				table.addCell(providerTypeHeader);
				table.addCell(providerActiveHeader);
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
		float[] columnWidths = {1f, 1f, 4f, 2f, 2f, 2f, 2f, 1f, 1f, 2f, 1f, 2f, 2f, 1f, 2f, 2f};
		table.setWidths(columnWidths);
		document.open();
		ArrayList<Provider> providers = (ArrayList<Provider>) model.get("providers");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (Provider provider : providers) {

			PdfPCell providerIdDetail = new PdfPCell(new Paragraph(provider.getId().toString(), fontDetails));
			PdfPCell providerCodeDetail = new PdfPCell(new Paragraph(provider.getCode().toString(), fontDetails));
			PdfPCell providerNameDetail = new PdfPCell(new Paragraph(provider.getName(), fontDetails));
			PdfPCell providerTaxIdDetail = new PdfPCell(new Paragraph(provider.getTaxId(), fontDetails));
			PdfPCell providerCorporateNameDetail = new PdfPCell(new Paragraph(provider.getCorporateName(), fontDetails));
			PdfPCell providerProvinceDetail = new PdfPCell(new Paragraph(provider.getProvince().getName(), fontDetails));
			PdfPCell providerLocalityDetail = new PdfPCell(new Paragraph(provider.getLocality(), fontDetails));
			PdfPCell providerAddressDetail = new PdfPCell(new Paragraph(provider.getAddress(), fontDetails));
			PdfPCell providerZipCodeDetail = new PdfPCell(new Paragraph(provider.getZipCode(), fontDetails));
			PdfPCell providerVATLiabilityDetail = new PdfPCell(new Paragraph(provider.getVATLiability().getDescription(), fontDetails));
			PdfPCell providerPhoneDetail = new PdfPCell(new Paragraph(provider.getPhone(), fontDetails));
			PdfPCell providerMailDetail = new PdfPCell(new Paragraph(provider.getMail(), fontDetails));
			PdfPCell providerGlnDetail = new PdfPCell(new Paragraph(provider.getGln(), fontDetails));
			PdfPCell providerAgentDetail = new PdfPCell(new Paragraph(provider.getAgent().getDescription(), fontDetails));
			PdfPCell providerTypeDetail = new PdfPCell(new Paragraph(provider.getType().getDescription(), fontDetails));
			PdfPCell providerActiveDetail = new PdfPCell(new Paragraph(provider.isActive() ? "SI" : "NO", fontDetails));

			providerIdDetail.setBorder(Rectangle.NO_BORDER);
			providerCodeDetail.setBorder(Rectangle.NO_BORDER);
			providerNameDetail.setBorder(Rectangle.NO_BORDER);
			providerTaxIdDetail.setBorder(Rectangle.NO_BORDER);
			providerCorporateNameDetail.setBorder(Rectangle.NO_BORDER);
			providerProvinceDetail.setBorder(Rectangle.NO_BORDER);
			providerLocalityDetail.setBorder(Rectangle.NO_BORDER);
			providerAddressDetail.setBorder(Rectangle.NO_BORDER);
			providerZipCodeDetail.setBorder(Rectangle.NO_BORDER);
			providerVATLiabilityDetail.setBorder(Rectangle.NO_BORDER);
			providerPhoneDetail.setBorder(Rectangle.NO_BORDER);
			providerMailDetail.setBorder(Rectangle.NO_BORDER);
			providerGlnDetail.setBorder(Rectangle.NO_BORDER);
			providerAgentDetail.setBorder(Rectangle.NO_BORDER);
			providerTypeDetail.setBorder(Rectangle.NO_BORDER);
			providerActiveDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(providerIdDetail);
			table.addCell(providerCodeDetail);
			table.addCell(providerNameDetail);
			table.addCell(providerTaxIdDetail);
			table.addCell(providerCorporateNameDetail);
			table.addCell(providerProvinceDetail);
			table.addCell(providerLocalityDetail);
			table.addCell(providerAddressDetail);
			table.addCell(providerZipCodeDetail);
			table.addCell(providerVATLiabilityDetail);
			table.addCell(providerPhoneDetail);
			table.addCell(providerMailDetail);
			table.addCell(providerGlnDetail);
			table.addCell(providerAgentDetail);
			table.addCell(providerTypeDetail);
			table.addCell(providerActiveDetail);
			document.add(table);
			table.flushContent();
		}
	}
}