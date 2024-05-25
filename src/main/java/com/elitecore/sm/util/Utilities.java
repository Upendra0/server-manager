package com.elitecore.sm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.springframework.util.StringUtils;

/**
 * The Class Utilities.
 */
public class Utilities {
	
	
	/** The Constant ZERO. */
	public static final String ZERO = "0";
	
	private static final String IPV4ADDRESSPATTERN =
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	Utilities(){
		//Default constructor
	}
	
	
	
	/**
	 * Gets the formatted service instance id.
	 *
	 * @param servInstanceId the serv instance id
	 * @return the formatted service instance id
	 */
	public static String getFormattedServiceInstanceId(int servInstanceId){
		
		String servInstanceIdStr = String.valueOf(servInstanceId);
		int count = 3;
		int length = servInstanceIdStr.length();
		while(count > length){
			servInstanceIdStr = ZERO + servInstanceIdStr;
			count--;
		}
		return servInstanceIdStr;
	}

	/**
	 * Gets the formatted service instance id.
	 *
	 * @param servInstanceIdStr the serv instance id str
	 * @return the formatted service instance id
	 */
	public static String getFormattedServiceInstanceId(String servInstanceIdStr){
		
		int count = 3;
		int length = servInstanceIdStr.length();
		while(count > length){
			servInstanceIdStr = ZERO + servInstanceIdStr;
			count--;
		}
		return servInstanceIdStr;
	}
	
	
	/** Method will convert comma separate String to integer array.
	 * @param parserMappingIds
	 * @return
	 */
	public static Integer[] convertStringArrayToInt(String parserMappingIds){
		Integer[] numbers = null;
		if (!StringUtils.isEmpty(parserMappingIds)){
			String [] ids = parserMappingIds.split(",");
			 numbers = new Integer[ids.length];
			for(int i = 0;i < ids.length;i++){
			   numbers[i] = Integer.parseInt(ids[i]);
			}
			return numbers;
		}else{
			return numbers;
		}
		
	}
	
	
	/**
	 * Method will replace backward slash for window path issue.
	 * @param path
	 * @return
	 */
	public static String replaceBackwardSlash(String path){
		if(path != null && path.contains("\\")){
			path = path.replace("\\", "\\\\");
		}
	  return path;
	}
	
	public static boolean isIPv4(String ipAddress) {
		if(ipAddress != null && !ipAddress.isEmpty()  && !ipAddress.contains(" ")){
			Pattern ipv4Pattern = Pattern.compile(IPV4ADDRESSPATTERN);
			Matcher ipv4Matcher = ipv4Pattern.matcher(ipAddress);
			return ipv4Matcher.matches();
		}
		else{
			return false;
		}
	}
	
	public static byte[] convertObjectToByteArray(Object object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] byteArray = null;
		try {
		  out = new ObjectOutputStream(bos);
		  out.writeObject(object);
		  out.flush();
		  byteArray = bos.toByteArray();
		} catch (IOException e) {//NOSONAR
			
		} finally {
		  try {
			  if(bos!=null)//NOSONAR
				  bos.close();
			  if(out!=null)
				  out.close();
		  } catch (IOException ex) {//NOSONAR
		  }
		}
		return byteArray;
	}
	
	public static Object convertByteArrayToObject(byte[] byteArray) {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(bis);
		  return in.readObject();
		} catch (Exception e) {//NOSONAR
			
		} finally {
		  try {
			  if(bis!=null)
				  bis.close();
			  if(in!=null)
				  in.close();
		  } catch (IOException ex) {//NOSONAR
		  }
		}
		return null;
	}
	
	public static byte[] convertBlobToByteArray(Blob blob) {
		
		int blobLength;
		byte[] blobAsBytes = null ;
		try {
			blobLength = (int) blob.length();
			blobAsBytes = blob.getBytes(1, blobLength);
		} catch (SQLException e) {//NOSONAR
			
		}  
		
		return blobAsBytes;
		
	}
	
	public static Date getCurrentHourDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime=LocalDateTime.of(localDateTime.getYear(),localDateTime.getMonth(),localDateTime.getDayOfMonth(),localDateTime.getHour(),0,0);
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static Date getLastHourDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime=LocalDateTime.of(localDateTime.getYear(),localDateTime.getMonth(),localDateTime.getDayOfMonth(),localDateTime.getHour(),0,0);
		localDateTime=localDateTime.minusHours(1);
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static long getTPSByHour(long total) {		
		return (total!=0)?(total/3600):0;
	}

	/**
	 * To encrypt the password to hexadecimal
	 * 
	 * @param pwd
	 * @return
	 */
	public static String encryptData(String pwd) {

		char[] chars = pwd.toCharArray();

		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		return hex.toString();

	}

	/**
	 * Decrypt password to ASCCII
	 * 
	 * @param pwd
	 * @return
	 */
	public static String decryptData(String pwd) {
		byte[] bytes = DatatypeConverter.parseHexBinary(pwd.trim());
		String decodedPwd = new String(bytes);
		return decodedPwd;
	}
}

