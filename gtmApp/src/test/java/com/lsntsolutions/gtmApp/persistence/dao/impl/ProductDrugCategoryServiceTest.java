package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.lsntsolutions.gtmApp.model.ProductDrugCategory;
import com.lsntsolutions.gtmApp.service.ProductDrugCategoryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class ProductDrugCategoryServiceTest {

	@Autowired
	private ProductDrugCategoryService productDrugCategoryService;

	@Before
	public void setUp() {

	}

	@After
	public void tearDown() throws Exception {
		List<ProductDrugCategory> productDrugCategories = this.productDrugCategoryService.getAll();
		for (ProductDrugCategory productDrugCategory : productDrugCategories) {
			this.productDrugCategoryService.delete(productDrugCategory.getId());
		}
	}

	@Test
	public void save() {
		ProductDrugCategory productDrugCategory = new ProductDrugCategory();
		productDrugCategory.setActive(true);
		productDrugCategory.setCode(123);
		productDrugCategory.setDescription("Test description");
		this.productDrugCategoryService.save(productDrugCategory);

		ProductDrugCategory savedProductDrugCategory = this.productDrugCategoryService.get(productDrugCategory.getId());
		Assert.isTrue(productDrugCategory.getDescription().equals(savedProductDrugCategory.getDescription()));
	}

	@Test
	public void getAll() {
		ProductDrugCategory productDrugCategory1 = new ProductDrugCategory();
		productDrugCategory1.setActive(true);
		productDrugCategory1.setCode(123);
		productDrugCategory1.setDescription("Test description");
		this.productDrugCategoryService.save(productDrugCategory1);

		ProductDrugCategory productDrugCategory2 = new ProductDrugCategory();
		productDrugCategory2.setActive(true);
		productDrugCategory2.setCode(456);
		productDrugCategory2.setDescription("Test description 2");
		this.productDrugCategoryService.save(productDrugCategory2);

		List<ProductDrugCategory> productDrugCategorys = this.productDrugCategoryService.getAll();

		Assert.isTrue(productDrugCategorys.size() == 2);
	}

	@Test
	public void exists() {
		ProductDrugCategory productDrugCategory = new ProductDrugCategory();
		productDrugCategory.setActive(true);
		productDrugCategory.setCode(123);
		productDrugCategory.setDescription("Test description");
		this.productDrugCategoryService.save(productDrugCategory);

		Boolean isTrue = this.productDrugCategoryService.exists(productDrugCategory.getCode());
		Assert.isTrue(isTrue == true);

		Boolean isFalse = this.productDrugCategoryService.exists(999);
		Assert.isTrue(isFalse == false);
	}

}
