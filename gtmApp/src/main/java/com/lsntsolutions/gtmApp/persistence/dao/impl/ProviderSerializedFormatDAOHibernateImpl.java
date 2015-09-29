package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProviderSerializedFormat;
import com.lsntsolutions.gtmApp.persistence.dao.ProviderSerializedFormatDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
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
}
