package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.helper.PrintOnPrinter;
import com.lsntsolutions.gtmApp.model.Affiliate;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.Property;
import com.lsntsolutions.gtmApp.service.PropertyService;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

@Service
public class OrderLabelPrinter implements ServletContextAware {

	private static final String JOB_NAME = "packing_list";
	private static final int MAX_PRODUCT_LIMIT = 16;
	public static final int NUMBER_OF_COPIES = 1;


	@Autowired
	private PropertyService propertyService;

	private ServletContext servletContext;

	private PrinterResultDTO printerResultDTO;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Autowired
	private PrintOnPrinter printOnPrinter;

	public void print(Order order, PrinterResultDTO printerResultDTO) throws IOException {
		this.printerResultDTO = printerResultDTO;

		String orderLabelPrinter = order.getProvisioningRequest().getAgreement().getOrderLabelPrinter();

		Map<Product, Integer> products = order.getProducts(false);
		Map<Product, Integer> coldProducts = order.getProducts(true);

		Integer tag = 0;
		Integer tags = 1;
		if (!products.isEmpty() && !coldProducts.isEmpty()) {
			tags = 2;
		}

		if (!products.isEmpty()) {
			tag += 1;
			try {
				this.generateOrderLabel(order, orderLabelPrinter, "-AMB", products, tag, tags, "AMBIENTE");

			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!coldProducts.isEmpty()) {
			tag += 1;
			try {
				this.generateOrderLabel(order, orderLabelPrinter, "-FRIO", coldProducts, tag, tags, "FRIO");

			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	private void generateOrderLabel(Order order, String orderLabelPrinter, String type, Map<Product, Integer> products, Integer tag, Integer tagsCount,
												String temperatureDescription) throws IOException {

		Document document;
		ByteArrayInputStream pdfDocument;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		float coordinateYStart = propertyService.get().getPrintPickingCoordinateYStart();

		try {
			Rectangle pagesize = new Rectangle(PageSize.A4);
			document = new Document(pagesize);

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			PdfWriter writer = PdfWriter.getInstance(document, out);

			document.addAuthor("gtmApp");
			document.addTitle(JOB_NAME + type);
			document.open();

			PdfContentByte overContent = writer.getDirectContent();

			BaseFont timesBoldBaseFont = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
			BaseFont timesHelveticaBaseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false);

			overContent.saveState();
			overContent.beginText();
			overContent.setFontAndSize(timesBoldBaseFont, 7.0f);

			// Logo
			String realPath = servletContext.getRealPath("/images/uploadedLogo.png");
			File file = new File(realPath);

			Image logo;
			if (file.exists()) {
				logo = Image.getInstance(realPath);
			} else {
				realPath = servletContext.getRealPath("/images/logo.png");
				logo = Image.getInstance(realPath);
			}
			logo.scaleToFit(25f, 25f);
			logo.setAbsolutePosition(85f * 2.8346f, (297.0f - 10.0f - coordinateYStart) * 2.8346f);
			overContent.addImage(logo);

			// imprimo agente que envía
			overContent.setTextMatrix(5.0f * 2.8346f, (297.0f - 5.0f - coordinateYStart) * 2.8346f);
			Property property = propertyService.get();
			String corporateName = property.getCorporateName();
			String address = property.getAddress();
			String locality = property.getLocality();
			overContent.showText("De " + corporateName + " - " + address + " - " + locality);

			overContent.setTextMatrix(5.0f * 2.8346f, (297.0f - 7.0f - coordinateYStart) * 2.8346f);
			overContent.showText("Para: ");

			// imprimo agente que recibe
			overContent.setFontAndSize(timesBoldBaseFont, 10.0f);
			overContent.setTextMatrix(5.0f * 2.8346f, (297.0f - 10.0f - coordinateYStart) * 2.8346f);
			String deliveryLocationName = order.getProvisioningRequest().getDeliveryLocation().getName();
			overContent.showText(deliveryLocationName);

			// imprimo 2 lineas separadoras
			overContent.saveState();
			overContent.setLineWidth(0.05f);
			overContent.moveTo(5.0f * 2.8346f, (297.0f - 12.0f - coordinateYStart) * 2.8346f);
			overContent.lineTo(75.0f * 2.8346f, (297.0f - 12.0f - coordinateYStart) * 2.8346f);
			overContent.stroke();
			overContent.restoreState();

			overContent.setFontAndSize(timesHelveticaBaseFont, 8.0f);
			overContent.showTextAligned(PdfContentByte.ALIGN_CENTER, order.getAgreement().getDescription(), 35.0f * 2.8346f, (297.0f - 15.0f - coordinateYStart) * 2.8346f, 0);

			overContent.saveState();
			overContent.setLineWidth(0.05f);
			overContent.moveTo(5.0f * 2.8346f, (297.0f - 16.0f - coordinateYStart) * 2.8346f);
			overContent.lineTo(75.0f * 2.8346f, (297.0f - 16.0f - coordinateYStart) * 2.8346f);
			overContent.stroke();
			overContent.restoreState();

			// dibujo 2 rectangulos
			overContent.setFontAndSize(timesHelveticaBaseFont, 8.0f);
			overContent.setTextMatrix(77.0f * 2.8346f, (297.0f - 14.0f - coordinateYStart) * 2.8346f);
			overContent.showText("P");

			Rectangle rect = new Rectangle(80.0f * 2.8346f, (297.0f - 12.0f - coordinateYStart) * 2.8346f, 85.0f * 2.8346f, (297.0f - 17.0f - coordinateYStart) * 2.8346f);
			rect.setBorder(Rectangle.BOX);
			rect.setBorderWidth(1.0f);
			overContent.rectangle(rect);

			overContent.setFontAndSize(timesHelveticaBaseFont, 8.0f);
			overContent.setTextMatrix(87.0f * 2.8346f, (297.0f - 14.0f - coordinateYStart) * 2.8346f);
			overContent.showText("C");

			rect = new Rectangle(90.0f * 2.8346f, (297.0f - 12.0f - coordinateYStart) * 2.8346f, 95.0f * 2.8346f, (297.0f - 17.0f - coordinateYStart) * 2.8346f);
			rect.setBorder(Rectangle.BOX);
			rect.setBorderWidth(1.0f);
			overContent.rectangle(rect);

			// imprimo datos del afiliado
			overContent.setFontAndSize(timesHelveticaBaseFont, 8.0f);
			overContent.setTextMatrix(5.0f * 2.8346f, (297.0f - 20.0f - coordinateYStart) * 2.8346f);
			Affiliate affiliate = order.getProvisioningRequest().getAffiliate();
			String affiliateCode = StringUtility.addLeadingZeros(affiliate.getCode(), 14);
			String affiliateSurname = affiliate.getSurname().toUpperCase();
			String affiliateName = affiliate.getName().toUpperCase();
			overContent.showText("Afiliado: \t" + affiliateCode);
			overContent.setTextMatrix(5.0f * 2.8346f, (297.0f - 23.0f - coordinateYStart) * 2.8346f);
			overContent.showText(affiliateSurname + " " + affiliateName);

			// imprimo linea separadora
			overContent.saveState();
			overContent.setLineWidth(0.05f);
			overContent.moveTo(5.0f * 2.8346f, (297.0f - 25.0f - coordinateYStart) * 2.8346f);
			overContent.lineTo(90.0f * 2.8346f, (297.0f - 25.0f - coordinateYStart) * 2.8346f);
			overContent.stroke();
			overContent.restoreState();

			// imprimo fecha y numero de pedido
			String date = dateFormatter.format(order.getProvisioningRequest().getDeliveryDate());
			String number = order.getProvisioningRequest().getFormatId();
			overContent.setTextMatrix(5.0f * 2.8346f, (297.0f - 30.0f - coordinateYStart) * 2.8346f);
			overContent.showText("Fecha: " + date + " / NP: " + number);

			// genero el codigo de barras EAN-128
			Barcode128 code128 = new Barcode128();
			code128.setCode(number);
			Image code128Image = code128.createImageWithBarcode(overContent, null, null);
			code128Image.scalePercent(75.0f);
			code128Image.setAbsolutePosition(75f * 2.8346f, (297.0f - 35.0f - coordinateYStart) * 2.8346f);
			overContent.addImage(code128Image);

			// imprimo 16 lineas separadoras de productos
			float j= 35.0f;
			for (int i= 0; i < MAX_PRODUCT_LIMIT; i++) {

				overContent.saveState();
				overContent.setLineWidth(0.05f);
				overContent.moveTo(5.0f * 2.8346f, (297.0f - j - coordinateYStart) * 2.8346f);
				overContent.lineTo(90.0f * 2.8346f, (297.0f - j - coordinateYStart) * 2.8346f);
				overContent.stroke();
				overContent.restoreState();

				j+=4;
			}

			// imprimo productos
			float prodyOffset = 38.0f;
			Iterator<Product> it = products.keySet().iterator();
			while (it.hasNext()) {
				Product product = it.next();

				overContent.setTextMatrix(7.0f * 2.8346f, (297.0f - prodyOffset - coordinateYStart) * 2.8346f);
				overContent.showText(product.getDescription());

				overContent.setTextMatrix(91.0f * 2.8346f, (297.0f - prodyOffset - coordinateYStart) * 2.8346f);
				overContent.showText("( " + products.get(product) + " )");

				prodyOffset +=4;
			}

			// imprimo pie de pagina del rotulo
			overContent.setTextMatrix(65.0f * 2.8346f, (297.0f - 98.0f - coordinateYStart) * 2.8346f);
			overContent.showText("List: " + tag + " de " + tagsCount + " (" + temperatureDescription + ")");

			overContent.endText();
			overContent.restoreState();

			document.close();

			pdfDocument = new ByteArrayInputStream(out.toByteArray());

			this.printOnPrinter.sendPDFToSpool(orderLabelPrinter, number + "_" + JOB_NAME + type, pdfDocument, printerResultDTO, NUMBER_OF_COPIES);

			pdfDocument.close();

		} catch (Exception e) {
			throw new RuntimeException("No se ha podido generar el Rotulo", e);
		}
	}
}