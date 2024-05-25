/**
 * 
 */
package com.elitecore.sm.migration.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.model.FTPDistributionDriver;
import com.elitecore.sm.drivers.model.LocalDistributionDriver;
import com.elitecore.sm.drivers.model.SFTPDistributionDriver;
import com.elitecore.sm.migration.model.FTPDistributionDriverEntity;
import com.elitecore.sm.migration.model.LocalDistributionDriverEntity;
import com.elitecore.sm.migration.model.SFTPDistributionDriverEntity;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.service.MigrationServiceImpl;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;


/**
 * @author Ranjitising Reval
 *
 */
@org.springframework.stereotype.Service("distributionDriverMigrationService")
public class DistributionDriverMigrationServiceImpl  implements DistributionDriverMigrationService {

	@Autowired
	private MigrationUtil migrationUtil;
	
	private static Logger logger = Logger.getLogger(MigrationServiceImpl.class);
	
	@Autowired
	DistributionDriverPahtListMigrationService distributionDriverPahtListMigrationService;
	/**
	 * Method will validate, un-marshaling and dozer conversion for drivers.
	 * @param finalDriverList
	 * @param distributionService
	 * @param folderDirPath
	 * @param staffId
	 * @param migrationPrefix
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject getDistributionDriverDetailsAndDependents(List<Drivers> finalDriverList, DistributionService distributionService, int staffId, String migrationPrefix) throws MigrationSMException {
		logger.debug("Reading distribution driver details for service " + distributionService.getName() + " id " + distributionService.getServInstanceId());
		
		ResponseObject responseObject = new ResponseObject();
		List<Drivers> driverList = distributionService.getMyDrivers();

		if (driverList != null && !driverList.isEmpty()) { 
			
			for (int i = 0; i < driverList.size(); i++) {
				
				Drivers drivers = driverList.get(i);
				DriverType driverType = (DriverType) MapCache.getConfigValueAsObject(drivers.getName());
				drivers.setDriverType(driverType);
				responseObject = checkDriverTypeAndSetName(drivers.getName());
				if (responseObject.isSuccess()) {

					responseObject = validateAndGetDriverObject(drivers, finalDriverList,distributionService, staffId, migrationPrefix);
					
					if(responseObject.isSuccess()){
						driverList.set(i, drivers);
					}else{
						break;
					}
				}else{
					break;
				} // for else part already setting failure message from checkdrivertypeAndsetname method.
			}
		} else {
			responseObject.setSuccess(true);
			logger.info("No driver configured for distribution service id  " + distributionService.getServInstanceId());
		}
		
		return responseObject;
	}

	
	/**
	 * Method will convert *-driver.xml to driver object details.
	 * @return ResponseObject
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject validateAndGetDriverObject(Drivers driver, List<Drivers> finalDriverList,DistributionService distributionService,int staffId, String migrationPrefix) throws MigrationSMException {
		ResponseObject responseObject = new ResponseObject();
		String driverType = driver.getName();
		Map<String,byte[]> fileContent = migrationUtil.getEntityFileContent();
		
		
		if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equalsIgnoreCase(driverType)) {
			String keyName = EngineConstants.DISTRIBUTION_SERVICE+"-"+ distributionService.getServInstanceId()+"-"+EngineConstants.FTP_DISTRIBUTION_DRIVER+"-"+driver.getApplicationOrder();
			
			if(fileContent.get(keyName) != null ){
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent.get(keyName), MigrationConstants.FTP_DISTRIBUTION_DRIVER_XSD, MigrationConstants.FTP_DISTRIBUTION_DRIVER_XML);
				if (responseObject.isSuccess()) {
					logger.info("FTPDistributionDriver JAXB class unmarshling done successfully");
					Map<String, Object> returnObjects = (Map<String, Object>) responseObject.getObject(); 
					Object jaxbObject  = returnObjects.get(MigrationConstants.MAP_JAXB_CLASS_KEY);
					FTPDistributionDriver fptDistributionDriver = (FTPDistributionDriver) returnObjects.get(MigrationConstants.MAP_SM_CLASS_KEY);
					FTPDistributionDriverEntity jaxbConvertedObj = (FTPDistributionDriverEntity) jaxbObject;
					if(fptDistributionDriver != null){
						
						fptDistributionDriver.setName(migrationUtil.getRandomName(migrationPrefix+ "_ftp_driver"));
						String encPassword = encryptPassword(fptDistributionDriver.getFtpConnectionParams().getPassword());
						setDriverDetails(fptDistributionDriver,driver, distributionService, staffId);
						fptDistributionDriver.getFtpConnectionParams().setPassword(encPassword);
						logger.info("Fetching all driver dependents for driver id :: " + fptDistributionDriver.getId());
					
						List<FTPDistributionDriverEntity.PathList.Path> jaxbPathList = jaxbConvertedObj.getPathList().getPath();
						responseObject = distributionDriverPahtListMigrationService.getDistributionDriverPathListDetails(jaxbPathList, fptDistributionDriver, staffId, migrationPrefix);
						if(responseObject.isSuccess()){
							List<PathList> smPathList = (List<PathList>) responseObject.getObject();
							fptDistributionDriver.setDriverPathList(smPathList);
							finalDriverList.add(fptDistributionDriver);
							responseObject.setSuccess(true);
						}
					}else{
						logger.debug("FTP Distribution driver found null.");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DRIVER_INSTANCE_NULL);
					}
				}
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_BYTE_DATA);
				responseObject.setArgs(new Object[] {keyName});
			}
			
			
			
			
		}else if (EngineConstants.SFTP_DISTRIBUTION_DRIVER.equalsIgnoreCase(driverType)) {
			
			String keyName = EngineConstants.DISTRIBUTION_SERVICE+"-"+ distributionService.getServInstanceId()+"-"+EngineConstants.SFTP_DISTRIBUTION_DRIVER+"-"+driver.getApplicationOrder();
			if(fileContent.get(keyName) != null ){
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent.get(keyName), MigrationConstants.SFTP_DISTRIBUTION_DRIVER_XSD, MigrationConstants.SFTP_DISTRIBUTION_DRIVER_XML);
				if (responseObject.isSuccess()) {
					
					logger.info("SFTPDistributionDriver JAXB class unmarshling done successfully");
					Map<String, Object> returnObjects = (Map<String, Object>) responseObject.getObject(); 
					Object jaxbObject  = returnObjects.get(MigrationConstants.MAP_JAXB_CLASS_KEY);
					SFTPDistributionDriver sfptDistributionDriver = (SFTPDistributionDriver) returnObjects.get(MigrationConstants.MAP_SM_CLASS_KEY);
					SFTPDistributionDriverEntity jaxbConvertedObj = (SFTPDistributionDriverEntity) jaxbObject;
					if(sfptDistributionDriver != null){
						
						sfptDistributionDriver.setName(migrationUtil.getRandomName(migrationPrefix+ "_sftp_driver"));
						String encPassword = encryptPassword(sfptDistributionDriver.getFtpConnectionParams().getPassword());
						sfptDistributionDriver.getFtpConnectionParams().setPassword(encPassword);

						setDriverDetails(sfptDistributionDriver,driver, distributionService, staffId);
						
						logger.info("Going to get all services object for server instance " + sfptDistributionDriver.getId());
						
						List<SFTPDistributionDriverEntity.PathList.Path> jaxbPathList = jaxbConvertedObj.getPathList().getPath();
						responseObject = distributionDriverPahtListMigrationService.getDistributionDriverPathListDetails(jaxbPathList, sfptDistributionDriver, staffId, migrationPrefix);
						
						if(responseObject.isSuccess()){
							List<PathList> smPathList = (List<PathList>) responseObject.getObject();
							sfptDistributionDriver.setDriverPathList(smPathList);
							finalDriverList.add(sfptDistributionDriver);
							responseObject.setSuccess(true);
						}
						
					}else{
						logger.debug("SFTP Distribution driver found null.");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DRIVER_INSTANCE_NULL);
						
					}
				}
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_BYTE_DATA);
				responseObject.setArgs(new Object[] {keyName});
			}
			
		}else if (EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equalsIgnoreCase(driverType)) {
			String keyName = EngineConstants.DISTRIBUTION_SERVICE+"-"+ distributionService.getServInstanceId()+"-"+EngineConstants.LOCAL_DISTRIBUTION_DRIVER+"-"+driver.getApplicationOrder();
			

			if(fileContent.get(keyName) != null ){
				
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent.get(keyName), MigrationConstants.LOCAL_DISTRIBUTION_DRIVER_XSD, MigrationConstants.LOCAL_DISTRIBUTION_DRIVER_XML);
				if (responseObject.isSuccess()) {
					
					logger.info("LocalDistributionDriver JAXB class unmarshling done successfully");
					Map<String, Object> returnObjects = (Map<String, Object>) responseObject.getObject(); 
					Object jaxbObject  = returnObjects.get(MigrationConstants.MAP_JAXB_CLASS_KEY);
					
					LocalDistributionDriver localDistributionDriver = (LocalDistributionDriver) returnObjects.get(MigrationConstants.MAP_SM_CLASS_KEY);
					LocalDistributionDriverEntity localDistributionJaxbObj = (LocalDistributionDriverEntity) jaxbObject;
					
					if(localDistributionDriver != null){
						localDistributionDriver.setName(migrationUtil.getRandomName(migrationPrefix+ "_local_driver"));
						logger.info("Going to get all services object for server instance " + localDistributionDriver.getId());
						setDriverDetails(localDistributionDriver, driver,distributionService, staffId);
						List<LocalDistributionDriverEntity.PathList.Path> jaxbPathList = localDistributionJaxbObj.getPathList().getPath();
						
						responseObject = distributionDriverPahtListMigrationService.getDistributionDriverPathListDetails(jaxbPathList, localDistributionDriver, staffId, migrationPrefix);
						if(responseObject.isSuccess()){
							List<PathList> smPathList = (List<PathList>) responseObject.getObject();
							localDistributionDriver.setDriverPathList(smPathList);
							finalDriverList.add(localDistributionDriver);
							responseObject.setSuccess(true);
						}
						
					}else{
						logger.debug("Local Distribution driver found null.");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DRIVER_INSTANCE_NULL);
					}
				}
				
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_BYTE_DATA);
				responseObject.setArgs(new Object[] {keyName});
			}
			
		} else {
			logger.info("Failed to migration distribution service as driver type found invalid!");
			responseObject.setSuccess(false);
			responseObject.setObject(responseObject.getObject());
		}
		return responseObject;
	} 
	
	
	
	private void setDriverDetails(Drivers smNewDriver,Drivers serviceDriverObj, DistributionService distributionService, int staffId){
		smNewDriver.setId(0);
		smNewDriver.setApplicationOrder(serviceDriverObj.getApplicationOrder());
		smNewDriver.setStatus(StateEnum.ACTIVE);
		smNewDriver.setDriverType(serviceDriverObj.getDriverType());
		smNewDriver.setService(distributionService);
		
		migrationUtil.setCurrentDateAndStaffId(smNewDriver,staffId);
	}
	/**
	 * Method will encrypt driver password. 
	 * @param password
	 * @return
	 */
	private String encryptPassword(String password) {
		char[] chars = password.toCharArray();
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		logger.debug(hex.toString());
		return hex.toString();

	}

	
	/**
	 * Method will check driver type and set driver file name accordingly.
	 * @param driverType
	 * @return
	 */
	private ResponseObject checkDriverTypeAndSetName(String driverType) {
		ResponseObject responseObject = new ResponseObject();
		String driverFileName;
		logger.info("Found driver type " + driverType);
		if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equalsIgnoreCase(driverType)) {
			driverFileName = MigrationConstants.FTP_DISTRIBUTION_DRIVER_XML; 
			responseObject.setObject(driverFileName);
			responseObject.setSuccess(true);
		} else if (EngineConstants.SFTP_DISTRIBUTION_DRIVER.equalsIgnoreCase(driverType)) {
			driverFileName = MigrationConstants.SFTP_DISTRIBUTION_DRIVER_XML;
			responseObject.setObject(driverFileName);
			responseObject.setSuccess(true);
		} else if (EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equalsIgnoreCase(driverType)) {
			driverFileName = MigrationConstants.LOCAL_DISTRIBUTION_DRIVER_XML;
			responseObject.setObject(driverFileName);
			responseObject.setSuccess(true);
		} else {
			logger.info("Failed to migration distribution service as driver type found invalid!");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}
}
