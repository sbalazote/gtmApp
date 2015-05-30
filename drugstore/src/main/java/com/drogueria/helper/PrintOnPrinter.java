package com.drogueria.helper;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.springframework.scheduling.annotation.Async;

public class PrintOnPrinter {

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
