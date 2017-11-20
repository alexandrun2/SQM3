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
import org.app.service.ejb.ClientDataService;
import org.app.service.ejb.ClientDataServiceEJB;
import org.app.service.ejb.EmployeeDataService;
import org.app.service.ejb.EmployeeDataServiceEJB;
import org.app.service.ejb.WarrantyIssueDataService;
import org.app.service.ejb.WarrantyIssueDataServiceEJB;
import org.app.service.entities.Client;
import org.app.service.entities.Employee;
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
public class TestWarrantyIssueDataServiceEJBArq {

	private static Logger logger = Logger.getLogger(TestWarrantyIssueDataServiceEJBArq.class.getName());

	@EJB
	private static WarrantyIssueDataService service;
	@EJB
	private static EmployeeDataService cService;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(WarrantyIssue.class.getPackage())
	                .addClass(WarrantyIssueDataService.class)
	                .addClass(WarrantyIssueDataServiceEJB.class)
	                .addClass(EmployeeDataService.class)
	                .addClass(EmployeeDataServiceEJB.class)
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
	public void test2_DeleteWarrantyIssue() {
		logger.info("DEBUG: Junit TESTING: testDeleteWarrantyIssue ...");
		
		Collection<WarrantyIssue> wi = service.getWarrantyIssues();
		for (WarrantyIssue w: wi)
			service.removeWarrantyIssue(w);
		Collection<WarrantyIssue> wiAfterDelete = service.getWarrantyIssues();
		//assertTrue("Fail to read warrantyissues!", wiAfterDelete.size() == 0);
	}	
	
	@Test
	public void test3_AddWarrantyIssue() {
		logger.info("DEBUG: Junit TESTING: testAddWarrantyIssue ...");
		
		Employee empl = new Employee(333, "emplTest", "pass" , "Gigi",
				"Ionescu",  "suport garantie" );
		/*ATENTIE*/
		if(cService.getById(333)==null)
		{
			cService.addEmployee(empl);
		}
		
		
		Date dd = Calendar.getInstance().getTime();
		
		Integer wiToAdd = 3;
		for (int i=9005; i < wiToAdd+9005; i++){
			service.addWarrantyIssue(new WarrantyIssue(i, "details.. " + (100 + i), dd, "in asteptare",cService.getById(333)));
		}
		Collection<WarrantyIssue> warrantyIssues = service.toCollection();
		//assertTrue("Fail to add WarrantyIssues!", warrantyIssues.size() ==  wiToAdd);
	}
	
	@Test
	public void test4_GetWarrantyIssues() {
		logger.info("DEBUG: Junit TESTING: testGetWarrantyIssues ...");
		Collection<WarrantyIssue> wi = service.getWarrantyIssues();
		assertTrue("Fail to read WarrantyIssues!", wi.size() > 0);
	}
}
