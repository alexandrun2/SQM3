package org.app.service.entities;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee extends User1 {

	@Temporal(value = TemporalType.DATE)
	private Date date_of_birth;
	private String first_name;
	private String last_name;
	private String role;
	@ManyToOne
	private Team team;
	public Date getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public Employee(Integer id_User, String user_name, String user_password,  String first_name,
			String last_name, String role) {
		super(id_User, user_name, user_password);
		//this.date_of_birth = date_of_birth;
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;
	}
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Employee(Integer id_User, String user_name, String user_password, Date date_of_birth, String first_name,
			String last_name, String role, Team team) {
		super(id_User, user_name, user_password);
		this.date_of_birth = date_of_birth;
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;
		this.team = team;
	}

	

	
}
