package com.drogueria.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.dto.SupplyingDTO;
import com.drogueria.dto.SupplyingDetailDTO;
import com.drogueria.model.Agreement;
import com.drogueria.model.Product;
import com.drogueria.model.ProductGtin;
import com.drogueria.model.Stock;
import com.drogueria.model.Supplying;
import com.drogueria.model.SupplyingDetail;
import com.drogueria.persistence.dao.SupplyingDAO;
import com.drogueria.service.AffiliateService;
import com.drogueria.service.AgreementService;
import com.drogueria.service.ClientService;
import com.drogueria.service.ProductGtinService;
import com.drogueria.service.ProductService;
import com.drogueria.service.StockService;
import com.drogueria.service.SupplyingService;

@Service
@Transactional
public class SupplyingServiceImpl implements SupplyingService {
	private static final Logger logger = Logger.getLogger(SupplyingServiceImpl.class);

	@Autowired
	private SupplyingDAO supplyingDAO;
	@Autowired
	private ClientService clientService;
	@Autowired
	private AffiliateService affiliateService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private ProductService productService;
	@Autowired
	private StockService stockService;
	@Autowired
	private ProductGtinService productGtinService;

	@Override
	public Supplying save(SupplyingDTO supplyingDTO) {
		Supplying supplying = this.buildSupplying(supplyingDTO);
		this.supplyingDAO.save(supplying);
		logger.info("Se ha generado exitosamente la dispensa numero: " + supplying.getId());

		return supplying;
	}

	private Supplying buildSupplying(SupplyingDTO supplyingDTO) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat expirationDateFormatter = null;

		try {
			Agreement agreement = this.agreementService.get(supplyingDTO.getAgreementId());

			Supplying supplying = new Supplying();
			supplying.setAgreement(agreement);

			supplying.setClient(this.clientService.get(supplyingDTO.getClientId()));
			supplying.setAffiliate(this.affiliateService.get(supplyingDTO.getAffiliateId()));
			supplying.setDate(dateFormatter.parse(supplyingDTO.getDate()));

			List<SupplyingDetail> details = new ArrayList<>();
			Product product = null;
			for (SupplyingDetailDTO supplyingDetailDTO : supplyingDTO.getSupplyingDetails()) {
				if (product == null || !product.getId().equals(supplyingDetailDTO.getProductId())) {
					product = this.productService.get(supplyingDetailDTO.getProductId());
				}

				SupplyingDetail supplyingDetail = new SupplyingDetail();
				supplyingDetail.setAmount(supplyingDetailDTO.getAmount());
				supplyingDetail.setBatch(supplyingDetailDTO.getBatch());
				if (supplyingDetailDTO.getExpirationDate() != null && !supplyingDetailDTO.getExpirationDate().isEmpty()) {
					if (supplyingDetailDTO.getInStock()) {
						expirationDateFormatter = new SimpleDateFormat("dd/MM/yy");
						supplyingDetail.setExpirationDate(expirationDateFormatter.parse(supplyingDetailDTO.getExpirationDate()));
					} else {
						expirationDateFormatter = new SimpleDateFormat("ddMMyy");
						supplyingDetail.setExpirationDate(expirationDateFormatter.parse(supplyingDetailDTO.getExpirationDate()));
					}
				}
				supplyingDetail.setSerialNumber(supplyingDetailDTO.getSerialNumber());

				if (supplyingDetailDTO.getGtin() != null) {

					ProductGtin productGtin = this.productGtinService.getByNumber(supplyingDetailDTO.getGtin());
					supplyingDetail.setGtin(productGtin);
				} else {
					if (product.getLastProductGtin() != null) {
						supplyingDetail.setGtin(product.getLastProductGtin());
					}
				}

				supplyingDetail.setProduct(product);
				supplyingDetail.setInStock(supplyingDetailDTO.getInStock());
				if (supplyingDetail.getInStock()) {
					this.updateStock(supplyingDetail, agreement);
				}
				details.add(supplyingDetail);
			}
			supplying.setSupplyingDetails(details);

			return supplying;

		} catch (Exception e) {
			throw new RuntimeException("No se ha podido mapear el SupplyingDTO", e);
		}
	}

	private void updateStock(SupplyingDetail supplyingDetail, Agreement agreement) {
		Stock stock = new Stock();
		stock.setAgreement(agreement);
		stock.setAmount(supplyingDetail.getAmount());
		stock.setBatch(supplyingDetail.getBatch());
		stock.setExpirationDate(supplyingDetail.getExpirationDate());
		stock.setProduct(supplyingDetail.getProduct());
		stock.setSerialNumber(supplyingDetail.getSerialNumber());
		if (supplyingDetail.getGtin() != null) {
			stock.setGtin(supplyingDetail.getGtin());
		}

		this.stockService.removeFromStock(stock);
	}

	@Override
	public Supplying get(Integer id) {
		return this.supplyingDAO.get(id);
	}

	@Override
	public List<Supplying> getAll() {
		return this.supplyingDAO.getAll();
	}

	@Override
	public void cancel(Supplying supplying) {
		supplying.setCancelled(true);
		try {
			this.addSupplyingToStock(supplying);
		} catch (Exception e) {
			logger.info("No se ha reingresar el stock");
		}
		try {
			this.save(supplying);
		} catch (Exception e) {
			logger.info("No se ha podido guardar los cambios para el egreso " + supplying.getId());
		}
	}

	private void addToStock(SupplyingDetail supplyingDetail, Agreement agreement) {
		Stock stock = new Stock();
		stock.setAgreement(agreement);
		stock.setAmount(supplyingDetail.getAmount());
		stock.setBatch(supplyingDetail.getBatch());
		stock.setExpirationDate(supplyingDetail.getExpirationDate());
		stock.setProduct(supplyingDetail.getProduct());
		stock.setSerialNumber(supplyingDetail.getSerialNumber());

		if (supplyingDetail.getGtin() != null) {
			stock.setGtin(supplyingDetail.getGtin());
		}

		this.stockService.addToStock(stock);
	}

	@Override
	public List<Supplying> getCancelleables() {
		return this.supplyingDAO.getCancelleables();
	}

	@Override
	public void addSupplyingToStock(Supplying supplying) {
		for (SupplyingDetail supplyingDetail : supplying.getSupplyingDetails()) {
			this.addToStock(supplyingDetail, supplying.getAgreement());
		}
	}

	@Override
	public void save(Supplying supplying) {
		this.supplyingDAO.save(supplying);
	}

}
