package com.lsntsolutions.gtmApp.util;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.Provider;

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

    public Input build() {
        return this.input;
    }
}
