package org.app.service.ejb;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.SoftwareProduct;
import org.app.service.entities.Warranty;
import org.app.service.entities.WarrantyIssue;

@Remote
public interface WarrantyDataService extends EntityRepository<Warranty>{

	Warranty createNewWarranty(Integer id);
	String getMessage();
	Warranty getWarrantyById(Integer warrantyId);

}
