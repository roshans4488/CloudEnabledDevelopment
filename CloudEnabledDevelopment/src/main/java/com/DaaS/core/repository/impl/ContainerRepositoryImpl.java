package com.DaaS.core.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.DaaS.core.objects.Container;
import com.DaaS.core.objects.Instance;
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
		return "db.Container.count({})";
	}

	@Override
	public Long count(Long instanceId) {
		String queryStr="db.Container.count({ 'instanceId' : "+instanceId+" })";
		Query query=getEntityManager().createNativeQuery(queryStr);
		long count=(Long)query.getSingleResult();
		return count;
	}

	@Override
	public List<Container> findAllForInstance(Long instance_id) {

		String queryStr="db.Container.find({'instanceId':"+instance_id+"})"; 
		System.out.println(queryStr);
		Query query=getEntityManager().createNativeQuery(queryStr,Container.class);
		
		
		List<Container> entities = (List<Container>) query.getResultList();
		
		return entities;
	
	}	
	
}
