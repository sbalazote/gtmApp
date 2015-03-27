package com.drogueria.persistence.dao.impl;

import java.util.List;

import com.drogueria.model.Concept;
import com.drogueria.service.ConceptService;
import com.drogueria.util.MockSecurityContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;

import com.drogueria.model.Agreement;
import com.drogueria.service.AgreementService;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-test.xml" })
public class AgreementServiceTest {

	@Autowired
	private AgreementService agreementService;

    @Autowired
    private ConceptService conceptService;

    private Concept concept;

    @Before
    public void setUp() {
        concept = new Concept();
        concept.setCode(1);
        concept.setActive(true);
        concept.setDeliveryNoteCopies(1);
        concept.setDeliveryNotePOS("");
        concept.setDescription("Test Concept");
        concept.setClient(true);
        concept.setEvents(null);
        concept.setInformAnmat(true);
        concept.setInput(true);
        concept.setLastDeliveryNoteNumber(1);
        concept.setPrintDeliveryNote(false);
        concept.setRefund(true);
        this.conceptService.save(concept);
    }

	@Test
	public void save() {
		Agreement agreement = new Agreement();
		agreement.setActive(true);
		agreement.setCode(123);
		agreement.setDescription("Test description");
        agreement.setDeliveryNoteConcept(concept);
        agreement.setDestructionConcept(concept);
		this.agreementService.save(agreement);

		Agreement savedAgreement = this.agreementService.get(agreement.getId());
		Assert.isTrue(agreement.getDescription().equals(savedAgreement.getDescription()));

		this.agreementService.delete(agreement.getId());
	}

	@Test
	public void getAll() {
		Agreement agreement1 = new Agreement();
		agreement1.setActive(true);
		agreement1.setCode(123);
		agreement1.setDescription("Test description");
        agreement1.setDeliveryNoteConcept(concept);
        agreement1.setDestructionConcept(concept);
		this.agreementService.save(agreement1);

		Agreement agreement2 = new Agreement();
		agreement2.setActive(true);
		agreement2.setCode(456);
		agreement2.setDescription("Test description 2");
        agreement2.setDeliveryNoteConcept(concept);
        agreement2.setDestructionConcept(concept);
		this.agreementService.save(agreement2);

		List<Agreement> agreements = this.agreementService.getAll();

		Assert.isTrue(agreements.size() == 2);

		for (Agreement agreement : agreements) {
			this.agreementService.delete(agreement.getId());
		}
	}

	@Test
	public void getAllActives() {
		Agreement agreement1 = new Agreement();
		agreement1.setActive(true);
		agreement1.setCode(123);
        agreement1.setDescription("Test description");
        agreement1.setDeliveryNoteConcept(concept);
        agreement1.setDestructionConcept(concept);
		this.agreementService.save(agreement1);

		Agreement agreement2 = new Agreement();
		agreement2.setActive(false);
		agreement2.setCode(456);
		agreement2.setDescription("Test description 2");
        agreement2.setDeliveryNoteConcept(concept);
        agreement2.setDestructionConcept(concept);
		this.agreementService.save(agreement2);

		List<Agreement> agreements = this.agreementService.getAllActives();

		Assert.isTrue(agreements.size() == 1);

		agreements = this.agreementService.getAll();
		for (Agreement agreement : agreements) {
			this.agreementService.delete(agreement.getId());
		}
	}

	@Test
	public void exists() {
		Agreement agreement = new Agreement();
		agreement.setActive(true);
        agreement.setCode(123);
        agreement.setDescription("Test description");
        agreement.setDeliveryNoteConcept(concept);

        agreement.setDestructionConcept(concept);

		this.agreementService.save(agreement);

		Boolean isTrue = this.agreementService.exists(agreement.getCode());
		Assert.isTrue(isTrue == true);

		Boolean isFalse = this.agreementService.exists(999);
		Assert.isTrue(isFalse == false);

		this.agreementService.delete(agreement.getId());
	}

}
