package com.DaaS.core.repository;

import java.util.List;

import com.DaaS.core.objects.Instance;
import com.DaaS.core.objects.User;

public interface UserRepository extends  BaseRepository<User,Long>{

	List<User> findAllAccountsByUserId(Long user_id);

}
