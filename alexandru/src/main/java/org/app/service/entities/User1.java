package org.app.service.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User1  implements /*Comparable<User1>,*/ Serializable {

	@Id /*@GeneratedValue @NotNull*/
	private Integer Id_User;
	private String user_name;
	private String user_password;
	public Integer getId_User() {
		return Id_User;
	}
	public void setId_User(Integer id_User) {
		Id_User = id_User;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
/*	
 @Override
	public int compareTo(User1 o) {
		// TODO Auto-generated method stub
		if (this.equals(o))
			return 0;
		return this.getUser_name().compareTo(o.getUser_name());
	}
	*/
	public User1(Integer id_User, String user_name, String user_password) {
		super();
		Id_User = id_User;
		this.user_name = user_name;
		this.user_password = user_password;
	}
	public User1() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
