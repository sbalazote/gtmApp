package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.DeliveryNoteEnumerator;

import java.util.List;

public interface DeliveryNoteEnumeratorDAO {

	void save(DeliveryNoteEnumerator deliveryNoteEnumerator);

	DeliveryNoteEnumerator get(Integer id);

	Boolean exists(Integer deliveryNotePOS, Boolean fake);

	List<DeliveryNoteEnumerator> getForAutocomplete(String term, Boolean active, Boolean fake, String sortId, String sortDeliveryNotePOS, String sortLastDeliveryNoteNumber, String sortIsActive);

	List<DeliveryNoteEnumerator> getAll();

	boolean delete(Integer deliveryNoteEnumeratorId);

	List<DeliveryNoteEnumerator> getPaginated(int start, int length);

	Long getTotalNumber();

	List<DeliveryNoteEnumerator> getReals(boolean fake);

	Boolean checkNewDeliveryNoteNumber(Integer id, Integer lastDeliveryNoteNumberInput);
}
