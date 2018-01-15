package org.app.service.ejb;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
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
import org.app.service.entities.EntityBase;
import org.app.service.entities.SoftwareProduct;
import org.app.service.entities.Team;


@Path("products")  /* http://localhost:8080/MSD-S4/data/products */
@Stateless @LocalBean
public class SoftwareProductDataServiceEJB 
	extends EntityRepositoryBase<SoftwareProduct> 
	implements  SoftwareProductDataService{


	private static Logger logger = Logger.getLogger(SoftwareProductDataServiceEJB.class.getName());
	
	//private EntityRepository<SoftwareProduct> entityRepository;
	
	@PostConstruct
	public void init() {
		logger.info("POSTCONSTRUCT-INIT injected EntityManager: " + this.em);
	}

	
	@Override
	@GET 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Collection<SoftwareProduct> toCollection() {
		logger.info("**** DEBUG REST toCollection()");
		return super.toCollection();
	}	
	
	@GET @Path("/{id}") 	/* MSD-S4/data/teams/data/{id} 	REST-resource: project-entity*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
	public SoftwareProduct getSoftwareProductById(@PathParam("id") Integer id) {
		SoftwareProduct soft = super.getById(id);
		logger.info("**** DEBUG REST getById(" + id +") = " + soft);
		return soft;
	}
	
	@POST 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	public Collection<SoftwareProduct> addIntoCollection(SoftwareProduct soft) {
		// save aggregate
		super.add(soft);
		logger.info("**** DEBUG REST save aggregate POST");
		// return updated collection
		return super.toCollection();
	}
	
	@PUT @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	@Override
	public SoftwareProduct add(SoftwareProduct soft) {
		soft = super.add(soft);
		// return updated collection	
		return soft;
	}	
	
//	@Override
	@DELETE 				/* MSD-S4/data/projects 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public Collection<SoftwareProduct> removeFromCollection(SoftwareProduct s) {
		logger.info("DEBUG: called REMOVE - team: " + s);
		super.remove(s);
		return super.toCollection();
	}
	
	@DELETE @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public void remove(@PathParam("id")Integer id) {
		logger.info("DEBUG: called REMOVE - ById() for teams >>>>>>>>>>>>>> simplified ! + id");
		SoftwareProduct s = super.getById(id);
		super.remove(s);
	}
	
	
	/*public String getMessage() {
		return "SoftwareProduct DataService is working...";
	}*/
	
	
	@GET @Path("/test") // Check if resource is up ...
	@Produces({ MediaType.TEXT_PLAIN})
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "TeamEmployeeDataService is working...";
	}

	@Override
	public SoftwareProduct addSoftwareProduct(SoftwareProduct SoftwareProductToAdd) {
		// TODO Auto-generated method stub
		em.persist(SoftwareProductToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(SoftwareProductToAdd);
		return SoftwareProductToAdd;
		
	}

	@Override
	public String removeSoftwareProduct(SoftwareProduct softwareProductToDelete) {
		// TODO Auto-generated method stub
		softwareProductToDelete = em.merge(softwareProductToDelete);
		em.remove(softwareProductToDelete);
		em.flush();
		return "True";
	}

	@Override
	public Collection<SoftwareProduct>  getSoftwareProducts() {
		// TODO Auto-generated method stub
		List<SoftwareProduct> softwareProducts = em.createQuery("SELECT s FROM SoftwareProduct s", SoftwareProduct.class)
				.getResultList();
		return softwareProducts;
	}

	/*@Override
	public SoftwareProduct getSoftwareProductById(Integer softId) {
		// TODO Auto-generated method stub
		return null;
	}*/

	// dummy XML marshall Rest: http://localhost:8080/MSD-S4/data/projects/projectdata
	@GET @Path("/teamdata")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getTeamData() throws Exception{
		SoftwareProduct dto = new SoftwareProduct(7777, "Soft 777");
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
