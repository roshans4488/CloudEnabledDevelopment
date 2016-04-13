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
	
	
	//52.26.95.143
	
	
	/*
	@Autowired(required = true)
    public void setInstanceService(InstanceService instanceService) {
        this.instanceService= instanceService;
    }
	*/
	
	@RequestMapping(value="/authenticateAWSUser/{user_id}",method = RequestMethod.GET,consumes = "application/json",  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String authenticateAWSUser(@PathVariable("user_id") long user_id) throws IOException, CloudDevException {
        
    	
		User user = userService.getUserById(user_id);
		System.out.println("Access Key: "+user.getAccessKey());
		System.out.println("Secret Key: "+user.getSecretKey());
		BasicAWSCredentials credentials = new BasicAWSCredentials(user.getAccessKey(), user.getSecretKey());
		
		
		
		
		//Setup Amazon EC2 client
		amazonEC2Client = new AmazonEC2Client(credentials);
		amazonEC2Client.setEndpoint("ec2.us-west-2.amazonaws.com"); 	
		
		return "AWS user authenticated successfully.";
		
		
    	
    	
    	
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
	
	
	
	
	@RequestMapping(value="/connectToEC2Instance",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String connectToEC2Instance() throws IOException, CloudDevException {
	
			
	//List<Reservation> reservations = amazonEC2Client.describeInstances().getReservations();
	//String publicIP = "52.10.147.50"; //reservations.get(0).getInstances().get(0).getPublicIpAddress();
	
	
	String instanceId =   "i-22fdf0e5";//EC2MetadataUtils.getInstanceId();
   System.out.println("instance_id:"+instanceId);
	List<Reservation> reservations = amazonEC2Client.describeInstances(new DescribeInstancesRequest()
	                                                    .withInstanceIds(instanceId))
	                             .getReservations();
	
	
	System.out.println("Size:"+reservations.size());
	
	System.out.println("ip:"+reservations.get(0).getInstances().get(0).getPublicIpAddress());
	                           /*  .stream()
	                             .map(Reservation::getInstances)
	                             .flatMap(List::stream)
	                             .findFirst()
	                             .map(Instance::getPublicIpAddress)
	                             .orElse(null);*/
	
return instanceId;
	
	
	//System.out.println("Size: "+publicIP.size());
	
//	String publicIP = reservations.get(0).getInstances().get(0).getPublicIpAddress();
//	
//	
//	System.out.println(publicIP);
//	
//	
//	String user = "ubuntu";
//    String host = publicIP;
//    int    port = 22;
//    
//    
//    String command = "./script.sh"; 
//     
//    User userObject = userService.getUserById(1L);
//    String privateKey = userObject.getPrivateKey();
//     
//     
//    SSHManager sshManager = new SSHManager("ubuntu",publicIP,privateKey,22);
//    sshManager.connect();
//    String response = sshManager.sendCommand(command);
//    System.out.println(response);
//	return response;
	     
	     
	//return publicIP;
	

	}
	
	
	/*
	@RequestMapping(value="/runEC2Instance",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String runEC2Instance() throws IOException, CloudDevException {
		
		
		
		//createSecurityGroup();
		//createKeyPair();
		
		
		RunInstancesRequest runInstancesRequest =
			      new RunInstancesRequest();

			  runInstancesRequest.withImageId("ami-9ff7e8af")
			                     .withInstanceType("t2.micro")
			                     .withMinCount(1)
			                     .withMaxCount(1)
			                     .withKeyName("295")
			                     .withSecurityGroups("JavaSecurityGroup");
			  
			  
			  RunInstancesResult runInstancesResult =
				      amazonEC2Client.runInstances(runInstancesRequest);
			  
			  
		  
			  
		return null;
		
	}
	*/
	
	@RequestMapping(value="/sync",method = RequestMethod.POST, consumes =
    	    "application/json" , produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody

    public JSONObject sync() {
     
		
		
		
		  String pemPath = "/home/pripawar/Priya.pem";
          String publicIP = "52.26.95.143";
          
          String command = "rsync -azvv -e \"ssh -i " + pemPath + "\"" + " src/main/resources/scripts" + " " + "ubuntu@" + publicIP + ":/home/ubuntu";
          System.out.println(command);
          String output = executeCommand(command);
          System.out.println(output);
                  
          String user = "ubuntu";
          String host = publicIP;
          int    port = 22;
          
          
          String dockerInstall = "/home/ubuntu/scripts/bootstrap.sh"; 
           
          User userObject = null;
		try {
			userObject = userService.getUserById(1L);
		} catch (CloudDevException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          String privateKey = userObject.getPrivateKey();
           
           
          SSHManager sshManager = new SSHManager("ubuntu",publicIP,privateKey,22);
          sshManager.connect();
          String response = sshManager.sendCommand(dockerInstall);
          System.out.println(response);
          
          String buildDockerImage = "/home/ubuntu/scripts/buildDockerImage.sh";
          String resp_buildImage = sshManager.sendCommand(buildDockerImage);
          System.out.println(resp_buildImage);
          sshManager.close();
      	
          
          
          
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
  			String publicIP = runInstancesResult.getReservation().getInstances().get(0).getPublicIpAddress();
  			System.out.println(runInstancesResult.getReservation().getInstances().size());
  			System.out.println("PublicIp: "+publicIP);
  			
  			
  			System.out.println("InstanceId: "+instanceId);
            instance.setPublicIp(publicIP);  			
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
    
    //refactor code
    
    private String executeCommand(String command) {

  		StringBuffer output = new StringBuffer();

  		Process p;
  		try {
  			p = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
  			p.waitFor();
  			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
  			
  			String line = "";			
  			while ((line = reader.readLine())!= null) {
  				output.append(line + "\n");
  			}
  			
  			 BufferedReader readErrorProc=new BufferedReader(new InputStreamReader(p.getErrorStream()));
  		      while(readErrorProc.ready()) {
  		        String output1 = readErrorProc.readLine();
  		        System.out.println(output1);
  		      }

  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		return output.toString();
    }
}
