package com.drogueria.persistence.dao.impl;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.drogueria.model.Agreement;
import com.drogueria.model.Concept;
import com.drogueria.model.Product;
import com.drogueria.model.ProductBrand;
import com.drogueria.model.ProductDrugCategory;
import com.drogueria.model.ProductGroup;
import com.drogueria.model.ProductMonodrug;
import com.drogueria.model.Stock;
import com.drogueria.service.AgreementService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.ProductBrandService;
import com.drogueria.service.ProductDrugCategoryService;
import com.drogueria.service.ProductGroupService;
import com.drogueria.service.ProductMonodrugService;
import com.drogueria.service.ProductService;
import com.drogueria.service.StockService;

@Ignore
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
	private ConceptService conceptService;
	@Autowired
	private ProductBrandService productBrandService;
	@Autowired
	private ProductDrugCategoryService productDrugCategoryService;
	@Autowired
	private ProductGroupService productGroupService;
	@Autowired
	private ProductMonodrugService productMonodrugService;
	@Mock
	private ConceptService conceptServiceMock;

	private Concept concept;
	private Agreement agreement;
	private Product product;
	private ProductBrand productBrand;
	private ProductDrugCategory productDrugCategory;
	private ProductGroup productGroup;
	private ProductMonodrug productMonodrug;

	@Before
	public void setUp() {
		this.concept = new Concept();
		this.concept.setCode(1);
		this.concept.setActive(true);
		this.concept.setDeliveryNoteCopies(1);
		this.concept.setDeliveryNotePOS("");
		this.concept.setDescription("Test Concept");
		this.concept.setClient(true);
		this.concept.setEvents(null);
		this.concept.setInformAnmat(true);
		this.concept.setInput(true);
		this.concept.setLastDeliveryNoteNumber(1);
		this.concept.setPrintDeliveryNote(false);
		this.concept.setRefund(true);
		this.concept.setId(1);
		this.conceptService.save(this.concept);

		this.agreement = new Agreement();
		this.agreement.setActive(true);
		this.agreement.setCode(123);
		this.agreement.setDescription("Test description");
		this.agreement.setDeliveryNoteFilepath("");
		this.agreement.setOrderLabelFilepath("");
		this.agreement.setPickingFilepath("");
		this.agreement.setNumberOfDeliveryNoteDetailsPerPage(999);
		this.agreement.setDeliveryNoteConcept(this.concept);
		this.agreement.setDestructionConcept(this.concept);
		this.agreementService.save(this.agreement);

		this.product = new Product();
		this.product.setActive(true);
		this.product.setCode(133623);
		this.product.setCold(false);
		this.product.setDescription("Test product");
		this.product.setInformAnmat(false);
		this.product.setType("SS");

		this.productBrand = new ProductBrand();
		this.productBrand.setActive(true);
		this.productBrand.setCode(4546);
		this.productBrand.setDescription("Test Product Brand Description");
		this.productBrandService.save(this.productBrand);
		this.product.setBrand(this.productBrand);

		this.productDrugCategory = new ProductDrugCategory();
		this.productDrugCategory.setActive(true);
		this.productDrugCategory.setDescription("Test Product Drug Category Description");
		this.productDrugCategory.setCode(3344);
		this.productDrugCategoryService.save(this.productDrugCategory);
		this.product.setDrugCategory(this.productDrugCategory);

		this.productGroup = new ProductGroup();
		this.productGroup.setActive(true);
		this.productGroup.setDescription("Test Product Group Description");
		this.productGroup.setCode(4444);
		this.productGroupService.save(this.productGroup);
		this.product.setGroup(this.productGroup);

		this.productMonodrug = new ProductMonodrug();
		this.productMonodrug.setActive(true);
		this.productMonodrug.setDescription("Test Product Brand Description");
		this.productMonodrug.setCode(2222);
		this.productMonodrugService.save(this.productMonodrug);
		this.product.setMonodrug(this.productMonodrug);

		this.productService.save(this.product);
	}

	@After
	public final void tearDown() {
		this.agreementService.delete(this.agreement.getId());
		this.productService.delete(this.product.getId());
	}

	@Test
	public void save() {
		Stock stock = new Stock();
		stock.setAgreement(this.agreement);
		stock.setAmount(10);
		stock.setBatch("abcd");
		stock.setExpirationDate(new Date());

		stock.setBatch("aabcd");
		stock.setExpirationDate(new Date());
		stock.setProduct(this.product);
		stock.setSerialNumber("1656565465");
		this.stockService.save(stock);

		Stock savedStock = this.stockService.get(stock.getId());
		Assert.isTrue(stock.getBatch().equals(savedStock.getBatch()));

		Assert.isTrue(Long.valueOf(stock.getAmount()) == this.stockService.getProductAmount(this.product.getId(), this.agreement.getId(), null));

		this.stockService.delete(stock.getId());
	}
}
