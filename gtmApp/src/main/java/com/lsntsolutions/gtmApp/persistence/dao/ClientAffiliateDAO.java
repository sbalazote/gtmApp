package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.ClientAffiliate;

public interface ClientAffiliateDAO {
    void save(ClientAffiliate clientAffiliate);

    ClientAffiliate get(Integer id);

    boolean delete(Integer clientAffiliateId);
}
