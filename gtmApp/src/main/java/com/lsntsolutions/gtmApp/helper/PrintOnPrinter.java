package com.lsntsolutions.gtmApp.helper;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

public class PrintOnPrinter {

	private static final Logger logger = Logger.getLogger(PrintOnPrinter.class);

	public boolean sendPDFToSpool(String printerName, String jobName, ByteArrayInputStream fileStream) {
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

	public void sendOrderLabelToSpool(String printerName, String jobName, byte[] byteArrayInputStream) {
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(byteArrayInputStream);
			DocAttributeSet das = new HashDocAttributeSet();

			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			pras.add(new JobName(jobName, Locale.getDefault()));
			pras.add(new Copies(1));

			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

			// busco el servicio de impresora correspondiente
			PrintService ps = findPrinterService(printerName);

			// creo el trabajo de impresion.
			DocPrintJob job = ps.createPrintJob();

			if (ps == null) {
				logger.error("Error al intentar imprimir documento. No se encuentra la impresora seleccionada: " + printerName);
				return;
			}

			// creo el documento a imprimir.
			Doc doc = new SimpleDoc(is, flavor, null);

			// monitoreo eventos provenientes del trabajo de impresion.
			PrintJobWatcher watcher = new PrintJobWatcher(job);

			// mando a imprimir.
			job.print(doc, pras);

			// espero a que el trabajo de impresion finalice.
			logger.debug("Rsperando a que el trabajo de impresion finalice.");
			watcher.waitForDone();

			logger.info("Se ha mandado a cola de impresion de la impresora: " + printerName + " el documento: " + jobName);
		} catch (Exception e) {
			logger.error("Error al intentar imprimir documento!", e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch(Exception e) {

				}
		}
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

	private static class PrintJobWatcher {
		// true iff it is safe to close the print job's input stream
		boolean done = false;

		PrintJobWatcher(DocPrintJob job) {
			// Add a listener to the print job
			job.addPrintJobListener(new PrintJobAdapter() {
				public void printJobCanceled(PrintJobEvent pje) {
					allDone();
				}

				public void printJobCompleted(PrintJobEvent pje) {
					allDone();
				}

				public void printJobFailed(PrintJobEvent pje) {
					allDone();
				}

				public void printJobNoMoreEvents(PrintJobEvent pje) {
					allDone();
				}

				void allDone() {
					synchronized (PrintJobWatcher.this) {
						done = true;
						PrintJobWatcher.this.notify();
					}
				}
			});
		}
		public synchronized void waitForDone() {
			try {
				while (!done) {
					wait();
				}
			} catch (InterruptedException e) {
			}
		}
	}
}
