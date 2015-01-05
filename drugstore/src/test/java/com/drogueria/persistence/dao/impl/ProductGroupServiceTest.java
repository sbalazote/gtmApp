package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.drogueria.model.ProductGroup;
import com.drogueria.service.ProductGroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-test.xml" })
public class ProductGroupServiceTest {

	@Autowired
	private ProductGroupService productGroupService;

	@Test
	public void save() {
		ProductGroup productGroup = new ProductGroup();
		productGroup.setActive(true);
		productGroup.setCode(123);
		productGroup.setDescription("Test description");
		this.productGroupService.save(productGroup);

		ProductGroup savedProductGroup = this.productGroupService.get(productGroup.getId());
		Assert.isTrue(productGroup.getDescription().equals(savedProductGroup.getDescription()));

		this.productGroupService.delete(productGroup.getId());
	}

	@Test
	public void getAll() {
		ProductGroup productGroup1 = new ProductGroup();
		productGroup1.setActive(true);
		productGroup1.setCode(123);
		productGroup1.setDescription("Test description");
		this.productGroupService.save(productGroup1);

		ProductGroup productGroup2 = new ProductGroup();
		productGroup2.setActive(true);
		productGroup2.setCode(456);
		productGroup2.setDescription("Test description 2");
		this.productGroupService.save(productGroup2);

		List<ProductGroup> productGroups = this.productGroupService.getAll();

		Assert.isTrue(productGroups.size() == 2);

		for (ProductGroup productGroup : productGroups) {
			this.productGroupService.delete(productGroup.getId());
		}
	}

	@Test
	public void exists() {
		ProductGroup productGroup = new ProductGroup();
		productGroup.setActive(true);
		productGroup.setCode(123);
		productGroup.setDescription("Test description");
		this.productGroupService.save(productGroup);

		Boolean isTrue = this.productGroupService.exists(productGroup.getCode());
		Assert.isTrue(isTrue == true);

		Boolean isFalse = this.productGroupService.exists(999);
		Assert.isTrue(isFalse == false);

		this.productGroupService.delete(productGroup.getId());
	}

}
