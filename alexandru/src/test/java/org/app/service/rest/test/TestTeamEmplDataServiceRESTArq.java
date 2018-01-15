package org.app.service.rest.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.app.patterns.EntityRepository;

import org.app.service.ejb.TeamEmployeeDataService;

import org.app.service.entities.Employee;
import org.app.service.entities.Team;
import org.app.service.rest.ApplicationConfig;
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
public class TestTeamEmplDataServiceRESTArq {

	
	private static Logger logger = Logger.getLogger(TestTeamEmplDataServiceRESTArq.class.getName());

//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/alexandru/data/teams";	
//	private static String serviceURL = "http://localhost:8080/MSD-S4/data/projects";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-s4-test.war")
	                .addPackage(Team.class.getPackage())
	                .addPackage(Employee.class.getPackage())
	                .addPackage(TeamEmployeeDataService.class.getPackage())
	                .addPackage(EntityRepository.class.getPackage())
	                .addPackage(ApplicationConfig.class.getPackage())
	                .addAsResource("META-INF/persistence.xml")
	                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); // all mode by default
	}	
	
	
	@EJB // Test EJB Data Service Reference is injected
	private static TeamEmployeeDataService serviceUR2;	
	
	@Test
	public void test1_GetMessage() {
		String resourceURL = serviceURL + "/test";
		logger.info("DEBUG: Junit TESTING: test1_GetMessage ...");
		String response = ClientBuilder.newClient().target(resourceURL)
				.request().get()
				.readEntity(String.class);
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
	}
	
	@Test
	public void test2_DeleteTeams() {
		String resourceURL = serviceURL + "/";
		logger.info("DEBUG: Junit TESTING: test2_DeleteProject ...");
		Client client = ClientBuilder.newClient();
		Collection<Team> teams = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Team>>(){});		
		
		for (Team p: teams) {
			client.target(resourceURL + p.getTeam_id()).request().delete();
		}
		
		Collection<Team> teamsAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Team>>(){});	
		assertTrue("Fail to read Projects!", teamsAfterDelete.size() == 0);
		
	}
	
	
	@Test
	public void test3_AddTeam() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddProject ...");
		Client client = ClientBuilder.newClient();
		Collection<Team> teams;
		Integer teamsToAdd = 3;
		Team team;
		for (int i=1; i <= teamsToAdd; i++){
			team = new Team(i, "Team_" + (100 + i));
		
			teams = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(team, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Team>>(){});
			assertTrue("Fail to read Teams!", teams.size() > 0);
		}
		teams = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Team>>(){});
		assertTrue("Fail to add Projects!", teams.size() >= teamsToAdd);
		teams.stream().forEach(System.out::println);
		
	/*	//add employees for first team
		Team team1 = service.getById(1);
		Set<Employee> employees = new HashSet<>();
		Date dd = Calendar.getInstance().getTime();
		for (int i=100; i<teamsToAdd+100; i++){
			employees.add(new Employee(i, "user" + i, "pass" + i,dd, "Nume" + i, "Prenume" + i, "Programator", team1));
		}
		team1.setEmployee(employees);
		service.add(team1);*/
		
		Team team2 = serviceUR2.createNewTeam(6969);
		team2 = serviceUR2.add(team2);
		
	}
	
	@Test
	public void test4_GetTeams() {
		logger.info("DEBUG: Junit TESTING: test4_GetProjects ...");
		Collection<Team> teams = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Team>>(){});
		assertTrue("Fail to read Projects!", teams.size() > 0);
		teams.stream().forEach(System.out::println);
	}
	
	@Test
	public void test5_UpdateTeam() {
		String resourceURL = serviceURL + "/1"; //createNewProject
		logger.info("************* DEBUG: Junit TESTING: test5_UpdateProject ... :" + resourceURL);
		Client client = ClientBuilder.newClient();
		// Get project
		Team team = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Team.class);
		
	//	assertNotNull("REST Data Service failed!", team);
		logger.info(">>> Initial Project: " + team);
		
		// update and save project
		team.setTeam_name(team.getTeam_name() + "_UPD_JSON");
		team = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(team, MediaType.APPLICATION_JSON))
				.readEntity(Team.class);
		
		logger.info(">>> Updated Project: " + team);
		
	//	assertNotNull("REST Data Service failed!", team);
	}	
}
