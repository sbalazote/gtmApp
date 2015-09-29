package com.lsntsolutions.gtmApp.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.lsntsolutions.gtmApp.dto.InputDTO;
import com.lsntsolutions.gtmApp.dto.InputDTOBuilder;
import com.lsntsolutions.gtmApp.dto.InputDetailDTO;
import com.lsntsolutions.gtmApp.dto.InputDetailDTOBuilder;
import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.ProductGtin;
import com.lsntsolutions.gtmApp.persistence.dao.InputDAO;
import com.lsntsolutions.gtmApp.service.impl.InputServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lsntsolutions.gtmApp.exceptions.DateParseException;
import com.lsntsolutions.gtmApp.exceptions.NullAgreementIdException;
import com.lsntsolutions.gtmApp.exceptions.NullConceptIdException;
import com.lsntsolutions.gtmApp.exceptions.NullDateException;
import com.lsntsolutions.gtmApp.exceptions.NullProviderAndDeliveryLocationIdsException;
import com.lsntsolutions.gtmApp.model.Agreement;
import com.lsntsolutions.gtmApp.service.AgreementService;
import com.lsntsolutions.gtmApp.service.AuditService;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.DeliveryLocationService;
import com.lsntsolutions.gtmApp.service.PropertyService;
import com.lsntsolutions.gtmApp.service.OrderService;
import com.lsntsolutions.gtmApp.service.OutputService;
import com.lsntsolutions.gtmApp.service.ProductGtinService;
import com.lsntsolutions.gtmApp.service.ProductService;
import com.lsntsolutions.gtmApp.service.ProviderService;
import com.lsntsolutions.gtmApp.service.StockService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class InputServiceTest {

	@InjectMocks
	private InputServiceImpl inputService;

	@Mock
	private InputDAO inputDAO;
	@Mock
	private ConceptService conceptServiceMock;
	@Mock
	private ProviderService providerServiceMock;
	@Mock
	private AgreementService agreementServiceMock;
	@Mock
	private ProductService productServiceMock;
	@Mock
	private StockService stockServiceMock;
	@Mock
	private PropertyService PropertyServiceMock;
	@Mock
	private DeliveryLocationService deliveryLocationServiceMock;
	@Mock
	private AuditService auditServiceMock;
	@Mock
	private OutputService outputServiceMock;
	@Mock
	private OrderService orderServiceMock;
	@Mock
	private ProductGtinService productGtinServiceMock;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public final void tearDown() {
	}

	@Test(expected = NullAgreementIdException.class)
	public void testSaveWithNullAgreement() throws Exception {
		ProductGtin productGtin = new ProductGtin();
		productGtin.setNumber("928029");
		when(this.productGtinServiceMock.get(1)).thenReturn(productGtin);

		InputDetailDTO iddto = new InputDetailDTOBuilder().amount(1).batch("L0065").expirationDate("110120").productId(1).productType("BE").serialNumber("")
				.gtin("").build();

		InputDTO dto = new InputDTOBuilder().agreementId(null).conceptId(1).date("01/01/2015").deliveryLocationId(1).deliveryNoteNumber("R0000da")
				.providerId(1).purchaseOrderNumber("a").addInputDetail(iddto).build();

		this.inputService.save(dto, false, "testAdmin");
	}

	@Test(expected = NullConceptIdException.class)
	public void testSaveWithNullConcept() throws Exception {

		InputDetailDTO iddto = new InputDetailDTOBuilder().amount(1).batch("L0065").expirationDate("110120").productId(1).productType("BE").serialNumber("")
				.gtin("").build();

		InputDTO dto = new InputDTOBuilder().agreementId(1).conceptId(null).date("01/01/2015").deliveryLocationId(1).deliveryNoteNumber("R0000da")
				.providerId(1).purchaseOrderNumber("a").addInputDetail(iddto).build();

		this.inputService.save(dto, false, "testAdmin");
	}

	@Test(expected = NullDateException.class)
	public void testSaveWithNullDate() throws Exception {

		InputDetailDTO iddto = new InputDetailDTOBuilder().amount(1).batch("L0065").expirationDate("110120").productId(1).productType("BE").serialNumber("")
				.gtin("").build();

		InputDTO dto = new InputDTOBuilder().agreementId(1).conceptId(1).date(null).deliveryLocationId(1).deliveryNoteNumber("R0000da").providerId(1)
				.purchaseOrderNumber("a").addInputDetail(iddto).build();

		this.inputService.save(dto, false, "testAdmin");
	}

	@Test(expected = DateParseException.class)
	public void testSaveWithMalFormedDate() throws Exception {
		Agreement f = new Agreement();
		Agreement gg = new Agreement();

		InputDetailDTO iddto = new InputDetailDTOBuilder().amount(1).batch("L0065").expirationDate("110120").productId(1).productType("BE").serialNumber("")
				.gtin("").build();

		InputDTO dto = new InputDTOBuilder().agreementId(1).conceptId(1).date("01-01-2015").deliveryLocationId(1).deliveryNoteNumber("R0000da").providerId(1)
				.purchaseOrderNumber("a").addInputDetail(iddto).build();

		when(this.agreementServiceMock.getAll()).thenReturn(Arrays.asList(f, gg));

		this.inputService.save(dto, false, "testAdmin");
	}

	@Test
	public void testSaveWithCorrectInput() throws Exception {
		Concept concept = new Concept();
		concept.setInformAnmat(false);

		InputDetailDTO iddto = new InputDetailDTOBuilder().amount(1).batch("L0065").expirationDate("110120").productId(1).productType("BE").serialNumber("")
				.gtin("").build();

		InputDTO dto = new InputDTOBuilder().agreementId(1).conceptId(1).date("01/01/2015").deliveryLocationId(null).deliveryNoteNumber("R0000da")
				.providerId(1).purchaseOrderNumber("a").addInputDetail(iddto).build();

		when(this.conceptServiceMock.get(1)).thenReturn(concept);

		doNothing().when(this.inputDAO).save(Matchers.isA(Input.class));

		this.inputService.save(dto, false, "testAdmin");
	}

	@Test(expected = NullProviderAndDeliveryLocationIdsException.class)
	public void testSaveWithNullProviderAndDeliveryLocationInput() throws Exception {
		Concept concept = new Concept();
		concept.setInformAnmat(false);

		InputDetailDTO iddto = new InputDetailDTOBuilder().amount(1).batch("L0065").expirationDate("110120").productId(1).productType("BE").serialNumber("")
				.gtin("").build();

		InputDTO dto = new InputDTOBuilder().agreementId(1).conceptId(1).date("01/01/2015").deliveryLocationId(null).deliveryNoteNumber("R0000da")
				.providerId(null).purchaseOrderNumber("a").addInputDetail(iddto).build();

		when(this.conceptServiceMock.get(1)).thenReturn(concept);

		doNothing().when(this.inputDAO).save(Matchers.isA(Input.class));

		this.inputService.save(dto, false, "testAdmin");
	}

}