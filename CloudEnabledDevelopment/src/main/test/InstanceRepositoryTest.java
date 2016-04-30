import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.DaaS.core.objects.Instance;
import com.DaaS.core.repository.InstanceRepository;

public class InstanceRepositoryTest {

	
	@Autowired
	InstanceRepository instanceRepository;
	

	

	

	@Test
	public void testSaveS() {
		Instance instance = new Instance();
		instance.setImageId("ami-5189a661");
		instance.setInstanceType("t2.micro");
		instance.setKeyName("Eragon");
		instance.setMaxCount(1);
		instance.setMinCount(1);
		instance.setSecurityGroup("Java Security group");
		instance.setWorkspaceName("Java workspace");
		instance.setWorkspaceType("AWS");
		
		instanceRepository.save(instance);
		
		Assert.assertNotNull(instance.getInstanceId());	
	
	}

	

	

	@Test
	public void testCount() {
		Long count = instanceRepository.count();
		assertEquals(10L,(Object)count);
	}

	

	@Test
	public void testDeleteById() {
		instanceRepository.deleteById(1L);
		Instance instance = instanceRepository.findOne(1L);
		assertNull(instance);
	}

	

	

	@Test
	public void testDeleteAll() {
		int i = instanceRepository.findAll().size();
		int deletedCount = instanceRepository.deleteAll();
		assertEquals(i,deletedCount);
	
	}

	

	
	@Test
	public void testFindAll() {
		List<Instance> instanceList = instanceRepository.findAll();
		
		assertEquals(3,instanceList.size());
	}


	@Test
	public void testFindAllPageable() {
		fail("Not yet implemented");
	}


}
