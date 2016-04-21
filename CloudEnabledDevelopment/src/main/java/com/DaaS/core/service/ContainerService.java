package com.DaaS.core.service;

import java.util.List;

import com.DaaS.core.objects.Container;


public interface ContainerService extends BaseService{

	public void save(Container  entity) throws CloudDevException;
	
	public void saveUpdate(Container  entity) throws CloudDevException;
	
	public Container getContainerById(Long id) throws CloudDevException;
	
	public List<Container> getContainerByIds(List<Long> ids) throws CloudDevException;
	
	public void deleteContainerById(Long id )throws CloudDevException;

	public List<Container> findAll()throws CloudDevException;

	public Container findOne(Long id)throws CloudDevException;

	public Long count()throws CloudDevException;
	
	public Long count(Long instanceId)throws CloudDevException;

	public int deleteAll();

	public List<Container> findAllForInstance(Long instance_id);
	
}
