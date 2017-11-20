package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.EmployeeDataService;
import org.app.service.ejb.EmployeeDataServiceEJB;
import org.app.service.ejb.TeamEmployeeDataService;
import org.app.service.ejb.TeamEmployeeDataServiceEJB;
import org.app.service.ejb.WarrantyDataService;
import org.app.service.ejb.WarrantyDataServiceEJB;
import org.app.service.ejb.WarrantyIssueDataService;
import org.app.service.ejb.WarrantyIssueDataServiceEJB;
import org.app.service.entities.Employee;
import org.app.service.entities.Team;
import org.app.service.entities.Warranty;
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
public class TeamEmployeeDataServiceEJBArq {
	private static Logger logger = Logger.getLogger(TeamEmployeeDataServiceEJBArq.class.getName());
	// Arquilian infrastructure
	@Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap
                .create(WebArchive.class, "msd-test.war")
                .addPackage(EntityRepository.class.getPackage()).addPackage(Team.class.getPackage())
                .addClass(EmployeeDataService.class).addClass(EmployeeDataServiceEJB.class)
                .addClass(TeamEmployeeDataService.class).addClass(TeamEmployeeDataServiceEJB.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	@EJB // Test EJB Data Service Reference is injected
	private static TeamEmployeeDataService service;	
	
	@Test
	public void test1_GetMessage() {
		logger.info("DEBUG: Junit TESTING: testGetMessage ...");
		String response = service.getMessage();
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
	}	
	
	@Test
	public void test2_DeleteTeam() {
		logger.info("DEBUG: Junit TESTING: testDeleteTeam 123...");
		Team team = service.getById(123);  // !!!
		if (team != null)
			service.remove(team);
		team = service.getById(123);  // !!!
		assertNull("Fail to delete Team 123!", team);
	}	
	@Test
	public void test3_CreateNewTeam(){
		Team team = service.createNewTeam(123);
		assertNotNull("Fail to create new TEAM in repository!", team);
		// update team
		team.setTeam_name(team.getTeam_name() + " - changed by test client");
		Set<Employee> employees = team.getEmployee();
		for(Employee t: employees)
			t.setFirst_name(t.getFirst_name() + " - changed by test client");
		team = service.add(team);
		assertNotNull("Fail to save new project in repository!", team);
		logger.info("DEBUG createNewProject: project changed: " + team);
		// check read
		team = service.getById(123);  // !!!
		assertNotNull("Fail to find changed team in repository!", team);
		logger.info("DEBUG createNewTeam: queried team" + team);
	}	
	
	@Test
	public void test4_GetTeam() {
		logger.info("DEBUG: Junit TESTING: testGetTeam 123 ...");
		Team team = service.getById(123);
		assertNotNull("Fail to Get Team 123!", team);
	}


}
