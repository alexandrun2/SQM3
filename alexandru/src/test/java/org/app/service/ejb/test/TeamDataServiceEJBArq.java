package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.ejb.ClientDataService;
import org.app.service.ejb.ClientDataServiceEJB;
import org.app.service.ejb.TeamDataService;
import org.app.service.ejb.TeamDataServiceEJB;
import org.app.service.entities.Client;
import org.app.service.entities.Team;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(Arquillian.class) 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TeamDataServiceEJBArq {

	private static Logger logger = Logger.getLogger(TeamDataServiceEJBArq.class.getName());

	@EJB
	private static TeamDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(Team.class.getPackage())
	                .addClass(TeamDataService.class)
	                .addClass(TeamDataServiceEJB.class)
	                .addClass(EntityRepository.class)
	                .addClass(EntityRepositoryBase.class)
	                .addAsResource("META-INF/persistence.xml")
	                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}	
	
	@Test
	public void test1_GetMessage() {
		logger.info("DEBUG: Junit TESTING: testGetMessage ...");
		String response = service.getMessage();
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
	}
	@Test
	public void test2_DeleteTeam() {
		logger.info("DEBUG: Junit TESTING: testDeleteTeam ...");
		
		Collection<Team> t = service.getTeams();
		for (Team tt: t)
			service.removeTeam(tt);
		Collection<Team> tAfterDelete = service.getTeams();
		assertTrue("Fail to read teams!", tAfterDelete.size() == 0);
	}	
	
	@Test
	public void test3_AddTeam() {
		logger.info("DEBUG: Junit TESTING: testAddTeam ...");
		
		Integer teamsToAdd = 3;
		for (int i=11; i <= teamsToAdd + 10; i++){
			service.addTeam(new Team(i, "Team " + i));
		}
		Collection<Team> teams  = service.toCollection();
		assertTrue("Fail to add Teams!", teams.size() == teamsToAdd);
	}
	
	@Test
	public void test4_GetTeams() {
		logger.info("DEBUG: Junit TESTING: testGetTeams ...");
		Collection<Team> t = service.getTeams();
		assertTrue("Fail to read Teams!", t.size() > 0);
	}
	
}
