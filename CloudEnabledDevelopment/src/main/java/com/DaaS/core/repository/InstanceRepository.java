package com.DaaS.core.repository;

import java.util.List;

import com.DaaS.core.objects.Instance;

/**
 * @author rosha
 *
 */
public interface InstanceRepository extends  BaseRepository<Instance,Long>{

	List<Instance> findAllForUser(Long user_id);

}
