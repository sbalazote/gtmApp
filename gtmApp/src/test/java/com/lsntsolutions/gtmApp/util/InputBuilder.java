package com.lsntsolutions.gtmApp.util;

import com.lsntsolutions.gtmApp.model.Input;

/**
 * Created by a983060 on 25/02/2016.
 */
public class InputBuilder {

    private final Input input;

    public InputBuilder() {
        this.input = new Input();
    }

    public Input build() {
        return this.input;
    }
}
