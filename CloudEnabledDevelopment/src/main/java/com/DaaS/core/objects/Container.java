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
	private String dockerID;
	private String projectType;
	private String buildType;
	private String ec2ipAddress;
	private String groupId;
	private String artifactId;
	private String version;
	private String packageName;
	private String agentPort;
	private String ttyPort;
	private String userPort;
	private String ncPort;
	


	

	



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

	public String getDockerID() {
		return dockerID;
	}

	public void setDockerID(String dockerID) {
		this.dockerID = dockerID;
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



	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}
	
	
	public String getEc2ipAddress() {
		return ec2ipAddress;
	}

	public void setEc2ipAddress(String ec2ipAddress) {
		this.ec2ipAddress = ec2ipAddress;
	}


	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	
	public String getAgentPort() {
		return agentPort;
	}

	public void setAgentPort(String agentPort) {
		this.agentPort = agentPort;
	}

	public String getTtyPort() {
		return ttyPort;
	}

	public void setTtyPort(String ttyPort) {
		this.ttyPort = ttyPort;
	}

	public String getUserPort() {
		return userPort;
	}

	public void setUserPort(String userPort) {
		this.userPort = userPort;
	}
	
	public String getNcPort() {
		return ncPort;
	}

	public void setNcPort(String ncPort) {
		this.ncPort = ncPort;
	}
}
