package org.app.service.rest.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.SoftwareProductDataService;
import org.app.service.ejb.TeamEmployeeDataService;
import org.app.service.entities.Employee;
import org.app.service.entities.SoftwareProduct;
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
public class TestSoftwareProductDataServiceRESTArq {

	private static Logger logger = Logger.getLogger(TestSoftwareProductDataServiceRESTArq.class.getName());
	
//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/alexandru/data/products";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-s4-test.war")
	                .addPackage(SoftwareProduct.class.getPackage())
	                .addPackage(SoftwareProductDataService.class.getPackage())
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
	public void test2_DeleteSoftwareProducts() {
		String resourceURL = serviceURL + "/";
		logger.info("DEBUG: Junit TESTING: test2_DeleteSoftwareProduct ...");
		Client client = ClientBuilder.newClient();
		Collection<SoftwareProduct> softwareProducts = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<SoftwareProduct>>(){});		
		
		for (SoftwareProduct p: softwareProducts) {
			client.target(resourceURL + p.getProduct_id()).request().delete();
		}
		
		Collection<SoftwareProduct> softsAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<SoftwareProduct>>(){});	
		assertTrue("Fail to read Projects!", softsAfterDelete.size() == 0);
		
	}
	
	@Test
	public void test3_AddSoftware() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddProject ...");
		Client client = ClientBuilder.newClient();
		Collection<SoftwareProduct> softwareproducts;
		Integer softToAdd = 10;
		SoftwareProduct soft;
		for (int i=10; i <= softToAdd+10; i++){
			soft = new SoftwareProduct(i, "Soft" + (1000 + i));
		
			softwareproducts = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(soft, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<SoftwareProduct>>(){});
			assertTrue("Fail to read Teams!", softwareproducts.size() > 0);
		}
		softwareproducts = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<SoftwareProduct>>(){});
		assertTrue("Fail to add Projects!", softwareproducts.size() >= softToAdd);
		softwareproducts.stream().forEach(System.out::println);
			
	}
	
	
	@Test
	public void test4_GetSoftwareProducts() {
		logger.info("DEBUG: Junit TESTING: test4_GetProjects ...");
		Collection<SoftwareProduct> products = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<SoftwareProduct>>(){});
		assertTrue("Fail to read Projects!", products.size() > 0);
		products.stream().forEach(System.out::println);
	}
	
	@Test
	public void test5_UpdateTeam() {
		String resourceURL = serviceURL + "/10"; //createNewProject
		logger.info("************* DEBUG: Junit TESTING: test5_UpdateProject ... :" + resourceURL);
		Client client = ClientBuilder.newClient();
		// Get project
		SoftwareProduct product = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(SoftwareProduct.class);
		
	//	assertNotNull("REST Data Service failed!", product);
		logger.info(">>> Initial Project: " + product);
		
		// update and save project
		product.setProduct_name(product.getProduct_name() + "_UPD_JSON");
		product = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(product, MediaType.APPLICATION_JSON))
				.readEntity(SoftwareProduct.class);
		
		logger.info(">>> Updated Project: " + product);
		
	//	assertNotNull("REST Data Service failed!", team);
	}	
}
