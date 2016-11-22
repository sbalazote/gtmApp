package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.model.ClientAffiliate;
import com.lsntsolutions.gtmApp.persistence.dao.ClientAffiliateDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClientAffiliateDAOHibernateImpl implements ClientAffiliateDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(ClientAffiliate clientAffiliate) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(clientAffiliate);
    }

    @Override
    public ClientAffiliate get(Integer id) {
        return (ClientAffiliate) this.sessionFactory.getCurrentSession().get(ClientAffiliate.class, id);
    }

    @Override
    public boolean delete(Integer clientAffiliateId) {
        ClientAffiliate clientAffiliate = this.get(clientAffiliateId);
        if (clientAffiliate != null) {
            this.sessionFactory.getCurrentSession().delete(clientAffiliate);
            return true;
        } else {
            return false;
        }
    }
}
