package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.ClientRequestDataService;
import org.app.service.ejb.ClientRequestDataServiceEJB;
import org.app.service.ejb.EmployeeDataService;
import org.app.service.ejb.EmployeeDataServiceEJB;
import org.app.service.ejb.RequestDataService;
import org.app.service.ejb.RequestDataServiceEJB;
import org.app.service.ejb.SoftwareProductDataService;
import org.app.service.ejb.SoftwareProductDataServiceEJB;
import org.app.service.ejb.TeamEmployeeDataService;
import org.app.service.ejb.TeamEmployeeDataServiceEJB;
import org.app.service.entities.Client;
import org.app.service.entities.Employee;
import org.app.service.entities.Request;
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
public class TestClientRequestDataServiceEJBArq {

	private static Logger logger = Logger.getLogger(TestClientRequestDataServiceEJBArq.class.getName());
	
	@Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap
                .create(WebArchive.class, "msd-test.war")
                .addPackage(EntityRepository.class.getPackage()).addPackage(Client.class.getPackage())
                .addClass(RequestDataService.class).addClass(RequestDataServiceEJB.class)
                .addClass(ClientRequestDataService.class).addClass(ClientRequestDataServiceEJB.class)
                .addClass(SoftwareProductDataService.class).addClass(SoftwareProductDataServiceEJB.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	@EJB // Test EJB Data Service Reference is injected
	private static ClientRequestDataService service;	
	
	@Test
	public void test1_GetMessage() {
		logger.info("DEBUG: Junit TESTING: testGetMessage ...");
		String response = service.getMessage();
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
	}	
	
	@Test
	public void test2_DeleteClient() {
		logger.info("DEBUG: Junit TESTING: testDelete Client 6969...");
		Client client = service.getById(6969);  // !!!
		if (client != null)
			service.remove(client);
		client = service.getById(6969);  // !!!
		assertNull("Fail to delete Client 6969!", client);
	}	
	
	@Test
	public void test3_CreateNewClient(){
		Client client = service.createNewClient(6969);
		assertNotNull("Fail to create new Client in repository!", client);
		// update team
		client.setAdress(client.getAdress() + " - changed by test client");
		Set<Request> requests = client.getRequest_change();
		for(Request r: requests)
			r.setDescription(r.getDescription() + " - changed by test client");
		client = service.add(client);
		assertNotNull("Fail to save new client in repository!", client);
		logger.info("DEBUG createNewClient: client changed: " + client);
		// check read
		client = service.getById(6969);  // !!!
		assertNotNull("Fail to find changed client in repository!", client);
		logger.info("DEBUG createNewClient: queried client" + client);
	}	
	
	@Test
	public void test4_GetClient() {
		logger.info("DEBUG: Junit TESTING: testGetClient 6969 ...");
		Client client = service.getById(6969);
		assertNotNull("Fail to Get Client 6969!", client);
	}

}
