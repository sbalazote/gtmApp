package com.drogueria.persistence.dao.impl;

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

import com.drogueria.model.ProductMonodrug;
import com.drogueria.service.ProductMonodrugService;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-test.xml" })
public class ProductMonodrugServiceTest {

	@Autowired
	private ProductMonodrugService productMonodrugService;

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() throws Exception {
		List<ProductMonodrug> productMonodrugs = this.productMonodrugService.getAll();

		for (ProductMonodrug productMonodrug : productMonodrugs) {
			this.productMonodrugService.delete(productMonodrug.getId());
		}
	}

	@Test
	public void save() {
		ProductMonodrug productMonodrug = new ProductMonodrug();
		productMonodrug.setActive(true);
		productMonodrug.setCode(123);
		productMonodrug.setDescription("Test description");
		this.productMonodrugService.save(productMonodrug);

		ProductMonodrug savedProductMonodrug = this.productMonodrugService.get(productMonodrug.getId());
		Assert.isTrue(productMonodrug.getDescription().equals(savedProductMonodrug.getDescription()));
	}

	@Test
	public void getAll() {
		ProductMonodrug productMonodrug1 = new ProductMonodrug();
		productMonodrug1.setActive(true);
		productMonodrug1.setCode(123);
		productMonodrug1.setDescription("Test description");
		this.productMonodrugService.save(productMonodrug1);

		ProductMonodrug productMonodrug2 = new ProductMonodrug();
		productMonodrug2.setActive(true);
		productMonodrug2.setCode(456);
		productMonodrug2.setDescription("Test description 2");
		this.productMonodrugService.save(productMonodrug2);

		List<ProductMonodrug> productMonodrugs = this.productMonodrugService.getAll();

		Assert.isTrue(productMonodrugs.size() == 2);
	}

	@Test
	public void exists() {
		ProductMonodrug productMonodrug = new ProductMonodrug();
		productMonodrug.setActive(true);
		productMonodrug.setCode(123);
		productMonodrug.setDescription("Test description");
		this.productMonodrugService.save(productMonodrug);

		Boolean isTrue = this.productMonodrugService.exists(productMonodrug.getCode());
		Assert.isTrue(isTrue == true);

		Boolean isFalse = this.productMonodrugService.exists(999);
		Assert.isTrue(isFalse == false);
	}

}
