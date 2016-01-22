package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Profile;
import com.lsntsolutions.gtmApp.persistence.dao.ProfileDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfileDAOHibernateImpl implements ProfileDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Profile profile) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(profile);
    }

    @Override
    public Profile get(Integer id) {
        return (Profile) this.sessionFactory.getCurrentSession().get(Profile.class, id);
    }

    @Override
    public Boolean exists(String description) {
        Query query = this.sessionFactory.getCurrentSession().createQuery("from Profile where description = :description");
        query.setParameter("description", description);
        return !query.list().isEmpty();
    }

    @Override
    public List<Profile> getForAutocomplete(String term, Boolean active, String sortId, String sortDescription) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Profile.class);
        if(!term.matches("")) {
            if (StringUtility.isInteger(term)) {
                criteria.add(Restrictions.eq("id", Integer.parseInt(term)));
            } else {
                criteria.add( Restrictions.ilike("description", term, MatchMode.ANYWHERE));
            }
        }

        if (active != null && Boolean.TRUE.equals(active)) {
            criteria.add(Restrictions.eq("active", "true"));
        }
        if (sortId != null) {
            if (sortId.equals("asc")) {
                criteria.addOrder(Order.asc("id"));
            } else {
                criteria.addOrder(Order.desc("id"));
            }
        } else if (sortDescription != null) {
            if (sortDescription.equals("asc")) {
                criteria.addOrder(Order.asc("description"));
            } else {
                criteria.addOrder(Order.desc("description"));
            }
        }else {
            criteria.addOrder(Order.asc("id"));
        }
        return (List<Profile>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Profile> getAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(Profile.class).list();
    }

    @Override
    public boolean delete(Integer userId) {
        Profile profile = this.get(userId);
        if (profile != null) {
            this.sessionFactory.getCurrentSession().delete(profile);
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Profile> getPaginated(int start, int length) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Profile.class);
        criteria.setMaxResults(length);
        criteria.setFirstResult(start);
        return criteria.list();
    }

    @Override
    public long getTotalNumber() {
        Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from Profile").uniqueResult();
        return count;
    }

}
