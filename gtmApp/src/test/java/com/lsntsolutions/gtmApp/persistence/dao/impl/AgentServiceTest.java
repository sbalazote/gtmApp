package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Agent;
import com.lsntsolutions.gtmApp.service.AgentService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class AgentServiceTest {

	@Autowired
	private AgentService agentService;

	@Test
	public void save() {
		Agent agent = new Agent();
		agent.setActive(true);
		agent.setCode(123);
		agent.setDescription("Test description");
		this.agentService.save(agent);

		Agent savedAgent = this.agentService.get(agent.getId());
		Assert.isTrue(agent.getDescription().equals(savedAgent.getDescription()));

		this.agentService.delete(agent.getId());
	}

	@Test
	public void getAll() {
		Agent agent1 = new Agent();
		agent1.setActive(true);
		agent1.setCode(123);
		agent1.setDescription("Test description");
		this.agentService.save(agent1);

		Agent agent2 = new Agent();
		agent2.setActive(true);
		agent2.setCode(456);
		agent2.setDescription("Test description 2");
		this.agentService.save(agent2);

		List<Agent> agents = this.agentService.getAll();

		Assert.isTrue(agents.size() == 2);

		for (Agent agent : agents) {
			this.agentService.delete(agent.getId());
		}
	}

	@Test
	public void exists() {
		Agent agent = new Agent();
		agent.setActive(true);
		agent.setCode(123);
		agent.setDescription("Test description");
		this.agentService.save(agent);

		Boolean isTrue = this.agentService.exists(agent.getCode());
		Assert.isTrue(isTrue == true);

		Boolean isFalse = this.agentService.exists(999);
		Assert.isTrue(isFalse == false);

		this.agentService.delete(agent.getId());
	}

}
