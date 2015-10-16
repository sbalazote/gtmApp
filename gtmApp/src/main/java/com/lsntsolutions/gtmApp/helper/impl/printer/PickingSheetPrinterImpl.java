package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.helper.AbstractPdfView;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.service.ProvisioningRequestService;
import com.lsntsolutions.gtmApp.service.ProvisioningRequestStateService;
import com.lsntsolutions.gtmApp.service.StockService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PickingSheetPrinterImpl extends AbstractPdfView {

	private ProvisioningRequestService provisioningRequestService;
	private ProvisioningRequestStateService provisioningRequestStateService;
	private StockService stockService;

	private PdfPTable addHeaderTable(String logo) {
		PdfPTable header = new PdfPTable(20);
		header.setWidthPercentage(100);
		header.setSpacingBefore(0);
		header.setSpacingAfter(0);

		PdfPCell cell = null;
		try {

			Image logoImage = Image.getInstance(logo);
			logoImage.scaleAbsolute(40f, 40f);
			header.addCell(new PdfPCell(logoImage, true));

			String name = PropertyProvider.getInstance().getProp("name");

			cell = new PdfPCell(new Paragraph(name + "\nHoja de Picking"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(12);
			header.addCell(cell);

			cell = new PdfPCell(new Paragraph("Ordenado por Cliente.\nReporte Detallado"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(7);
			header.addCell(cell);

		} catch (Exception e) {
			throw new RuntimeException("No se ha podido generar el encabezado del archivo pdf", e);
		}
		return header;
	}

	private PdfPTable addBodyDataTable(ProvisioningRequest provisioningRequest) {
		Integer idProvisioningRequest = provisioningRequest.getId();
		Client client = provisioningRequest.getClient();
		Agreement agreement = provisioningRequest.getAgreement();
		String deliveryDate = new SimpleDateFormat("dd/MM/yyyy").format(provisioningRequest.getDeliveryDate());
		DeliveryLocation deliveryLocation = provisioningRequest.getDeliveryLocation();
		Affiliate affiliate = provisioningRequest.getAffiliate();

		PdfPTable header = new PdfPTable(2);
		header.setWidthPercentage(100);
		header.setSpacingBefore(0);
		header.setSpacingAfter(0);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("Suc./Cliente: (" + client.getId() + "/" + client.getCode() + ") " + client.getName()));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("N. Venta: " + idProvisioningRequest + "(" + agreement.getDescription() + "\tVen: 0002\tConcepto: FIRME"));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph(""));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Receta: 00000000001  F.Entrega: " + deliveryDate));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Entregar en: (Z:" + deliveryLocation.getProvince().getName() + "\t) " + deliveryLocation.getAddress()));
		header.addCell(cell);
		cell = new PdfPCell(new Paragraph(""));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Entregar por: " + deliveryLocation.getCode() + " " + deliveryLocation.getName()));
		header.addCell(cell);
		cell = new PdfPCell(new Paragraph(""));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Afiliado: " + affiliate.getCode() + "\t" + affiliate.getSurname() + " " + affiliate.getName()));
		header.addCell(cell);
		cell = new PdfPCell(new Paragraph(""));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Observaciones en la Nota de venta:"));
		header.addCell(cell);
		cell = new PdfPCell(new Paragraph(""));
		header.addCell(cell);

		return header;
	}

	private void addDetailsTable(Document document, ProvisioningRequest provisioningRequest) throws DocumentException {
		PdfPTable header = new PdfPTable(21);
		header.setWidthPercentage(100);
		header.setSpacingBefore(0);
		header.setSpacingAfter(0);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("CodInt"));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Descripcion"));
		cell.setColspan(8);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Ruteo"));
		cell.setColspan(2);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("UxB"));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Ped_Bult."));
		cell.setColspan(2);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Ped_Unid."));
		cell.setColspan(2);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Cant_Provista"));
		cell.setColspan(2);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("Repartidor"));
		cell.setColspan(3);
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
				this.printDetail(document, header, cell, code, description, acumAmount, details);
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
				details += "Ptda: 000000  Lt:" + batch + "\t" + expirationDate + "  Cant: " + amount + "\t";
				acumAmount += amount;
			}
		}
		this.printDetail(document, header, cell, code, description, acumAmount, details);

	}

	private void printDetail(Document document, PdfPTable header, PdfPCell cell, Integer code, String description, Integer AcumAmount, String details)
			throws DocumentException {
		header = new PdfPTable(21);
		header.setWidthPercentage(100);
		header.setSpacingBefore(0);
		header.setSpacingAfter(0);

		cell = new PdfPCell(new Paragraph(code.toString()));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph(description));
		cell.setColspan(8);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("00000000"));
		cell.setColspan(2);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("1"));
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph(String.valueOf(AcumAmount.floatValue())));
		cell.setColspan(2);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph(String.valueOf(AcumAmount.floatValue())));
		cell.setColspan(2);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("______________"));
		cell.setColspan(2);
		header.addCell(cell);

		cell = new PdfPCell(new Paragraph("______________"));
		cell.setColspan(3);
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

			String timestamp = new SimpleDateFormat("'Fecha:' dd/MM/yyyy 'Hora:' HH:mm:ss").format(new Date());
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
					new Phrase(String.format("%s \t Usr: %s \t gtmApp", timestamp, this.userName.toUpperCase())),
					(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
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

		for (String id : provisioningIds) {
			ProvisioningRequest provisioningRequest = provisioningRequestService.get(Integer.parseInt(id));
			provisioningRequest.setState(state);
			provisioningRequestService.save(provisioningRequest);
			//this.createPdf(userName, provisioningRequest);
			document.add(this.addHeaderTable(logoPath));
			document.add(this.addBodyDataTable(provisioningRequest));
			this.addDetailsTable(document, provisioningRequest);
			document.newPage();
		}
	}
}
