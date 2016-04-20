package com.DaaS.core.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.DaaS.core.objects.Instance;
import com.DaaS.core.repository.InstanceRepository;


/**
 * @author rosha
 *
 */
@Repository
@Transactional
public class InstanceRepositoryImpl extends BaseJpaRepository<Instance, Long> implements InstanceRepository {
	
	
	public InstanceRepositoryImpl() {
		super.setEntityClass(Instance.class);
	}

	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	
	@Override
	public String findAllQuery() {
		return "select i from Instance i";
	}

	@Override
	public String findAllByIdsQuery() {
		return "select i from Instance i where i.id IN :ids";
	}
	
	
	@Override
	public String countQuery() {
		return "select COUNT(i.id) from Instance i";
	}


	@Override
	public List<Instance> findAllForUser(Long user_id) {
	
		String queryStr="db.Instance.find({'userId':"+user_id+"})"; //
		System.out.println(queryStr);
		Query query=getEntityManager().createNativeQuery(queryStr);
		
		
		
		
		
		List<Instance> entities = query.getResultList();
		return entities;
		
		
		/*
		String jpqlQuery = "select i from Instance i where i.user ="+user_id;
		Query query = getEntityManager().createQuery(jpqlQuery, getEntityClass());
		//query.setParameter("ids", arg0);

		List<Instance> entities = query.getResultList();
		return entities;
		*/
		
		
	}	
	
}
