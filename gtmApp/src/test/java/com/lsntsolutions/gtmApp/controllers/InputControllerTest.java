package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.dto.InputDTO;
import com.lsntsolutions.gtmApp.dto.InputDTOBuilder;
import com.lsntsolutions.gtmApp.dto.InputDetailDTO;
import com.lsntsolutions.gtmApp.dto.InputDetailDTOBuilder;
import com.lsntsolutions.gtmApp.exceptions.NullInputDetailsException;
import com.lsntsolutions.gtmApp.service.ConceptService;
import com.lsntsolutions.gtmApp.service.InputService;
import com.lsntsolutions.gtmApp.util.IntegrationTestUtil;
import com.lsntsolutions.gtmApp.util.MockSecurityContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
@WebAppConfiguration
@EnableWebMvc
@TransactionConfiguration(defaultRollback = true)
public class InputControllerTest {

	private MockMvc mockMvc;

	@Autowired
	protected UserDetailsService userDetailsService;

	@Resource
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private InputService inputServiceMock;

	@Autowired
	private ConceptService conceptServiceMock;

	@Autowired
	private InputController inputControllerMock;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockHttpSession session;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).addFilter(this.springSecurityFilterChain, "/*").build();

		this.session = new MockHttpSession();
		this.session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, new MockSecurityContext(this.getPrincipal("admin")));
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(this.getPrincipal("admin"));
	}

	@After
	public final void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Ignore
	@Test(expected = NullInputDetailsException.class)
	public void addEmptyInput() throws Exception {

		InputDTO dto = new InputDTOBuilder().agreementId(null).conceptId(null).date(null).deliveryLocationId(null).deliveryNoteNumber(null).providerId(null)
				.purchaseOrderNumber(null).build();

		dto.setInputDetails(null);

		this.mockMvc
				.perform(
						post("/saveInput.do").session(this.session).secure(true).param("isSerializedReturn", "false").accept(MediaType.APPLICATION_JSON)
								.header("Content-Type", MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
								.content(IntegrationTestUtil.convertObjectToJsonBytes(dto))).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());
	}

	@Test
	@Transactional
	public void addOneInput() throws Exception {

		InputDetailDTO iddto = new InputDetailDTOBuilder().amount(1).batch("L0065").expirationDate("110120").productId(1).productType("BE").serialNumber(null)
				.build();

		InputDTO dto = new InputDTOBuilder().agreementId(1).conceptId(1).date("15/09/2014").deliveryLocationId(1).deliveryNoteNumber("R00012345678")
				.providerId(1).purchaseOrderNumber("").build();

		List<InputDetailDTO> idList = new ArrayList<InputDetailDTO>();
		for (int i = 0; i < 10; i++) {
			idList.add(iddto);
		}
		dto.setInputDetails(idList);

		this.mockMvc
				.perform(
						post("/saveInput.do").session(this.session).secure(true).param("isSerializedReturn", "false").accept(MediaType.APPLICATION_JSON)
								.header("Content-Type", MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
								.content(IntegrationTestUtil.convertObjectToJsonBytes(dto))).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());

	}

    @Ignore
	@Test
	@Transactional
	@Repeat(500)
	public void add500Inputs() throws Exception {
		this.addOneInput();
	}

	protected UsernamePasswordAuthenticationToken getPrincipal(String username) {
		UserDetails user = this.userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		return authentication;
	}
}