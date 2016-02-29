package com.lsntsolutions.gtmApp.builder;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.DeliveryNoteEnumerator;
import com.lsntsolutions.gtmApp.model.Event;

import java.util.List;

public class ConceptBuilder {

    private final Concept concept;

    public ConceptBuilder() {
        this.concept = new Concept();
    }

    public ConceptBuilder active(boolean active) {
        this.concept.setActive(active);
        return this;
    }

    public ConceptBuilder deliveryNoteEnumerator(DeliveryNoteEnumerator deliveryNoteEnumerator) {
        this.concept.setDeliveryNoteEnumerator(deliveryNoteEnumerator);
        return this;
    }

    public ConceptBuilder code(Integer code) {
        this.concept.setCode(code);
        return this;
    }

    public ConceptBuilder informAnmat(boolean informAnmat) {
        this.concept.setInformAnmat(informAnmat);
        return this;
    }

    public ConceptBuilder events(List<Event> events){
        this.concept.setEvents(events);
        return this;
    }

    public Concept build() {
        return this.concept;
    }
}
