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
import org.app.service.ejb.FullRequestDataService;
import org.app.service.ejb.WarrantyDataService;
import org.app.service.entities.Request;
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
public class TestFullRequestDataServiceRESTArq {

	private static Logger logger = Logger.getLogger(TestFullRequestDataServiceRESTArq.class.getName());
	
//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/alexandru/data/requests";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-s4-test.war")
	                .addPackage(Request.class.getPackage())
	                .addPackage(FullRequestDataService.class.getPackage())
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
	public void test2_DeleteRequests() {
		String resourceURL = serviceURL + "/";
		logger.info("DEBUG: Junit TESTING: test2_DeleteProject ...");
		Client client = ClientBuilder.newClient();
		Collection<Request> reqs = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Request>>(){});		
		
		for (Request r: reqs) {
			client.target(resourceURL + r.getRequest_id()).request().delete();
		}
		
		Collection<Request> reqsAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Request>>(){});	
	//	assertTrue("Fail to read Projects!", reqsAfterDelete.size() == 0);
		
	}
	
	@EJB // Test EJB Data Service Reference is injected
	private static FullRequestDataService serviceUR2;
	
	@Test
	public void test3_AddRequest() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddProject ...");
		Client client = ClientBuilder.newClient();
		Collection<Request> reqs;
		Integer reqToAdd = 3;
		Request req;
		
		Date dd = Calendar.getInstance().getTime();
		for (int i=1+939; i <= reqToAdd+939; i++){
			req = new Request (i, dd, " I can't add any entities in main module", "New", "ASSISTANCE");
		
			reqs = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(req, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Request>>(){});
			assertTrue("Fail to read Teams!", reqs.size() > 0);
		}
		reqs = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Request>>(){});
		assertTrue("Fail to add Projects!", reqs.size() >= reqToAdd);
		reqs.stream().forEach(System.out::println);
		
		Request req2 = serviceUR2.createNewFullRequest(1515);
		req2 = serviceUR2.add(req2);
	
		
	}
	
	@Test
	public void test4_GetRequests() {
		logger.info("DEBUG: Junit TESTING: test4_GetProjects ...");
		Collection<Request> reqs = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Request>>(){});
		assertTrue("Fail to read Projects!", reqs.size() > 0);
		reqs.stream().forEach(System.out::println);
	}
	
	@Test
	public void test5_UpdateRequest() {
		String resourceURL = serviceURL + "/940"; //createNewProject
		logger.info("************* DEBUG: Junit TESTING: test5_UpdateProject ... :" + resourceURL);
		Client client = ClientBuilder.newClient();
		// Get project
		Request req = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Request.class);
		
	//	assertNotNull("REST Data Service failed!", team);
		logger.info(">>> Initial Project: " + req);
		
		// update and save project
		req.setDescription(req.getDescription() + "_UPD_JSON");
		req = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(req, MediaType.APPLICATION_JSON))
				.readEntity(Request.class);
		
		logger.info(">>> Updated Project: " + req);
		
	//	assertNotNull("REST Data Service failed!", team);
	}	
}
