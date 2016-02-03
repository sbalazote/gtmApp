package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.service.ProvisioningRequestService;
import com.lsntsolutions.gtmApp.service.ProvisioningRequestStateService;
import com.lsntsolutions.gtmApp.service.StockService;
import com.lsntsolutions.gtmApp.util.StringUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PickingSheetPrinterImpl extends AbstractPdfView {

	// Las coordenadas se miden en puntos ('user points'). 1 pulgada se divide en 72 puntos, por lo tanto 1 milimetro equivale a 2.8346 puntos.
	private float MILLIMITER_TO_POINTS_FACTOR = 2.8346f;

	private ProvisioningRequestService provisioningRequestService;
	private ProvisioningRequestStateService provisioningRequestStateService;
	private StockService stockService;
    private Document document;
    private PdfWriter writer;
    private String logoPath;
	private Float offset;

	private Document addHeader(ProvisioningRequest provisioningRequest, Boolean firstHalf) {
		Client client = provisioningRequest.getClient();
		Agreement agreement = provisioningRequest.getAgreement();
		String deliveryDate = new SimpleDateFormat("dd/MM/yyyy").format(provisioningRequest.getDeliveryDate());
		DeliveryLocation deliveryLocation = provisioningRequest.getDeliveryLocation();
		Affiliate affiliate = provisioningRequest.getAffiliate();

        try {

			Font fontHeader = new Font(Font.HELVETICA, 10f, Font.NORMAL, Color.BLACK);
			Font fontHeaderName = new Font(Font.HELVETICA, 14f, Font.NORMAL, Color.BLACK);
			Font fontHeaderBold = new Font(Font.HELVETICA, 9f, Font.BOLD, Color.BLACK);
			Font fontHeaderProvisioningRequest = new Font(Font.HELVETICA, 14f, Font.BOLD, Color.BLACK);

			Image logoImage = Image.getInstance(logoPath);
            logoImage.scaleToFit(80f, 80f);
            logoImage.setAbsolutePosition(10f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 15.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR);
			document.add(logoImage);

            String name = PropertyProvider.getInstance().getProp("name");

            PdfContentByte canvas = writer.getDirectContent();

			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(name.toUpperCase(), fontHeaderName), 45.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 13.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("HOJA DE PICKING", fontHeader), 45.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 18.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			String timestamp = new SimpleDateFormat("'Impreso:' dd/MM/yyyy HH:mm:ss'hs.'").format(new Date());
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(timestamp, fontHeader), 135.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 18.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Separador del logo y resto
            LineSeparator headerSeparator = new LineSeparator();
			headerSeparator.drawLine(canvas, 15.0f * MILLIMITER_TO_POINTS_FACTOR, 195.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 20f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR);

			// imprimo Pedido Nro.
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Pedido Nro.: " + provisioningRequest.getFormatId(), fontHeaderProvisioningRequest), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 30f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			String provisioningAgreementDescription =   StringUtility.addLeadingZeros(String.valueOf(agreement.getCode()), 5) + " - " + agreement.getDescription() + ")";
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("(Convenio: ", fontHeader), 73.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 30f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(provisioningAgreementDescription, fontHeaderBold), 92.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 30f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Fecha de Entrega
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Fecha de Entrega: ", fontHeader), 140.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 30f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(deliveryDate, fontHeaderProvisioningRequest), 170.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 30f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Cliente
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Cliente: ", fontHeader), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			String clientDescription = StringUtility.addLeadingZeros(String.valueOf(client.getCode()),4) + " - " + client.getName();
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(clientDescription, fontHeaderBold), 30.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Afiliado
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Afiliado: ", fontHeader), 105.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			String affiliateDetails = StringUtility.addLeadingZeros(affiliate.getCode(), 5) + " - " + affiliate.getName() + " " + affiliate.getSurname();
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(affiliateDetails, fontHeaderBold), 120.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Entregar en:
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Entregar en: ", fontHeader), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 40f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
            String deliveryLocationDescription = "(" + StringUtility.addLeadingZeros(String.valueOf(deliveryLocation.getCode().toString()), 8) + ") " + deliveryLocation.getName();
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(deliveryLocationDescription, fontHeaderBold), 40.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 40f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Direccion:
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Dirección: ", fontHeader), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 45f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			String deliveryLocationAddress = deliveryLocation.getLocality() + " - " + deliveryLocation.getAddress() + " (" + deliveryLocation.getProvince().getName() + ")";
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(deliveryLocationAddress, fontHeaderBold), 35.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 45f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Observaciones
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Observaciones: ", fontHeader), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 50f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(provisioningRequest.getComment(), fontHeaderBold), 45.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 50f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

		} catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
	}

	private Document addDetails(ProvisioningRequest provisioningRequest, Boolean firstHalf) throws DocumentException {
		PdfContentByte canvas = writer.getDirectContent();

		// imprimo Separador entre encabezado y detalle del producto
		LineSeparator headerSeparator = new LineSeparator();
		headerSeparator.drawLine(canvas, 15.0f * MILLIMITER_TO_POINTS_FACTOR, 195.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 55.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR);

		offset = 55.0f + (firstHalf ? 0.0f : 145f);

		// DETALLES
		Integer id = 0;
		Integer acumAmount = 0;
		Integer neededAmount = 0;
		String details = "";
		String gtin = "";
		String description = "";
		String batch = "";
		String expirationDate = "";

		Iterator<ProvisioningRequestDetail> it = provisioningRequest.getProvisioningRequestDetails().iterator();

		while (it.hasNext()) {
			ProvisioningRequestDetail prd = it.next();
			if ((!prd.getProduct().getId().equals(id)) && (id != 0)) {
				this.printDetail(gtin, description, acumAmount, details);
				acumAmount = 0;
				neededAmount = 0;
				details = "";
				gtin = "";
				description = "";
				batch = "";
				expirationDate = "";
				offset+=5;
			}

			id = prd.getProduct().getId();
			neededAmount = prd.getAmount();
			if (prd.getProduct().getType().equals("PS") || prd.getProduct().getType().equals("SS")) {
				gtin = (prd.getProduct().getLastGtin() != null) ? prd.getProduct().getLastGtin() : "-";
				description = prd.getProduct().getDescription();
				acumAmount = prd.getAmount();
				details = "";
			}

			// obtengo el stock ordenado por FEFO (First Expire - First Out.
			List<Stock> productStockInFEFOOrder = stockService.getBatchExpirationDateStock(id, provisioningRequest.getAgreement().getId());
			Iterator<Stock> it2 = productStockInFEFOOrder.iterator();
			while ((it2.hasNext()) && (neededAmount > acumAmount)) {
				Stock stock = it2.next();

				gtin = (prd.getProduct().getLastGtin() != null) ? prd.getProduct().getLastGtin() : "-";
				description = prd.getProduct().getDescription();
				batch = stock.getBatch();
				expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(stock.getExpirationDate());
				Integer stockAmount = stock.getAmount();
				Integer amount;
				if (stockAmount >= neededAmount) {
					amount = (neededAmount - acumAmount);
				} else {
					amount = stockAmount;
				}
				details += "Lote: " + batch + " - Vto.: " + expirationDate + "  - Cant.: " + amount + "\t";
				acumAmount += amount;
			}
		}
		this.printDetail(gtin, description, acumAmount, details);
        return document;
	}

	private void printDetail(String gtin, String description, Integer acumAmount, String details)
			throws DocumentException {
        PdfPTable header = new PdfPTable(3);
		header.setTotalWidth(180.0f * MILLIMITER_TO_POINTS_FACTOR);
		float[] columnWidths = {2f, 6f, 1f};
		header.setWidths(columnWidths);

        PdfPCell cell = new PdfPCell(new Paragraph(gtin));
        cell.setBorder(Rectangle.NO_BORDER);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph(description));
        cell.setBorder(Rectangle.NO_BORDER);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph(String.valueOf(acumAmount)));
        cell.setBorder(Rectangle.NO_BORDER);
		header.addCell(cell);

		PdfContentByte canvas = writer.getDirectContent();
		Font fontBatchExpirationDate = new Font(Font.HELVETICA, 8.0f, Font.ITALIC, Color.BLACK);
		header.writeSelectedRows(0, -1, 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - offset) * MILLIMITER_TO_POINTS_FACTOR, canvas);
		if (!details.isEmpty()) {
			header.flushContent();
			cell = new PdfPCell(new Paragraph(details, fontBatchExpirationDate));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(3);
			header.addCell(cell);

			header.writeSelectedRows(0, -1, 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - (offset + 5.0f)) * MILLIMITER_TO_POINTS_FACTOR, canvas);

			offset += 5.0f;
		}
	}

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		document.setPageSize(PageSize.A4);

		document.open();

        this.document = document;
        this.writer = writer;

		provisioningRequestService = (ProvisioningRequestService)model.get("provisioningRequestService");
		provisioningRequestStateService = (ProvisioningRequestStateService)model.get("provisioningRequestStateService");
		stockService = (StockService)model.get("stockService");

		ProvisioningRequestState state = provisioningRequestStateService.get(State.PRINTED.getId());

		String[] provisioningIds = (String[]) model.get("provisioningIds");

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

		for (int currentProvisioning = 1; currentProvisioning <= provisioningIds.length; currentProvisioning+=2) {
			ProvisioningRequest provisioningRequest = provisioningRequestService.get(Integer.parseInt(provisioningIds[currentProvisioning-1]));
			provisioningRequest.setState(state);
			provisioningRequestService.save(provisioningRequest);

			addHeader(provisioningRequest, true);
			addDetails(provisioningRequest, true);

            PdfContentByte canvas = writer.getDirectContent();
			DottedLineSeparator separator = new DottedLineSeparator();
			separator.draw(canvas, 0, 0, 210f * MILLIMITER_TO_POINTS_FACTOR, 0, 150f * MILLIMITER_TO_POINTS_FACTOR);

            if (currentProvisioning + 1 <=  provisioningIds.length) {
                provisioningRequest = provisioningRequestService.get(Integer.parseInt(provisioningIds[currentProvisioning]));
                provisioningRequest.setState(state);
                provisioningRequestService.save(provisioningRequest);

                addHeader(provisioningRequest, false);
                addDetails(provisioningRequest, false);
            }
			document.newPage();
		}
	}
}