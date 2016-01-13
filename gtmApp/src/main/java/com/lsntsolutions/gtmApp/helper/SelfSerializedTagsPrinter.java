package com.lsntsolutions.gtmApp.helper;

import com.lsntsolutions.gtmApp.model.InputDetail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class SelfSerializedTagsPrinter {

	private static final String FILE_NAME = "I1491.001";

	private FileWriter fileWritter;
	private final BufferedWriter bufferWritter;
	private final File selfSerializedTagsPrinter;

	public SelfSerializedTagsPrinter(String filepath) {

		new File(filepath).mkdirs();
		this.selfSerializedTagsPrinter = new File(filepath + FILE_NAME);

		try {
			if (!this.selfSerializedTagsPrinter.exists()) {
				this.selfSerializedTagsPrinter.createNewFile();
			}

			this.fileWritter = new FileWriter(this.selfSerializedTagsPrinter.getAbsolutePath(), true);
		} catch (IOException e) {
			throw new RuntimeException("No se ha podido abrir el archivo: " + filepath + FILE_NAME, e);
		}
		this.bufferWritter = new BufferedWriter(this.fileWritter);
	}

	public void print(String inputId, String productCode, String batch, String expirationDate, String serialNumber) {
		try {
			this.bufferWritter.write(String.format("%1$10s%2$10s%3$-30s%4$-10s%5$-32s%6$-7s", inputId, productCode, batch, "0", expirationDate,
					serialNumber));
			this.bufferWritter.newLine();

		} catch (IOException e) {
			throw new RuntimeException("No se ha podido escribir en el archivo", e);
		}
	}

	public void print(List<InputDetail> inputDetailList, String inputId, String gln){

		for (InputDetail inputDetail : inputDetailList) {
			if ("SS".equals(inputDetail.getProduct().getType())) {
				String productCode = inputDetail.getProduct().getCode().toString();
				String batch = inputDetail.getBatch();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String expirationDate = sdf.format(inputDetail.getExpirationDate());
				String serialNumber = inputDetail.getSerialNumber();
				String tag = serialNumber.replaceFirst(gln, "");
				this.print(inputId, productCode, batch, expirationDate, tag);
			}
		}
	}

	public void close() {
		try {
			this.bufferWritter.close();
		} catch (IOException e) {
			throw new RuntimeException("No se ha podido cerrar en el archivo", e);
		}
	}

}
