package com.DaaS.core.objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	private Long instanceId;
	
	
	
	
	private String imageId;
	private String instanceType;
	private int minCount;
	private int maxCount;
	private String keyName;
	private String securityGroup;
	private String publicIp;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;
	
	
	//getters and setters
	
	public String getImageId() {
		return imageId;
	}
	public Long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	public String getPublicIp() {
		return publicIp;
	}
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
	
}
