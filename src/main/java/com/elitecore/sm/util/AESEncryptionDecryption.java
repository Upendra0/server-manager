/**
 * 
 */
package com.elitecore.sm.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.elitecore.sm.common.exception.SMException;


/**
 * @author Sunil Gulabani Apr 15, 2015
 */
public class AESEncryptionDecryption {
	private AESEncryptionDecryption (){
		// utility class should have private constructor
	}
	
	 private static final String ALGO = "AES";
	 private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't',	'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
	    
	    /**
	     * Encrypts the data.
	     * @param Data
	     * @return
	     * @throws SMException
	     */
	    public static String encrypt(String Data) throws SMException {
	    	String encryptedValue="";
	    	try{
	    		 Key key = generateKey();
	 	        Cipher c = Cipher.getInstance(ALGO);//NOSONAR
	 	        c.init(Cipher.ENCRYPT_MODE, key);
	 	        byte[] encVal = c.doFinal(Data.getBytes());
	 	        encryptedValue = Base64.encodeBase64String(encVal);	
	    	}catch(Exception e){
				throw new  SMException(e);
	    	}
	       
	        return encryptedValue;
	    }

	    /**
	     * Decrypts the data.
	     * @param encryptedData
	     * @return
	     * @throws SMException
	     */
	    public static String decrypt(String encryptedData) throws SMException {
	    	String decryptedValue="";
	    	try{
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGO);//NOSONAR
	        c.init(Cipher.DECRYPT_MODE, key);
	        byte[] decordedValue = Base64.decodeBase64(encryptedData);
	        byte[] decValue = c.doFinal(decordedValue);
	        decryptedValue = new String(decValue);
	    	}catch(Exception e){
	    		throw new  SMException(e);
	    	}
	        return decryptedValue;
	    }
	    
	    private static Key generateKey() throws SMException {//NOSONAR
	        Key key = new SecretKeySpec(keyValue, ALGO);//NOSONAR
	        return key;
	}
	
}