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

import com.DaaS.core.objects.User;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.UserService;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.jcraft.jsch.Channel;
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
public class UserController {

	
	@Autowired
	private UserService userService;
	private AWSCredentials credentials;
	private AmazonEC2Client amazonEC2Client;
	
	@RequestMapping(value="/createAWSUser",method = RequestMethod.POST,consumes = "application/json",  produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createAWSUser(@RequestBody @Valid User user) throws IOException, CloudDevException {
        
    	
		
		System.out.println("Access Key: "+user.getAccessKey());
		System.out.println("Secret Key: "+user.getSecretKey());
		BasicAWSCredentials credentials = new BasicAWSCredentials(user.getAccessKey(), user.getSecretKey());
		
		
		
		
		//Setup Amazon EC2 client
		amazonEC2Client = new AmazonEC2Client(credentials);
		amazonEC2Client.setEndpoint("ec2.us-west-2.amazonaws.com"); 	
		
		
		CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();
		createKeyPairRequest.withKeyName(user.getName());  //Keypair created with user name
		CreateKeyPairResult createKeyPairResult =
				  amazonEC2Client.createKeyPair(createKeyPairRequest);
		
		 String privateKey = createKeyPairResult.getKeyPair().getKeyMaterial();
		
		user.setPrivateKey(privateKey);
		
		
		userService.save(user);
		return "AWS user authenticated successfully.";
		
		
    	
    	
    	
    }
	
	
	
	
	
	
	
	
	
	//Find User
    @RequestMapping(value="/getUser/{user_id}",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User getUser(@PathVariable("user_id") Long user_id) {
       
    	User user = null;
		try {
			user = userService.findOne(user_id);
		} catch (CloudDevException e) {
			e.printStackTrace();
		}

    	return user;
    }
   
	
   //List all Users
    @RequestMapping(value="/getAllUsers",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<User> getAllUsers() throws IOException, CloudDevException {
        
    	
    	List<User>  results = userService.findAll();
       
    	return results;
    	
    	
    }
	
    
    //Delete a user
    @RequestMapping(value = "/deleteUser/{user_id}",method = RequestMethod.DELETE, produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteUser(@PathVariable("user_id") Long user_id)  {
    	
    	try {
    		userService.deleteUserById(user_id);
		} catch (CloudDevException e) {
			e.printStackTrace();
		}
    
    }
    
	
    //Delete all users
    @RequestMapping(value="/deleteAllUsers",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int deleteAllUsers() throws IOException, CloudDevException {
        
    	
    	int deletedCount = userService.deleteAll();
       
    	return deletedCount;
    	
    	
    }
    
    //Get count
    @RequestMapping(value="/getUserCount",method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Long getUserCount() throws IOException, CloudDevException {
        
    	
    	long result = userService.count();
       
    	return result;
    	
    	
    }
    
}
