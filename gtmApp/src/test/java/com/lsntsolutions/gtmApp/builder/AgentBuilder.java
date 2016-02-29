package com.lsntsolutions.gtmApp.builder;

import com.lsntsolutions.gtmApp.model.Agent;

/**
 * Created by a983060 on 29/02/2016.
 */
public class AgentBuilder {

    private final Agent agent;

    public AgentBuilder() {
        this.agent = new Agent();
    }

    public AgentBuilder id(Integer id) {
        this.agent.setId(id);
        return this;
    }

    public Agent build() {
        return this.agent;
    }
}
