package com.DaaS.core.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.DaaS.core.objects.Container;
import com.DaaS.core.repository.ContainerRepository;


@Repository
@Transactional
public class ContainerRepositoryImpl extends BaseJpaRepository<Container, Long> implements ContainerRepository {

	public ContainerRepositoryImpl() {
		super.setEntityClass(Container.class);
	}
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	@Override
	public String findAllQuery() {
		return "select c from Container c";
	}
	

	@Override
	public String findAllByIdsQuery() {
		return "select c from Container c where c.id IN :ids";
	}
	
	@Override
	public String countQuery() {
		return "select COUNT(c.id) from Container c";
	}	
	
}
