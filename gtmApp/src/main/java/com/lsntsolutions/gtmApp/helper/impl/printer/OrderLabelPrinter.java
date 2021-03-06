package com.lsntsolutions.gtmApp.helper.impl.printer;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
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

	private static final String JOB_NAME = "picking_list";
	private static final int MAX_PRODUCT_LIMIT = 16;
	public static final int NUMBER_OF_COPIES = 1;
	public static final float UNIT = 2.8346f;


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
		float coordinateXStart = propertyService.get().getPrintPickingCoordinateXStart();
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
			logo.setAbsolutePosition((85f+coordinateXStart) * UNIT, (297.0f - 10.0f - coordinateYStart) * UNIT);
			overContent.addImage(logo);

			// imprimo agente que envía
			overContent.setTextMatrix((5.0f+coordinateXStart) * UNIT, (297.0f - 5.0f - coordinateYStart) * UNIT);
			Property property = propertyService.get();
			String corporateName = property.getCorporateName();
			String address = property.getAddress();
			String locality = property.getLocality();
			overContent.showText("De " + corporateName + " - " + address + " - " + locality);

			overContent.setTextMatrix((5.0f+coordinateXStart) * UNIT, (297.0f - 7.0f - coordinateYStart) * UNIT);
			overContent.showText("Para: ");

			// imprimo agente que recibe
			overContent.setFontAndSize(timesBoldBaseFont, 10.0f);
			overContent.setTextMatrix((5.0f+coordinateXStart) * UNIT, (297.0f - 10.0f - coordinateYStart) * UNIT);
			String deliveryLocationName = order.getProvisioningRequest().getDeliveryLocation().getName();
			overContent.showText(deliveryLocationName);

			// imprimo 2 lineas separadoras
			overContent.saveState();
			overContent.setLineWidth(0.05f);
			overContent.endText();
			overContent.moveTo((5.0f+coordinateXStart) * UNIT, (297.0f - 12.0f - coordinateYStart) * UNIT);
			overContent.lineTo((75.0f+coordinateXStart) * UNIT, (297.0f - 12.0f - coordinateYStart) * UNIT);
			overContent.stroke();
			overContent.beginText();
			overContent.restoreState();

			overContent.setFontAndSize(timesHelveticaBaseFont, 8.0f);
			String agreement = order.getAgreement().getDescription() + " - " + order.getProvisioningRequest().getClient().getName();
			overContent.showTextAligned(PdfContentByte.ALIGN_CENTER, agreement, (35.0f+coordinateXStart) * UNIT, (297.0f - 15.0f - coordinateYStart) * UNIT, 0);

			overContent.saveState();
			overContent.setLineWidth(0.05f);
			overContent.endText();
			overContent.moveTo((5.0f+coordinateXStart) * UNIT, (297.0f - 16.0f - coordinateYStart) * UNIT);
			overContent.lineTo((75.0f+coordinateXStart) * UNIT, (297.0f - 16.0f - coordinateYStart) * UNIT);
			overContent.stroke();
			overContent.beginText();
			overContent.restoreState();

			// dibujo 2 rectangulos
			overContent.setFontAndSize(timesHelveticaBaseFont, 8.0f);
			overContent.setTextMatrix((77.0f+coordinateXStart) * UNIT, (297.0f - 14.0f - coordinateYStart) * UNIT);
			overContent.showText("P");

			Rectangle rect = new Rectangle((80.0f+coordinateXStart) * UNIT, (297.0f - 12.0f - coordinateYStart) * UNIT, (85.0f+coordinateXStart) * UNIT, (297.0f - 17.0f - coordinateYStart) * UNIT);
			rect.setBorder(Rectangle.BOX);
			rect.setBorderWidth(1.0f);
			overContent.endText();
			overContent.rectangle(rect);
			overContent.beginText();
			overContent.setFontAndSize(timesHelveticaBaseFont, 8.0f);
			overContent.setTextMatrix((87.0f+coordinateXStart) * UNIT, (297.0f - 14.0f - coordinateYStart) * UNIT);
			overContent.showText("C");

			rect = new Rectangle((90.0f+coordinateXStart) * UNIT, (297.0f - 12.0f - coordinateYStart) * UNIT, (95.0f+coordinateXStart) * UNIT, (297.0f - 17.0f - coordinateYStart) * UNIT);
			rect.setBorder(Rectangle.BOX);
			rect.setBorderWidth(1.0f);
			overContent.endText();
			overContent.rectangle(rect);
			overContent.beginText();

			// imprimo datos del afiliado
			overContent.setFontAndSize(timesHelveticaBaseFont, 8.0f);
			overContent.setTextMatrix((5.0f+coordinateXStart) * UNIT, (297.0f - 20.0f - coordinateYStart) * UNIT);
			Affiliate affiliate = order.getProvisioningRequest().getAffiliate();
			String affiliateCode = StringUtility.addLeadingZeros(affiliate.getCode(), 14);
			String affiliateSurname = affiliate.getSurname().toUpperCase();
			String affiliateName = affiliate.getName().toUpperCase();
			overContent.showText("Afiliado: \t" + affiliateCode);
			overContent.setTextMatrix((5.0f+coordinateXStart) * UNIT, (297.0f - 23.0f - coordinateYStart) * UNIT);
			overContent.showText(affiliateSurname + " " + affiliateName);

			// imprimo linea separadora
			overContent.saveState();
			overContent.setLineWidth(0.05f);
			overContent.endText();
			overContent.moveTo((5.0f+coordinateXStart) * UNIT, (297.0f - 25.0f - coordinateYStart) * UNIT);
			overContent.lineTo((90.0f+coordinateXStart) * UNIT, (297.0f - 25.0f - coordinateYStart) * UNIT);
			overContent.stroke();
			overContent.beginText();
			overContent.restoreState();

			// imprimo fecha y numero de pedido
			String date = dateFormatter.format(order.getProvisioningRequest().getDeliveryDate());
			String number = order.getProvisioningRequest().getFormatId();
			overContent.setTextMatrix((5.0f+coordinateXStart) * UNIT, (297.0f - 30.0f - coordinateYStart) * UNIT);
			overContent.showText("Fecha: " + date + " / NP: " + number);

			// genero el codigo de barras EAN-128
			Barcode128 code128 = new Barcode128();
			code128.setCode(number);
			Image code128Image = code128.createImageWithBarcode(overContent, null, null);
			code128Image.scalePercent(75.0f);
			code128Image.setAbsolutePosition((75f+coordinateXStart) * UNIT, (297.0f - 35.0f - coordinateYStart) * UNIT);
			overContent.addImage(code128Image);

			// imprimo 16 lineas separadoras de productos
			float j= 35.0f;
			for (int i= 0; i < MAX_PRODUCT_LIMIT; i++) {

				overContent.saveState();
				overContent.setLineWidth(0.05f);
				overContent.endText();
				overContent.moveTo((5.0f+coordinateXStart) * UNIT, (297.0f - j - coordinateYStart) * UNIT);
				overContent.lineTo((90.0f+coordinateXStart) * UNIT, (297.0f - j - coordinateYStart) * UNIT);
				overContent.stroke();
				overContent.beginText();
				overContent.restoreState();

				j+=4;
			}

			// imprimo productos
			float prodyOffset = 38.0f;
			Iterator<Product> it = products.keySet().iterator();
			while (it.hasNext()) {
				Product product = it.next();

				overContent.setTextMatrix((7.0f+coordinateXStart) * UNIT, (297.0f - prodyOffset - coordinateYStart) * UNIT);
				overContent.showText(product.getDescription());

				overContent.setTextMatrix((91.0f+coordinateXStart) * UNIT, (297.0f - prodyOffset - coordinateYStart) * UNIT);
				overContent.showText("( " + products.get(product) + " )");

				prodyOffset +=4;
			}

			// imprimo pie de pagina del rotulo
			overContent.setTextMatrix((65.0f+coordinateXStart) * UNIT, (297.0f - 98.0f - coordinateYStart) * UNIT);
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