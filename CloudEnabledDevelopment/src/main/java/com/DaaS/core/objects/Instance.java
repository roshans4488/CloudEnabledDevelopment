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
public class Instance {

	@Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "instance")
	   @TableGenerator(
	      name = "instance",
	      table = "sequences",
	      pkColumnName = "key",
	      pkColumnValue = "instance",
	      valueColumnName = "seed",
	      allocationSize=1
	   )
	private Long id;
	
	
	
	private String imageId;
	private String instanceType;
	private int minCount;
	private int maxCount;
	private String keyName;
	private String securityGroup;
	
	
	
	
	//getters and setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
	public int getMinCount() {
		return minCount;
	}
	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getSecurityGroup() {
		return securityGroup;
	}
	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}
}
