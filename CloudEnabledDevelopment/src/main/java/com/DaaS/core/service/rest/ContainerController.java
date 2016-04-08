package com.DaaS.core.service.rest;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.DaaS.core.objects.Container;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.ContainerService;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;

/**
 * @author rosha
 *
 */

@RestController
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class,DataSourceAutoConfiguration.class})
@ContextConfiguration(locations={"classpath:/spring-config.xml"})
public class ContainerController {

	
	@Autowired
	private ContainerService containerService;
	
	
	//create Container
	@RequestMapping(value="/createContainer",method = RequestMethod.POST,consumes = "application/json",  produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createContainer(@RequestBody @Valid Container container) throws IOException, CloudDevException {
		
		containerService.save(container);
		return "Contianer created successfully.";
		
    	
    }
	
	
	
	
	//Find Container
    @RequestMapping(value="/getContainer/{container_id}",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Container getContainer(@PathVariable("container_id") Long container_id) {
       
    	Container container = null;
		try {
			container = containerService.findOne(container_id);
		} catch (CloudDevException e) {
			e.printStackTrace();
		}

    	return container;
    }
   
	
   //List all Containers
    @RequestMapping(value="/getAllContainers",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Container> getAllContainers() throws IOException, CloudDevException {
        
    	
    	List<Container>  results = containerService.findAll();
       
    	return results;
    	
    	
    }
	
    
    //Delete a container
    @RequestMapping(value = "/deleteContainer/{container_id}",method = RequestMethod.DELETE, produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteContainer(@PathVariable("container_id") Long container_id)  {
    	
    	try {
    		containerService.deleteContainerById(container_id);
		} catch (CloudDevException e) {
			e.printStackTrace();
		}
    
    }
    
	
    //Delete all containers
    @RequestMapping(value="/deleteAllContainers",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int deleteAllContainers() throws IOException, CloudDevException {
        
    	
    	int deletedCount = containerService.deleteAll();
       
    	return deletedCount;
    	
    	
    }
    
    //Get count
    @RequestMapping(value="/getContainerCount",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Long getContainerCount() throws IOException, CloudDevException {
        
    	
    	long result = containerService.count();
       
    	return result;
    	
    	
    }
    
}
