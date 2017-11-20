package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.Team;

@Remote
public interface TeamDataService extends EntityRepository<Team>{

	String getMessage();

	Team addTeam(Team teamToAdd);
	
	String removeTeam(Team teamToDelete);
	
	Collection<Team> getTeams();
	
}
