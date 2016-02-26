package com.lsntsolutions.gtmApp.webservice.helper;

import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.util.InputBuilder;
import com.lsntsolutions.gtmApp.webservice.WebService;
import com.lsntsolutions.gtmApp.webservice.WebServiceHelper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-test.xml" })
public class InputWSHelperTest {

    @Mock
    private com.lsntsolutions.gtmApp.service.PropertyService PropertyService;

    @Mock
    private WebServiceHelper webServiceHelper;

    @Mock
    private WebService webService;

    @Mock
    private InputWSHelper inputWSHelper;

    @Test
    public void confirmDrugAndUpdateInput() throws Exception {
        Input input = new InputBuilder().build();
        inputWSHelper.sendDrugInformationToAnmat(input);
    }
}