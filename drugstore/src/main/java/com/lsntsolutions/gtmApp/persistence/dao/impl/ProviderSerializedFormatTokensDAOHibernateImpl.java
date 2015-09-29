package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProviderSerializedFormatTokens;
import com.lsntsolutions.gtmApp.persistence.dao.ProviderSerializedFormatTokensDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderSerializedFormatTokensDAOHibernateImpl implements ProviderSerializedFormatTokensDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProviderSerializedFormatTokens providerSerializedFormatTokens) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(providerSerializedFormatTokens);
	}

	@Override
	public ProviderSerializedFormatTokens get(Integer id) {
		return (ProviderSerializedFormatTokens) this.sessionFactory.getCurrentSession().get(ProviderSerializedFormatTokens.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderSerializedFormatTokens> getAll() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from ProviderSerializedFormatTokens");
		return query.list();
	}
}
