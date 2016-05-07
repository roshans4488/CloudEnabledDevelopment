package com.DaaS.core.objects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


/**
 * @author rosha
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class,DataSourceAutoConfiguration.class})
@Configuration
@ImportResource("spring-config.xml")
public class CloudEnabledDevelopmentApplication {

	
	
	public static void main(String[] args)  {
		
		SpringApplication.run(CloudEnabledDevelopmentApplication.class, args);
		
	
	}
	
}
