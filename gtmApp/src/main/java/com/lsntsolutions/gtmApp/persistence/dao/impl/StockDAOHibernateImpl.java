package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.dto.ProviderSerializedProductDTO;
import com.lsntsolutions.gtmApp.dto.ProviderSerializedSerialFormatDTO;
import com.lsntsolutions.gtmApp.dto.StockDTO;
import com.lsntsolutions.gtmApp.helper.SerialParser;
import com.lsntsolutions.gtmApp.helper.StockDTOTotalAmountComparator;
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
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository
public class StockDAOHibernateImpl implements StockDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProductService productService;

	@Autowired
	private SerialParser serialParser;

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
	public Stock getSerializedProductStock(Integer productId, String serialNumber, String batch, String expirationDate, String gtin, Integer agreementId) {
		try {
			Product product = null;
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Stock.class, "stock");
			if(productId != null) {
				product = this.productService.get(productId);
				criteria.add(Restrictions.eq("product.id", productId));
			}
			criteria.add(Restrictions.eq("serialNumber", serialNumber));
			if (!StringUtils.isEmpty(expirationDate)) {
				try {
					SimpleDateFormat dateFormatter;
					dateFormatter = (expirationDate.length() == 6) ? new SimpleDateFormat("yyMMdd") : new SimpleDateFormat("yyyyMMdd");
					Date expirationDateParsed = dateFormatter.parse(expirationDate);
					criteria.add(Restrictions.eq("expirationDate", expirationDateParsed));
				} catch (ParseException e) {
					throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
				}
			}
			if (!StringUtils.isEmpty(batch)) {
				criteria.add(Restrictions.eq("batch", batch));
			}
			if(agreementId != null) {
				criteria.add(Restrictions.eq("agreement.id", agreementId));
			}
			criteria.createAlias("stock.product", "product");
			criteria.createAlias("product.gtins", "gtin");

			if (gtin != null && product != null) {
				if (!gtin.isEmpty() && product.getType().equals("PS")) {
					criteria.add(Restrictions.eq("gtin.number", gtin));
				}
			}
			return (Stock) criteria.list().get(0);
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
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Stock.class, "stock");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateFromFormated;
		Date dateToFormated;
		criteria.createAlias("stock.product", "product");
		criteria.createAlias("stock.gtin", "gtin");

		if (stockQuery.getSearchPhrase() != null) {
			criteria.add(Restrictions.or(Restrictions.ilike("gtin.number", stockQuery.getSearchPhrase(), MatchMode.ANYWHERE), Restrictions.ilike("product.description", stockQuery.getSearchPhrase(), MatchMode.ANYWHERE)));
		}

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

		if (stockQuery.getMonodrugId() != null) {
			criteria.add(Restrictions.eq("product.monodrug.id", stockQuery.getMonodrugId()));
		}

		List<Stock> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountStockSearch(StockQuery stockQuery) {
		return this.getStockForSearch(stockQuery).size() < Constants.QUERY_MAX_RESULTS;
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

	@Override
	public List<StockDTO> getForAutocomplete(String searchPhrase, String sortCode, String sortProduct, String sortAgreement, String sortGtin, String sortAmount, String agreementId, String batchNumber, String expirateDateFrom, String expirateDateTo, String monodrugId, String productId, String serialNumber) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Stock.class, "stock");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFromFormated;
		Date dateToFormated;
		criteria.createAlias("stock.product", "product");
		criteria.createAlias("stock.agreement", "agreement");
		criteria.createAlias("stock.gtin", "gtin");

		criteria.add(Restrictions.or(Restrictions.ilike("gtin.number", searchPhrase, MatchMode.ANYWHERE), Restrictions.ilike("product.description", searchPhrase, MatchMode.ANYWHERE)));

		if (!StringUtils.isEmpty(expirateDateFrom)) {
			try {
				dateFromFormated = dateFormatter.parse(expirateDateFrom);
				criteria.add(Restrictions.sqlRestriction("expiration_date >= '" + sqlDateFormatter.format(dateFromFormated) + "'"));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}
		if (!StringUtils.isEmpty(expirateDateTo)) {
			try {
				dateToFormated = dateFormatter.parse(expirateDateTo);
				criteria.add(Restrictions.sqlRestriction("expiration_date <= '" + sqlDateFormatter.format(dateToFormated) + "'"));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}

		if (!StringUtils.isEmpty(productId)) {
			criteria.add(Restrictions.eq("product.id", Integer.parseInt(productId)));
		}

		if (!StringUtils.isEmpty(agreementId)) {
			criteria.add(Restrictions.eq("agreement.id", Integer.parseInt(agreementId)));
		}

		if (!StringUtils.isEmpty(serialNumber)) {
			criteria.add(Restrictions.eq("serialNumber", serialNumber));
		}

		if (!StringUtils.isEmpty(batchNumber)) {
			criteria.add(Restrictions.eq("batch", batchNumber));
		}

		if (!StringUtils.isEmpty(monodrugId)) {
			criteria.add(Restrictions.eq("product.monodrug.id", Integer.parseInt(monodrugId)));
		}

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("product.id"), "productId");
		projectionList.add(Projections.groupProperty("agreement.id"), "agreementId");
		projectionList.add(Projections.property("product.code"), "productCode");
		projectionList.add(Projections.property("product.description"), "productDescription");
		projectionList.add(Projections.property("agreement.description"), "agreementDescription");
		projectionList.add(Projections.property("gtin.number"), "gtinNumber");
		projectionList.add(Projections.property("serialNumber"), "serialNumber");
		projectionList.add(Projections.sqlProjection("sum(amount) as totalAmount", new String[]{"totalAmount"}, new Type[]{StandardBasicTypes.INTEGER}));


		criteria.setProjection(projectionList);

		if (sortCode != null) {
			if (sortCode.equals("asc")) {
				criteria.addOrder(Order.asc("product.code"));
			} else {
				criteria.addOrder(Order.desc("product.code"));
			}
		} else if (sortProduct != null) {
			if (sortProduct.equals("asc")) {
				criteria.addOrder(Order.asc("product.description"));
			} else {
				criteria.addOrder(Order.desc("product.description"));
			}
		} else if (sortAgreement != null) {
			if (sortAgreement.equals("asc")) {
				criteria.addOrder(Order.asc("agreement.description"));
			} else {
				criteria.addOrder(Order.desc("agreement.description"));
			}
		} else if (sortGtin != null) {
			if (sortGtin.equals("asc")) {
				criteria.addOrder(Order.asc("gtin.number"));
			} else {
				criteria.addOrder(Order.desc("gtin.number"));
			}
		} else {
			criteria.addOrder(Order.asc("product.id"));
		}

		criteria.setResultTransformer(Transformers.aliasToBean(StockDTO.class));

		List<StockDTO> results = (List<StockDTO>) criteria.list();

        if (sortAmount != null) {
            StockDTOTotalAmountComparator stockDTOTotalAmountComparator = new StockDTOTotalAmountComparator();
            if (sortAmount.equals("asc")) {
                Collections.sort(results, stockDTOTotalAmountComparator);
            } else {
                Collections.sort(results, Collections.reverseOrder(stockDTOTotalAmountComparator));
            }
        }


		return results;
	}

	@Override
	public Stock getStockByParseSerial(Integer productId, String serialNumber, Integer agreementId) {
		List<ProviderSerializedSerialFormatDTO> providerSerializedFormatMatchedDTOs = serialParser.getMatchParsers(serialNumber);
		for(ProviderSerializedSerialFormatDTO providerSerializedSerialFormatDTO : providerSerializedFormatMatchedDTOs){
			ProviderSerializedProductDTO providerSerializedProductDTO = serialParser.parse(serialNumber, providerSerializedSerialFormatDTO.getProviderSerializedFormatId());
			Stock stock = this.getSerializedProductStockByAll(providerSerializedProductDTO.getSerialNumber(), agreementId, productId, providerSerializedProductDTO.getBatch(), providerSerializedProductDTO.getExpirationDate(), providerSerializedProductDTO.getGtin());
			if(stock != null){
				return stock;
			}
		}
		return null;

	}

	public Stock getSerializedProductStockByAll(String serialNumber, Integer agreementId, Integer productId, String batch, String expirateDate, String gtin) {
		try {
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Stock.class, "stock");
			criteria.createAlias("stock.gtin", "gtin");

			Date expirationDate;

			criteria.add(Restrictions.eq("product.id", productId));
			criteria.add(Restrictions.eq("agreement.id", agreementId));
			criteria.add(Restrictions.eq("gtin.number", gtin));
			criteria.add(Restrictions.eq("serialNumber", serialNumber));

			if (!StringUtils.isEmpty(expirateDate)) {
				try {
					SimpleDateFormat dateFormatter;
					dateFormatter = (expirateDate.length() == 6) ? new SimpleDateFormat("yyMMdd") : new SimpleDateFormat("yyyyMMdd");
					expirationDate = dateFormatter.parse(expirateDate);
					criteria.add(Restrictions.eq("expirationDate", expirationDate));
				} catch (ParseException e) {
					throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
				}
			}
			if (!StringUtils.isEmpty(batch)) {
				criteria.add(Restrictions.eq("batch", batch));
			}

			return (Stock) criteria.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}