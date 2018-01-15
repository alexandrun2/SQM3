package org.app.service.rest.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.ClientDataService;
import org.app.service.ejb.SoftwareProductDataService;
import org.app.service.entities.Client;
import org.app.service.entities.SoftwareProduct;
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
public class TestClientDataServiceRESTArq {

	private static Logger logger = Logger.getLogger(TestClientDataServiceRESTArq.class.getName());
	
//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/alexandru/data/clients";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-s4-test.war")
	                .addPackage(Client.class.getPackage())
	                .addPackage(ClientDataService.class.getPackage())
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
	public void test3_AddClient() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddProject ...");
		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		Collection<org.app.service.entities.Client> clients;
		Integer clientToAdd = 10;
		org.app.service.entities.Client c;
		for (int i=10; i <= clientToAdd+10; i++){
			c = new org.app.service.entities.Client (i, "user" + (100 + i), "pass" + (100 + i), "fc" + (100 + i),
					"Name " + (100 + i), "075" + (100 + i) + (300 + i) + i, "Adress " + (100 + i), "test@mail.com" );
		
			clients = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(c, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<org.app.service.entities.Client>>(){});
			assertTrue("Fail to read Teams!", clients.size() > 0);
		}
		clients = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<org.app.service.entities.Client>>(){});
		assertTrue("Fail to add Projects!", clients.size() >= clientToAdd);
		clients.stream().forEach(System.out::println);
			
	}
	
	
	

}

