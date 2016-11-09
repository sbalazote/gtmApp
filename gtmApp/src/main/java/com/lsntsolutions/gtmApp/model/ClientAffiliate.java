package com.lsntsolutions.gtmApp.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "client_affiliate")
@AssociationOverrides({
        @AssociationOverride(name = "pk.affiliate",
                joinColumns = @JoinColumn(name = "id")),
        @AssociationOverride(name = "pk.client",
                joinColumns = @JoinColumn(name = "id")) })
public class ClientAffiliate implements Serializable {

    private ClientAffiliateId pk = new ClientAffiliateId();

    @EmbeddedId
    public ClientAffiliateId getPk() {
        return pk;
    }

    public void setPk(ClientAffiliateId pk) {
        this.pk = pk;
    }

    @Transient
    public Client getClient() {
        return getPk().getClient();
    }

    public void setClient(Client client) {
        getPk().setClient(client);
    }

    @Transient
    public Affiliate getAffiliate() {
        return getPk().getAffiliate();
    }

    public void setAffiliate(Affiliate affiliate) {
        getPk().setAffiliate(affiliate);
    }

    @ManyToOne
    @PrimaryKeyJoinColumn(name="client_id", referencedColumnName="id")
    private Client client;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="affiliate_id", referencedColumnName="id")
    private Affiliate affiliate;

    @Column(name="associate_number")
    private String associateNumber;

    /*public Client getClient() {
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
    }*/

    public String getAssociateNumber() {
        return associateNumber;
    }

    public void setAssociateNumber(String associateNumber) {
        this.associateNumber = associateNumber;
    }
}
