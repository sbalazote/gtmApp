package com.lsntsolutions.gtmApp.model;

import javax.persistence.*;

@Entity
@Table(name="client_affiliate")
public class ClientAffiliate {


    @ManyToOne
    @PrimaryKeyJoinColumn(name="client_id", referencedColumnName="ID")
    private Client client;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="affiliate_id", referencedColumnName="ID")
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
