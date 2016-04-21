package com.DaaS.core.service.rest;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
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
import com.DaaS.core.objects.Instance;
import com.DaaS.core.objects.User;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.ContainerService;
import com.DaaS.core.service.InstanceService;
import com.DaaS.core.service.UserService;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	
	@Autowired
	private InstanceService instanceService;
	
	private AmazonEC2Client amazonEC2Client;
	
	//create Container     /createContainer/1  (MongoId of instance)
	@RequestMapping(value="/createContainer/{instance_id}",method = RequestMethod.POST,consumes = "application/json",  produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createContainer(@RequestBody @Valid Container container,@PathVariable("instance_id") Long instance_id) throws IOException, CloudDevException {
		
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(container);
		
		User userObject = null;
		try {
			userObject = instanceService.getInstanceById(instance_id).getUser();
		} catch (CloudDevException e) {
			e.printStackTrace();
		}
		
		//Authenticate user
		BasicAWSCredentials credentials = new BasicAWSCredentials(userObject.getAccessKey(), userObject.getSecretKey());
		amazonEC2Client = new AmazonEC2Client(credentials);
		amazonEC2Client.setEndpoint("ec2.us-west-2.amazonaws.com"); 	
		
		
		//get publicIP and private key
		String publicIP = Yoda.getPublicIp(instanceService.getInstanceById(instance_id).getEc2InstanceId(), amazonEC2Client); //"52.26.95.143";
		String privateKey = userObject.getPrivateKey();
		
		//configuring port
		Long basePort = 8000L;
        Long containerCount = containerService.count(instance_id);
        basePort+=containerCount;
        String port = basePort.toString();
        
        
		
		//create container on ec2 instance
        String createContainer = "/home/ubuntu/scripts/createContainer.sh " + port;
        SSHManager sshManager = new SSHManager(userObject.getName(),publicIP,privateKey,22);
        sshManager.connect();
        String containerID = sshManager.sendCommand(createContainer);
        System.out.println("contaner id is: " + containerID);
        
        //set containerID and ec2 ip
        container.setDockerID(containerID);
        container.setEc2ipAddress(publicIP);
        container.setPort(port);
        
        
        //copy Agent jar file
        String copyAgentJar = "/home/ubuntu/scripts/copyfile.sh " + containerID;
        String resp_copyAgent = sshManager.sendCommand(copyAgentJar);
        System.out.println("Copy Jar: " + resp_copyAgent);
        
//        //log in to the container
//        String logIntoContainer = "/home/ubuntu/scripts/logIntoContainer.sh " + containerID;
//        String resp_loginAgent = sshManager.sendCommand(logIntoContainer);
//        System.out.println("Login to the container: " + resp_loginAgent);
        
      //execute workspace agent
        Thread t = new Thread(){
        	public void run(){
                String logIntContainerNExecute = "/home/ubuntu/agentScripts/executejar.sh " + containerID;
                String resp_loginAgent = sshManager.sendCommand(logIntContainerNExecute);
                System.out.println("Login to the container: " + logIntContainerNExecute);
                
        	}
        };
        
        t.start();
       
        String responseString = null;
        
        try {
        	
        	//wait till the workspace agent is up
			//t.join();
		    Thread.sleep(8000);
			
			//invoke create project api in container
			
			  @SuppressWarnings("deprecation")
				HttpClient client = new DefaultHttpClient();
				
				String url = "http://" + publicIP + ":"+port+"/create";
				
				System.out.println("Url:"+url);
				
		        HttpPost post = new HttpPost(url);
				StringEntity input;
				HttpResponse response = null;
				
				try {
					
					System.out.println("Executing");
					input = new StringEntity(jsonInString);
					input.setContentType("application/json");
					System.out.println("Input:"+jsonInString);
			        post.setEntity(input);
			        response = client.execute(post);
			        responseString = new BasicResponseHandler().handleResponse(response);
						    
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		       
//				resp_obj = new JSONObject();
//		        resp_obj.put("result", responseString);
			
			
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        
      
       
	    //persist the container object in mongo db
		containerService.save(container);
		

		 return responseString;
		
		
		
    	
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
    @RequestMapping(value="/getContainerCount/{instance_id}",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Long getContainerCount(@PathVariable("instance_id") Long instance_id) throws IOException, CloudDevException {
        
    	
    	long result = containerService.count(instance_id);
       
    	return result;
    	
    	
    }
    
    /*
    @ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value="/create", method = RequestMethod.POST ,  produces = "application/json", consumes = "application/json")
    public JSONObject createProject(@RequestBody JSONObject obj) {
    	
    	
		@SuppressWarnings("deprecation")
		HttpClient client = new DefaultHttpClient();
		
		// write code to get the docker containers IP address
		
		String IPAddress = "52.36.111.118";
		
		String url = "http://" + IPAddress + ":8000/create";
		
        HttpPost post = new HttpPost(url);
		StringEntity input;
		HttpResponse response = null;
		String responseString = null;
		try {
			
			System.out.println("Executing");
			input = new StringEntity(obj.toString());
			input.setContentType("application/json");
	        post.setEntity(input);
	        response = client.execute(post);
	        responseString = new BasicResponseHandler().handleResponse(response);
	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		JSONObject resp_obj = new JSONObject();
        resp_obj.put("result", responseString);
        return resp_obj;

    }
    
    */

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
		String responseString = null;
		
		try {
			input = new StringEntity(obj.toString());
			input.setContentType("application/json");
	        post.setEntity(input);
	        response = client.execute(post);
	        responseString = new BasicResponseHandler().handleResponse(response);
	       
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    	JSONObject resp_obj = new JSONObject();
        resp_obj.put("result", responseString);
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
		String responseString = null;
		
		try {
			input = new StringEntity(obj.toString());
			input.setContentType("application/json");
	        post.setEntity(input);
	        response = client.execute(post);
	        responseString = new BasicResponseHandler().handleResponse(response);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    	JSONObject resp_obj = new JSONObject();
        resp_obj.put("result", responseString);
        return resp_obj;
    }
	
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value="/compile", method = RequestMethod.POST ,  produces = "application/json", consumes = "application/json")
    public @ResponseBody JSONObject compileProject(@RequestBody JSONObject obj) {
    	
    	
		@SuppressWarnings("deprecation")
		HttpClient client = new DefaultHttpClient();
		
	    //container id has to be passed as parameter to retrieve IP address and project name
		
		String IPAddress = "52.36.111.118";
		
		String url = "http://" + IPAddress + ":8080/compile";
		
        HttpPost post = new HttpPost(url);
		StringEntity input;
		HttpResponse response = null;
		String responseString = null;
		
		try {
			input = new StringEntity(obj.toString());
			input.setContentType("application/json");
	        post.setEntity(input);
	        response = client.execute(post);
	        responseString = new BasicResponseHandler().handleResponse(response);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    	JSONObject resp_obj = new JSONObject();
        resp_obj.put("result", responseString);
        return resp_obj;
    }
    

    
	 //get Containers for Instance
    @RequestMapping(value="/getContainersForInstance/{instance_id}",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Container> getContainersForInstance(@PathVariable("instance_id") Long instance_id) throws IOException, CloudDevException {
        
    	
    	List<Container>  results = containerService.findAllForInstance(instance_id);
       

    	return results;
    	
    	
    }
    
    
    
}
