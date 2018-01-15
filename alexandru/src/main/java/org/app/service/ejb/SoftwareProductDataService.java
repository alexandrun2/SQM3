package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.SoftwareProduct;
import org.app.service.entities.Team;

@Remote
public interface SoftwareProductDataService extends EntityRepository<SoftwareProduct> {

	String getMessage();

	SoftwareProduct addSoftwareProduct(SoftwareProduct SoftwareProductToAdd);
	
	String removeSoftwareProduct(SoftwareProduct softwareProductToDelete);
	
	Collection<SoftwareProduct> getSoftwareProducts();
	
	SoftwareProduct getSoftwareProductById(Integer softId);
	
}
