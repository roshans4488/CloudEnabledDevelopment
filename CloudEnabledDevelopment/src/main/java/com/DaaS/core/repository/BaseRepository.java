
package com.DaaS.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author rosha
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseRepository<T, ID>  {

	public Page<T> findAll(Pageable pageable);
	
	public <S extends T> S save(S entity);

	public T findOne(ID id);
	
	public boolean exists(ID id) ;
	
	public long count();
	
	public long latest();

	public void deleteById(ID id);
	
	public void delete(T entity) ;

	public void delete(Iterable<? extends T> entities) ;
	
	public int deleteAll() ;
	
	public void deleteAllInBatch();
	
	public void deleteInBatch(Iterable<T> arg0) ;
	
	public List<T> findAll() ;
	
	public List<T> findAll(Sort arg0) ;

	public List<T> findAll(Iterable<ID> arg0);

	public void flush();
	
	public T getOne(ID arg0) ;

	public <S extends T> List<S> save(Iterable<S> arg0);
	
	public <S extends T> S saveAndFlush(S arg0) ;
	


	
	

}
