package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Affiliate;
import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.service.AffiliateService;
import com.lsntsolutions.gtmApp.service.VATLiabilityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.lsntsolutions.gtmApp.model.Province;
import com.lsntsolutions.gtmApp.model.VATLiability;
import com.lsntsolutions.gtmApp.service.ClientService;
import com.lsntsolutions.gtmApp.service.ProvinceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class AffiliateServiceTest {
	@Autowired
	private AffiliateService affiliateService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private VATLiabilityService vatLiabilityService;
	@Autowired
	private ProvinceService provinceService;

	@Test
	public void save() {

		Client client = new Client();
		client.setCode(111);
		client.setActive(true);
		client.setAddress("sarasa");
		client.setCorporateName("la SA");
		client.setLocality("Alguna");
		client.setName("Sarasa SA");
		client.setPhone("2565");
		client.setTaxId("6556");

		VATLiability vatLiability = new VATLiability();
		vatLiability.setAcronym("sara");
		vatLiability.setDescription("desc");
		this.vatLiabilityService.save(vatLiability);

		client.setVATLiability(vatLiability);
		Province province = new Province();
		province.setName("Buenos Aires");
		this.provinceService.save(province);
		client.setProvince(province);
		client.setZipCode("1212");
		client.setMedicalInsuranceCode(11);
		this.clientService.save(client);

		Affiliate affiliate = new Affiliate();
		affiliate.setActive(true);
		affiliate.setCode("234");
		affiliate.setDocument("4564");
		affiliate.setDocumentType("LC");
		affiliate.setName("juan");
		affiliate.setSurname("gomez");
		this.affiliateService.save(affiliate);

		Affiliate savedAffiliate = this.affiliateService.getByCode(affiliate.getId());
		Assert.isTrue(affiliate.getSurname().equals(savedAffiliate.getSurname()));

		this.affiliateService.delete(affiliate.getId());

		this.clientService.delete(client.getId());

	}

}
