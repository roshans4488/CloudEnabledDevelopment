package com.DaaS.core.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

/**
 * @author rosha
 *
 */
@Entity
public class User {

	@Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "user")
	   @TableGenerator(
	      name = "user",
	      table = "sequences",
	      pkColumnName = "key",
	      pkColumnValue = "user",
	      valueColumnName = "seed",
	      allocationSize=1
	   )
	private Long id;
	
	
	
	private String name;
	private String accessKey;
	private String secretKey;
	private String privateKey;
	
	
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	//getters and setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	
	
	
	
	
}
