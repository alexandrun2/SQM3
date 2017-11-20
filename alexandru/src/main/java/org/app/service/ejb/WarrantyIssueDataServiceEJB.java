package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.WarrantyIssue;

@Stateless @LocalBean 
public class WarrantyIssueDataServiceEJB extends EntityRepositoryBase< WarrantyIssue> 
implements WarrantyIssueDataService{

	private static Logger logger = Logger.getLogger(WarrantyIssueDataServiceEJB.class.getName());
	
	@PostConstruct
	public void init() {
		logger.info("POSTCONSTRUCT-INIT injected EntityManager: " + this.em);
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "WarrantyIssueDataService is working...";
	}

	@Override
	public WarrantyIssue addWarrantyIssue(WarrantyIssue wiToAdd) {
		// TODO Auto-generated method stub
		em.persist(wiToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(wiToAdd);
		return wiToAdd;
	}

	@Override
	public String removeWarrantyIssue(WarrantyIssue wiToDelete) {
		// TODO Auto-generated method stub
		wiToDelete = em.merge(wiToDelete);
		em.remove(wiToDelete);
		em.flush();
		return "True";
	}

	@Override
	public Collection<WarrantyIssue> getWarrantyIssues() {
		// TODO Auto-generated method stub
		List<WarrantyIssue> wi = em.createQuery("SELECT w FROM WarrantyIssue w", WarrantyIssue.class)
				.getResultList();
		return wi;
	}

}
