package com.elitecore.sm.migration.service;

import static com.elitecore.sm.common.constants.MigrationConstants.COLLECTION_SERVICE_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.FTP_COLLECTION_DRIVER;
import static com.elitecore.sm.common.constants.MigrationConstants.FTP_COLLECTION_DRIVER_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.LOCAL_COLLECTION_DRIVER;
import static com.elitecore.sm.common.constants.MigrationConstants.LOCAL_COLLECTION_DRIVER_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.MAP_JAXB_CLASS_KEY;
import static com.elitecore.sm.common.constants.MigrationConstants.MAP_SM_CLASS_KEY;
import static com.elitecore.sm.common.constants.MigrationConstants.SFTP_COLLECTION_DRIVER;
import static com.elitecore.sm.common.constants.MigrationConstants.SFTP_COLLECTION_DRIVER_XML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.migration.model.FTPCollectionDriverEntity;
import com.elitecore.sm.migration.model.LocalCollectionDriverEntity;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.migration.model.SFTPCollectionDriverEntity;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;

@org.springframework.stereotype.Service(value = "collectionServiceMigration")
public class CollectionServiceMigrationImpl implements CollectionServiceMigration {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private MigrationUtil migrationUtil;

	private String migrationPrefix;

	private int position;

	private ServerInstance serverInstance;

	private Service service;

	@Override
	public ResponseObject iterateAndSetCollectionServiceDetails(List<Service> serviceList, int position, Service service, String directoryPath, ServerInstance serverInstance, String migrationPrefix) throws MigrationSMException {
		this.position = position;
		this.migrationPrefix = migrationPrefix;
		this.serverInstance = serverInstance;
		this.service = service;
		logger.info("CollectionServiceMigrationImpl.iterateAndSetCollectionServiceDetails() *** ");
		return executeCollectionServiceInstance(serviceList);
	}

	/**
	 * MEthod will iterate and get collection service detail object.
	 * @param directoryPath
	 * @param serviceList
	 * @return
	 * @throws MigrationSMException
	 */
	private ResponseObject executeCollectionServiceInstance(List<Service> serviceList) throws MigrationSMException {
		logger.info("CollectionServiceMigrationImpl.executeCollectionServiceInstance() ***");
		ResponseObject responseObject = executeCollectionService();
		if (responseObject.isSuccess() && responseObject.getObject() instanceof CollectionService) {
			CollectionService collectionService = (CollectionService) responseObject.getObject();
			responseObject = loadDriversInCollectionService(collectionService);
			if (responseObject.isSuccess() && responseObject.getObject() instanceof CollectionService) {
				collectionService = (CollectionService) responseObject.getObject();
				collectionService.setName(migrationUtil.getRandomName(this.service.getName()));
				collectionService.setServerInstance(this.serverInstance);
				migrationUtil.setCurrentDateAndStaffId(collectionService, 1);
				serviceList.set(this.position, collectionService);
				responseObject.setObject(collectionService);
				
				displayCollectionService(collectionService);
			}

		}

		return responseObject;
	}

	/**
	 * Method will convert and get sm object with its xsd validation.
	 * @param directoryPath
	 * @return
	 * @throws MigrationSMException
	 */
	@SuppressWarnings("unchecked")
	private ResponseObject executeCollectionService() throws MigrationSMException {
		logger.info("CollectionServiceMigrationImpl.executeCollectionService() : "
				+ COLLECTION_SERVICE_XML);
		CollectionService collectionService;
		String serviceKey = EngineConstants.COLLECTION_SERVICE + "-" + this.service.getServInstanceId();
		 ResponseObject responseObject = migrationUtil.getFileContentFromMap(serviceKey);
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();
			responseObject = migrationUtil.getAndValidateMappingEntityObj(COLLECTION_SERVICE_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if (responseObject.isSuccess()) {
					Map<String, Object> mapOfObjects = (Map<String, Object>) responseObject.getObject();
					Object smObject = mapOfObjects.get(MAP_SM_CLASS_KEY);
					if (smObject instanceof CollectionService) {
						collectionService = (CollectionService) smObject;
						collectionService.setServInstanceId(this.service.getServInstanceId());
						ServiceType serviceType = (ServiceType) MapCache.getConfigValueAsObject(this.service.getSvctype().getAlias());
						collectionService.setSvctype(serviceType);
						responseObject.setObject(collectionService);
						responseObject.setSuccess(true);
					}
				}
			}
		} 
		return responseObject;
	}

	private ResponseObject loadDriversInCollectionService(CollectionService collectionService) throws MigrationSMException {
		logger.info("CollectionServiceMigrationImpl.executeCollectionDriverDirectory() *** ");
		ResponseObject responseObject = null;
		List<Drivers> drivers = collectionService.getMyDrivers();
		int driversLength = drivers.size();
		for (int i = driversLength - 1; i >= 0; i--) {
			if (drivers.get(i).getName().equalsIgnoreCase(LOCAL_COLLECTION_DRIVER)) {
				
				responseObject = getCollectionDriver(collectionService, drivers.get(i), LOCAL_COLLECTION_DRIVER_XML, EngineConstants.LOCAL_COLLECTION_DRIVER);
				if(responseObject.isSuccess()) {
					Drivers smDriver = (Drivers) responseObject.getObject();
					drivers.set(i, smDriver);
				} else {
					return responseObject;
				}
				
			} else if (drivers.get(i).getName()	.equalsIgnoreCase(FTP_COLLECTION_DRIVER)) {
				responseObject = getCollectionDriver(collectionService, drivers.get(i), FTP_COLLECTION_DRIVER_XML, EngineConstants.FTP_COLLECTION_DRIVER);
				if(responseObject.isSuccess()) {
					Drivers smDriver = (Drivers) responseObject.getObject();
					drivers.set(i, smDriver);
				} else {
					return responseObject;
				}
			} else if (drivers.get(i).getName().equalsIgnoreCase(SFTP_COLLECTION_DRIVER)) {
				responseObject = getCollectionDriver(collectionService, drivers.get(i), SFTP_COLLECTION_DRIVER_XML, EngineConstants.SFTP_COLLECTION_DRIVER);
				if(responseObject.isSuccess()) {
					Drivers smDriver = (Drivers) responseObject.getObject();
					drivers.set(i, smDriver);
				} else {
					return responseObject;
				}

			}

		}
		if(responseObject == null){
			responseObject = new ResponseObject();
		}
		responseObject.setSuccess(true);
		responseObject.setObject(collectionService);
		
		return responseObject;
	}

	/**
	 * @param directoryPath
	 * @param service
	 * @param driver
	 * @param driverXml
	 * @param migDriverName
	 * @return
	 * @throws MigrationSMException
	 */
	private ResponseObject getCollectionDriver(Service service,Drivers driver, String driverXml, String migDriverName) throws MigrationSMException {
		ResponseObject responseObject = executeCollectionDriver(driverXml, driver, service);
		if (responseObject.isSuccess()) {
			Drivers collectionDriverObject = (Drivers) responseObject.getObject();
			collectionDriverObject = migrationUtil.getUpdatedDriverObject(collectionDriverObject, driver.getApplicationOrder(), this.migrationPrefix
					+ "_" + migDriverName, service);
			responseObject.setObject(collectionDriverObject);
		}
		return responseObject;
		
	}

	/**
	 * @param directoryPath
	 * @param name
	 * @param xmlName
	 * @param driver
	 * @param service
	 * @return
	 * @throws MigrationSMException
	 */
	@SuppressWarnings("unchecked")
	private ResponseObject executeCollectionDriver(String xmlName, Drivers driver, Service service) throws MigrationSMException {
		logger.info("CollectionServiceMigrationImpl.executeCollectionDriver() ***  collection driver execution starts ***");
		String keyName = EngineConstants.COLLECTION_SERVICE+"-"+ service.getServInstanceId()+"-" + driver.getName()+ "-"+driver.getApplicationOrder();
		
		ResponseObject responseObject = migrationUtil.getFileContentFromMap(keyName);
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();
			responseObject = migrationUtil.getAndValidateMappingEntityObj(xmlName);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping  entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent,entityMapping.getXsdName(), xmlName);
				if (responseObject.isSuccess()) {
					Map<String, Object> objectMap = (Map<String, Object>) responseObject.getObject();
					Object smObject = objectMap.get(MAP_SM_CLASS_KEY);
					Object jaxbObject = objectMap.get(MAP_JAXB_CLASS_KEY);
					Drivers drivers = loadCollectionDriverPathList(jaxbObject, smObject);
					responseObject.setObject(drivers);
				}
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_ENTITY_MAPPING_DETAILS);
				responseObject.setArgs(new Object[] {xmlName});
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_BYTE_DATA);
			responseObject.setArgs(new Object[] {keyName});
		}

		return responseObject;
	}

	/**
	 * Method will load collection driver path-list based on driver type.
	 * @param jaxbCollectionDriverObject
	 * @param smCollectionDriverObject
	 * @return
	 * @throws MigrationSMException
	 */
	public Drivers loadCollectionDriverPathList(Object jaxbCollectionDriverObject, Object smCollectionDriverObject) throws MigrationSMException {
		logger.info("CollectionServiceMigrationImpl.loadCollectionDriverPathList() ***");
		if (jaxbCollectionDriverObject instanceof LocalCollectionDriverEntity) {
			LocalCollectionDriverEntity jaxbLocalCollectionDriver = (LocalCollectionDriverEntity) jaxbCollectionDriverObject;
			return getUpdatedDriverWithPathList(jaxbLocalCollectionDriver.getPathList().getPath(), smCollectionDriverObject, LOCAL_COLLECTION_DRIVER);
		} else if (jaxbCollectionDriverObject instanceof FTPCollectionDriverEntity) {
			FTPCollectionDriverEntity jaxbFtpCollectionDriver = (FTPCollectionDriverEntity) jaxbCollectionDriverObject;
			return getUpdatedDriverWithPathList(jaxbFtpCollectionDriver.getPathList().getPath(), smCollectionDriverObject, FTP_COLLECTION_DRIVER);
		} else if (jaxbCollectionDriverObject instanceof SFTPCollectionDriverEntity) {
			SFTPCollectionDriverEntity jaxbSftpCollectionDriver = (SFTPCollectionDriverEntity) jaxbCollectionDriverObject;
			return getUpdatedDriverWithPathList(jaxbSftpCollectionDriver.getPathList().getPath(), smCollectionDriverObject, SFTP_COLLECTION_DRIVER);
		}

		return null;
	}

	/**
	 * Method will get updated driver with pathlist object.
	 * @param jaxbPathList
	 * @param smDriverObject
	 * @param driverType
	 * @return
	 * @throws MigrationSMException
	 */
	private Drivers getUpdatedDriverWithPathList(List<?> jaxbPathList,Object smDriverObject, String driverType) throws MigrationSMException {
		
		logger.info("CollectionServiceMigrationImpl.getUpdatedDriverWithPathList()");
		List<PathList> driverPathList = getCollectionDriverPathListFromJaxbPathList(jaxbPathList);
		if (smDriverObject instanceof Drivers) {
			Drivers smDriver = (Drivers) smDriverObject;
			smDriver.setDriverPathList(driverPathList);
			DriverType driverTypeObj = (DriverType) MapCache.getConfigValueAsObject(driverType);
			smDriver.setDriverType(driverTypeObj);
			return smDriver;
		}
		return null;

	}

	/**
	 * Method will get driver path list detail from jaxb oject.
	 * @param jaxbPathList
	 * @return
	 * @throws MigrationSMException
	 */
	public List<PathList> getCollectionDriverPathListFromJaxbPathList(List<?> jaxbPathList) throws MigrationSMException {
		logger.info("CollectionServiceMigrationImpl.getCollectionDriverPathListFromJaxbPathList()");
		Class<?> destinationClass = migrationUtil.getFullClassName("com.elitecore.sm.pathlist.model.CollectionDriverPathList");
		List<PathList> collectionDriverPathList = new ArrayList<>();
		for (Object jaxbPath : jaxbPathList) {
			ResponseObject responseObject = migrationUtil.convertJaxbToSMObject(jaxbPath, destinationClass);
			if (responseObject.isSuccess() && responseObject.getObject() instanceof CollectionDriverPathList) {
				CollectionDriverPathList smCollectionDriverPathList = (CollectionDriverPathList) responseObject.getObject();
				smCollectionDriverPathList.setName(migrationUtil.getRandomName(this.migrationPrefix+ "_COLLECTION_PATHLIST"));
				collectionDriverPathList.add(smCollectionDriverPathList);
			}
		}
		return collectionDriverPathList;
	}

	private void displayCollectionService(CollectionService cs) {
		logger.info("===================================================================================");
		logger.info("startup mode ENUM : "
				+ cs.getSvcExecParams().getStartupMode());
		logger.info("sorting type : " + cs.getSvcExecParams().getSortingType());
		logger.info("Service Execution Params.sorting creteria : "
				+ cs.getSvcExecParams().getSortingCriteria());
		logger.info("Service Execution Params.is execution on startup : "
				+ cs.getSvcExecParams().isExecuteOnStartup());
		logger.info("Service Execution Params.queue size : "
				+ cs.getSvcExecParams().getQueueSize());
		logger.info("Service Execution Params.min thread : "
				+ cs.getSvcExecParams().getMinThread());
		logger.info("Service Execution Params.max thread : "
				+ cs.getSvcExecParams().getMaxThread());
		logger.info("Service Execution Params.file batch size : "
				+ cs.getSvcExecParams().getFileBatchSize());
		logger.info("Service Execution Params.min disk space : "
				+ cs.getServerInstance().getMinDiskSpace());
		logger.info("enable db status : " + cs.isEnableDBStats());
		logger.info("Total driver associated with service : "
				+ cs.getMyDrivers().size());
		for (Drivers d : cs.getMyDrivers()) {
			logger.info("Driver................................................");
			logger.info("application order : " + d.getApplicationOrder());
			logger.info("name : " + d.getName());
			if (d.getDriverPathList() != null) {
				logger.info("driver path list : "
						+ d.getDriverPathList().size());
			}
			for (PathList pathList : d.getDriverPathList()) {
				if (pathList instanceof CollectionDriverPathList) {
					logger.info("pathlist...........................................");
					CollectionDriverPathList l = (CollectionDriverPathList) pathList;
					displayCollectionDriverPathList(l);
				}
			}
		}

		logger.info("status of collection service : " + cs.getStatus());
		logger.info("service scheduling params.is scheduling enabled : "
				+ cs.getServiceSchedulingParams().isSchedulingEnabled());
		logger.info("service scheduling params.is scheduling enabled : "
				+ cs.getServiceSchedulingParams().getSchType());
		logger.info("service scheduling params.date : "
				+ cs.getServiceSchedulingParams().getDate());
		logger.info("service scheduling params.day : "
				+ cs.getServiceSchedulingParams().getDay());
		logger.info("service scheduling params.time : "
				+ cs.getServiceSchedulingParams().getTime());
		logger.info("service instance id PARENT FOLDER NAME: "
				+ cs.getServInstanceId());
		logger.info("===================================================================================");
	}

	public void displayCollectionDriverPathList(CollectionDriverPathList l) {
		logger.info("readFilePath : " + l.getReadFilePath());
		logger.info("readFilenamePrefix : " + l.getReadFilenamePrefix());
		logger.info("readFilenameSuffix : " + l.getReadFilenameSuffix());
		logger.info("maxFilesCountAlert : " + l.getMaxFilesCountAlert());
		logger.info("readFilenameContains : " + l.getReadFilenameContains());
		logger.info("writeFilePath : " + l.getWriteFilePath());
		logger.info("writeFilenamePrefix : " + l.getWriteFilenamePrefix());
		logger.info("remoteFileAction : " + l.getRemoteFileAction());
		logger.info("remoteFileActionParamName : "
				+ l.getRemoteFileActionParamName());
		logger.info("remoteFileActionValue : " + l.getRemoteFileActionValue());
		logger.info("dateFormat : " + l.getDateFormat());
		logger.info("position : " + l.getPosition());
		logger.info("startIndex : " + l.getStartIndex());
		logger.info("endIndex : " + l.getEndIndex());
		logger.info("fileGrepDateEnabled : " + l.getFileGrepDateEnabled());
		logger.info("seqStartIndex : " + l.getSeqStartIndex());
		logger.info("seqEndIndex : " + l.getSeqEndIndex());
		//logger.info("maxCounterLimit : " + l.getMaxCounterLimit());
		logger.info("fileSeqAlertEnabled : " + l.getFileSeqAlertEnabled());
	}
}
