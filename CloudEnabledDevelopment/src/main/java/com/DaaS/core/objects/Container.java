package com.DaaS.core.objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

@Entity
public class Container {
	
	@Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "container")
	   @TableGenerator(
	      name = "container",
	      table = "sequences",
	      pkColumnName = "key",
	      pkColumnValue = "container",
	      valueColumnName = "seed",
	      allocationSize=1
	   )
	private Long projectId;
	private String projectName;
	private String dockerName;
	private String projectType;
	private String buildType;
	private String ipAddress;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="instanceId")
	private Instance instance;

	public Container() {
		super();
		// TODO Auto-generated constructor stub
	}

	//getters and setters
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDockerName() {
		return dockerName;
	}

	public void setDockerName(String dockerName) {
		this.dockerName = dockerName;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getBuildType() {
		return buildType;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}
	

}
