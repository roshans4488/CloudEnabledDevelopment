package com.DaaS.core.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.DaaS.core.objects.Instance;


/**
 * @author rosha
 *
 */

public interface InstanceService extends BaseService {

	
	public void save(Instance  entity) throws CloudDevException;
	
	public void saveUpdate(Instance  entity) throws CloudDevException;
	
	public Instance getInstanceById(Long id) throws CloudDevException;
	
	public List<Instance> getInstanceByIds(List<Long> ids) throws CloudDevException;
	
	public void deleteInstanceById(Long id )throws CloudDevException;

	public List<Instance> findAll()throws CloudDevException;

	public Instance findOne(Long id)throws CloudDevException;

	public Long count()throws CloudDevException;

	public int deleteAll();

	public List<Instance> findAllForUser(Long user_id);
}
