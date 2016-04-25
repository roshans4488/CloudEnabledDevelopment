package com.DaaS.core.service.rest;

import com.jcraft.jsch.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSHManager
{
private static final Logger LOGGER = 
    Logger.getLogger(SSHManager.class.getName());
private JSch jsch;
private Session session;
private String user;
private String connectionIP;
private String privateKey;
private int port;
private int intTimeOut;


public SSHManager(String user,String connectionIP, String privateKey, int port)
{
   jsch = new JSch();
   this.user = user;
   this.privateKey = privateKey;
   this.connectionIP = connectionIP;
   this.port = port;
   intTimeOut = 60000;
}


public String connect()
{
   String errorMessage = null;
   File file = null;
   /*
  

   */
   try {
	   try {
		   file = new File(user+".pem");
		   
		   if(!file.exists()){
					file.createNewFile();
					 FileWriter fileWritter = new FileWriter(file.getName(),true);
					   BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
					   bufferWritter.write(privateKey);
					   bufferWritter.close();
				}
		   file.deleteOnExit();
		  
		    
		} catch (IOException e) {
		}
	   
	   
	
	   
	   System.out.println("Pem file path:"+file.getCanonicalPath());
	jsch.addIdentity(file.getCanonicalPath());
	System.out.println("Identity added.");
	
} catch (JSchException e) {
		e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

   try {
  	 
	 session = jsch.getSession("ubuntu", connectionIP, port);
	 //session.setHost(connectionIP);
	
	 session.setConfig("StrictHostKeyChecking", "no");
	 session.connect(intTimeOut);
	 System.out.println("session created.");
     
} catch (JSchException e) {
	e.printStackTrace();
}

   return errorMessage;
}

private String logError(String errorMessage)
{
   if(errorMessage != null)
   {
      LOGGER.log(Level.SEVERE, "{0}:{1} - {2}", 
          new Object[]{connectionIP, port, errorMessage});
   }

   return errorMessage;
}

private String logWarning(String warnMessage)
{
   if(warnMessage != null)
   {
      LOGGER.log(Level.WARNING, "{0}:{1} - {2}", 
         new Object[]{connectionIP, port, warnMessage});
   }

   return warnMessage;
}

public String sendCommand(String command)
{
   StringBuilder outputBuffer = new StringBuilder();

   try
   {
      Channel channel = session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);
      InputStream commandOutput = channel.getInputStream();
      channel.connect();
      int readByte = commandOutput.read();

      while(readByte != 0xffffffff)
      {
         outputBuffer.append((char)readByte);
         readByte = commandOutput.read();
      }

      channel.disconnect();
   }
   catch(IOException ioX)
   {
      logWarning(ioX.getMessage());
      return null;
   }
   catch(JSchException jschX)
   {
      logWarning(jschX.getMessage());
      return null;
   }

   return outputBuffer.toString();
}



public String openStream()
{
   StringBuilder outputBuffer = new StringBuilder();

   try
   {
      Channel channel = session.openChannel("shell");
      channel.setInputStream(System.in);
      channel.setOutputStream(System.out);
      channel.connect();

   }catch(Exception e){
	   e.printStackTrace();
   }
   

   return outputBuffer.toString();
}











public void close()
{
   session.disconnect();
}

}