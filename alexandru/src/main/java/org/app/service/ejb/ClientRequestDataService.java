package org.app.service.ejb;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.Client;

@Remote
public interface ClientRequestDataService extends EntityRepository<Client> {

	// create aggregate entity: client root with requests as components
			Client createNewClient(Integer id);
			// Query method on release components
			Client getClientById(Integer clientId);
			// Other
			String getMessage();
}
