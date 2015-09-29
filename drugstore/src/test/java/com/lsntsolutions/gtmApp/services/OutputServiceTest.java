package com.lsntsolutions.gtmApp.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.lsntsolutions.gtmApp.dto.OutputDTO;
import com.lsntsolutions.gtmApp.dto.OutputDetailDTO;
import com.lsntsolutions.gtmApp.dto.OutputDetailDTOBuilder;
import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.model.ProductGtin;
import com.lsntsolutions.gtmApp.persistence.dao.OutputDAO;
import com.lsntsolutions.gtmApp.service.impl.OutputServiceImpl;
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

import com.lsntsolutions.gtmApp.dto.OutputDTOBuilder;
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
