package org.app.service.ejb;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.app.service.entities.Client;
import org.app.service.entities.Employee;
import org.app.service.entities.SoftwareProduct;
import org.app.service.entities.Team;
import org.app.service.entities.Warranty;
import org.app.service.entities.WarrantyIssue;

@Path("warranties")  /* http://localhost:8080/alexandru/data/warranties */
@Stateless @LocalBean
public class WarrantyDataServiceEJB extends EntityRepositoryBase<Warranty>
implements WarrantyDataService {

	private static Logger logger = Logger.getLogger(WarrantyDataServiceEJB.class.getName());
	
	@EJB // injected DataService
	private WarrantyIssueDataService WarrantyIssueDataService; 
	private EntityRepository<Warranty> warrantyRepository;
	
	
	@EJB  
	private ClientDataService ClientDataService; 
	private EntityRepository<Client> clientRepository;
	@EJB 
	private SoftwareProductDataService SoftwareproductDataService; 
	private EntityRepository<SoftwareProduct> softwareProductRepository;

	@PostConstruct
	public void init() {
		// local instantiation of local component-entity-repository
		warrantyRepository = new EntityRepositoryBase<Warranty>(this.em,Warranty.class);
		logger.info("POSTCONSTRUCT-INIT releaseRepository: " + this.warrantyRepository);
		logger.info("POSTCONSTRUCT-INIT featureDataService: " + this.WarrantyIssueDataService);
		
		
		logger.info("POSTCONSTRUCT-INIT clientRepository: " + this.clientRepository);
		logger.info("POSTCONSTRUCT-INIT  ClientDataService: " + this.ClientDataService);
		logger.info("POSTCONSTRUCT-INIT softwareProductRepository: " + this.softwareProductRepository);
		logger.info("POSTCONSTRUCT-INIT SoftwareproductDataService: " + this.SoftwareproductDataService);
		
	}
	/*@Override
	public Warranty createNewWarranty(Integer id) {
		// TODO Auto-generated method stub
		// create project aggregate
				
		SoftwareProduct product = new SoftwareProduct(id, "Software" + id);
		Client client = new Client(id, "user" + (100 + id), "pass" + (100 + id), "fc" + (100 + id),
					"Name " + (100 + id), "075" + (100 + id) + (300 + id) + id, "Adress " + (100 + id), "test@mail.com" );
				
		if (ClientDataService.getById(id) == null )
			{
			ClientDataService.addClient(client);
			}
		if (SoftwareproductDataService.getById(id)==null)
			{
			SoftwareproductDataService.addSoftwareProduct(product);
			}
				
		Date d = Calendar.getInstance().getTime();
		
		Warranty warranty = new Warranty(id, d, 2, "" + id, "Full Warranty", SoftwareproductDataService.getById(id),ClientDataService.getById(id));
		Set<WarrantyIssue> warrantyIssue = new HashSet<>();
				
		Date dd = Calendar.getInstance().getTime();
				
		Integer warrantyIssueCount = 3;
		for (int i=1; i<=warrantyIssueCount; i++)
		{
			warrantyIssue.add(new WarrantyIssue(i, "This software freeze daily", dd, "NEW",  warranty));
		}
				
		warranty.setWarranty_issues(warrantyIssue);
				
		// save warranty aggregate
		this.add(warranty);
		// return warranty aggregate to service client
		return warranty;
	}*/

	/*@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "WarrantyDataService is working...";
	}*/
	/*@Override
	public Warranty getWarrantyById(Integer warrantyId) {
		// TODO Auto-generated method stub
		return warrantyRepository.getById(warrantyId);
	}*/
	
	
	@Override
	@GET 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Collection<Warranty> toCollection() {
		logger.info("**** DEBUG REST toCollection()");
		return super.toCollection();
	}	
	
	@GET @Path("/{id}") 	/* MSD-S4/data/teams/data/{id} 	REST-resource: project-entity*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
	public Warranty getWarrantyById(@PathParam("id") Integer id) {
		Warranty war = super.getById(id);
		logger.info("**** DEBUG REST getById(" + id +") = " + war);
		return war;
	}
	
	@POST 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	public Collection<Warranty> addIntoCollection(Warranty w) {
		// save aggregate
		super.add(w);
		logger.info("**** DEBUG REST save aggregate POST");
		// return updated collection
		return super.toCollection();
	}
	
	@PUT @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	@Override
	public Warranty add(Warranty war) {
		// restore aggregation-relation
		for (WarrantyIssue wi: war.getWarranty_issues())
			wi.setWarranty(war);
		logger.info("**** DEBUG REST restore aggregation-relation PUT");
		// save aggregate
		logger.info("**** DEBUG REST save aggregate PUT");
		war = super.add(war);
		// return updated collection	
		return war;
	}	
	
	//@Override
	@DELETE 				/* MSD-S4/data/projects 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public Collection<Warranty> removeFromCollection(Warranty war) {
		logger.info("DEBUG: called REMOVE - team: " + war);
		super.remove(war);
		return super.toCollection();
	}
	
	@DELETE @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public void remove(@PathParam("id")Integer id) {
		logger.info("DEBUG: called REMOVE - ById() for teams >>>>>>>>>>>>>> simplified ! + id");
		Warranty w = super.getById(id);
		super.remove(w);
	}
	
	@POST @Path("/new/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	@Override
	public Warranty createNewWarranty(Integer id) {
		// TODO Auto-generated method stub
		// create project aggregate
				
		SoftwareProduct product = new SoftwareProduct(id, "Software" + id);
		Client client = new Client(id, "user" + (100 + id), "pass" + (100 + id), "fc" + (100 + id),
					"Name " + (100 + id), "075" + (100 + id) + (300 + id) + id, "Adress " + (100 + id), "test@mail.com" );
				
		if (ClientDataService.getById(id) == null )
			{
			ClientDataService.addClient(client);
			}
		if (SoftwareproductDataService.getById(id)==null)
			{
			SoftwareproductDataService.addSoftwareProduct(product);
			}
				
		Date d = Calendar.getInstance().getTime();
		
		Warranty warranty = new Warranty(id, d, 2, "" + id, "Full Warranty", SoftwareproductDataService.getById(id),ClientDataService.getById(id));
		Set<WarrantyIssue> warrantyIssue = new HashSet<>();
				
		Date dd = Calendar.getInstance().getTime();
				
		Integer warrantyIssueCount = 3;
		for (int i=1; i<=warrantyIssueCount; i++)
		{
			warrantyIssue.add(new WarrantyIssue(i, "This software freeze daily", dd, "NEW",  warranty));
		}
				
		warranty.setWarranty_issues(warrantyIssue);
				
		// save warranty aggregate
		this.add(warranty);
		// return warranty aggregate to service client
		return warranty;
	}
	
	@GET @Path("/test") // Check if resource is up ...
	@Produces({ MediaType.TEXT_PLAIN})
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "TeamEmployeeDataService is working...";
	}
	
	
	// dummy XML marshall Rest: http://localhost:8080/MSD-S4/data/projects/projectdata
			@GET @Path("/warrantydata")
			@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
			public Response getTeamData() throws Exception{
				Date d = Calendar.getInstance().getTime();
				Warranty dto = new Warranty (88, d, 2, "" + 88, "Full Warranty");
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
