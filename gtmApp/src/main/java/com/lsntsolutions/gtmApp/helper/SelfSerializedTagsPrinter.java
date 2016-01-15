package com.lsntsolutions.gtmApp.helper;

import com.lsntsolutions.gtmApp.model.InputDetail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class SelfSerializedTagsPrinter {

	private FileWriter fileWritter;
	private BufferedWriter bufferWritter;
	private File selfSerializedTagsPrinter;

	public SelfSerializedTagsPrinter() {
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

	public void print(List<InputDetail> inputDetailList, String inputId, String gln, String filepath){
		Integer productId = 0;
		Integer count = 1;
		String currentBatch = "";
		for (InputDetail inputDetail : inputDetailList) {
			if ("SS".equals(inputDetail.getProduct().getType())) {
				if(productId != inputDetail.getProduct().getId()){
					productId = inputDetail.getProduct().getId();
					currentBatch = inputDetail.getBatch();
					count = 1;
					createFile(inputId, filepath, count, inputDetail);
				}else{
					if(!inputDetail.getBatch().equals(currentBatch)){
						count++;
						currentBatch = inputDetail.getBatch();
						createFile(inputId, filepath, count, inputDetail);
					}
				}
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

	private void createFile(String inputId, String filepath, Integer count, InputDetail inputDetail) {
		close();
		new File(filepath).mkdirs();
		//"I" de ingreso + "nro de ingreso" + "P" de producto + "código de producto" + "N" + "un número incremental para identificar lote".
		String fileName = "I" + inputId + "P" + inputDetail.getProduct().getCode() + "N" + count + ".001";
		this.selfSerializedTagsPrinter = new File(filepath + fileName);
		try {
            if (!this.selfSerializedTagsPrinter.exists()) {
                this.selfSerializedTagsPrinter.createNewFile();
            }
            this.fileWritter = new FileWriter(this.selfSerializedTagsPrinter.getAbsolutePath(), true);
			this.bufferWritter = new BufferedWriter(this.fileWritter);
        } catch (IOException e) {
            throw new RuntimeException("No se ha podido abrir el archivo: " + filepath + fileName, e);
        }
	}

	public void close(){
		if(bufferWritter != null){
			try {
				this.bufferWritter.close();
			} catch (IOException e) {
				throw new RuntimeException("No se ha podido cerrar en el archivo", e);
			}
		}
	}

}
