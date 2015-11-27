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

public class PickingSheetPrinterImpl extends AbstractPdfView {

	private ProvisioningRequestService provisioningRequestService;
	private ProvisioningRequestStateService provisioningRequestStateService;
	private StockService stockService;
    private Document document;
    private PdfWriter writer;
    private String logoPath;

    private Document addHeader(ProvisioningRequest provisioningRequest, Boolean firstHalf) {

        BaseFont bfHelveticaBold;
        BaseFont bfTimes;
        try {
            bfHelveticaBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "Cp1252", false);
            bfTimes = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);

			Image logoImage = Image.getInstance(logoPath);
            logoImage.scaleToFit(80f, 80f);
            logoImage.setAbsolutePosition(10f * 2.8346f, (297.0f - 20.0f - (firstHalf ? 0.0f : 145f)) * 2.8346f);

            String name = PropertyProvider.getInstance().getProp("name");

            PdfContentByte cb = writer.getDirectContent();
            cb.beginText();
            cb.setFontAndSize(bfHelveticaBold, 18f);
            cb.setTextMatrix(45f * 2.8346f, (297.0f - 10.0f - (firstHalf ? 0.0f : 145f)) * 2.8346f);
            cb.showText(name);
            cb.setTextMatrix(45f * 2.8346f, (297.0f - 18.0f - (firstHalf ? 0.0f : 145f)) * 2.8346f);
            cb.showText("Hoja de Picking");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bfTimes, 12f);
            cb.setTextMatrix(155f * 2.8346f, (297.0f - 10.0f - (firstHalf ? 0.0f : 145f)) * 2.8346f);
            cb.showText("Ordenado por Cliente.");
            cb.setTextMatrix(155f * 2.8346f, (297.0f - 15.0f - (firstHalf ? 0.0f : 145f)) * 2.8346f);
            cb.showText("Reporte Detallado");
            cb.endText();

            document.add(logoImage);
            document.add(Chunk.NEWLINE);

            if (!firstHalf) {
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
            }

            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));
            document.add(Chunk.NEWLINE);

            Client client = provisioningRequest.getClient();
            Agreement agreement = provisioningRequest.getAgreement();
            String deliveryDate = new SimpleDateFormat("dd/MM/yyyy").format(provisioningRequest.getDeliveryDate());
            DeliveryLocation deliveryLocation = provisioningRequest.getDeliveryLocation();
            Affiliate affiliate = provisioningRequest.getAffiliate();

            PdfPTable header = new PdfPTable(1);
            header.setWidthPercentage(100);
            header.setSpacingBefore(0);
            header.setSpacingAfter(0);

            Font fontHeader = new Font(Font.HELVETICA, 10f, Font.NORMAL, Color.BLACK);
            document.add(new Chunk("Suc./Cliente: "));

            Chunk clientDescription = new Chunk("(" + StringUtility.addLeadingZeros(String.valueOf(client.getCode()),4) + ") " + client.getName() + " (" + client.getProvince().getName() + ") " + client.getAddress() + " (CP: " + client.getZipCode() + ")", fontHeader);
            document.add(clientDescription);
            document.add(Chunk.NEWLINE);

            document.add(new Chunk("Pedido Nro.: "));
            Chunk provisioningDescription = new Chunk(provisioningRequest.getFormatId() + " Convenio: " + StringUtility.addLeadingZeros(String.valueOf(agreement.getCode()), 5) + " - " + agreement.getDescription(), fontHeader);
            document.add(provisioningDescription);
            document.add(Chunk.NEWLINE);

            document.add(new Chunk("Fecha de Entrega: "));
            Chunk deliveryDateDescription = new Chunk(deliveryDate, fontHeader);
            document.add(deliveryDateDescription);
            document.add(Chunk.NEWLINE);

            document.add(new Chunk("Entregar en: "));
            Chunk deliveryLocationDescription = new Chunk("(" + deliveryLocation.getProvince().getName() + ") " + deliveryLocation.getAddress() + " (CP: " + deliveryLocation.getZipCode() + ")", fontHeader);
            document.add(deliveryLocationDescription);
            document.add(Chunk.NEWLINE);

            document.add(new Chunk("Entregar por: "));
            Chunk deliveryLocationName = new Chunk("(" + StringUtility.addLeadingZeros(deliveryLocation.getCode().toString(), 8) + ") " + deliveryLocation.getName(), fontHeader);
            document.add(deliveryLocationName);
            document.add(Chunk.NEWLINE);

            String documentType;
            if(affiliate.getDocumentType() != null){
                documentType = DocumentType.types.get(Integer.valueOf(affiliate.getDocumentType()));
            }else{
                documentType = "";
            }
            String documentNumber;
            if(affiliate.getDocument() != null){
                documentNumber = affiliate.getDocument();
            }else{
                documentNumber = "";
            }

            document.add(new Chunk("Afiliado: "));
            Chunk affiliateDetails = new Chunk("Cod. " + StringUtility.addLeadingZeros(affiliate.getCode(), 5) + " ) - " + documentType + " " + documentNumber + " - " + affiliate.getName() + " " + affiliate.getSurname(), fontHeader);
            document.add(affiliateDetails);
            document.add(Chunk.NEWLINE);

            document.add(new Chunk("Observaciones: "));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
	}

	private Document addDetails(ProvisioningRequest provisioningRequest, Boolean firstHalf) throws DocumentException {
		PdfPTable header = new PdfPTable(3);
		header.setWidthPercentage(100);
		header.setSpacingBefore(0);
		header.setSpacingAfter(0);
		float[] columnWidths = {2f, 7f, 1f};
		header.setWidths(columnWidths);

		PdfPCell cell;

		cell = new PdfPCell(new Paragraph("Cod. Prod."));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Descripcion"));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Cantidad"));
		header.addCell(cell);

		document.add(header);

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
				this.printDetail(document, code, description, acumAmount, details);
				acumAmount = 0;
				neededAmount = 0;
				details = "";
				code = 0;
				description = "";
				batch = "";
				expirationDate = "";
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
		this.printDetail(document, code, description, acumAmount, details);
        return document;
	}

	private void printDetail(Document document, Integer code, String description, Integer AcumAmount, String details)
			throws DocumentException {
        PdfPTable header = new PdfPTable(3);
		header.setWidthPercentage(100);
		header.setSpacingBefore(0);
		header.setSpacingAfter(0);
		float[] columnWidths = {2f, 7f, 1f};
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

        document.add(header);
        document.add(new Paragraph(details));
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
			Rectangle rect = writer.getBoxSize("art");

            Font fontEndPage = new Font(Font.HELVETICA, 12f, Font.ITALIC, Color.BLACK);

			String timestamp = new SimpleDateFormat("'Fecha:' dd/MM/yyyy 'Hora:' HH:mm:ss").format(new Date());
            String name = PropertyProvider.getInstance().getProp("name");

            PdfContentByte cb = writer.getDirectContent();
            LineSeparator separator = new LineSeparator();
            separator.draw(cb, 0, 0, 210f * 2.8346f, 0, rect.getBottom() - 10);

            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
					new Phrase(String.format("%s  -  Usuario: %s  -  %s", timestamp, this.userName, name), fontEndPage),
					(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 20, 0);
		}
	}

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		document.setPageSize(PageSize.A4);
		writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

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

            PdfContentByte cb = writer.getDirectContent();
			DottedLineSeparator separator = new DottedLineSeparator();
			separator.draw(cb, 0, 0, 210f * 2.8346f, 0, 150f * 2.8346f);

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