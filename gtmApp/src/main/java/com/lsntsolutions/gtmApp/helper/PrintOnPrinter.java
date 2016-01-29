package com.lsntsolutions.gtmApp.helper;

import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.SheetCollate;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PrintOnPrinter {

	private static final Logger logger = Logger.getLogger(PrintOnPrinter.class);
	public static final String ERROR_PRINT_NOT_FOUND = "Error al intentar imprimir documento. No se encuentra la impresora seleccionada: ";

	public void sendPDFToSpool(String printerName, String jobName, ByteArrayInputStream fileStream, PrinterResultDTO printerResultDTO, Integer numberOfCopies) {
		PDDocument PDFDocument = null;
		try {
			PDFDocument = PDDocument.load(fileStream);
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
			aset.add(SheetCollate.UNCOLLATED);
			PrinterJob job = PrinterJob.getPrinterJob();
			PrintService printerService = findPrinterService(printerName);
			if (printerService == null) {
				logger.error(ERROR_PRINT_NOT_FOUND + printerName);
				printerResultDTO.addErrorMessage(ERROR_PRINT_NOT_FOUND + printerName);
				return;
			}
			job.setPrintService(printerService);
			job.setJobName(jobName);
			job.setPageable(PDFDocument);
			job.setCopies(numberOfCopies);
			job.print(aset);
			logger.info("Se ha mandado a cola de impresion de la impresora: " + printerName + " el documento: " + jobName);
			printerResultDTO.addSuccessMessage("Se ha mandado a cola de impresion de la impresora: " + printerName + " el documento: " + jobName);
			return;
		} catch (Exception e) {
			logger.error("Error al intentar imprimir documento!", e);
			printerResultDTO.addErrorMessage("Error al intentar imprimir documento! " +  e.getMessage());
		} finally {
			if (PDFDocument != null) {
				try {
					PDFDocument.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return;
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

	public List<String> canPrint(String printerName){
		List<String> errors = new ArrayList<>();
		PrintService printerService = findPrinterService(printerName);
		if (printerService == null) {
			logger.error(ERROR_PRINT_NOT_FOUND + printerName);
			errors.add(ERROR_PRINT_NOT_FOUND + printerName);
		}else{
			Attribute printerIsAcceptingJobs = printerService.getAttributes().get(PrinterIsAcceptingJobs.class);
			if(printerIsAcceptingJobs == null || printerIsAcceptingJobs.toString() == "not-accepting-jobs"){
				logger.error("La Impresora no esta aceptando trabajos " + printerName);
				errors.add("La Impresora no esta aceptando trabajos " + printerName);
			}
		}
		return errors;
	}

	public PrintService[] findAllPrinters() {
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		return services;
	}
}
