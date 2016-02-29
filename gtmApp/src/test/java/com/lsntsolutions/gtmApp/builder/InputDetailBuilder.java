package com.lsntsolutions.gtmApp.builder;

import com.lsntsolutions.gtmApp.model.InputDetail;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.ProductGtin;

import java.util.Date;

/**
 * Created by a983060 on 29/02/2016.
 */
public class InputDetailBuilder {

    private final InputDetail inputDetail;

    public InputDetailBuilder() {
        this.inputDetail = new InputDetail();
    }

    public InputDetailBuilder id(Integer id) {
        this.inputDetail.setId(id);
        return this;
    }

    public InputDetailBuilder date(Date date) {
        this.inputDetail.setExpirationDate(date);
        return this;
    }

    public InputDetailBuilder batch(String batch) {
        this.inputDetail.setBatch(batch);
        return this;
    }

    public InputDetailBuilder serial(String serial) {
        this.inputDetail.setSerialNumber(serial);
        return this;
    }

    public InputDetailBuilder gtin(ProductGtin gtin) {
        this.inputDetail.setGtin(gtin);
        return this;
    }

    public InputDetailBuilder product(Product product) {
        this.inputDetail.setProduct(product);
        return this;
    }


    public InputDetail build() {
        return this.inputDetail;
    }
}
