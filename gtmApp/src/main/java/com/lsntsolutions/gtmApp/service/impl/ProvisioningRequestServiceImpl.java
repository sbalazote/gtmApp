package com.lsntsolutions.gtmApp.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.dto.ProvisioningRequestDTO;
import com.lsntsolutions.gtmApp.dto.ProvisioningRequestDetailDTO;
import com.lsntsolutions.gtmApp.model.LogisticsOperator;
import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.model.ProvisioningRequestDetail;
import com.lsntsolutions.gtmApp.model.ProvisioningRequestState;
import com.lsntsolutions.gtmApp.persistence.dao.ProvisioningRequestDAO;
import com.lsntsolutions.gtmApp.util.OperationResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.query.ProvisioningQuery;

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

	@Override
	public void save(ProvisioningRequest provisioningRequest) {
		this.provisioningRequestDAO.save(provisioningRequest);
	}

	@Override
	public OperationResult save(ProvisioningRequestDTO provisioningRequestDTO) {
		ProvisioningRequest provisioningRequest = this.buildModel(provisioningRequestDTO);
		OperationResult operationResult = new OperationResult();
		List<String> productsList = this.checkAvaiableStock(provisioningRequest);
		if(productsList.isEmpty()) {
			this.provisioningRequestDAO.save(provisioningRequest);
			logger.info("Se han guardado los cambios exitosamente. Pedido numero: " + provisioningRequest.getId());
			operationResult.setOperationId(String.valueOf(provisioningRequest.getId()));
			operationResult.setResultado(true);
			return operationResult;
		}else{
			List<String> errors = new ArrayList<>();
			errors.add("No fue posible asignar el stock por que no hay suficiente disponibilidad de el/los siguiente/s producto/s: ");
			for(String product : productsList){
				errors.add(product);
			}
			operationResult.setMyOwnErrors(errors);
			operationResult.setResultado(false);
		}
		return operationResult;
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
	public void authorizeProvisioningRequests(List<Integer> provisioningIds) {
		ProvisioningRequestState state = this.provisioningRequestStateService.get(State.AUTHORIZED.getId());
		for (Integer id : provisioningIds) {
			ProvisioningRequest provisioningRequest = this.get(id);
			provisioningRequest.setState(state);
			this.save(provisioningRequest);

			logger.info("Se ha autorizado el Pedido numero: " + id);
		}
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
	public List<ProvisioningRequest> getFilterProvisionings(Integer agreementId, Integer clientId, Integer stateId) {
		return this.provisioningRequestDAO.getFilterProvisionings(agreementId, clientId, stateId);
	}

	@Override
	public void reassignOperators(ProvisioningRequest provisioningRequest, Integer operatorLogisticId) {
		LogisticsOperator logisticsOperator = this.logisticsOperatorService.get(operatorLogisticId);
		provisioningRequest.setLogisticsOperator(logisticsOperator);
		this.save(provisioningRequest);
	}

}
