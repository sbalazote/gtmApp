package com.lsntsolutions.gtmApp.builder;

import com.lsntsolutions.gtmApp.model.Agent;
import com.lsntsolutions.gtmApp.model.Event;

/**
 * Created by a983060 on 29/02/2016.
 */
public class EventBuilder {
    private final Event event;

    public EventBuilder() {
        this.event = new Event();
    }

    public EventBuilder originAgent(Agent agent) {
        this.event.setOriginAgent(agent);
        return this;
    }

    public EventBuilder code(Integer code){
        this.event.setCode(code);
        return this;
    }

    public Event build() {
        return this.event;
    }
}
