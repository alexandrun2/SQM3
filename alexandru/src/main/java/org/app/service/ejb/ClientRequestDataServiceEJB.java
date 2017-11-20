package org.app.service.ejb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.Client;
import org.app.service.entities.Request;
import org.app.service.entities.SoftwareProduct;

@Stateless @LocalBean
public class ClientRequestDataServiceEJB extends EntityRepositoryBase<Client> 
implements ClientRequestDataService,  Serializable{

	private static Logger logger = Logger.getLogger(ClientRequestDataServiceEJB.class.getName());
	
	@EJB // injected DataService
	private RequestDataService requestDataService; 
	// Local component-entity-repository
	private EntityRepository<Client> clientRepository;
	
	@EJB 
	private SoftwareProductDataService SoftwareproductDataService; 
	private EntityRepository<SoftwareProduct> softwareProductRepository;
	
	@PostConstruct
	public void init() {
		// local instantiation of local component-entity-repository
		clientRepository = new EntityRepositoryBase<Client>(this.em,Client.class);
		logger.info("POSTCONSTRUCT-INIT teamRepository: " + this.clientRepository);
		logger.info("POSTCONSTRUCT-INIT employeeDataService: " + this.requestDataService);
	}
	
@Override
public Client createNewClient(Integer id) {
	// TODO Auto-generated method stub
			// create project aggregate
	
			/*SoftwareProduct product = new SoftwareProduct(id, "Software" + id);*/
	
			if (SoftwareproductDataService.getById(id)==null)
			{
				SoftwareproductDataService.addSoftwareProduct(new SoftwareProduct(id, "Software" + id));
			}
	
			
			Client client = new Client(id, "user" + (100 + id), "pass" + (100 + id), "fc" + (100 + id),
					"Name " + (100 + id), "075" + (100 + id) + (300 + id) + id, "Adress " + (100 + id), "test@mail.com" );
			Set<Request> requests = new HashSet<>();
			
			Date dd = Calendar.getInstance().getTime();
			
			Integer requestsCount = 3;
			for (int i=933; i<requestsCount+933; i++){
				requests.add(new Request(i, dd, " I can't add any entities in main module", "New", "ASSISTANCE", 
							client,SoftwareproductDataService.getById(id)));
			}
			
			client.setRequest_change(requests);
			
			// save warranty aggregate
			this.add(client);
			// return warranty aggregate to service client
			return client;
}

@Override
public Client getClientById(Integer clientId) {
	// TODO Auto-generated method stub
	return clientRepository.getById(clientId);
}

@Override
public String getMessage() {
	// TODO Auto-generated method stub
	return "ClientReuqestDataService is working...";
}

}
