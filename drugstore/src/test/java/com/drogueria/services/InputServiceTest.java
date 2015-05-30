package com.drogueria.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;

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

import com.drogueria.dto.InputDTO;
import com.drogueria.dto.InputDTOBuilder;
import com.drogueria.dto.InputDetailDTO;
import com.drogueria.dto.InputDetailDTOBuilder;
import com.drogueria.exceptions.DateParseException;
import com.drogueria.exceptions.NullAgreementIdException;
import com.drogueria.exceptions.NullConceptIdException;
import com.drogueria.exceptions.NullDateException;
import com.drogueria.exceptions.NullProviderAndDeliveryLocationIdsException;
import com.drogueria.model.Agreement;
import com.drogueria.model.Concept;
import com.drogueria.model.Input;
import com.drogueria.model.ProductGtin;
import com.drogueria.persistence.dao.InputDAO;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.PropertyService;
import com.drogueria.service.OrderService;
import com.drogueria.service.OutputService;
import com.drogueria.service.ProductGtinService;
import com.drogueria.service.ProductService;
import com.drogueria.service.ProviderService;
import com.drogueria.service.StockService;
import com.drogueria.service.impl.InputServiceImpl;

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