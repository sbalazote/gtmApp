package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Agreement;
import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.DeliveryNoteEnumerator;
import com.lsntsolutions.gtmApp.service.AgreementService;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.DeliveryNoteEnumeratorService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class AgreementServiceTest {

	@Autowired
	private AgreementService agreementService;

	@Autowired
	private ConceptService conceptService;

	@Autowired
	private DeliveryNoteEnumeratorService deliveryNoteEnumeratorService;

	private Concept concept;

	@Before
	public void setUp() {
		this.concept = new Concept();
		this.concept.setCode(1);
		this.concept.setActive(true);
		this.concept.setDeliveryNoteCopies(1);
		this.concept.setDescription("Test Concept");
		this.concept.setClient(true);
		this.concept.setEvents(null);
		this.concept.setInformAnmat(true);
		this.concept.setInput(true);
		this.concept.setPrintDeliveryNote(false);
		this.concept.setRefund(true);
		this.concept.setId(1);

		DeliveryNoteEnumerator deliveryNoteEnumerator = new DeliveryNoteEnumerator();
		deliveryNoteEnumerator.setActive(true);
		deliveryNoteEnumerator.setDeliveryNotePOS(new Random().nextInt());
		deliveryNoteEnumerator.setFake(false);
		deliveryNoteEnumerator.setLastDeliveryNoteNumber(1);
		this.deliveryNoteEnumeratorService.save(deliveryNoteEnumerator);
		this.concept.setDeliveryNoteEnumerator(deliveryNoteEnumerator);

		this.conceptService.save(this.concept);
	}

	@Test
	public void save() {
		Agreement agreement = new Agreement();
		agreement.setActive(true);
		agreement.setCode(1111);
		agreement.setDescription("Test description");
		agreement.setDeliveryNotePrinter("");
		agreement.setOrderLabelPrinter("");
		agreement.setNumberOfDeliveryNoteDetailsPerPage(999);
		agreement.setDeliveryNoteConcept(this.concept);
		agreement.setDestructionConcept(this.concept);
		this.agreementService.save(agreement);

		Agreement savedAgreement = this.agreementService.get(agreement.getId());
		Assert.isTrue(agreement.getDescription().equals(savedAgreement.getDescription()));

		this.agreementService.delete(savedAgreement.getId());

	}

	@Test
	public void getAll() {
		Agreement agreement1 = new Agreement();
		agreement1.setActive(true);
		agreement1.setCode(1234);
		agreement1.setDescription("Test description");
		agreement1.setDeliveryNoteConcept(this.concept);
		agreement1.setDestructionConcept(this.concept);
		agreement1.setDeliveryNotePrinter("");
		agreement1.setOrderLabelPrinter("");
		agreement1.setNumberOfDeliveryNoteDetailsPerPage(999);
		this.agreementService.save(agreement1);

		Agreement agreement2 = new Agreement();
		agreement2.setActive(true);
		agreement2.setCode(456);
		agreement2.setDescription("Test description 2");
		agreement2.setDeliveryNoteConcept(this.concept);
		agreement2.setDestructionConcept(this.concept);
		agreement2.setDeliveryNotePrinter("");
		agreement2.setOrderLabelPrinter("");
		agreement2.setNumberOfDeliveryNoteDetailsPerPage(999);
		this.agreementService.save(agreement2);

		List<Agreement> agreements = this.agreementService.getAll();

		Assert.isTrue(agreements.size() == 2);
	}
/*
	@Test
	public void getAllActives() {
		Agreement agreement1 = new Agreement();
		agreement1.setActive(true);
		agreement1.setCode(2222);
		agreement1.setDescription("Test description");
		agreement1.setDeliveryNotePrinter("");
		agreement1.setOrderLabelPrinter("");
		agreement1.setNumberOfDeliveryNoteDetailsPerPage(999);
		agreement1.setDeliveryNoteConcept(this.concept);
		agreement1.setDestructionConcept(this.concept);
		this.agreementService.save(agreement1);

		Agreement agreement2 = new Agreement();
		agreement2.setActive(false);
		agreement2.setCode(456);
		agreement2.setDescription("Test description 2");
		agreement2.setDeliveryNotePrinter("");
		agreement2.setOrderLabelPrinter("");
		agreement2.setNumberOfDeliveryNoteDetailsPerPage(999);
		agreement2.setDeliveryNoteConcept(this.concept);
		agreement2.setDestructionConcept(this.concept);
		this.agreementService.save(agreement2);

		List<Agreement> agreements = this.agreementService.getAllActives();

		Assert.isTrue(agreements.size() == 1);
	}

	@Test
	public void exists() {
		Agreement agreement = new Agreement();
		agreement.setActive(true);
		agreement.setCode(3333);
		agreement.setDescription("Test description");
		agreement.setDeliveryNotePrinter("");
		agreement.setOrderLabelPrinter("");
		agreement.setNumberOfDeliveryNoteDetailsPerPage(999);
		agreement.setDeliveryNoteConcept(this.concept);
		agreement.setDestructionConcept(this.concept);
		this.agreementService.save(agreement);

		/*Boolean isTrue = this.agreementService.exists(agreement.getCode());
		Assert.isTrue(isTrue == true);

		Boolean isFalse = this.agreementService.exists(999);
		Assert.isTrue(isFalse == false);
	}
*/
}
