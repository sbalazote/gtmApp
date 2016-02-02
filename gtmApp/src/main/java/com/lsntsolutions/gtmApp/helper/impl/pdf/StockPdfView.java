package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.ProductGtin;
import com.lsntsolutions.gtmApp.model.Stock;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class StockPdfView extends AbstractPdfView {

    private static final Logger logger = Logger.getLogger(StockPdfView.class);

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
    private PdfPTable table;
    private PdfPCell col1Detail;
    private PdfPCell col2Detail;
    private PdfPCell col3Detail;
    private PdfPCell col4Detail;
    private PdfPCell col5Detail;
    private PdfPCell col6Detail;

    private Font fontDetails;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        fontDetails = new Font(Font.TIMES_ROMAN, 10f, Font.BOLD, Color.BLACK);


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

        //document.setPageSize(PageSize.A4);
        HeaderFooter event = new HeaderFooter();
        writer.setPageEvent(event);
        document.open();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressWarnings("unchecked")
        List<Stock> stockList = (List<Stock>) model.get("stocks");

        TreeMap<String, List<Stock>> inputDetail = groupByProductAndBatch(stockList);

        TreeMap<String, Integer> productsCount = countByProduct(stockList);

        table = new PdfPTable(6); // 6 columnas
        table.setWidthPercentage(100);
        table.setSpacingBefore(5f);
        table.setSpacingAfter(5f);
        float[] columnWidths = {2f, 3f, 3f, 3f, 3f, 1f};
        table.setWidths(columnWidths);

        // recorro el mapa de detalle de productos a imprimir.
        for (Map.Entry<String, List<Stock>> entry : inputDetail.entrySet()) {
            String key = entry.getKey();
            List<Stock> details = entry.getValue();
            String[] parts = key.split(",");
            String currentProductId = parts[0];
            String productType = details.get(0).getProduct().getType();

            ProductGtin productGtin = details.get(0).getGtin();
            String gtin = (productGtin != null) ? productGtin.getNumber() : "";
            String description = details.get(0).getProduct().getDescription();
            Integer code = details.get(0).getProduct().getCode();
            String agreement = details.get(0).getAgreement().getDescription();
            String batch = details.get(0).getBatch();
            String expirationDate = dateFormatter.format(details.get(0).getExpirationDate());
            int totalAmount = details.get(0).getAmount();
            String batchAmount = Integer.toString(productType.equals("BE") ? totalAmount : details.size());

            col1Detail = new PdfPCell(new Paragraph(gtin, PdfConstants.fontHeader));
            col2Detail = new PdfPCell(new Paragraph(description + " (" + String.valueOf(code) + ")", PdfConstants.fontHeader));
            col3Detail = new PdfPCell(new Paragraph(agreement, PdfConstants.fontHeader));
            col4Detail = new PdfPCell(new Paragraph(batch, PdfConstants.fontHeader));
            col5Detail = (new PdfPCell(new Paragraph(expirationDate, PdfConstants.fontHeader)));
            Integer total = productsCount.get(currentProductId);
            col6Detail = new PdfPCell(new Paragraph(batchAmount + " / " + String.valueOf(total), PdfConstants.fontHeader));

            col1Detail.setBorder(Rectangle.NO_BORDER);
            col2Detail.setBorder(Rectangle.NO_BORDER);
            col3Detail.setBorder(Rectangle.NO_BORDER);
            col4Detail.setBorder(Rectangle.NO_BORDER);
            col5Detail.setBorder(Rectangle.NO_BORDER);
            col6Detail.setBorder(Rectangle.NO_BORDER);

            table.addCell(col1Detail);
            table.addCell(col2Detail);
            table.addCell(col3Detail);
            table.addCell(col4Detail);
            table.addCell(col5Detail);
            table.addCell(col6Detail);

            if (!productType.equals("BE")) {
                List<Stock> detailAux = new ArrayList<>();
                Iterator<Stock> it = details.iterator();
                int serialIdx = 0;
                while (it.hasNext()) {
                    Stock od = it.next();
                    detailAux.add(od);

                    serialIdx++;

                    if ((serialIdx == 4) || (!it.hasNext())) {
                        printSerialDetails(detailAux);
                        serialIdx = 0;
                        detailAux = new ArrayList<>();
                    }
                }
            }
        }
        document.add(table);
    }

    private void printSerialDetails(List<Stock> orderDetails) {

        switch (orderDetails.size()) {
            case 1: {
                col1Detail = new PdfPCell(new Paragraph("", fontDetails));
                col2Detail = new PdfPCell(new Paragraph(orderDetails.get(0).getSerialNumber(), fontDetails));
                col3Detail = new PdfPCell(new Paragraph("", fontDetails));
                col4Detail = new PdfPCell(new Paragraph("", fontDetails));
                col5Detail = new PdfPCell(new Paragraph("", fontDetails));
                col6Detail = new PdfPCell(new Paragraph("", fontDetails));
                break;
            }
            case 2: {
                col1Detail = new PdfPCell(new Paragraph("", fontDetails));
                col2Detail = new PdfPCell(new Paragraph(orderDetails.get(0).getSerialNumber(), fontDetails));
                col3Detail = new PdfPCell(new Paragraph(orderDetails.get(1).getSerialNumber(), fontDetails));
                col4Detail = new PdfPCell(new Paragraph("", fontDetails));
                col5Detail = new PdfPCell(new Paragraph("", fontDetails));
                col6Detail = new PdfPCell(new Paragraph("", fontDetails));
                break;
            }
            case 3: {
                col1Detail = new PdfPCell(new Paragraph("", fontDetails));
                col2Detail = new PdfPCell(new Paragraph(orderDetails.get(0).getSerialNumber(), fontDetails));
                col3Detail = new PdfPCell(new Paragraph(orderDetails.get(1).getSerialNumber(), fontDetails));
                col4Detail = new PdfPCell(new Paragraph(orderDetails.get(2).getSerialNumber(), fontDetails));
                col5Detail = new PdfPCell(new Paragraph("", fontDetails));
                col6Detail = new PdfPCell(new Paragraph("", fontDetails));
                break;
            }
            case 4: {
                col1Detail = new PdfPCell(new Paragraph("", fontDetails));
                col2Detail = new PdfPCell(new Paragraph(orderDetails.get(0).getSerialNumber(), fontDetails));
                col3Detail = new PdfPCell(new Paragraph(orderDetails.get(1).getSerialNumber(), fontDetails));
                col4Detail = new PdfPCell(new Paragraph(orderDetails.get(2).getSerialNumber(), fontDetails));
                col5Detail = new PdfPCell(new Paragraph(orderDetails.get(3).getSerialNumber(), fontDetails));
                col6Detail = new PdfPCell(new Paragraph("", fontDetails));
                break;
            }
            default: {
                break;
            }
        }

        col1Detail.setBorder(Rectangle.NO_BORDER);
        col2Detail.setBorder(Rectangle.NO_BORDER);
        col3Detail.setBorder(Rectangle.NO_BORDER);
        col4Detail.setBorder(Rectangle.NO_BORDER);
        col5Detail.setBorder(Rectangle.NO_BORDER);
        col6Detail.setBorder(Rectangle.NO_BORDER);

        table.addCell(col1Detail);
        table.addCell(col2Detail);
        table.addCell(col3Detail);
        table.addCell(col4Detail);
        table.addCell(col5Detail);
        table.addCell(col6Detail);
    }

    private TreeMap<String, List<Stock>> groupByProductAndBatch(List<Stock> stocks) {
		TreeMap<String, List<Stock>> detailsMap = new TreeMap<>();

		for(Stock stock : stocks){
			String id = Integer.toString(stock.getProduct().getId());
			String batch = stock.getBatch();
			String key = id + "," + batch;

			List<Stock> list = (List<Stock>) detailsMap.get(key);
			if(list == null) {
				list = new ArrayList<>();
			}
			list.add(stock);
			detailsMap.put(key, list);
		}

		return detailsMap;
	}

	private TreeMap<String, Integer> countByProduct(List<Stock> details) {
		TreeMap<String, Integer> detailsMap = new TreeMap<>();

		for(Stock detail : details){
			String id = Integer.toString(detail.getProduct().getId());

			Integer amount = detailsMap.get(id);
			if(amount == null) {
				amount = 0;
			}
			amount += detail.getAmount();
			detailsMap.put(id, amount);
		}

		return detailsMap;
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
				logoImage.setAbsolutePosition(10f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 15.0f) * MILLIMITER_TO_POINTS_FACTOR);
				document.add(logoImage);

				String name = PropertyProvider.getInstance().getProp("name");

				PdfContentByte canvas = writer.getDirectContent();

				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(name.toUpperCase(), fontHeaderName), 45.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 13.0f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("STOCK", fontHeader), 45.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 18.0f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				String timestamp = new SimpleDateFormat("'Impreso:' dd/MM/yyyy HH:mm:ss'hs.'").format(new Date());
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(timestamp, fontHeader), 230.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 18.0f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Separador del logo y resto
				LineSeparator headerSeparator = new LineSeparator();
				headerSeparator.drawLine(canvas, 15.0f * MILLIMITER_TO_POINTS_FACTOR, 282.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 25f) * MILLIMITER_TO_POINTS_FACTOR);

				// imprimo Producto.
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Producto: " + product, fontHeaderProvisioningRequest), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 30f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Lote y Vencimiento (desde y hasta).
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Lote: ", fontHeader), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(batchNumber, fontHeaderProvisioningRequest), 25.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Venc. Desde: ", fontHeader), 120.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(expirateDateFrom, fontHeaderProvisioningRequest), 145.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Venc. Hasta: ", fontHeader), 240.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(expirateDateTo, fontHeaderProvisioningRequest), 265.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 35f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Nro. de Serie.
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Nro. de Serie: ", fontHeader), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(serialNumber, fontHeaderBold), 40.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Convenio.
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Convenio: ", fontHeader), 120.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(agreement, fontHeaderBold), 145.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				// imprimo Monodroga.
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Monodroga: ", fontHeader), 240.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(productMonodrug, fontHeaderBold), 265.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 40f) * MILLIMITER_TO_POINTS_FACTOR, 0f);

				//headerSeparator = new LineSeparator();
				headerSeparator.drawLine(canvas, 15.0f * MILLIMITER_TO_POINTS_FACTOR, 282.0f * MILLIMITER_TO_POINTS_FACTOR, (210.0f - 42f) * MILLIMITER_TO_POINTS_FACTOR);

				for (int i = 0; i < 5; i++) {
					document.add(Chunk.NEWLINE);
				}

				PdfPTable table = new PdfPTable(6); // 6 columnas
				table.setWidthPercentage(100);
				table.setSpacingBefore(5f);
				float[] columnWidths = {2f, 3f, 3f, 3f, 3f, 1f};
				table.setWidths(columnWidths);

				//Encabezado
				PdfPCell stockGtinHeader = new PdfPCell(new Paragraph("GTIN.", fontHeader));
				PdfPCell stockDescriptionHeader = new PdfPCell(new Paragraph("Descripcion (Cod.)", fontHeader));
				PdfPCell stockAgreementHeader = new PdfPCell(new Paragraph("Convenio", fontHeader));
				PdfPCell stockBatchHeader = new PdfPCell(new Paragraph("Lote", fontHeader));
				PdfPCell stockExpirationDateHeader = new PdfPCell(new Paragraph("Vto.", fontHeader));
				PdfPCell stockAmountHeader = new PdfPCell(new Paragraph("Cant.", fontHeader));

				stockGtinHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				stockDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				stockAgreementHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				stockBatchHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				stockExpirationDateHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
				stockAmountHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

				table.addCell(stockGtinHeader);
				table.addCell(stockDescriptionHeader);
				table.addCell(stockAgreementHeader);
				table.addCell(stockBatchHeader);
				table.addCell(stockExpirationDateHeader);
				table.addCell(stockAmountHeader);
				document.add(table);

			} catch (DocumentException | IOException e) {
                logger.error(e.getMessage());
			}
        }

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte canvas = writer.getDirectContent();
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(String.format("Pág. %s", this.pagenumber)), 148.5f * MILLIMITER_TO_POINTS_FACTOR, 7.0f * MILLIMITER_TO_POINTS_FACTOR, 0f);
		}
	}
}