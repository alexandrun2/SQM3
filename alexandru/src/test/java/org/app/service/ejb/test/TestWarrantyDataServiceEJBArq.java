package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.ClientDataService;
import org.app.service.ejb.ClientDataServiceEJB;
import org.app.service.ejb.SoftwareProductDataService;
import org.app.service.ejb.SoftwareProductDataServiceEJB;
import org.app.service.ejb.WarrantyDataService;
import org.app.service.ejb.WarrantyDataServiceEJB;
import org.app.service.ejb.WarrantyIssueDataService;
import org.app.service.ejb.WarrantyIssueDataServiceEJB;
import org.app.service.entities.Client;
import org.app.service.entities.SoftwareProduct;
import org.app.service.entities.Warranty;
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
public class TestWarrantyDataServiceEJBArq {
	private static Logger logger = Logger.getLogger(TestWarrantyDataServiceEJBArq.class.getName());
	// Arquilian infrastructure
	@Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap
                .create(WebArchive.class, "msd-test.war")
                .addPackage(EntityRepository.class.getPackage()).addPackage(Warranty.class.getPackage())
                .addClass(WarrantyIssueDataService.class).addClass(WarrantyIssueDataServiceEJB.class)
                .addClass(WarrantyDataService.class).addClass(WarrantyDataServiceEJB.class)
                .addClass(ClientDataService.class).addClass(ClientDataServiceEJB.class)
                .addClass(SoftwareProductDataService.class).addClass(SoftwareProductDataServiceEJB.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	@EJB // Test EJB Data Service Reference is injected
	private static WarrantyDataService service;	
	

	@Test
	public void test1_GetMessage() {
		logger.info("DEBUG: Junit TESTING: testGetMessage ...");
		String response = service.getMessage();
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
}	
	@Test
	public void test2_DeleteWarranty() {
		logger.info("DEBUG: Junit TESTING: testDeleteWarranty 999...");
		Warranty war = service.getById(999);  // !!!
		if (war != null)
			service.remove(war);
		war = service.getById(7002);  // !!!
		assertNull("Fail to delete Warranty 999!", war);
	}
	
	@Test
	public void test3_CreateNewWarranty(){
		Warranty war = service.createNewWarranty(999);
		assertNotNull("Fail to create new warranty in repository!", war);
		// update warranty
		war.setDescription("Modified for test3");	
		Set<WarrantyIssue> issues = war.getWarranty_issues();
		for(WarrantyIssue ws: issues)
			ws.setIssue_details(ws.getIssue_details() + "changed by test");
		war = service.add(war);
		assertNotNull("Fail to save new project in repository!", war);
		logger.info("DEBUG createNewProject: project changed: " + war);
		// check read
				war = service.getById(999);  // !!!
				assertNotNull("Fail to find changed warranty in repository!", war);
				logger.info("DEBUG createNewWarranty: queried warranty" + war);
	}	
	
	
	@Test
	public void test4_GetWarranty() {
		logger.info("DEBUG: Junit TESTING: testGetWarranty 999 ...");
		Warranty war = service.getById(999);
		assertNotNull("Fail to Get Project 7002!", war);
	}
	
}
