package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.Request;

@Stateless @LocalBean
public class RequestDataServiceEJB extends EntityRepositoryBase<Request> 
implements RequestDataService{

	private static Logger logger = Logger.getLogger(RequestDataServiceEJB.class.getName());
	
	@PostConstruct
	public void init() {
		logger.info("POSTCONSTRUCT-INIT injected EntityManager: " + this.em);
	}


	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "RequestDataService is working...";
	}

	@Override
	public Request addRequest(Request requestToAdd) {
		// TODO Auto-generated method stub
		em.persist(requestToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(requestToAdd);
		return requestToAdd;
	}

	@Override
	public String removeRequest(Request requestToDelete) {
		// TODO Auto-generated method stub
		requestToDelete = em.merge(requestToDelete);
		em.remove(requestToDelete);
		em.flush();
		return "True";
	}

	@Override
	public Collection<Request> getRequests() {
		// TODO Auto-generated method stub
		List<Request> req = em.createQuery("SELECT r FROM Request r", Request.class)
				.getResultList();
		return req;
	}

}
