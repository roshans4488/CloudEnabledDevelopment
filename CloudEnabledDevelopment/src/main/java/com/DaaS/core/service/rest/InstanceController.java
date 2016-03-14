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

import com.DaaS.core.objects.Instance;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.InstanceService;

/**
 * @author rosha
 *
 */

@RestController
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class,DataSourceAutoConfiguration.class})
@ContextConfiguration(locations={"classpath:/spring-config.xml"})
public class InstanceController {

	
	@Autowired
	private InstanceService instanceService;
	
	
	
	@Autowired(required = true)
    public void setInstanceService(InstanceService instanceService) {
        this.instanceService= instanceService;
    }
	
	//Save Instance
	@RequestMapping(value="/createInstance",method = RequestMethod.POST, consumes =
    	    "application/json" , produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody

    public Instance createInstance(@RequestBody @Valid Instance instance) {
     
    	try {
    		instanceService.save(instance);
		} catch (CloudDevException e) {
			e.printStackTrace();
		}
        
        return instance;
    }   
	
	
	//Find Instance
    @RequestMapping(value="/getInstance/{instance_id}",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Instance getInstance(@PathVariable("instance_id") Long instance_id) {
       
    	Instance instance = null;
		try {
			instance = instanceService.findOne(instance_id);
		} catch (CloudDevException e) {
			e.printStackTrace();
		}

    	return instance;
    }
   
	
   //List all Instances
    @RequestMapping(value="/getAllInstances",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Instance> getAllInstances() throws IOException, CloudDevException {
        
    	
    	List<Instance>  results = instanceService.findAll();
       
    	return results;
    	
    	
    }
	
    
    //Delete an instance
    @RequestMapping(value = "/deleteInstance/{instance_id}",method = RequestMethod.DELETE, produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteInstance(@PathVariable("instance_id") Long instance_id)  {
    	
    	try {
			instanceService.deleteInstanceById(instance_id);
		} catch (CloudDevException e) {
			e.printStackTrace();
		}
    
    }
    
	
    //Delete all instances
    @RequestMapping(value="/deleteAllInstances",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int deleteAllInstances() throws IOException, CloudDevException {
        
    	
    	int deletedCount = instanceService.deleteAll();
       
    	return deletedCount;
    	
    	
    }
    
    //Get count
    @RequestMapping(value="/getInstanceCount",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Long getInstanceCount() throws IOException, CloudDevException {
        
    	
    	long result = instanceService.count();
       
    	return result;
    	
    	
    }
    
}
