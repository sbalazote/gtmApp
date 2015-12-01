package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.constant.DocumentType;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.service.ProvisioningRequestService;
import com.lsntsolutions.gtmApp.service.ProvisioningRequestStateService;
import com.lsntsolutions.gtmApp.service.StockService;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
import java.util.List;

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

        BaseFont bfHelveticaBold;
        BaseFont bfTimes;
        try {
            bfHelveticaBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "Cp1252", false);
            bfTimes = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);

			Image logoImage = Image.getInstance(logoPath);
            logoImage.scaleToFit(80f, 80f);
            logoImage.setAbsolutePosition(10f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 15.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR);
			document.add(logoImage);

            String name = PropertyProvider.getInstance().getProp("name");

            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginText();
            canvas.setFontAndSize(bfHelveticaBold, 18f);
            canvas.setTextMatrix(45f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 10.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR);
            canvas.showText(name);
            canvas.setTextMatrix(45f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 18.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR);
            canvas.showText("Hoja de Picking");
            canvas.endText();

            canvas.beginText();
            canvas.setFontAndSize(bfTimes, 12f);
            canvas.setTextMatrix(155f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 10.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR);
            canvas.showText("Ordenado por Cliente.");
            canvas.setTextMatrix(155f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 15.0f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR);
            canvas.showText("Reporte Detallado");
            canvas.endText();

			// imprimo Separador del logo y resto
            LineSeparator headerSeparator = new LineSeparator();
			headerSeparator.drawLine(canvas, 15.0f * MILLIMITER_TO_POINTS_FACTOR, 195.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 20f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR);

			// imprimo Suc./Cliente
            Font fontHeader = new Font(Font.HELVETICA, 10f, Font.NORMAL, Color.BLACK);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Suc./Cliente: "), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 30f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

            String clientDescription = "(" + StringUtility.addLeadingZeros(String.valueOf(client.getCode()),4) + ") " + client.getName() + " (" + client.getProvince().getName() + ") " + client.getAddress() + " (CP: " + client.getZipCode() + ")";
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(clientDescription, fontHeader), 40.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 30f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Pedido Nro.
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Pedido Nro.: "), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
            String provisioningDescription = provisioningRequest.getFormatId() + " (Convenio: " + StringUtility.addLeadingZeros(String.valueOf(agreement.getCode()), 5) + " - " + agreement.getDescription() + ")";
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(provisioningDescription, fontHeader), 40.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 35f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Fecha de Entrega
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Fecha de Entrega: "), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 40f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(deliveryDate, fontHeader), 51.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 40f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Entregar en:
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Entregar en: "), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 45f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
            String deliveryLocationDescription = "(" + deliveryLocation.getProvince().getName() + ") " + deliveryLocation.getAddress() + " (CP: " + deliveryLocation.getZipCode() + ")";
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(deliveryLocationDescription, fontHeader), 40.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 45f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Entregar por:
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Entregar por: "), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 50f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			String deliveryLocationName = "(" + StringUtility.addLeadingZeros(deliveryLocation.getCode().toString(), 8) + ") " + deliveryLocation.getName();
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(deliveryLocationName, fontHeader), 41.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 50f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Afiliado
            String documentType;
            if (affiliate.getDocumentType() != null) {
                documentType = DocumentType.types.get(Integer.valueOf(affiliate.getDocumentType()));
            } else {
                documentType = "";
            }
            String documentNumber;
            if (affiliate.getDocument() != null) {
                documentNumber = affiliate.getDocument();
            } else {
                documentNumber = "";
            }

			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Afiliado: "), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 55f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);
			String affiliateDetails = "(Cod. " + StringUtility.addLeadingZeros(affiliate.getCode(), 5) + " ) - " + documentType + " " + documentNumber + " - " + affiliate.getName() + " " + affiliate.getSurname();
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(affiliateDetails, fontHeader), 32.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 55f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

			// imprimo Observaciones
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Observaciones: "), 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 60f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, 0f);

		} catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
	}

	private Document addDetails(ProvisioningRequest provisioningRequest, Boolean firstHalf) throws DocumentException {
		PdfPTable header = new PdfPTable(3);
		header.setTotalWidth(180.0f * MILLIMITER_TO_POINTS_FACTOR);
		float[] columnWidths = {2f, 6f, 1f};
		header.setWidths(columnWidths);

		PdfPCell cell;

		cell = new PdfPCell(new Paragraph("Cod. Prod."));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Descripcion"));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Cantidad"));
		header.addCell(cell);

		PdfContentByte canvas = writer.getDirectContent();
		header.writeSelectedRows(0, -1, 15.0f * MILLIMITER_TO_POINTS_FACTOR, (297.0f - 70f - (firstHalf ? 0.0f : 145f)) * MILLIMITER_TO_POINTS_FACTOR, canvas);

		offset = 75.0f + (firstHalf ? 0.0f : 145f);

		// DETALLES
		Integer id = 0;
		Integer acumAmount = 0;
		Integer neededAmount = 0;
		String details = "";
		Integer code = 0;
		String description = "";
		String batch = "";
		String expirationDate = "";

		Iterator<ProvisioningRequestDetail> it = provisioningRequest.getProvisioningRequestDetails().iterator();

		while (it.hasNext()) {
			ProvisioningRequestDetail prd = it.next();
			if ((!prd.getProduct().getId().equals(id)) && (id != 0)) {
				this.printDetail(code, description, acumAmount, details);
				acumAmount = 0;
				neededAmount = 0;
				details = "";
				code = 0;
				description = "";
				batch = "";
				expirationDate = "";
				offset+=5;
			}

			id = prd.getProduct().getId();
			neededAmount = prd.getAmount();
			if (prd.getProduct().getType().equals("PS") || prd.getProduct().getType().equals("SS")) {
				code = prd.getProduct().getCode();
				description = prd.getProduct().getDescription();
				acumAmount = prd.getAmount();
				details = "";
			}

			// obtengo el stock ordenado por FEFO (First Expire - First Out.
			List<Stock> productStockInFEFOOrder = stockService.getBatchExpirationDateStock(id, provisioningRequest.getAgreement().getId());
			Iterator<Stock> it2 = productStockInFEFOOrder.iterator();
			while ((it2.hasNext()) && (neededAmount > acumAmount)) {
				Stock stock = it2.next();

				code = prd.getProduct().getCode();
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
		//offset+=5;
		this.printDetail(code, description, acumAmount, details);
        return document;
	}

	private void printDetail(Integer code, String description, Integer AcumAmount, String details)
			throws DocumentException {
        PdfPTable header = new PdfPTable(3);
		header.setTotalWidth(180.0f * MILLIMITER_TO_POINTS_FACTOR);
		float[] columnWidths = {2f, 6f, 1f};
		header.setWidths(columnWidths);

        PdfPCell cell = new PdfPCell(new Paragraph(code.toString()));
        cell.setBorder(Rectangle.NO_BORDER);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph(description));
        cell.setBorder(Rectangle.NO_BORDER);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph(String.valueOf(AcumAmount)));
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

			offset+=5.0f;
		}
	}

	/** Inner class to add a header and a footer. */
	class HeaderFooterPageEvent extends PdfPageEventHelper {

		private final Phrase[] header = new Phrase[2];
		// TODO: revisar. Este campo no se usa
		private int pagenumber;
		private final String userName;

		public HeaderFooterPageEvent(String username) {
			this.userName = username;
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
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
            Font fontEndPage = new Font(Font.HELVETICA, 12f, Font.ITALIC, Color.BLACK);

			String timestamp = new SimpleDateFormat("'Fecha:' dd/MM/yyyy 'Hora:' HH:mm:ss").format(new Date());
            String name = PropertyProvider.getInstance().getProp("name");

            PdfContentByte canvas = writer.getDirectContent();
            LineSeparator separator = new LineSeparator();
            separator.drawLine(canvas, 0.0f, 210.0f * MILLIMITER_TO_POINTS_FACTOR, 7.0f * MILLIMITER_TO_POINTS_FACTOR);
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
					new Phrase(String.format("%s  -  Usuario: %s  -  %s", timestamp, this.userName, name), fontEndPage),
					105.0f * MILLIMITER_TO_POINTS_FACTOR, 2.0f * MILLIMITER_TO_POINTS_FACTOR, 0);
		}
	}

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		document.setPageSize(PageSize.A4);

		// Obtengo usuario logueado.
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		HeaderFooterPageEvent event = new HeaderFooterPageEvent(userName);
		writer.setPageEvent(event);

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