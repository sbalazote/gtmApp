package com.lsntsolutions.gtmApp.webservice;

import com.inssjp.mywebservice.business.MedicamentosDTO;
import com.inssjp.mywebservice.business.WebServiceError;
import com.inssjp.mywebservice.business.WebServiceResult;
import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.model.Affiliate;
import com.lsntsolutions.gtmApp.model.ClientAffiliate;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WebServiceHelper {

	private static final Logger logger = Logger.getLogger(WebServiceHelper.class);

	@Autowired
	private WebService webService;

	@Autowired
	private com.lsntsolutions.gtmApp.service.PropertyService PropertyService;

	public WebServiceResult run(List<MedicamentosDTO> medicines, String username, String password, List<String> errors) {
		this.setProxy();
		WebServiceResult result = null;
		try {
			MedicamentosDTO[] medicamentos = new MedicamentosDTO[medicines.size()];
			for (int i = 0; i < medicines.size(); ++i) {
				medicamentos[i] = medicines.get(i);
			}
			return this.webService.sendMedicamentos(medicamentos, username, password);

		} catch (Exception e) {
			errors.add("No se pudo enviar la transacci�n");
			logger.error(e);
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
						String expirationDate, String gtin, String eventId, String serialNumber, String batch, Date date, boolean isDeliveryNote,
						Affiliate affiliate, Integer medicalInsuranceCode, String associateNumber) {
		String dateFormatted = new SimpleDateFormat("dd/MM/yyyy").format(date).toString();
		drug.setF_evento(dateFormatted);

		if (isDeliveryNote) {
			drug.setH_evento(new SimpleDateFormat("HH:mm").format(date).toString());
		} else {
			drug.setH_evento(new SimpleDateFormat("HH:mm").format(new Date()).toString());
		}
		Boolean isProducion = Boolean.valueOf(PropertyProvider.getInstance().getProp(PropertyProvider.IS_PRODUCTION));
		if (isProducion) {
			drug.setGln_origen(originGLN);
			drug.setGln_destino(destinationGLN);

			drug.setCuit_origen(originTax);
			drug.setCuit_destino(destinationTax);
			drug.setId_evento(eventId);
			drug.setGtin(StringUtility.addLeadingZeros(gtin, 14));
			if(associateNumber != null){
				drug.setNro_asociado(associateNumber);
			}
			if (affiliate != null) {
				if(affiliate.getSurname() != null) {
					drug.setApellido(affiliate.getSurname());
				}
				if(affiliate.getName() != null){
					drug.setNombres(affiliate.getName());
				}
				if(affiliate.getAddress() != null){
					drug.setDireccion(affiliate.getAddress());
				}
				if(affiliate.getApartment() != null){
					drug.setDepto(affiliate.getApartment());
				}
				if(affiliate.getDocument() != null){
					drug.setN_documento(affiliate.getDocument());
				}
				if(affiliate.getDocumentType() != null){
					drug.setTipo_documento(affiliate.getDocumentType());
				}
				if(affiliate.getFloor() != null){
					drug.setPiso(affiliate.getFloor());
				}
				if(affiliate.getLocality() != null){
					drug.setLocalidad(affiliate.getLocality());
				}
				if(affiliate.getNumber() != null){
					drug.setNumero(affiliate.getNumber());
				}
				if(affiliate.getPhone() != null){
					drug.setTelefono(affiliate.getPhone());
				}
				if(affiliate.getZipCode() != null){
					drug.setN_postal(affiliate.getZipCode());
				}
			}
			if (medicalInsuranceCode != null) {
				drug.setId_obra_social(String.valueOf(medicalInsuranceCode));
			}
		} else {
			drug.setGln_origen(Constants.TEST_DESTINATION_GLN);
			drug.setGln_destino(Constants.TEST_ORIGIN_GLN);

			drug.setCuit_origen(Constants.TEST_ORIGIN_TAXID);
			drug.setCuit_destino(Constants.TEST_DESTINATION_TAXID);
			drug.setId_evento(Constants.TEST_EVENT);
			drug.setGtin(Constants.TEST_GTIN);
			if (affiliate != null) {
				drug.setNro_asociado(associateNumber);
			}
			if (medicalInsuranceCode != null) {
				drug.setId_obra_social(String.valueOf(medicalInsuranceCode));
			}
		}
		if (deliveryNote != null) {
			drug.setN_remito(deliveryNote);
		}
		drug.setVencimiento(expirationDate);

		drug.setNumero_serial(serialNumber);

		drug.setLote(batch);
	}

	public void setProxy() {
		if (this.PropertyService.get().isHasProxy()) {
			this.webService.setProxy(this.PropertyService.get().getProxy());
			this.webService.setProxyPort(this.PropertyService.get().getProxyPort());
			this.webService.setInformProxy(true);
		} else {
			this.webService.setInformProxy(false);
		}
	}
}
