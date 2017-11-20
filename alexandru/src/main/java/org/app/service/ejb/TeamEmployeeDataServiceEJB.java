package org.app.service.ejb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.Employee;
import org.app.service.entities.Team;

@Stateless @LocalBean
public class TeamEmployeeDataServiceEJB extends EntityRepositoryBase<Team>
implements TeamEmployeeDataService, Serializable {

	private static Logger logger = Logger.getLogger(TeamEmployeeDataServiceEJB.class.getName());
	
	@EJB // injected DataService
	private EmployeeDataService employeeDataService; 
	// Local component-entity-repository
	private EntityRepository<Team> teamRepository;
	@PostConstruct
	public void init() {
		// local instantiation of local component-entity-repository
		teamRepository = new EntityRepositoryBase<Team>(this.em,Team.class);
		logger.info("POSTCONSTRUCT-INIT teamRepository: " + this.teamRepository);
		logger.info("POSTCONSTRUCT-INIT employeeDataService: " + this.employeeDataService);
	}
	
	@Override
	public Team createNewTeam(Integer id) {
		// TODO Auto-generated method stub
		// create project aggregate
		Team team = new Team(id, "Team " + id);
		Set<Employee> employees = new HashSet<>();
		
		Date dd = Calendar.getInstance().getTime();
		
		Integer employeesCount = 3;
		for (int i=100; i<employeesCount+100; i++){
			employees.add(new Employee(i, "user" + i, "pass" + i,dd, "Nume" + i, "Prenume" + i, "Programator", team));
		}
		
		team.setEmployee(employees);
		
		// save warranty aggregate
		this.add(team);
		// return warranty aggregate to service client
		return team;
	}

	@Override
	public Team getTeamById(Integer teamid) {
		// TODO Auto-generated method stub
		return teamRepository.getById(teamid);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "TeamEmployeeDataService is working...";
	}

	
}
