package com.drogueria.service.impl;

import static com.drogueria.constant.Constants.SELF_SERIALIZED_TAG_LEADING_REGEX;
import static com.drogueria.constant.Constants.SERIAL_NUMBER_LENGTH;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.constant.AuditState;
import com.drogueria.constant.Constants;
import com.drogueria.constant.RoleOperation;
import com.drogueria.dto.InputDTO;
import com.drogueria.dto.InputDetailDTO;
import com.drogueria.exceptions.DateParseException;
import com.drogueria.exceptions.NullAgreementIdException;
import com.drogueria.exceptions.NullAmountException;
import com.drogueria.exceptions.NullBatchException;
import com.drogueria.exceptions.NullConceptIdException;
import com.drogueria.exceptions.NullDateException;
import com.drogueria.exceptions.NullDeliveryNoteNumberException;
import com.drogueria.exceptions.NullInputDetailsException;
import com.drogueria.exceptions.NullProductIdException;
import com.drogueria.exceptions.NullProductTypeException;
import com.drogueria.exceptions.NullProviderAndDeliveryLocationIdsException;
import com.drogueria.exceptions.NullSerialNumberException;
import com.drogueria.helper.SelfSerializedTagsPrinter;
import com.drogueria.model.Agreement;
import com.drogueria.model.DrugstoreProperty;
import com.drogueria.model.Input;
import com.drogueria.model.InputDetail;
import com.drogueria.model.Product;
import com.drogueria.model.ProductGtin;
import com.drogueria.model.Stock;
import com.drogueria.persistence.dao.InputDAO;
import com.drogueria.query.InputQuery;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.DrugstorePropertyService;
import com.drogueria.service.InputService;
import com.drogueria.service.OrderService;
import com.drogueria.service.OutputService;
import com.drogueria.service.ProductGtinService;
import com.drogueria.service.ProductService;
import com.drogueria.service.ProviderService;
import com.drogueria.service.StockService;
import com.drogueria.service.TraceabilityService;
import com.drogueria.util.OperationResult;
import com.drogueria.util.StringUtils;
import com.inssjp.mywebservice.business.WebServiceResult;

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
	private DrugstorePropertyService drugstorePropertyService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private OutputService outputService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private TraceabilityService traceabilityService;
	@Autowired
	private ProductGtinService productGtinService;

	@Override
	public Input save(InputDTO inputDTO, Boolean isSerializedReturn, String username) throws Exception {
		Boolean mustGenerateTags = this.prepareSelfSerializedInputDetails(inputDTO, isSerializedReturn);
		Input input = this.buildInput(inputDTO);
		this.inputDAO.save(input);
		if (mustGenerateTags) {
			this.printSelfSerializedTags(input);
		}
		this.auditService.addAudit(username, RoleOperation.INPUT.getId(), AuditState.COMFIRMED, input.getId());

		logger.info("Se ha generado exitosamente la Recepción de Mercadería número: " + input.getId());

		return input;
	}

	@Override
	@Async
	public void sendAsyncTransaction(Input input) throws Exception {
		// Se corre el proceso asyncronicamente
		OperationResult result = this.traceabilityService.processInputPendingTransactions(input);
		if (result != null) {
			// Si no hubo errores se setea el codigo de transaccion del ingreso y se ingresa la mercaderia en stock.
			if (result.getResultado()) {
				input.setTransactionCodeANMAT(result.getCodigoTransaccion());
				this.saveAndUpdateStock(input);
				input.setInformed(true);
			}
		}
	}

	private void printSelfSerializedTags(Input input) {
		SelfSerializedTagsPrinter selfSerializedTagsPrinter = new SelfSerializedTagsPrinter(this.drugstorePropertyService.get().getSelfSerializedTagFilepath());

		String inputId = input.getId().toString();
		for (InputDetail inputDetail : input.getInputDetails()) {
			if ("SS".equals(inputDetail.getProduct().getType())) {
				String productCode = inputDetail.getProduct().getCode().toString();
				String batch = inputDetail.getBatch();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String expirationDate = sdf.format(inputDetail.getExpirationDate());
				String serialNumber = inputDetail.getSerialNumber();
				String tag = serialNumber.replaceFirst(SELF_SERIALIZED_TAG_LEADING_REGEX, "");
				selfSerializedTagsPrinter.print(inputId, productCode, batch, expirationDate, tag);
			}
		}
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
			DrugstoreProperty drugstoreProperty = this.drugstorePropertyService.getAndUpdateSelfSerializedTag(totalAmount);
			List<InputDetailDTO> newSelfSerializedDetails = this.generateSelfSerializedDetails(totalAmount, selfSerializedDetails, drugstoreProperty);
			newDetails.addAll(newSelfSerializedDetails);
		}

		if (isSerializedReturn.booleanValue()) {
			newDetails.addAll(selfSerializedDetails);
		}

		inputDTO.setInputDetails(newDetails);

		return (!selfSerializedDetails.isEmpty());
	}

	private List<InputDetailDTO> generateSelfSerializedDetails(Integer totalAmount, List<InputDetailDTO> selfSerializedDetails,
			DrugstoreProperty drugstoreProperty) {
		List<InputDetailDTO> newSelfSerializedDetails = new ArrayList<>();
		Integer currentSerialNumber = drugstoreProperty.getLastTag() - totalAmount + 1;

		for (InputDetailDTO inputDetailDTO : selfSerializedDetails) {
			for (int i = 0; i < inputDetailDTO.getAmount(); i++) {
				InputDetailDTO newInputDetailDTO = new InputDetailDTO();
				newInputDetailDTO.setProductId(inputDetailDTO.getProductId());
				newInputDetailDTO.setProductType(inputDetailDTO.getProductType());
				newInputDetailDTO.setBatch(inputDetailDTO.getBatch());
				newInputDetailDTO.setExpirationDate(inputDetailDTO.getExpirationDate());
				newInputDetailDTO.setAmount(1);

				String serialNumber = StringUtils.addLeadingZeros(currentSerialNumber, SERIAL_NUMBER_LENGTH);

				// String serial = String.format(SELF_SERIALIZED_SERIAL_FORMAT, drugstoreProperty.getGln(), serialNumber);
				// newInputDetailDTO.setSerialNumber(serial);

				newInputDetailDTO.setSerialNumber(drugstoreProperty.getGln() + serialNumber);

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
		input.setInputDetails(details);

		if (input.hasToInform()) {
			input.setInformAnmat(true);
		} else {
			input.setInformAnmat(false);
			for (InputDetail inputDetail : input.getInputDetails()) {
				this.updateStock(inputDetail, agreement);
			}
		}

		input.setCancelled(false);
		input.setTransactionCodeANMAT(null);
		input.setInformed(false);

		return input;
	}

	private void updateStock(InputDetail inputDetail, Agreement agreement) {
		Stock stock = new Stock();
		stock.setAgreement(agreement);
		stock.setAmount(inputDetail.getAmount());
		stock.setBatch(inputDetail.getBatch());
		stock.setExpirationDate(inputDetail.getExpirationDate());
		stock.setProduct(inputDetail.getProduct());
		stock.setSerialNumber(inputDetail.getSerialNumber());
		if (inputDetail.getGtin() != null) {
			stock.setGtin(inputDetail.getGtin());
		}
		this.stockService.addToStock(stock);
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
	public void saveAndUpdateStock(Input input) {
		for (InputDetail inputDetail : input.getInputDetails()) {
			this.updateStock(inputDetail, input.getAgreement());
		}
		this.inputDAO.save(input);
	}

	@Override
	public void save(Input input) {
		this.inputDAO.save(input);
	}

	@Override
	public boolean canCancelInput(Input input) {
		if ((input.getTransactionCodeANMAT() == null && input.isInformAnmat())) {
			return true;
		} else {
			return !this.inputDAO.exitsMovements(input);
		}
	}

	@Override
	public Input update(InputDTO inputDTO) {
		Input input = this.get(inputDTO.getId());
		input.setConcept(this.conceptService.get(inputDTO.getConceptId()));
		if (inputDTO.getProviderId() == null) {
			input.setProvider(null);
		} else {
			input.setProvider(this.providerService.get(inputDTO.getProviderId()));
		}
		if (inputDTO.getDeliveryLocationId() == null) {
			input.setDeliveryLocation(null);
		} else {
			input.setDeliveryLocation(this.deliveryLocationService.get(inputDTO.getDeliveryLocationId()));
		}
		input.setPurchaseOrderNumber(inputDTO.getPurchaseOrderNumber());
		input.setDeliveryNoteNumber(inputDTO.getDeliveryNoteNumber());
		try {
			if (input.hasToInform()) {
				input.setInformAnmat(true);
			} else {
				input.setInformAnmat(false);
				for (InputDetail inputDetail : input.getInputDetails()) {
					this.updateStock(inputDetail, input.getAgreement());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	@Override
	public void saveAndRemoveFromStock(Input input) {
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

			this.stockService.removeFromStock(stock);
		}
		this.save(input);
	}

	@Override
	public OperationResult updateInput(InputDTO inputDTO, String username) throws Exception {
		Input input = this.update(inputDTO);
		this.auditService.addAudit(username, RoleOperation.INPUT.getId(), AuditState.AUTHORITED, input.getId());
		OperationResult operationResult = null;
		if (input.isInformAnmat()) {
			operationResult = this.traceabilityService.processInputPendingTransactions(input);
		}
		if (operationResult != null) {
			// Si no hubo errores se setea el codigo de transaccion del ingreso y se ingresa la mercaderia en stock.
			if (operationResult.getResultado()) {
				input.setTransactionCodeANMAT(operationResult.getCodigoTransaccion());
				input.setInformed(true);
				this.saveAndUpdateStock(input);
			}
			operationResult.setOperationId(input.getId());
		}
		return operationResult;
	}

	@Override
	public List<Input> getInputs(boolean cancelled) {
		return this.inputDAO.getInputs(cancelled);
	}

	@Override
	public void cancelInputs(List<Integer> inputIds) {
		for (Integer inputId : inputIds) {
			boolean canCancel = true;
			boolean hasNoSerials = true;
			Input input = this.get(inputId);
			for (InputDetail inputDetail : input.getInputDetails()) {
				if (inputDetail.getSerialNumber() != null && !inputDetail.getProduct().getType().equals("SS")) {
					hasNoSerials = false;
					if (this.outputService.existSerial(inputDetail.getProduct().getId(), inputDetail.getSerialNumber())
							|| this.orderService.existSerial(inputDetail.getProduct().getId(), inputDetail.getSerialNumber())) {
						canCancel = false;
					}
				}
			}
			if (canCancel && !hasNoSerials) {
				try {
					WebServiceResult result = this.traceabilityService.cancelInputTransaction(input);
					boolean alreadyCancelled = false;
					if (result != null) {
						// Si no hubo errores se setea el codigo de transaccion del ingreso y se ingresa la mercaderia en stock.
						if (result.getErrores() != null) {
							if (result.getErrores(0).get_c_error().equals(Constants.ERROR_ANMAT_ALREADY_CANCELLED)) {
								alreadyCancelled = true;
							}
						}
						if (result.getResultado() || alreadyCancelled) {
							input.setCancelled(true);
							this.saveAndRemoveFromStock(input);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (hasNoSerials) {
				input.setCancelled(true);
				this.saveAndRemoveFromStock(input);
			}
		}
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
		return input;
	}
}
