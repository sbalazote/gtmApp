package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.constant.DocumentType;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.model.ProvisioningRequestDetail;
import com.lsntsolutions.gtmApp.util.StringUtility;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProvisioningPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		document.open();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<ProvisioningRequest> provisionings = (List<ProvisioningRequest>) model.get("provisionings");

		// Fuentes
		Font fontHeader = new Font(Font.TIMES_ROMAN, 11f, Font.NORMAL, Color.BLACK);
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);
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

		for (ProvisioningRequest provisioningRequest : provisionings) {

			HashMap<Integer, List<ProvisioningRequestDetail>> groupByProduct = groupByProduct(provisioningRequest);

			PdfPTable table = new PdfPTable(3); // 3 columnas
			table.setWidthPercentage(95);
			table.setSpacingBefore(10f);

			table.setSpacingAfter(10f);
			float[] columnWidths = {1f, 7f, 3f};

			table.setWidths(columnWidths);

			//Encabezado

			PdfPCell productCodeHeader = new PdfPCell(new Paragraph("GTIN."));
			PdfPCell productDescriptionHeader = new PdfPCell(new Paragraph("Descripcion (Cod.)"));
			PdfPCell productAmountHeader = new PdfPCell(new Paragraph("Cant."));

			productCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productAmountHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

			table.addCell(productCodeHeader);
			table.addCell(productDescriptionHeader);
			table.addCell(productAmountHeader);

			// add text at an absolute position
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
			// NOMBRE MEMBRETE
			cb.beginText();
			cb.setFontAndSize(bf_times, 16f);
			cb.setTextMatrix(40f * 2.8346f, 195f * 2.8346f);
			cb.showText(name);
			cb.endText();

			// FECHA
			cb.beginText();
			cb.setFontAndSize(bf_times, 11f);
			cb.setTextMatrix(230 * 2.8346f, 200 * 2.8346f);
			cb.showText("Fecha: " + dateFormatter.format(provisioningRequest.getDeliveryDate()));
			cb.endText();

			// SOLICITUD NRO
			cb.beginText();
			cb.setTextMatrix(230 * 2.8346f, 195 * 2.8346f);
			cb.showText("Pedido Nro.: " + StringUtility.addLeadingZeros(provisioningRequest.getId().toString(), 8));
			cb.endText();

			document.add(logo);

			document.add(Chunk.NEWLINE);

			LineSeparator ls = new LineSeparator();
			document.add(new Chunk(ls));

			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Convenio: ", fontHeader));
			String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(provisioningRequest.getAgreement().getCode()), 5);
			Chunk description = new Chunk(codeAgreement + " - " + provisioningRequest.getAgreement().getDescription(), fontHeader);
			document.add(description);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Cliente: ", fontHeader));
			String clientCode = StringUtility.addLeadingZeros(String.valueOf(provisioningRequest.getClient().getCode()), 4);
			Chunk client = new Chunk(clientCode + " - " + provisioningRequest.getClient().getName(), fontHeader);
			document.add(client);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Afiliado: ", fontHeader));
			String documentType;
			if (provisioningRequest.getAffiliate().getDocumentType() != null) {
				documentType = DocumentType.types.get(Integer.valueOf(provisioningRequest.getAffiliate().getDocumentType()));
			} else {
				documentType = "";
			}
			String documentNumber;
			if (provisioningRequest.getAffiliate().getDocument() != null) {
				documentNumber = provisioningRequest.getAffiliate().getDocument();
			} else {
				documentNumber = "";
			}

			Chunk code = new Chunk("(Cod. " + StringUtility.addLeadingZeros(provisioningRequest.getAffiliate().getCode(), 5) + " ) - " + documentType + " " + documentNumber + " - " + provisioningRequest.getAffiliate().getName() + " " + provisioningRequest.getAffiliate().getSurname(), fontHeader);
			document.add(code);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Lugar de Entrega: ", fontHeader));
			Chunk deliveryLocation = new Chunk(provisioningRequest.getDeliveryLocation().getName(), fontHeader);
			document.add(deliveryLocation);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Observación: ", fontHeader));
			Chunk comment = new Chunk(provisioningRequest.getComment() != null ? provisioningRequest.getComment() : "", fontHeader);
			document.add(comment);
			document.add(Chunk.NEWLINE);

			for (Integer productId : groupByProduct.keySet()) {
				String gtin = "-";
				ProvisioningRequestDetail sd = groupByProduct.get(productId).get(0);
				if (sd.getProduct().getLastGtin() != null) {
					gtin = sd.getProduct().getLastGtin();
				}
				PdfPCell productCodeDetail;
				PdfPCell productDescriptionDetail;
				PdfPCell productAmountDetail;

				boolean isGroup = false;
				if (groupByProduct.get(productId).size() > 1) {
					isGroup = true;
					productCodeDetail = new PdfPCell(new Paragraph(gtin, fontDetails));
					productDescriptionDetail = new PdfPCell(new Paragraph(sd.getProduct().getDescription() + " (" + String.valueOf(sd.getProduct().getCode()) + ")", fontDetails));
					Integer total = 0;
					for (ProvisioningRequestDetail provisioningRequestDetail : groupByProduct.get(productId)) {
						total += provisioningRequestDetail.getAmount();
					}
					productAmountDetail = new PdfPCell(new Paragraph(String.valueOf(total), fontDetails));

					productCodeDetail.setBorder(Rectangle.NO_BORDER);
					productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
					productAmountDetail.setBorder(Rectangle.NO_BORDER);

					table.addCell(productCodeDetail);
					table.addCell(productDescriptionDetail);
					table.addCell(productAmountDetail);
				}
				for (ProvisioningRequestDetail provisioningRequestDetail : groupByProduct.get(productId)) {
					gtin = "-";
					if (provisioningRequestDetail.getProduct().getLastGtin() != null) {
						gtin = provisioningRequestDetail.getProduct().getLastGtin();
					}
					productCodeDetail = new PdfPCell(new Paragraph(gtin, fontDetails));

					String productDescription = "";
					if (!isGroup) {
						productDescription += provisioningRequestDetail.getProduct().getDescription() + " (" + String.valueOf(provisioningRequestDetail.getProduct().getCode()) + ")";
					}
					productDescriptionDetail = new PdfPCell(new Paragraph(productDescription, fontDetails));
					String amount = String.valueOf(provisioningRequestDetail.getAmount());
					productAmountDetail = new PdfPCell(new Paragraph(amount, fontDetails));

					productCodeDetail.setBorder(Rectangle.NO_BORDER);
					productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
					productAmountDetail.setBorder(Rectangle.NO_BORDER);

					table.addCell(productCodeDetail);
					table.addCell(productDescriptionDetail);
					table.addCell(productAmountDetail);
				}
			}
			document.add(table);
			document.newPage();
		}
	}

	private HashMap<Integer, List<ProvisioningRequestDetail>> groupByProduct(ProvisioningRequest provisioningRequest) {
		HashMap<Integer,List<ProvisioningRequestDetail>> details = new HashMap<>();

		for(ProvisioningRequestDetail provisioningRequestDetail : provisioningRequest.getProvisioningRequestDetails()){
			List<ProvisioningRequestDetail> list = details.get(provisioningRequestDetail.getProduct().getId());
			if(list == null) {
				list = new ArrayList<>();
			}
			list.add(provisioningRequestDetail);
			details.put(provisioningRequestDetail.getProduct().getId(),list);
		}

		return details;
	}
}
