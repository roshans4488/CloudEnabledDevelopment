/**
 * 
 */
package com.DaaS.core.repository;

import static org.junit.Assert.*;

import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.DaaS.core.objects.Instance;
import com.DaaS.core.objects.TransactionManagerClass;

/**
 * @author rosha
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring-config.xml"})
public class InstanceRepositoryTest {
	
	@Autowired
	private  InstanceRepository instanceRepository ;
	
	

	

	@Test
	public void testSaveS() {
		
		Instance ec2Instance = new Instance();
		

		ec2Instance.setImageId("ami-4b814f22");
		ec2Instance.setInstanceType("m1.small");
		ec2Instance.setMinCount(1);
		ec2Instance.setMaxCount(1);
		ec2Instance.setKeyName("aws-ec2-keypair");
		ec2Instance.setSecurityGroup("default");
		
		
		instanceRepository.save(ec2Instance);
		
		/*
		TransactionManager tm = TransactionManagerClass.getTransactionManager();

		try {
			tm.begin();
			
			
			tm.commit();


		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		*/
		
		
		
	}

	
}
