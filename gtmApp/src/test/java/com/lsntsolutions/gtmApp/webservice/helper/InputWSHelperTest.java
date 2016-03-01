package com.lsntsolutions.gtmApp.webservice.helper;

import com.inssjp.mywebservice.business.*;
import com.lsntsolutions.gtmApp.builder.*;
import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.util.OperationResult;
import com.lsntsolutions.gtmApp.util.StringUtility;
import com.lsntsolutions.gtmApp.webservice.WebService;
import com.lsntsolutions.gtmApp.webservice.WebServiceHelper;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class InputWSHelperTest {

    public static final String SERIAL = "44444444";
    public static final String GLN_ORIGEN = "1111111111";
    public static final String GTIN_NUMBER = "12345";
    public static final String PROVIDER_SERIALIZED = "PS";
    public static final String SELF_SERIALIZED = "SS";
    @Mock
    private com.lsntsolutions.gtmApp.service.PropertyService propertyService;

    @Mock
    private WebServiceHelper webServiceHelper;

    @Mock
    private WebService webService;

    @Mock
    private PropertyProvider properyProvider;

    @InjectMocks
    private InputWSHelper inputWSHelper;

    private Property property;

    private Input input;

    private TransaccionesNoConfirmadasWSResult transaccionesNoConfirmadasWSResult;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        property = new Property();
        property.setDaysAgoPendingTransactions(10);
        List<Event> startTraceEvents = new ArrayList<>();
        startTraceEvents.add(new EventBuilder().code(111).build());
        property.setStartTraceConcept(new ConceptBuilder().events(startTraceEvents).build());
    }

    private void getInput(String productType) {
        List<Event> events = new ArrayList<>();
        Agent agent = new AgentBuilder().id(1).build();
        events.add(new EventBuilder().originAgent(agent).code(1).build());
        List<InputDetail> details = new ArrayList<>();
        when(this.propertyService.get()).thenReturn(property);
        InputDetail inputDetail = new InputDetailBuilder().batch("11111").date(new Date()).serial(SERIAL).
                gtin(new ProductGtinBuilder().number(GTIN_NUMBER).build()).product(new ProductBuilder().informAnmat(true).type(productType).build()).build();
        details.add(inputDetail);
        input = new InputBuilder().date(new Date()).concept(new ConceptBuilder().informAnmat(true).events(events).build()).provider(new ProviderBuilder().agent(agent).gln(GLN_ORIGEN).build()).details(details).build();
    }

    @Test
    public void should_be_an_error_tryng_to_get_transactions() throws Exception {
        getInput(PROVIDER_SERIALIZED);
        transaccionesNoConfirmadasWSResult = null;
        OperationResult operationResult = inputWSHelper.sendDrugInformationToAnmat(input);
        Assert.assertTrue(operationResult.getMyOwnErrors().size() == 1);
        Assert.assertFalse(operationResult.getResultado());
    }

    @Test
    public void should_get_transactions_serial_not_inform() throws Exception {
        getInput(PROVIDER_SERIALIZED);
        OperationResult operationResult = inputWSHelper.sendDrugInformationToAnmat(input);
        Assert.assertTrue(operationResult.getMyOwnErrors().size() == 1);
        Assert.assertEquals("GTIN: 12345 Serie: 44444444 SERIE NO INFORMADO", operationResult.getMyOwnErrors().get(0));
        Assert.assertFalse(operationResult.getResultado());
    }

    @Test
    public void should_not_confirm_drug_because_cannot_connect_with_webservice() throws Exception {
        getInput(PROVIDER_SERIALIZED);
        getPendingTransactions();
        OperationResult operationResult = inputWSHelper.sendDrugInformationToAnmat(input);
        Assert.assertTrue(operationResult.getMyOwnErrors().size() == 1);
        Assert.assertEquals("No se ha podido conectar con el Servidor de ANMAT", operationResult.getMyOwnErrors().get(0));
        Assert.assertFalse(operationResult.getResultado());
    }

    @Test
    public void should_confirm_drug() throws Exception {
        getInput(PROVIDER_SERIALIZED);
        getPendingTransactions();
        WebServiceConfirmResult webServiceConfirmResult = new WebServiceConfirmResult();
        webServiceConfirmResult.setResultado(true);
        when(webService.confirmarTransaccion(any(ConfirmacionTransaccionDTO[].class), anyString(), anyString())).thenReturn(webServiceConfirmResult);
        OperationResult operationResult = inputWSHelper.sendDrugInformationToAnmat(input);
        Assert.assertTrue(operationResult.getResultado());
    }

    @Test
    public void should_inform_selfSerialized() throws Exception {
        getInput(SELF_SERIALIZED);
        WebServiceResult webServiceResult = new WebServiceResult();
        webServiceResult.setResultado(true);
        when(webServiceHelper.run(anyList(), anyString(), anyString(), anyList())).thenReturn(webServiceResult);
        OperationResult operationResult = inputWSHelper.sendDrugs(input);
        Assert.assertTrue(operationResult.getResultado());
    }

    private void getPendingTransactions() throws Exception {
        transaccionesNoConfirmadasWSResult = new TransaccionesNoConfirmadasWSResult();
        transaccionesNoConfirmadasWSResult.setCantPaginas(new Long(1));
        transaccionesNoConfirmadasWSResult.setHay_error(false);
        List<TransaccionPlainWS> transaccionPlainWSes = new ArrayList<>();
        TransaccionPlainWS transaccionPlainWS = new TransaccionPlainWS();
        transaccionPlainWS.set_gln_origen(GLN_ORIGEN);
        transaccionPlainWS.set_gtin(StringUtility.addLeadingZeros(GTIN_NUMBER, 14));
        transaccionPlainWS.set_numero_serial(SERIAL);
        transaccionPlainWSes.add(transaccionPlainWS);
        transaccionPlainWS.set_id_transaccion("11111");
        TransaccionPlainWS[] transaccionPlainArray = new TransaccionPlainWS[transaccionPlainWSes.size()];
        transaccionPlainArray = transaccionPlainWSes.toArray(transaccionPlainArray);
        transaccionesNoConfirmadasWSResult.setList(transaccionPlainArray);
        when(webService.getTransaccionesNoConfirmadas(anyString(), anyString(), anyLong(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyString(), anyLong(), anyLong())).thenReturn(transaccionesNoConfirmadasWSResult);
    }

}