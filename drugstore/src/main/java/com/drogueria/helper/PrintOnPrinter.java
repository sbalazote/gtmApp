package com.drogueria.helper;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.scheduling.annotation.Async;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class PrintOnPrinter {

	private static final Logger logger = Logger.getLogger(PrintOnPrinter.class);

	public boolean sendToSpool(String printerName, String jobName, ByteArrayInputStream fileStream) {
		try {
			PDDocument PDFDocument = PDDocument.load(fileStream);
			PrinterJob job = PrinterJob.getPrinterJob();
			PrintService printerService = findPrinterService(printerName);
			if (printerService == null) {
				logger.error("Error al intentar imprimir documento. No se encuentra la impresora seleccionada: " + printerName);
				return false;
			}
			job.setPrintService(printerService);
			job.setJobName(jobName);
			job.setPageable(PDFDocument);
			job.print();
			logger.info("Se ha mandado a cola de impresion de la impresora: " + printerName + " el documento: " + jobName);
			return true;
		} catch (Exception e) {
			logger.error("Error al intentar imprimir documento!", e);
		}
		return false;
	}

	private PrintService findPrinterService(String printerName) {
		PrintService[] printServices = PrinterJob.lookupPrintServices();
		for (int count = 0; count < printServices.length; ++count) {
			if (printerName.equalsIgnoreCase(printServices[count].getName())) {
				return printServices[count];
			}
		}
		return null;
	}

	@Async
	public void send(String ipNumber, int port, String pdfPath) throws IOException {
		Socket socket = null;
		try {
			socket = new Socket(ipNumber, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = new FileInputStream(new File(pdfPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DataOutputStream os = null;
		try {
			os = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedInputStream bis = new BufferedInputStream(is);
		DataInputStream dis = new DataInputStream(bis);

		try {
			while (dis.available() != 0) {
				os.write(dis.readByte());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		os.close();
		dis.close();
		bis.close();
		is.close();
	}

	// Esto sirve para enviar a un recurso que tiene acceso el servidor directamente, queda por si algun momento se requiere.
	public void sendToPrint(String pdfName, String printerPath) {
		FileInputStream psStream = null;
		try {
			psStream = new FileInputStream(pdfName);
		} catch (FileNotFoundException ffne) {
			ffne.printStackTrace();
		}
		if (psStream == null) {
			return;
		}
		DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);

		// this step is necessary because I have several printers configured
		PrintService myPrinter = null;
		for (PrintService service : services) {
			String svcName = service.toString();
			System.out.println("Servicio encontrado: " + svcName);
			// Aca se tiene que identificar el servicio, no se a nivel de servidores como ubicas una impresora
			// El nombre es la descripcion que tiene la impresora.
			String printerName = svcName.replace('\\', '/');
			if (printerName.contains(printerPath)) {
				myPrinter = service;
				System.out.println("Impresora hallada: " + printerName);
				break;
			}
		}

		if (myPrinter != null) {
			DocPrintJob job = myPrinter.createPrintJob();
			try {
				job.print(myDoc, aset);

			} catch (Exception pe) {
				pe.printStackTrace();
			}
		} else {
			System.out.println("No fueron encontradas impresoras");
		}
	}
	// Para enviar pdf a imprimir
}
