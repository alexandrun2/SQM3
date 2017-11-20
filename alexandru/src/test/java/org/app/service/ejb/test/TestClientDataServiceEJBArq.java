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

import org.app.service.entities.Client;
import org.app.service.entities.SoftwareProduct;
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
public class TestClientDataServiceEJBArq {

	private static Logger logger = Logger.getLogger(TestClientDataServiceEJBArq.class.getName());

	@EJB
	private static ClientDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(Client.class.getPackage())
	                .addClass(ClientDataService.class)
	                .addClass(ClientDataServiceEJB.class)
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
	public void test2_DeleteClient() {
		logger.info("DEBUG: Junit TESTING: testDeleteProject ...");
		
		Collection<Client> c = service.getClients();
		for (Client cc: c)
			service.removeClient(cc);
		Collection<Client> cAfterDelete = service.getClients();
		assertTrue("Fail to read clients!", cAfterDelete.size() == 0);
	}	
	
	@Test
	public void test3_AddClients() {
		logger.info("DEBUG: Junit TESTING: testAddProject ...");
		
		Integer clientsToAdd = 3;
		for (int i=11; i <= clientsToAdd + 10; i++){
			service.addClient(new Client(i, "user" + (100 + i), "pass" + (100 + i), "fc" + (100 + i),
					"Name " + (100 + i), "075" + (100 + i) + (300 + i) + i, "Adress " + (100 + i), "test@mail.com" ));
		}
		Collection<Client> clients = service.toCollection();
		assertTrue("Fail to add Products!", clients.size() == clientsToAdd);
	}
	
	@Test
	public void test4_GetClients() {
		logger.info("DEBUG: Junit TESTING: testGetProjects ...");
		Collection<Client> c = service.getClients();
		assertTrue("Fail to read Clients!", c.size() > 0);
	}
	
}
