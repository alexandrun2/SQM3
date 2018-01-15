package org.app.service.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="product") 
@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class SoftwareProduct implements /*Comparable<SoftwareProduct>,*/ Serializable {
	@Id /*@GeneratedValue @NotNull*/
	private Integer product_id;
	private String product_name;
	
	@XmlElement
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	
	@XmlElement
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

	public static String BASE_URL = "http://localhost:8080/alexandru/data/products/";
	@XmlElement(name = "link")
    public AtomLink getLink() throws Exception {
		String restUrl = BASE_URL + this.getProduct_id();
        return new AtomLink(restUrl, "get-product");
    }	
	
	public void setLink(AtomLink link){}
	
	
	
	
	
	

	
}
