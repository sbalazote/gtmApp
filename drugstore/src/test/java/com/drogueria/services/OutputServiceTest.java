package com.drogueria.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

import com.drogueria.dto.OutputDTO;
import com.drogueria.dto.OutputDTOBuilder;
import com.drogueria.dto.OutputDetailDTO;
import com.drogueria.dto.OutputDetailDTOBuilder;
import com.drogueria.model.Concept;
import com.drogueria.model.Output;
import com.drogueria.model.ProductGtin;
import com.drogueria.persistence.dao.OutputDAO;
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
import com.drogueria.service.impl.OutputServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-test.xml" })
public class OutputServiceTest {

	@InjectMocks
	private OutputServiceImpl outputService;

	@Mock
	private OutputDAO outputDAO;
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

	@Test
	public void testSaveWithCorrectInput() throws Exception {
		Concept concept = new Concept();
		concept.setInformAnmat(false);
		ProductGtin productGtin = new ProductGtin();
		OutputDetailDTO iddto = new OutputDetailDTOBuilder().amount(1).batch("L0065").expirationDate("11/01/20").productId(1).productType("BE")
				.serialNumber("").gtin("").build();

		OutputDTO dto = new OutputDTOBuilder().agreementId(1).conceptId(1).date("01/01/2015").deliveryLocationId(null).providerId(1).addInputDetail(iddto)
				.build();

		when(this.conceptServiceMock.get(1)).thenReturn(concept);
		when(this.productGtinServiceMock.get(1)).thenReturn(productGtin);

		doNothing().when(this.outputDAO).save(Matchers.isA(Output.class));

		this.outputService.save(dto);
	}
}
