package org.app.service.ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.Client;
import org.app.service.entities.SoftwareProduct;
import org.app.service.entities.Warranty;
import org.app.service.entities.WarrantyIssue;

@Stateless @LocalBean
public class WarrantyDataServiceEJB extends EntityRepositoryBase<Warranty>
implements WarrantyDataService {

	private static Logger logger = Logger.getLogger(WarrantyDataServiceEJB.class.getName());
	
	@EJB // injected DataService
	private WarrantyIssueDataService WarrantyIssueDataService; 
	private EntityRepository<Warranty> warrantyRepository;
	
	
	@EJB  
	private ClientDataService ClientDataService; 
	private EntityRepository<Client> clientRepository;
	@EJB 
	private SoftwareProductDataService SoftwareproductDataService; 
	private EntityRepository<SoftwareProduct> softwareProductRepository;

	@PostConstruct
	public void init() {
		// local instantiation of local component-entity-repository
		warrantyRepository = new EntityRepositoryBase<Warranty>(this.em,Warranty.class);
		logger.info("POSTCONSTRUCT-INIT releaseRepository: " + this.warrantyRepository);
		logger.info("POSTCONSTRUCT-INIT featureDataService: " + this.WarrantyIssueDataService);
		
		
		logger.info("POSTCONSTRUCT-INIT clientRepository: " + this.clientRepository);
		logger.info("POSTCONSTRUCT-INIT  ClientDataService: " + this.ClientDataService);
		logger.info("POSTCONSTRUCT-INIT softwareProductRepository: " + this.softwareProductRepository);
		logger.info("POSTCONSTRUCT-INIT SoftwareproductDataService: " + this.SoftwareproductDataService);
		
	}
	@Override
	public Warranty createNewWarranty(Integer id) {
		// TODO Auto-generated method stub
		// create project aggregate
				
		SoftwareProduct product = new SoftwareProduct(id, "Software" + id);
		Client client = new Client(id, "user" + (100 + id), "pass" + (100 + id), "fc" + (100 + id),
					"Name " + (100 + id), "075" + (100 + id) + (300 + id) + id, "Adress " + (100 + id), "test@mail.com" );
				
		if (ClientDataService.getById(id) == null )
			{
			ClientDataService.addClient(client);
			}
		if (SoftwareproductDataService.getById(id)==null)
			{
			SoftwareproductDataService.addSoftwareProduct(product);
			}
				
		Date d = Calendar.getInstance().getTime();
		
		Warranty warranty = new Warranty(id, d, 2, "" + id, "Full Warranty", SoftwareproductDataService.getById(id),ClientDataService.getById(id));
		Set<WarrantyIssue> warrantyIssue = new HashSet<>();
				
		Date dd = Calendar.getInstance().getTime();
				
		Integer warrantyIssueCount = 3;
		for (int i=1; i<=warrantyIssueCount; i++)
		{
			warrantyIssue.add(new WarrantyIssue(i, "This software freeze daily", dd, "NEW",  warranty));
		}
				
		warranty.setWarranty_issues(warrantyIssue);
				
		// save warranty aggregate
		this.add(warranty);
		// return warranty aggregate to service client
		return warranty;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "WarrantyDataService is working...";
	}
	@Override
	public Warranty getWarrantyById(Integer warrantyId) {
		// TODO Auto-generated method stub
		return warrantyRepository.getById(warrantyId);
	}

}
