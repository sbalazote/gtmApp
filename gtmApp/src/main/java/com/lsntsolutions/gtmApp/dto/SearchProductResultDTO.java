package com.lsntsolutions.gtmApp.dto;

/**
 * Created by Sebastian on 10/03/2016.
 */

import java.util.List;

public class SearchProductResultDTO {
    private List<SearchProductDTO> inputs;
    private List<SearchProductDTO> outputs;
    private List<SearchProductDTO> orders;
    private List<SearchProductDTO> deliveryNotes;
    private List<SearchProductDTO> supplyings;

    public List<SearchProductDTO> getInputs() {
        return this.inputs;
    }

    public void setInputs(List<SearchProductDTO> inputs) {
        this.inputs = inputs;
    }

    public List<SearchProductDTO> getOutputs() {
        return this.outputs;
    }

    public void setOutputs(List<SearchProductDTO> outputs) {
        this.outputs = outputs;
    }

    public List<SearchProductDTO> getOrders() {
        return this.orders;
    }

    public void setOrders(List<SearchProductDTO> orders) {
        this.orders = orders;
    }

    public List<SearchProductDTO> getDeliveryNotes() {
        return this.deliveryNotes;
    }

    public void setDeliveryNotes(List<SearchProductDTO> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public List<SearchProductDTO> getSupplyings() {
        return this.supplyings;
    }

    public void setSupplyings(List<SearchProductDTO> supplyings) {
        this.supplyings = supplyings;
    }
}