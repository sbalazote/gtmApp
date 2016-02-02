package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProviderSerializedFormat;
import com.lsntsolutions.gtmApp.persistence.dao.ProviderSerializedFormatDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderSerializedFormatDAOHibernateImpl implements ProviderSerializedFormatDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProviderSerializedFormat providerSerializedFormat) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(providerSerializedFormat);
	}

	@Override
	public ProviderSerializedFormat get(Integer id) {
		return (ProviderSerializedFormat) this.sessionFactory.getCurrentSession().get(ProviderSerializedFormat.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderSerializedFormat> getAll() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from ProviderSerializedFormat");
		return query.list();
	}

	@Override
	public boolean delete(Integer id) {
		ProviderSerializedFormat providerSerializedFormat = this.get(id);
		if (providerSerializedFormat != null) {
			this.sessionFactory.getCurrentSession().delete(providerSerializedFormat);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean exists(ProviderSerializedFormat providerSerializedFormat) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProviderSerializedFormat.class);

		if (providerSerializedFormat.getGtinLength() != null) {
			criteria.add(Restrictions.eq("gtinLength", providerSerializedFormat.getGtinLength()));
		}
		if (providerSerializedFormat.getSerialNumberLength() != null) {
			criteria.add(Restrictions.eq("serialNumberLength", providerSerializedFormat.getSerialNumberLength()));
		}

		if (providerSerializedFormat.getExpirationDateLength() != null) {
			criteria.add(Restrictions.eq("expirationDateLength", providerSerializedFormat.getExpirationDateLength()));
		}

		if (providerSerializedFormat.getBatchLength() != null) {
			criteria.add(Restrictions.eq("batchLength", providerSerializedFormat.getBatchLength()));
		}

		if (providerSerializedFormat.getSequence() != null) {
			criteria.add(Restrictions.eq("sequence", providerSerializedFormat.getSequence()));
		}

		return !criteria.list().isEmpty();
	}
}
