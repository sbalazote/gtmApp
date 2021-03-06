package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.Concept;

import java.util.List;

public interface ConceptService {

	void save(Concept concept);

	Concept get(Integer id);

	Boolean exists(Integer code);

	List<Concept> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortDeliveryNotePOS, String sortIsInformAnmat, String sortIsActive);

	List<Concept> getAll();

	List<Concept> getAllActives(Boolean input);

	List<Concept> getAllReturnFromClientConcepts();

	boolean delete(Integer conceptId);

	List<Concept> getPaginated(int start, int length);

	Long getTotalNumber();

	Concept getAndUpdateDeliveryNote(Integer id, Integer deliveryNoteNumbersRequired);

	boolean isAlreadyInUse(Integer conceptId);

	List<Concept> getConceptForInput();

	List<Concept> getDestructionConcept();
}
