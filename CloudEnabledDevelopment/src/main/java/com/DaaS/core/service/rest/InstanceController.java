package com.DaaS.core.service.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
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

import com.DaaS.core.objects.CloudEnabledDevelopmentApplication;
import com.DaaS.core.objects.Instance;
import com.DaaS.core.objects.User;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.InstanceService;
import com.DaaS.core.service.UserService;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.amazonaws.util.EC2MetadataUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


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
	@Autowired
	private UserService userService;
	private AWSCredentials credentials;
	private AmazonEC2Client amazonEC2Client;
	
	
	
	
	/*
	@Autowired(required = true)
    public void setInstanceService(InstanceService instanceService) {
        this.instanceService= instanceService;
    }
	*/
	
	@RequestMapping(value="/authenticateAWSUser/{user_id}",method = RequestMethod.GET,consumes = "application/json",  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AmazonEC2Client authenticateAWSUser(@PathVariable("user_id") long user_id) throws IOException, CloudDevException {
        
    	
		User user = userService.getUserById(user_id);
		System.out.println("Access Key: "+user.getAccessKey());
		System.out.println("Secret Key: "+user.getSecretKey());
		BasicAWSCredentials credentials = new BasicAWSCredentials(user.getAccessKey(), user.getSecretKey());
		
		
		
		
		//Setup Amazon EC2 client
		amazonEC2Client = new AmazonEC2Client(credentials);
		amazonEC2Client.setEndpoint("ec2.us-west-2.amazonaws.com"); 	
		
		return amazonEC2Client;
		
		
    	
    	
    	
    }
	
	
	
	@RequestMapping(value="/authenticateKeys",method = RequestMethod.POST,consumes = "application/json",  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean authenticateKeys(@RequestBody JSONObject o)  {
        
    	Boolean isValid = true;
		BasicAWSCredentials credentials = new BasicAWSCredentials(o.get("accessKey").toString(), o.get("secretKey").toString());
		
		
		//Setup Amazon EC2 client
		amazonEC2Client = new AmazonEC2Client(credentials);
		amazonEC2Client.setEndpoint("ec2.us-west-2.amazonaws.com"); 	
		
		try{
			System.out.println(amazonEC2Client.describeAvailabilityZones().toString());
		}
		catch(Exception e){
			isValid = false;
			e.printStackTrace();
		}
		return isValid;
		
		
    	
    	
    	
    }
	
	
	
	
	@RequestMapping(value="/createSecurityGroup",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String createSecurityGroup() throws IOException, CloudDevException {
		
		CreateSecurityGroupRequest csgr = new CreateSecurityGroupRequest();
		csgr.withGroupName("CloudDevSecurityGroup").withDescription("Cloud Dev security group");
		
		
		CreateSecurityGroupResult createSecurityGroupResult =
			    amazonEC2Client.createSecurityGroup(csgr);
		
		
		IpPermission ipPermission1 =
			    new IpPermission();

			ipPermission1.withIpRanges("0.0.0.0/32")
			            .withIpProtocol("tcp")
			            .withFromPort(22)
			            .withToPort(22);
			
			IpPermission ipPermission2 =
				    new IpPermission();

				ipPermission2.withIpRanges("0.0.0.0/0")
				            .withIpProtocol("icmp")
							.withFromPort(8)  //The from_port is the ICMP type number and the to_port is the ICMP code
							.withToPort(0);
				            
			
			AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =
				    new AuthorizeSecurityGroupIngressRequest();

				authorizeSecurityGroupIngressRequest.withGroupName("CloudDevSecurityGroup")
				                                    .withIpPermissions(ipPermission1,ipPermission2);
		
				
		amazonEC2Client.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
		
		return "Security Group created.";
		
	}
	
	
	//                      /connectToEC2Instance/1 
	@RequestMapping(value="/connectToEC2Instance/{instance_id}",method = RequestMethod.GET,  produces = "application/json")  
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String connectToEC2Instance(@PathVariable("instance_id") Long instance_id) throws IOException, CloudDevException {
	
	Instance instance = instanceService.getInstanceById(instance_id);
			
	//retrieve public IP of instance
	String publicIP = Yoda.getPublicIp(instance.getEc2InstanceId(), amazonEC2Client) ;     
	System.out.println(publicIP);
    /*
    //get private key
    User   userObject = userService.getUserById(instance.getUser().getId());
    String privateKey = userObject.getPrivateKey();
    
    //Execute command on ec2 instance
    String command = "netstat"; 
    SSHManager sshManager = new SSHManager("ubuntu",publicIP,privateKey,22);
    sshManager.connect();
    String response = sshManager.sendCommand(command);
    System.out.println(response);
	return response;
	*/
	return publicIP;

	}
	
	
	//openStreamChannel
	@RequestMapping(value="/openStreamChannel/{instance_id}",method = RequestMethod.POST, consumes =
    	    "application/json" , produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody

    public void openStreamChannel(@PathVariable("instance_id") Long instance_id) {
     
		
		Instance instance = null;
		try {
			instance = instanceService.getInstanceById(instance_id);
		} catch (CloudDevException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//retrieve public IP
		String publicIP = Yoda.getPublicIp(instance.getEc2InstanceId(), amazonEC2Client) ;   
		
		
		//retrieve private key
		User userObject = null;
		try {
			userObject = userService.getUserById(instance.getUser().getId());
		} catch (CloudDevException e1) {
			e1.printStackTrace();
		}
	    String privateKey = userObject.getPrivateKey();
				
			    
			    
			    
    //create temp user.pem file
    String name = userObject.getName();
	SSHManager sshManager = new SSHManager(name,publicIP,privateKey,22);  //change
    sshManager.connect();
    sshManager.openStream();
    //System.out.println(response);
	
	
	
	
	}
	
	
	
	
	               //       /sync/2
	@RequestMapping(value="/sync/{instance_id}",method = RequestMethod.POST, consumes =
    	    "application/json" , produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody

    public JSONObject sync(@PathVariable("instance_id") Long instance_id) {
     
		
		Instance instance = null;
		try {
			instance = instanceService.getInstanceById(instance_id);
		} catch (CloudDevException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		
		//retrieve public IP
		String publicIP = Yoda.getPublicIp(instance.getEc2InstanceId(), amazonEC2Client) ;   
		
		
		//retrieve private key
		User userObject = null;
		try {
			userObject = userService.getUserById(instance.getUser().getId());
		} catch (CloudDevException e1) {
			e1.printStackTrace();
		}
	    String privateKey = userObject.getPrivateKey();
		
	    
	    
	    
	    //create temp user.pem file
	    String name = userObject.getName();
	    String pemPath = Yoda.createPrivateKeyFile(privateKey, name);
	    
	    String changePerm = "chmod 600 " + pemPath;
	    System.out.println(pemPath);
	    System.out.println("Output: " + Yoda.executeCommand(changePerm));
	    
		//String pemPath = "/home/pripawar/Priya.pem";
        //String publicIP = "52.26.95.143";
          
			
			
		  //execute rsync command
          String command = "rsync -azvv -e \"ssh -i " + pemPath + " -o StrictHostKeyChecking=no\"" + " src/main/resources/scripts" + " " + "ubuntu@" + publicIP + ":/home/ubuntu";
          System.out.println(command);
          String output = Yoda.executeCommand(command);
          System.out.println(output);
          
          String agentSync = "rsync -azvv -e \"ssh -i " + pemPath + " -o StrictHostKeyChecking=no\"" + " src/main/resources/agentScripts" + " " + "ubuntu@" + publicIP + ":/home/ubuntu";
          System.out.println(agentSync);
          output = Yoda.executeCommand(agentSync);
          System.out.println(output);
          
          
          String ttySync = "rsync -azvv -e \"ssh -i " + pemPath + " -o StrictHostKeyChecking=no\"" + " src/main/resources/ttyUtility" + " " + "ubuntu@" + publicIP + ":/home/ubuntu";
          System.out.println(ttySync);
          output = Yoda.executeCommand(ttySync);
          System.out.println(output);
          
          //install docker
          String dockerInstall = "/home/ubuntu/scripts/bootstrap.sh"; 
          SSHManager sshManager = new SSHManager(name,publicIP,privateKey,22);  //change
          sshManager.connect();
          String response = sshManager.sendCommand(dockerInstall);
          System.out.println(response);
          
          
          //build docker image
          String buildDockerImage = "/home/ubuntu/scripts/buildDockerImage.sh";
          String resp_buildImage = sshManager.sendCommand(buildDockerImage);
          System.out.println(resp_buildImage);
          sshManager.close();
      	
         //send reponse back
         JSONObject obj = new JSONObject();
         obj.put("rsynch_output",output);
         obj.put("bootstrap_output", response);
		
		return obj;
		
	}
	
	
	
	
	
	
	
	
	
	
	//Save Instance
	@RequestMapping(value="/createInstance",method = RequestMethod.POST, consumes =
    	    "application/json" , produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody

    public Instance createInstance(@RequestBody @Valid Instance instance) {
     
    	try {
    		
    		RunInstancesRequest runInstancesRequest =
  			      new RunInstancesRequest();

  			  runInstancesRequest.withImageId(instance.getImageId())
  			                     .withInstanceType(instance.getInstanceType())
  			                     .withMinCount(instance.getMinCount())
  			                     .withMaxCount(instance.getMaxCount())
  			                     .withKeyName(instance.getKeyName())
  			                     .withSecurityGroups(instance.getSecurityGroup());
  			  
  			  
  			  
  			  RunInstancesResult runInstancesResult =
  				      amazonEC2Client.runInstances(runInstancesRequest);
    		
  			  
  			//Thread.sleep(60000);
  			  
  			String instanceId = runInstancesResult.getReservation().getInstances().get(0).getInstanceId();
  			
  			
  			System.out.println("InstanceId: "+instanceId);
            instance.setEc2InstanceId(instanceId);		
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
	
    
    
    //get Instances for User
    @RequestMapping(value="/getInstancesForUser/{user_id}",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Instance> getInstancesForUser(@PathVariable("user_id") Long user_id) throws IOException, CloudDevException {
        
    	
    	List<Instance>  results = instanceService.findAllForUser(user_id);
       

    	return results;
    	
    	
    }
    
    
    
    
    //Delete an instance
    @RequestMapping(value = "/deleteInstance/{instance_id}",method = RequestMethod.DELETE, produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
  
    public void deleteInstance(@PathVariable("instance_id") Long instance_id)  {
    	
    	try {
    		
    		
    		TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest();
    		Collection<String> instanceIds = Arrays.asList(instanceService.getInstanceById(instance_id).getEc2InstanceId().toString());
			terminateInstancesRequest.setInstanceIds(instanceIds);
    		TerminateInstancesResult terminateInstancesResult = amazonEC2Client.terminateInstances(terminateInstancesRequest);

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
