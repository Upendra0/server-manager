package com.elitecore.sm.license.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.LicenseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.license.dao.CircleDao;
import com.elitecore.sm.license.dao.LicenseDao;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.license.model.HourlyCDRCount;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.license.model.LicenseTypeEnum;
import com.elitecore.sm.license.model.LicenseUtilizationInfo;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.productconfig.service.ProductConfigurationService;
import com.elitecore.sm.server.dao.ServerDao;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.server.service.ServerTypeService;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Utilities;
import com.elitecore.sm.version.wrapper.VersionWrapper;

/**
 * 
 * @author Keyur Raval - Create the trial license service layer business logic
 *         for the save the trial license in the database - checking that is
 *         trial license exist by create the System Folder parallel to License
 *         Folder
 */

@org.springframework.stereotype.Service(value = "licenseService")
public class LicenseServiceImpl implements LicenseService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	private LicenseDao licenseDao;

	@Autowired
	ProductConfigurationService productConfiguration;

	@Autowired
	ServerTypeService serverTypeService;

	@Autowired
	@Qualifier(value = "licenseUtilityQualifier")
	LicenseUtility licenseUtility;

	@Autowired
	ServletContext servletcontext;

	@Autowired
	ServerInstanceService serverInstanceService;
	
	@Autowired
	ServerService serverService;	
	
	@Autowired
	ServerDao serverDao;

	@Autowired
	private CircleDao circleDao;
	
	@Transactional
	@Override
	public ResponseObject getServerManagerLicenseDetails() {
		ResponseObject responseObject = new ResponseObject();
		License linfo = licenseDao.getServerManagerLicenseDetails();
		logger.debug(linfo);
		if (linfo != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(linfo);
		} else {
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * add license details to database
	 * 
	 * @param license
	 * @return
	 */

	@Transactional(rollbackFor = Exception.class)
	@Override
	@Auditable(auditActivity = AuditConstants.SM_TRIAL_LICENSE, actionType = BaseConstants.LICENSE_ACTION, currentEntity = License.class, ignorePropList= "")
	public ResponseObject createTrialLicense(License license, String repositoryPath, String systemPath) {
		ResponseObject responseObject = new ResponseObject();
		File trailVerFile = null;
		try {
			Date startDate = new Date();
			Date endDate = getEndDate();
			license.setCreatedDate(startDate);
			license.setEndDate(endDate);
			license.setComponentType(LicenseConstants.LICENSE_SM);
			license.setServerInstance(null);
			license.setApplicationPath(System.getenv(BaseConstants.TOMCAT_HOME));
			logger.debug("Creating trail license from " + startDate + " to  " + endDate);
			trailVerFile = new File(repositoryPath + LicenseConstants.TRIAL_FILE);
			logger.debug("respository path is : " + repositoryPath);
			File sysDir = new File(systemPath);
			if (!sysDir.exists()) {
				sysDir.mkdir(); // Creating system folder.
			}
			File sysFile = new File(systemPath + LicenseConstants.SYS_FILE); // creating system file.
			if (!sysFile.exists()) {
				String encryptTrailData = DateFormatter.formatDate(startDate, LicenseConstants.DATE_FORMAT)
						+ "_"
						+ DateFormatter.formatDate(endDate, LicenseConstants.DATE_FORMAT)
						+ "_" + license.getProductType();
				boolean isSuccess = licenseUtility.encryptSymMsg(encryptTrailData, trailVerFile, repositoryPath); // adding encrypted data in file.
				if (isSuccess) {
					responseObject = isServerIdRegistered(license.getSmServerId());
					if (!responseObject.isSuccess()) {
						licenseDao.save(license);
						if (license.getId() != 0) {
							logger.debug("Trail license has been created successfully now apply default profiling for product "
									+ license.getProductType());
							productConfiguration.createCustomProfileUsingServerType(license);
							serverTypeService.setActiveProductType(license.getProductType());
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.TRIAL_LICENSE_ACTIVATE_SUCCESS);
							sysFile.createNewFile(); // Creating system file.
							File licenseFile = new File(repositoryPath + LicenseUtility.LICENSEFILE);
							if (licenseFile.exists()) {
								licenseFile.delete(); // Removing full license file if exists.
							}
						} else {
							responseObject = setLicenseFailureMessage(trailVerFile);
						}
					} else {
						logger.debug("Trail license already existed for serverid "
								+ license.getSmServerId());
						responseObject = setLicenseFailureMessage(trailVerFile);
					}
				} else {
					responseObject = setLicenseFailureMessage(trailVerFile);
				}
			} else {
				logger.debug("Trail license already created.");
				responseObject = setLicenseFailureMessage(trailVerFile);
				responseObject.setResponseCode(ResponseCode.TRAIL_LICENSE_ALREADY_EXIST);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(trailVerFile != null){
				responseObject = setLicenseFailureMessage(trailVerFile);
			}
		}
		return responseObject;
	}

	/**
	 * Method will set license failure message.
	 * 
	 * @return
	 */
	private ResponseObject setLicenseFailureMessage(File trailVerFile) {
		
		ResponseObject responseObject = new ResponseObject();
		if (trailVerFile != null && trailVerFile.exists()) {
			trailVerFile.delete(); // Removing trail file.
		}
		responseObject.setSuccess(false);
		responseObject.setResponseCode(ResponseCode.TRIAL_LICENSE_ACTIVATE_FAIL);
		return responseObject;
	}

	/**
	 * Method will return end date adding 30 days.
	 * 
	 * @return
	 */
	private Date getEndDate() {
		Date currentDate = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, 30);
		return cal.getTime();
	}

	/**
	 * Method will create full trial license
	 * 
	 * @param file
	 * @param productTypes
	 * @return
	 * @throws IOException
	 * @throws SMException
	 * @throws FileNotFoundException
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.SM_FULL_LICENSE, actionType = BaseConstants.LICENSE_ACTION, currentEntity = License.class, ignorePropList= "")
	public ResponseObject applyFullLicense(File file, String productTypes, String repositoryPath) {
		ResponseObject responseObject = new ResponseObject();
		String encryptedFileLoc;
		try {
			byte[] fileData = IOUtils.toByteArray(new FileInputStream(file));
			encryptedFileLoc = licenseUtility.saveFile(file.getName(), fileData, repositoryPath);
			// Decrypt the file from repository and store in hash Map
			String licenseData = licenseUtility.decrypt(encryptedFileLoc, repositoryPath);
			Map<String, String> licenseHashMap = licenseUtility.getData(licenseData);
			boolean isValidLicense = licenseUtility.validateProductType(productTypes.trim(), licenseHashMap.get(LicenseUtility.PRODUCT).trim());
			if (isValidLicense) {
				logger.debug("License products are validated successfully.");
				isValidLicense = licenseUtility.validateLicDate(licenseHashMap.get(LicenseUtility.START_DATE).trim(), licenseHashMap.get(LicenseUtility.END_DATE).trim());

				if (isValidLicense) {
					logger.info("License date validated successfully.");
					isValidLicense = licenseUtility.validateServerId(licenseHashMap.get(LicenseUtility.MACID));
					if (isValidLicense) {

						Date startDate = DateFormatter.formatDate(licenseHashMap.get(LicenseConstants.START_DATE), LicenseConstants.DATE_FORMAT);
						Date endDate = DateFormatter.formatDate(licenseHashMap.get(LicenseConstants.END_DATE), LicenseConstants.DATE_FORMAT);
						License license;

						responseObject = getLicenseByServerId(licenseHashMap.get(LicenseUtility.MACID));
						if (responseObject.isSuccess()) {
							license = (License) responseObject.getObject();
							license = new License(license.getId(), licenseHashMap.get(LicenseUtility.HOSTNAME), licenseHashMap.get(LicenseUtility.MACID), licenseHashMap.get(LicenseConstants.LOCATION), licenseHashMap.get(LicenseConstants.CUSTOMER), startDate, endDate, LicenseTypeEnum.FULL, licenseHashMap.get(LicenseUtility.PRODUCT).trim(), null, licenseHashMap.get(LicenseConstants.DAILY_RECORDS), licenseHashMap.get(LicenseConstants.MONTHLY_RECORDS));
							license.setComponentType(LicenseConstants.LICENSE_SM);
							license.setCreatedDate(new Date());
							license.setLastUpdatedDate(new Date());
							license.setServerInstance(null);
							license.setApplicationPath(System.getenv(BaseConstants.TOMCAT_HOME));
							licenseDao.merge(license); // updating data if license details is already available.

						} else {
							license = new License(0, licenseHashMap.get(LicenseUtility.HOSTNAME), licenseHashMap.get(LicenseUtility.MACID), licenseHashMap.get(LicenseConstants.LOCATION), licenseHashMap.get(LicenseConstants.CUSTOMER), startDate, endDate, LicenseTypeEnum.FULL, licenseHashMap.get(LicenseUtility.PRODUCT).trim(), null, licenseHashMap.get(LicenseConstants.DAILY_RECORDS), licenseHashMap.get(LicenseConstants.MONTHLY_RECORDS));
							license.setComponentType(LicenseConstants.LICENSE_SM);
							license.setCreatedDate(new Date());
							license.setLastUpdatedDate(new Date());
							license.setServerInstance(null);
							license.setApplicationPath(System.getenv(BaseConstants.TOMCAT_HOME));
							licenseDao.save(license); // Adding full license details to database

							if (license.getId() > 0) {
								productConfiguration.createCustomProfileUsingServerType(license); // Applying profiling for first time direct activation of full license.
							}

						}

						removeTrialLicenseFile(repositoryPath); // Remove trail license file and entry from database.

						logger.debug("Full license activated successfully for server manager. changing list of server type for cache.");
						serverTypeService.setActiveProductType(productTypes.trim()); // Disable other server type and
																						// reload map cache.

						responseObject.setResponseCode(ResponseCode.FULL_LICENSE_CREATE_SUCCESS);
						responseObject.setSuccess(true);
					} else {
						logger.debug("License Validation Failed, Server Id Does'nt match");
						responseObject = setFullLicenseFailureMessage(encryptedFileLoc);
					}
				} else {
					logger.debug("License Validation Failed, Date is Expired");
					responseObject = setFullLicenseFailureMessage(encryptedFileLoc);
				}
			} else {
				logger.debug("License is not proper, please contact support team.");
				responseObject = setFullLicenseFailureMessage(encryptedFileLoc);
			}
		} catch (IOException | SMException sme) {
			logger.error("Error occured whiel applying full license." + sme);
			logger.trace(sme);
			responseObject.setResponseCode(ResponseCode.FULL_LICENSE_CREATE_FAIL);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Method will set failure message for full license.
	 * 
	 * @param encryptedFileLoc
	 * @return
	 */
	private ResponseObject setFullLicenseFailureMessage(String encryptedFileLoc) {
		ResponseObject responseObject = new ResponseObject();
		responseObject.setResponseCode(ResponseCode.FULL_LICENSE_CREATE_FAIL);
		responseObject.setSuccess(false);
		File file = new File(encryptedFileLoc);
		if (file.exists()) {
			file.delete();
		}
		return responseObject;
	}

	/**
	 * Method will remove trail license file.
	 * 
	 * @param repositoryPath
	 * @throws SMException
	 */
	private void removeTrialLicenseFile(String repositoryPath) throws SMException {
		File trialFile = new File(repositoryPath + LicenseConstants.TRIAL_FILE);
		if (trialFile.exists()) {
			trialFile.delete();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.license.service.LicenseService#removeTrialLicense()
	 */
	@Transactional
	@Override
	public boolean clearLicenseDetails(Map<String, String> licenseHashMap) throws SMException {
		License license;
		boolean result = false;
		ResponseObject responseObject = getLicenseByServerId(licenseUtility.getServerId());
		if (responseObject.isSuccess()) {
			license = (License) responseObject.getObject();
			licenseDao.deleteObject(license); // Removing license details from
												// database.
		} else {
			logger.debug("Failed to clear license details.");
			result = false;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.elitecore.sm.license.service.LicenseService#updateLicenseDetails(
	 * java.util.Map)
	 */
	@Transactional
	@Override
	public boolean updateLicenseDetails(Map<String, String> licenseHashMap) {

		String serverId = licenseUtility.getServerId();
		boolean result;
		String ipAddress = licenseUtility.getIpAddress();
		ResponseObject responseObject = getLicenseByServerId(serverId);
		if (responseObject.isSuccess()) {
			License license = (License) responseObject.getObject();

			Date start = DateFormatter.formatDate(licenseHashMap.get(LicenseConstants.START_DATE), LicenseConstants.DATE_FORMAT);
			Date end = DateFormatter.formatDate(licenseHashMap.get(LicenseConstants.END_DATE), LicenseConstants.DATE_FORMAT);
			String applicationPath=license.getApplicationPath();
			if (licenseHashMap.containsKey(LicenseConstants.CUSTOMER)) {

				if (licenseHashMap.get(LicenseConstants.MACID).equalsIgnoreCase(LicenseConstants.UPGRADE)) {
					// Condition added as per discussion and as per old server
					// manager but as of now there is no use for this condition.
					license = new License(license.getId(), licenseHashMap.get(LicenseConstants.HOSTNAME), (licenseHashMap.get(LicenseConstants.MACID)
							+ LicenseConstants.HIFEN + serverId).toUpperCase(), licenseHashMap.get(LicenseConstants.LOCATION), licenseHashMap.get(LicenseConstants.CUSTOMER), start, end, LicenseTypeEnum.FULL, licenseHashMap.get(LicenseConstants.PRODUCT).toUpperCase(), null, licenseHashMap.get(LicenseConstants.DAILY_RECORDS), licenseHashMap.get(LicenseConstants.MONTHLY_RECORDS));
				} else {
					license = new License(license.getId(), licenseHashMap.get(LicenseConstants.HOSTNAME), licenseHashMap.get(LicenseConstants.MACID).toUpperCase(), licenseHashMap.get(LicenseConstants.LOCATION), licenseHashMap.get(LicenseConstants.CUSTOMER), start, end, LicenseTypeEnum.FULL, licenseHashMap.get(LicenseConstants.PRODUCT).toUpperCase(),null, licenseHashMap.get(LicenseConstants.DAILY_RECORDS), licenseHashMap.get(LicenseConstants.MONTHLY_RECORDS));
				}
			} else {
				license = new License(license.getId(), licenseUtility.getHostName(ipAddress), serverId.toUpperCase(), "", "", start, end, LicenseTypeEnum.TRIAL, licenseHashMap.get(LicenseConstants.PRODUCT).toUpperCase(), null, LicenseConstants.TRIAL_DAILY, LicenseConstants.TRIAL_MONTHLY);
			}
			license.setComponentType(LicenseConstants.LICENSE_SM);
			license.setServerInstance(null);
			license.setApplicationPath(applicationPath);
			licenseDao.merge(license); // updating license details.
			result = true;
			logger.debug("License details has been updated successfully.");
		} else {
			logger.debug("Failed to update license details.");
			result = false;
		}
		return result;
	}

	/**
	 * Method will check license details for full and trial.
	 * 
	 * @param repositoryPath
	 * @return
	 * @throws SMException
	 */
	@Transactional
	@Override
	public ResponseObject checkLicenseDetails(String repositoryPath) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		
		File licenseFile = new File(repositoryPath + LicenseUtility.LICENSEFILE);
		File trialFile = new File(repositoryPath + LicenseConstants.TRIAL_FILE);
		String serverId = licenseUtility.getServerId();
		logger.info("license file repositoryPath ---- " +repositoryPath);
				
		if (licenseFile.exists()) {
			logger.info("inside licenseFile exists");
			try {
				Map<String, String> licenseData = licenseUtility.getData(licenseUtility.decrypt(repositoryPath	+ LicenseUtility.LICENSEFILE, repositoryPath));
				if (licenseData.get(LicenseUtility.MACID).equalsIgnoreCase(LicenseConstants.UPGRADE)) {
					// Condition added as per discussion and as per old server manager but as of now there is no use for this condition.
					responseObject = isServerIdRegistered((licenseData.get(LicenseUtility.MACID)
							+ LicenseConstants.HIFEN + serverId).toUpperCase());
					logger.info("inside licenseFile exists if");
				} else {
					responseObject = isServerIdRegistered(serverId); // checking register server id for full license details.
					logger.info("inside licenseFile exists if else isServerIdRegistered "+serverId);
				}
			} catch (IOException e) {
				logger.error("Failed to validate license details due to exception " + e);
				responseObject.setSuccess(false); // checking register server id for trail license details.
			}
		} else if (trialFile.exists()) {
			logger.info("inside trialFile exists");
			responseObject = isServerIdRegistered(serverId); // checking register server id for full license details.
		} else {
			logger.info("Failed to get license details so redirecting to license agreement page.");
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Method will count remaining days for full/trail license.
	 * 
	 * @param endDate
	 * @return
	 */
	@Override
	public int countLicenseDays(String endDate) {
		int day = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(LicenseConstants.DATE_FORMAT);
		Date date;
		try {
			date = sdf.parse(endDate);
			day = (int) ((date.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			logger.error("Error occured while count license days difference  "
					+ e.getMessage());
			logger.trace(e);
			day = -1;
		}
		return day;
	}

	/**
	 * Method will check register server id.
	 * 
	 * @param serverId
	 * @return
	 */
	private ResponseObject isServerIdRegistered(String serverId) {
		ResponseObject responseObject = new ResponseObject();
		License license = licenseDao.getLicenseByServerId(serverId,LicenseUtility.TOMCAT_HOME);
		if (license != null) {
			logger.debug("Server id found successfully.");
			responseObject.setSuccess(true);
		} else {
			logger.debug("Failed to get license details for server id : " + serverId);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * /**
	 * 
	 * @param licenseDetails
	 */
	@SuppressWarnings("deprecation")
	private void isValidProductAlias(Map<String, String> licenseDetails) {
		boolean validProductAlias = false;
		String moduleAlias = licenseDetails.get(LicenseUtility.PRODUCT).toUpperCase();
		if (moduleAlias != null) {
			String[] moduleAliasArr = moduleAlias.split(",");
			for (String moduleName : moduleAliasArr) {
				validProductAlias = false;
				if (moduleName.equalsIgnoreCase(LicenseConstants.MEDIATION)
						|| moduleName.equalsIgnoreCase(LicenseConstants.IPLMS)
						|| moduleName.equalsIgnoreCase(LicenseConstants.CGF)) {
					validProductAlias = true;
				} else {
					break;
				}
			}
			if (!validProductAlias) {
				licenseDetails.put(LicenseUtility.PRODUCT, LicenseConstants.MEDIATION);
			} else {
				licenseDetails.put(LicenseUtility.PRODUCT, moduleAlias.toUpperCase());
			}
			MapCache.addConfigObject(SystemParametersConstant.LICENSE_PRODUCT, moduleAlias.toUpperCase());
		} else {
			licenseDetails.put(LicenseUtility.PRODUCT, LicenseConstants.MEDIATION);
			MapCache.addConfigObject(SystemParametersConstant.LICENSE_PRODUCT, LicenseConstants.MEDIATION);
		}
	}

	/**
	 * Method will get license details by server id.
	 * 
	 * @param serverId
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject getLicenseByServerId(String serverId) {
		ResponseObject responseObject = new ResponseObject();

		License license = licenseDao.getLicenseByServerId(serverId,LicenseUtility.TOMCAT_HOME);
		if (license != null) {
			logger.debug("License details fetch successfully.");
			responseObject.setObject(license);
			responseObject.setSuccess(true);
		} else {
			logger.debug("Failed to get license details for serverid " + serverId);
			responseObject.setObject(null);
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.elitecore.sm.license.service.LicenseService#validateLicenseDetails
	 * (java.lang.String)
	 */
	@Transactional
	@Override
	public ResponseObject validateLicenseDetails(String repositoryPath) {
		
		ResponseObject responseObject = new ResponseObject();
		String serverId = licenseUtility.getServerId(); // getting current server id based on ip and host combination
		if(serverId != null){
			responseObject  = isServerIdRegistered(serverId); // checking register server id for full license details.
			if(responseObject.isSuccess()){
				logger.debug("Checking full or trail license details.");
				File licenseFile = new File(repositoryPath + LicenseUtility.LICENSEFILE);
				File trialFile = new File(repositoryPath + LicenseConstants.TRIAL_FILE);
				if (licenseFile.exists()) {
					responseObject = validateFullLicenseDetails(repositoryPath, licenseFile);
				} else if (trialFile.exists()) {
					responseObject = validateTrailLicenseDetails(repositoryPath, trialFile, licenseFile);
				}
			}
		}else{
			logger.debug("Failed to get server mac id.");
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Method will validate full license details.
	 * 
	 * @param repositoryPath
	 * @param trialFile
	 * @param licenseFile
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ResponseObject validateFullLicenseDetails(String repositoryPath, File licenseFile) {
		ResponseObject responseObject = new ResponseObject();
		Map<String, String> licenseDetails;
		try {

			MapCache.addConfigObject(SystemParametersConstant.LICENSE_DETAILS, LicenseConstants.FULL);
			licenseDetails = licenseUtility.getData(licenseUtility.decrypt(repositoryPath
					+ LicenseUtility.LICENSEFILE, repositoryPath));
			isValidProductAlias(licenseDetails);
			updateLicenseDetails(licenseDetails);

			boolean isValideDate = licenseUtility.validateLicDate(licenseDetails.get(LicenseUtility.START_DATE), licenseDetails.get(LicenseUtility.END_DATE));
			if (!isValideDate) {
				clearLicenseDetails(licenseDetails);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SM_LICENSE_EXPIRE);
				responseObject.setModuleName(LicenseConstants.LICENSE_EXPIRED);
				responseObject.setArgs(new Object[] { licenseDetails.get(LicenseUtility.END_DATE) });
				if (licenseFile.exists()) {
					licenseFile.delete();
				}
			} else {
				int dayDiff = countLicenseDays(licenseDetails.get(LicenseUtility.END_DATE));
				responseObject.setSuccess(true);

				if (dayDiff != -1) { // Checking -1 for days calculation of
										// parsing not failed.
					if (dayDiff <= 0) {
						responseObject.setModuleName(LicenseConstants.LICENSE_EXPIRED);
						responseObject.setResponseCode(ResponseCode.SM_LICENSE_EXPIRE);
						responseObject.setArgs(new Object[] { licenseDetails.get(LicenseUtility.END_DATE) });
					} else if (dayDiff <= 15) {
						responseObject.setResponseCode(ResponseCode.FULL_LICENSE_REMINDER);
						responseObject.setModuleName(LicenseConstants.LICENSE_FULL_REMINDER);
						responseObject.setArgs(new Object[] { dayDiff + 1 });

						responseObject.setObject(licenseDetails);

					} else {
						logger.debug("Found valid full license details.");
						responseObject.setModuleName(LicenseConstants.LICENSE_VALID);
					}
				} else {
					responseObject.setModuleName(LicenseConstants.LICENSE_EXPIRED);
				}
			}
		} catch (IOException | SMException e) {
			logger.error("Error occured during get full license details " + e);
			logger.trace(e);
			if (licenseFile.exists()) {
				licenseFile.delete();
			}
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Method will validate trail license details.
	 * 
	 * @param trialFile
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ResponseObject validateTrailLicenseDetails(String repositoryPath, File trialFile, File licenseFile) {
		ResponseObject responseObject = new ResponseObject();
		Map<String, String> licenseDetails = new HashMap<>();
		try {
			MapCache.addConfigObject(SystemParametersConstant.LICENSE_DETAILS, LicenseConstants.TRIAL);
			byte[] trailVerByte = licenseUtility.readBytesFromFile(trialFile);
			String trailData = licenseUtility.decryptSymMsg(trailVerByte, repositoryPath);
			String[] trailDataArray = trailData.split("_");
			licenseDetails.put(LicenseUtility.START_DATE, trailDataArray[0]);
			licenseDetails.put(LicenseUtility.END_DATE, trailDataArray[1]);
			licenseDetails.put(LicenseUtility.PRODUCT, trailDataArray[2]);
			if (trailDataArray.length > 2) {
				isValidProductAlias(licenseDetails);
			}

			updateLicenseDetails(licenseDetails); // updating license details.
			boolean isValidLicense = licenseUtility.validateLicDate(trailDataArray[0], trailDataArray[1]);
			if (!isValidLicense) {
				clearLicenseDetails(licenseDetails);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SM_LICENSE_EXPIRE);
				responseObject.setModuleName(LicenseConstants.LICENSE_EXPIRED);
				responseObject.setArgs(new Object[] { trailDataArray[1] });
			} else {
				int dayDiff = countLicenseDays(trailDataArray[1]);
				responseObject.setSuccess(true);

				if (dayDiff != -1) { // -1 for days calculation to check parsing
										// not failed.
					if (dayDiff <= 0) {
						responseObject.setModuleName(LicenseConstants.LICENSE_EXPIRED);
						responseObject.setResponseCode(ResponseCode.SM_LICENSE_EXPIRE);
						responseObject.setArgs(new Object[] { trailDataArray[1] });

					} else if (dayDiff <= 9) {
						responseObject.setResponseCode(ResponseCode.TRAIL_LICENSE_REMINDER);
						responseObject.setModuleName(LicenseConstants.LICENSE_TRAIL_REMINDER);
						responseObject.setArgs(new Object[] { dayDiff + 1 });
						responseObject.setObject(licenseDetails);
						// Add short message code for less than 2 days
					} else {
						logger.debug("Found valid trail license.");
						responseObject.setModuleName(LicenseConstants.LICENSE_VALID);
					}
				} else {
					responseObject.setModuleName(LicenseConstants.LICENSE_EXPIRED);
					responseObject.setArgs(new Object[] { trailDataArray[1] });
				}
				if (licenseFile.exists()) {
					licenseFile.delete();
				}
			}
		} catch (IOException | SMException ioe) {
			logger.error("Error occure while reading trail license details  "
					+ ioe.getMessage());
			logger.trace(ioe);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Method will activate engine full license.
	 * 
	 * @param file
	 * @param productTypes
	 * @param repositoryPath
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.ENGINE_FULL_LICENSE, actionType = BaseConstants.LICENSE_ACTION, currentEntity = License.class, ignorePropList= "")
	public ResponseObject applyEngineFullLicense(File file, String productTypes, String repositoryPath, int serverInstanceId) throws IOException {
		logger.debug("Applying engine full license for server id : " + serverInstanceId);
		byte[] fileData = IOUtils.toByteArray(new FileInputStream(file));
		return applyEngineLicense(fileData,serverInstanceId);
	}

	@Transactional
	@Override
	public ResponseObject applyEngineFullLicense(byte[] fileData, int serverInstanceId) throws IOException {
		logger.debug("Applying engine full license for server id : " + serverInstanceId);
		return applyEngineLicense(fileData,serverInstanceId);
	}
	
	private ResponseObject applyEngineLicense(byte[] fileData, int serverInstanceId){
		ResponseObject responseObject = new ResponseObject();

		ServerInstance serverInstance = serverInstanceService.getServerInstance(serverInstanceId);
		RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
		boolean isSuccess = jmxConnection.activateFullVersion(fileData);
		if (isSuccess) {
			Map<String, String> licenseDetails = jmxConnection.licenseInfo();
			List<License> serverInstanceLicenseList = licenseDao.getAllServerInstancesByHost(serverInstance.getServer().getIpAddress());

			if (serverInstanceLicenseList != null && !serverInstanceLicenseList.isEmpty()) {
				for (int i = 0; i < serverInstanceLicenseList.size(); i++) {
					Date startDate = DateFormatter.formatDate(licenseDetails.get(LicenseConstants.START_DATE), LicenseConstants.DATE_FORMAT);
					Date endDate = DateFormatter.formatDate(licenseDetails.get(LicenseConstants.END_DATE), LicenseConstants.DATE_FORMAT);
					License serverLicenseDetails = serverInstanceLicenseList.get(i);
					serverLicenseDetails.setStartDate(startDate);
					serverLicenseDetails.setEndDate(endDate);

					String licenseVersion = licenseDetails.get(LicenseConstants.VERSION);
					if (licenseVersion.equalsIgnoreCase(LicenseTypeEnum.FULL.toString())) {
						serverLicenseDetails.setLicenceType(LicenseTypeEnum.FULL);
					} else {
						serverLicenseDetails.setLicenceType(LicenseTypeEnum.TRIAL);
					}
					licenseDao.merge(serverLicenseDetails);
				}
			} else {
				// Add code for remove engine license.
			}
			responseObject.setSuccess(true);
		} else {
			responseObject.setResponseCode(ResponseCode.FULL_LICENSE_CREATE_FAIL);
			responseObject.setResponseCodeNFV(NFVResponseCode.LICENSE_CREATION_FAIL);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	@Override
	public Map<String, String> licenseFullInfo(String serverInstanceIP,int port){
		logger.debug("Reading Engine license Info got IP : " + serverInstanceIP + " and port : " + port);
		Map<String, String> licenseDetails = null;
		ResponseObject responseObject = serverInstanceService.getServerInstanceByIPAndPort(serverInstanceIP, port);
		if (responseObject.isSuccess()) {
			logger.info("Server instance details found successfully!");
			ServerInstance serverInstance= (ServerInstance) responseObject.getObject();
			RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(),
					serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
					serverInstance.getConnectionTimeout());
			licenseDetails = jmxConnection.licenseFullInfo();
		}
		return licenseDetails;
	}
	
	@Override
	public Map<String, String> serverIdDetails(String serverInstanceIP,int port){
		logger.debug("Reading Engine license Info got IP : " + serverInstanceIP + " and port : " + port);
		Map<String, String> licenseDetails = null;
		ResponseObject responseObject = serverInstanceService.getServerInstanceByIPAndPort(serverInstanceIP, port);
		if (responseObject.isSuccess()) {
			logger.info("Server instance details found successfully!");
			ServerInstance serverInstance= (ServerInstance) responseObject.getObject();
			RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(),
					serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
					serverInstance.getConnectionTimeout());
			licenseDetails = jmxConnection.serverIdDetails();
		}
		return licenseDetails;
	}
	
	/**
	 * Method will fetch and count engine license details for server instance.
	 * 
	 * @param serverInstanceId
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject getLicenseDetailsByServerInstance(int serverInstanceId) {
		ResponseObject responseObject = new ResponseObject();
		License license;
		logger.debug("Checking server manager license details");
		String serverId = licenseUtility.getServerId();
		if (serverId != null && !StringUtils.isEmpty(serverId)) {
			responseObject = getLicenseByServerId(serverId);
			if (responseObject.isSuccess()) {
				license = (License) responseObject.getObject();
				if (LicenseTypeEnum.FULL.equals(license.getLicenceType())) {
					logger.debug("Fetching license details for server instance  "
							+ serverInstanceId);
					license = licenseDao.getLicenseDetailsByServerInstanceId(serverInstanceId);
					if (license != null) {
						int dayDiff = countLicenseDays(DateFormatter.formatDate(license.getEndDate(), LicenseConstants.DATE_FORMAT));
						if (dayDiff != -1) {
							if (dayDiff <= 10) {
								responseObject.setObject(new Integer(dayDiff));
								responseObject.setModuleName(LicenseConstants.MODULE_FULL);
							} else {
								responseObject.setObject(new String("-"));
								responseObject.setModuleName(LicenseConstants.MODULE_FULL);
							}
							responseObject.setSuccess(true);
						} else {
							responseObject.setModuleName(LicenseConstants.MODULE_FULL);
							responseObject.setResponseCode(ResponseCode.LICENSE_FOUND);
							responseObject.setSuccess(false);
							logger.debug("Failed to get Days for licese details");
						}
					} else {
						logger.debug("Failed to get license details for server instance id  "
								+ serverInstanceId);
						responseObject.setResponseCode(ResponseCode.LICENSE_FOUND);
						responseObject.setModuleName(LicenseConstants.MODULE_FULL);
						responseObject.setSuccess(false);
					}
				} else {
					responseObject.setSuccess(true);
					responseObject.setModuleName(LicenseConstants.MODULE_TRIAL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setModuleName(LicenseConstants.MODULE_TRIAL);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setModuleName(LicenseConstants.MODULE_TRIAL);
		}
		return responseObject;
	}

	/**
	 * Method will fetch and count engine license details for server instance.
	 * 
	 * @param serverInstanceId
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject getLicenseDetailsByComponentType() {

		logger.info("Fetching license details where componentType = serverManager for aboutUs");
		
		ResponseObject responseObject = new ResponseObject();
		License license = licenseDao.getLicenseDetailsByComponentType();

		if(license !=null){
			responseObject.setSuccess(true);
			responseObject.setObject(license);
			logger.info("Successfully fetched license details");
		}
		else{
			logger.info("Failed to retrieve license details");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.ABOUTUS_NULL_LICENSE_DATA);
		}
		return responseObject;
		
	}

	/**
	 * Method will fetch data from license table for grid in aboutUs page
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public List<License> getPaginatedList(int startIndex, int limit, String sidx, String sord) {

		logger.info("Fetching license details for AboutUs page  ");

		return licenseDao.getLicensePaginatedList(License.class, startIndex, limit, sidx, sord);
	}

	/**
	 * Method will give count of license where type is Engine.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public long getLicenseDetailsCount() {
		logger.info("Fetching count :");
		return licenseDao.getLicenseCount(License.class);
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject getEngineLicenseDetailsByInstance(int serverTypeId) {
		ResponseObject responseObject = new ResponseObject();
		if(serverTypeId > 0 ){
			Server server = serverDao.getServer(serverTypeId);
				if(server != null && server.getIpAddress() != null){
					RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(server.getIpAddress(), server.getUtilityPort(), 10, 10, 10);
					String crestelHome = remoteJMXHelper.getCrestelPEngineHome();
					
					if(crestelHome != null){
						Map<String, String> serverDetailMap = remoteJMXHelper.serverIdDetails();
						if(serverDetailMap != null){
							responseObject.setSuccess(true);
							responseObject.setObject(serverDetailMap);
						}else{
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.FAIL_DOWNLOAD_ENGINE_FILE);
						}
					}
					else if (remoteJMXHelper.getErrorMessage() != null){
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.P_ENGINE_NOT_RUNNING);
						responseObject.setArgs(new Object[] {server.getIpAddress(),String.valueOf(server.getUtilityPort())});
						responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
					}
				}else{
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FAIL_DOWNLOAD_ENGINE_FILE);
				}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_DOWNLOAD_ENGINE_FILE);
		}
		return responseObject;
	}

	/**
	 * Method will check system file exist or not to disable the trail license button for license activation.
	 * @see com.elitecore.sm.license.service.LicenseService#checkSystemFile(java.lang.String)
	 * @param systemPath
	 */
	@Override
	public boolean checkSystemFile(String systemPath) {
		boolean isFileExist = false;
		File sysFile = new File(systemPath + LicenseConstants.SYS_FILE); // creating system file.
		if(sysFile.exists()){
			isFileExist = true;
		}
		return isFileExist;
	}

	@Override
	@Transactional
	public ResponseObject getLicenseDetailsByInstanceId(int serverInstanceId) {
		logger.debug("Fetching license detaisl for server instance id  ::  " + serverInstanceId);
		ResponseObject responseObject = new ResponseObject();
		License license = licenseDao.getLicenseDetailsByInstanceId(serverInstanceId);
		if(license != null ){
			logger.info("License details fetch successfully.");
			responseObject.setObject(license);
			responseObject.setSuccess(true);
		}else{
			logger.info("Failed to get license details for server instance id : " + serverInstanceId);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/* (non-Javadoc)
	 * @see com.elitecore.sm.license.service.LicenseService#updateLicenseDetails(com.elitecore.sm.license.model.License)
	 */
	@Override
	@Transactional
	public ResponseObject updateLicenseDetails(License license) {
		ResponseObject responseObject = new ResponseObject();
		licenseDao.merge(license);
		responseObject.setSuccess(true);
		return responseObject;
	}

	/**
	 * Method will fetch the data from LicenseServiceImpl and VersionDaoImpl
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public ResponseObject getVersionDetails() {

		logger.info("Fetching version Details for aboutus page  :");
		ResponseObject responseObject;
		responseObject = getLicenseDetailsByComponentType();
		VersionWrapper versionWrapper;
		String imageName=System.getenv(BaseConstants.IMAGE_NAME);
		if(imageName!=null && !imageName.equals("")){
	    	imageName=imageName.substring(imageName.lastIndexOf(":")+1,imageName.length());
	    }else{
	    	imageName="";
	    }
		License license = (License) responseObject.getObject();
		
		if (license != null){
			versionWrapper = new VersionWrapper(imageName, "", "", "", "",  DateFormatter.formatDate(license.getStartDate()), DateFormatter.formatDate(license.getEndDate()), license.getLicenceType().toString(), license.getProductType());
			responseObject.setSuccess(true);
			responseObject.setObject(versionWrapper);
			logger.info("showing installed image name and license details");
		}else if(imageName!=null){
			versionWrapper = new VersionWrapper(imageName, "", "", "", "",  "", "", "", "");
			responseObject.setSuccess(true);
			responseObject.setObject(versionWrapper);
			logger.info("showing installed image name only");
		}else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.ABOUTUS_NULL_VERSION_WRAPPER_DATA);
			logger.info("Failed to retrieve the data from image name and License tables :");
		}

		return responseObject;
	}
	
	/**
	 * Method will upgrade default license of Engine
	 * 
	 * @param file
	 * @param repositoryPath
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPGRADE_DEFAULT_LICENSE, actionType = BaseConstants.LICENSE_ACTION, currentEntity = License.class, ignorePropList= "")
	public ResponseObject upgradeEngineDefaultLicense(File file, String repositoryPath, int serverId) throws IOException {
		logger.debug("Upgrading engine default license for server id : " + serverId);
		byte[] fileData = IOUtils.toByteArray(new FileInputStream(file));
		return upgradeEngineLicense(fileData,serverId);
	}
	
	private ResponseObject upgradeEngineLicense(byte[] fileData, int serverId){
		ResponseObject responseObject = new ResponseObject();
		Server server = serverService.getServer(serverId);
		RemoteJMXHelper jmxConnection = new RemoteJMXHelper(server.getIpAddress(), server.getUtilityPort(),3,5000,20);
		String pEngineHome = jmxConnection.getCrestelPEngineHome();
		if (pEngineHome != null) {
			boolean isSuccess = jmxConnection.upgradeDefaultLicense(fileData);
			//boolean isContainer = server.isContainerEnvironment();
			if (isSuccess) {
				Map<String, String> licenseDetails = jmxConnection.getLicenseInfo();
				if (licenseDetails != null && !licenseDetails.isEmpty()) {
					List<License> serverInstanceLicenseList = licenseDao.getAllServerInstancesByHostContainerEnv(server.getIpAddress(),server.getUtilityPort(),true);
					if (serverInstanceLicenseList != null && !serverInstanceLicenseList.isEmpty()) {
						for (int i = 0; i < serverInstanceLicenseList.size(); i++) {
							License serverLicenseDetails = serverInstanceLicenseList.get(i);
							Server server1 = serverLicenseDetails.getServerInstance().getServer();
							//if (isContainer) {
								if (/*server1.isContainerEnvironment() &&*/ (server1.getUtilityPort() == server.getUtilityPort())) {
									Date startDate = DateFormatter.formatDate(licenseDetails.get(LicenseConstants.START_DATE), LicenseConstants.DATE_FORMAT);
									Date endDate = DateFormatter.formatDate(licenseDetails.get(LicenseConstants.END_DATE), LicenseConstants.DATE_FORMAT);
									String tps = licenseDetails.get(LicenseConstants.TPS);
									serverLicenseDetails.setStartDate(startDate);
									serverLicenseDetails.setEndDate(endDate);
								//	serverLicenseDetails.setTps(tps);
									serverLicenseDetails.setLicenceType(LicenseTypeEnum.FULL);
									licenseDao.merge(serverLicenseDetails);
								}
							/*} else {
								if (!server1.isContainerEnvironment()) {
									Date startDate = DateFormatter.formatDate(licenseDetails.get(LicenseConstants.START_DATE), LicenseConstants.DATE_FORMAT);
									Date endDate = DateFormatter.formatDate(licenseDetails.get(LicenseConstants.END_DATE), LicenseConstants.DATE_FORMAT);
									String tps = licenseDetails.get(LicenseConstants.TPS);
									serverLicenseDetails.setStartDate(startDate);
									serverLicenseDetails.setEndDate(endDate);
									serverLicenseDetails.setTps(tps);
									serverLicenseDetails.setLicenceType(LicenseTypeEnum.FULL);
									licenseDao.merge(serverLicenseDetails);
								}
								
							}*/
						}
					} else {
						// Add code for remove engine license.
					}
				}
				responseObject.setSuccess(true);
			} else {
				responseObject.setResponseCode(ResponseCode.FULL_LICENSE_CREATE_FAIL);
				responseObject.setResponseCodeNFV(NFVResponseCode.LICENSE_CREATION_FAIL);
				responseObject.setSuccess(false);
			}
		} else if (jmxConnection.getErrorMessage() != null){
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.P_ENGINE_NOT_RUNNING);
			responseObject.setResponseCodeNFV(NFVResponseCode.P_ENGINE_NOT_RUNNING);
			responseObject.setArgs(new Object[] {server.getIpAddress(),String.valueOf(server.getUtilityPort())});
			responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
		}
		
		return responseObject;
	}

	@Transactional
	@Override
	public ResponseObject applyNewLicenseKeySM(String repositoryPath) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("inside method applyNewLicenseKeySM : " + repositoryPath);
		// Read existing License.key
		File licenseKey = new File (repositoryPath + LicenseUtility.LICENSEFILE);
		File trialKey = new File (repositoryPath + LicenseUtility.TRIAL_STR);
		Map<String, String> licenseDetails;
		Map<String, String> newLicenseDetails;
		try {
			if (licenseKey.exists()) { 
				licenseDetails = licenseUtility.getData(licenseUtility.decrypt(repositoryPath + LicenseUtility.LICENSEFILE, repositoryPath));
				if(licenseDetails.containsKey(LicenseUtility.IS_ABOVE_730)) {
					logger.debug("License.key found with Is above 730 key, so nothing to do");
				} else {
					logger.debug("old License.key found. Is above 730 key not found. so deleting existing license key and applying new License.key");
					String licenseString = licenseUtility.convertLicenseDetailsToString(licenseDetails);
					String isLicenseCreated = licenseUtility.encryptLicenseDetail(licenseString,repositoryPath);
					if (isLicenseCreated != null && !(isLicenseCreated.equals("false"))) {
						logger.info("New license created with version and no of container details");
					}
					newLicenseDetails = licenseUtility.getData(licenseUtility.decrypt(repositoryPath + LicenseUtility.LICENSEFILE, repositoryPath));
					updateServerIdForSM(newLicenseDetails);
					responseObject.setSuccess(true);
				}
			} else if (trialKey.exists()) {
				byte[] trailVerByte = licenseUtility.readBytesFromFile(trialKey);
				String trailData = licenseUtility.decryptSymMsg(trailVerByte, repositoryPath);
				String[] trailDataArray = trailData.split("_");
				Map<String, String> trial_licenseDetails = new HashMap<>();
				trial_licenseDetails.put(LicenseUtility.START_DATE, trailDataArray[0]);
				trial_licenseDetails.put(LicenseUtility.END_DATE, trailDataArray[1]);
				trial_licenseDetails.put(LicenseUtility.PRODUCT, trailDataArray[2]);
				updateServerIdForSM(trial_licenseDetails);
				responseObject.setSuccess(true);
			}
		}
		catch (IOException e ) {
			logger.error(e);
			responseObject.setSuccess(false);	
		}
		return responseObject;
	}
	
	private ResponseObject getSMLicenseDetails() {
		ResponseObject responseObject = new ResponseObject();
		License linfo = licenseDao.getSMLicenseDetails();
		logger.debug(linfo);
		if (linfo != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(linfo);
		} else {
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	
	private boolean updateServerIdForSM(Map<String, String> licenseHashMap) {
		ResponseObject responseObject = getSMLicenseDetails();
		String serverId = licenseUtility.getServerId();
		boolean result;
		String ipAddress = licenseUtility.getIpAddress();
		
		if (responseObject.isSuccess()) {
			License license = (License) responseObject.getObject();
			
			Date start = DateFormatter.formatDate(licenseHashMap.get(LicenseConstants.START_DATE), LicenseConstants.DATE_FORMAT);
			Date end = DateFormatter.formatDate(licenseHashMap.get(LicenseConstants.END_DATE), LicenseConstants.DATE_FORMAT);
			String applicationPath=license.getApplicationPath();
			
			if (licenseHashMap.containsKey(LicenseConstants.CUSTOMER)) {
				license = new License(license.getId(), licenseHashMap.get(LicenseConstants.HOSTNAME), licenseHashMap.get(LicenseConstants.MACID).toUpperCase(), licenseHashMap.get(LicenseConstants.LOCATION), licenseHashMap.get(LicenseConstants.CUSTOMER), start, end, LicenseTypeEnum.FULL, licenseHashMap.get(LicenseConstants.PRODUCT).toUpperCase(), null, licenseHashMap.get(LicenseConstants.DAILY_RECORDS), licenseHashMap.get(LicenseConstants.MONTHLY_RECORDS));
				
			} else {
				license = new License(license.getId(), licenseUtility.getHostName(ipAddress), serverId.toUpperCase(), "", "", start, end, LicenseTypeEnum.TRIAL, licenseHashMap.get(LicenseConstants.PRODUCT).toUpperCase(), null, LicenseConstants.TRIAL_DAILY, LicenseConstants.TRIAL_MONTHLY);
			}
			
			license.setComponentType(LicenseConstants.LICENSE_SM);
			license.setServerInstance(null);
			license.setApplicationPath(applicationPath);
			licenseDao.merge(license); // updating license details.
			result = true;
			logger.debug("License details has been updated successfully.");
		} else {
			logger.debug("Failed to update license details.");
			result = false;
		}
		return result;
	}
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.SM_FULL_LICENSE, actionType = BaseConstants.LICENSE_ACTION, currentEntity = License.class, ignorePropList= "")
	public ResponseObject upgradeContainerLicense(File file, String repositoryPath, String hostIP, String containerLicPath) {
		ResponseObject responseObject = new ResponseObject();
		String encryptedFileLoc = "";
		try {
			byte[] fileData = IOUtils.toByteArray(new FileInputStream(file));
			encryptedFileLoc = licenseUtility.saveFile(file.getName(), fileData, containerLicPath);
			// Decrypt the file from repository and store in hash Map
			String licenseData = licenseUtility.decrypt(encryptedFileLoc, repositoryPath);
			Map<String, String> licenseHashMap = licenseUtility.getData(licenseData);
			
			if (licenseHashMap.containsKey(LicenseUtility.NO_OF_CONTAINER)) {
				logger.debug("License key containes No of Container Details.");
				responseObject.setResponseCode(ResponseCode.CONTAINER_LICENSE_UPGRADE_SUCCESS);
				responseObject.setArgs(new Object[] {hostIP});
				responseObject.setSuccess(true);
			} else {
				logger.debug("License key does not contain No of Container Details.");
				responseObject.setResponseCode(ResponseCode.CONTAINER_LICENSE_UPGRADE_FAIL);
				responseObject.setArgs(new Object[] {hostIP});
				responseObject.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error("Error occured while applying license for container." + e);
			logger.trace(e);
			File lFile = new File(encryptedFileLoc);
			if (lFile.exists()) {
				lFile.delete();
			}
			
			responseObject.setResponseCode(ResponseCode.CONTAINER_LICENSE_UPGRADE_FAIL);
			responseObject.setArgs(new Object[] {hostIP});
			responseObject.setSuccess(false);
		}
			return responseObject;
	}
	
	public int getLicensedContainerForHost(String repositoryPath , String containerLicPath) {
		File licenseFile = new File(containerLicPath + LicenseUtility.LICENSEFILE);
		File defaultLicenseFile = new File(repositoryPath + LicenseUtility.LICENSEFILE);
		
		int containers = 0;
		try {
			byte[] fileData;
			if (licenseFile.exists()) {
				fileData = IOUtils.toByteArray(new FileInputStream(licenseFile));
				String encryptedFileLoc = licenseUtility.saveFile(licenseFile.getName(), fileData, containerLicPath);
				String licenseData = licenseUtility.decrypt(encryptedFileLoc, repositoryPath);
				Map<String, String> licenseHashMap = licenseUtility.getData(licenseData);
				if (licenseHashMap.containsKey(LicenseUtility.NO_OF_CONTAINER)) {
					containers =  Integer.parseInt(licenseHashMap.get(LicenseUtility.NO_OF_CONTAINER));
					logger.debug("License key containes : " + containers);
				}
			} else if (defaultLicenseFile.exists()) {
				fileData = IOUtils.toByteArray(new FileInputStream(defaultLicenseFile));
				String encryptedFileLoc = licenseUtility.saveFile(defaultLicenseFile.getName(), fileData, repositoryPath);
				String licenseData = licenseUtility.decrypt(encryptedFileLoc, repositoryPath);
				Map<String, String> licenseHashMap = licenseUtility.getData(licenseData);
				if (licenseHashMap.containsKey(LicenseUtility.NO_OF_CONTAINER)) {
					containers =  Integer.parseInt(licenseHashMap.get(LicenseUtility.NO_OF_CONTAINER));
					logger.debug("Default License key containes : " + containers);
				} else {
					containers =  Integer.parseInt(LicenseUtility.DEFAULT_NO_OF_CONTAINER);
					logger.debug("Default License key does not contain No of Containers. so returning : " + containers);
				}
			} else {
				containers =  Integer.parseInt(LicenseUtility.DEFAULT_NO_OF_CONTAINER);
				logger.debug("No License.key found. so returning default no of containers : " + containers);
			}
		} catch (Exception e) {
			containers =  Integer.parseInt(LicenseUtility.DEFAULT_NO_OF_CONTAINER);
			logger.trace(e);
			logger.debug("Error occured while reading containers from License.key. so returning default no of containers : " + containers);
		}
		
		return containers;
	}
	
	@Transactional
	@Override
	public Map<Date, Long> getHourWiseTotalUtilizationMap() {
		logger.debug("Fetching Hour wise CDR Count summary by process date");
        Map<Date, Long> map = licenseDao.getHourWiseTotalUtilizationMap();
		return map;
	}

	@Transactional
	@Override
	public Map<Date, Long> getHourWiseCurrentUtilizationMap() {
		logger.debug("Fetching Hour wise CDR Count summary by cdr date");
        Map<Date, Long> map = licenseDao.getHourWiseCurrentUtilizationMap();
		return map;
	}
	
	@Transactional
	@Override
	public List<HourlyCDRCount> getHourlyCDRDCountByProcessDate(Date processDate) {
		logger.debug("Fetching Hourly CDR count details by process date");
		return licenseDao.getHourlyCDRDCountByProcessDate(processDate);
	}
	
	@Override
	public long getMaxLicneseUtilizationCount(Map<Date, Long> map) {
		long maxCount = 0l;
		for (long cnt : map.values())  {
			if(cnt>maxCount)
				maxCount=cnt;
		}             
		return maxCount;
	}
	
	@Override
	public long getCurrentLicenseUtilizationCountFromMap(Map<Date, Long> map) {
		long currentTps = 0l;
		if(map!=null && map.get(Utilities.getLastHourDate())!=null) {
			currentTps = map.get(Utilities.getLastHourDate());
		}
		return currentTps;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateLicenseUtilizationMap(List<HourlyCDRCount> list) {
		Map<Date, Long> processDateMap=  (Map<Date, Long>) MapCache.getConfigValueAsObject(BaseConstants.HOUR_WISE_TOTAL_TPS_MAP);		
		Map<Date, Long> cdrDateMap= (Map<Date, Long>) MapCache.getConfigValueAsObject(BaseConstants.HOUR_WISE_CURRENT_TPS_MAP);
		long totalCountByProcessDate = 0l;
		long newCDRCount;
		long oldCDRCount;
		Date processDate = null;
		Date cdrDate = null;
		
		if(list!=null && !list.isEmpty()) {
			for (HourlyCDRCount obj : list) {
				processDate = obj.getProcessEndDate();
				cdrDate = obj.getCdrDate();			
				newCDRCount = obj.getTotalCDRCount();	
				//updating total cdr count by process date wise to retrieve max utilization count
				totalCountByProcessDate += newCDRCount;				
				
				//updating total cdr count by cdr date wise to retrieve current utilization count
				oldCDRCount  = cdrDateMap.get(cdrDate);				
				cdrDateMap.put(cdrDate, newCDRCount+oldCDRCount);
			}
			if(processDate!=null) {
				processDateMap.put(processDate, totalCountByProcessDate);
				MapCache.addConfigObject(BaseConstants.HOUR_WISE_TOTAL_TPS_MAP, processDateMap);
				MapCache.addConfigObject(BaseConstants.CURRENT_LICENSE_TPS,  Utilities.getTPSByHour(processDateMap.get(processDate)));
			}
			if(cdrDate!=null) {				
				MapCache.addConfigObject(BaseConstants.HOUR_WISE_CURRENT_TPS_MAP, cdrDateMap);
				MapCache.addConfigObject(BaseConstants.CURRENT_LICENSE_TPS,  Utilities.getTPSByHour(cdrDateMap.get(Utilities.getLastHourDate())));
			}
		}
	}
	
	/**
	 * Fetch DataSource Details  for synchronization
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getCircleList() {
		
		ResponseObject responseObject = new ResponseObject();
		
		List<Circle> circleList = circleDao.getAllCirclesList();
		
		if (circleList != null){
			responseObject.setSuccess(true);
			responseObject.setObject(circleList);
		} else {
			responseObject.setSuccess(false);
		}
		
		return responseObject;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getCircleDetailsById(int id) {
		
		ResponseObject responseObject = new ResponseObject();
		
		Circle circle= circleDao.findByPrimaryKey(Circle.class, id);
		
		if (circle != null){
			responseObject.setSuccess(true);
			responseObject.setObject(circle);
		} else {
			responseObject.setSuccess(false);
		}
		
		return responseObject;
	}

	@Override
	public ResponseObject getLicenseList() {
		
		ResponseObject responseObject = new ResponseObject();
		
		List<License> licenseList = licenseDao.getAllObject(License.class);
		
		if (licenseList != null){
			responseObject.setSuccess(true);
			responseObject.setObject(licenseList);
		} else {
			responseObject.setSuccess(false);
		}
		
		return responseObject;
	}
	
	/**
	 * Method will get license details by server id.
	 * 
	 * @param serverId
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject getLicenseByCircleId(int circleId) {
		ResponseObject responseObject = new ResponseObject();

		License license = licenseDao.getLicenseDetailsByCircleId(circleId);
		if (license != null) {
			logger.debug("License details fetch successfully.");
			responseObject.setObject(license);
			responseObject.setSuccess(true);
		} else {
			logger.debug("Failed to get license details for serverid " + circleId);
			responseObject.setObject(null);
			responseObject.setSuccess(false);
		}

		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getLicenseUtilization(License license) {
		ResponseObject responseObject = new ResponseObject();
		logger.info("Inside getLicenseUtilization method.");
		logger.info("Getting data for license type : "+license.getLicenceType());
		switch (license.getLicenceType()) {
			case CIRCLE:
				responseObject = getLicenseUtilizationByCircle(license);
				break;
			case DEVICE:
				responseObject = getLicenseUtilizationByDevice(license);
				break;
			default:
				break;
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getLicenseUtilizationByCircle(License license) {
		ResponseObject responseObject = new ResponseObject();
		List<LicenseUtilizationInfo> list = new ArrayList<>();		
		if(license!=null && license.getCircle()!=null) {
			int circleId = license.getCircle().getId();
			String repositoryPath = servletcontext.getRealPath(BaseConstants.LICENSE_PATH) + File.separator;			
			long appliedTps = 0;
			try {
				if(license.getTps()!=null)
					appliedTps = Long.valueOf(licenseUtility.decryptSymMsg(license.getTps(), repositoryPath));
			} catch (IOException e) {
				logger.error("Error to decrypt License TPS value", e);
			}
			Long currentCount = licenseDao.getCurrentLicenseUtilizationByCircle(license.getCircle().getId());			
			long currentTps = 0;
			if(currentCount!=null && currentCount>0) {				
				currentTps = currentCount/3600;				
			}
			LicenseUtilizationInfo licenseUtilizationInfo = new LicenseUtilizationInfo("NA", license.getCircle().getName(),
					license.getLicenceType().toString(), appliedTps, currentTps);
			long maxTps = licenseDao.getMaxLicenseUtilizationByCircle(circleId);					
			maxTps = (maxTps>0)? maxTps/3600 : 0;						
			licenseUtilizationInfo.setMaxTps(maxTps);
			if(appliedTps<maxTps)
				licenseUtilizationInfo.setLicenseExhausted(true);
			
			list.add(licenseUtilizationInfo);
			responseObject.setObject(list);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getLicenseUtilizationByDevice(License license) {
		ResponseObject responseObject = new ResponseObject();
		String repositoryPath = servletcontext.getRealPath(BaseConstants.LICENSE_PATH) + File.separator;
		List<LicenseUtilizationInfo> list = new ArrayList<>();
		if(license!=null && license.getTps()!=null) {
			Map<String, String> appliedMap = new HashMap<>();
			try {
				if(license.getTps()!=null)
					appliedMap = licenseUtility.extractLicenseDevicemap(licenseUtility.decryptSymMsg(license.getTps(), repositoryPath));
			} catch (IOException e) {
				logger.error("Error to decrypt License TPS value", e);
			}
			Map<String,Long> currentMap = licenseDao.getCurrentLicenseUtilizationByCircleAndDevice(license.getCircle().getId());
			Map<String,Long> maxMap = licenseDao.getMaxLicenseUtilizationByCircleAndDevice(license.getCircle().getId());
			LicenseUtilizationInfo licenseUtilizationInfo = null;
			for (String deviceName : appliedMap.keySet()) {
				Long currentCount = currentMap.get(deviceName);
				long currentTps = 0;
				if(currentMap.containsKey(deviceName) && currentCount!=null && currentCount > 0L ) {
					currentTps = currentCount/3600;					
				}
				long appliedTps = Long.parseLong(appliedMap.get(deviceName));
				licenseUtilizationInfo = new LicenseUtilizationInfo(deviceName, license.getCircle().getName(),
						license.getLicenceType().toString(), appliedTps, currentTps);
				
				if(maxMap!=null && !maxMap.isEmpty() && maxMap.containsKey(deviceName)) {
					long maxTps = (maxMap.get(deviceName)==0 || maxMap.get(deviceName) == null)? 0 : maxMap.get(deviceName)/3600;
					licenseUtilizationInfo.setMaxTps(maxTps);
				}
				
				if(appliedTps<licenseUtilizationInfo.getMaxTps())
					licenseUtilizationInfo.setLicenseExhausted(true);
				
				list.add(licenseUtilizationInfo);
			}
			responseObject.setObject(list);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
}