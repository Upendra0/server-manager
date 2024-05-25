/**
 * 
 */
package com.elitecore.sm.common.controller;

/**
 * @author Sunil Gulabani
 * Aug 28, 2015
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionCounterListener implements HttpSessionListener {
	
	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	private static int totalActiveSessions;

	public static int getTotalActiveSession() {
		return totalActiveSessions;
	}

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		totalActiveSessions++;//NOSONAR
		writeToFile("============================================\n\n");
		writeToFile("sessionCreated - add one session into counter. totalActiveSessions: " + totalActiveSessions);
		writeToFile("\tSessionId: " + sessionEvent.getSession().getId());
		printSessionParameters();
		writeToFile("============================================\n\n");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		totalActiveSessions--;//NOSONAR
		writeToFile("============================================\n\n");
		writeToFile("sessionDestroyed - deduct one session into counter. totalActiveSessions: " + totalActiveSessions);
		writeToFile("\tSessionId: " + sessionEvent.getSession().getId());
		printSessionParameters();
		writeToFile("============================================\n\n");
	}
	
	private void printSessionParameters(){
		try {
			if(RequestContextHolder.getRequestAttributes()!=null){
				HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null){
					writeToFile("\tauth.getPrincipal: " + auth.getPrincipal());
					
				}
				writeToFile("\tRequest Headers: ");
				Enumeration<String> headerNames = request.getHeaderNames();
				while(headerNames.hasMoreElements()){
					String headerName = headerNames.nextElement();
					writeToFile("\t\t " + headerName + ": " + request.getHeader(headerName));
				}
			}
			
			/*writeToFile("\tRequest parameters: ");
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				writeToFile("\t\t " + paramName + ": " + request.getParameterValues(paramName));
			}*/
			
			/*writeToFile("\tRequest attributes: ");
			Enumeration attributesNames = request.getAttributeNames();
			while(attributesNames.hasMoreElements()){
				String attributeName = (String)attributesNames.nextElement();
				writeToFile("\t\t" + attributeName+": " + (request.getAttribute(attributeName)).toString());
			}*/

			/*writeToFile("\tSession attributes: ");
			attributesNames = request.getSession(false).getAttributeNames();
			while(attributesNames.hasMoreElements()){
				String attributeName = (String)attributesNames.nextElement();
				writeToFile("\t\t" + attributeName+": " + (request.getSession(false).getAttribute(attributeName)).toString());
			}*/
		} catch (Exception e) {
			logger.error("Error :"+e,e);
		}
	}
	
	private void writeToFile(String content){
		/**logger.debug(content);
		try{
			String fileName = System.getProperty("catalina.base") + "/logs/sm-session.log";			
			File file =new File(fileName);

			//if file doesnt exists, then create it
			if(!file.exists()){
				logger.debug("Creating file : " + fileName);
				file.createNewFile();
			}
	
			//true = append file
			try(FileWriter fileWritter = new FileWriter(fileName,true);
					BufferedWriter bufferWritter = new BufferedWriter(fileWritter);) {
				bufferWritter.write("\n");
				bufferWritter.write(content);
			}
    	}catch(IOException e){
    		logger.error("Error :"+e,e);
    	}**/
	}
}