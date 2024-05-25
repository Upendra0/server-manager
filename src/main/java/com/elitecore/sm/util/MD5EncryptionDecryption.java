/**
 * 
 */
package com.elitecore.sm.util;

import java.security.MessageDigest;

import com.elitecore.sm.common.exception.SMException;

import liquibase.pro.packaged.en;

/**
 * @author Sunil Gulabani
 * Apr 24, 2015
 */
public class MD5EncryptionDecryption {
	
	
	
	private MD5EncryptionDecryption(){
		// used for static utility only no instantiation needed.
	}
	
	/**
	 * Encrypts the data
	 * @param value
	 * @return
	 * @throws SMException
	 */
	public static String encrypt(String value) throws SMException{
		String encryptedPassword = "";
		try{
			encryptedPassword = new PasswordEncoderConfiguration().encode(value);		
		}
			catch(Exception e){
		throw new  SMException(e);
		}
		return encryptedPassword;		
	}
}
