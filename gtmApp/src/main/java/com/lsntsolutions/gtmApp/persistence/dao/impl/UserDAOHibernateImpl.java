package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.persistence.dao.UserDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lsntsolutions.gtmApp.model.User;

@Repository
public class UserDAOHibernateImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(User user) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(user);
	}

	@Override
	public User get(Integer id) {
		return (User) this.sessionFactory.getCurrentSession().get(User.class, id);
	}

	@Override
	public User getByName(String name) {
		try {
			Query query = this.sessionFactory.getCurrentSession().createQuery("from User where name = :name");
			query.setParameter("name", name);
			return (User) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public Boolean exists(String name) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from User where name = :name");
		query.setParameter("name", name);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getForAutocomplete(String term, Boolean active) {
		String sentence = "from User where (name like :name";
		sentence += ")";

		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and active = true";
		}

		Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("name", "%" + term + "%");

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(User.class).list();
	}

	@Override
	public boolean delete(Integer userId) {
		User user = this.get(userId);
		if (user != null) {
			this.sessionFactory.getCurrentSession().delete(user);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from User").uniqueResult();
		return count;
	}

}