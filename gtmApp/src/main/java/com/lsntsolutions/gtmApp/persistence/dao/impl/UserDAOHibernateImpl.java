package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.User;
import com.lsntsolutions.gtmApp.persistence.dao.UserDAO;
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
	public List<User> getForAutocomplete(String searchPhrase, Boolean active, String sortId, String sortName, String sortIsActive, String sortProfile) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(User.class, "user");
		criteria.createAlias("user.profile", "profile");
		if (StringUtility.isInteger(searchPhrase)) {
			criteria.add(Restrictions.eq("id", Integer.parseInt(searchPhrase)));
		} else {
			criteria.add(Restrictions.or(Restrictions.ilike("name", searchPhrase, MatchMode.ANYWHERE), Restrictions.ilike("profile.description", searchPhrase, MatchMode.ANYWHERE)));
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
		} else if (sortName != null) {
			if (sortName.equals("asc")) {
				criteria.addOrder(Order.asc("name"));
			} else {
				criteria.addOrder(Order.desc("name"));
			}
		}  else if (sortIsActive != null) {
			if (sortIsActive.equals("asc")) {
				criteria.addOrder(Order.asc("active"));
			} else {
				criteria.addOrder(Order.desc("active"));
			}
		}else if (sortProfile != null) {
				if (sortProfile.equals("asc")) {
					criteria.addOrder(Order.asc("profile.description"));
				} else {
					criteria.addOrder(Order.desc("profile.description"));
				}
		}else {
			criteria.addOrder(Order.asc("id"));
		}

		return (List<User>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(User.class).addOrder(Order.asc("name")).list();
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