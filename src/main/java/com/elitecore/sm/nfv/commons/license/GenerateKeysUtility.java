package com.elitecore.sm.nfv.commons.license;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * The Class GenerateKeysUtility.
 */
public class GenerateKeysUtility {
	
	/** The algorithmdesede. */
	public static final String ALGORITHMDESEDE="DESede";
	
	/** The algorithm. */
	public static final String ALGORITHM = "RSA";
	
	/** The customer. */
	public static final String CUSTOMER = "Customer Name";
	
	/** The location. */
	public static final String LOCATION = "Location";
	
	/** The macid. */
	public static final String MACID = "MacID";
	
	/** The product. */
	public static final String PRODUCT= "Product";
	
	/** The version. */
	public static final String VERSION = "Version";
	
	/** The startdate. */
	public static final String STARTDATE = "Start Date";
	
	/** The enddate. */
	public static final String ENDDATE = "End Date";
	
	/** The hostname. */
	public static final String HOSTNAME = "HostName";
	
	/** The tps. */
	public static final String TPS = "Tps";
	
	/** The daily records. */
	public static final String DAILY_RECORDS = "Daily Records";
	
	/** The monthly records. */
	public static final String MONTHLY_RECORDS = "Monthly Records";
	
	/** The publickey. */
	public static final String PUBLICKEY = "public.key";
	
	/** The pubkey. */
	public static final String PUBKEY = "pub.key";
	
	/** The deskey. */
	public static final String DESKEY = "des.key";
	
	/** The productinfokey. */
	public static final String PRODUCTINFOKEY = "Product_Info.txt.temp";
	 
	/** The licensekey. */
	public static final String LICENSEKEY = "License.key.temp";
	 
	/** The logger. */
	private static Logger logger = Logger.getLogger(GenerateKeysUtility.class);
	
    /**
     * Encrypt license detail.
     *
     * @param licData the lic data
     * @param REPOSITORY_PATH the repository path
     * @return the byte[]
     */
    public byte[] encryptLicenseDetail(String licData,String REPOSITORY_PATH) {
    	byte[] fileData = null;
    	String message = licData;
    	String privateKeyPath = REPOSITORY_PATH+PUBKEY;
    	String licenseKey = REPOSITORY_PATH+LICENSEKEY;
    	File productInfo = new File(REPOSITORY_PATH+PRODUCTINFOKEY);
    	File licenseKeyFile = new File(licenseKey); 
    	byte [] b1 = encryptSymMsg(message,REPOSITORY_PATH);
    	if(b1 != null){
    		if(writeBytesToFile(productInfo,b1)){
    			try (FileInputStream fis = new FileInputStream(productInfo)){
    				boolean result = encryptFile(privateKeyPath, fis, new FileOutputStream(licenseKeyFile));
    				if(result) {
    					try {
							fileData = IOUtils.toByteArray(new FileInputStream(licenseKey));
						} catch (IOException e) {
							logger.error( "Error occurred while reading encrypted file data " + e.getMessage());
		    				logger.trace(e);
						}
    				}
    			} catch (FileNotFoundException e) {
    				logger.error( "Error occurred while encrypting license data " + e.getMessage());
    				logger.trace(e);
    			} catch (IOException e1) {
    				logger.error( "Error occurred while encrypting license data " + e1.getMessage());
    				logger.trace(e1);
				}finally{
    				if(productInfo.exists())
    					productInfo.delete();
    				if(licenseKeyFile.exists())
    					licenseKeyFile.delete();
    			}
    		}
    	}
    	return fileData;
    }
    
    /**
     * Encrypt file.
     *
     * @param privateKeyPath the private key path
     * @param in the in
     * @param out the out
     * @return true, if successful
     */
    @SuppressWarnings("resource")
	private boolean encryptFile(String privateKeyPath, InputStream in, OutputStream out) {  
        byte[] b = new byte[128];  
        // Create a cipher object and use the generated key to initialize it
        PrivateKey privateKey = null;
        Cipher cipher = null;
        CipherOutputStream cos = null;
        boolean result = false;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(privateKeyPath))) {
        	privateKey = (PrivateKey) inputStream.readObject();
        	cipher = Cipher.getInstance(ALGORITHM);  //NOSONAR
        	cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
        	cos = new CipherOutputStream(out, cipher);  
        	int i;  
        	while ((i = in.read(b)) >= 0) {  
        		cos.write(b, 0, i);
        	} 
        	result = true;
        } catch (IOException | ClassNotFoundException  | NoSuchAlgorithmException  |
				NoSuchPaddingException  | InvalidKeyException e) {
			logger.error( "Error occurred while encrypting data " + e.getMessage());
            logger.trace(e);
		} finally {
			try {
				if(cos!=null) {
					cos.flush();
					cos.close();  
				}
			} catch (IOException e) {
	            logger.trace(e);
			}
			try{
				if(in!=null) 
					in.close();
			} catch (IOException e) {
				logger.trace(e);
			}
	         
		}
		return result;
    }
    
    /**
     * Encrypt message.
     *
     * @param str the str
     * @param REPOSITORY_PATH the repository path
     * @return the byte[]
     */
    private byte[] encryptSymMsg(String str,String REPOSITORY_PATH) {
    	Key symKey = null;
    	Cipher c = null;
    	String symKeyFile =REPOSITORY_PATH+DESKEY;
    	byte [] encryptedMessage = null;
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(symKeyFile)))){			
			symKey = (Key) inputStream.readObject();
			c = Cipher.getInstance(ALGORITHMDESEDE);//NOSONAR
			c.init(Cipher.ENCRYPT_MODE, symKey);
			encryptedMessage = c.doFinal(str.getBytes());
		} catch (IOException | ClassNotFoundException  | NoSuchAlgorithmException  |
				NoSuchPaddingException  | InvalidKeyException e) {
			logger.error( "Error occurred while encrypting data with des key " + e.getMessage());
            logger.trace(e);
		} catch (Exception e) {
			logger.error( "Error occurred while encrypting data with des key " + e.getMessage());
            logger.trace(e);		}
		return encryptedMessage;
    }
    
    /**
     * Write bytes to file.
     *
     * @param theFile the the file
     * @param bytes the bytes
     * @return true, if successful
     */
    private boolean writeBytesToFile(File theFile, byte[] bytes)  {
	    BufferedOutputStream bos = null;
	    boolean bSuccess = false;
	    try (FileOutputStream fos = new FileOutputStream(theFile)){	      
	      bos = new BufferedOutputStream(fos); 
	      bos.write(bytes);
	      bSuccess = true;
	    } catch (IOException e) {
			logger.error( "Error occurred while writing file data " + e.getMessage());
            logger.trace(e);
		}finally {
	      if(bos != null) {
	        try  {
	          bos.flush();
	          bos.close();
	        } catch(Exception e){
	            logger.trace(e);
	        }
	      }
	    }
	    return bSuccess;
	}
}
