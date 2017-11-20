package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.app.patterns.EntityRepositoryBase;

import org.app.service.entities.Employee;

@Stateless @LocalBean
public class EmployeeDataServiceEJB extends EntityRepositoryBase<Employee> 
implements EmployeeDataService {

	private static Logger logger = Logger.getLogger( EmployeeDataServiceEJB .class.getName());
	
	//private EntityRepository<SoftwareProduct> entityRepository;
	
	@PostConstruct
	public void init() {
		logger.info("POSTCONSTRUCT-INIT injected EntityManager: " + this.em);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "EmployeeDataService is working...";
	}

	@Override
	public Employee addEmployee(Employee employeeToAdd) {
		// TODO Auto-generated method stub
		em.persist(employeeToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(employeeToAdd);
		return employeeToAdd;
	}

	@Override
	public String removeEmployee(Employee employeeToDelete) {
		// TODO Auto-generated method stub
		employeeToDelete = em.merge(employeeToDelete);
		em.remove(employeeToDelete);
		em.flush();
		return "True";
	}

	@Override
	public Collection<Employee> getEmployees() {
		// TODO Auto-generated method stub
		List<Employee> empls = em.createQuery("SELECT e FROM Employee e", Employee.class)
				.getResultList();
		return empls;
	}

}
