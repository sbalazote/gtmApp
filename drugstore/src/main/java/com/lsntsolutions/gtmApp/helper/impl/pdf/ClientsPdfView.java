package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.Client;
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

public class ClientsPdfView extends AbstractPdfView {
	PdfPTable table = new PdfPTable(13); // 13 columnas
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	Date now = new Date();

	/**
	 * Inner class to add a table as header.
	 */
	class HeaderFooterPageEvent extends PdfPageEventHelper {
		/**
		 * Current page number (will be reset for every chapter).
		 */
		int pagenumber = 1;

		public void onStartPage(PdfWriter writer, Document document) {
			table.flushContent();
			Rectangle rect = writer.getBoxSize("art");

			try {
				// Logo
				String realPath = getServletContext().getRealPath("/images/uploadedLogo.png");

				File file = new File(realPath);

				Image logo;
				if (file.exists()) {
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
				cb.showText("Listado de Clientes");
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
				PdfPCell clientIdHeader = new PdfPCell(new Paragraph("ID", fontHeader));
				PdfPCell clientCodeHeader = new PdfPCell(new Paragraph("CÓD.", fontHeader));
				PdfPCell clientNameHeader = new PdfPCell(new Paragraph("NOMBRE", fontHeader));
				PdfPCell clientTaxIdHeader = new PdfPCell(new Paragraph("CUIT", fontHeader));
				PdfPCell clientCorporateNameHeader = new PdfPCell(new Paragraph("RAZ.SOC.", fontHeader));
				PdfPCell clientProvinceHeader = new PdfPCell(new Paragraph("PROVINCIA", fontHeader));
				PdfPCell clientLocalityHeader = new PdfPCell(new Paragraph("LOCALIDAD", fontHeader));
				PdfPCell clientAddressHeader = new PdfPCell(new Paragraph("DIRECCION", fontHeader));
				PdfPCell clientZipCodeHeader = new PdfPCell(new Paragraph("COD. POSTAL", fontHeader));
				PdfPCell clientVATLiabilityHeader = new PdfPCell(new Paragraph("TIPO DE IVA", fontHeader));
				PdfPCell clientPhoneHeader = new PdfPCell(new Paragraph("TELEFONO", fontHeader));
				PdfPCell clientActiveHeader = new PdfPCell(new Paragraph("ACTIVO", fontHeader));
				PdfPCell clientMedicalInsuranceHeader = new PdfPCell(new Paragraph("CÓD MED.", fontHeader));

				clientIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientNameHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientTaxIdHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientCorporateNameHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientProvinceHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientLocalityHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientAddressHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientZipCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientVATLiabilityHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientPhoneHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientActiveHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				clientMedicalInsuranceHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(clientIdHeader);
				table.addCell(clientCodeHeader);
				table.addCell(clientNameHeader);
				table.addCell(clientTaxIdHeader);
				table.addCell(clientCorporateNameHeader);
				table.addCell(clientProvinceHeader);
				table.addCell(clientLocalityHeader);
				table.addCell(clientAddressHeader);
				table.addCell(clientZipCodeHeader);
				table.addCell(clientVATLiabilityHeader);
				table.addCell(clientPhoneHeader);
				table.addCell(clientActiveHeader);
				table.addCell(clientMedicalInsuranceHeader);
				document.add(table);
				table.flushContent();
			} catch (Exception e) {

			}

		}

		public void onEndPage(PdfWriter writer, Document document) {
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
		float[] columnWidths = {1f, 1f, 3f, 3f, 2f, 2f, 2f, 2f, 1f, 1f, 2f, 1f, 1f};
		table.setWidths(columnWidths);
		document.open();
		@SuppressWarnings("unchecked")
		List<Client> clients = (List<Client>) model.get("clients");

		// Fuente
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		for (Client client : clients) {

			PdfPCell clientIdDetail = new PdfPCell(new Paragraph(client.getId().toString(), fontDetails));
			PdfPCell clientCodeDetail = new PdfPCell(new Paragraph(client.getCode().toString(), fontDetails));
			PdfPCell clientNameDetail = new PdfPCell(new Paragraph(client.getName(), fontDetails));
			PdfPCell clientTaxIdDetail = new PdfPCell(new Paragraph(client.getTaxId(), fontDetails));
			PdfPCell clientCorporateNameDetail = new PdfPCell(new Paragraph(client.getCorporateName(), fontDetails));
			PdfPCell clientProvinceDetail = new PdfPCell(new Paragraph(client.getCorporateName(), fontDetails));
			PdfPCell clientLocalityDetail = new PdfPCell(new Paragraph(client.getProvince().getName(), fontDetails));
			PdfPCell clientAddressDetail = new PdfPCell(new Paragraph(client.getAddress(), fontDetails));
			PdfPCell clientZipCodeDetail = new PdfPCell(new Paragraph(client.getZipCode(), fontDetails));
			PdfPCell clientVATLiabilityDetail = new PdfPCell(new Paragraph(client.getVATLiability().getDescription(), fontDetails));
			PdfPCell clientPhoneDetail = new PdfPCell(new Paragraph(client.getPhone() == null ? "-" : client.getPhone(), fontDetails));
			PdfPCell clientActiveDetail = new PdfPCell(new Paragraph(client.isActive() ? "SI" : "NO", fontDetails));
			PdfPCell clientMedicalInsuranceDetail = new PdfPCell(new Paragraph(client.getMedicalInsuranceCode().toString(), fontDetails));

			clientIdDetail.setBorder(Rectangle.NO_BORDER);
			clientCodeDetail.setBorder(Rectangle.NO_BORDER);
			clientNameDetail.setBorder(Rectangle.NO_BORDER);
			clientTaxIdDetail.setBorder(Rectangle.NO_BORDER);
			clientCorporateNameDetail.setBorder(Rectangle.NO_BORDER);
			clientProvinceDetail.setBorder(Rectangle.NO_BORDER);
			clientLocalityDetail.setBorder(Rectangle.NO_BORDER);
			clientAddressDetail.setBorder(Rectangle.NO_BORDER);
			clientZipCodeDetail.setBorder(Rectangle.NO_BORDER);
			clientVATLiabilityDetail.setBorder(Rectangle.NO_BORDER);
			clientPhoneDetail.setBorder(Rectangle.NO_BORDER);
			clientActiveDetail.setBorder(Rectangle.NO_BORDER);
			clientMedicalInsuranceDetail.setBorder(Rectangle.NO_BORDER);

			table.addCell(clientIdDetail);
			table.addCell(clientCodeDetail);
			table.addCell(clientNameDetail);
			table.addCell(clientTaxIdDetail);
			table.addCell(clientCorporateNameDetail);
			table.addCell(clientProvinceDetail);
			table.addCell(clientLocalityDetail);
			table.addCell(clientAddressDetail);
			table.addCell(clientZipCodeDetail);
			table.addCell(clientVATLiabilityDetail);
			table.addCell(clientPhoneDetail);
			table.addCell(clientActiveDetail);
			table.addCell(clientMedicalInsuranceDetail);
			document.add(table);
			table.flushContent();
		}
	}
}