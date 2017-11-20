package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;

import org.app.service.ejb.SoftwareProductDataService;
import org.app.service.ejb.SoftwareProductDataServiceEJB;
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
public class TestSoftwareProductDataServiceEJBArq {
	
	private static Logger logger = Logger.getLogger(TestSoftwareProductDataServiceEJBArq.class.getName());

	@EJB
	private static SoftwareProductDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(SoftwareProduct.class.getPackage())
	                .addClass(SoftwareProductDataService.class)
	                .addClass(SoftwareProductDataServiceEJB.class)
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
	public void test2_DeleteSoftwareProduct() {
		logger.info("DEBUG: Junit TESTING: testDeleteProject ...");
		
		Collection<SoftwareProduct> sp = service.getSoftwareProducts();
		for (SoftwareProduct s: sp)
			service.removeSoftwareProduct(s);
		Collection<SoftwareProduct> spAfterDelete = service.getSoftwareProducts();
		assertTrue("Fail to read features!", spAfterDelete.size() == 0);
	}	
	
	@Test
	public void test3_AddSoftwareProduct() {
		logger.info("DEBUG: Junit TESTING: testAddProject ...");
		
		Integer productsToAdd = 3;
		for (int i=1; i <= productsToAdd; i++){
			service.addSoftwareProduct(new SoftwareProduct(i, "Soft " + (100 + i)));
		}
		Collection<SoftwareProduct> products = service.toCollection();
		assertTrue("Fail to add Products!", products.size() == productsToAdd);
	}
	
	@Test
	public void test4_GetSoftwareProducts() {
		logger.info("DEBUG: Junit TESTING: testGetProjects ...");
		Collection<SoftwareProduct> softwareProducts = service.getSoftwareProducts();
		assertTrue("Fail to read Products!", softwareProducts.size() > 0);
	}
	
	
}
