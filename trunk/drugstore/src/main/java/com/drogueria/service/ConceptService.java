package com.drogueria.service;

import java.util.List;

import com.drogueria.model.Concept;

public interface ConceptService {

	void save(Concept concept);

	Concept get(Integer id);

	Boolean exists(Integer code);

	List<Concept> getForAutocomplete(String term, Boolean active);

	public List<Concept> getAll();

	List<Concept> getAllActives(Boolean input);

	List<Concept> getAllReturnConcepts();

	boolean delete(Integer conceptId);

	List<Concept> getPaginated(int start, int length);

	Long getTotalNumber();

	Concept getAndUpdateDeliveryNote(Integer id, Integer deliveryNoteNumbersRequired);

}
