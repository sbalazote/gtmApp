package com.lsntsolutions.gtmApp.helper;

import com.ibm.icu.text.SimpleDateFormat;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Service
@Transactional
public class OrderLabelCreator {

	private static final String FILE_NAME = "packing_list";
	private static final String FILE_EXTENSION = ".RPD";
	private static final String FILE_TEMPLATE = "f:/emercury11/PLANTILLAS/packing_list.RPV";
	private static final int MAX_PRODUCT_LIMIT = 16;

	@Autowired
	private PrintOnPrinter printOnPrinter;

	public void getLabelFile(Order order) throws IOException {
		String filepath = order.getProvisioningRequest().getAgreement().getOrderLabelFilepath();

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
				this.createFile(order, filepath, "-AMB", products, tag, tags, "AMBIENTE");

			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!coldProducts.isEmpty()) {
			tag += 1;
			try {
				this.createFile(order, filepath, "-FRIO", coldProducts, tag, 1, "FRIO");

			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	private void createFile(Order order, String filepath, String type, Map<Product, Integer> products, Integer tag, Integer tagsCount,
			String temperatureDescription) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

		outputWriter.write("template=" + FILE_TEMPLATE);
		outputWriter.newLine();
		outputWriter.write("@nropedido=" + order.getProvisioningRequest().getId());
		outputWriter.newLine();
		outputWriter.write("@nropedido1=" + order.getProvisioningRequest().getId() + "00");
		outputWriter.newLine();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		outputWriter.write("@fecha_empaque=" + dateFormatter.format(date));
		outputWriter.newLine();

		outputWriter.write("@nombre_cliente_entrega=" + order.getProvisioningRequest().getDeliveryLocation().getCorporateName());
		outputWriter.newLine();

		outputWriter.write("@codigo_afiliado=" + order.getProvisioningRequest().getAffiliate().getCode());
		outputWriter.newLine();

		outputWriter.write("@convenio=" + order.getProvisioningRequest().getAgreement().getDescription());
		outputWriter.newLine();

		outputWriter.write("@nombre_afiliado=" + order.getProvisioningRequest().getAffiliate().getSurname() + " "
				+ order.getProvisioningRequest().getAffiliate().getName());
		outputWriter.newLine();

		outputWriter.write("@entrega_localidad_cliente=" + order.getProvisioningRequest().getClient().getLocality());
		outputWriter.newLine();

		Iterator<Product> it = products.keySet().iterator();
		Integer count = 1;
		while (it.hasNext()) {
			Product product = it.next();
			outputWriter.write("@producto" + count + "=" + product.getDescription());
			outputWriter.newLine();
			outputWriter.write("@cantidad" + count + "= ( " + products.get(product) + " )");
			outputWriter.newLine();
			count++;
		}

		if (count < MAX_PRODUCT_LIMIT) {
			for (int i = count; i < MAX_PRODUCT_LIMIT + 1; i++) {
				outputWriter.write("@producto" + count + "=");
				outputWriter.newLine();
				outputWriter.write("@cantidad" + count + "=");
				outputWriter.newLine();
				count++;
			}
		}

		outputWriter.write("@etiq=" + tag);
		outputWriter.newLine();

		outputWriter.write("@etiqs=" + tagsCount);
		outputWriter.newLine();

		outputWriter.write("@tipo=" + temperatureDescription);
		outputWriter.newLine();

		outputWriter.flush();
		outputWriter.close();

		byte[] byteArrayOutputStream = outputStream.toByteArray();

		outputStream.close();

		this.printOnPrinter.sendOrderLabelToSpool(filepath, FILE_NAME + type + FILE_EXTENSION, byteArrayOutputStream);
	}
}
