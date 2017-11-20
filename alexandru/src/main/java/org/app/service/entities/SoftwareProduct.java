package org.app.service.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class SoftwareProduct implements /*Comparable<SoftwareProduct>,*/ Serializable {
	@Id /*@GeneratedValue @NotNull*/
	private Integer product_id;
	private String product_name;
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public SoftwareProduct(Integer product_id, String product_name) {
		super();
		this.product_id = product_id;
		this.product_name = product_name;
	}
	public SoftwareProduct() {
		super();
		// TODO Auto-generated constructor stub
	}
	/*
	 * @Override
	public int compareTo(SoftwareProduct o) {
		// TODO Auto-generated method stub
		if (this.equals(o))
			return 0;
		return this.getProduct_name().compareTo(o.getProduct_name());
	}
	*/

	
	
	
	

	
}
