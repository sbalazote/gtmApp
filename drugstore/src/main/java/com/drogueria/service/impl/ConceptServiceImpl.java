package com.drogueria.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.Concept;
import com.drogueria.persistence.dao.ConceptDAO;
import com.drogueria.service.ConceptService;

@Service
@Transactional
public class ConceptServiceImpl implements ConceptService {

	private static final Logger logger = Logger.getLogger(ConceptServiceImpl.class);

	@Autowired
	private ConceptDAO conceptDAO;

	@Override
	public void save(Concept concept) {
		this.conceptDAO.save(concept);
		logger.info("Se han guardado los cambios exitosamente. Id de Concepto: " + concept.getId());
	}

	@Override
	public Concept get(Integer id) {
		return this.conceptDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.conceptDAO.exists(code);
	}

	@Override
	public List<Concept> getForAutocomplete(String term, Boolean active) {
		return this.conceptDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<Concept> getAll() {
		return this.conceptDAO.getAll();
	}

	@Override
	public List<Concept> getAllActives(Boolean input) {
		return this.conceptDAO.getAllActives(input);
	}

	@Override
	public List<Concept> getAllReturnConcepts() {
		return this.conceptDAO.getAllReturnConcepts();
	}

	@Override
	public boolean delete(Integer conceptId) {
		return this.conceptDAO.delete(conceptId);
	}

	@Override
	public List<Concept> getPaginated(int start, int length) {
		return this.conceptDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.conceptDAO.getTotalNumber();
	}

	@Override
	public Concept getAndUpdateDeliveryNote(Integer id, Integer amount) {
		Concept concept = this.conceptDAO.getForUpdate(id);
		Integer lastNumber = concept.getLastDeliveryNoteNumber();
		Integer newLastNumber = lastNumber + amount;
		concept.setLastDeliveryNoteNumber(newLastNumber);
		this.conceptDAO.save(concept);
		return concept;
	}
}