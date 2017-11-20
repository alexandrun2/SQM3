package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.Employee;

@Remote
public interface EmployeeDataService extends EntityRepository<Employee> {

	String getMessage();

	Employee addEmployee(Employee employeeToAdd);
	
	String removeEmployee(Employee employeeToDelete);
	
	Collection<Employee> getEmployees();
}
