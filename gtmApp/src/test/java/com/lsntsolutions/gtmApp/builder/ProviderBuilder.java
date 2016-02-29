package com.lsntsolutions.gtmApp.builder;

import com.lsntsolutions.gtmApp.model.Agent;
import com.lsntsolutions.gtmApp.model.Provider;

/**
 * Created by a983060 on 29/02/2016.
 */
public class ProviderBuilder {
    private final Provider provider;

    public ProviderBuilder() {
        this.provider = new Provider();
    }

    public ProviderBuilder agent(Agent agent){
        this.provider.setAgent(agent);
        return this;
    }

    public ProviderBuilder gln(String gln){
        this.provider.setGln(gln);
        return this;
    }

    public Provider build() {
        return this.provider;
    }
}
