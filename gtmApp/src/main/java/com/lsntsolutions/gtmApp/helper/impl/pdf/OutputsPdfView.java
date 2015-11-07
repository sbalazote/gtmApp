package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.model.OutputDetail;
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

public class OutputsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		document.open();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<Output> outputs = (List<Output>) model.get("outputs");
		Map<Integer, List<DeliveryNote>> associatedOutputs = (Map<Integer, List<DeliveryNote>>) model.get("associatedOutputs");

		// Fuentes
		Font fontHeader = new Font(Font.TIMES_ROMAN, 11f, Font.NORMAL, Color.BLACK);
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);
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

		for (Output output : outputs) {

			HashMap<Integer,List<OutputDetail>> groupByProduct = groupByProduct(output);

			PdfPTable table = new PdfPTable(6); // 6 columnas
			table.setWidthPercentage(95);
			table.setSpacingBefore(10f);

			table.setSpacingAfter(10f);
			float[] columnWidths = {1f, 7f, 1.5f, 0.7f, 1.7f, 0.6f};

			table.setWidths(columnWidths);

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
			cb.showText("Fecha: " + dateFormatter.format(output.getDate()));
			cb.endText();

			// INGRESO NRO
			cb.beginText();
			cb.setTextMatrix(230 * 2.8346f, 195 * 2.8346f);
			cb.showText("Egreso Nro.: " + StringUtility.addLeadingZeros(output.getId().toString(), 8));
			cb.endText();

			// DOC. NRO
			List<DeliveryNote> outputDeliveryNotes = associatedOutputs.get(new Integer(output.getId()));
			String dnNumbers = "";
			if (outputDeliveryNotes != null) {
				int offsetY = 190;
				cb.beginText();
				cb.setTextMatrix(230 * 2.8346f, offsetY * 2.8346f);
				cb.showText("Doc. Nro.: " + dnNumbers);
				cb.endText();
				for (DeliveryNote elem : outputDeliveryNotes) {
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
			String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(output.getAgreement().getCode()),5);
			Chunk description = new Chunk(codeAgreement + " - " + output.getAgreement().getDescription(), fontHeader);
			document.add(description);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Cliente/Proveedor: ", fontHeader));
			Chunk active = new Chunk(output.getClientOrProviderDescription(), fontHeader);
			document.add(active);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Concepto: ", fontHeader));
			String conceptCode = StringUtility.addLeadingZeros(output.getConcept().getCode(),4);
			Chunk code = new Chunk(conceptCode + " - " + output.getConcept().getDescription(), fontHeader);
			document.add(code);
			document.add(Chunk.NEWLINE);

			for(Integer productId : groupByProduct.keySet()){
				String gtin = "-";
				OutputDetail id = groupByProduct.get(productId).get(0);
				if(id.getGtin() != null){
					gtin = id.getGtin().getNumber();
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
					productDescriptionDetail = new PdfPCell(new Paragraph(id.getProduct().getDescription() + " (" + String.valueOf(id.getProduct().getCode()) + ")", fontDetails));
					productBatchDetail = new PdfPCell(new Paragraph("", fontDetails));
					productExpirationDateDetail = (new PdfPCell(new Paragraph("", fontDetails)));
					productSerialNumberDetail = new PdfPCell(new Paragraph("", fontDetails));
					Integer total = 0;
					for(OutputDetail outputDetail : groupByProduct.get(productId)){
						total += outputDetail.getAmount();
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

				for (OutputDetail outputDetail : groupByProduct.get(productId)) {
					gtin = "-";
					if(outputDetail.getGtin() != null){
						gtin = outputDetail.getGtin().getNumber();
					}
					productCodeDetail = new PdfPCell(new Paragraph(gtin, fontDetails));
					String productDescription = "";
					if(!isGroup){
						productDescription = outputDetail.getProduct().getDescription() + " (" + String.valueOf(outputDetail.getProduct().getCode()) + ")";
					}
					productDescriptionDetail = new PdfPCell(new Paragraph(productDescription, fontDetails));
					productBatchDetail = new PdfPCell(new Paragraph(outputDetail.getBatch(), fontDetails));
					productExpirationDateDetail = (new PdfPCell(new Paragraph(dateFormatter.format(outputDetail.getExpirationDate()), fontDetails)));
					String serialNumber = "-";
					if(outputDetail.getSerialNumber() != null){
						serialNumber = outputDetail.getSerialNumber();
					}
					productSerialNumberDetail = new PdfPCell(new Paragraph(serialNumber, fontDetails));
					productAmountDetail = new PdfPCell(new Paragraph(String.valueOf(outputDetail.getAmount()), fontDetails));

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

	private HashMap<Integer, List<OutputDetail>> groupByProduct(Output output) {
		HashMap<Integer,List<OutputDetail>> details = new HashMap<>();

		for(OutputDetail outputDetail : output.getOutputDetails()){
			List<OutputDetail> list = details.get(outputDetail.getProduct().getId());
			if(list == null) {
				list = new ArrayList<>();
			}
			list.add(outputDetail);
			details.put(outputDetail.getProduct().getId(),list);
		}

		return details;
	}
}
