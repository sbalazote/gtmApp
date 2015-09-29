package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.Stock;
import com.lsntsolutions.gtmApp.persistence.dao.StockDAO;
import com.lsntsolutions.gtmApp.query.StockQuery;
import com.lsntsolutions.gtmApp.service.ProductService;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class StockDAOHibernateImpl implements StockDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProductService productService;

	@Override
	public void save(Stock stock) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(stock);
	}

	@Override
	public Stock get(Integer id) {
		return (Stock) this.sessionFactory.getCurrentSession().get(Stock.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Stock> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Stock.class).list();
	}

	@Override
	public void delete(Stock stock) {
		this.sessionFactory.getCurrentSession().delete(stock);
	}

	@Override
	public Stock getSerializedProductStock(Integer productId, String serialNumber, String gtin, Integer agreementId) {
		try {
			String sentence = "from Stock where product.id = :productId and serialNumber = :serialNumber and agreement.id = :agreementId ";
			// TODO validar si esto esta bien.
			Product product = this.productService.get(productId);
			if (gtin != null && product.getType().equals("PS")) {
				if (gtin != "") {
					sentence += "and gtin.number = :gtin";
				}
			}

			Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
			query.setParameter("productId", productId);
			query.setParameter("serialNumber", serialNumber);
			query.setParameter("agreementId", agreementId);

			if (gtin != null && product.getType().equals("PS")) {
				if (gtin != "") {
					query.setParameter("gtin", gtin);
				}
			}

			return (Stock) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public Stock getSerializedProductStock(String serialNumber, Integer agreementId) {
		try {
			Query query = this.sessionFactory.getCurrentSession().createQuery("from Stock where agreement.id = :agreementId and serialNumber = :serialNumber");
			query.setParameter("agreementId", agreementId);
			query.setParameter("serialNumber", serialNumber);

			return (Stock) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public Stock getBatchExpirationDateStockForUpdate(Integer productId, String batch, Date expirationDate, Integer agreementId) {
		try {
			String queryStr = "from Stock where product.id = :productId and batch = :batch and expirationDate = :expirationDate and agreement.id = :agreementId";
			Query query = this.sessionFactory.getCurrentSession().createQuery(queryStr).setLockOptions(LockOptions.UPGRADE);
			query.setParameter("productId", productId);
			query.setParameter("batch", batch);
			query.setParameter("expirationDate", expirationDate);
			query.setParameter("agreementId", agreementId);
			return (Stock) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Stock> getBatchExpirationDateStock(Integer productId, Integer agreementId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(
				"from Stock where product.id = :productId and agreement.id = :agreementId and serialNumber is null order by expirationDate asc");
		query.setParameter("productId", productId);
		query.setParameter("agreementId", agreementId);
		return query.list();
	}

	@Override
	public Long getProductAmount(Integer productId, Integer agreementId, Integer provisioningId) {
		Long amount = 0L;

		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery(
				"select sum(amount) from Stock where product.id = :productId and agreement.id = :agreementId");
		query.setParameter("productId", productId);
		query.setParameter("agreementId", agreementId);

		Long stock = ((Long) query.uniqueResult());
		amount += (stock != null) ? stock : 0;

		String sentence = "select sum(d.amount) from ProvisioningRequest as p inner join p.provisioningRequestDetails as d"
				+ " where d.product.id = :productId and p.agreement.id = :agreementId and (p.state.id = 1 or p.state.id = 2 or p.state.id = 3)";
		if (provisioningId != null) {
			sentence += " and p.id <> :provisioningId";
		}

		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("productId", productId);
		query.setParameter("agreementId", agreementId);
		if (provisioningId != null) {
			query.setParameter("provisioningId", provisioningId);
		}

		Long reservedAmount = ((Long) query.uniqueResult());
		amount -= (reservedAmount != null) ? reservedAmount : 0;

		return amount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Stock> getStockForSearch(StockQuery stockQuery) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Stock.class);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateFromFormated = null;
		Date dateToFormated = null;

		if (!StringUtils.isEmpty(stockQuery.getExpirateDateFrom())) {
			try {
				dateFromFormated = dateFormatter.parse(stockQuery.getExpirateDateFrom());
				criteria.add(Restrictions.ge("expirationDate", dateFromFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}
		if (!StringUtils.isEmpty(stockQuery.getExpirateDateTo())) {
			try {
				dateToFormated = dateFormatter.parse(stockQuery.getExpirateDateTo());
				criteria.add(Restrictions.le("expirationDate", dateToFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}

		if (stockQuery.getProductId() != null) {
			criteria.add(Restrictions.eq("product.id", stockQuery.getProductId()));
		}

		if (stockQuery.getAgreementId() != null) {
			criteria.add(Restrictions.eq("agreement.id", stockQuery.getAgreementId()));
		}

		if (!StringUtils.isEmpty(stockQuery.getSerialNumber())) {
			criteria.add(Restrictions.eq("serialNumber", stockQuery.getSerialNumber()));
		}

		if (!StringUtils.isEmpty(stockQuery.getBatchNumber())) {
			criteria.add(Restrictions.eq("batch", stockQuery.getBatchNumber()));
		}

		List<Stock> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountStockSearch(StockQuery stockQuery) {
		if (this.getStockForSearch(stockQuery).size() < Constants.QUERY_MAX_RESULTS) {
			return true;
		} else {
			return false;
		}
	}

	// TODO cambiar cuando se agregue el gtin_id en stock
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getForAutocomplete(String term, Integer agreementId) {
		String sentence = "select distinct (p) from Stock as s inner join s.product as p where (p.description like :description or p.brand.description like :brand or p.monodrug.description like :monodrug";
		if (StringUtility.isInteger(term)) {
			sentence += " or convert(p.code, CHAR) like :code";
		}
		sentence += ")";
		if (agreementId != null) {
			sentence += " and s.agreement.id = :agreementId";
		}

		Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("description", "%" + term + "%");
		query.setParameter("brand", "%" + term + "%");
		query.setParameter("monodrug", "%" + term + "%");
		if (agreementId != null) {
			query.setParameter("agreementId", agreementId);
		}
		if (StringUtility.isInteger(term)) {
			query.setParameter("code", "%" + term + "%");
		}
		return query.list();
	}

	// TODO cambiar cuando se agregue el gtin_id en stock
	@Override
	public Product getByGtin(String gtin, Integer agreementId) {
		try {
			String sentence = "select distinct (p) from Stock as s inner join s.product as p inner join p.gtins g "
					+ "where (g.number = :gtin and p.active = true)";

			if (agreementId != null) {
				sentence += " and s.agreement.id = :agreementId";
			}
			Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
			query.setParameter("gtin", StringUtility.removeLeadingZero(gtin));
			if (agreementId != null) {
				query.setParameter("agreementId", agreementId);
			}
			return (Product) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public boolean delete(Integer stockId) {
		Stock stock = this.get(stockId);
		if (stock != null) {
			this.sessionFactory.getCurrentSession().delete(stock);
			return true;
		} else {
			return false;
		}
	}
}
