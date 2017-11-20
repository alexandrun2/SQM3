package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.Team;

@Stateless @LocalBean
public class TeamDataServiceEJB extends EntityRepositoryBase<Team> 
implements TeamDataService{

	private static Logger logger = Logger.getLogger(TeamDataServiceEJB.class.getName());
	
	@PostConstruct
	public void init() {
		logger.info("POSTCONSTRUCT-INIT injected EntityManager: " + this.em);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "TeamDataService is working...";
	}

	@Override
	public Team addTeam(Team teamToAdd) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				em.persist(teamToAdd);
				em.flush();
				// transactions are managed by default by container
				em.refresh(teamToAdd);
				return teamToAdd;
	}

	@Override
	public String removeTeam(Team teamToDelete) {
		// TODO Auto-generated method stub
		teamToDelete = em.merge(teamToDelete);
		em.remove(teamToDelete);
		em.flush();
		return "True";
	}

	@Override
	public Collection<Team> getTeams() {
		// TODO Auto-generated method stub
		List<Team> teams = em.createQuery("SELECT t FROM Team t", Team.class)
				.getResultList();
		return teams;
	}



}
