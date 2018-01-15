package org.app.service.ejb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.Client;
import org.app.service.entities.Employee;
import org.app.service.entities.Request;
import org.app.service.entities.SoftwareProduct;
import org.app.service.entities.Team;

@Path("requests")  /* http://localhost:8080/alexandru/data/requests */
@Stateless @LocalBean
public class FullRequestDataServiceEJB extends EntityRepositoryBase<Request>
implements FullRequestDataService, Serializable{

	private static Logger logger = Logger.getLogger(FullRequestDataServiceEJB.class.getName());
	
	@EJB // injected DataService
	private RequestDataService requestDataService; 
	// Local component-entity-repository
	private EntityRepository<Request> requestRepository;
	@EJB // injected DataService
	private ClientDataService clientDataService; 
	// Local component-entity-repository
	private EntityRepository<Client> clientRepository;
	@EJB // injected DataService
	private TeamDataService TeamDataService; 
	// Local component-entity-repository
	private EntityRepository<Team>teamRepository;
	@EJB // injected DataService
	private EmployeeDataService EmployeeDataService; 
	// Local component-entity-repository
	private EntityRepository<Employee> employeeRepository;
	@EJB // injected DataService
	private SoftwareProductDataService softwareProductDataService; 
	// Local component-entity-repository
	private EntityRepository<SoftwareProduct> softwareProductRepository;
	
	
	
	
	@PostConstruct
	public void init() {
		// local instantiation of local component-entity-repository
		requestRepository = new EntityRepositoryBase<Request>(this.em,Request.class);
		logger.info("POSTCONSTRUCT-INIT requestRepository: " + this.requestRepository);
	}
	
	
	/*@Override
	public Request createNewFullRequest(Integer id) {
		// TODO Auto-generated method stub
		Date dd = Calendar.getInstance().getTime();
		
		if (clientDataService.getById(id) == null)
		{
			clientDataService.addClient(new Client(id, "user" + (100 + id), "pass" + (100 + id), "fc" + (100 + id),
					"Name " + (100 + id), "075" + (100 + id) + (300 + id) + id, "Adress " + (100 + id), "test@mail.com" ));
		}
		
		if(TeamDataService.getById(id) == null)
		{
			TeamDataService.addTeam(new Team(id, "Team " + id));
		}
		
		if(EmployeeDataService.getById(id+1) == null)
		{
			EmployeeDataService.addEmployee(new Employee(id+1, "user" + (900 + id), "pass" + (800 + id), "Nume" + (id),
					"Prenume" + (id),  "tester" ));
		}
		
		if(softwareProductDataService.getById(id+2) == null)
		{
			softwareProductDataService.addSoftwareProduct(new SoftwareProduct(id+2, "Software " + id));
		}
		
		
		Request req = new Request(id, dd, "No description", "NEW", "CHANGE REQUEST", clientDataService.getById(id),
				TeamDataService.getById(id), EmployeeDataService.getById(id+1), softwareProductDataService.getById(id+2));
		
		this.add(req);
		return req;
	}*/

	/*@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "FullRequestDataService is working...";
	}*/

	/*@Override
	public Request geRequestById(Integer requestId) {
		// TODO Auto-generated method stub
		return requestRepository.getById(requestId);
	}*/

	
	@Override
	@GET 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Collection<Request> toCollection() {
		logger.info("**** DEBUG REST toCollection()");
		return super.toCollection();
	}	
	
	@GET @Path("/{id}") 	/* MSD-S4/data/teams/data/{id} 	REST-resource: project-entity*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
	public Request geRequestById(@PathParam("id") Integer id) {
		Request request = super.getById(id);
		logger.info("**** DEBUG REST getById(" + id +") = " + request);
		return request;
	}
	
	@POST 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	public Collection<Request> addIntoCollection(Request req) {
		// save aggregate
		super.add(req);
		logger.info("**** DEBUG REST save aggregate POST");
		// return updated collection
		return super.toCollection();
	}
	
	@PUT @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	@Override
	public Request add(Request req) {
		// restore aggregation-relation
		
		req = super.add(req);
		// return updated collection	
		return req;
	}	
	
//	@Override
	@DELETE 				/* MSD-S4/data/projects 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public Collection<Request> removeFromCollection(Request req) {
		logger.info("DEBUG: called REMOVE - team: " + req);
		super.remove(req);
		return super.toCollection();
	}
	
	@DELETE @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public void remove(@PathParam("id")Integer id) {
		logger.info("DEBUG: called REMOVE - ById() for teams >>>>>>>>>>>>>> simplified ! + id");
		Request req = super.getById(id);
		super.remove(req);
	}
	
	
	@POST @Path("/new/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	@Override
	public Request createNewFullRequest(Integer id) {
		// TODO Auto-generated method stub
		Date dd = Calendar.getInstance().getTime();
		
		if (clientDataService.getById(id) == null)
		{
			clientDataService.addClient(new Client(id, "user" + (100 + id), "pass" + (100 + id), "fc" + (100 + id),
					"Name " + (100 + id), "075" + (100 + id) + (300 + id) + id, "Adress " + (100 + id), "test@mail.com" ));
		}
		
		if(TeamDataService.getById(id) == null)
		{
			TeamDataService.addTeam(new Team(id, "Team " + id));
		}
		
		if(EmployeeDataService.getById(id+1) == null)
		{
			EmployeeDataService.addEmployee(new Employee(id+1, "user" + (900 + id), "pass" + (800 + id), "Nume" + (id),
					"Prenume" + (id),  "tester" ));
		}
		
		if(softwareProductDataService.getById(id+2) == null)
		{
			softwareProductDataService.addSoftwareProduct(new SoftwareProduct(id+2, "Software " + id));
		}
		
		
		Request req = new Request(id, dd, "No description", "NEW", "CHANGE REQUEST", clientDataService.getById(id),
				TeamDataService.getById(id), EmployeeDataService.getById(id+1), softwareProductDataService.getById(id+2));
		
		this.add(req);
		return req;
	}
	
	@GET @Path("/test") // Check if resource is up ...
	@Produces({ MediaType.TEXT_PLAIN})
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "TeamEmployeeDataService is working...";
	}
	
	
}
