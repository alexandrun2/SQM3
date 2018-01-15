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
import javax.persistence.EntityManager;
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
import org.app.service.entities.SoftwareProduct;
import org.app.service.entities.Team;

@Path("clients")  /* http://localhost:8080/MSD-S4/data/clients */
@Stateless @LocalBean
public class ClientDataServiceEJB extends EntityRepositoryBase<Client> 
									implements ClientDataService
{
private static Logger logger = Logger.getLogger(ClientDataServiceEJB.class.getName());
	
	//private EntityRepository<SoftwareProduct> entityRepository;
	
	@PostConstruct
	public void init() {
		logger.info("POSTCONSTRUCT-INIT injected EntityManager: " + this.em);
	}

	/*@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "ClientDataService is working...";
	}*/

	@Override
	public Client addClient(Client clientToAdd) {
		// TODO Auto-generated method stub
		em.persist(clientToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(clientToAdd);
		return clientToAdd;
	}

	@Override
	public String removeClient(Client clientToDelete) {
		// TODO Auto-generated method stub
		clientToDelete = em.merge(clientToDelete);
		em.remove(clientToDelete);
		em.flush();
		return "True";
	}

	@Override
	public Collection<Client> getClients() {
		// TODO Auto-generated method stub
		List<Client> clients = em.createQuery("SELECT c FROM Client c", Client.class)
				.getResultList();
		return clients;
	}

	/*@Override
	public Client getClientById(Integer clientId) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	@Override
	@GET 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Collection<Client> toCollection() {
		logger.info("**** DEBUG REST toCollection()");
		return super.toCollection();
	}	
	
	@GET @Path("/{id}") 	/* MSD-S4/data/teams/data/{id} 	REST-resource: project-entity*/
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
	public Client  getClientById(@PathParam("id") Integer id) {
		Client c = super.getById(id);
		logger.info("**** DEBUG REST getById(" + id +") = " + c);
		return c;
	}
	
	@POST 					/* MSD-S4/data/teams 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	public Collection<Client> addIntoCollection(Client c) {
		// save aggregate
		super.add(c);
		logger.info("**** DEBUG REST save aggregate POST");
		// return updated collection
		return super.toCollection();
	}
	
	
	@PUT @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	@Override
	public Client add(Client c) {
		c = super.add(c);
		// return updated collection	
		return c;
	}	
	
	
	@DELETE 				/* MSD-S4/data/projects 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public Collection<Client> removeFromCollection(Client c) {
		logger.info("DEBUG: called REMOVE - team: " + c);
		super.remove(c);
		return super.toCollection();
	}
	
	@DELETE @Path("/{id}") 	/* MSD-S4/data/teams/{id} 	REST-resource: project-entity*/	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public void remove(@PathParam("id")Integer id) {
		logger.info("DEBUG: called REMOVE - ById() for teams >>>>>>>>>>>>>> simplified ! + id");
		Client c = super.getById(id);
		super.remove(c);
	}
	
	@GET @Path("/test") // Check if resource is up ...
	@Produces({ MediaType.TEXT_PLAIN})
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "TeamEmployeeDataService is working...";
	}

	
	

}
