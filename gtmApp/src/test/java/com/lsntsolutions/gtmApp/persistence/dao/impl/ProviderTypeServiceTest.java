package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.service.ProviderTypeService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.lsntsolutions.gtmApp.model.ProviderType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class ProviderTypeServiceTest {

	@Autowired
	private ProviderTypeService providerTypeService;

	@Test
	public void save() {
		ProviderType providerType = new ProviderType();
		providerType.setActive(true);
		providerType.setCode(123);
		providerType.setDescription("Test description");
		this.providerTypeService.save(providerType);

		ProviderType savedProviderType = this.providerTypeService.get(providerType.getId());
		Assert.isTrue(providerType.getDescription().equals(savedProviderType.getDescription()));

		this.providerTypeService.delete(providerType.getId());
	}

	@Test
	public void getAll() {
		ProviderType providerType1 = new ProviderType();
		providerType1.setActive(true);
		providerType1.setCode(123);
		providerType1.setDescription("Test description");
		this.providerTypeService.save(providerType1);

		ProviderType providerType2 = new ProviderType();
		providerType2.setActive(true);
		providerType2.setCode(456);
		providerType2.setDescription("Test description 2");
		this.providerTypeService.save(providerType2);

		List<ProviderType> providerTypes = this.providerTypeService.getAll();

		Assert.isTrue(providerTypes.size() == 2);

		for (ProviderType providerType : providerTypes) {
			this.providerTypeService.delete(providerType.getId());
		}
	}

	@Test
	public void exists() {
		ProviderType providerType = new ProviderType();
		providerType.setActive(true);
		providerType.setCode(123);
		providerType.setDescription("Test description");
		this.providerTypeService.save(providerType);

		Boolean isTrue = this.providerTypeService.exists(providerType.getCode());
		Assert.isTrue(isTrue == true);

		Boolean isFalse = this.providerTypeService.exists(999);
		Assert.isTrue(isFalse == false);

		this.providerTypeService.delete(providerType.getId());
	}

}
