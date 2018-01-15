package org.app.service.entities;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@XmlRootElement(name="employee") 
@XmlAccessorType(XmlAccessType.NONE)
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
	
	@XmlElement
	public Date getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	
	@XmlElement
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	
	@XmlElement
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	@XmlElement
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	//@XmlElement
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
	
	public static String BASE_URL = Team.BASE_URL;
	@XmlElement(name = "link")
    public AtomLink getLink() throws Exception {
		String restUrl = BASE_URL 
				+ ((this.getTeam() != null) ? this.getTeam().getTeam_id() : "")
				+ "/employee/" 
				+ this.getId_User();
        return new AtomLink(restUrl, "get-employee");
    }	
	public void setLink(AtomLink link){}

	

	
}
