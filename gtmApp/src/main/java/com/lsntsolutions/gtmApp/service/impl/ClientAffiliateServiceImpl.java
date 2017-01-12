package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.model.ClientAffiliate;
import com.lsntsolutions.gtmApp.persistence.dao.ClientAffiliateDAO;
import com.lsntsolutions.gtmApp.service.ClientAffiliateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientAffiliateServiceImpl implements ClientAffiliateService {

    @Autowired
    private ClientAffiliateDAO clientAffiliateDAO;

    @Override
    public void save(ClientAffiliate clientAffiliate) {
        this.clientAffiliateDAO.save(clientAffiliate);
    }

    @Override
    public ClientAffiliate get(Integer id) {
        return this.clientAffiliateDAO.get(id);
    }

    @Override
    public boolean delete(Integer clientAffiliateId) {
        return this.clientAffiliateDAO.delete(clientAffiliateId);
    }
}
