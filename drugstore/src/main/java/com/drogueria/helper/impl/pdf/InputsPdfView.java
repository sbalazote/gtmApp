package com.drogueria.helper.impl.pdf;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Input;
import com.drogueria.model.InputDetail;
import com.drogueria.service.PropertyService;
import com.drogueria.util.StringUtility;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.pdf.draw.VerticalPositionMark;
import org.springframework.beans.factory.annotation.Autowired;

public class InputsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		document.open();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<Input> inputs = (List<Input>) model.get("inputs");

		// Fuentes
		Font fontHeader = new Font(Font.TIMES_ROMAN, 11f, Font.NORMAL, Color.BLACK);
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);
		// Logo
		String absoluteDiskPath = getServletContext().getRealPath("/images/logo.png");
		Image logo = Image.getInstance(absoluteDiskPath);
		logo.scaleToFit(50f, 50f);
		logo.setAbsolutePosition(10f * 2.8346f, 190f * 2.8346f);

		String name = PropertyProvider.getInstance().getProp("name");

		for (Input input : inputs) {

			PdfPTable table = new PdfPTable(6); // 6 columnas
			table.setWidthPercentage(95);
			table.setSpacingBefore(10f);

			table.setSpacingAfter(10f);
			float[] columnWidths = {2f, 4f, 3f, 2f, 3f, 1f};

			table.setWidths(columnWidths);

			//Encabezado

			PdfPCell productCodeHeader = new PdfPCell(new Paragraph("GTIN."));
			PdfPCell productDescriptionHeader = new PdfPCell(new Paragraph("Descripcion (Cod.)"));
			PdfPCell productBatchHeader = new PdfPCell(new Paragraph("Lote"));
			PdfPCell productExpirationDateHeader = new PdfPCell(new Paragraph("Vto."));
			PdfPCell productSerialNumberHeader = new PdfPCell(new Paragraph("Serie"));
			PdfPCell productAmountHeader = new PdfPCell(new Paragraph("Cant."));

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
			cb.showText("Fecha: " + dateFormatter.format(input.getDate()));
			cb.endText();

			// INGRESO NRO
			cb.beginText();
			cb.setTextMatrix(230 * 2.8346f, 195 * 2.8346f);
			cb.showText("Ingreso Nro.: " + StringUtility.addLeadingZeros(input.getId().toString(), 8));
			cb.endText();

			// DOC. NRO
			cb.beginText();
			cb.setTextMatrix(230 * 2.8346f, 190 * 2.8346f);
			String POS = input.getDeliveryNoteNumber().substring(0, 4);
			String number = input.getDeliveryNoteNumber().substring(4, 12);
			cb.showText("Doc. Nro.: R" + POS + "-" + number);
			cb.endText();

			document.add(logo);

			document.add(Chunk.NEWLINE);


			LineSeparator ls = new LineSeparator();
			document.add(new Chunk(ls));

			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Convenio: ", fontHeader));
			String codeAgreement = StringUtility.addLeadingZeros(String.valueOf(input.getAgreement().getCode()),5);
			Chunk description = new Chunk(codeAgreement + " - " + input.getAgreement().getDescription(), fontHeader);
			document.add(description);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Cliente/Proveedor: ", fontHeader));
			Chunk active = new Chunk(input.getClientOrProviderDescription(), fontHeader);
			document.add(active);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Concepto: ", fontHeader));
			String conceptCode = StringUtility.addLeadingZeros(input.getConcept().getCode(),4);
			Chunk code = new Chunk(conceptCode + " - " + input.getConcept().getDescription(), fontHeader);
			document.add(code);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Orden de Compra: ", fontHeader));
			Chunk purchaseNumber = new Chunk(input.getPurchaseOrderNumber(), fontHeader);
			document.add(purchaseNumber);
			document.add(Chunk.NEWLINE);

			Chunk tab = new Chunk(new VerticalPositionMark(), 150, true);

			document.add(new Chunk("GLN Origen: ", fontHeader));
			String originGLNString = input.getOriginGln();
			if(originGLNString == null){
				originGLNString = " - ";
			}
			Chunk originGLN = new Chunk(originGLNString, fontHeader);
			document.add(originGLN);

			document.add(tab);

			document.add(new Chunk("GLN Destino: ", fontHeader));
			String destinationGLNString = (String)model.get("destinationGln");
			if(destinationGLNString == null){
				destinationGLNString = " - ";
			}
			Chunk destinationGLN = new Chunk(destinationGLNString, fontHeader);
			document.add(destinationGLN);
			document.add(Chunk.NEWLINE);

			for (InputDetail inputDetail : input.getInputDetails()) {
				String gtin = "-";
				if(inputDetail.getGtin() != null){
					gtin = inputDetail.getGtin().getNumber();
				}
				PdfPCell productCodeDetail = new PdfPCell(new Paragraph(gtin, fontDetails));
				PdfPCell productDescriptionDetail = new PdfPCell(new Paragraph(inputDetail.getProduct().getDescription() + " (" + String.valueOf(inputDetail.getProduct().getCode()) + ")", fontDetails));
				PdfPCell productBatchDetail = new PdfPCell(new Paragraph(inputDetail.getBatch(), fontDetails));
				PdfPCell productExpirationDateDetail = (new PdfPCell(new Paragraph(dateFormatter.format(inputDetail.getExpirationDate()), fontDetails)));

				String serialNumber = "-";
				if(inputDetail.getSerialNumber() != null){
					serialNumber = inputDetail.getSerialNumber();
				}
				PdfPCell productSerialNumberDetail = new PdfPCell(new Paragraph(serialNumber, fontDetails));
				PdfPCell productAmountDetail = new PdfPCell(new Paragraph(String.valueOf(inputDetail.getAmount()), fontDetails));

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
			document.add(table);
			document.newPage();
		}
	}
}
