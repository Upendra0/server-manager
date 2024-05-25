/**
 * 
 */
package com.elitecore.sm.scripteditor.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * @author hiral.panchal
 *
 */
public class RemoteServerUtils {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	Session session = null;

	/**Establish the SSH connection
	 * @throws JSchException */
	public Channel connectToServer(String host, int port, String userName, String password, String operation) throws JSchException{
		
		Channel channel = null;
		JSch jsch = new JSch();
		session = jsch.getSession(userName, host, port);
		session.setPassword(password);
		session.setConfig("StrictHostKeyChecking", "no");
		logger.info("Establishing Connection...");
		
		session.connect();
		
		logger.info("Connection established.Now creating Channel...");
		if("READ".equalsIgnoreCase(operation.trim()) || "WRITE".equalsIgnoreCase(operation.trim())){
			
			channel = (ChannelSftp) session.openChannel("sftp");
		}else{
			//in case of execute the file
			channel = (ChannelExec) session.openChannel("exec");
		}
		
//		channel.connect();
//		logger.info("SFTP Channel created!!");
		return channel;
	
	}
	
	/**Terminate the SSH connection*/
	public void releaseConnection(Channel channel){
		
		logger.info("RemoteServerUtils.releaseConnection() called..");
		channel.disconnect();
		session.disconnect();
		logger.debug("releaseConnection():channel.isClosed():"+channel.isClosed()+", channel.isConnected():"+channel.isConnected());
	}
	

	/**Read the remote file content and return
	 * @throws JSchException 
	 * @throws SftpException 
	 * @throws IOException */
	public StringBuilder readFile(String host, int port, String userName, String password, String remotefile)  {

		logger.info("RemoteServerUtils.readFile() called...host:"+host+",port:"+port+",userName:"+userName+",remotefile:"+remotefile);
		
		StringBuilder strbuilder = new StringBuilder();
		ChannelSftp sftpChannel = null;
		String line;
		InputStream inputStream = null;
		BufferedReader br =  null;

		try{

			sftpChannel = (ChannelSftp) this.connectToServer(host, port, userName, password,"READ");
			sftpChannel.connect();
			inputStream = sftpChannel.get(remotefile);

			br = new BufferedReader(new InputStreamReader(inputStream));
			 
             while ((line = br.readLine()) != null) {
            	 strbuilder.append(line + System.lineSeparator());
             }

			if(strbuilder.length() > 0){
				
				logger.info("File has some content.!");
			}
			
		}catch(JSchException |IOException | SftpException e){
			logger.error(e);
		}finally{
			
			if(inputStream !=  null){
				
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if(br != null){	
				
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			releaseConnection(sftpChannel);
		}
		return strbuilder;
	}
	
	/**Save the modified content to the file on remote server*/
	public boolean saveFile(String host, int port, String userName, String password, String remotefile, String fileContent){

		logger.info("RemoteServerUtils.saveFile() called...host:"+host+",port:"+port+",userName:"
				+userName+",remotefile:"+remotefile);

		ChannelSftp sftpChannel = null;
		InputStream obj_InputStream = null;
		boolean isSaved = true;

		try {
			
			sftpChannel = (ChannelSftp) this.connectToServer(host, port, userName, password, "WRITE");
			
			sftpChannel.connect();
			
			obj_InputStream = new ByteArrayInputStream(fileContent.getBytes());
			
			sftpChannel.put(obj_InputStream, remotefile );
			
		} catch (JSchException | SftpException e) {

			isSaved = false;
			logger.error(e);
			
		}finally{
			
			//Release resources and Close connection
			try {
				if(obj_InputStream!=null)
					obj_InputStream.close();
				obj_InputStream = null;
				
			} catch (IOException e) {
				
				isSaved = false;
				logger.error(e);
			}
			
			releaseConnection(sftpChannel);
		}
		return isSaved;
	}
	
	/**Execute the file and return the output of execution*/
	public StringBuilder executeFile(String host, int port, String userName, String password, String filepath, String exenCommand){

		ChannelExec channelExec = null;
		StringBuilder strbuilder = new StringBuilder();
		BufferedReader reader = null;

		try {
			channelExec = (ChannelExec) this.connectToServer(host, port, userName, password, "EXECUTE");
			channelExec.setCommand("cd "+filepath+";"+exenCommand);
			channelExec.setInputStream(null);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			channelExec.setOutputStream(out);
		//	channelExec.setErrStream(System.err);
			channelExec.setErrStream(out);
			InputStream in=channelExec.getInputStream();
			
			// Execute the command
			channelExec.connect();

			// Read the output from the input stream we set above
			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			
			// get the output
			while ((line = reader.readLine()) != null)
			{
				strbuilder.append(line+System.lineSeparator());
			}
			
		/*	byte[] tmp = new byte[1024];
			
			 while (true) {
		            while (in.available() > 0) {
		                int i = in.read(tmp, 0, 1024);
		                if (i < 0)
		                    break;
		                strbuilder.append(new String(tmp, 0, i));
		            }
		            if (channelExec.isEOF()) {
		                break;
		            }
		        }*/
			 
			if(strbuilder.length() == 0){
				
				strbuilder.append(out.toString());
			}
			
			//retrieve the exit status of the remote command corresponding to this channel
			int exitStatus = channelExec.getExitStatus();
			
			logger.info("exitStatus:"+exitStatus);

		} catch (JSchException | IOException e) {
			logger.error(e);
		}finally {
			
			try {
				if(reader != null){
					
					reader.close();
				}
			} catch (IOException e) {
				logger.error(e);
			}
			
			releaseConnection(channelExec);
		}
		return strbuilder;
	}
	
	public OutputStream downloadFile(String host, int port, String userName, String password, String remotefile, OutputStream output){

		ChannelSftp sftpChannel = null;
		String line;
		InputStream inputStream = null;
		BufferedReader br =  null;
		byte[] buffer = new byte[10240];

		try {
			sftpChannel = (ChannelSftp) this.connectToServer(host, port, userName, password,"READ");
			sftpChannel.connect();
			inputStream = sftpChannel.get(remotefile);
			for (int length = 0; (length = inputStream.read(buffer)) > 0;) {
		        output.write(buffer, 0, length);
		    }
			
		} catch (JSchException | SftpException | IOException e) {
			logger.error(e);
		}

			return output;

	}
	public static void main(String[] args) {
		
		RemoteServerUtils u = new RemoteServerUtils();
		//StringBuilder strbuilder = u.executeFile("10.151.1.147", 22, "mso", "mso@qa", "/opt/msosetup/test/", "cat file1.txt");
		StringBuilder strbuilder = null;
//		strbuilder = u.readFile("10.151.1.147", 22, "mso", "mso@qa",  "/opt/msosetup/test/first.sh");
	//	u.saveFile("10.151.1.147", 22, "mso", "mso@qa","/opt/msosetup/test/first.sh", "test.....");
	//	strbuilder = u.readFile("10.151.1.147", 22, "mso", "mso@qa",  "/opt/msosetup/test/file2.txt");
		//strbuilder = u.readFile("10.151.33.43", 22, "docker-qa", "docker-qa",  "/opt/versionbuild/MOUNT_POINT/QA_PARSING_PEngine/Services/XML_Utility/run.sh");
//		strbuilder = u.executeFile("10.151.33.43", 22, "docker-qa", "docker-qa",  "/opt/versionbuild/MOUNT_POINT/QA_PARSING_PEngine/Services/XML_Utility", "sh run.sh");
		strbuilder = u.executeFile("10.151.1.147", 22, "mso", "mso@qa",  "/opt/msosetup/test","sh while.sh");//NOSONAR
		
		//windows
		//strbuilder = u.executeFile("10.121.28.167", 22, "hiral.panchal", "test",  "C:\\Users\\hiral.panchal\\Desktop\\test","test.txt");
		
//String host, int port, String userName, String password, String filepath, String exenCommand		
		System.out.println("strbuilder:"+strbuilder);
		
	}
}
