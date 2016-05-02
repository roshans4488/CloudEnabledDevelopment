package com.DaaS.core.objects;


import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
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
import com.DaaS.core.service.rest.InstanceController;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;


/**
 * @author rosha
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class,DataSourceAutoConfiguration.class})
@Configuration
@ImportResource("spring-config.xml")
public class CloudEnabledDevelopmentApplication {

	
	
	public static void main(String[] args) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		
		SpringApplication.run(CloudEnabledDevelopmentApplication.class, args);
		
		/*
		Repository existingRepo = new FileRepositoryBuilder()
			    .setGitDir(new File("/home/roshan/git/CloudEnabledDevelopment/.git"))
			    .build();
		
		
		Git git = new Git(existingRepo);
		System.out.println("rrrrrrrrrrrrrrr");
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider("roshans4488", "Rosh@4488");
		Collection<Ref> remoteRefs = git.lsRemote()
		    .setCredentialsProvider(cp)
		    .setRemote("origin")
		    .setTags(true)
		    .setHeads(true)
		    .call();
		for (Ref ref : remoteRefs) {
			System.out.println("RepoDetails:");
		    System.out.println(ref.getName() + " -&gt; " + ref.getObjectId().name());
	}
	*/
	
	}
	
}
