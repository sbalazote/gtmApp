package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.model.DeliveryLocation;
import com.lsntsolutions.gtmApp.persistence.dao.ClientDAO;
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
	public List<Client> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortName, String sortTaxId, String sortProvince, String sortIsActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Client.class);

		criteria.createAlias("province", "p");

		if (StringUtility.isInteger(term)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(term)), Restrictions.eq("code",  Integer.parseInt(term))));
		} else {
			criteria.add(Restrictions.or(Restrictions.ilike("name", term, MatchMode.ANYWHERE), Restrictions.ilike("p.name", term, MatchMode.ANYWHERE), Restrictions.ilike("taxId", term, MatchMode.ANYWHERE)));
		}

		if (active != null && Boolean.TRUE.equals(active)) {
			criteria.add(Restrictions.eq("active", true));
		}

		if (sortId != null) {
			if (sortId.equals("asc")) {
				criteria.addOrder(Order.asc("id"));
			} else {
				criteria.addOrder(Order.desc("id"));
			}
		} else if (sortCode != null) {
			if (sortCode.equals("asc")) {
				criteria.addOrder(Order.asc("code"));
			} else {
				criteria.addOrder(Order.desc("code"));
			}
		} else if (sortName != null) {
			if (sortName.equals("asc")) {
				criteria.addOrder(Order.asc("name"));
			} else {
				criteria.addOrder(Order.desc("name"));
			}
		} else if (sortProvince != null) {
			if (sortProvince.equals("asc")) {
				criteria.addOrder(Order.asc("p.name"));
			} else {
				criteria.addOrder(Order.desc("p.name"));
			}
		} else if (sortTaxId != null) {
			if (sortTaxId.equals("asc")) {
				criteria.addOrder(Order.asc("taxId"));
			} else {
				criteria.addOrder(Order.desc("taxId"));
			}
		} else if (sortIsActive != null) {
			if (sortIsActive.equals("asc")) {
				criteria.addOrder(Order.asc("active"));
			} else {
				criteria.addOrder(Order.desc("active"));
			}
		} else {
			criteria.addOrder(Order.asc("id"));
		}

		return (List<Client>) criteria.list();
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