package com.DaaS.core.service.rest;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
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
import com.DaaS.core.objects.User;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.ContainerService;
import com.DaaS.core.service.UserService;
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
	
	@Autowired
	private UserService userService;
	
	
	//create Container
	@RequestMapping(value="/createContainer",method = RequestMethod.POST,consumes = "application/json",  produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createContainer(@RequestBody @Valid Container container) throws IOException, CloudDevException {
		
		//Rashmi scripts create docker container + deploy agent jar
		
		
		
		User userObject = null;
		try {
			userObject = userService.getUserById(1L);
		} catch (CloudDevException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String publicIP = "52.26.95.143";
		String privateKey = userObject.getPrivateKey();
		String port = "8000";
        
        String createContainer = "/home/ubuntu/scripts/createContainer.sh " + port;
		
        SSHManager sshManager = new SSHManager("ubuntu",publicIP,privateKey,22);
        sshManager.connect();
        String containerID = sshManager.sendCommand(createContainer);
        System.out.println("contaner id is: " + containerID);
        
        container.setDockerID(containerID);
        
        String copyAgentJar = "/home/ubuntu/scripts/copyfile.sh " + containerID;
        String resp_copyAgent = sshManager.sendCommand(copyAgentJar);
        System.out.println("Copy Jar: " + resp_copyAgent);
        
//        String logIntoContainer = "/home/ubuntu/scripts/logIntoContainer.sh " + containerID;
//        String resp_loginAgent = sshManager.sendCommand(logIntoContainer);
//        System.out.println("Login to the container: " + resp_loginAgent);
        
//        String logIntContainerNExecute = "/home/ubuntu/scripts/executejar.sh " + containerID;
//        String resp_loginAgent = sshManager.sendCommand(logIntContainerNExecute);
//        System.out.println("Login to the container: " + logIntContainerNExecute);
        
	
		containerService.save(container);
		
		
		
//		@SuppressWarnings("deprecation")
//		HttpClient client = new DefaultHttpClient();
//		
//		// write code to get the docker containers IP address
//		
//		String IPAddress = "";
//		
//		String url = "http://" + IPAddress + ":8080/create";
//		
//        HttpPost post = new HttpPost(url);
//		StringEntity input;
//		HttpResponse response = null;
//		
//		try {
//			input = new StringEntity(obj.toString());
//			input.setContentType("application/json");
//	        post.setEntity(input);
//	        response = client.execute(post);
//	        
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//        
//    	JSONObject resp_obj = new JSONObject();
//        resp_obj.put("result", response);
//        return resp_obj;
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
    
    
    

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value="/save", method = RequestMethod.POST ,  produces = "application/json", consumes = "application/json")
    public @ResponseBody JSONObject saveProject(@RequestBody JSONObject obj) {
    	
    	
		@SuppressWarnings("deprecation")
		HttpClient client = new DefaultHttpClient();
		
		// write code to get the docker containers IP address
		
		String IPAddress = "";
		
		String url = "http://" + IPAddress + ":8080/save";
		
        HttpPost post = new HttpPost(url);
		StringEntity input;
		HttpResponse response = null;
		
		try {
			input = new StringEntity(obj.toString());
			input.setContentType("application/json");
	        post.setEntity(input);
	        response = client.execute(post);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    	JSONObject resp_obj = new JSONObject();
        resp_obj.put("result", response);
        return resp_obj;
    }
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value="/load", method = RequestMethod.POST ,  produces = "application/json", consumes = "application/json")
    public @ResponseBody JSONObject loadProject(@RequestBody JSONObject obj) {
    	
    	
		@SuppressWarnings("deprecation")
		HttpClient client = new DefaultHttpClient();
		
		// write code to get the docker containers IP address
		
		String IPAddress = "";
		
		String url = "http://" + IPAddress + ":8080/load";
		
        HttpPost post = new HttpPost(url);
		StringEntity input;
		HttpResponse response = null;
		
		try {
			input = new StringEntity(obj.toString());
			input.setContentType("application/json");
	        post.setEntity(input);
	        response = client.execute(post);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    	JSONObject resp_obj = new JSONObject();
        resp_obj.put("result", response);
        return resp_obj;
    }
	
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value="/compile", method = RequestMethod.POST ,  produces = "application/json", consumes = "application/json")
    public @ResponseBody JSONObject compileProject(@RequestBody JSONObject obj) {
    	
    	
		@SuppressWarnings("deprecation")
		HttpClient client = new DefaultHttpClient();
		
		// write code to get the docker containers IP address and Project Name
		
		String IPAddress = "";
		
		String url = "http://" + IPAddress + ":8080/compile";
		
        HttpPost post = new HttpPost(url);
		StringEntity input;
		HttpResponse response = null;
		
		try {
			input = new StringEntity(obj.toString());
			input.setContentType("application/json");
	        post.setEntity(input);
	        response = client.execute(post);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    	JSONObject resp_obj = new JSONObject();
        resp_obj.put("result", response);
        return resp_obj;
    }
    

    
    
    
    
    
}
