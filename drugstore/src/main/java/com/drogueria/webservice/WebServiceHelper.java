package com.drogueria.webservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.drogueria.config.PropertyProvider;
import com.drogueria.constant.Constants;
import com.drogueria.service.DrugstorePropertyService;
import com.drogueria.util.StringUtility;
import com.inssjp.mywebservice.business.MedicamentosDTO;
import com.inssjp.mywebservice.business.WebServiceError;
import com.inssjp.mywebservice.business.WebServiceResult;

public class WebServiceHelper {

	private static final Logger logger = Logger.getLogger(WebServiceHelper.class);

	@Autowired
	private WebService webService;

	@Autowired
	private DrugstorePropertyService drugstorePropertyService;

	public WebServiceResult run(List<MedicamentosDTO> medicines, String username, String password, List<String> errors) {
		this.setProxy();
		WebServiceResult result = null;
		try {
			MedicamentosDTO[] medicamentos = new MedicamentosDTO[medicines.size()];
			for (int i = 0; i < medicines.size(); ++i) {
				medicamentos[i] = medicines.get(i);
			}
			return result = this.webService.sendMedicamentos(medicamentos, username, password);

		} catch (Exception e) {
			errors.add("No se pudo enviar la transacci�n");
			logger.info(e);
		}
		if (processResult(result)) {
			logger.info("La consulta con ANMAT fue satisfactoria. C�digo de Transacci�n: " + result.getCodigoTransaccion());
		}
		return result;
	}

	private static boolean processResult(WebServiceResult result) {
		boolean isOk = false;
		if (result != null) {
			if (result.getResultado()) {
				isOk = true;
			} else {
				WebServiceError[] errors = result.getErrores();
				for (WebServiceError webServiceError : errors) {
					logger.error("C�digo de Error: " + webServiceError.get_c_error() + "Descripci�n de Error: " + webServiceError.get_d_error());
				}
			}
		}
		return isOk;
	}

	public WebServiceResult sendCancelacTransacc(Long transactionCode, String username, String password) {
		this.setProxy();
		WebServiceResult webServiceResult = null;
		try {
			webServiceResult = this.webService.sendCancelacTransacc(transactionCode, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webServiceResult;
	}

	public void setDrug(MedicamentosDTO drug, String originGLN, String destinationGLN, String originTax, String destinationTax, String deliveryNote,
			String expirationDate, String gtin, String eventId, String serialNumber, String batch, Date date, boolean isDeliveryNote) {
		String dateFormatted = new SimpleDateFormat("dd/MM/yyyy").format(date).toString();
		drug.setF_evento(dateFormatted);

		if (isDeliveryNote) {
			drug.setH_evento(new SimpleDateFormat("HH:mm").format(date).toString());
		} else {
			drug.setH_evento(new SimpleDateFormat("HH:mm").format(new Date()).toString());
		}
		String isProducion = PropertyProvider.getInstance().getProp(PropertyProvider.IS_PRODUCTION);
		if (isProducion.equals("true")) {
			drug.setGln_origen(originGLN);
			drug.setGln_destino(destinationGLN);

			drug.setCuit_origen(originTax);
			drug.setCuit_destino(destinationTax);
			drug.setId_evento(eventId);
			drug.setGtin(StringUtility.addLeadingZeros(gtin, 14));
		} else {
			drug.setGln_origen(Constants.TEST_DESTINATION_GLN);
			drug.setGln_destino(Constants.TEST_ORIGIN_GLN);

			drug.setCuit_origen(Constants.TEST_ORIGIN_TAXID);
			drug.setCuit_destino(Constants.TEST_DESTINATION_TAXID);
			drug.setId_evento(Constants.TEST_EVENT);
			drug.setGtin(Constants.TEST_GTIN);
		}
		if (deliveryNote != null) {
			drug.setN_remito(deliveryNote);
		}
		drug.setVencimiento(expirationDate);

		drug.setNumero_serial(serialNumber);

		drug.setLote(batch);
	}

	public void setProxy() {
		if (this.drugstorePropertyService.get().isHasProxy()) {
			this.webService.setProxy(this.drugstorePropertyService.get().getProxy());
			this.webService.setProxyPort(this.drugstorePropertyService.get().getProxyPort());
			this.webService.setInformProxy(true);
		} else {
			this.webService.setInformProxy(false);
		}
	}
}
