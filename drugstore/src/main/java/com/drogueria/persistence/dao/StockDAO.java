package com.drogueria.persistence.dao;

import java.util.Date;
import java.util.List;

import com.drogueria.model.Product;
import com.drogueria.model.Stock;
import com.drogueria.query.StockQuery;

public interface StockDAO {

	void save(Stock stock);

	Stock get(Integer id);

	List<Stock> getAll();

	void delete(Stock stock);

	Stock getSerializedProductStock(Integer productId, String serialNumber, String gtin);

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
