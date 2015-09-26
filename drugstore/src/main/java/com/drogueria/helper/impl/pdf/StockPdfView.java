package com.drogueria.helper.impl.pdf;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Stock;
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
import java.util.List;

public class StockPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		document.open();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<Stock> stockList = (List<Stock>) model.get("stocks");

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

		//for (Stock stock : stockList) {

			HashMap<Integer,List<Stock>> groupByProduct = groupByProduct(stockList);

			PdfPTable table = new PdfPTable(7); // 7 columnas
			table.setWidthPercentage(95);
			table.setSpacingBefore(10f);

			table.setSpacingAfter(10f);
			float[] columnWidths = {1f, 4f, 2f, 1.5f, 0.7f, 1.7f, 0.6f};

			table.setWidths(columnWidths);

			//Encabezado

			PdfPCell stockGtinHeader = new PdfPCell(new Paragraph("GTIN."));
			PdfPCell stockDescriptionHeader = new PdfPCell(new Paragraph("Descripcion (Cod.)"));
			PdfPCell stockAgreementHeader = new PdfPCell(new Paragraph("Convenio"));
			PdfPCell stockBatchHeader = new PdfPCell(new Paragraph("Lote"));
			PdfPCell stockExpirationDateHeader = new PdfPCell(new Paragraph("Vto."));
			PdfPCell stockSerialNumberHeader = new PdfPCell(new Paragraph("Serie"));
			PdfPCell stockAmountHeader = new PdfPCell(new Paragraph("Cant."));

			stockGtinHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			stockDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			stockAgreementHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			stockBatchHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			stockExpirationDateHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			stockSerialNumberHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			stockAmountHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

			table.addCell(stockGtinHeader);
			table.addCell(stockDescriptionHeader);
			table.addCell(stockAgreementHeader);
			table.addCell(stockBatchHeader);
			table.addCell(stockExpirationDateHeader);
			table.addCell(stockSerialNumberHeader);
			table.addCell(stockAmountHeader);

			// add text at an absolute position
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);

			// NOMBRE MEMBRETE
			cb.beginText();
			cb.setFontAndSize(bf_times, 16f);
			cb.setTextMatrix(40f * 2.8346f, 195f * 2.8346f);
			cb.showText(name);
			cb.endText();

			document.add(logo);

			document.add(Chunk.NEWLINE);

			LineSeparator ls = new LineSeparator();
			document.add(new Chunk(ls));

			document.add(Chunk.NEWLINE);

            for(Integer productId : groupByProduct.keySet()){
                String gtin = "-";
                Stock id = groupByProduct.get(productId).get(0);
                if(id.getGtin() != null){
                    gtin = id.getGtin().getNumber();
                }
                PdfPCell stockGtinDetail;
                PdfPCell stockDescriptionDetail;
				PdfPCell stockAgreementDetail;
                PdfPCell stockBatchDetail;
                PdfPCell stockExpirationDateDetail;
                PdfPCell stockSerialNumberDetail;
                PdfPCell stockAmountDetail;

				boolean isGroup = false;
                if(groupByProduct.get(productId).size() > 1) {
					isGroup = true;
                    stockGtinDetail = new PdfPCell(new Paragraph(gtin, fontDetails));
                    stockDescriptionDetail = new PdfPCell(new Paragraph(id.getProduct().getDescription() + " (" + String.valueOf(id.getProduct().getCode()) + ")", fontDetails));
					stockAgreementDetail = new PdfPCell(new Paragraph("", fontDetails));
                    stockBatchDetail = new PdfPCell(new Paragraph("", fontDetails));
                    stockExpirationDateDetail = (new PdfPCell(new Paragraph("", fontDetails)));
                    stockSerialNumberDetail = new PdfPCell(new Paragraph("", fontDetails));
                    Integer total = 0;
                    for(Stock inputDetail : groupByProduct.get(productId)){
                        total += inputDetail.getAmount();
                    }
                    stockAmountDetail = new PdfPCell(new Paragraph(String.valueOf(total), fontDetails));

                    stockGtinDetail.setBorder(Rectangle.NO_BORDER);
                    stockDescriptionDetail.setBorder(Rectangle.NO_BORDER);
					stockAgreementDetail.setBorder(Rectangle.NO_BORDER);
                    stockBatchDetail.setBorder(Rectangle.NO_BORDER);
                    stockExpirationDateDetail.setBorder(Rectangle.NO_BORDER);
                    stockSerialNumberDetail.setBorder(Rectangle.NO_BORDER);
                    stockAmountDetail.setBorder(Rectangle.NO_BORDER);

                    table.addCell(stockGtinDetail);
                    table.addCell(stockDescriptionDetail);
					table.addCell(stockAgreementDetail);
                    table.addCell(stockBatchDetail);
                    table.addCell(stockExpirationDateDetail);
                    table.addCell(stockSerialNumberDetail);
                    table.addCell(stockAmountDetail);
                }

                for (Stock inputDetail : groupByProduct.get(productId)) {
                    gtin = "-";
                    if(inputDetail.getGtin() != null){
                        gtin = inputDetail.getGtin().getNumber();
                    }
                    stockGtinDetail = new PdfPCell(new Paragraph(gtin, fontDetails));
					String stockDescription = "";
					if(!isGroup){
						stockDescription = inputDetail.getProduct().getDescription() + " (" + String.valueOf(inputDetail.getProduct().getCode()) + ")";
					}
                    stockDescriptionDetail = new PdfPCell(new Paragraph(stockDescription, fontDetails));
					stockAgreementDetail = new PdfPCell(new Paragraph(inputDetail.getAgreement().getDescription(), fontDetails));
                    stockBatchDetail = new PdfPCell(new Paragraph(inputDetail.getBatch(), fontDetails));
                    stockExpirationDateDetail = (new PdfPCell(new Paragraph(dateFormatter.format(inputDetail.getExpirationDate()), fontDetails)));

                    String serialNumber = "-";
                    if(inputDetail.getSerialNumber() != null){
                        serialNumber = inputDetail.getSerialNumber();
                    }
                    stockSerialNumberDetail = new PdfPCell(new Paragraph(serialNumber, fontDetails));
                    stockAmountDetail = new PdfPCell(new Paragraph(String.valueOf(inputDetail.getAmount()), fontDetails));

                    stockGtinDetail.setBorder(Rectangle.NO_BORDER);
                    stockDescriptionDetail.setBorder(Rectangle.NO_BORDER);
					stockAgreementDetail.setBorder(Rectangle.NO_BORDER);
                    stockBatchDetail.setBorder(Rectangle.NO_BORDER);
                    stockExpirationDateDetail.setBorder(Rectangle.NO_BORDER);
                    stockSerialNumberDetail.setBorder(Rectangle.NO_BORDER);
                    stockAmountDetail.setBorder(Rectangle.NO_BORDER);

                    table.addCell(stockGtinDetail);
                    table.addCell(stockDescriptionDetail);
					table.addCell(stockAgreementDetail);
                    table.addCell(stockBatchDetail);
                    table.addCell(stockExpirationDateDetail);
                    table.addCell(stockSerialNumberDetail);
                    table.addCell(stockAmountDetail);
                }
            }
			document.add(table);
			document.newPage();
		//}
	}

	private HashMap<Integer, List<Stock>> groupByProduct(List<Stock> stocks) {
		HashMap<Integer,List<Stock>> details = new HashMap<>();

		for(Stock stock : stocks){
			List<Stock> list = details.get(stock.getProduct().getId());
			if(list == null) {
				list = new ArrayList<>();
			}
			list.add(stock);
			details.put(stock.getProduct().getId(),list);
		}

		return details;
	}
}
