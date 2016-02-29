package com.lsntsolutions.gtmApp.builder;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.InputDetail;
import com.lsntsolutions.gtmApp.model.Provider;

import java.util.Date;
import java.util.List;

public class InputBuilder {

    private final Input input;

    public InputBuilder() {
        this.input = new Input();
    }


    public InputBuilder concept(Concept concept) {
        this.input.setConcept(concept);
        return this;
    }

    public InputBuilder provider(Provider provider) {
        this.input.setProvider(provider);
        return this;
    }

    public InputBuilder details(List<InputDetail> details) {
        this.input.setInputDetails(details);
        return this;
    }

    public InputBuilder date(Date date) {
        this.input.setDate(date);
        return this;
    }

    public Input build() {
        return this.input;
    }
}
