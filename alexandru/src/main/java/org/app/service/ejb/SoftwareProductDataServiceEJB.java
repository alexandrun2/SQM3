package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.EntityBase;
import org.app.service.entities.SoftwareProduct;

@Stateless @LocalBean
public class SoftwareProductDataServiceEJB 
	extends EntityRepositoryBase<SoftwareProduct> 
	implements  SoftwareProductDataService{


	private static Logger logger = Logger.getLogger(SoftwareProductDataServiceEJB.class.getName());
	
	//private EntityRepository<SoftwareProduct> entityRepository;
	
	@PostConstruct
	public void init() {
		logger.info("POSTCONSTRUCT-INIT injected EntityManager: " + this.em);
	}

	public String getMessage() {
		return "SoftwareProduct DataService is working...";
	}

	@Override
	public SoftwareProduct addSoftwareProduct(SoftwareProduct SoftwareProductToAdd) {
		// TODO Auto-generated method stub
		em.persist(SoftwareProductToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(SoftwareProductToAdd);
		return SoftwareProductToAdd;
		
	}

	@Override
	public String removeSoftwareProduct(SoftwareProduct softwareProductToDelete) {
		// TODO Auto-generated method stub
		softwareProductToDelete = em.merge(softwareProductToDelete);
		em.remove(softwareProductToDelete);
		em.flush();
		return "True";
	}

	@Override
	public Collection<SoftwareProduct>  getSoftwareProducts() {
		// TODO Auto-generated method stub
		List<SoftwareProduct> softwareProducts = em.createQuery("SELECT s FROM SoftwareProduct s", SoftwareProduct.class)
				.getResultList();
		return softwareProducts;
	}

}
