package org.app.service.ejb;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.Team;

@Remote
public interface TeamEmployeeDataService  extends EntityRepository<Team>{

	// create aggregate entity: team root with employees as components
		Team createNewTeam(Integer id);
		// Query method on release components
		Team getTeamById(Integer teamid);
		// Other
		String getMessage();
}
