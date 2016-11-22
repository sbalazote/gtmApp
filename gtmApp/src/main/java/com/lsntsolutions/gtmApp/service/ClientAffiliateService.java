package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.ClientAffiliate;

/**
 * Created by lucho on 21/11/2016.
 */
public interface ClientAffiliateService {

    void save(ClientAffiliate clientAffiliate);

    ClientAffiliate get(Integer id);

    boolean delete(Integer clientAffiliateId);
}
