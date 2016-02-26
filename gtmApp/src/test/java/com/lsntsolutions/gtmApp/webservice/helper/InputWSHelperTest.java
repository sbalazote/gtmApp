package com.lsntsolutions.gtmApp.webservice.helper;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.Provider;
import com.lsntsolutions.gtmApp.util.InputBuilder;
import com.lsntsolutions.gtmApp.util.OperationResult;
import com.lsntsolutions.gtmApp.webservice.WebService;
import com.lsntsolutions.gtmApp.webservice.WebServiceHelper;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class InputWSHelperTest {

    @Mock
    private com.lsntsolutions.gtmApp.service.PropertyService PropertyService;

    @Mock
    private WebServiceHelper webServiceHelper;

    @Mock
    private WebService webService;

    @InjectMocks
    private InputWSHelper inputWSHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_not_confirm_drug() throws Exception {
        Input input = new InputBuilder().concept(new Concept()).build();
        OperationResult operationResult = inputWSHelper.sendDrugInformationToAnmat(input);
        Assert.assertFalse(operationResult.getResultado());
    }
}