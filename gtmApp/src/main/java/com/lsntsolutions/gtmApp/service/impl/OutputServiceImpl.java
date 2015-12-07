package com.lsntsolutions.gtmApp.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.lsntsolutions.gtmApp.dto.OutputDTO;
import com.lsntsolutions.gtmApp.model.Agreement;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.Stock;
import com.lsntsolutions.gtmApp.persistence.dao.OutputDAO;
import com.lsntsolutions.gtmApp.query.OutputQuery;
import com.lsntsolutions.gtmApp.model.Output;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.dto.OutputDetailDTO;
import com.lsntsolutions.gtmApp.model.OutputDetail;
import com.lsntsolutions.gtmApp.model.ProductGtin;
import com.lsntsolutions.gtmApp.service.AgreementService;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.DeliveryLocationService;
import com.lsntsolutions.gtmApp.service.OutputService;
import com.lsntsolutions.gtmApp.service.ProductGtinService;
import com.lsntsolutions.gtmApp.service.ProductService;
import com.lsntsolutions.gtmApp.service.ProviderService;
import com.lsntsolutions.gtmApp.service.StockService;

@Service
@Transactional
public class OutputServiceImpl implements OutputService {
	private static final Logger logger = Logger.getLogger(OutputServiceImpl.class);

	@Autowired
	private OutputDAO outputDAO;
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
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private ProductGtinService productGtinService;

	@Override
	public Output save(OutputDTO outputDTO) {
		Output output = this.buildInput(outputDTO);
		this.outputDAO.save(output);
		logger.info("Se ha generado exitosamente el Egreso de Mercader�a n�mero: " + output.getId());

		return output;
	}

	private Output buildInput(OutputDTO outputDTO) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat expirationDateFormatter = new SimpleDateFormat("dd/MM/yy");

		try {
			Agreement agreement = this.agreementService.get(outputDTO.getAgreementId());

			Output output = new Output();
			output.setAgreement(agreement);

			output.setConcept(this.conceptService.get(outputDTO.getConceptId()));
			if (outputDTO.getProviderId() != null) {
				output.setProvider(this.providerService.get(outputDTO.getProviderId()));
			}
			if (outputDTO.getDeliveryLocationId() != null) {
				output.setDeliveryLocation(this.deliveryLocationService.get(outputDTO.getDeliveryLocationId()));
			}
			output.setDate(dateFormatter.parse(outputDTO.getDate()));

			List<OutputDetail> details = new ArrayList<>();
			Product product = null;
			for (OutputDetailDTO outputDetailDTO : outputDTO.getOutputDetails()) {
				if (product == null || !product.getId().equals(outputDetailDTO.getProductId())) {
					product = this.productService.get(outputDetailDTO.getProductId());
				}

				OutputDetail outputDetail = new OutputDetail();
				outputDetail.setAmount(outputDetailDTO.getAmount());
				outputDetail.setBatch(outputDetailDTO.getBatch());
				if (outputDetailDTO.getExpirationDate() != null && !outputDetailDTO.getExpirationDate().isEmpty()) {
					outputDetail.setExpirationDate(expirationDateFormatter.parse(outputDetailDTO.getExpirationDate()));
				}
				outputDetail.setSerialNumber(outputDetailDTO.getSerialNumber());

				if (outputDetailDTO.getGtin() != null) {

					ProductGtin productGtin = this.productGtinService.getByNumber(outputDetailDTO.getGtin());
					outputDetail.setGtin(productGtin);
				} else {
					if (product.getLastProductGtin() != null) {
						outputDetail.setGtin(product.getLastProductGtin());
					}
				}

				outputDetail.setProduct(product);
				this.updateStock(outputDetail, agreement);
				details.add(outputDetail);
			}
			output.setOutputDetails(details);

			return output;

		} catch (Exception e) {
			throw new RuntimeException("No se ha podido mapear el OutputDTO", e);
		}
	}

	private void updateStock(OutputDetail outputDetail, Agreement agreement) {
		Stock stock = new Stock();
		stock.setAgreement(agreement);
		stock.setAmount(outputDetail.getAmount());
		stock.setBatch(outputDetail.getBatch());
		stock.setExpirationDate(outputDetail.getExpirationDate());
		stock.setProduct(outputDetail.getProduct());
		stock.setSerialNumber(outputDetail.getSerialNumber());
		if (outputDetail.getGtin() != null) {
			stock.setGtin(outputDetail.getGtin());
		}

		this.stockService.removeFromStock(stock);
	}

	@Override
	public Output get(Integer id) {
		return this.outputDAO.get(id);
	}

	@Override
	public List<Output> getAll() {
		return this.outputDAO.getAll();
	}

	@Override
	public List<Output> getOutputForSearch(OutputQuery outputQuery) {
		return this.outputDAO.getOutputForSearch(outputQuery);
	}

	@Override
	public boolean getCountOutputSearch(OutputQuery outputQuery) {
		return this.outputDAO.getCountOutputSearch(outputQuery);
	}

	@Override
	public void cancel(Output output) {
		output.setCancelled(true);
		try {
			this.addOutputToStock(output);
		} catch (Exception e) {
			logger.info("No se ha reingresar el stock");
		}
		try {
			this.save(output);
		} catch (Exception e) {
			logger.info("No se ha podido guardar los cambios para el egreso " + output.getId());
		}
	}

	@Override
	public List<Output> getCancelleables() {
		return this.outputDAO.getCancelleables();
	}


	@Override
	public void addOutputToStock(Output output) {
		for (OutputDetail outputDetail : output.getOutputDetails()) {
			this.stockService.updateStock(outputDetail, output.getAgreement());
		}
	}

	@Override
	public void save(Output output) {
		this.outputDAO.save(output);
	}

	@Override
	public boolean isConceptInUse(Integer conceptId){
		return this.outputDAO.isConceptInUse(conceptId);
	}
}
