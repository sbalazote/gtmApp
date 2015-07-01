package com.drogueria.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.constant.State;
import com.drogueria.dto.ProvisioningRequestDTO;
import com.drogueria.dto.ProvisioningRequestDetailDTO;
import com.drogueria.model.LogisticsOperator;
import com.drogueria.model.ProvisioningRequest;
import com.drogueria.model.ProvisioningRequestDetail;
import com.drogueria.model.ProvisioningRequestState;
import com.drogueria.persistence.dao.ProvisioningRequestDAO;
import com.drogueria.query.ProvisioningQuery;
import com.drogueria.service.AffiliateService;
import com.drogueria.service.AgreementService;
import com.drogueria.service.ClientService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.LogisticsOperatorService;
import com.drogueria.service.ProductService;
import com.drogueria.service.ProvisioningRequestService;
import com.drogueria.service.ProvisioningRequestStateService;

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

	@Override
	public void save(ProvisioningRequest provisioningRequest) {
		this.provisioningRequestDAO.save(provisioningRequest);
	}

	@Override
	public ProvisioningRequest save(ProvisioningRequestDTO provisioningRequestDTO) {
		ProvisioningRequest provisioningRequest = this.buildModel(provisioningRequestDTO);
		this.provisioningRequestDAO.save(provisioningRequest);

		logger.info("Se han guardado los cambios exitosamente. Solicitud de Abastecimiento numero: " + provisioningRequest.getId());

		return provisioningRequest;
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
			throw new RuntimeException("No se ha podido actualizar la fecha de entrega para la Solicitud de Abastecimiento " + provisioningRequest.getId(), e);
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

			logger.info("Se ha autorizado la Solicitud de Abastecimiento numero: " + id);
		}
	}

	@Override
	public void cancelProvisioningRequests(List<Integer> provisioningIds) {
		ProvisioningRequestState state = this.provisioningRequestStateService.get(State.CANCELED.getId());
		for (Integer id : provisioningIds) {
			ProvisioningRequest provisioningRequest = this.get(id);
			provisioningRequest.setState(state);
			this.save(provisioningRequest);

			logger.info("Se ha anulado la Solicitud de Abastecimiento numero: " + id);
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
