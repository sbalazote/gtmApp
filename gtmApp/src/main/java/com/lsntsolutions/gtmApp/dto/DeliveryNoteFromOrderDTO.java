package com.lsntsolutions.gtmApp.dto;

import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Order;

/**
 * Created by Sebastian on 10/11/2015.
 */
public class DeliveryNoteFromOrderDTO {
    private DeliveryNote deliveryNote;
    private Order order;

    public DeliveryNoteFromOrderDTO(DeliveryNote deliveryNote, Order order) {
        this.deliveryNote = deliveryNote;
        this.order = order;
    }

    public DeliveryNote getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
