/**
 * 
 */
package com.elitecore.sm.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.iam.model.Staff;

/**
 * Mail Utility
 *  
 * @author Sunil Gulabani
 * Mar 24, 2015
 */
public class SendMail{
	
	private static Logger logger = Logger.getLogger(SendMail.class);
	
	public static boolean sendMailForPasswordReset(Staff staff, HttpServletRequest request) {

		// Get system properties
		Properties properties = System.getProperties();
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		String base = url.substring(0, url.length() - uri.length() + ctx.length()) + "/";
		boolean tlsEnable = MapCache.getConfigValueAsBoolean(SystemParametersConstant.AUTHENTICATION_TYPE, false);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", tlsEnable);
		properties.put("mail.smtp.host",  MapCache.getConfigValueAsString(SystemParametersConstant.SMTP_MAIL_HOST_IP, ""));
		properties.put("mail.smtp.port", MapCache.getConfigValueAsInteger(SystemParametersConstant.SMTP_MAIL_HOST_PORT, 25));

		Authenticator auth = new Authenticator() {
				// override the getPasswordAuthentication method
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							MapCache.getConfigValueAsString(SystemParametersConstant.FROM_EMAIL_ADDRESS, ""),
							MapCache.getConfigValueAsString(SystemParametersConstant.FROM_EMAIL_PASSWORD, ""));
				}
			};
		
		Session session = Session.getInstance(properties,auth);

		try {
			
			String resetLink = "<form name=\"submitForm\" method=\"POST\" action="+base+"verifyUserForForgotPassword"+">\n" + 
					"<input type=\"hidden\" name=\"username\" value=\""+staff.getUsername()+"\">\n" +
					"<input type=\"hidden\" name=\"forgotPWdOption\" value=\"username\">\n" +
					"<input type=\"hidden\" name=\"tokenExpirationTime\" value=\""+AESEncryptionDecryption.encrypt(System.currentTimeMillis()+"")+"\">\n" +
					"<a HREF=\"javascript:document.submitForm.submit()\" target=\"_blank\">Reset Link</a>\n" + 
					"</form>";
			
			String mailTemplet = MapCache.getConfigValueAsString(SystemParametersConstant.RESET_PASSWORD_MAIL_TEMPLET, "");
			mailTemplet = mailTemplet.replaceFirst("<username>", staff.getName());
			mailTemplet = mailTemplet.replaceFirst("<resetlink>", resetLink);
			mailTemplet = mailTemplet + "<br><br><img src=\""+MapCache.getConfigValueAsString(SystemParametersConstant.FOOTER_IMAGE, "")+"\">";
				
			InternetHeaders headers = new InternetHeaders();
			headers.addHeader("Content-type", "text/html; charset=UTF-8");
			MimeBodyPart body = new MimeBodyPart(headers, mailTemplet.getBytes("UTF-8"));
			 MimeMultipart multipart=new MimeMultipart();
		     multipart.addBodyPart(body);
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			message.setContent(multipart);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(MapCache.getConfigValueAsString(SystemParametersConstant.FROM_EMAIL_ADDRESS, "")));

			// Set To: header field of the header.
			/*for(String recipient: recipients){
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			}*/
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(staff.getEmailId()));

			// Set Subject: header field
			message.setSubject(MapCache.getConfigValueAsString(SystemParametersConstant.RESET_PASSWORD_MAIL_SUBJECT, ""));
			Transport.send(message);
			logger.info("Sent message successfully....");
			return true;
		} catch (Exception mex) {
			logger.error("issue in mail sent" ,mex);
		}
		return false;
	}
 }