package com.drogueria.webservice;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.drogueria.config.PropertyProvider;
import com.drogueria.model.InputDetail;
import com.drogueria.service.PropertyService;
import com.drogueria.webservice.helper.InputWSHelper;
import com.inssjp.mywebservice.business.TransaccionPlainWS;
import com.inssjp.mywebservice.business.TransaccionesNoConfirmadasWSResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class InputWSHelperTest {
	@Mock
	WebService webService;
	@Mock
	PropertyService propertyService;
	@Mock
	PropertyProvider propertyProvider;

	private TransaccionesNoConfirmadasWSResult transaccionesNoConfigmadasList;

	private List<InputDetail> inputDetails;

	@Autowired
	InputWSHelper inputWSHelper;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.transaccionesNoConfigmadasList = new TransaccionesNoConfirmadasWSResult();
		TransaccionPlainWS[] transaccionPlainWSList = new TransaccionPlainWS[3];
		for (int i = 0; i < 3; i++) {
			transaccionPlainWSList[i] = new TransaccionPlainWS();
			transaccionPlainWSList[i].set_d_evento("01");
			transaccionPlainWSList[i].set_f_evento("01");
			transaccionPlainWSList[i].set_f_transaccion("01");
			transaccionPlainWSList[i].set_gln_origen("0000000001");
			transaccionPlainWSList[i].set_gln_destino("0000000002");
			transaccionPlainWSList[i].set_gtin("11111111");
			transaccionPlainWSList[i].set_id_evento(new Long(i));
			transaccionPlainWSList[i].set_id_estado(new Long(i));
			transaccionPlainWSList[i].set_id_transaccion(String.valueOf(i));
			transaccionPlainWSList[i].set_id_transaccion_global("1");
			transaccionPlainWSList[i].set_n_factura("R0001000000001");
			transaccionPlainWSList[i].set_n_remito("1");
			transaccionPlainWSList[i].set_nombre("Test");
			transaccionPlainWSList[i].set_numero_serial(String.valueOf(i));
			transaccionPlainWSList[i].set_razon_social_destino("TEST_1");
			transaccionPlainWSList[i].set_razon_social_origen("TEST_2");
			transaccionPlainWSList[i].set_vencimiento("01/01/2020");
		}
		this.transaccionesNoConfigmadasList.setList(transaccionPlainWSList);

		this.inputDetails = new ArrayList<InputDetail>();
		InputDetail inputDetail = new InputDetail();
	}

	@Test
	public void checkPendingTransactionsHasToReturnTwoInputDetails() throws Exception {
		when(
				this.webService.getTransaccionesNoConfirmadas(Matchers.anyString(), Matchers.anyString(), Matchers.anyLong(), Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyLong(), Matchers.anyString(), Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),
						Matchers.anyLong(), Matchers.anyString(), Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong())).thenReturn(
				this.transaccionesNoConfigmadasList);
		// given
		List<InputDetail> pendingProducts = new ArrayList<InputDetail>();
		List<String> errors = new ArrayList<String>();
		boolean error = this.inputWSHelper.getPendingTransactions(this.inputDetails, pendingProducts, errors);
		// Assert.isTrue(error);
		// Assert.isTrue(pendingProducts.size() == 0);
	}
}
