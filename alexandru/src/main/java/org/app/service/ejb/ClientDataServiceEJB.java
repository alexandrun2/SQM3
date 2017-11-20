package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.Client;


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

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "ClientDataService is working...";
	}

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

	

}
