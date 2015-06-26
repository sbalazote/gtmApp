package com.drogueria.service;

import com.drogueria.model.DeliveryNoteEnumerator;

import java.util.List;

public interface DeliveryNoteEnumeratorService {

    void save(DeliveryNoteEnumerator deliveryNoteEnumerator);

    DeliveryNoteEnumerator get(Integer id);

    Boolean exists(Integer deliveryNotePOS, Boolean fake);

    List<DeliveryNoteEnumerator> getForAutocomplete(String term, Boolean active, Boolean fake);

    List<DeliveryNoteEnumerator> getAll();

    boolean delete(Integer deliveryNoteEnumeratorId);

    List<DeliveryNoteEnumerator> getPaginated(int start, int length);

    Long getTotalNumber();
}
