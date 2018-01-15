package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.Client;
import org.app.service.entities.SoftwareProduct;

@Remote
public interface ClientDataService extends EntityRepository<Client> {

	String getMessage();

	Client addClient(Client clientToAdd);
	
	String removeClient(Client clientToDelete);
	
	Collection<Client> getClients();
	
	Client getClientById(Integer clientId);
	
}
