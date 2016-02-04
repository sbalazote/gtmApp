package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.dto.ProvisioningRequestDTO;
import com.lsntsolutions.gtmApp.dto.ProvisioningRequestDetailDTO;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.persistence.dao.ProvisioningRequestDAO;
import com.lsntsolutions.gtmApp.query.ProvisioningQuery;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.OperationResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class ProvisioningRequestServiceImpl implements ProvisioningRequestService {

	private static final Logger logger = Logger.getLogger(ProvisioningRequestServiceImpl.class);

	@Autowired
	private ProvisioningRequestDAO provisioningRequestDAO;
	@Autowired
	private AffiliateService affiliateService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private LogisticsOperatorService logisticsOperatorService;
	@Autowired
	private ProvisioningRequestStateService provisioningRequestStateService;
	@Autowired
	private ProductService productService;
	@Autowired
	private StockService stockService;
	@Autowired
	private AuditService auditService;

	@Override
	public void save(ProvisioningRequest provisioningRequest) {
		this.provisioningRequestDAO.save(provisioningRequest);
	}

	@Override
	public OperationResult save(ProvisioningRequestDTO provisioningRequestDTO) {
		ProvisioningRequest provisioningRequest = this.buildModel(provisioningRequestDTO);
		OperationResult operationResult = new OperationResult();
		List<String> productsList = this.checkAvaiableStock(provisioningRequest);
		List<String> errors = new ArrayList<>();
		if(productsList.isEmpty()) {
			List<String> repeatProducts = checkRepeatProducts(provisioningRequest);
			if(repeatProducts.size() > 0){
				errors.add("El/Los siguientes productos se encuentran duplicados: ");
				for(String product : productsList){
					errors.add(product);
				}
				operationResult.setMyOwnErrors(errors);
				operationResult.setResultado(false);
			}else {
				this.provisioningRequestDAO.save(provisioningRequest);
				logger.info("Se han guardado los cambios exitosamente. Pedido numero: " + provisioningRequest.getId());
				operationResult.setOperationId(String.valueOf(provisioningRequest.getId()));
				operationResult.setResultado(true);
				return operationResult;
			}
		}else{
			errors.add("No fue posible asignar el stock por que no hay suficiente disponibilidad de el/los siguiente/s producto/s: ");
			for(String product : productsList){
				errors.add(product);
			}
			operationResult.setMyOwnErrors(errors);
			operationResult.setResultado(false);
		}
		return operationResult;
	}

	private List<String> checkRepeatProducts(ProvisioningRequest provisioningRequest) {
		HashMap<Integer,Product> products = new HashMap<>();
		List<String> repeatProducts = new ArrayList<>();
		for(ProvisioningRequestDetail provisioningRequestDetail : provisioningRequest.getProvisioningRequestDetails()){
			Product product = products.get(provisioningRequestDetail.getProduct().getId());
			if(product == null){
				products.put(provisioningRequestDetail.getProduct().getId(),provisioningRequestDetail.getProduct());
			}else{
				repeatProducts.add(product.toString());
			}
		}
		return repeatProducts;
	}

	private List<String> checkAvaiableStock(ProvisioningRequest provisioningRequest) {
		List<String> productsList = new ArrayList<>();
		for(ProvisioningRequestDetail provisioningRequestDetail : provisioningRequest.getProvisioningRequestDetails()){
			Long amount = this.stockService.getProductAmount(provisioningRequestDetail.getProduct().getId(),provisioningRequest.getAgreement().getId(),provisioningRequest.getId());
			if(amount.compareTo(Long.valueOf(provisioningRequestDetail.getAmount())) < 0){
				productsList.add(provisioningRequestDetail.getProduct().toString());
			}
		}
		return productsList;
	}

	@Override
	public ProvisioningRequest get(Integer id) {
		return this.provisioningRequestDAO.get(id);
	}

	@Override
	public List<ProvisioningRequest> getAll() {
		return this.provisioningRequestDAO.getAll();
	}

	@Override
	public List<ProvisioningRequest> getAllByState(Integer stateId) {
		return this.provisioningRequestDAO.getAllByState(stateId);
	}

	@Override
	public List<ProvisioningRequest> getProvisioningForSearch(ProvisioningQuery provisioningQuery) {
		return this.provisioningRequestDAO.getProvisioningForSearch(provisioningQuery);
	}

	@Override
	public boolean getCountOfProvisioningSearch(ProvisioningQuery provisioningQuery) {
		return this.provisioningRequestDAO.getCountOfProvisioningSearch(provisioningQuery);
	}

	private ProvisioningRequest buildModel(ProvisioningRequestDTO provisioningRequestDTO) {
		SimpleDateFormat deliveryDateFormatter = new SimpleDateFormat("dd/MM/yyyy");

		ProvisioningRequest provisioningRequest;

		if (provisioningRequestDTO.getId() == null) {
			provisioningRequest = new ProvisioningRequest();
		} else {
			provisioningRequest = this.get(provisioningRequestDTO.getId());
		}

		provisioningRequest.setAffiliate(this.affiliateService.get(provisioningRequestDTO.getAffiliateId()));
		provisioningRequest.setAgreement(this.agreementService.get(provisioningRequestDTO.getAgreementId()));
		provisioningRequest.setClient(this.clientService.get(provisioningRequestDTO.getClientId()));
		provisioningRequest.setComment(provisioningRequestDTO.getComment());
		provisioningRequest.setDeliveryLocation(this.deliveryLocationService.get(provisioningRequestDTO.getDeliveryLocationId()));

		try {
			provisioningRequest.setDeliveryDate(deliveryDateFormatter.parse(provisioningRequestDTO.getDeliveryDate()));
		} catch (Exception e) {
			throw new RuntimeException("No se ha podido actualizar la fecha de entrega para el Pedido " + provisioningRequest.getId(), e);
		}

		Integer logisticsOperatorId = provisioningRequestDTO.getLogisticsOperatorId();
		if (logisticsOperatorId != null) {
			provisioningRequest.setLogisticsOperator(this.logisticsOperatorService.get(logisticsOperatorId));
		} else {
			provisioningRequest.setLogisticsOperator(null);
		}
		provisioningRequest.setState(this.provisioningRequestStateService.get(State.ENTERED.getId()));

		List<ProvisioningRequestDetail> newDetails = new ArrayList<ProvisioningRequestDetail>();
		for (ProvisioningRequestDetailDTO product : provisioningRequestDTO.getProducts()) {
			ProvisioningRequestDetail provisioningRequestDetail = new ProvisioningRequestDetail();
			provisioningRequestDetail.setAmount(product.getAmount());
			provisioningRequestDetail.setProduct(this.productService.get(product.getProductId()));
			newDetails.add(provisioningRequestDetail);
		}

		if (provisioningRequest.getId() != null) {

			// Modifico los existentes y/o agrego los nuevos
			for (ProvisioningRequestDetail newDetail : newDetails) {
				if (provisioningRequest.getProvisioningRequestDetails().contains(newDetail)) {
					Integer index = provisioningRequest.getProvisioningRequestDetails().indexOf(newDetail);
					ProvisioningRequestDetail oldDetail = provisioningRequest.getProvisioningRequestDetails().get(index);
					oldDetail.setAmount(newDetail.getAmount());
				} else {
					provisioningRequest.getProvisioningRequestDetails().add(newDetail);
				}
			}

			// Borro los que no estan mas
			for (Iterator<ProvisioningRequestDetail> it = provisioningRequest.getProvisioningRequestDetails().iterator(); it.hasNext();) {
				ProvisioningRequestDetail oldDetail = it.next();
				if (!newDetails.contains(oldDetail)) {
					it.remove();
				}
			}

		} else {
			provisioningRequest.setProvisioningRequestDetails(newDetails);
		}

		return provisioningRequest;
	}

	@Override
	public List<PrinterResultDTO> authorizeProvisioningRequests(List<Integer> provisioningIds, String username) {
		ProvisioningRequestState state = this.provisioningRequestStateService.get(State.AUTHORIZED.getId());
		List<PrinterResultDTO> printerResultDTOs = new ArrayList<PrinterResultDTO>();
		for (Integer id : provisioningIds) {
			ProvisioningRequest provisioningRequest = this.get(id);
			PrinterResultDTO printerResultDTO = new PrinterResultDTO(provisioningRequest.getFormatId());
			if (provisioningRequest.getState().getId() < State.ASSEMBLED.getId()) {
				provisioningRequest.setState(state);
				this.save(provisioningRequest);
				this.auditService.addAudit(username, RoleOperation.PROVISIONING_REQUEST_AUTHORIZATION.getId(), AuditState.AUTHORITED, id);
				List<String> success = new ArrayList<>();
				success.add("Se ha autorizado el Pedido Nro.: " + provisioningRequest.getFormatId());
				logger.info("Se ha autorizado el Pedido Nro.: " + provisioningRequest.getFormatId());
				printerResultDTO.setSuccessMessages(success);
			} else {
				List<String> errors = new ArrayList<>();
				errors.add("No se puede autorizar la Solicitud Nro: " + provisioningRequest.getFormatId() + " ya que se encuentra en estado: " + provisioningRequest.getState().getDescription());
				logger.error("No se puede autorizar el Pedido Nro.: " + provisioningRequest.getFormatId() + " ya que se encuentra en estado: " + provisioningRequest.getState().getDescription());
				printerResultDTO.setErrorMessages(errors);
			}
            printerResultDTOs.add(printerResultDTO);
		}
		return printerResultDTOs;
	}

	@Override
	public boolean cancelProvisioningRequest(Integer provisioningRequestId) {
		ProvisioningRequestState state = this.provisioningRequestStateService.get(State.CANCELED.getId());
		ProvisioningRequest provisioningRequest = this.get(provisioningRequestId);
		if(provisioningRequest.canCancel()) {
			provisioningRequest.setState(state);
			this.save(provisioningRequest);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<ProvisioningRequest> getFilterProvisionings(Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId, Integer stateId) {
		return this.provisioningRequestDAO.getFilterProvisionings(provisioningRequestId, agreementId, logisticsOperatorId, clientId, deliveryLocationId, stateId);
	}

	@Override
	public void reassignOperators(List<Integer> provisioningsIdsToReassign, Integer operatorLogisticId) {
		LogisticsOperator logisticsOperator = this.logisticsOperatorService.get(operatorLogisticId);
		for (Integer provisioningRequestId : provisioningsIdsToReassign) {
			ProvisioningRequest provisioningRequest = this.get(provisioningRequestId);
			provisioningRequest.setLogisticsOperator(logisticsOperator);
			this.save(provisioningRequest);
		}
	}

	@Override
	public List<Agreement> getProvisioningsAgreement(boolean provisioningRequireAuthorization) {
		return this.provisioningRequestDAO.getProvisioningsAgreement(provisioningRequireAuthorization);
	}

	@Override
	public List<DeliveryLocation> getProvisioningsDeliveryLocations(boolean provisioningRequireAuthorization) {
		return this.provisioningRequestDAO.getProvisioningsDeliveryLocations(provisioningRequireAuthorization);
	}

	@Override
	public List<LogisticsOperator> getProvisioningsLogisticsOperators(boolean provisioningRequireAuthorization) {
		return this.provisioningRequestDAO.getProvisioningsLogisticsOperators(provisioningRequireAuthorization);
	}

	@Override
	public List<Client> getProvisioningsClient(boolean provisioningRequireAuthorization) {
		return this.provisioningRequestDAO.getProvisioningsClient(provisioningRequireAuthorization);
	}
}