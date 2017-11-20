package org.app.service.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Client extends User1 {

	private String fiscal_code;
	private String name;
	private String phone_number;
	private String adress;
	private String email_adress;
	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Request> request_change = new HashSet<Request>();
	public String getFiscal_code() {
		return fiscal_code;
	}
	public void setFiscal_code(String fiscal_code) {
		this.fiscal_code = fiscal_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getEmail_adress() {
		return email_adress;
	}
	public void setEmail_adress(String email_adress) {
		this.email_adress = email_adress;
	}
	public Set<Request> getRequest_change() {
		return request_change;
	}
	public void setRequest_change(Set<Request> request_change) {
		this.request_change = request_change;
	}
	public Client(Integer id_User, String user_name, String user_password, String fiscal_code, String name,
			String phone_number, String adress, String email_adress) {
		super(id_User, user_name, user_password);
		this.fiscal_code = fiscal_code;
		this.name = name;
		this.phone_number = phone_number;
		this.adress = adress;
		this.email_adress = email_adress;
		//this.request_change = request_change;
	}
	public Client() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Client(Integer id_User, String user_name, String user_password) {
		super(id_User, user_name, user_password);
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
}
