package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.drogueria.model.ProductBrand;
import com.drogueria.service.ProductBrandService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-test.xml" })
public class ProductBrandServiceTest {

	@Autowired
	private ProductBrandService productBrandService;

	@Test
	public void save() {
		ProductBrand productBrand = new ProductBrand();
		productBrand.setActive(true);
		productBrand.setCode(123);
		productBrand.setDescription("Test description");
		this.productBrandService.save(productBrand);

		ProductBrand savedProductBrand = this.productBrandService.get(productBrand.getId());
		Assert.isTrue(productBrand.getDescription().equals(savedProductBrand.getDescription()));

		this.productBrandService.delete(productBrand.getId());
	}

	@Test
	public void getAll() {
		ProductBrand productBrand1 = new ProductBrand();
		productBrand1.setActive(true);
		productBrand1.setCode(123);
		productBrand1.setDescription("Test description");
		this.productBrandService.save(productBrand1);

		ProductBrand productBrand2 = new ProductBrand();
		productBrand2.setActive(true);
		productBrand2.setCode(456);
		productBrand2.setDescription("Test description 2");
		this.productBrandService.save(productBrand2);

		List<ProductBrand> productBrands = this.productBrandService.getAll();

		Assert.isTrue(productBrands.size() == 2);

		for (ProductBrand productBrand : productBrands) {
			this.productBrandService.delete(productBrand.getId());
		}
	}

	@Test
	public void exists() {
		ProductBrand productBrand = new ProductBrand();
		productBrand.setActive(true);
		productBrand.setCode(123);
		productBrand.setDescription("Test description");
		this.productBrandService.save(productBrand);

		Boolean isTrue = this.productBrandService.exists(productBrand.getCode());
		Assert.isTrue(isTrue == true);

		Boolean isFalse = this.productBrandService.exists(999);
		Assert.isTrue(isFalse == false);

		this.productBrandService.delete(productBrand.getId());
	}

}
