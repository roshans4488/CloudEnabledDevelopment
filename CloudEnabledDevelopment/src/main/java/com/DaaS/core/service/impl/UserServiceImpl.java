package com.DaaS.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DaaS.core.objects.User;
import com.DaaS.core.repository.UserRepository;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.InstanceService;
import com.DaaS.core.service.UserService;

@Service("userService")
@Transactional(readOnly=true)
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	
	
	public UserServiceImpl() {
	}

	@Override
	@Transactional
	public void save(User entity) throws CloudDevException {
		userRepository.save(entity);
		
	}

	@Override
	@Transactional
	public void saveUpdate(User entity) throws CloudDevException {
		userRepository.save(entity);
	}

	@Override
	public User getUserById(Long id) throws CloudDevException {
		return userRepository.findOne(id);
	}

	@Override
	public List<User> getUserByIds(List<Long> ids) throws CloudDevException {
		return userRepository.findAll(ids);
	}

	@Override
	public void deleteUserById(Long id) throws CloudDevException {
		userRepository.deleteById(id);
		
	}

	@Override
	public List<User> findAll() throws CloudDevException {
		return userRepository.findAll();
	}

	@Override
	public User findOne(Long id) throws CloudDevException {
		return userRepository.findOne(id);
	}

	@Override
	public Long count() throws CloudDevException {
		return userRepository.count();
	}

	@Override
	public int deleteAll() {
		return userRepository.deleteAll();
	}

	@Override
	public List<User> findAllAccountsByUserId(Long user_id) {
		return userRepository.findAllAccountsByUserId(user_id);
	}

	@Override
	public User findUserByName(String username) {
		return userRepository.findUserByName(username);
	}

}
