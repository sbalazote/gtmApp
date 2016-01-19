package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.Stock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class StockPdfView extends AbstractPdfView {

	// Las coordenadas se miden en puntos ('user points'). 1 pulgada se divide en 72 puntos, por lo tanto 1 milimetro equivale a 2.8346 puntos.
	private float MILLIMITER_TO_POINTS_FACTOR = 2.8346f;

	private String logoPath;
	private String product;
	private String batchNumber;
	private String expirateDateFrom;
	private String expirateDateTo;
	private String serialNumber;
	private String agreement;
	private String productMonodrug;

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		// Logo
		String logoPath;
		String realPath = getServletContext().getRealPath("/images/uploadedLogo.png");

		File file = new File(realPath);

		//Image logo;
		if(file.exists()) {
			logoPath = realPath;
		} else {
			logoPath = getServletContext().getRealPath("/images/logo.png");
		}

		this.logoPath = logoPath;

		this.product = (String) model.get("product");
		this.batchNumber = (String) model.get("batchNumber");
		this.expirateDateFrom = (String) model.get("expirateDateFrom");
		this.expirateDateTo = (String) model.get("expirateDateTo");
		this.serialNumber = (String) model.get("serialNumber");
		this.agreement = (String) model.get("agreement");
		this.productMonodrug = (String) model.get("monodrug");

		document.setPageSize(PageSize.A4);
		HeaderFooter event = new HeaderFooter();
		writer.setPageEvent(event);
		document.open();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<Stock> stockList = (List<Stock>) model.get("stocks");

		HashMap<Integer,List<Stock>> groupByProduct = groupByProduct(stockList);

		PdfPTable table = new PdfPTable(7); // 7 columnas
		table.setWidthPercentage(100);
		table.setSpacingBefore(5f);
		table.setSpacingAfter(5f);
		float[] columnWidths = {2f, 3f, 2f, 1.5f, 2f, 3.5f, 1f};
		table.setWidths(columnWidths);

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
				stockGtinDetail = new PdfPCell(new Paragraph(gtin, PdfConstants.fontDetails));
				stockDescriptionDetail = new PdfPCell(new Paragraph(id.getProduct().getDescription() + " (" + String.valueOf(id.getProduct().getCode()) + ")", PdfConstants.fontDetails));
				stockAgreementDetail = new PdfPCell(new Paragraph("", PdfConstants.fontDetails));
				stockBatchDetail = new PdfPCell(new Paragraph("", PdfConstants.fontDetails));
				stockExpirationDateDetail = (new PdfPCell(new Paragraph("", PdfConstants.fontDetails)));
				stockSerialNumberDetail = new PdfPCell(new Paragraph("", PdfConstants.fontDetails));
				Integer total = 0;
				for(Stock inputDetail : groupByProduct.get(productId)){
					total += inputDetail.getAmount();
				}
				stockAmountDetail = new PdfPCell(new Paragraph(String.valueOf(total), PdfConstants.fontDetails));

				stockGtinDetail.setBorder(Rectangle.RIGHT);
				stockDescriptionDetail.setBorder(Rectangle.RIGHT);
				stockAgreementDetail.setBorder(Rectangle.RIGHT);
				stockBatchDetail.setBorder(Rectangle.RIGHT);
				stockExpirationDateDetail.setBorder(Rectangle.RIGHT);
				stockSerialNumberDetail.setBorder(Rectangle.RIGHT);
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
				stockGtinDetail = new PdfPCell(new Paragraph(gtin, PdfConstants.fontDetails));
				String stockDescription = "";
				if(!isGroup){
					stockDescription = inputDetail.getProduct().getDescription() + " (" + String.valueOf(inputDetail.getProduct().getCode()) + ")";
				}
				stockDescriptionDetail = new PdfPCell(new Paragraph(stockDescription, PdfConstants.fontDetails));
				stockAgreementDetail = new PdfPCell(new Paragraph(inputDetail.getAgreement().getDescription(), PdfConstants.fontDetails));
				stockBatchDetail = new PdfPCell(new Paragraph(inputDetail.getBatch(), PdfConstants.fontDetails));
				stockExpirationDateDetail = (new PdfPCell(new Paragraph(dateFormatter.format(inputDetail.getExpirationDate()), PdfConstants.fontDetails)));

				String serialNumber = "-";
				if(inputDetail.getSerialNumber() != null){
					serialNumber = inputDetail.getSerialNumber();
				}
				stockSerialNumberDetail = new PdfPCell(new Paragraph(serialNumber, PdfConstants.fontDetails));
				stockAmountDetail = new PdfPCell(new Paragraph(String.valueOf(inputDetail.getAmount()), PdfConstants.fontDetails));

				stockGtinDetail.setBorder(Rectangle.RIGHT);
				stockDescriptionDetail.setBorder(Rectangle.RIGHT);
				stockAgreementDetail.setBorder(Rectangle.RIGHT);
				stockBatchDetail.setBorder(Rectangle.RIGHT);
				stockExpirationDateDetail.setBorder(Rectangle.RIGHT);
				stockSerialNumberDetail.setBorder(Rectangle.RIGHT);
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

	/** Inner class to add a header and a footer. */
	class HeaderFooter extends PdfPageEventHelper {

		private final Phrase[] header = new Phrase[2];
		private int pagenumber;

		public HeaderFooter() {
		}

		@Override
		public void onOpenDocument(PdfWriter writer, Document document) {
			this.header[0] = new Phrase("");
		}

		@Override
		public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
			this.header[1] = new Phrase(title.getContent());
			this.pagenumber = 1;
		}

		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			this.pagenumber++;

			try {
				Font fontHeader = new Font(Font.HELVETICA, 10f, Font.NORMAL, Color.BLACK);
				Font fontHeaderName = new Font(Font.HELVETICA, 14f, Font.NORMAL, Color.BLACK);
				Font fontHeaderBold = new Font(Font.HELVETICA, 9f, Font.BOLD, Color.BLACK);
				Font fontHeaderProvisioningRequest = new Font(Font.HELVETICA, 14f, Font.BOLD, Color.BLACK);

				Image logoImage = Image.getInstance(logoPath);
				logoImage.scaleToFit(80f, 80f);
				logoImage.setAbsolutePosition(10f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 15.0f) * MILLIMITER_TO_POINTS_FACTOR);
				document.add(logoImage);

				String name = PropertyProvider.getInstance().getProp("name");

				PdfContentByte canvas = writer.getDirectContent();

				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(name.toUpperCase(), fontHeaderName), 45.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 13.0f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("STOCK", fontHeader), 45.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 18.0f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				String timestamp = new SimpleDateFormat("'Impreso:' dd/MM/yyyy HH:mm:ss'hs.'").format(new Date());
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(timestamp, fontHeader), 135.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 18.0f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Separador del logo y resto
				LineSeparator headerSeparator = new LineSeparator();
				headerSeparator.drawLine(canvas, 15.0f * MILLIMITER_TO_POINTS_FACTOR, 195.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 25f) * MILLIMITER_TO_POINTS_FACTOR);

				// imprimo Producto.
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Producto: " + product, fontHeaderProvisioningRequest), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 30f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Lote y Vencimiento (desde y hasta).
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Lote: ", fontHeader), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(batchNumber, fontHeaderProvisioningRequest), 25.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Venc. Desde: ", fontHeader), 70.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(expirateDateFrom, fontHeaderProvisioningRequest), 95.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Venc. Hasta: ", fontHeader), 140.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(expirateDateTo, fontHeaderProvisioningRequest), 165.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Nro. de Serie.
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Nro. de Serie: ", fontHeader), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(serialNumber, fontHeaderBold), 40.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Convenio.
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Convenio: ", fontHeader), 105.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(agreement, fontHeaderBold), 125.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Monodroga.
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Monodroga: ", fontHeader), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 45f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(productMonodrug, fontHeaderBold), 40.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 45f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				//headerSeparator = new LineSeparator();
				headerSeparator.drawLine(canvas, 15.0f * MILLIMITER_TO_POINTS_FACTOR, 195.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 47f) * MILLIMITER_TO_POINTS_FACTOR);

				for (int i = 0; i < 5; i++) {
					document.add(Chunk.NEWLINE);
				}

				PdfPTable table = new PdfPTable(7); // 7 columnas
				table.setWidthPercentage(100);
				table.setSpacingBefore(5f);
				//table.setSpacingAfter(5f);
				float[] columnWidths = {2f, 3f, 2f, 1.5f, 2f, 3.5f, 1f};
				table.setWidths(columnWidths);

				//Encabezado
				PdfPCell stockGtinHeader = new PdfPCell(new Paragraph("GTIN.", fontHeader));
				PdfPCell stockDescriptionHeader = new PdfPCell(new Paragraph("Descripcion (Cod.)", fontHeader));
				PdfPCell stockAgreementHeader = new PdfPCell(new Paragraph("Convenio", fontHeader));
				PdfPCell stockBatchHeader = new PdfPCell(new Paragraph("Lote", fontHeader));
				PdfPCell stockExpirationDateHeader = new PdfPCell(new Paragraph("Vto.", fontHeader));
				PdfPCell stockSerialNumberHeader = new PdfPCell(new Paragraph("Serie", fontHeader));
				PdfPCell stockAmountHeader = new PdfPCell(new Paragraph("Cant.", fontHeader));

				stockGtinHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT);
				stockDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT);
				stockAgreementHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT);
				stockBatchHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT);
				stockExpirationDateHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT);
				stockSerialNumberHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT);
				stockAmountHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(stockGtinHeader);
				table.addCell(stockDescriptionHeader);
				table.addCell(stockAgreementHeader);
				table.addCell(stockBatchHeader);
				table.addCell(stockExpirationDateHeader);
				table.addCell(stockSerialNumberHeader);
				table.addCell(stockAmountHeader);
				document.add(table);

			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte canvas = writer.getDirectContent();
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(String.format("Pág. %s", this.pagenumber)), 105.0f * MILLIMITER_TO_POINTS_FACTOR, 7.0f * MILLIMITER_TO_POINTS_FACTOR, 0f);
		}
	}
}