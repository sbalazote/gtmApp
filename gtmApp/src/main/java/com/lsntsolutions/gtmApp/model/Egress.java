package com.lsntsolutions.gtmApp.model;

import java.util.Date;
import java.util.List;

/**
 * Created by a983060 on 03/12/2015.
 */
public interface Egress {

    Integer getId();
    String getFormatId();
    Agreement getAgreement();
    Date getDate();
    List getDetails();
    String getName();
    boolean hasToInformANMAT();
}
