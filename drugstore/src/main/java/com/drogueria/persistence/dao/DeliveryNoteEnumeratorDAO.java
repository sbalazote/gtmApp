package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.DeliveryNoteEnumerator;

public interface DeliveryNoteEnumeratorDAO {

	void save(DeliveryNoteEnumerator deliveryNoteEnumerator);

	DeliveryNoteEnumerator get(Integer id);

	Boolean exists(Integer deliveryNotePOS, Boolean fake);

	List<DeliveryNoteEnumerator> getForAutocomplete(String term, Boolean active, Boolean fake);

	List<DeliveryNoteEnumerator> getAll();

	boolean delete(Integer deliveryNoteEnumeratorId);

	List<DeliveryNoteEnumerator> getPaginated(int start, int length);

	Long getTotalNumber();

	List<DeliveryNoteEnumerator> getReals(boolean fake);
}