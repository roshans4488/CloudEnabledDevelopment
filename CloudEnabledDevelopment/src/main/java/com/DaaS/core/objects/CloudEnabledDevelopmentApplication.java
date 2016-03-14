package com.DaaS.core.objects;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.DaaS.core.repository.InstanceRepository;


/**
 * @author rosha
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class,DataSourceAutoConfiguration.class})
@Configuration
@ImportResource("spring-config.xml")
public class CloudEnabledDevelopmentApplication {

	
	
	public static void main(String[] args) {
		
        SpringApplication.run(CloudEnabledDevelopmentApplication.class, args);

		
	}
	
	
	
	
}
