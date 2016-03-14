package com.DaaS.core.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.DaaS.core.repository.BaseRepository;
import com.DaaS.core.repository.INamedQuery;


/**
 * @author rosha
 *
 * @param <T>
 * @param <ID>
 */
@Repository
@Transactional(readOnly=true)
public class BaseJpaRepository<T,ID> implements BaseRepository<T, ID>, INamedQuery {

	private Class<T> entityClass;
		 
	
	private EntityManager entityManager;

	@Override
	public Page<T> findAll(Pageable pageable) {
		String sqlQuery=findAllQuery();
		Query query=getEntityManager().createQuery(sqlQuery);
		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		
		return (Page<T>) query.getResultList();
	}

	@Override
	@Transactional
	public <S extends T> S save(S entity) {
		entity=getEntityManager().merge(entity);
		getEntityManager().persist(entity);
		return entity;
	}

	@Override
	
	public T findOne(ID id) {
		T entity =	getEntityManager().find(entityClass, id);
		return entity;
	}

	@Override
	
	public boolean exists(ID id) {
		return findOne(id)!=null;
	}

	@Override
	public long count() {
		String queryStr=countQuery();
		Query query=getEntityManager().createQuery(queryStr);
		long count=(Long)query.getSingleResult();
		return count;
	}

	@Override
	public long latest() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public void deleteById(ID id) {
		T t =findOne(id);
		getEntityManager().remove(t);
		
	}

	@Override
	@Transactional
	public void delete(T entity) {
		if(getEntityManager().contains(entity)){
			entity=getEntityManager().merge(entity);
			getEntityManager().remove(entity);
		}
	}

	@Override
	@Transactional
	public void delete(Iterable<? extends T> entities) {
		if(entities!=null){
			for (T entity : entities) {
				delete(entity);
			}
		}
	}

	@Override
	@Transactional
	public int deleteAll() {
		int deleteCount=0;
		List<T>  entities = findAll();
		if(entities!=null){
			for (T entity : entities) {
				delete(entity);
				deleteCount++;
			}
		}
		return deleteCount;
	}

	@Override
	public void deleteAllInBatch() {
		
		
	}

	@Override
	public void deleteInBatch(Iterable<T> arg0) {
		
		
	}

	@Override
	public List<T> findAll() {
		String queryStr=findAllQuery();
		Query query= getEntityManager().createQuery(queryStr);
		List<T> entites = query.getResultList();
		return entites;
	}

	@Override
	public List<T> findAll(Sort arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(Iterable<ID> arg0) {
		String jpqlQuery = findAllByIdsQuery();
		Query query = getEntityManager().createQuery(jpqlQuery, getEntityClass());
		query.setParameter("ids", arg0);

		List<T> entities = query.getResultList();
		return entities;
	}

	@Override
	@Transactional
	public void flush() {
		getEntityManager().flush();
		
	}

	@Override
	public T getOne(ID arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends T> List<S> save(Iterable<S> arg0) {
		List<S> result = new ArrayList<S>();
		if (arg0 == null) {
			return result;
		}
		for (S entity : arg0) {
			result.add(save(entity));
		}
		return result;
	}

	@Override
	public <S extends T> S saveAndFlush(S arg0) {
		arg0=getEntityManager().merge(arg0);
		getEntityManager().persist(arg0);
		getEntityManager().flush();
		return arg0;
	}


	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public String findAllQuery() {
		return "select h from "+ entityClass.getName()+" h";
	}

	@Override
	public String findAllByIdsQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String countQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	


	
	
}
