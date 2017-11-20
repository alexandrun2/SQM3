package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.Request;

@Remote
public interface RequestDataService  extends EntityRepository<Request> {

	String getMessage();

	Request addRequest(Request requestToAdd);
	
	String removeRequest(Request requestToDelete);
	
	Collection<Request> getRequests();
}
