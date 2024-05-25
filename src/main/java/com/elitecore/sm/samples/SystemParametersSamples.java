/**
 * 
 */
package com.elitecore.sm.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.systemparam.model.ImageModel;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.model.SystemParameterGroupData;
import com.elitecore.sm.systemparam.model.SystemParameterValuePoolData;
import com.elitecore.sm.systemparam.service.SystemParameterService;

/**
 * @author Sunil Gulabani Apr 17, 2015
 */
public class SystemParametersSamples {

	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Insert System Parameter in database 
	 * @param systemParameterService
	 */
	public void addSystemParameters(SystemParameterService systemParameterService) {
		
		int i=1;
		
		SystemParameterGroupData genGroup = new SystemParameterGroupData();
		genGroup.setEnabled(true);
		genGroup.setName("General Parameters");

		SystemParameterGroupData pwdGroup = new SystemParameterGroupData();
		pwdGroup.setEnabled(true);
		pwdGroup.setName("Password Policy");

		SystemParameterGroupData custGroup = new SystemParameterGroupData();
		custGroup.setEnabled(true);
		custGroup.setName("Customer Details");

		systemParameterService.addSystemParameterGroup(genGroup);
		systemParameterService.addSystemParameterGroup(pwdGroup);
		systemParameterService.addSystemParameterGroup(custGroup);
		
		SystemParameterData dateFormatParam = new SystemParameterData(
				"Date Format",
				SystemParametersConstant.DATE_TIME_FORMAT,
				"MM/dd/yyyy hh:mm:ss",
				true,
				"dd : Day, MM : Month, yy : Year, hh : Hour, mm : Minutes, ss : Seconds",
				"([dMyhms//: ]+){1,50}",
				"systemParameter.dateFormat.error",
				genGroup, i++,new Date(),0,new Date(),0);

		SystemParameterData shortDateFormatParam = new SystemParameterData(
				"Short Date Format",
				SystemParametersConstant.DATE_FORMAT,
				"MM/dd/yyyy",
				true,
				"dd : Day, MM : Month, yy : Year",
				"[dMy//:-]{1,50}",
				"systemParameter.shortdateFormat.error",
				genGroup, i++,new Date(),0,new Date(),0);
	/*	SystemParameterData totalRowParam = new SystemParameterData(
				"Total Row", 
				SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, "10", true,
				"Total Number of Row Display in Search", "^[0-9]{1,3}$",
				"systemParameter.totalRow.error", genGroup, 3,new Date(),0,new Date(),0); */
		SystemParameterData sessionExpiryTime = new SystemParameterData(
				"Session Expiry Time", 
				SystemParametersConstant.SESSION_TIMEOUT_IN_MINUTES, "30", true,
				"Session Expiry Time in Minutes", "^[0-9]{1,10}$", "systemParameter.sessionExpTime.error",
				genGroup, i++,new Date(),0,new Date(),0);
		
		SystemParameterData adminLockStatus = new SystemParameterData(
				"Admin Lock Status", 
				SystemParametersConstant.LOCK_ADMIN_ON_WRONG_ATTEMPTS, "false", true,
				"Lock the Admin after invalid attempt or not", "[A-Z a-z]*",
				"systemParameter.adminLockStatus.error", genGroup, i++,new Date(),0,new Date(),0);
		
		/*SystemParameterData allowMultipleSession = new SystemParameterData(
				"Allow Multiple Session", 
				SystemParametersConstant.ALLOW_MULTIPLE_SESSION, "N", true,
				"Allow Multiple Session or not", "[A-Z a-z]*", "Error", genGroup, 7,new Date(),0,new Date(),0);*/
		
		/*SystemParameterData autoSyncStatus = new SystemParameterData(
				"Auto Synchronization", 
				SystemParametersConstant.AUTO_SYNC, "N", true,
				"Auto Synchronization Status", "[A-Z a-z]*", "systemParameter.autoSync.error", genGroup,
				9,new Date(),0,new Date(),0);*/
		SystemParameterData autoSyncTime = new SystemParameterData(
				"Auto Synchronization Time", 
				SystemParametersConstant.AUTO_SYNC_TIME, "120", true,
				"Auto Synchronization Time in minutes.  0 means no auto sync and greater than 0 means auto sync in specified minutes.", "^[1-9][0-9]*{1,10}$",
				"systemParameter.autoSyncTime.error", genGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData lockModeforSSOStaff = new SystemParameterData(
				"Lock Mode For SSO Staff",
				SystemParametersConstant.LOCKBYSSO,
				"false",
				true,
				"Make account lock after number of invalid attempt with Single Sign On",
				"[A-Za-z]*", "systemParameter.lockModeForSSOStaff.error", genGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData profileImageSizeKB = new SystemParameterData(
				"Staff profile picture size in KB",
				SystemParametersConstant.PROFILE_IMAGE_SIZE,
				"1024",
				true,
				"Maximum file size in KB for staff profile picture",
				"^[0-9]+{1,255}$", "systemParameter.profile.image.size.error", genGroup, i++,new Date(),0,new Date(),0);
		
		SystemParameterData exportPath = new SystemParameterData(
				"System Backup File Path",
				SystemParametersConstant.SYSTEM_BACKUP_FILE_PATH,
				SystemParametersConstant.SYSTEM_BACKUP_FILE_PATH_VALUE,
				true,
				"Path to store backup files when any entity is deleted from system",
				"^[\\_$\\w:/]{1,255}$", "systemParameter.system.backup.path.error", genGroup, i++,new Date(),0,new Date(),0);
		
		SystemParameterData nmsLink = new SystemParameterData("NMS Url", SystemParametersConstant.NMS_LINK, "http://www.google.com", true, "url of your NMS system", "((?:http?://|www.)(?:[-\\w]+.)*[-\\w]+.*){1,255}", "systemParameter.ems.link.error", genGroup, i++, new Date(),0,new Date(),0);
		
		/*SystemParameterData mandatoryEmpCode = new SystemParameterData(
				"Mandatory Employee Code", 
				SystemParametersConstant.MAND_EMP_CODE, "N", true,
				"Employee code is mandatory in Create Staff Screen or not", "[A-Z a-z]*", "systemParameter.mandatoryEmpCode.error", genGroup, 11,new Date(),0,new Date(),0);*/
		SystemParameterData noOfAllowedInvalidAttempt = new SystemParameterData(
				"Max Invalid Login Attempts",
				SystemParametersConstant.MAX_WRONG_PASSWORD_ATTEMPTS,
				"3",
				true,
				"No. of invalid attempt after which account is lock for specified time",
				"^[0-9]{1,10}$", "systemParameter.invalidAttempt.error", pwdGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData lockReleaseTime = new SystemParameterData(
				"Lock Release Time", 
				SystemParametersConstant.RELEASE_LOCK_ON_WRONG_ATTEMPTS_IN_MINUTES, "30", true,
				"No. of Minutes after which account lock is release",
				"^[0-9]{1,10}$", "systemParameter.lockReleaseTime.error", pwdGroup, i++,new Date(),0,new Date(),0);

		SystemParameterData pwdType = new SystemParameterData("Password Type",
				SystemParametersConstant.PASSWORD_TYPE, "^[^\\W\\d]{6,255}$", true,
				"Describe the type of password", "^[^\\W\\d]{6,255}$", "systemParameter.passwordType.error",
				pwdGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData pwdRegex = new SystemParameterData(
				"Regular Expression for password", 
				SystemParametersConstant.STAFF_PASSWORD,
				"^[^\\W\\d]{6,255}$", true,
				"Regular Expression for password type",
				"^[^\\W\\d]{6,255}$", "systemParameter.regExOnPwd.error", pwdGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData pwdExpiryDays = new SystemParameterData(
				"Password Expiry Duration",
				SystemParametersConstant.PASSWORD_EXPIRY_DAYS,
				"90",
				true,
				"User should automatically redirected for change password if password is not changed from the specified no. of days",
				"^[0-9]{1,10}$", "systemParameter.pwdExpDay.error", pwdGroup, i++,new Date(),0,new Date(),0);
		/*SystemParameterData pwdHistoryCheck = new SystemParameterData(
				"Password History Check", 
				SystemParametersConstant.PASSOWRD_HISTORY_CHECK, "Y", true,
				"New Password is not used previously", "[A-Za-z]*", "systemParameter.pwdHistoryCheck.error",
				pwdGroup, 18,new Date(),0,new Date(),0);*/
		SystemParameterData noOfPasswordHistoryCheck = new SystemParameterData(
				"History Check count", 
				SystemParametersConstant.NEW_PASSWORD_LAST_N_CHECK, "3", true,
				"Password will be checked for sepecified times. Keep 0 for no password check.",
				"^[0-9]{1,10}$", "systemParameter.noOfPwdHistory.error", pwdGroup, i++,new Date(),0,new Date(),0);
		
		SystemParameterData customerLogo = new SystemParameterData(
				"Logo (50 * 50)", 
				SystemParametersConstant.CUSTOMER_LOGO, "1", true,
				"Upload Small customer Logo for display",
				"[A-Za-z]*", "systemParameter.customerLogo.error", custGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData customerLogoLarge = new SystemParameterData(
				"Logo (50 * 100)", 
				SystemParametersConstant.CUSTOMER_LOGO_LARGE, "2", true,
				"Upload Large customer Logo for display",
				"[A-Za-z]*", "systemParameter.customerLogo.error", custGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData custName = new SystemParameterData("Name",
				SystemParametersConstant.CUSTOMER_NAME, "ELITECORE TECHNOLOGIES PVT. LTD.", true,
				"Name of the customer to be displayed alongwith logo",
				"^[\\w.][\\w\\s.]{1,255}+$",
				"systemParameter.customerName.error",
				custGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData custAddress = new SystemParameterData(
				"Address", 
				SystemParametersConstant.CUSTOMER_ADDRESS, "INDIA", true,
				"Address of the customer", "^[\\s\\w,.]{1,255}$", "systemParameter.customerAddress.error", custGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData customerWebsite = new SystemParameterData(
				"Website", 
				SystemParametersConstant.CUSTOMER_WEB_SITE, "www.elitecore.com", true,
				"Customer Web Site", "((?:https?://|www.)(?:[-\\w]+.)*[-\\w]+.*){1,255}", "systemParameter.customerWebSite.error", custGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData customerContact = new SystemParameterData(
				"Contact", 
				SystemParametersConstant.CUSTOMER_CONTACT, "+91-79-66065606", true,
				"Customer Contact no.", "^[0-9+-/]{1,50}$", "systemParameter.customerContact.error",
				custGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData contactPerson = new SystemParameterData(
				"Contact Person", 
				SystemParametersConstant.CONTACT_PERSON, "Maulik N. Shah", true,
				"Contact Person Name", "^[\\w.][\\w\\s.]{1,255}+$", "systemParameter.contactPerson.error",
				custGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData mobileNo = new SystemParameterData(
				"MobileNo", 
				SystemParametersConstant.CONTACT_MOBILENO, "+91-9909026864", true,
				"Customer mobile no.", "^[0-9+-/]{1,50}$", "systemParameter.contactMobileNo.error",
				custGroup, i++,new Date(),0,new Date(),0);
		SystemParameterData emailId = new SystemParameterData(
				"EmailId", 
				SystemParametersConstant.CONTACT_EMAILID, "maulik.shah@sterlite.com ", true,
				"Customer email id", "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", "systemParameter.contactEmailId.error",
				custGroup, i++,new Date(),0,new Date(),0);
		

		systemParameterService.addSystemParameter(dateFormatParam);
		systemParameterService.addSystemParameter(shortDateFormatParam);
		systemParameterService.addSystemParameter(noOfAllowedInvalidAttempt);
		systemParameterService.addSystemParameter(lockReleaseTime);
		systemParameterService.addSystemParameter(sessionExpiryTime);
//		systemParameterService.addSystemParameter(allowMultipleSession);
		systemParameterService.addSystemParameter(adminLockStatus);
		/*systemParameterService.addSystemParameter(autoSyncStatus);*/
		systemParameterService.addSystemParameter(autoSyncTime);
		/*systemParameterService.addSystemParameter(mandatoryEmpCode);*/
		systemParameterService.addSystemParameter(exportPath);
		systemParameterService.addSystemParameter(nmsLink);

		systemParameterService.addSystemParameter(pwdType);
		systemParameterService.addSystemParameter(pwdRegex);
		systemParameterService.addSystemParameter(pwdExpiryDays);
//		systemParameterService.addSystemParameter(pwdHistoryCheck);
		systemParameterService.addSystemParameter(noOfPasswordHistoryCheck);
		systemParameterService.addSystemParameter(lockModeforSSOStaff);

		systemParameterService.addSystemParameter(customerLogo);
		systemParameterService.addSystemParameter(customerLogoLarge);
		systemParameterService.addSystemParameter(custName);
		systemParameterService.addSystemParameter(custAddress);
		systemParameterService.addSystemParameter(customerContact);
		systemParameterService.addSystemParameter(customerWebsite);
		systemParameterService.addSystemParameter(profileImageSizeKB);
		systemParameterService.addSystemParameter(contactPerson);
		systemParameterService.addSystemParameter(mobileNo);
		systemParameterService.addSystemParameter(emailId);

		/*SystemParameterValuePoolData mandatoryEmpCodeValue1 = new SystemParameterValuePoolData(
				"Yes", "Y", mandatoryEmpCode);
		SystemParameterValuePoolData mandatoryEmpCodeValue2 = new SystemParameterValuePoolData(
				"No", "N", mandatoryEmpCode);*/

		/*SystemParameterValuePoolData autoSyncStatusValue1 = new SystemParameterValuePoolData(
				"Yes", "Y", autoSyncStatus);
		SystemParameterValuePoolData autoSyncStatusValue2 = new SystemParameterValuePoolData(
				"No", "N", autoSyncStatus);*/

/*		SystemParameterValuePoolData pwdHistoryCheckValue1 = new SystemParameterValuePoolData(
				"Yes", "Y", pwdHistoryCheck);
		SystemParameterValuePoolData pwdHistoryCheckValue2 = new SystemParameterValuePoolData(
				"No", "N", pwdHistoryCheck);*/

		SystemParameterValuePoolData adminLockStatusValue1 = new SystemParameterValuePoolData(
				"Yes", "true", adminLockStatus);
		SystemParameterValuePoolData adminLockStatusValue2 = new SystemParameterValuePoolData(
				"No", "false", adminLockStatus);

		/*SystemParameterValuePoolData allowMultipleSessionValue1 = new SystemParameterValuePoolData(
				"Yes", "Y", allowMultipleSession);
		SystemParameterValuePoolData allowMultipleSessionValue2 = new SystemParameterValuePoolData(
				"No", "N", allowMultipleSession);*/

		SystemParameterValuePoolData lockModeforSSOStaffValue1 = new SystemParameterValuePoolData(
				"Yes", "true", lockModeforSSOStaff);
		SystemParameterValuePoolData lockModeforSSOStaffValue2 = new SystemParameterValuePoolData(
				"No", "false", lockModeforSSOStaff);

		SystemParameterValuePoolData pwdTypeValue1 = new SystemParameterValuePoolData(
				"Alphabetic with min 6 chars", "^[^\\W\\d]{6,255}$", pwdType);
		SystemParameterValuePoolData pwdTypeValue2 = new SystemParameterValuePoolData(
				"Alphabetic with min 8 chars", "^[^\\W\\d]{8,255}$", pwdType);
		SystemParameterValuePoolData pwdTypeValue3 = new SystemParameterValuePoolData(
				"Alphanumeric with min 6 chars", "^(?=.*\\d)(?=.*[^\\W\\d])[\\w]{6,255}$", pwdType);
		SystemParameterValuePoolData pwdTypeValue4 = new SystemParameterValuePoolData(
				"Alphanumeric with min 8 chars", "^(?=.*\\d)(?=.*[^\\W\\d])[\\w]{8,255}$", pwdType);
		SystemParameterValuePoolData pwdTypeValue5 = new SystemParameterValuePoolData(
				"Numeric with min 6 digits", "^[0-9]{6,255}$", pwdType);
		SystemParameterValuePoolData pwdTypeValue6 = new SystemParameterValuePoolData(
				"Numeric with min 8 digits", "^[0-9]{8,255}$", pwdType);
		SystemParameterValuePoolData pwdTypeValue7 = new SystemParameterValuePoolData(
				"Regular Expression", SystemParametersConstant.PASSWORD_TYPE_REGEX, pwdType);

		systemParameterService.addSystemParameterValuePool(adminLockStatusValue1);
		systemParameterService.addSystemParameterValuePool(adminLockStatusValue2);
		/*systemParameterService.addSystemParameterValuePool(allowMultipleSessionValue1);
		systemParameterService.addSystemParameterValuePool(allowMultipleSessionValue2);*/
		/*systemParameterService.addSystemParameterValuePool(autoSyncStatusValue1);
		systemParameterService.addSystemParameterValuePool(autoSyncStatusValue2);*/
		/*systemParameterService.addSystemParameterValuePool(mandatoryEmpCodeValue1);
		systemParameterService.addSystemParameterValuePool(mandatoryEmpCodeValue2);*/
		systemParameterService.addSystemParameterValuePool(pwdTypeValue1);
		systemParameterService.addSystemParameterValuePool(pwdTypeValue2);
		systemParameterService.addSystemParameterValuePool(pwdTypeValue3);
		systemParameterService.addSystemParameterValuePool(pwdTypeValue4);
		systemParameterService.addSystemParameterValuePool(pwdTypeValue5);
		systemParameterService.addSystemParameterValuePool(pwdTypeValue6);
		systemParameterService.addSystemParameterValuePool(pwdTypeValue7);
		/*systemParameterService.addSystemParameterValuePool(pwdHistoryCheckValue1);
		systemParameterService.addSystemParameterValuePool(pwdHistoryCheckValue2);*/
		systemParameterService.addSystemParameterValuePool(lockModeforSSOStaffValue1);
		systemParameterService.addSystemParameterValuePool(lockModeforSSOStaffValue2);
	}

	/**
	 * Insert Default Customer Logo in database
	 * @param systemParameterService
	 * @param servletContext
	 */
	public void addSampleImage(SystemParameterService systemParameterService, ServletContext servletContext) {
		ImageModel imageModel = new ImageModel();
		FileInputStream imageInFile1 = null;
		FileInputStream imageInFile2 = null;

		List<ImageModel> imageModelList = systemParameterService.getAllImageModels();
		if (imageModelList != null && !imageModelList.isEmpty()) {
			
			logger.debug("image is Already avilable , no need to insert image");
			
		} else {
			try {
				logger.debug("Going to save image in DB ");
				File file = new File(servletContext.getRealPath("/WEB-INF/resources/img/crestel_small_logo.png"));
				imageInFile1 = new FileInputStream(file);
				byte[] imageData = new byte[(int) file.length()];
				int readDataBytes = imageInFile1.read(imageData);
				logger.debug("image file read bytes : " + readDataBytes);
				imageModel.setFileName("crestel_small_logo.png");
				imageModel.setExtRefId((systemParameterService.getSystemParameterByAlias(SystemParametersConstant.CUSTOMER_LOGO)).getId());
				imageModel.setImageType("png");

				imageModel.setImage(new javax.sql.rowset.serial.SerialBlob(imageData));
				imageModel.setImageBytes(Base64.encodeBase64(IOUtils.toByteArray(imageModel.getImage().getBinaryStream())));
				systemParameterService.addImage(imageModel);
				// Insert Large Logo
				File largeLogofile = new File(servletContext.getRealPath("/WEB-INF/resources/img/Crestel_logo_resize.jpg"));
				imageInFile2 = new FileInputStream(largeLogofile);
				byte[] largeLogoimageData = new byte[(int) largeLogofile.length()];
				int largeLogoBytes = imageInFile2.read(largeLogoimageData);
				logger.debug("image largeLogoBytes read bytes : " + largeLogoBytes);
				imageModel.setFileName("Crestel_logo_resize.jpg");
				imageModel.setExtRefId((systemParameterService.getSystemParameterByAlias(SystemParametersConstant.CUSTOMER_LOGO_LARGE)).getId());
				imageModel.setImageType("jpg");

				imageModel.setImage(new javax.sql.rowset.serial.SerialBlob(largeLogoimageData));
				imageModel.setImageBytes(Base64.encodeBase64(IOUtils.toByteArray(imageModel.getImage().getBinaryStream())));
				systemParameterService.addImage(imageModel);
			} catch (Exception ioe) {
				logger.error("Exception  logo read code:" + ioe, ioe);
			} finally {
				if (imageInFile1 != null) {
					try {
						imageInFile1.close();
					} catch (IOException e) {
						logger.error("Exception :" + e, e);
					}
				}
				if (imageInFile2 != null) {
					try {
						imageInFile2.close();
					} catch (IOException e) {
						logger.error("Exception :" + e, e);
					}
				}
			}
		}

	}

}