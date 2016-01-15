package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.persistence.dao.ConceptDAO;
import com.lsntsolutions.gtmApp.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConceptServiceImpl implements ConceptService {

	private static final Logger logger = Logger.getLogger(ConceptServiceImpl.class);

	@Autowired
	private ConceptDAO conceptDAO;

    @Autowired
    private InputService inputService;

    @Autowired
    private OutputService outputService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private PropertyService propertyService;

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
	public List<Concept> getAllReturnFromClientConcepts() {
		return this.conceptDAO.getAllReturnFromClientConcepts();
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
	public synchronized Concept getAndUpdateDeliveryNote(Integer id, Integer amount) {
		Concept concept = this.conceptDAO.get(id);
		Integer lastNumber = concept.getDeliveryNoteEnumerator().getLastDeliveryNoteNumber();
		Integer newLastNumber = lastNumber + amount;
		concept.getDeliveryNoteEnumerator().setLastDeliveryNoteNumber(newLastNumber);
		this.conceptDAO.save(concept);
		return concept;
	}

	@Override
    public boolean isAlreadyInUse(Integer conceptId){
        boolean toReturn;

        boolean inUseOnInput = inputService.isConceptInUse(conceptId);

        boolean isUseOnAgreements = agreementService.isConceptInUse(conceptId);

        boolean isUseOnOutput = outputService.isConceptInUse(conceptId);

        boolean isUseOnProperty = propertyService.isConceptInUse(conceptId);

        toReturn =(inUseOnInput || isUseOnAgreements || isUseOnOutput || isUseOnProperty);

        return toReturn;
    }

	@Override
	public List<Concept> getConceptForInput() {
		return this.conceptDAO.getConceptForInput();
	}
}