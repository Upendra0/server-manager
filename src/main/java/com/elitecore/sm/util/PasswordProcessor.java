/**
 * 
 */
package com.elitecore.sm.util;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.SystemParametersConstant;

/**
 * @author Sunil Gulabani Apr 15, 2015
 */
public class PasswordProcessor {
	private static Logger logger = Logger.getLogger(PasswordProcessor.class);

	private PasswordProcessor() {
		// Private constructor to fix sonar critical issues
	}

	/**
	 * Encrypts the password based on algorithm provided.
	 * 
	 * @param password
	 * @param algorithm
	 * @return
	 */
	public static String encryptPassword(String password, String algorithm) {
		String encryptedPassword = "";//NOSONAR

		if (!StringUtils.isEmpty(algorithm)) {
			if (algorithm
					.equals(SystemParametersConstant.ENCRYPTION_POLICY_PLAIN)) {
				encryptedPassword = password;
			} else if (algorithm
					.equals(SystemParametersConstant.ENCRYPTION_POLICY_AES)) {
				try {
					encryptedPassword = AESEncryptionDecryption
							.encrypt(password);
				} catch (Exception e) {
					logger.error("Error in encrypt Password AES", e);
				}
			} else if (algorithm
					.equals(SystemParametersConstant.ENCRYPTION_POLICY_MD5)) {
				try {
					encryptedPassword = MD5EncryptionDecryption
							.encrypt(password);
				} catch (Exception e) {
					logger.error("Error in encrypt Password MD5", e);
				}
			}
		} else {
			encryptedPassword = password;
		}

		return encryptedPassword;
	}

	/**
	 * Decrypts the password based on algorithm provided.
	 * 
	 * @param password
	 * @param algorithm
	 * @return
	 */
	public static String decryptPassword(String password, String algorithm) {
		String decryptedPassword = "";//NOSONAR

		if (!StringUtils.isEmpty(algorithm)) {
			if (algorithm
					.equals(SystemParametersConstant.ENCRYPTION_POLICY_PLAIN)) {
				decryptedPassword = password;
			} else if (algorithm
					.equals(SystemParametersConstant.ENCRYPTION_POLICY_AES)) {
				try {
					decryptedPassword = AESEncryptionDecryption
							.decrypt(password);
				} catch (Exception e) {
					logger.error("Error in decrypt Password AES", e);
				}
			}
		} else {
			decryptedPassword = password;
		}

		return decryptedPassword;
	}
}