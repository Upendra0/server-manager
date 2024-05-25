/**
 * 
 */
package com.elitecore.sm.util;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.elitecore.passwordutil.md5.MD5Encryption;


/**
 * @author Sunil Gulabani
 * Apr 24, 2015
 */
@Configuration
public class PasswordEncoderConfiguration implements PasswordEncoder {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	MD5Encryption md5 = new MD5Encryption();
	
	@Override
	public String encode(CharSequence rawPassword) {
		String password = "", ePassword = "";
		try {
			password = rawPassword.toString();
			ePassword = md5.crypt(password); 
		
		}catch (Exception e) {
			logger.trace(e);
		}
		return ePassword;
		
	}
	
	@Override
	public boolean matches(CharSequence rawPassword,
            String encodedPassword) {
		return encodedPassword.equals(encode(rawPassword));
	}
}
