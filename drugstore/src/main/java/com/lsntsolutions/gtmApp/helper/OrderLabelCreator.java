package com.lsntsolutions.gtmApp.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;

@Service
@Transactional
public class OrderLabelCreator {

	private static final String FILE_NAME = "packing_list";

	private static final String FILE_EXTENSION = ".RPD";

	private static final String FILE_TEMPLATE = "f:/emercury11/PLANTILLAS/packing_list.RPV";

	private static final int MAX_PRODUCT_LIMIT = 16;

	@Autowired
	private com.lsntsolutions.gtmApp.service.PropertyService PropertyService;

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
		File file = new File(filepath + FILE_NAME + type + FILE_EXTENSION);
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write("template=" + FILE_TEMPLATE);
		output.newLine();
		output.write("@nropedido=" + order.getProvisioningRequest().getId());
		output.newLine();
		output.write("@nropedido1=" + order.getProvisioningRequest().getId() + "00");
		output.newLine();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		output.write("@fecha_empaque=" + dateFormatter.format(date));
		output.newLine();

		output.write("@nombre_cliente_entrega=" + order.getProvisioningRequest().getDeliveryLocation().getCorporateName());
		output.newLine();

		output.write("@codigo_afiliado=" + order.getProvisioningRequest().getAffiliate().getCode());
		output.newLine();

		output.write("@convenio=" + order.getProvisioningRequest().getAgreement().getDescription());
		output.newLine();

		output.write("@nombre_afiliado=" + order.getProvisioningRequest().getAffiliate().getSurname() + " "
				+ order.getProvisioningRequest().getAffiliate().getName());
		output.newLine();

		output.write("@entrega_localidad_cliente=" + order.getProvisioningRequest().getClient().getLocality());
		output.newLine();

		Iterator<Product> it = products.keySet().iterator();
		Integer count = 1;
		while (it.hasNext()) {
			Product product = it.next();
			output.write("@producto" + count + "=" + product.getDescription());
			output.newLine();
			output.write("@cantidad" + count + "= ( " + products.get(product) + " )");
			output.newLine();
			count++;
		}

		if (count < MAX_PRODUCT_LIMIT) {
			for (int i = count; i < MAX_PRODUCT_LIMIT + 1; i++) {
				output.write("@producto" + count + "=");
				output.newLine();
				output.write("@cantidad" + count + "=");
				output.newLine();
				count++;
			}
		}

		output.write("@etiq=" + tag);
		output.newLine();

		output.write("@etiqs=" + tagsCount);
		output.newLine();

		output.write("@tipo=" + temperatureDescription);
		output.newLine();

		output.close();
	}
}
