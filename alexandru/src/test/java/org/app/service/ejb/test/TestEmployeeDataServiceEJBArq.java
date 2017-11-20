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
import org.app.service.ejb.EmployeeDataService;
import org.app.service.ejb.EmployeeDataServiceEJB;
import org.app.service.entities.Client;
import org.app.service.entities.Employee;
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
public class TestEmployeeDataServiceEJBArq {

	private static Logger logger = Logger.getLogger(TestEmployeeDataServiceEJBArq.class.getName());

	@EJB
	private static EmployeeDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(Employee.class.getPackage())
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
	public void test2_DeleteEmployee() {
		logger.info("DEBUG: Junit TESTING: testDeleteEmployee ...");
		
		Collection<Employee> e = service.getEmployees();
		for (Employee ee: e)
			service.remove(ee);
		Collection<Employee> eAfterDelete = service.getEmployees();
		assertTrue("Fail to read clients!", eAfterDelete.size() == 0);
	}	
	
	@Test
	public void test3_AddEmployees() {
		logger.info("DEBUG: Junit TESTING: testAddEmployee ...");
		
		Integer employeesToAdd = 3;
		for (int i=1; i <= employeesToAdd; i++){
			service.addEmployee(new Employee(i, "user" + (900 + i), "pass" + (800 + i), "Nume" + (i),
					"Prenume" + (i),  "tester" ));
		}
		Collection<Employee> emplooyees = service.toCollection();
		assertTrue("Fail to add Products!", emplooyees.size() == employeesToAdd);
	}
	
	@Test
	public void test4_GetEmployee() {
		logger.info("DEBUG: Junit TESTING: testGetEmployee ...");
		Collection<Employee> e = service.getEmployees();
		assertTrue("Fail to read Employees!", e.size() > 0);
	}
	
}
