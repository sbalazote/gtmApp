package com.lsntsolutions.gtmApp.service.impl;

import java.util.Date;
import java.util.List;

import com.lsntsolutions.gtmApp.model.Agreement;
import com.lsntsolutions.gtmApp.persistence.dao.StockDAO;
import com.lsntsolutions.gtmApp.service.StockService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.Stock;
import com.lsntsolutions.gtmApp.query.StockQuery;

@Service
@Transactional
public class StockServiceImpl implements StockService {

	private static final Logger logger = Logger.getLogger(StockServiceImpl.class);

	@Autowired
	private StockDAO stockDAO;

	@Override
	public void save(Stock stock) {
		this.stockDAO.save(stock);
	}

	@Override
	public Stock get(Integer id) {
		return this.stockDAO.get(id);
	}

	@Override
	public List<Stock> getAll() {
		return this.stockDAO.getAll();
	}

	@Override
	public void addToStock(Stock stock) {
		if (stock.getSerialNumber() != null) {
			Stock serializedProductStock;
			if (stock.getGtin() != null) {
				serializedProductStock = this.stockDAO.getSerializedProductStock(stock.getProduct().getId(), stock.getSerialNumber(), stock.getGtin()
						.getNumber(), stock.getAgreement().getId());
			} else {
				serializedProductStock = this.stockDAO.getSerializedProductStock(stock.getProduct().getId(), stock.getSerialNumber(), null, stock
						.getAgreement().getId());
			}
			if (serializedProductStock != null) {
				throw new RuntimeException("Ya existe un registro de stock para el serie '" + stock.getSerialNumber() + "' del producto '"
						+ stock.getProduct().getCode() + "'");
			}
		} else {
			Stock batchExpirationDateStock = this.stockDAO.getBatchExpirationDateStockForUpdate(stock.getProduct().getId(), stock.getBatch(),
					stock.getExpirationDate(), stock.getAgreement().getId());
			if (batchExpirationDateStock != null) {
				Integer amount = batchExpirationDateStock.getAmount();
				amount += stock.getAmount();
				batchExpirationDateStock.setAmount(amount);
				this.save(batchExpirationDateStock);
				return;
			}
		}
		this.save(stock);
	}

	@Override
	public void removeFromStock(Stock stock) {
		if (stock.getSerialNumber() != null) {
			this.removeFromSerializedStock(stock);
		} else {
			this.removeFromBatchExpirationDateStock(stock);
		}
	}

	@Override
	public Long getProductAmount(Integer productId, Integer agreementId, Integer provisioningId) {
		return this.stockDAO.getProductAmount(productId, agreementId, provisioningId);
	}

	@Override
	public List<Stock> getBatchExpirationDateStock(Integer productId, Integer agreementId) {
		return this.stockDAO.getBatchExpirationDateStock(productId, agreementId);
	}

	@Override
	public Stock getSerializedStock(Integer productId, String serialNumber, String gtin, Integer agreementId) {
		return this.stockDAO.getSerializedProductStock(productId, serialNumber, gtin, agreementId);
	}

	@Override
	public Stock getSerializedProductStock(String serialNumber, Integer agreementId) {
		return this.stockDAO.getSerializedProductStock(serialNumber, agreementId);
	}

	private void removeFromSerializedStock(Stock stock) {
		String gtin = null;
		if (stock.getGtin() != null) {
			gtin = stock.getGtin().getNumber();
		}
		logger.info("Antes de obtener el stock");
		Stock serializedProductStock = this.stockDAO.getSerializedProductStock(stock.getProduct().getId(), stock.getSerialNumber(), gtin, stock.getAgreement()
				.getId());
		logger.info("Despues de obtener el stock" + stock.toString());
		if (serializedProductStock != null) {
			this.stockDAO.delete(serializedProductStock);
		} else {
			throw new RuntimeException("No existe un registro de stock para el serie '" + stock.getSerialNumber() + "' del producto con id '"
					+ stock.getProduct().getId() + "'");
		}
	}

	private void removeFromBatchExpirationDateStock(Stock stock) {
		Stock batchExpirationDateStock = this.stockDAO.getBatchExpirationDateStockForUpdate(stock.getProduct().getId(), stock.getBatch(),
				stock.getExpirationDate(), stock.getAgreement().getId());
		if (batchExpirationDateStock != null) {
			Integer amount = batchExpirationDateStock.getAmount();

			if (amount == stock.getAmount()) {
				this.stockDAO.delete(batchExpirationDateStock);
				return;
			} else if (amount < 0) {
				throw new RuntimeException("Stock insuficiente del producto con lote '" + stock.getBatch() + "', vencimiento '" + stock.getExpirationDate()
						+ "' y id '" + stock.getProduct().getId() + "'. Stock disponible: " + batchExpirationDateStock.getAmount() + " - Stock solicitado: "
						+ stock.getAmount());
			}
			amount -= stock.getAmount();

			batchExpirationDateStock.setAmount(amount);
			this.stockDAO.save(batchExpirationDateStock);

		} else {
			throw new RuntimeException("No existe un registro de stock para el producto con lote '" + stock.getBatch() + "', vencimiento '"
					+ stock.getExpirationDate() + "', id '" + stock.getProduct().getId() + "' y convenio '" + stock.getAgreement().getId() + "'");
		}
	}

	@Override
	public boolean getCountStockSearch(StockQuery stockQuery) {
		return this.stockDAO.getCountStockSearch(stockQuery);
	}

	@Override
	public List<Stock> getStockForSearch(StockQuery stockQuery) {
		return this.stockDAO.getStockForSearch(stockQuery);
	}

	@Override
	public List<Product> getForAutocomplete(String term, Integer agreementId) {
		return this.stockDAO.getForAutocomplete(term, agreementId);
	}

	@Override
	public Product getByGtin(String gtin, Integer agreementId) {
		return this.stockDAO.getByGtin(gtin, agreementId);
	}

	@Override
	public void updateAgreementStock(Integer productId, String serialNumber, String gtin, Agreement originAgreement, Agreement destinationAgreement) {
		Stock stock = this.getSerializedStock(productId, serialNumber, gtin, originAgreement.getId());
		stock.setAgreement(destinationAgreement);
		this.save(stock);
	}

	@Override
	public boolean delete(Integer stockId) {
		return this.stockDAO.delete(stockId);
	}

	@Override
	public boolean hasStock(Integer productId, String batch, Date expirationDate, Integer agreementId, Integer amount) {
		Stock stock = this.stockDAO.getBatchExpirationDateStockForUpdate(productId, batch, expirationDate, agreementId);
		return stock.getAmount() >= amount;
	}
}
