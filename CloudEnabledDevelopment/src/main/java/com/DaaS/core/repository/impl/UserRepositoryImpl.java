package com.DaaS.core.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.DaaS.core.objects.Container;
import com.DaaS.core.objects.Instance;
import com.DaaS.core.objects.User;
import com.DaaS.core.repository.InstanceRepository;
import com.DaaS.core.repository.UserRepository;

@Repository
@Transactional
public class UserRepositoryImpl extends BaseJpaRepository<User, Long> implements UserRepository {

	public UserRepositoryImpl() {
		super.setEntityClass(User.class);
	} 

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	
	@Override
	public String findAllQuery() {
		return "select u from User u";
	}
	
	@Override
	public String findAllByIdsQuery() {
		return "select u from User u where u.id IN :ids";
	}
	
	
	@Override
	public String countQuery() {
		return "select COUNT(u.id) from User u";
	}


	@Override
	public List<User> findAllAccountsByUserId(Long user_id) {
		String queryStr="db.User.find({'userId':"+user_id+"})"; 
		System.out.println(queryStr);
		Query query=getEntityManager().createNativeQuery(queryStr,User.class);
		
		
		List<User> entities = (List<User>) query.getResultList();
		
		return entities;
	}
	
}
