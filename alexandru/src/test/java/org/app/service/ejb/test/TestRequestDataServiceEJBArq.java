package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.ejb.RequestDataService;
import org.app.service.ejb.RequestDataServiceEJB;
import org.app.service.entities.Request;
import org.app.service.entities.WarrantyIssue;
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
public class TestRequestDataServiceEJBArq {

	private static Logger logger = Logger.getLogger(TestRequestDataServiceEJBArq.class.getName());

	@EJB
	private static RequestDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(Request.class.getPackage())
	                .addClass(RequestDataService.class)
	                .addClass(RequestDataServiceEJB.class)
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
	public void test2_DeleteRequest() {
		logger.info("DEBUG: Junit TESTING: testDeleteProject ...");
		
		Collection<Request> r = service.getRequests();
		for (Request rr: r)
			service.removeRequest(rr);
		Collection<Request> rAfterDelete = service.getRequests();
		assertTrue("Fail to read requests!", rAfterDelete.size() == 0);
	}	
	
	@Test
	public void test3_AddRequests() {
		logger.info("DEBUG: Junit TESTING: testAddProject ...");
		
		Date dd = Calendar.getInstance().getTime();
		
		Integer requestsToAdd = 3;
		for (int i=1; i <= requestsToAdd; i++){
			service.addRequest(new Request(i, dd, " I can't add any entities in main module", "New", "ASSISTANCE"));
		}
		Collection<Request> requests = service.toCollection();
		assertTrue("Fail to add Requests!", requests.size() == requestsToAdd);
	}
	
	@Test
	public void test4_GetRequests() {
		logger.info("DEBUG: Junit TESTING: testGetRequests ...");
		Collection<Request> req = service.getRequests();
		assertTrue("Fail to read Requests!", req.size() > 0);
	}
	
}
