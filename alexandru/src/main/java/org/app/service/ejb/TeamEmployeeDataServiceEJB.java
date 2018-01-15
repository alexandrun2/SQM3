package org.app.service.ejb;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.Employee;

import org.app.service.entities.Team;

@Path("teams")  /* http://localhost:8080/MSD-S4/data/teams */
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
	@GET 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Collection<Team> toCollection() {
		logger.info("**** DEBUG REST toCollection()");
		return super.toCollection();
	}	
	
	@GET @Path("/{id}") 	/* MSD-S4/data/teams/data/{id} 	REST-resource: project-entity*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
	public Team getTeamById(@PathParam("id") Integer id) {
		Team team = super.getById(id);
		logger.info("**** DEBUG REST getById(" + id +") = " + team);
		return team;
	}
	
	@POST 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	public Collection<Team> addIntoCollection(Team team) {
		// save aggregate
		super.add(team);
		logger.info("**** DEBUG REST save aggregate POST");
		// return updated collection
		return super.toCollection();
	}
	
	
	@PUT @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	@Override
	public Team add(Team team) {
		// restore aggregation-relation
		for (Employee e: team.getEmployee())
			e.setTeam(team);
		logger.info("**** DEBUG REST restore aggregation-relation PUT");
		// save aggregate
		logger.info("**** DEBUG REST save aggregate PUT");
		team = super.add(team);
		// return updated collection	
		return team;
	}	
	
//	@Override
	@DELETE 				/* MSD-S4/data/projects 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public Collection<Team> removeFromCollection(Team team) {
		logger.info("DEBUG: called REMOVE - team: " + team);
		super.remove(team);
		return super.toCollection();
	}
	
	
	@DELETE @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public void remove(@PathParam("id")Integer id) {
		logger.info("DEBUG: called REMOVE - ById() for teams >>>>>>>>>>>>>> simplified ! + id");
		Team team = super.getById(id);
		super.remove(team);
	}
	
	@POST @Path("/new/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	@Override
	public Team createNewTeam(@PathParam("id")Integer id) {
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


	@GET @Path("/test") // Check if resource is up ...
	@Produces({ MediaType.TEXT_PLAIN})
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "TeamEmployeeDataService is working...";
	}
	
	// dummy XML marshall Rest: http://localhost:8080/MSD-S4/data/projects/projectdata
		@GET @Path("/teamdata")
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		public Response getTeamData() throws Exception{
			Team dto = new Team(1111, "Team 1111");
			JAXBContext jaxbContext = JAXBContext.newInstance(Team.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			marshaller.marshal(dto, os);
			String aString = new String(os.toByteArray(),"UTF-8");
			
			Response response = Response
					.status(Status.OK)
					.type(MediaType.TEXT_PLAIN)
					.entity(aString)
					.build();
			
			return response;
		}

	
}
