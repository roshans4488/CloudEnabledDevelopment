package com.DaaS.core.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DaaS.core.objects.Instance;
import com.DaaS.core.repository.InstanceRepository;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.InstanceService;

/**
 * @author rosha
 *
 */
@Service("instanceService")
@Transactional(readOnly=true)

public class InstanceServiceImpl implements InstanceService{

	

	@Autowired
	private InstanceRepository instanceRepository;
	
	public InstanceServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@Transactional
	public void save(Instance entity) throws CloudDevException {
		instanceRepository.save(entity);
	}

	@Override
	@Transactional
	public void saveUpdate(Instance entity) throws CloudDevException {
		instanceRepository.save(entity);
	}

	@Override
	public Instance getInstanceById(Long id) throws CloudDevException {
		return instanceRepository.findOne(id);
	}

	@Override
	public List<Instance> getInstanceByIds(List<Long> ids) throws CloudDevException {
		return instanceRepository.findAll(ids);
	}

	@Override
	public void deleteInstanceById(Long id) throws CloudDevException {
		instanceRepository.deleteById(id);		
	}

	@Override
	public List<Instance> findAll() throws CloudDevException {
		return instanceRepository.findAll();
	}


	@Override
	public Instance findOne(Long id) throws CloudDevException {
		return instanceRepository.findOne(id);
	}
	
	
	@Override
	public Long count() throws CloudDevException {
		return instanceRepository.count();
	}

	@Override
	public int deleteAll() {
		return instanceRepository.deleteAll();
	}

	@Override
	public List<Instance> findAllForUser(Long user_id) {
		return instanceRepository.findAllForUser(user_id);
	}

	

}
