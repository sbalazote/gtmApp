package com.lsntsolutions.gtmApp.webservice.helper;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.InputDetail;
import com.lsntsolutions.gtmApp.util.DateUtils;
import com.lsntsolutions.gtmApp.util.OperationResult;
import com.lsntsolutions.gtmApp.util.StringUtility;
import com.lsntsolutions.gtmApp.webservice.WebService;
import com.lsntsolutions.gtmApp.webservice.WebServiceHelper;
import com.inssjp.mywebservice.business.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

public class InputWSHelper {

    private static final Logger logger = Logger.getLogger(WebServiceHelper.class);

    @Autowired
    private com.lsntsolutions.gtmApp.service.PropertyService PropertyService;

    @Autowired
    private WebServiceHelper webServiceHelper;

    @Autowired
    private WebService webService;

    private static String ERROR_AGENT_HAS_NOT_INFORM = "No se ha podido informar el ingreso dado que los siguientes productos no fueron informados por el Agente de origen";
    private static String ERROR_CANNOT_CONNECT_TO_ANMAT_PENDING_TRANSACTION = "No se han podido obtener las transacciones pendientes";
    private static String ERROR_CANNOT_CONNECT_TO_ANMAT = "No se ha podido conectar con el Servidor de ANMAT";

    public OperationResult sendDrugInformationToAnmat(Input input) throws Exception {
        OperationResult operationResult = new OperationResult();
        operationResult.setResultado(false);
        WebServiceConfirmResult webServiceResult = null;
        List<String> errors = new ArrayList<>();
        String eventId = input.getEvent();

        if (eventId != null) {
            List<ConfirmacionTransaccionDTO> toConfirm = new ArrayList<>();
            this.getPendingTransactions(input.getInputDetails(), errors, toConfirm, input);
            // Si la lista esta vacia es porque de los productos que informan ninguno esta pendiente de informar por el agente de origen
            if(Boolean.valueOf(PropertyProvider.getInstance().getProp(PropertyProvider.IS_PRODUCTION))) {
                if (errors.isEmpty()) {
                    webServiceResult = this.confirmDrugs(toConfirm, errors);
                }
            }else{
                WebServiceConfirmResult webServiceConfirmResult = new WebServiceConfirmResult();
                webServiceConfirmResult.setResultado(true);
                webServiceConfirmResult.setCodigoTransaccion("11111");
                webServiceConfirmResult.setId_transac_asociada("22222");
                webServiceResult = webServiceConfirmResult;
            }

        } else {
            String error = "No ha podido obtenerse el evento a informar dado el concepto y el cliente/provedor seleccionados (Concepto: '"
                    + input.getConcept().toString() + "' Cliente/Proveedor '" + input.getClientOrProviderDescription() + "' Tipo de Agente: '"
                    + input.getClientOrProviderAgentDescription() + "'). El ingreso no fue informado.";
            errors.add(error);
        }
        operationResult.setMyOwnErrors(errors);
        if (webServiceResult != null) {
            operationResult.setFromWebServiceConfirmResult(webServiceResult);
        }
        logger.info(errors);
        return operationResult;
    }

    private WebServiceConfirmResult confirmDrugs(List<ConfirmacionTransaccionDTO> toConfirm, List<String> errors) {
        WebServiceConfirmResult webServiceResult = null;
        if (!toConfirm.isEmpty() && errors.isEmpty()) {
            logger.info("Iniciando consulta con ANMAT");
            ConfirmacionTransaccionDTO[] toConfirmArray = new ConfirmacionTransaccionDTO[toConfirm.size()];
            toConfirmArray = toConfirm.toArray(toConfirmArray);
            try {
                 webServiceResult = this.webService.confirmarTransaccion(toConfirmArray, this.PropertyService.get().getANMATName(), this.PropertyService.get().getDecryptPassword());
            } catch (Exception e) {
                errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT);
                e.printStackTrace();
            }
            if (webServiceResult == null) {
                errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT);
            }
        }
        logger.info(errors);
        return webServiceResult;
    }

    public void getPendingTransactions(List<InputDetail> details, List<String> errors, List<ConfirmacionTransaccionDTO> toConfirm, Input input) {
        try {
            Map<String, List<InputDetail>> inputDetailsMap = createDetailsMap(details);
            for (String gtin : inputDetailsMap.keySet()) {
                gtin = StringUtility.addLeadingZeros(gtin, 14);
                List<InputDetail> inputDetails = checkPendingInputProducts(errors,inputDetailsMap.get(gtin),gtin,toConfirm,input);
                if(inputDetails.size() > 0){
                    for(InputDetail inputDetail : inputDetails){
                        errors.add("GTIN: " + inputDetail.getGtin().getNumber() +" Serie: " + inputDetail.getSerialNumber() + " SERIE NO INFORMADO");
                    }
                }
            }
        } catch (Exception e) {
            logger.info(ERROR_CANNOT_CONNECT_TO_ANMAT_PENDING_TRANSACTION);
            errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT_PENDING_TRANSACTION);
        }
    }

    private Map<String, List<InputDetail>> createDetailsMap(List<InputDetail> details) {
        Map<String, List<InputDetail>> toReturn = new HashMap<>();
        for (InputDetail inputDetail : details) {
            if (inputDetail.getGtin() != null && inputDetail.getProduct().isInformAnmat() && "PS".equals(inputDetail.getProduct().getType())) {
                String gtin = StringUtility.addLeadingZeros(inputDetail.getGtin().getNumber(), 14);
                List<InputDetail> inputDetails = toReturn.get(gtin);
                if (inputDetails == null) {
                    inputDetails = new ArrayList<>();
                }
                inputDetails.add(inputDetail);
                toReturn.put(gtin, inputDetails);
            }
        }
        return toReturn;
    }

    private List<InputDetail> checkPendingInputProducts(List<String> errors, List<InputDetail> inputDetails, String gtin, List<ConfirmacionTransaccionDTO> toConfirm, Input input) throws Exception {
        TransaccionesNoConfirmadasWSResult pendingTransactions;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = DateUtils.getDateFrom(-this.PropertyService.get().getDaysAgoPendingTransactions());
        long nullValue = -1;
        Long pageNumber = new Long(1);
        Long total = new Long(1);
        while (pageNumber <= total && inputDetails.size() > 0) {
            pendingTransactions = this.webService.getTransaccionesNoConfirmadas(this.PropertyService.get().getANMATName(), this.PropertyService.get().getDecryptPassword(), nullValue, null, null, null, gtin, nullValue, null, null, simpleDateFormat.format(date), simpleDateFormat.format(new Date()), null, null, null, null, nullValue, null, null, pageNumber, new Long(100));
            if (pendingTransactions != null) {
                total = pendingTransactions.getCantPaginas();
                if (pendingTransactions.getErrores() == null) {
                    if (pendingTransactions.getList() != null) {
                        for (TransaccionPlainWS transaccionPlainWS : pendingTransactions.getList()) {
                            Iterator<InputDetail> iterator = inputDetails.iterator();
                            while (iterator.hasNext()) {
                                InputDetail inputDetail = iterator.next();
                                if (transaccionPlainWS.get_numero_serial().equals(inputDetail.getSerialNumber()) && transaccionPlainWS.get_gtin().equals(gtin)) {
                                    if(transaccionPlainWS.get_gln_origen().equals(input.getOriginGln())){
                                        ConfirmacionTransaccionDTO confirmacionTransaccionDTO = new ConfirmacionTransaccionDTO();
                                        confirmacionTransaccionDTO.setF_operacion(simpleDateFormat.format(input.getDate()));
                                        confirmacionTransaccionDTO.setP_ids_transac(Long.valueOf(transaccionPlainWS.get_id_transaccion()));
                                        toConfirm.add(confirmacionTransaccionDTO);
                                    }else{
                                        String error = "Gtin:" +  inputDetail.getGtin().getNumber() + " Serie: " + inputDetail.getSerialNumber() + " GLN: " + transaccionPlainWS.get_gln_origen() + " (" + transaccionPlainWS.get_razon_social_origen() + ")" +  ", Asignado: " + input.getOriginGln();
                                        errors.add(error);
                                    }
                                    iterator.remove();
                                    break;
                                }
                            }
                        }
                    }else{
                        errors.add("La lista de pendientes devuelta por ANMAT no es valida");
                        break;
                    }
                } else {
                    for (WebServiceError error : pendingTransactions.getErrores()) {
                        errors.add("Errores informados por ANMAT: " + error.get_c_error() + " - " + error.get_d_error());
                    }
                    break;
                }
            }
            pageNumber += 1;
        }
        return inputDetails;
    }

    private WebServiceResult sendDrugs(Input input, List<MedicamentosDTO> medicines, List<String> errors, String eventId) {
        WebServiceResult webServiceResult = null;
        for (InputDetail inputDetail : input.getInputDetails()) {
            // Solo si el producto informa anmat se hace el servicio
            if (inputDetail.getProduct().isInformAnmat()
                    && ("PS".equals(inputDetail.getProduct().getType()) || "SS".equals(inputDetail.getProduct().getType()))) {
                if (inputDetail.getGtin() != null) {
                    MedicamentosDTO drug = this.setDrug(input, eventId, inputDetail);
                    medicines.add(drug);
                } else {
                    String error = "El producto " + inputDetail.getProduct().getDescription() + " no registra GTIN, no puede ser informado.";
                    errors.add(error);
                }
            }
        }
        if (!medicines.isEmpty() && errors.isEmpty()) {
            logger.info("Iniciando consulta con ANMAT");
            webServiceResult = this.webServiceHelper.run(medicines, this.PropertyService.get().getANMATName(), this.PropertyService.get().getDecryptPassword(),
                    errors);
            if (webServiceResult == null) {
                errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT);
            }
        }
        logger.info(errors);
        return webServiceResult;
    }


    private MedicamentosDTO setDrug(Input input, String eventId, InputDetail inputDetail) {
        MedicamentosDTO drug = new MedicamentosDTO();
        String deliveryNote = "R" + StringUtility.addLeadingZeros(input.getDeliveryNoteNumber(), 12);
        String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(inputDetail.getExpirationDate()).toString();

        if ("SS".equals(inputDetail.getProduct().getType())) {
            String startTraceEvent = this.PropertyService.get().getStartTraceConcept().getEventOnInput(input.getClientOrProviderAgent());
            if (startTraceEvent != null) {
                eventId = startTraceEvent;
            }
        }
        this.webServiceHelper.setDrug(drug, input.getOriginGln(), this.PropertyService.get().getGln(), input.getOriginTax(), this.PropertyService.get()
                        .getTaxId(), deliveryNote, expirationDate, inputDetail.getGtin().getNumber(), eventId, inputDetail.getSerialNumber(), inputDetail.getBatch(),
                input.getDate(), false, null, null, null, null, null);

        return drug;
    }

}
