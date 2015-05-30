package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.AgreementTransfer;
import com.drogueria.persistence.dao.AgreementTransferDAO;

@Repository
public class AgreementTransferDAOHibernateImpl implements AgreementTransferDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(AgreementTransfer agreementTransfer) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(agreementTransfer);
	}

	@Override
	public AgreementTransfer get(Integer id) {
		return (AgreementTransfer) this.sessionFactory.getCurrentSession().get(AgreementTransfer.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AgreementTransfer> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(AgreementTransfer.class).list();
	}

}
