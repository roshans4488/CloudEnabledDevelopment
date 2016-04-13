package com.DaaS.core.service.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.DaaS.core.objects.Instance;
import com.DaaS.core.service.CloudDevException;
import com.DaaS.core.service.InstanceService;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.Reservation;

public class Yoda {

	
	
	
	//return public ip of EC2 instance
	static String getPublicIp(Long instance_id, AmazonEC2Client amazonEC2Client, InstanceService instanceService)
    {
    Instance instance = null;
	try {
		instance = instanceService.getInstanceById(instance_id);
	} catch (CloudDevException e) {
		e.printStackTrace();
	}
	String ec2InstanceId =  instance.getEc2InstanceId();
	List<Reservation> reservations = amazonEC2Client.describeInstances(new DescribeInstancesRequest()
	                                                .withInstanceIds(ec2InstanceId))
	                             					.getReservations();
	String publicIP = reservations.get(0).getInstances().get(0).getPublicIpAddress();
	return publicIP;
    
    }
	
	
	//Execute command on bash shell using Runtime library
	 static String executeCommand(String command) {

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
	
	
	 //create temp private key file (.pem)
	 static String createPrivateKeyFile(String privateKey,String name) {
	 File file=null;
	 try {
		   file = new File(name+".pem");
		   
		   if(!file.exists()){
					file.createNewFile();
				}
		   file.deleteOnExit();
		   FileWriter fileWritter = new FileWriter(file.getName(),true);
		   BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		   bufferWritter.write(privateKey);
		   bufferWritter.close();
		    
		} catch (IOException e) {
		}
	 
	 	String pemPath = null;
		try {
			pemPath = file.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pemPath;
		
	 }
	
}
