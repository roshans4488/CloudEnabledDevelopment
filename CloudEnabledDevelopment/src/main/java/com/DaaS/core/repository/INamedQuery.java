package com.DaaS.core.repository;

/**
 * @author rosha
 *
 */
public interface INamedQuery {

	
	public String findAllQuery();
	
	public String findAllByIdsQuery();
	
	public String countQuery();
}
