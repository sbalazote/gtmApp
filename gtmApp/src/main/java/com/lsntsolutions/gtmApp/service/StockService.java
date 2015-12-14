package com.lsntsolutions.gtmApp.service;

import java.util.Date;
import java.util.List;

import com.lsntsolutions.gtmApp.model.Agreement;
import com.lsntsolutions.gtmApp.model.Detail;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.Stock;
import com.lsntsolutions.gtmApp.query.StockQuery;

public interface StockService {

	void save(Stock stock);

	Stock get(Integer id);

	List<Stock> getAll();

	void addToStock(Stock stock);

	void updateStock(Detail detail, Agreement agreement);

	void removeFromStock(Stock stock);

	void removeFromStock(Detail detail, Agreement agreement);

	Long getProductAmount(Integer productId, Integer agreementId, Integer provisioningId);

	List<Stock> getBatchExpirationDateStock(Integer productId, Integer agreementId);

	Stock getSerializedStock(Integer productId, String serialNumber, String gtin, Integer agreementId);

	Stock getSerializedProductStock(String serialNumber, Integer agreementId);

	boolean getCountStockSearch(StockQuery stockQuery);

	List<Stock> getStockForSearch(StockQuery stockQuery);

	List<Product> getForAutocomplete(String term, Integer agreementId);

	Product getByGtin(String gtin, Integer agreementId);

	void updateAgreementStock(Integer id, String serialNumber, String number, Agreement originAgreement, Agreement destinationAgreement);

	boolean delete(Integer stockId);

	boolean hasStock(Integer productId, String batch, Date expirationDate, Integer agreementId, Integer amount);

	boolean existsSerial(Integer productId, String gtin, Integer agreementId, String serial);

	void removeFromStock(List<Stock> stocks);
}
