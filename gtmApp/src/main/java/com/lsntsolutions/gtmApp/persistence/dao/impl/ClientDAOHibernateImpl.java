package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.model.DeliveryLocation;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lsntsolutions.gtmApp.persistence.dao.ClientDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;

@Repository
public class ClientDAOHibernateImpl implements ClientDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Client client) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(client);
	}

	@Override
	public Client get(Integer id) {
		return (Client) this.sessionFactory.getCurrentSession().get(Client.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Client where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Client> getForAutocomplete(String term, Boolean active) {
		String sentence = "from Client where (taxId like :taxId or corporateName like :corporateName or locality like :locality";
		if (StringUtility.isInteger(term)) {
			sentence += " or convert(code, CHAR) like :code";
		}
		sentence += ")";
		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and active = true";
		}

		Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("taxId", "%" + term + "%");
		query.setParameter("corporateName", "%" + term + "%");
		query.setParameter("locality", "%" + term + "%");

		if (StringUtility.isInteger(term)) {
			query.setParameter("code", "%" + term + "%");
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Client> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Client.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Client> getAllActives() {
		return this.sessionFactory.getCurrentSession().createQuery("from Client where active = true").list();
	}

	@Override
	public boolean delete(Integer clientId) {
		Client client = this.get(clientId);
		if (client != null) {
			client.getDeliveryLocations().clear();
			this.sessionFactory.getCurrentSession().delete(client);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Client> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Client.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from Client").uniqueResult();
		return count;
	}

	@Override
	public List<DeliveryLocation> getDeliveriesLocations(Integer clientId) {
		String sentence = "select distinct dl from Client as c inner join c.deliveryLocations dl where c.id = :clientId order by dl.name";

		Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("clientId", clientId);
		return query.list();
	}
}