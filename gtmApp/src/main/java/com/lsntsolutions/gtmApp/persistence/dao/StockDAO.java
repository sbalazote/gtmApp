package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.Date;
import java.util.List;

import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.Stock;
import com.lsntsolutions.gtmApp.query.StockQuery;

public interface StockDAO {

	void save(Stock stock);

	Stock get(Integer id);

	List<Stock> getAll();

	void delete(Stock stock);

	Stock getSerializedProductStock(Integer productId, String serialNumber, String gtin, Integer agreementId);

	Stock getSerializedProductStock(String serialNumber, Integer agreementId);

	Stock getBatchExpirationDateStockForUpdate(Integer productId, String batch, Date expirationDate, Integer agreementId);

	List<Stock> getBatchExpirationDateStock(Integer productId, Integer agreementId);

	Long getProductAmount(Integer productId, Integer agreementId, Integer provisioningId);

	boolean getCountStockSearch(StockQuery stockQuery);

	List<Stock> getStockForSearch(StockQuery stockQuery);

	List<Product> getForAutocomplete(String term, Integer agreementId);

	Product getByGtin(String gtin, Integer agreementId);

	boolean delete(Integer stockId);
}
