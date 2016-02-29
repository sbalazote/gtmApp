package com.lsntsolutions.gtmApp.builder;

import com.lsntsolutions.gtmApp.model.Product;

/**
 * Created by a983060 on 29/02/2016.
 */
public class ProductBuilder {
    private final Product event;

    public ProductBuilder() {
        this.event = new Product();
    }

    public ProductBuilder code(Integer code){
        this.event.setCode(code);
        return this;
    }

    public ProductBuilder informAnmat(boolean informAnmat){
        this.event.setInformAnmat(informAnmat);
        return this;
    }

    public ProductBuilder type(String type){
        this.event.setType(type);
        return this;
    }

    public Product build() {
        return this.event;
    }
}
