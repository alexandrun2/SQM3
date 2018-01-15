package org.app.service.rest.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.TeamEmployeeDataService;
import org.app.service.ejb.WarrantyDataService;
import org.app.service.entities.Employee;
import org.app.service.entities.Team;
import org.app.service.entities.Warranty;
import org.app.service.entities.WarrantyIssue;
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
public class TestWarrantyIssuesDataServieRESTArq {

	private static Logger logger = Logger.getLogger(TestWarrantyIssuesDataServieRESTArq.class.getName());
	
//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/alexandru/data/warranties";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-s4-test.war")
	                .addPackage(Warranty.class.getPackage())
	                .addPackage(WarrantyIssue.class.getPackage())
	                .addPackage(WarrantyDataService.class.getPackage())
	                .addPackage(EntityRepository.class.getPackage())
	                .addPackage(ApplicationConfig.class.getPackage())
	                .addAsResource("META-INF/persistence.xml")
	                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); // all mode by default
	}	
	
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
	public void test2_DeleteWarranties() {
		String resourceURL = serviceURL + "/";
		logger.info("DEBUG: Junit TESTING: test2_DeleteProject ...");
		Client client = ClientBuilder.newClient();
		Collection<Warranty> wars = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Warranty>>(){});		
		
		for (Warranty w: wars) {
			client.target(resourceURL + w.getWarranty_id()).request().delete();
		}
		
		Collection<Warranty> warsAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Warranty>>(){});	
		assertTrue("Fail to read Projects!", warsAfterDelete.size() == 0);
		
	}
	
	@EJB // Test EJB Data Service Reference is injected
	private static WarrantyDataService serviceUR2;	
	
	
	@Test
	public void test3_AddWarranty() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddProject ...");
		Client client = ClientBuilder.newClient();
		Collection<Warranty> warranties;
		Integer warToAdd = 3;
		Warranty war;
		
		Date d = Calendar.getInstance().getTime();
		for (int i=1+939; i <= warToAdd+939; i++){
			war = new Warranty (i, d, 2, "" + i, "Full Warranty");
		
			warranties = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(war, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Warranty>>(){});
			assertTrue("Fail to read Teams!", warranties.size() > 0);
		}
		warranties = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Warranty>>(){});
		assertTrue("Fail to add Projects!", warranties.size() >= warToAdd);
		warranties.stream().forEach(System.out::println);
		
		Warranty war2 = serviceUR2.createNewWarranty(1212);
		war2 = serviceUR2.add(war2);
	
		
	}
	
	@Test
	public void test4_GetWarranties() {
		logger.info("DEBUG: Junit TESTING: test4_GetProjects ...");
		Collection<Warranty> wars = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Warranty>>(){});
		assertTrue("Fail to read Projects!", wars.size() > 0);
		wars.stream().forEach(System.out::println);
	}
	
	@Test
	public void test5_UpdateTeam() {
		String resourceURL = serviceURL + "/940"; //createNewProject
		logger.info("************* DEBUG: Junit TESTING: test5_UpdateProject ... :" + resourceURL);
		Client client = ClientBuilder.newClient();
		// Get project
		Warranty war = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Warranty.class);
		
	//	assertNotNull("REST Data Service failed!", team);
		logger.info(">>> Initial Project: " + war);
		
		// update and save project
		war.setDescription(war.getDescription() + "_UPD_JSON");
		war = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(war, MediaType.APPLICATION_JSON))
				.readEntity(Warranty.class);
		
		logger.info(">>> Updated Project: " + war);
		
	//	assertNotNull("REST Data Service failed!", team);
	}	
	
}
