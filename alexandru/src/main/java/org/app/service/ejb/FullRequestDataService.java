package org.app.service.ejb;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.Request;

@Remote
public interface FullRequestDataService extends EntityRepository<Request> {

	Request createNewFullRequest(Integer id);
	String getMessage();
	Request geRequestById(Integer requestId);
}
