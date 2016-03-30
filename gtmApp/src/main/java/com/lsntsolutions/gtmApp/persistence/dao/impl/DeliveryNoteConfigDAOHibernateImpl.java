package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.DeliveryNoteConfig;
import com.lsntsolutions.gtmApp.persistence.dao.DeliveryNoteConfigDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeliveryNoteConfigDAOHibernateImpl implements DeliveryNoteConfigDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(DeliveryNoteConfig deliveryNoteConfig) {
        this.sessionFactory.getCurrentSession().merge(deliveryNoteConfig);
    }

    @Override
    public DeliveryNoteConfig get(String key) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliveryNoteConfig.class);
        criteria.add(Restrictions.sqlRestriction("`key` = '" + key + "'"));
        return (DeliveryNoteConfig) criteria.uniqueResult();
    }

    @Override
    public List<DeliveryNoteConfig> getAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(DeliveryNoteConfig.class).list();
    }
}