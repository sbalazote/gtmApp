package com.lsntsolutions.gtmApp.dto;

import com.lsntsolutions.gtmApp.model.Audit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 05/04/2016.
 */
public class SearchAuditResultDTO {

    private List<Audit> inputs = new ArrayList<>();
    private List<Audit> outputs = new ArrayList<>();
    private List<Audit> provisioningRequests = new ArrayList<>();
    private List<Audit> orders = new ArrayList<>();
    private List<Audit> deliveryNotes = new ArrayList<>();
    private List<Audit> supplyings = new ArrayList<>();

    public List<Audit> getInputs() {
        return this.inputs;
    }

    public void setInputs(List<Audit> inputs) {
        this.inputs = inputs;
    }

    public List<Audit> getOutputs() {
        return this.outputs;
    }

    public void setOutputs(List<Audit> outputs) {
        this.outputs = outputs;
    }

    public List<Audit> getProvisioningRequests() {
        return provisioningRequests;
    }

    public void setProvisioningRequests(List<Audit> provisioningRequests) {
        this.provisioningRequests = provisioningRequests;
    }

    public List<Audit> getOrders() {
        return this.orders;
    }

    public void setOrders(List<Audit> orders) {
        this.orders = orders;
    }

    public List<Audit> getDeliveryNotes() {
        return this.deliveryNotes;
    }

    public void setDeliveryNotes(List<Audit> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public List<Audit> getSupplyings() {
        return this.supplyings;
    }

    public void setSupplyings(List<Audit> supplyings) {
        this.supplyings = supplyings;
    }

    public void addInputAudit(Audit audit) {
        this.inputs.add(audit);
    }

    public void addOutputAudit(Audit audit) {
        this.outputs.add(audit);
    }

    public void addOrderAudit(Audit audit) {
        this.orders.add(audit);
    }

    public void addDeliveryNoteAudit(Audit audit) {
        this.deliveryNotes.add(audit);
    }

    public void addSupplyingAudit(Audit audit) {
        this.supplyings.add(audit);
    }

    public Integer getResultsSize() {
        return (inputs.size() + outputs.size() + provisioningRequests.size() + orders.size() + deliveryNotes.size() + supplyings.size());
    }
}