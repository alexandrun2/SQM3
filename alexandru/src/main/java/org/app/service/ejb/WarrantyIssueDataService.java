package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.WarrantyIssue;

@Remote
public interface WarrantyIssueDataService extends EntityRepository<WarrantyIssue>  {

	String getMessage();

	WarrantyIssue addWarrantyIssue( WarrantyIssue wiToAdd);
	
	String removeWarrantyIssue ( WarrantyIssue wiToDelete);
	
	Collection< WarrantyIssue> getWarrantyIssues();
	
}
