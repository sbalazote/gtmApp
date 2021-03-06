package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.constant.DocumentType;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.SupplyingDetail;
import com.lsntsolutions.gtmApp.service.DeliveryNoteService;
import com.lsntsolutions.gtmApp.util.StringUtility;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Supplying;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SupplyingsPdfView extends AbstractPdfView {

	@Autowired
	private DeliveryNoteService deliveryNoteService;

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		document.open();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<Supplying> supplyings = (List<Supplying>) model.get("supplyings");
		Map<Integer, List<DeliveryNote>> associatedSupplyings = (Map<Integer, List<DeliveryNote>>) model.get("associatedSupplyings");

		// Fuentes
		Font fontHeader = new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 11f, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
		Font fontDetails = new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10f, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
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

		for (Supplying supplying : supplyings) {

			HashMap<Integer,List<SupplyingDetail>> groupByProduct = groupByProduct(supplying);

			PdfPTable table = new PdfPTable(6); // 6 columnas
			table.setWidthPercentage(95);
			table.setSpacingBefore(10f);

			table.setSpacingAfter(10f);

			table.setWidths(PdfConstants.columnWidths);

			//Encabezado

			PdfPCell productCodeHeader = new PdfPCell(new Paragraph("GTIN."));
			PdfPCell productDescriptionHeader = new PdfPCell(new Paragraph("Descripcion (Cod.)"));
			PdfPCell productBatchHeader = new PdfPCell(new Paragraph("Lote"));
			PdfPCell productExpirationDateHeader = new PdfPCell(new Paragraph("Vto."));
			PdfPCell productSerialNumberHeader = new PdfPCell(new Paragraph("Serie"));
			PdfPCell productAmountHeader = new PdfPCell(new Paragraph("Cant."));

			productCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productBatchHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productExpirationDateHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productSerialNumberHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productAmountHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

			table.addCell(productCodeHeader);
			table.addCell(productDescriptionHeader);
			table.addCell(productBatchHeader);
			table.addCell(productExpirationDateHeader);
			table.addCell(productSerialNumberHeader);
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
			cb.showText("Fecha: " + dateFormatter.format(supplying.getDate()));
			cb.endText();

			// INGRESO NRO
			cb.beginText();
			cb.setTextMatrix(230 * 2.8346f, 195 * 2.8346f);
			cb.showText("Dispensa Nro.: " + StringUtility.addLeadingZeros(supplying.getId().toString(), 8));
			cb.endText();

			// DOC. NRO
			List<DeliveryNote> supplyingDeliveryNotes = associatedSupplyings.get(new Integer(supplying.getId()));
			String dnNumbers = "";
			if (supplyingDeliveryNotes != null) {
				int offsetY = 190;
				cb.beginText();
				cb.setTextMatrix(230 * 2.8346f, offsetY * 2.8346f);
				cb.showText("Doc. Nro.: " + dnNumbers);
				cb.endText();
				for (DeliveryNote elem : supplyingDeliveryNotes) {
					cb.beginText();
					cb.setTextMatrix(250 * 2.8346f, offsetY * 2.8346f);
					String pre = elem.isFake() ? "X" : "R";
					dnNumbers = pre.concat(elem.getNumber());
					cb.showText(dnNumbers);
					cb.endText();
					offsetY-=5;
					document.add(Chunk.NEWLINE);
				}
			} else {
				cb.beginText();
				cb.setTextMatrix(230 * 2.8346f, 190 * 2.8346f);
				cb.showText("Doc. Nro.: NO IMPRIME" );
				cb.endText();
			}

			document.add(logo);

			document.add(Chunk.NEWLINE);

			LineSeparator ls = new LineSeparator();
			document.add(new Chunk(ls));

			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Convenio: ", fontHeader));
			String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(supplying.getAgreement().getCode()),5);
			Chunk description = new Chunk(codeAgreement + " - " + supplying.getAgreement().getDescription(), fontHeader);
			document.add(description);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Cliente: ", fontHeader));
			String clientCode = StringUtility.addLeadingZeros(String.valueOf(supplying.getClient().getCode()),4);
			Chunk client = new Chunk(clientCode + " - " + supplying.getClient().getName(), fontHeader);
			document.add(client);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Afiliado: ", fontHeader));
			String documentType;
			if(supplying.getAffiliate().getDocumentType() != null){
				documentType = DocumentType.types.get(Integer.valueOf(supplying.getAffiliate().getDocumentType()));
			}else{
				documentType = "";
			}
			String documentNumber;
			if(supplying.getAffiliate().getDocument() != null){
				documentNumber = supplying.getAffiliate().getDocument();
			}else{
				documentNumber = "";
			}

			Chunk code = new Chunk("(Cod. " + StringUtility.addLeadingZeros(supplying.getAffiliate().getCode(), 5) + " ) - " + documentType + " " + documentNumber + " - " + supplying.getAffiliate().getSurname() + " " + supplying.getAffiliate().getName() , fontHeader);
			document.add(code);
			document.add(Chunk.NEWLINE);

			for(Integer productId : groupByProduct.keySet()) {
				String gtin = "-";
				SupplyingDetail sd = groupByProduct.get(productId).get(0);
				if (sd.getGtin() != null) {
					gtin = sd.getGtin().getNumber();
				}
				PdfPCell productCodeDetail;
				PdfPCell productDescriptionDetail;
				PdfPCell productBatchDetail;
				PdfPCell productExpirationDateDetail;
				PdfPCell productSerialNumberDetail;
				PdfPCell productAmountDetail;

				boolean isGroup = false;
				if(groupByProduct.get(productId).size() > 1) {
					isGroup = true;
					productCodeDetail = new PdfPCell(new Paragraph(gtin, fontDetails));
					productDescriptionDetail = new PdfPCell(new Paragraph(sd.getProduct().getDescription() + " (" + String.valueOf(sd.getProduct().getCode()) + ")", fontDetails));
					productBatchDetail = new PdfPCell(new Paragraph("", fontDetails));
					productExpirationDateDetail = (new PdfPCell(new Paragraph("", fontDetails)));
					productSerialNumberDetail = new PdfPCell(new Paragraph("", fontDetails));
					Integer total = 0;
					for(SupplyingDetail supplyingDetail : groupByProduct.get(productId)){
						total += supplyingDetail.getAmount();
					}
					productAmountDetail = new PdfPCell(new Paragraph(String.valueOf(total), fontDetails));

					productCodeDetail.setBorder(Rectangle.NO_BORDER);
					productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
					productBatchDetail.setBorder(Rectangle.NO_BORDER);
					productExpirationDateDetail.setBorder(Rectangle.NO_BORDER);
					productSerialNumberDetail.setBorder(Rectangle.NO_BORDER);
					productAmountDetail.setBorder(Rectangle.NO_BORDER);

					table.addCell(productCodeDetail);
					table.addCell(productDescriptionDetail);
					table.addCell(productBatchDetail);
					table.addCell(productExpirationDateDetail);
					table.addCell(productSerialNumberDetail);
					table.addCell(productAmountDetail);
				}
				for (SupplyingDetail supplyingDetail : groupByProduct.get(productId)) {
					gtin = "-";
					if (supplyingDetail.getGtin() != null) {
						gtin = supplyingDetail.getGtin().getNumber();
					}
					productCodeDetail = new PdfPCell(new Paragraph(gtin, fontDetails));

					String productDescription = "";
					if (!supplyingDetail.getInStock()) {
						productDescription += "(*) ";
					}
					if(!isGroup){
						productDescription += supplyingDetail.getProduct().getDescription() + " (" + String.valueOf(supplyingDetail.getProduct().getCode()) + ")";
					}
					productDescriptionDetail = new PdfPCell(new Paragraph(productDescription, fontDetails));
					productBatchDetail = new PdfPCell(new Paragraph(supplyingDetail.getBatch(), fontDetails));
					productExpirationDateDetail = (new PdfPCell(new Paragraph(dateFormatter.format(supplyingDetail.getExpirationDate()), fontDetails)));
					String serialNumber = "-";
					if (supplyingDetail.getSerialNumber() != null) {
						serialNumber = supplyingDetail.getSerialNumber();
					}
					productSerialNumberDetail = new PdfPCell(new Paragraph(serialNumber, fontDetails));
					String amount = String.valueOf(supplyingDetail.getAmount());
					productAmountDetail = new PdfPCell(new Paragraph(amount, fontDetails));

					productCodeDetail.setBorder(Rectangle.NO_BORDER);
					productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
					productBatchDetail.setBorder(Rectangle.NO_BORDER);
					productExpirationDateDetail.setBorder(Rectangle.NO_BORDER);
					productSerialNumberDetail.setBorder(Rectangle.NO_BORDER);
					productAmountDetail.setBorder(Rectangle.NO_BORDER);

					table.addCell(productCodeDetail);
					table.addCell(productDescriptionDetail);
					table.addCell(productBatchDetail);
					table.addCell(productExpirationDateDetail);
					table.addCell(productSerialNumberDetail);
					table.addCell(productAmountDetail);
				}
			}
			document.add(table);
			document.newPage();
		}
	}

	private HashMap<Integer, List<SupplyingDetail>> groupByProduct(Supplying supplying) {
		HashMap<Integer,List<SupplyingDetail>> details = new HashMap<>();

		for(SupplyingDetail supplyingDetail : supplying.getSupplyingDetails()){
			List<SupplyingDetail> list = details.get(supplyingDetail.getProduct().getId());
			if(list == null) {
				list = new ArrayList<>();
			}
			list.add(supplyingDetail);
			details.put(supplyingDetail.getProduct().getId(),list);
		}

		return details;
	}
}
