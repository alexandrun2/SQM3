package org.app.service.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class Request implements Serializable  {
	@Id
	private Integer request_id;
	@Temporal(value = TemporalType.DATE)
	private Date date_of_registration;
	private String description;
	private String status;
	private String type_of_request;
	@ManyToOne
	private Client client;
	@ManyToOne
	private Team team;
	@ManyToOne 
	private Employee manager;
	@ManyToOne
	private SoftwareProduct software_product;
	public Integer getRequest_id() {
		return request_id;
	}
	public void setRequest_id(Integer request_id) {
		this.request_id = request_id;
	}
	public Date getDate_of_registration() {
		return date_of_registration;
	}
	public void setDate_of_registration(Date date_of_registration) {
		this.date_of_registration = date_of_registration;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType_of_request() {
		return type_of_request;
	}
	public void setType_of_request(String type_of_request) {
		this.type_of_request = type_of_request;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public Employee getManager() {
		return manager;
	}
	public void setManager(Employee manager) {
		this.manager = manager;
	}
	public SoftwareProduct getSoftware_product() {
		return software_product;
	}
	public void setSoftware_product(SoftwareProduct software_product) {
		this.software_product = software_product;
	}
	public Request(Integer request_id, Date date_of_registration, String description, String status,
			String type_of_request) {
		super();
		this.request_id = request_id;
		this.date_of_registration = date_of_registration;
		this.description = description;
		this.status = status;
		this.type_of_request = type_of_request;
	}
	public Request(Integer request_id, Date date_of_registration, String description, String status,
			String type_of_request, Client client, Team team, Employee manager, SoftwareProduct software_product) {
		super();
		this.request_id = request_id;
		this.date_of_registration = date_of_registration;
		this.description = description;
		this.status = status;
		this.type_of_request = type_of_request;
		this.client = client;
		this.team = team;
		this.manager = manager;
		this.software_product = software_product;
	}
	public Request() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Request(Integer request_id, Date date_of_registration, String description, String status,
			String type_of_request, Client client, Employee manager, SoftwareProduct software_product) {
		super();
		this.request_id = request_id;
		this.date_of_registration = date_of_registration;
		this.description = description;
		this.status = status;
		this.type_of_request = type_of_request;
		this.client = client;
		this.manager = manager;
		this.software_product = software_product;
	}
	public Request(Integer request_id, Date date_of_registration, String description, String status,
			String type_of_request, Client client) {
		super();
		this.request_id = request_id;
		this.date_of_registration = date_of_registration;
		this.description = description;
		this.status = status;
		this.type_of_request = type_of_request;
		this.client = client;
	}
	public Request(Integer request_id, Date date_of_registration, String description, String status,
			String type_of_request, Client client, SoftwareProduct software_product) {
		super();
		this.request_id = request_id;
		this.date_of_registration = date_of_registration;
		this.description = description;
		this.status = status;
		this.type_of_request = type_of_request;
		this.client = client;
		this.software_product = software_product;
	}
	
	
	
	
	
	
	
}
