package com.lsntsolutions.gtmApp.builder;

import com.lsntsolutions.gtmApp.model.ProductGtin;

/**
 * Created by a983060 on 29/02/2016.
 */
public class ProductGtinBuilder {
    private final ProductGtin productGtin;

    public ProductGtinBuilder() {
        this.productGtin = new ProductGtin();
    }


    public ProductGtinBuilder number(String number){
        this.productGtin.setNumber(number);
        return this;
    }

    public ProductGtin build() {
        return this.productGtin;
    }
}
