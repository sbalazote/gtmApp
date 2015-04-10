package com.drogueria.service;

import java.util.List;

import com.drogueria.model.Agreement;
import com.drogueria.model.Product;
import com.drogueria.model.Stock;
import com.drogueria.query.StockQuery;

public interface StockService {

	void save(Stock stock);

	Stock get(Integer id);

	List<Stock> getAll();

	void addToStock(Stock stock);

	void removeFromStock(Stock stock);

	Long getProductAmount(Integer productId, Integer agreementId, Integer provisioningId);

	List<Stock> getBatchExpirationDateStock(Integer productId, Integer agreementId);

	Stock getSerializedStock(Integer productId, String serialNumber, String gtin);

	Stock getSerializedProductStock(String serialNumber, Integer agreementId);

	boolean getCountStockSearch(StockQuery stockQuery);

	List<Stock> getStockForSearch(StockQuery stockQuery);

	List<Product> getForAutocomplete(String term, Integer agreementId);

	Product getByGtin(String gtin, Integer agreementId);

	void updateAgreementStock(Integer id, String serialNumber, String number, Agreement destinationAgreement);

	boolean delete(Integer stockId);

}
