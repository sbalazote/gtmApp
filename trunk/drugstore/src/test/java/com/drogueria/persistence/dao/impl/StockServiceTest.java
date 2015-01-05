package com.drogueria.persistence.dao.impl;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.drogueria.model.Agreement;
import com.drogueria.model.Product;
import com.drogueria.model.ProductBrand;
import com.drogueria.model.ProductDrugCategory;
import com.drogueria.model.ProductGroup;
import com.drogueria.model.ProductMonodrug;
import com.drogueria.model.Stock;
import com.drogueria.service.AgreementService;
import com.drogueria.service.ProductBrandService;
import com.drogueria.service.ProductDrugCategoryService;
import com.drogueria.service.ProductGroupService;
import com.drogueria.service.ProductMonodrugService;
import com.drogueria.service.ProductService;
import com.drogueria.service.StockService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-test.xml" })
public class StockServiceTest {

	@Autowired
	private StockService stockService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private ProductBrandService productBrandService;
	@Autowired
	private ProductDrugCategoryService productDrugCategoryService;
	@Autowired
	private ProductGroupService productGroupService;
	@Autowired
	private ProductMonodrugService productMonodrugService;

	@Test
	public void save() {
		Stock stock = new Stock();
		Agreement agreement = new Agreement();
		agreement.setActive(true);
		agreement.setCode(5656);
		agreement.setDescription("Test Agreement");
		this.agreementService.save(agreement);
		stock.setAgreement(agreement);
		stock.setAmount(10);
		stock.setBatch("abcd");
		stock.setExpirationDate(new Date());
		Product product = new Product();
		product.setActive(true);

		ProductBrand productBrand = new ProductBrand();
		productBrand.setActive(true);
		productBrand.setCode(4546);
		productBrand.setDescription("Test Product Brand Description");
		this.productBrandService.save(productBrand);

		product.setBrand(productBrand);
		product.setCode(133623);
		product.setCold(false);
		product.setDescription("Test product");

		ProductDrugCategory productDrugCategory = new ProductDrugCategory();
		productDrugCategory.setActive(true);
		productDrugCategory.setDescription("Test Product Drug Category Description");
		productDrugCategory.setCode(3344);
		this.productDrugCategoryService.save(productDrugCategory);
		product.setDrugCategory(productDrugCategory);

		ProductGroup productGroup = new ProductGroup();
		productGroup.setActive(true);
		productGroup.setDescription("Test Product Group Description");
		productGroup.setCode(4444);
		this.productGroupService.save(productGroup);

		product.setGroup(productGroup);
		product.setInformAnmat(false);
		ProductMonodrug productMonodrug = new ProductMonodrug();
		productMonodrug.setActive(true);
		productMonodrug.setDescription("Test Product Brand Description");
		productMonodrug.setCode(2222);
		this.productMonodrugService.save(productMonodrug);
		product.setMonodrug(productMonodrug);

		product.setType("SS");
		this.productService.save(product);

		stock.setBatch("aabcd");
		stock.setExpirationDate(new Date());
		stock.setProduct(product);
		stock.setSerialNumber("1656565465");
		this.stockService.save(stock);

		Stock savedStock = this.stockService.get(stock.getId());
		Assert.isTrue(stock.getBatch().equals(savedStock.getBatch()));

		Assert.isTrue(Long.valueOf(stock.getAmount()) == this.stockService.getProductAmount(product.getId(), agreement.getId(), null));

		// Assert.isTrue(product.getId().equals(this.stockService.getByGtin(product.getGtin(), agreement.getId())));
	}
}
