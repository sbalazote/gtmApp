package com.lsntsolutions.gtmApp.model;

import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Sebastian on 09/11/2016.
 */
public class ClientAffiliateId implements Serializable {

    private Client client;
    private Affiliate affiliate;

    @ManyToOne
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @ManyToOne
    public Affiliate getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientAffiliateId that = (ClientAffiliateId) o;

        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        if (affiliate != null ? !affiliate.equals(that.affiliate) : that.affiliate != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (client != null ? client.hashCode() : 0);
        result = 31 * result + (affiliate != null ? affiliate.hashCode() : 0);
        return result;
    }
}
