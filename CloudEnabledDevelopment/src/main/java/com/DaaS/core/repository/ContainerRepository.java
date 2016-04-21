package com.DaaS.core.repository;

import java.util.List;

import com.DaaS.core.objects.Container;

public interface ContainerRepository extends  BaseRepository<Container,Long>{

	Long count(Long instanceId);

	List<Container> findAllForInstance(Long instance_id);

}
