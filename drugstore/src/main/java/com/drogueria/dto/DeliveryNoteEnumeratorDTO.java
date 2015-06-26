package com.drogueria.dto;

public class DeliveryNoteEnumeratorDTO {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer deliveryNotePOS;
    private Integer lastDeliveryNoteNumber;
    private boolean fake;
    private boolean active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeliveryNotePOS() {
        return deliveryNotePOS;
    }

    public void setDeliveryNotePOS(Integer deliveryNotePOS) {
        this.deliveryNotePOS = deliveryNotePOS;
    }

    public Integer getLastDeliveryNoteNumber() {
        return lastDeliveryNoteNumber;
    }

    public void setLastDeliveryNoteNumber(Integer lastDeliveryNoteNumber) {
        this.lastDeliveryNoteNumber = lastDeliveryNoteNumber;
    }

    public boolean isFake() {
        return fake;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
