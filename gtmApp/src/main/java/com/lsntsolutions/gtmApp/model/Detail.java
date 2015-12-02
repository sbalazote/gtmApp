package com.lsntsolutions.gtmApp.model;

import java.util.Date;

/**
 * Created by a983060 on 02/12/2015.
 */
public interface Detail {

    Integer getId();

    void setId(Integer id);

    Product getProduct();

    void setProduct(Product product);

    String getSerialNumber();

    void setSerialNumber(String serialNumber);

    ProductGtin getGtin();

    void setGtin(ProductGtin gtin) ;

    String getBatch();

    void setBatch(String batch);

    Date getExpirationDate();

    void setExpirationDate(Date expirationDate);

    Integer getAmount();

    void setAmount(Integer amount);
}
