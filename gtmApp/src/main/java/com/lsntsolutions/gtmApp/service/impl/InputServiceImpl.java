package com.lsntsolutions.gtmApp.service.impl;

import com.inssjp.mywebservice.business.WebServiceResult;
import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.InputDTO;
import com.lsntsolutions.gtmApp.dto.InputDetailDTO;
import com.lsntsolutions.gtmApp.exceptions.*;
import com.lsntsolutions.gtmApp.helper.SelfSerializedTagsPrinter;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.persistence.dao.InputDAO;
import com.lsntsolutions.gtmApp.query.InputQuery;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.OperationResult;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class InputServiceImpl implements InputService {

	private static final Logger logger = Logger.getLogger(InputServiceImpl.class);

	@Autowired
	private InputDAO inputDAO;
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private ProviderService providerService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private ProductService productService;
	@Autowired
	private StockService stockService;
	@Autowired
	private PropertyService PropertyService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private TraceabilityService traceabilityService;
	@Autowired
	private ProductGtinService productGtinService;
	@Autowired
	private LogisticsOperatorService logisticsOperatorService;

	@Override
	public Input save(InputDTO inputDTO, Boolean isSerializedReturn, String username) throws Exception {
		Boolean mustGenerateTags = this.prepareSelfSerializedInputDetails(inputDTO, isSerializedReturn);
		Input input = this.buildInput(inputDTO);
		this.inputDAO.save(input);
		if (mustGenerateTags) {
			this.printSelfSerializedTags(input);
		}
		this.auditService.addAudit(username, RoleOperation.INPUT.getId(), AuditState.COMFIRMED, input.getId());

		logger.info("Se ha generado exitosamente la Recepcion de Mercaderia numero: " + input.getId());

		return input;
	}

	private void printSelfSerializedTags(Input input) {
		SelfSerializedTagsPrinter selfSerializedTagsPrinter = new SelfSerializedTagsPrinter();
		String gln = this.PropertyService.get().getGln();
		String inputId = input.getId().toString();
		selfSerializedTagsPrinter.print(input.getInputDetails(),inputId,gln,this.PropertyService.get().getSelfSerializedTagFilepath() );
		selfSerializedTagsPrinter.close();
	}

	@Override
	public Input get(Integer id) {
		return this.inputDAO.get(id);
	}

	@Override
	public List<Input> getAll() {
		return this.inputDAO.getAll();
	}

	@Override
	public Boolean existsSerial(String serialNumber, Integer productId, String gtin) {
		return this.inputDAO.existsSerial(serialNumber, productId, gtin);
	}

	@Override
	public List<Input> getInputForSearch(InputQuery inputQuery) {
		return this.inputDAO.getInputForSearch(inputQuery);
	}

	private Boolean prepareSelfSerializedInputDetails(InputDTO inputDTO, Boolean isSerializedReturn) throws NullInputDetailsException, NullProductTypeException {
		List<InputDetailDTO> newDetails = new ArrayList<>();
		List<InputDetailDTO> selfSerializedDetails = new ArrayList<>();
		Integer totalAmount = 0;

		if (inputDTO.getInputDetails() == null) {
			throw new NullInputDetailsException("El listado de productos de Ingreso indicado es nulo.");
		}

		for (InputDetailDTO inputDetailDTO : inputDTO.getInputDetails()) {
			if (inputDetailDTO.getProductType() == null) {
				throw new NullProductTypeException("El Tipo de Producto de Ingreso indicado es nulo.");
			}

			if ("SS".equals(inputDetailDTO.getProductType())) {
				selfSerializedDetails.add(inputDetailDTO);
				totalAmount += inputDetailDTO.getAmount();
			} else {
				newDetails.add(inputDetailDTO);
			}
		}

		if (!selfSerializedDetails.isEmpty() && !isSerializedReturn.booleanValue()) {
			Property Property = this.PropertyService.getAndUpdateSelfSerializedTag(totalAmount);
			List<InputDetailDTO> newSelfSerializedDetails = this.generateSelfSerializedDetails(totalAmount, selfSerializedDetails, Property);
			newDetails.addAll(newSelfSerializedDetails);
		}

		if (isSerializedReturn.booleanValue()) {
			newDetails.addAll(selfSerializedDetails);
		}

		inputDTO.setInputDetails(newDetails);

		return (!selfSerializedDetails.isEmpty());
	}

	private List<InputDetailDTO> generateSelfSerializedDetails(Integer totalAmount, List<InputDetailDTO> selfSerializedDetails, Property Property) {
		List<InputDetailDTO> newSelfSerializedDetails = new ArrayList<>();
		Integer currentSerialNumber = Property.getLastTag() - totalAmount + 1;

		for (InputDetailDTO inputDetailDTO : selfSerializedDetails) {
			for (int i = 0; i < inputDetailDTO.getAmount(); i++) {
				InputDetailDTO newInputDetailDTO = new InputDetailDTO();
				newInputDetailDTO.setProductId(inputDetailDTO.getProductId());
				newInputDetailDTO.setProductType(inputDetailDTO.getProductType());
				newInputDetailDTO.setBatch(inputDetailDTO.getBatch());
				newInputDetailDTO.setExpirationDate(inputDetailDTO.getExpirationDate());
				newInputDetailDTO.setAmount(1);

				String serialNumber = StringUtility.addLeadingZeros(currentSerialNumber, Constants.SERIAL_NUMBER_LENGTH);

				// String serial = String.format(SELF_SERIALIZED_SERIAL_FORMAT, Property.getGln(), serialNumber);
				// newInputDetailDTO.setSerialNumber(serial);

				newInputDetailDTO.setSerialNumber(Property.getGln() + serialNumber);

				newSelfSerializedDetails.add(newInputDetailDTO);

				currentSerialNumber++;
			}
		}

		return newSelfSerializedDetails;
	}

	private Input buildInput(InputDTO inputDTO) throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat expirationDateFormatter = new SimpleDateFormat("ddMMyy");

		Input input = new Input();
		if (inputDTO.getAgreementId() == null) {
			logger.error("El identificador de Auditoria es nulo.");
			throw new NullAgreementIdException("El identificador de Auditoria es nulo.");
		}

		if (inputDTO.getConceptId() == null) {
			logger.error("El identificador de Concepto es nulo.");
			throw new NullConceptIdException("El identificador de Concepto es nulo.");
		}

		if ((inputDTO.getProviderId() == null) && (inputDTO.getDeliveryLocationId() == null)) {
			logger.error("El identificador de Proveedor y el identificador de Lugar de Entrega son nulos.");
			throw new NullProviderAndDeliveryLocationIdsException("El identificador de Proveedor y el identificador de Lugar de Entrega son nulos.");
		}

		if (inputDTO.getDate() == null) {
			logger.error("La Fecha indicada es nula.");
			throw new NullDateException("La Fecha indicada es nula.");
		}

		if (inputDTO.getDeliveryNoteNumber() == null) {
			logger.error("El nro. de Remito indicado es nulo.");
			throw new NullDeliveryNoteNumberException("El nro. de Remito indicado es nulo.");
		}

		/* if (inputDTO.getPurchaseOrderNumber() == null) { logger.error("El nro. de Orden de Compra indicado es nulo."); throw new
		 * NullPurchaseOrderNumberException("El nro. de Orden de Compra indicado es nulo."); } */

		if (inputDTO.getInputDetails() == null) {
			logger.error("El listado de productos de Ingreso indicado es nulo.");
			throw new NullInputDetailsException("El listado de productos de Ingreso indicado es nulo.");
		}

		Agreement agreement = this.agreementService.get(inputDTO.getAgreementId());
		input.setAgreement(agreement);
		input.setConcept(this.conceptService.get(inputDTO.getConceptId()));

		if (inputDTO.getProviderId() != null) {
			input.setProvider(this.providerService.get(inputDTO.getProviderId()));
		} else {
			input.setDeliveryLocation(this.deliveryLocationService.get(inputDTO.getDeliveryLocationId()));
		}

		if (inputDTO.getLogisticsOperatorId() != null) {
			input.setLogisticsOperator(this.logisticsOperatorService.get(inputDTO.getLogisticsOperatorId()));
		}

		try {
			input.setDate(dateFormatter.parse(inputDTO.getDate()));
		} catch (ParseException pe) {
			logger.error("La Fecha de Ingreso indicada esta malformada.");
			throw new DateParseException("La Fecha de Ingreso indicada esta malformada.");
		}
		input.setDeliveryNoteNumber(inputDTO.getDeliveryNoteNumber());
		input.setPurchaseOrderNumber(inputDTO.getPurchaseOrderNumber());

		List<InputDetail> details = new ArrayList<>();
		Product product = null;
		for (InputDetailDTO inputDetailDTO : inputDTO.getInputDetails()) {
			if (inputDetailDTO.getProductId() == null) {
				logger.error("El identificador de Producto de Ingreso es nulo.");
				throw new NullProductIdException("El identificador de Producto de Ingreso es nulo.");
			}

			if (inputDetailDTO.getAmount() == null) {
				logger.error("La cantidad de Producto de Ingreso es nula.");
				throw new NullAmountException("La cantidad de Producto de Ingreso es nula.");
			}

			if (inputDetailDTO.getBatch() == null) {
				logger.error("El Lote de Producto de Ingreso es nulo.");
				throw new NullBatchException("El Lote de Producto de Ingreso es nulo.");
			}

			if (inputDetailDTO.getExpirationDate() == null) {
				logger.error("La Fecha de Vencimiento de Producto de Ingreso es nula.");
				throw new NullDateException("La Fecha de Vencimiento de Producto de Ingreso es nula.");
			}

			if ((inputDetailDTO.getSerialNumber() == null) && ("PS".equals(inputDetailDTO.getProductType()))) {
				logger.error("El Serie de Producto de Ingreso es nulo.");
				throw new NullSerialNumberException("El Serie de Producto de Ingreso es nulo.");
			}

			if (product == null || !product.getId().equals(inputDetailDTO.getProductId())) {
				product = this.productService.get(inputDetailDTO.getProductId());
			}

			InputDetail inputDetail = new InputDetail();
			inputDetail.setAmount(inputDetailDTO.getAmount());
			inputDetail.setBatch(inputDetailDTO.getBatch());

			try {
				inputDetail.setExpirationDate(expirationDateFormatter.parse(inputDetailDTO.getExpirationDate()));
			} catch (ParseException pe) {
				logger.error("La Fecha de Vencimiento de Ingreso esta malformada.");
				throw new DateParseException("La Fecha de Vencimiento de Ingreso esta malformada.");
			}

			if (inputDetailDTO.getGtin() != null) {
				ProductGtin productGtin = this.productGtinService.getByNumber(inputDetailDTO.getGtin());
				inputDetail.setGtin(productGtin);
			} else {
				if (product.getLastProductGtin() != null) {
					inputDetail.setGtin(product.getLastProductGtin());
				}
			}
			if (("PS".equals(inputDetailDTO.getProductType())) || ("SS".equals(inputDetailDTO.getProductType()))) {
				inputDetail.setSerialNumber(inputDetailDTO.getSerialNumber());
			}
			inputDetail.setProduct(product);
			details.add(inputDetail);
		}

		input.setForcedInput(false);
		input.setInputDetails(details);

		if (input.hasToInform() && this.PropertyService.get().isInformAnmat()) {
			input.setInformAnmat(true);
		} else {
			input.setInformAnmat(false);
			for (InputDetail inputDetail : input.getInputDetails()) {
				this.stockService.updateStock(inputDetail, agreement);
			}
		}

		input.setCancelled(false);
		input.setTransactionCodeANMAT(null);
		input.setInformed(false);

		return input;
	}

	@Override
	public boolean getCountInputSearch(InputQuery inputQuery) {
		return this.inputDAO.getCountInputSearch(inputQuery);
	}

	@Override
	public List<Input> getInputToAuthorize() {
		return this.inputDAO.getInputToAuthorize();
	}

    @Override
    public List<Input> getForcedInputs() {
        return this.inputDAO.getForcedInputs();
    }

	@Override
	public void saveAndUpdateStock(Input input) {
		for (InputDetail inputDetail : input.getInputDetails()) {
			this.stockService.updateStock(inputDetail, input.getAgreement());
		}
		this.inputDAO.save(input);
	}

	@Override
	public void save(Input input) {
		this.inputDAO.save(input);
	}

	@Override
	public Input update(InputDTO inputDTO) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Input input = this.get(inputDTO.getId());
		input.setConcept(this.conceptService.get(inputDTO.getConceptId()));
		if (inputDTO.getProviderId() == null) {
			input.setProvider(null);
		} else {
			input.setProvider(this.providerService.get(inputDTO.getProviderId()));
		}
		if (inputDTO.getLogisticsOperatorId() == null) {
			input.setLogisticsOperator(null);
		} else {
			input.setLogisticsOperator(this.logisticsOperatorService.get(inputDTO.getLogisticsOperatorId()));
		}
		if (inputDTO.getDeliveryLocationId() == null) {
			input.setDeliveryLocation(null);
		} else {
			input.setDeliveryLocation(this.deliveryLocationService.get(inputDTO.getDeliveryLocationId()));
		}
		input.setPurchaseOrderNumber(inputDTO.getPurchaseOrderNumber());
		input.setDeliveryNoteNumber(inputDTO.getDeliveryNoteNumber());
		try {
			input.setDate(dateFormatter.parse(inputDTO.getDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (input.hasToInform()) {
				input.setInformAnmat(true);
			} else {
				input.setInformAnmat(false);
				for (InputDetail inputDetail : input.getInputDetails()) {
					this.stockService.updateStock(inputDetail, input.getAgreement());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	@Override
	public void saveAndRemoveFromStock(Input input) {
		List<Stock> stocks = new ArrayList<>();
		for (InputDetail inputDetail : input.getInputDetails()) {
			Stock stock = new Stock();
			stock.setAgreement(input.getAgreement());
			stock.setAmount(inputDetail.getAmount());
			stock.setBatch(inputDetail.getBatch());
			stock.setExpirationDate(inputDetail.getExpirationDate());
			stock.setProduct(inputDetail.getProduct());
			stock.setSerialNumber(inputDetail.getSerialNumber());
			if (inputDetail.getGtin() != null) {
				stock.setGtin(inputDetail.getGtin());
			}
			stocks.add(stock);
		}
		this.stockService.removeFromStock(stocks);
		this.save(input);
	}

	@Override
	public OperationResult updateInput(InputDTO inputDTO, String username) throws Exception {
		Input input = this.update(inputDTO);
		return informANMAT(input);
	}

	@Override
	@Async
	public void sendAsyncTransaction(Input input) throws Exception {
		informANMAT(input);
	}

	private OperationResult informANMAT(Input input) throws Exception {
		boolean selfSerializedHasinformed = true;
		boolean providerSerializedHasInformed = true;
		OperationResult providerSerializedResult = null;
		OperationResult selfSerializedResult = null;
		OperationResult result = new OperationResult();
		result.setProviderSerializedInform(false);
		result.setSelfSerializedInform(false);
		result.setOperationId(String.valueOf(input.getId()));
		if(input.hasToInform(Constants.PROVIDER_SERIALIZED) && input.getTransactionCodeANMAT() == null) {
			providerSerializedResult = this.traceabilityService.processInputPendingTransactions(input);
			providerSerializedHasInformed = (providerSerializedResult != null && providerSerializedResult.getResultado());
		}
		if(input.hasToInform(Constants.SELF_SERIALIZED) && input.getSelfSerializedTransactionCodeANMAT() == null) {
			selfSerializedResult = this.traceabilityService.processSelfSerializedInputPendingTransactions(input);
			selfSerializedHasinformed = (selfSerializedResult != null && selfSerializedResult.getResultado());
		}
		if(selfSerializedHasinformed && providerSerializedHasInformed){
			if (input.hasToInform(Constants.SELF_SERIALIZED) && selfSerializedHasinformed && selfSerializedResult != null && selfSerializedResult.getResultado()) {
				input.setSelfSerializedTransactionCodeANMAT(selfSerializedResult.getCodigoTransaccion());
				result.setSelfSerializedInform(true);
				result.setSelfSerializedTransactionCode(selfSerializedResult.getCodigoTransaccion());
			}
			if(input.hasToInform(Constants.PROVIDER_SERIALIZED) && providerSerializedHasInformed && providerSerializedResult != null && providerSerializedResult.getResultado()){
				input.setTransactionCodeANMAT(providerSerializedResult.getCodigoTransaccion());
				result.setCodigoTransaccion(providerSerializedResult.getCodigoTransaccion());
				result.setProviderSerializedInform(true);
			}
			input.setInformed(true);
			this.saveAndUpdateStock(input);
			result.setResultado(true);
		}else{
			result.setResultado(false);
		}
		if(selfSerializedResult != null){
			result.setMySelfSerializedOwnErrors(selfSerializedResult.getMySelfSerializedOwnErrors());
		}else{
			result.setMySelfSerializedOwnErrors(new ArrayList<String>());
		}
		if(providerSerializedResult != null){
			result.setMyOwnErrors(providerSerializedResult.getMyOwnErrors());
		}else{
			result.setMyOwnErrors(new ArrayList<String>());
		}
		return result;
	}

	@Override
	public boolean cancelInput(Integer inputId) {
		boolean toReturn = false;
		Input input = this.get(inputId);
		boolean hasSerials = input.hasSerials();
		boolean canCancel = this.canCancelInput(input);
		if (canCancel && hasSerials) {
			try {
				if (input.hasToInform() && !input.isForcedInput()) {
					boolean selfSerializedHasinformed = true;
					boolean providerSerializedHasInformed = true;
					if(input.hasToInform(Constants.PROVIDER_SERIALIZED)){
						WebServiceResult providerSerializedResult = this.traceabilityService.cancelInputTransaction(input.getTransactionCodeANMAT());
						providerSerializedHasInformed = (providerSerializedResult != null && (providerSerializedResult.getResultado() ||
								(providerSerializedResult.getErrores() != null && providerSerializedResult.getErrores(0).get_c_error().equals(Constants.ERROR_ANMAT_ALREADY_CANCELLED))));
					}
					if(input.hasToInform(Constants.SELF_SERIALIZED)){
						WebServiceResult selfSerializedResult = this.traceabilityService.cancelInputTransaction(input.getSelfSerializedTransactionCodeANMAT());
						selfSerializedHasinformed = (selfSerializedResult != null && selfSerializedResult.getResultado() ||
								(selfSerializedResult.getErrores() != null && selfSerializedResult.getErrores(0).get_c_error().equals(Constants.ERROR_ANMAT_ALREADY_CANCELLED)));
					}
					if(selfSerializedHasinformed && providerSerializedHasInformed) {
						input.setCancelled(true);
						this.saveAndRemoveFromStock(input);
						toReturn = true;
					}
				} else {
					input.setCancelled(true);
					this.saveAndRemoveFromStock(input);
					toReturn = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (canCancel && !hasSerials) {
			input.setCancelled(true);
			this.saveAndRemoveFromStock(input);
			toReturn = true;
		}
		return toReturn;
	}

	@Override
	public List<Input> getCancelables(InputQuery inputQuery) {
		return this.inputDAO.getCancelables(inputQuery);
	}

	@Override
	public Input authorizeWithoutInform(InputDTO inputDTO, String name) {
		Input input = this.update(inputDTO);
		if (input.isInformAnmat()) {
			input.setInformed(true);
		}
		this.saveAndUpdateStock(input);
		this.auditService.addAudit(name, RoleOperation.INPUT_AUTHORIZATION.getId(), AuditState.COMFIRMED, input.getId());
		return input;
	}

	private boolean canCancelInput(Input input) {
		for (InputDetail inputDetail : input.getInputDetails()) {
			// Los serializados se fija si existen en stock
			if (inputDetail.getSerialNumber() != null) {
				String gtin = inputDetail.getGtin() != null ? inputDetail.getGtin().getNumber() : null;
				if (!this.stockService.existsSerial(inputDetail.getProduct().getId(),gtin,input.getAgreement().getId(),inputDetail.getSerialNumber())) {
					return false;
				}
			} else {
				// Si no tiene serie entonces es lote y vencimiento, se fija que haya la cantidad suficiente.
				if (!this.stockService.hasStock(inputDetail.getProduct().getId(), inputDetail.getBatch(), inputDetail.getExpirationDate(), input.getAgreement()
						.getId(), inputDetail.getAmount())) {
					return false;
				}
			}
		}
		return true;
	}

    @Override
    public OperationResult sendTransaction(Input input) throws Exception {
		return informANMAT(input);
    }

	@Override
	public boolean isConceptInUse(Integer conceptId){
		return this.inputDAO.isConceptInUse(conceptId);
	}

	@Override
	public Input importStock(List<InputDetail> inputDetails, Integer agreementId, Integer conceptId, Integer providerId, String userName) {
		Concept concept = this.conceptService.get(conceptId);
		Agreement agreement = this.agreementService.get(agreementId);
		Provider provider = this.providerService.get(providerId);
		Input input = new Input();
		input.setAgreement(agreement);
		input.setConcept(concept);
		input.setProvider(provider);
		input.setDate(new Date());
		input.setInformAnmat(false);
		input.setCancelled(false);
		input.setForcedInput(false);
		input.setInformed(false);
		input.setInputDetails(inputDetails);
		for(InputDetail inputDetail : inputDetails){
			this.stockService.updateStock(inputDetail,agreement);
		}
		this.save(input);

		this.auditService.addAudit(userName, RoleOperation.INPUT.getId(), AuditState.COMFIRMED, input.getId());
		return input;
	}
}
