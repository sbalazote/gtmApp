package com.lsntsolutions.gtmApp.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "delivery_note_enumerator")
public class DeliveryNoteEnumerator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "deliveryNotePOS", unique = true, nullable = false)
    private Integer deliveryNotePOS;

    @Column(name = "lastDeliveryNoteNumber", nullable = false)
    private Integer lastDeliveryNoteNumber;

    @Column(name = "fake", nullable = false)
    private boolean fake;

    @Column(name = "active", nullable = false)
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

    public Integer getDeliveryNoteNumber() {
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
