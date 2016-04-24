package com.DaaS.core.service;

import java.util.List;

import com.DaaS.core.objects.User;


public interface UserService extends BaseService {

	
public void save(User  entity) throws CloudDevException;
	
	public void saveUpdate(User  entity) throws CloudDevException;
	
	public User getUserById(Long id) throws CloudDevException;
	
	public List<User> getUserByIds(List<Long> ids) throws CloudDevException;
	
	public void deleteUserById(Long id )throws CloudDevException;

	public List<User> findAll()throws CloudDevException;

	public User findOne(Long id)throws CloudDevException;

	public Long count()throws CloudDevException;

	public int deleteAll();

	public List<User> findAllAccountsByUserId(Long user_id);

	public User findUserByName(String username);
}
