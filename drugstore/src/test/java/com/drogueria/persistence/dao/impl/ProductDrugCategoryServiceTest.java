package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.drogueria.model.ProductDrugCategory;
import com.drogueria.service.ProductDrugCategoryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-test.xml" })
public class ProductDrugCategoryServiceTest {

	@Autowired
	private ProductDrugCategoryService productDrugCategoryService;

	@Test
	public void save() {
		ProductDrugCategory productDrugCategory = new ProductDrugCategory();
		productDrugCategory.setActive(true);
		productDrugCategory.setCode(123);
		productDrugCategory.setDescription("Test description");
		this.productDrugCategoryService.save(productDrugCategory);

		ProductDrugCategory savedProductDrugCategory = this.productDrugCategoryService.get(productDrugCategory.getId());
		Assert.isTrue(productDrugCategory.getDescription().equals(savedProductDrugCategory.getDescription()));

		this.productDrugCategoryService.delete(productDrugCategory.getId());
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

		for (ProductDrugCategory productDrugCategory : productDrugCategorys) {
			this.productDrugCategoryService.delete(productDrugCategory.getId());
		}
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

		this.productDrugCategoryService.delete(productDrugCategory.getId());
	}

}