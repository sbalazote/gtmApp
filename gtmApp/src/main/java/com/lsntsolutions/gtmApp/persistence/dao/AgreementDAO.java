package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.Agreement;

import java.util.List;

public interface AgreementDAO {

	void save(Agreement agreement);

	Agreement get(Integer id);

	Boolean exists(Integer code);

	List<Agreement> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortNumberOfDeliveryNoteDetailsPerPage, String sortIsPickingList, String sortIsActive, String sortIsDeliveryNoteConcept, String sortDestructionConcept);

	List<Agreement> getAll();

	List<Agreement> getAllActives();

	boolean delete(Integer agreementId);

	List<Agreement> getPaginated(int start, int length);

	Long getTotalNumber();

	boolean isConceptInUse(Integer conceptId);
}
