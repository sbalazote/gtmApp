package com.drogueria.webservice;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import com.drogueria.helper.EncryptionHelper;
import com.drogueria.model.InputDetail;
import com.drogueria.model.Product;
import com.drogueria.model.ProductGtin;
import com.drogueria.model.Property;
import com.drogueria.service.PropertyService;
import com.drogueria.webservice.helper.InputWSHelper;
import com.inssjp.mywebservice.business.TransaccionPlainWS;
import com.inssjp.mywebservice.business.TransaccionesNoConfirmadasWSResult;

@Ignore
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class InputWSHelperTest {
	@Mock
	WebService webService;

	private TransaccionesNoConfirmadasWSResult transaccionesNoConfigmadasList;

	private List<InputDetail> inputDetails;

	@Mock
	PropertyService propertyService;

	@InjectMocks
	InputWSHelper inputWSHelper;

	@Mock
	EncryptionHelper encryptionHelper;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.transaccionesNoConfigmadasList = new TransaccionesNoConfirmadasWSResult();
		TransaccionPlainWS[] transaccionPlainWSList = new TransaccionPlainWS[3];
		Product productTest = new Product();
		productTest.setType("PS");
		productTest.setInformAnmat(true);
		ProductGtin productGtinTest = new ProductGtin();
		List<ProductGtin> gtins = new ArrayList<ProductGtin>();
		productGtinTest.setNumber("1");
		productGtinTest.setId(1);
		productGtinTest.setDate(new Date());
		gtins.add(productGtinTest);
		productTest.setGtins(gtins);
		for (int i = 0; i < 3; i++) {
			transaccionPlainWSList[i] = new TransaccionPlainWS();
			transaccionPlainWSList[i].set_d_evento("01");
			transaccionPlainWSList[i].set_f_evento("01");
			transaccionPlainWSList[i].set_f_transaccion("01");
			transaccionPlainWSList[i].set_gln_origen("0000000001");
			transaccionPlainWSList[i].set_gln_destino("0000000002");
			transaccionPlainWSList[i].set_gtin("1");
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
		for (int i = 0; i < 3; i++) {
			InputDetail inputDetail = new InputDetail();
			inputDetail.setSerialNumber(String.valueOf(i));
			inputDetail.setAmount(1);
			inputDetail.setBatch("111");
			inputDetail.setExpirationDate(new Date());
			inputDetail.setProduct(productTest);
			inputDetail.setId(i);
			inputDetail.setGtin(productGtinTest);
			this.inputDetails.add(inputDetail);
		}
	}

	@Test
	public void checkPendingTransactionsHasToReturnTwoInputDetails() throws Exception {
		when(
				this.webService.getTransaccionesNoConfirmadas(Matchers.anyString(), Matchers.anyString(), Matchers.anyLong(), Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyLong(), Matchers.anyString(), Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),
						Matchers.anyLong(), Matchers.anyString(), Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong())).thenReturn(
								this.transaccionesNoConfigmadasList);
		Property property = new Property();
		property.setDaysAgoPendingTransactions(10);
		when(this.propertyService.get()).thenReturn(property);
		List<InputDetail> pendingProducts = new ArrayList<InputDetail>();
		List<String> errors = new ArrayList<String>();
		boolean response = this.inputWSHelper.getPendingTransactions(this.inputDetails, pendingProducts, errors, true);
		Assert.isTrue(response);
		Assert.isTrue(errors.size() == 0);
		Assert.isTrue(pendingProducts.size() == 0);
	}
}
