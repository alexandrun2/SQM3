package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.ClientDataService;
import org.app.service.ejb.ClientDataServiceEJB;
import org.app.service.ejb.EmployeeDataService;
import org.app.service.ejb.EmployeeDataServiceEJB;
import org.app.service.ejb.FullRequestDataService;
import org.app.service.ejb.FullRequestDataServiceEJB;
import org.app.service.ejb.RequestDataService;
import org.app.service.ejb.RequestDataServiceEJB;
import org.app.service.ejb.SoftwareProductDataService;
import org.app.service.ejb.SoftwareProductDataServiceEJB;
import org.app.service.ejb.TeamDataService;
import org.app.service.ejb.TeamDataServiceEJB;
import org.app.service.ejb.TeamEmployeeDataService;
import org.app.service.ejb.TeamEmployeeDataServiceEJB;
import org.app.service.entities.Employee;
import org.app.service.entities.Request;
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
public class TestFullRequestDataServiceEJBAqr {
	
	private static Logger logger = Logger.getLogger(TestFullRequestDataServiceEJBAqr.class.getName());
	
	@Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap
                .create(WebArchive.class, "msd-test.war")
                .addPackage(EntityRepository.class.getPackage()).addPackage(Request.class.getPackage())
                .addClass(FullRequestDataService.class).addClass(FullRequestDataServiceEJB.class)
                .addClass(RequestDataService.class).addClass(RequestDataServiceEJB.class)
                .addClass(ClientDataService.class).addClass(ClientDataServiceEJB.class)
                .addClass(TeamDataService.class).addClass(TeamDataServiceEJB.class)
                .addClass(EmployeeDataService.class).addClass(EmployeeDataServiceEJB.class)
                .addClass(SoftwareProductDataService.class).addClass(SoftwareProductDataServiceEJB.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	@EJB // Test EJB Data Service Reference is injected
	private static FullRequestDataService service;	

	
	@Test
	public void test1_GetMessage() {
		logger.info("DEBUG: Junit TESTING: testGetMessage ...");
		String response = service.getMessage();
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
	}	
	
	@Test
	public void test2_DeleteFullRequest() {
		logger.info("DEBUG: Junit TESTING: testDeleteRequest 43...");
		Request req = service.getById(43);  // !!!
		if (req != null)
			service.remove(req);
		req= service.getById(43);  // !!!
		//assertNull("Fail to delete Request 43!", req);
	}
	
	@Test
	public void test3_CreateNewFullRequest(){
		Request request = service.createNewFullRequest(43);
		assertNotNull("Fail to create new Request in repository!", request);
		// update team
		request.setDescription(request.getDescription() + " - changed by test client");
		
		request = service.add(request);
		assertNotNull("Fail to save new request in repository!", request);
		logger.info("DEBUG createNewRequest: request changed: " + request);
		// check read
		request = service.getById(43);  // !!!
		assertNotNull("Fail to find changed request in repository!", request);
		logger.info("DEBUG createNewRequest: queried request" + request);
	}	
	
	@Test
	public void test4_GetRequest() {
		logger.info("DEBUG: Junit TESTING: testGetRequest 43 ...");
		Request req = service.getById(43);
		assertNotNull("Fail to Get Request 63!", req);
	}
}
