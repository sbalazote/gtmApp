package com.lsntsolutions.gtmApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "client_affiliate")
public class ClientAffiliate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="client_id")
    @JsonBackReference
    private Client client;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="affiliate_id")
    @JsonBackReference
    private Affiliate affiliate;

    @Column(name="associate_number")
    private String associateNumber;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Affiliate getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
    }

    public String getAssociateNumber() {
        return associateNumber;
    }

    public void setAssociateNumber(String associateNumber) {
        this.associateNumber = associateNumber;
    }
}
