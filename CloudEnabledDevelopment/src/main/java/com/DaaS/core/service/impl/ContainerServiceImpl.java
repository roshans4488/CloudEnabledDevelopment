package com.DaaS.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DaaS.core.objects.Container;
import com.DaaS.core.repository.ContainerRepository;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.ContainerService;

@Service("containerService")
@Transactional(readOnly=true)
public class ContainerServiceImpl implements ContainerService{

	public ContainerServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private ContainerRepository containerRepository;
	
	@Override
	@Transactional
	public void save(Container entity) throws CloudDevException {
		containerRepository.save(entity);
	}

	@Override
	@Transactional
	public void saveUpdate(Container entity) throws CloudDevException {
		containerRepository.save(entity);		
	}

	@Override
	public Container getContainerById(Long id) throws CloudDevException {
		return containerRepository.findOne(id);
	}

	@Override
	public List<Container> getContainerByIds(List<Long> ids) throws CloudDevException {
		return containerRepository.findAll(ids);
	}

	@Override
	public void deleteContainerById(Long id) throws CloudDevException {
		containerRepository.deleteById(id);
		
	}

	@Override
	public List<Container> findAll() throws CloudDevException {
		return containerRepository.findAll();
	}

	@Override
	public Container findOne(Long id) throws CloudDevException {
		return containerRepository.findOne(id);
	}

	@Override
	public Long count() throws CloudDevException {
		return containerRepository.count();
	}

	@Override
	public int deleteAll() {
		return containerRepository.deleteAll();
	}

	@Override
	public Long count(Long instanceId) throws CloudDevException {
		return containerRepository.count(instanceId);
	}

	@Override
	public List<Container> findAllForInstance(Long instance_id) {
		return containerRepository.findAllForInstance(instance_id);
	}

}
