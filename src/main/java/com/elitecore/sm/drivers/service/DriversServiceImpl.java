package com.elitecore.sm.drivers.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.SequenceManagement;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;	
import com.elitecore.sm.composer.model.EnumComposerAttributeHeader;	
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerAttribute;
import com.elitecore.sm.composer.service.CharRenameOperationService;
import com.elitecore.sm.composer.service.ComposerService;
import com.elitecore.sm.drivers.dao.ConnectionParameterDao;
import com.elitecore.sm.drivers.dao.DriverAttributeDao;
import com.elitecore.sm.drivers.dao.DriverTypeDao;
import com.elitecore.sm.drivers.dao.DriversDao;
import com.elitecore.sm.drivers.dao.SequenceMgmtDao;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.model.ConnectionParameter;
import com.elitecore.sm.drivers.model.ControlFileParams;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriver;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriverAttribute;
import com.elitecore.sm.drivers.model.DistributionDriver;
import com.elitecore.sm.drivers.model.DriverCategory;
import com.elitecore.sm.drivers.model.DriverDataTypeEnum;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.model.EnumDbDriverAttributeHeader;
import com.elitecore.sm.drivers.model.FTPCollectionDriver;
import com.elitecore.sm.drivers.model.FTPDistributionDriver;
import com.elitecore.sm.drivers.model.HadoopDistributionDriver;
import com.elitecore.sm.drivers.model.HostParameters;
import com.elitecore.sm.drivers.model.LocalCollectionDriver;
import com.elitecore.sm.drivers.model.LocalDistributionDriver;
import com.elitecore.sm.drivers.model.SFTPCollectionDriver;
import com.elitecore.sm.drivers.model.SFTPDistributionDriver;
import com.elitecore.sm.drivers.validator.DriverValidator;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.netflowclient.dao.NetflowClientDao;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.pathlist.dao.FileGroupingParameterDao;
import com.elitecore.sm.pathlist.model.FileFetchParams;
import com.elitecore.sm.pathlist.model.FileGroupingParameter;
import com.elitecore.sm.pathlist.model.FileGroupingParameterCollection;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.CSVUtils;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.Regex;

/**
 * 
 * @author avani.panchal
 *
 */
@org.springframework.stereotype.Service(value = "driversService")
public class DriversServiceImpl implements DriversService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	DriversDao driversDao;

	@Autowired
	ServicesService servicesService;

	@Autowired
	private PathListService pathListService;

	@Autowired
	private FileGroupingParameterDao fileGroupingParamDao;

	@Autowired
	private ConnectionParameterDao connectionParamDao;

	@Autowired
	private DriverTypeDao driverTypeDao;

	@Autowired
	private ServicesDao servicesDao;

	@Autowired
	private NetflowClientDao clientDao;

	@Autowired
	private DriverValidator driverValidator;

	@Autowired
	ComposerService composerService;

	@Autowired
	CharRenameOperationService charRenameOperationService;

	@Autowired
	private DriverAttributeDao driverAttributeDao;

	@Autowired
	SequenceMgmtDao ControlFileDao;
	/**
	 * Add driver into database
	 */
	@Override
	@Transactional
	public ResponseObject addDriver(Drivers driver) {
		ResponseObject responseObject = new ResponseObject();

		driver.setCreatedDate(new Date());
		driver.setLastUpdatedDate(driver.getCreatedDate());
		driver.setLastUpdatedByStaffId(driver.getCreatedByStaffId());

		driversDao.save(driver);

		if (driver.getId() != 0) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DRIVER_ADD_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DRIVER_ADD_FAIL);
		}

		return responseObject;
	}

	/**
	 * Update collection driver
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_DRIVER, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Drivers.class,
			ignorePropList = "")
	public ResponseObject updateCollectionDriver(CollectionDriver collectionDriver) {
		ResponseObject responseObject = new ResponseObject();

		if (isDriverUniqueForUpdate(getServiceId(collectionDriver.getService()), collectionDriver.getName())) {

			String collectionDriverType = collectionDriver.getDriverType().getAlias();
			if (EngineConstants.FTP_COLLECTION_DRIVER.equals(collectionDriverType)
					|| EngineConstants.SFTP_COLLECTION_DRIVER.equals(collectionDriverType)) {

				logger.debug("inside updateCollectionDriver : FTP or SFTP Collection Driver Instance found");
				CollectionDriver ftpCollectionDriver = driversDao.getFTPCollectionDriverById(collectionDriver.getId());
				ftpCollectionDriver.setName(collectionDriver.getName());
				ftpCollectionDriver.setTimeout(collectionDriver.getTimeout());
				ftpCollectionDriver.setStatus(collectionDriver.getStatus());
				ftpCollectionDriver.setLastUpdatedByStaffId(collectionDriver.getLastUpdatedByStaffId());
				ftpCollectionDriver.setLastUpdatedDate(new Date());
				
				if(ftpCollectionDriver instanceof SFTPCollectionDriver && 
						EngineConstants.FTP_COLLECTION_DRIVER.equals(collectionDriverType)){
					DriverType dtype = driverTypeDao.getDriverTypeByAlias(EngineConstants.FTP_COLLECTION_DRIVER);
					ftpCollectionDriver.setDriverType(dtype);
					driversDao.updateDriverType(ftpCollectionDriver.getId(), BaseConstants.DRIVER_TYPE_FTP_COLLECTION_DRIVER);
					
				}else if(ftpCollectionDriver instanceof FTPCollectionDriver && 
						EngineConstants.SFTP_COLLECTION_DRIVER.equals(collectionDriverType)){
					DriverType dtype = driverTypeDao.getDriverTypeByAlias(EngineConstants.SFTP_COLLECTION_DRIVER);
					ftpCollectionDriver.setDriverType(dtype);
					driversDao.updateDriverType(ftpCollectionDriver.getId(), BaseConstants.DRIVER_TYPE_SFTP_COLLECTION_DRIVER);
				}
				driversDao.merge(ftpCollectionDriver);

				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.DRIVER_UPDATE_SUCCESS);
				responseObject.setObject(ftpCollectionDriver);

			} else if (EngineConstants.LOCAL_COLLECTION_DRIVER.equals(collectionDriver.getDriverType().getAlias())) {
				logger.debug("inside updateCollectionDriver : Local Collection Driver Instance found");
				LocalCollectionDriver localCollectionDriver = driversDao.getLocalCollectionDriverById(collectionDriver.getId());
				localCollectionDriver.setName(collectionDriver.getName());
				localCollectionDriver.setTimeout(collectionDriver.getTimeout());
				localCollectionDriver.setStatus(collectionDriver.getStatus());
				localCollectionDriver.setLastUpdatedByStaffId(collectionDriver.getLastUpdatedByStaffId());
				localCollectionDriver.setLastUpdatedDate(new Date());

				driversDao.merge(localCollectionDriver);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.DRIVER_UPDATE_SUCCESS);
				responseObject.setObject(localCollectionDriver);
			}
		} else {
			logger.debug("inside updateCollectionDriver : duplicate driver name found in update:" + collectionDriver.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DRIVER_NAME);
		}
		return responseObject;
	}

	/**
	 * Import driver into DB
	 */
	@Transactional
	@Override
	public ResponseObject importOrDeleteDriverConfig(Drivers driver, boolean isImport) {

		ResponseObject responseObject = new ResponseObject();

		if (driver != null && driver instanceof CollectionDriver) {
			logger.debug("*******************************************************");
			logger.debug("Driver name is in Driver Service " + driver.getName());
			logger.debug("*******************************************************");
			responseObject = iterateCollectionDriverDependants(driver, isImport);

		} else if (driver != null && driver instanceof DistributionDriver) {

			logger.debug("Found distribution driver object now going to add or delete its dependents");
			responseObject = importOrDeleteFTPDistributionDriverDependents(driver, isImport);
		}

		if (responseObject.isSuccess() && driver != null) {

			if (isImport) {
				logger.debug("Create Driver");

				int totalDriverCount = driversDao.getDriverCountByServiceId(driver.getService().getId());
				logger.debug("inside importOrDeleteDriverConfig : Total Driver Count is ::" + totalDriverCount);
				logger.debug("inside importOrDeleteDriverConfig : Next Application Order is ::" + totalDriverCount);
				driver.setApplicationOrder(totalDriverCount);
				driversDao.save(driver);

				if (driver.getId() != 0) {
					responseObject.setSuccess(true);
				} else {
					responseObject.setSuccess(false);
				}
			} else {
				logger.debug("Delete Driver");
				driver.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, driver.getName()));
				driver.setStatus(StateEnum.DELETED);
				driver.setLastUpdatedDate(new Date());
				driversDao.merge(driver);
				responseObject.setSuccess(true);
			}
			if (responseObject.isSuccess()) {
				responseObject = pathListService.iterateDriverPathListDetails(driver, isImport);

			} else {
				logger.debug("Fail to create driver");
			}

		} else {
			logger.debug("Fail to create driver depedants");
		}

		return responseObject;
	}

	/**
	 * import collection driver dependants
	 * 
	 * @param driver
	 * @return ResponseObject
	 */
	@Transactional
	public ResponseObject iterateCollectionDriverDependants(Drivers driver, boolean isImport) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Create or delete Collection driver dependants");
		FileGroupingParameterCollection fileGroup = ((CollectionDriver) driver).getFileGroupingParameter();

		if (isImport) {

			if (fileGroup != null) {
				fileGroup.setId(0);
				fileGroup.setCreatedByStaffId(driver.getCreatedByStaffId());
				fileGroup.setCreatedDate(new Date());
				fileGroup.setLastUpdatedByStaffId(driver.getCreatedByStaffId());
				fileGroup.setLastUpdatedDate(new Date());
				((CollectionDriver) driver).setFileGroupingParameter(fileGroup);
			} else {
				((CollectionDriver) driver).setFileGroupingParameter(new FileGroupingParameterCollection());
			}

		} else {
			fileGroup.setStatus(StateEnum.DELETED);
			fileGroup.setLastUpdatedByStaffId(driver.getCreatedByStaffId());
			fileGroup.setLastUpdatedDate(new Date());
		}

		if (driver instanceof SFTPCollectionDriver) {

			FileFetchParams fileFetchParam = ((SFTPCollectionDriver) driver).getMyFileFetchParams();
			if (fileFetchParam != null) {
				((SFTPCollectionDriver) driver).setMyFileFetchParams(fileFetchParam);
			} else {
				((SFTPCollectionDriver) driver).setMyFileFetchParams(new FileFetchParams());
			}

			responseObject = iterateDriversConnectionParams(((SFTPCollectionDriver) driver).getFtpConnectionParams(), isImport);
			((FTPCollectionDriver) driver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());
			driver.setDriverType(driverTypeDao.getDriverTypeByAlias(EngineConstants.SFTP_COLLECTION_DRIVER));

		} else if (driver instanceof FTPCollectionDriver) {

			FileFetchParams fileFetchParam = ((FTPCollectionDriver) driver).getMyFileFetchParams();
			if (fileFetchParam != null) {
				((FTPCollectionDriver) driver).setMyFileFetchParams(fileFetchParam);
			} else {
				((FTPCollectionDriver) driver).setMyFileFetchParams(new FileFetchParams());
			}

			responseObject = iterateDriversConnectionParams(((FTPCollectionDriver) driver).getFtpConnectionParams(), isImport);
			((FTPCollectionDriver) driver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());

			driver.setDriverType(driverTypeDao.getDriverTypeByAlias(EngineConstants.FTP_COLLECTION_DRIVER));

		} else if (driver instanceof LocalCollectionDriver) {
			driver.setDriverType(driverTypeDao.getDriverTypeByAlias(EngineConstants.LOCAL_COLLECTION_DRIVER));
		}

		responseObject.setSuccess(true);
		responseObject.setObject(driver);

		return responseObject;
	}
	
	@Transactional
	public ResponseObject importCollectionDriverAddMode(Drivers exportedDriver) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Create Collection driver dependants");
		FileGroupingParameterCollection fileGroup = ((CollectionDriver) exportedDriver).getFileGroupingParameter();
		
		if (fileGroup != null) {
			servicesService.importFileGroupingParameterAddMode(fileGroup, exportedDriver.getCreatedByStaffId());
			((CollectionDriver) exportedDriver).setFileGroupingParameter(fileGroup);
		} else {
			((CollectionDriver) exportedDriver).setFileGroupingParameter(new FileGroupingParameterCollection());
		}
		
		if (exportedDriver instanceof SFTPCollectionDriver) {
			FileFetchParams fileFetchParam = ((SFTPCollectionDriver) exportedDriver).getMyFileFetchParams();
			if (fileFetchParam != null) {
				((SFTPCollectionDriver) exportedDriver).setMyFileFetchParams(fileFetchParam);
			} else {
				((SFTPCollectionDriver) exportedDriver).setMyFileFetchParams(new FileFetchParams());
			}

			responseObject = importDriverConnectionParamAddMode(((SFTPCollectionDriver) exportedDriver).getFtpConnectionParams());
			((FTPCollectionDriver) exportedDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());
			exportedDriver.setDriverType(driverTypeDao.getDriverTypeByAlias(EngineConstants.SFTP_COLLECTION_DRIVER));
			
		} else if (exportedDriver instanceof FTPCollectionDriver) {
			FileFetchParams fileFetchParam = ((FTPCollectionDriver) exportedDriver).getMyFileFetchParams();
			if (fileFetchParam != null) {
				((FTPCollectionDriver) exportedDriver).setMyFileFetchParams(fileFetchParam);
			} else {
				((FTPCollectionDriver) exportedDriver).setMyFileFetchParams(new FileFetchParams());
			}

			responseObject = importDriverConnectionParamAddMode(((FTPCollectionDriver) exportedDriver).getFtpConnectionParams());
			((FTPCollectionDriver) exportedDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());

			exportedDriver.setDriverType(driverTypeDao.getDriverTypeByAlias(EngineConstants.FTP_COLLECTION_DRIVER));
		} else if (exportedDriver instanceof LocalCollectionDriver) {
			exportedDriver.setDriverType(driverTypeDao.getDriverTypeByAlias(EngineConstants.LOCAL_COLLECTION_DRIVER));
		}
		responseObject.setSuccess(true);
		responseObject.setObject(exportedDriver);

		return responseObject;
	}
	
	@Transactional
	public void importDistributionDriverAddMode(Drivers exportedDriver) {
		logger.debug("Import Distribution driver dependants");
		
		if(exportedDriver instanceof LocalDistributionDriver) {
			iterateDriversControlFileParams(((LocalDistributionDriver) exportedDriver).getControlFileSeq(),true);
		} else if(exportedDriver instanceof FTPDistributionDriver) {
			iterateDriversConnectionParams(((FTPDistributionDriver) exportedDriver).getFtpConnectionParams(), true);
			iterateDriversControlFileParams(((FTPDistributionDriver) exportedDriver).getControlFileSeq(),true);
		} else if(exportedDriver instanceof DatabaseDistributionDriver) {
			importOrDeleteDatabaseDisDriverDependents(exportedDriver, true);
		} else if(exportedDriver instanceof HadoopDistributionDriver) {
			//no parameter to set
		} else if(exportedDriver instanceof SFTPDistributionDriver) {
			iterateDriversConnectionParams(((SFTPDistributionDriver) exportedDriver).getFtpConnectionParams(), true);
			iterateDriversControlFileParams(((SFTPDistributionDriver) exportedDriver).getControlFileSeq(),true);
		}
	}
	
	@Transactional
	public ResponseObject importCollectionDriverUpdateMode(CollectionDriver dbCollectionDriver, CollectionDriver exportedCollectionDriver) {
		logger.debug("Update Collection driver dependants");
		ResponseObject responseObject = new ResponseObject();
		
		//set basic model parameter of Drivers class
		iterateDriverForImport(dbCollectionDriver, exportedCollectionDriver);
		
		//set basic parameter of CollectionDriver class
		FileGroupingParameterCollection exportedFileGroup = exportedCollectionDriver.getFileGroupingParameter();
		FileGroupingParameterCollection dbFileGroup = dbCollectionDriver.getFileGroupingParameter();
		
		if (exportedFileGroup != null && dbFileGroup != null) {
			//update file group
			servicesService.importFileGroupingParameterUpdateMode(dbFileGroup, exportedFileGroup);
			
		} else if(dbFileGroup == null && exportedFileGroup != null) {
			//add file group
			servicesService.importFileGroupingParameterAddMode(exportedFileGroup, exportedCollectionDriver.getCreatedByStaffId());
			dbCollectionDriver.setFileGroupingParameter(exportedFileGroup);
		}
		
		//set basic parameter of SFTPCollectionDriver
		if(exportedCollectionDriver instanceof SFTPCollectionDriver
				&& dbCollectionDriver instanceof SFTPCollectionDriver) {
			
			((SFTPCollectionDriver) dbCollectionDriver).setCertKey(((SFTPCollectionDriver) exportedCollectionDriver).getCertKey());
			FileFetchParams exportedFileFetchParam = ((SFTPCollectionDriver) exportedCollectionDriver).getMyFileFetchParams();
			FileFetchParams dbFileFetchParam = ((SFTPCollectionDriver) dbCollectionDriver).getMyFileFetchParams();
			if (dbFileFetchParam != null && exportedFileFetchParam != null) {
				//update file fetch parameter
				iterateFileFetchParam(dbFileFetchParam, exportedFileFetchParam);
			} else if(dbFileFetchParam == null && exportedFileFetchParam != null) {
				//add file fetch parameter
				((SFTPCollectionDriver) dbCollectionDriver).setMyFileFetchParams(exportedFileFetchParam);
			}

			responseObject = importDriverConnectionParamUpdateMode(((SFTPCollectionDriver) dbCollectionDriver).getFtpConnectionParams(),
					((SFTPCollectionDriver) exportedCollectionDriver).getFtpConnectionParams());
			((FTPCollectionDriver) dbCollectionDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());
			
		} 
		//set basic parameter of FTPCollectionDriver
		else if (exportedCollectionDriver instanceof FTPCollectionDriver
				&& dbCollectionDriver instanceof FTPCollectionDriver) {
			
			FileFetchParams exportedFileFetchParam = ((FTPCollectionDriver) exportedCollectionDriver).getMyFileFetchParams();
			FileFetchParams dbFileFetchParam = ((FTPCollectionDriver) dbCollectionDriver).getMyFileFetchParams();
			if (dbFileFetchParam != null && exportedFileFetchParam != null) {
				//update file fetch parameter
				iterateFileFetchParam(dbFileFetchParam, exportedFileFetchParam);
			} else if(dbFileFetchParam == null && exportedFileFetchParam != null) {
				//add file fetch parameter
				((SFTPCollectionDriver) dbCollectionDriver).setMyFileFetchParams(exportedFileFetchParam);
			}

			responseObject = importDriverConnectionParamUpdateMode(((FTPCollectionDriver) dbCollectionDriver).getFtpConnectionParams(),
					((FTPCollectionDriver) exportedCollectionDriver).getFtpConnectionParams());
			((FTPCollectionDriver) dbCollectionDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());

		}
		//set basic parameter of LocalCollectionDriver
		else if (exportedCollectionDriver instanceof LocalCollectionDriver
				&& dbCollectionDriver instanceof LocalCollectionDriver) {
			//no parameter in local collection driver
		}
		responseObject.setSuccess(true);
		responseObject.setObject(dbCollectionDriver);

		return responseObject;
	}
	
	@Transactional
	public ResponseObject importCollectionDriverAddMode(CollectionDriver dbCollectionDriver, CollectionDriver exportedCollectionDriver) {
		logger.debug("Update Collection driver dependants");
		ResponseObject responseObject = new ResponseObject();
			
		//set basic parameter of CollectionDriver class
		FileGroupingParameterCollection exportedFileGroup = exportedCollectionDriver.getFileGroupingParameter();
		FileGroupingParameterCollection dbFileGroup = dbCollectionDriver.getFileGroupingParameter();
		
		if(dbFileGroup == null && exportedFileGroup != null) {
			//add file group
			servicesService.importFileGroupingParameterAddMode(exportedFileGroup, exportedCollectionDriver.getCreatedByStaffId());
			dbCollectionDriver.setFileGroupingParameter(exportedFileGroup);
		}
		
		//set basic parameter of SFTPCollectionDriver
		if(exportedCollectionDriver instanceof SFTPCollectionDriver
				&& dbCollectionDriver instanceof SFTPCollectionDriver) {
			
			((SFTPCollectionDriver) dbCollectionDriver).setCertKey(((SFTPCollectionDriver) exportedCollectionDriver).getCertKey());
			FileFetchParams exportedFileFetchParam = ((SFTPCollectionDriver) exportedCollectionDriver).getMyFileFetchParams();
			FileFetchParams dbFileFetchParam = ((SFTPCollectionDriver) dbCollectionDriver).getMyFileFetchParams();
			
			if(dbFileFetchParam == null && exportedFileFetchParam != null) {
				//add file fetch parameter
				((SFTPCollectionDriver) dbCollectionDriver).setMyFileFetchParams(exportedFileFetchParam);
			}

			responseObject = importDriverConnectionParamAddMode(((SFTPCollectionDriver) dbCollectionDriver).getFtpConnectionParams(),
					((SFTPCollectionDriver) exportedCollectionDriver).getFtpConnectionParams());
			((FTPCollectionDriver) dbCollectionDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());
			
		} 
		//set basic parameter of FTPCollectionDriver
		else if (exportedCollectionDriver instanceof FTPCollectionDriver
				&& dbCollectionDriver instanceof FTPCollectionDriver) {
			
			FileFetchParams exportedFileFetchParam = ((FTPCollectionDriver) exportedCollectionDriver).getMyFileFetchParams();
			FileFetchParams dbFileFetchParam = ((FTPCollectionDriver) dbCollectionDriver).getMyFileFetchParams();
			
			if(dbFileFetchParam == null && exportedFileFetchParam != null) {
				//add file fetch parameter
				((SFTPCollectionDriver) dbCollectionDriver).setMyFileFetchParams(exportedFileFetchParam);
			}

			responseObject = importDriverConnectionParamAddMode(((FTPCollectionDriver) dbCollectionDriver).getFtpConnectionParams(),
					((FTPCollectionDriver) exportedCollectionDriver).getFtpConnectionParams());
			((FTPCollectionDriver) dbCollectionDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());

		}
		//set basic parameter of LocalCollectionDriver
		else if (exportedCollectionDriver instanceof LocalCollectionDriver
				&& dbCollectionDriver instanceof LocalCollectionDriver) {
			//no parameter in local collection driver
		}
		responseObject.setSuccess(true);
		responseObject.setObject(dbCollectionDriver);

		return responseObject;
	}
	
	public void iterateFileFetchParam(FileFetchParams dbFileFetchParam, FileFetchParams exportedFileFetchParam) {
		dbFileFetchParam.setFileFetchRuleEnabled(exportedFileFetchParam.isFileFetchRuleEnabled());
		dbFileFetchParam.setFileFetchType(exportedFileFetchParam.getFileFetchType());
		dbFileFetchParam.setFileFetchIntervalMin(exportedFileFetchParam.getFileFetchIntervalMin());
		dbFileFetchParam.setTimeZone(exportedFileFetchParam.getTimeZone());
	}
	
	@Transactional
	public ResponseObject importDistributionDriverUpdateMode(DistributionDriver dbDistributionDriver, DistributionDriver exportedDistributionDriver) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Update Distribution driver dependants");
		
		//set basic model parameter of Drivers class
		iterateDriverForImport(dbDistributionDriver, exportedDistributionDriver);
		
		if(dbDistributionDriver instanceof LocalDistributionDriver
				&& exportedDistributionDriver instanceof LocalDistributionDriver) {
			importDriverControlFileParamAddMode (dbDistributionDriver , exportedDistributionDriver);
		} else if(dbDistributionDriver instanceof FTPDistributionDriver
				&& exportedDistributionDriver instanceof FTPDistributionDriver) {
			
			responseObject = importDriverConnectionParamUpdateMode(((FTPDistributionDriver) dbDistributionDriver).getFtpConnectionParams(),
					((FTPDistributionDriver) exportedDistributionDriver).getFtpConnectionParams());
			
			((FTPDistributionDriver) dbDistributionDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());
			importDriverControlFileParamAddMode (dbDistributionDriver , exportedDistributionDriver);
		} else if(dbDistributionDriver instanceof DatabaseDistributionDriver
				&& exportedDistributionDriver instanceof DatabaseDistributionDriver) {
			
			importDatabaseDistributionDriverUpdateMode((DatabaseDistributionDriver) dbDistributionDriver, (DatabaseDistributionDriver) exportedDistributionDriver);
		} else if(dbDistributionDriver instanceof HadoopDistributionDriver
				&& exportedDistributionDriver instanceof HadoopDistributionDriver) {
			//no parameter to set
		} else if(dbDistributionDriver instanceof SFTPDistributionDriver
				&& exportedDistributionDriver instanceof SFTPDistributionDriver) {
			
			responseObject = importDriverConnectionParamUpdateMode(((SFTPDistributionDriver) dbDistributionDriver).getFtpConnectionParams(),
					((SFTPDistributionDriver) exportedDistributionDriver).getFtpConnectionParams());
			
			((SFTPDistributionDriver) dbDistributionDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());
			importDriverControlFileParamAddMode (dbDistributionDriver , exportedDistributionDriver);
		}
		
		responseObject.setSuccess(true);
		responseObject.setObject(dbDistributionDriver);
		return responseObject;
	}
	
	@Transactional
	public ResponseObject importDistributionDriverAddMode(DistributionDriver dbDistributionDriver, DistributionDriver exportedDistributionDriver) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Update Distribution driver dependants");
		
		if(dbDistributionDriver instanceof LocalDistributionDriver
				&& exportedDistributionDriver instanceof LocalDistributionDriver) {
			importDriverControlFileParamUpdateMode (dbDistributionDriver , exportedDistributionDriver);
		} else if(dbDistributionDriver instanceof FTPDistributionDriver
				&& exportedDistributionDriver instanceof FTPDistributionDriver) {
			
			responseObject = importDriverConnectionParamUpdateMode(((FTPDistributionDriver) dbDistributionDriver).getFtpConnectionParams(),
					((FTPDistributionDriver) exportedDistributionDriver).getFtpConnectionParams());
			
			((FTPDistributionDriver) dbDistributionDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());
			importDriverControlFileParamUpdateMode (dbDistributionDriver , exportedDistributionDriver);
		} else if(dbDistributionDriver instanceof DatabaseDistributionDriver
				&& exportedDistributionDriver instanceof DatabaseDistributionDriver) {
			
			importDatabaseDistributionDriverUpdateMode((DatabaseDistributionDriver) dbDistributionDriver, (DatabaseDistributionDriver) exportedDistributionDriver);
		} else if(dbDistributionDriver instanceof HadoopDistributionDriver
				&& exportedDistributionDriver instanceof HadoopDistributionDriver) {
			//no parameter to set
		} else if(dbDistributionDriver instanceof SFTPDistributionDriver
				&& exportedDistributionDriver instanceof SFTPDistributionDriver) {
			
			responseObject = importDriverConnectionParamUpdateMode(((SFTPDistributionDriver) dbDistributionDriver).getFtpConnectionParams(),
					((SFTPDistributionDriver) exportedDistributionDriver).getFtpConnectionParams());
			
			((SFTPDistributionDriver) dbDistributionDriver).setFtpConnectionParams((ConnectionParameter) responseObject.getObject());
			importDriverControlFileParamUpdateMode (dbDistributionDriver , exportedDistributionDriver);
		}
		
		responseObject.setSuccess(true);
		responseObject.setObject(dbDistributionDriver);
		return responseObject;
	}
	
	public void importDatabaseDistributionDriverUpdateMode(DatabaseDistributionDriver dbDriver, DatabaseDistributionDriver exportDriver) {
		if(dbDriver != null && exportDriver != null) {
			dbDriver.setTableName(exportDriver.getTableName());
			List<DatabaseDistributionDriverAttribute> exportedAttributeList = exportDriver.getAttributeList();
			List<DatabaseDistributionDriverAttribute> newAttributeList = new ArrayList<>();
			if(exportedAttributeList != null && !exportedAttributeList.isEmpty()) {
				int length = exportedAttributeList.size();
				for(int i = length-1; i >= 0; i--) {
					DatabaseDistributionDriverAttribute exportedAttribute = exportedAttributeList.get(i);
					if(exportedAttribute != null && !StateEnum.DELETED.equals(exportedAttribute.getStatus())) {
						exportedAttribute.setId(0);
						exportedAttribute.setCreatedByStaffId(dbDriver.getCreatedByStaffId());
						exportedAttribute.setCreatedDate(EliteUtils.getDateForImport(false));
						exportedAttribute.setLastUpdatedByStaffId(dbDriver.getCreatedByStaffId());
						exportedAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
						exportedAttribute.setDbDisDriver(dbDriver);
						newAttributeList.add(exportedAttribute);
					}
				}
			}
			List<DatabaseDistributionDriverAttribute> dbAttributeList = dbDriver.getAttributeList();
			if(dbAttributeList != null && !dbAttributeList.isEmpty()) {
				int length = dbAttributeList.size();
				for(int i = length-1; i >= 0; i--) {
					DatabaseDistributionDriverAttribute dbAttribute = dbAttributeList.get(i);
					if(dbAttribute != null) {
						dbAttribute.setStatus(StateEnum.DELETED);
						dbAttribute.setLastUpdatedByStaffId(dbDriver.getCreatedByStaffId());
						dbAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
						driverAttributeDao.merge(dbAttribute);
						dbAttributeList.remove(i);
					}
				}
			}
			dbDriver.setAttributeList(newAttributeList);
		}
	}
	
	public void iterateDriverForImport(Drivers dbDriver, Drivers exportedDriver) {
		dbDriver.setTimeout(exportedDriver.getTimeout());
		//dbDriver.setApplicationOrder(exportedDriver.getApplicationOrder());
		dbDriver.setMaxRetrycount(exportedDriver.getMaxRetrycount());
		dbDriver.setFileSeqOrder(exportedDriver.isFileSeqOrder());
		dbDriver.setMinFileRange(exportedDriver.getMinFileRange());
		dbDriver.setMaxFileRange(exportedDriver.getMaxFileRange());
		dbDriver.setNoFileAlert(exportedDriver.getNoFileAlert());
		dbDriver.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}
	
	/**
	 * Method will add or delete Distribution driver dependents.
	 * 
	 * @param driver
	 * @param isImport
	 * @return ResponseObject
	 */
	public ResponseObject importOrDeleteFTPDistributionDriverDependents(Drivers driver, boolean isImport) {
		ResponseObject responseObject;

		if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(driver.getDriverType().getAlias())
				|| EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(driver.getDriverType().getAlias())) {
			FTPDistributionDriver ftpDistributionDriver = (FTPDistributionDriver) driver;
			logger.debug("Driver user name :: " + ftpDistributionDriver.getFtpConnectionParams().getUsername());
			responseObject = iterateDriversConnectionParams(ftpDistributionDriver.getFtpConnectionParams(), isImport);
			responseObject = iterateDriversControlFileParams(ftpDistributionDriver.getControlFileSeq(), isImport);
			if (isImport) {
				responseObject.setSuccess(true);
				responseObject.setObject(ftpDistributionDriver);
			} else {
				ftpDistributionDriver.getFtpConnectionParams().setLastUpdatedByStaffId(driver.getLastUpdatedByStaffId());
				responseObject.setSuccess(true);
				responseObject.setObject(ftpDistributionDriver);
			}

		} else if (EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(driver.getDriverType().getAlias())) {
			logger.debug("Local distribution driver object found.");
			LocalDistributionDriver localDistributionDriver = (LocalDistributionDriver) driver;
			responseObject = new ResponseObject();
			responseObject = iterateDriversControlFileParams(localDistributionDriver.getControlFileSeq(), isImport);
			responseObject.setObject(driver);
			responseObject.setSuccess(true);
		} else if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(driver.getDriverType().getAlias())) {
			logger.debug("Database distribution driver object found.");
			responseObject = new ResponseObject();
			responseObject.setObject(driver);
			responseObject.setSuccess(true);
		} else {
			logger.debug("No driver type found.");
			responseObject = new ResponseObject();
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	/**
	 * Method will create or delete connection parameters for drivers.
	 * 
	 * @param connectionParams
	 * @return ResponseObject
	 */
	private ResponseObject iterateDriversConnectionParams(ConnectionParameter connectionParams, boolean isImport) {
		ResponseObject responseObject = new ResponseObject();

		if (isImport) {
			if (connectionParams != null) {
				logger.debug("Create Connection Parameter.");
				connectionParams.setId(0);
				connectionParams.setCreatedDate(EliteUtils.getDateForImport(false));
				if (connectionParams.getiPAddressHostList() != null) {
					List<String> ipAddressList = new ArrayList<>();
					List<HostParameters> hostParameterList = connectionParams.getiPAddressHostList();
					if(hostParameterList != null && !hostParameterList.isEmpty()) {
						int length = hostParameterList.size();
						for (int i = length-1; i >= 0; i--) {
							HostParameters hostParameter = hostParameterList.get(i);
							if(hostParameter != null) {
								ipAddressList.add(hostParameter.getiPAddressHost());
							}
						}
					}
					String ipAddress = String.join(",", ipAddressList);
					connectionParams.setiPAddressHost(ipAddress);
				}
				responseObject.setObject(connectionParams);
			} else {
				responseObject.setObject(new ConnectionParameter());
			}
		} else {
			logger.debug("Delete Connection Parameter for driver");
			connectionParams.setStatus(StateEnum.DELETED);
			connectionParams.setLastUpdatedDate(EliteUtils.getDateForImport(false));
			responseObject.setObject(connectionParams);
		}
		return responseObject;
	}
	
	
	/**
	 * Method will create or delete Control file parameters for drivers.
	 * 
	 * @param controlFileSeq
	 * @return ResponseObject
	 */
	private ResponseObject iterateDriversControlFileParams(SequenceManagement controlFileSeq, boolean isImport) {
		ResponseObject responseObject = new ResponseObject();

		if (isImport) {
			if (controlFileSeq != null) {
				logger.debug("Create Control File Sequence.");
				controlFileSeq.setId(0);
				controlFileSeq.setCreatedDate(EliteUtils.getDateForImport(false));
				responseObject.setObject(controlFileSeq);
			} else {
				responseObject.setObject(new SequenceManagement());
			}
		} else {
			if (controlFileSeq != null && controlFileSeq.getId() != 0) {
				logger.debug("Delete Control File Parameter for driver");
				controlFileSeq.setStatus(StateEnum.DELETED);
				controlFileSeq.setLastUpdatedDate(EliteUtils.getDateForImport(false));
				responseObject.setObject(controlFileSeq);
			}
		}
		return responseObject;
	}
	
	
	public ResponseObject importDriverConnectionParamAddMode(ConnectionParameter exportedConnectionParams) {
		ResponseObject responseObject = new ResponseObject();
		if (exportedConnectionParams != null) {
			logger.debug("Create Connection Parameter.");
			exportedConnectionParams.setId(0);
			exportedConnectionParams.setCreatedDate(EliteUtils.getDateForImport(false));
			if (exportedConnectionParams.getiPAddressHostList() != null) {
				List<String> ipAddressList = new ArrayList<>();
				for (int i = 0, size = exportedConnectionParams.getiPAddressHostList().size(); i < size; i++) {
					ipAddressList.add(exportedConnectionParams.getiPAddressHostList().get(i).getiPAddressHost());
				}
				String ipAddress = String.join(",", ipAddressList);
				exportedConnectionParams.setiPAddressHost(ipAddress);
			}
			responseObject.setObject(exportedConnectionParams);
		} else {
			responseObject.setObject(new ConnectionParameter());
		}
		return responseObject;
	}
	
	public ResponseObject importDriverConnectionParamUpdateMode(ConnectionParameter dbConnectionParams, ConnectionParameter exportedConnectionParams) {
		ResponseObject responseObject = new ResponseObject();
		Date date = new Date();
		if(dbConnectionParams != null && exportedConnectionParams != null) {
			//update connection parameter
			logger.debug("Update Connection Parameter.");
			dbConnectionParams.setFileTransferMode(exportedConnectionParams.getFileTransferMode());
			dbConnectionParams.setPort(exportedConnectionParams.getPort());
			dbConnectionParams.setTimeout(exportedConnectionParams.getTimeout());
			dbConnectionParams.setUsername(exportedConnectionParams.getUsername());
			dbConnectionParams.setPassword(exportedConnectionParams.getPassword());
			dbConnectionParams.setKeyFileLocation(exportedConnectionParams.getKeyFileLocation());
			dbConnectionParams.setFileSeparator(exportedConnectionParams.getFileSeparator());
			if (exportedConnectionParams.getiPAddressHostList() != null) {
				List<String> ipAddressList = new ArrayList<>();
				for (int i = 0, size = exportedConnectionParams.getiPAddressHostList().size(); i < size; i++) {
					ipAddressList.add(exportedConnectionParams.getiPAddressHostList().get(i).getiPAddressHost());
				}
				String ipAddress = String.join(",", ipAddressList);
				dbConnectionParams.setiPAddressHost(ipAddress);
			}
			dbConnectionParams.setLastUpdatedDate(date);
			responseObject.setObject(dbConnectionParams);
			
		} else if(dbConnectionParams == null && exportedConnectionParams != null) {
			//add connection parameter
			logger.debug("Create Connection Parameter.");
			exportedConnectionParams.setId(0);
			exportedConnectionParams.setCreatedDate(date);
			if (exportedConnectionParams.getiPAddressHostList() != null) {
				List<String> ipAddressList = new ArrayList<>();
				for (int i = 0, size = exportedConnectionParams.getiPAddressHostList().size(); i < size; i++) {
					ipAddressList.add(exportedConnectionParams.getiPAddressHostList().get(i).getiPAddressHost());
				}
				String ipAddress = String.join(",", ipAddressList);
				exportedConnectionParams.setiPAddressHost(ipAddress);
			}
			responseObject.setObject(exportedConnectionParams);
		}
		return responseObject;
	}
	
	public ResponseObject importDriverConnectionParamAddMode(ConnectionParameter dbConnectionParams, ConnectionParameter exportedConnectionParams) {
		ResponseObject responseObject = new ResponseObject();
		Date date = new Date();
		if(dbConnectionParams == null && exportedConnectionParams != null) {
			//add connection parameter
			logger.debug("Create Connection Parameter.");
			exportedConnectionParams.setId(0);
			exportedConnectionParams.setCreatedDate(date);
			if (exportedConnectionParams.getiPAddressHostList() != null) {
				List<String> ipAddressList = new ArrayList<>();
				for (int i = 0, size = exportedConnectionParams.getiPAddressHostList().size(); i < size; i++) {
					ipAddressList.add(exportedConnectionParams.getiPAddressHostList().get(i).getiPAddressHost());
				}
				String ipAddress = String.join(",", ipAddressList);
				exportedConnectionParams.setiPAddressHost(ipAddress);
			}
			responseObject.setObject(exportedConnectionParams);
		}
		return responseObject;
	}
	
	public void importDriverControlFileParamUpdateMode(DistributionDriver dbDistributionDriver, DistributionDriver exportedDistributionDriver) {
		
		dbDistributionDriver.getDriverControlFileParams().setControlFileEnabled(exportedDistributionDriver.getDriverControlFileParams().isControlFileEnabled());
		dbDistributionDriver.getDriverControlFileParams().setControlFileLocation(exportedDistributionDriver.getDriverControlFileParams().getControlFileLocation());
		dbDistributionDriver.getDriverControlFileParams().setAttributes(exportedDistributionDriver.getDriverControlFileParams().getAttributes());
		dbDistributionDriver.getDriverControlFileParams().setAttributeSeparator(exportedDistributionDriver.getDriverControlFileParams().getAttributeSeparator());
		dbDistributionDriver.getDriverControlFileParams().setFileRollingDuration(exportedDistributionDriver.getDriverControlFileParams().getFileRollingDuration());
		dbDistributionDriver.getDriverControlFileParams().setFileRollingStartTime(exportedDistributionDriver.getDriverControlFileParams().getFileRollingStartTime());
		dbDistributionDriver.getDriverControlFileParams().setControlFileName(exportedDistributionDriver.getDriverControlFileParams().getControlFileName());
		dbDistributionDriver.getDriverControlFileParams().setFileSeqEnable(exportedDistributionDriver.getDriverControlFileParams().isFileSeqEnable());
		
		SequenceManagement dbControlFileSeq = dbDistributionDriver.getControlFileSeq();
		SequenceManagement exportedControlFileSeq = exportedDistributionDriver.getControlFileSeq();
		
		if (dbControlFileSeq != null && exportedControlFileSeq != null) {
			logger.debug("Update Control File Sequence Parameter.");
			dbControlFileSeq.setStartRange(exportedControlFileSeq.getStartRange());
			dbControlFileSeq.setEndRange(exportedControlFileSeq.getEndRange());
			dbControlFileSeq.setNextValue(exportedControlFileSeq.getNextValue());
			dbControlFileSeq.setResetFrequency(exportedControlFileSeq.getResetFrequency());
			dbControlFileSeq.setPaddingEnable(exportedControlFileSeq.isPaddingEnable());
		} else if (dbControlFileSeq == null && exportedControlFileSeq != null) {
			logger.debug("Adding Control File Sequence Parameter.");
			SequenceManagement newSeq = exportedControlFileSeq;
			newSeq.setId(0);
			newSeq.setCreatedDate(new Date());
			dbDistributionDriver.setControlFileSeq(newSeq);
		}
	}
	
	public void importDriverControlFileParamAddMode(DistributionDriver dbDistributionDriver, DistributionDriver exportedDistributionDriver) {		
		
		SequenceManagement dbControlFileSeq = dbDistributionDriver.getControlFileSeq();
		SequenceManagement exportedControlFileSeq = exportedDistributionDriver.getControlFileSeq();		
		
		if(dbControlFileSeq == null && exportedControlFileSeq != null) {
			logger.debug("Adding Control File Sequence Parameter.");
			SequenceManagement newSeq = exportedControlFileSeq;
			newSeq.setId(0);
			newSeq.setCreatedDate(new Date());
			dbDistributionDriver.setControlFileSeq(newSeq);
		}
	}

	/**
	 * Add Driver Type in DataBase
	 */
	@Transactional
	@Override
	public ResponseObject addDriverType(DriverType driverType) {
		ResponseObject responseObject = new ResponseObject();
		driverTypeDao.save(driverType);

		if (driverType.getId() != 0) {
			responseObject.setSuccess(true);

		} else {
			responseObject.setSuccess(false);

		}
		return responseObject;
	}

	/**
	 * Fetch Collection Driver
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllDriverTypeList(DriverCategory driverCategory) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Fetching driver type list.");

		List<DriverType> driverTypeList = driverTypeDao.getAllDriverTypeList(driverCategory);

		if (driverTypeList != null && !driverTypeList.isEmpty()) {
			logger.info("Found " + driverTypeList.size() + " for driver category " + driverCategory);
			responseObject.setSuccess(true);
			responseObject.setObject(driverTypeList);
		} else {
			logger.info("Failed to get driver type list for category " + driverCategory);
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	/**
	 * Add Collection Driver
	 * 
	 * @param collectionDriver
	 * @return ResponseObject
	 */
	@Transactional()
	@Auditable(auditActivity = AuditConstants.CREATE_DRIVER, actionType = BaseConstants.CREATE_ACTION, currentEntity = Drivers.class,
			ignorePropList = "")
	@Override
	public ResponseObject addCollectionDriver(CollectionDriver collectionDriver) {

		ResponseObject responseObject = new ResponseObject();

		int driverCount = driversDao.getDriverCount(collectionDriver.getName(), getServiceId(collectionDriver.getService()));
		if (driverCount > 0) {

			logger.debug("inside addCollectionDriver : duplicate driver name found:" + collectionDriver.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DRIVER_NAME);

		} else {

			Service service = servicesDao.findByPrimaryKey(Service.class, collectionDriver.getService().getId());

			DriverType driverType = driverTypeDao.getDriverTypeByAlias(collectionDriver.getDriverType().getAlias());

			if (driverType != null) {

				String collectionDriverType = collectionDriver.getDriverType().getAlias();
				CollectionDriver newcollectionDriver = null;

				if (EngineConstants.FTP_COLLECTION_DRIVER.equals(collectionDriverType)
						|| EngineConstants.SFTP_COLLECTION_DRIVER.equals(collectionDriverType)) {

					ConnectionParameter connectionParameter = new ConnectionParameter();
					connectionParameter.setCreatedByStaffId(collectionDriver.getCreatedByStaffId());
					connectionParameter.setLastUpdatedByStaffId(collectionDriver.getCreatedByStaffId());
					connectionParameter.setCreatedDate(new Date());

					FileFetchParams fileFetchParam = new FileFetchParams();
					connectionParameter.setCreatedByStaffId(collectionDriver.getCreatedByStaffId());
					connectionParameter.setCreatedDate(new Date());

					if (EngineConstants.FTP_COLLECTION_DRIVER.equals(collectionDriverType)) {
						logger.debug("inside addCollectionDriver : FTP Collection Driver Found:" + collectionDriver.getName());
						newcollectionDriver = new FTPCollectionDriver();
						connectionParameter.setPort(21);
						((FTPCollectionDriver) newcollectionDriver).setFtpConnectionParams(connectionParameter);
						((FTPCollectionDriver) newcollectionDriver).setMyFileFetchParams(fileFetchParam);
					} else {
						logger.debug("inside addCollectionDriver : SFTP Collection Driver Found:" + collectionDriver.getName());
						newcollectionDriver = new SFTPCollectionDriver();
						connectionParameter.setPort(22);
						((SFTPCollectionDriver) newcollectionDriver).setFtpConnectionParams(connectionParameter);
						((SFTPCollectionDriver) newcollectionDriver).setMyFileFetchParams(fileFetchParam);
					}

				} else if (EngineConstants.LOCAL_COLLECTION_DRIVER.equals(collectionDriver.getDriverType().getAlias())) {

					logger.debug("inside addCollectionDriver : Local Collection Driver Found");
					newcollectionDriver = new LocalCollectionDriver();

				}
				int totalDriverCount = driversDao.getDriverCountByServiceId(collectionDriver.getService().getId());
				logger.debug("inside addCollectionDriver : Total Driver Count is ::" + totalDriverCount);
				logger.debug("inside addCollectionDriver : Next Application Order is ::" + totalDriverCount);

				if (newcollectionDriver != null) {

					newcollectionDriver.setCreatedByStaffId(collectionDriver.getCreatedByStaffId());
					newcollectionDriver.setCreatedDate(collectionDriver.getCreatedDate());
					newcollectionDriver.setLastUpdatedByStaffId(collectionDriver.getCreatedByStaffId());
					newcollectionDriver.setLastUpdatedDate(collectionDriver.getCreatedDate());
					newcollectionDriver.setName(collectionDriver.getName());
					newcollectionDriver.setTimeout(collectionDriver.getTimeout());
					newcollectionDriver.setApplicationOrder(totalDriverCount);
					newcollectionDriver.setStatus(collectionDriver.getStatus());
					newcollectionDriver.setService(service);
					newcollectionDriver.setDriverType(driverType);

					FileGroupingParameterCollection fileGroupingParameter = new FileGroupingParameterCollection();
					fileGroupingParameter.setCreatedByStaffId(collectionDriver.getCreatedByStaffId());
					fileGroupingParameter.setLastUpdatedByStaffId(collectionDriver.getCreatedByStaffId());
					fileGroupingParameter.setCreatedDate(new Date());
					newcollectionDriver.setFileGroupingParameter(fileGroupingParameter);

					iterateCollectionDriverDependants(newcollectionDriver, true);

					logger.debug("Create Driver and its dependants created successfully");

					if (EngineConstants.FTP_COLLECTION_DRIVER.equals(collectionDriverType)) {
						driversDao.save((FTPCollectionDriver) newcollectionDriver);
					} else if (EngineConstants.SFTP_COLLECTION_DRIVER.equals(collectionDriverType)) {
						driversDao.save((SFTPCollectionDriver) newcollectionDriver);
					} else if (EngineConstants.LOCAL_COLLECTION_DRIVER.equals(collectionDriverType)) {
						driversDao.save((LocalCollectionDriver) newcollectionDriver);
					}

					if (newcollectionDriver.getId() != 0) {
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.DRIVER_ADD_SUCCESS);
						responseObject.setObject(newcollectionDriver);
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DRIVER_ADD_FAIL);
					}

				} else {
					logger.debug("Driver type is null");
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.DRIVER_ADD_FAIL);
				}
			} else {
				logger.debug("Driver type is null");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DRIVER_ADD_FAIL);
			}
		}
		return responseObject;
	}

	/**
	 * Fetch list of collection driver configured in service
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getDriversByServiceId(int serviceId) {

		ResponseObject responseObject = new ResponseObject();
		List<Drivers> driverList = driversDao.getDriverByServiceId(serviceId);

		if (driverList != null && !driverList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(driverList);
		} else {
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Fetch driver type list
	 */
	@Transactional
	@Override
	public List<DriverType> getDriverTypeList(DriverCategory driverCategory) {
		logger.debug("Getting all drivers for category of " + driverCategory);
		return driverTypeDao.getAllDriverTypeList(driverCategory);
	}

	/**
	 * Fetch Collection Driver Dependents using id and type
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getDriverByTypeAndId(int driverId, String driverTypeAlias) {

		ResponseObject responseObject = new ResponseObject();

		if (EngineConstants.FTP_COLLECTION_DRIVER.equals(driverTypeAlias)) {
			logger.debug("inside getCollectionDriverByIdAndType : Fetch configuration for FTP Collection Driver");
			FTPCollectionDriver ftpDriver = driversDao.getFTPCollectionDriverById(driverId);
			if (ftpDriver != null) {
				responseObject.setSuccess(true);
				responseObject.setObject(ftpDriver);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SFTP_DRIVER_TYPE_NOT_FOUND);
			}
		} else if (EngineConstants.SFTP_COLLECTION_DRIVER.equals(driverTypeAlias)) {
			logger.debug("inside getCollectionDriverByIdAndType : Fetch configuration for SFTP Collection Driver");

			SFTPCollectionDriver sftpDriver = driversDao.getSFTPCollectionDriverById(driverId);
			if (sftpDriver != null) {
				responseObject.setSuccess(true);
				responseObject.setObject(sftpDriver);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SFTP_DRIVER_TYPE_NOT_FOUND);
			}
		} else if (EngineConstants.LOCAL_COLLECTION_DRIVER.equals(driverTypeAlias)) {
			logger.debug("inside getCollectionDriverByIdAndType : Fetch configuration for Local Collection Driver");
			LocalCollectionDriver localDriver = driversDao.getLocalCollectionDriverById(driverId);
			if (localDriver != null) {
				responseObject.setSuccess(true);
				responseObject.setObject(localDriver);
			} else {
				responseObject.setSuccess(false);
			}

		} else if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(driverTypeAlias)
				|| EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(driverTypeAlias)
				|| EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(driverTypeAlias)
				|| EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(driverTypeAlias)) {
			logger.debug("inside getCollectionDriverByIdAndType : Fetch configuration for Distribution Driver");
			responseObject = getDistributionDriverByIdAndType(driverId, driverTypeAlias);
		}
		return responseObject;
	}

	/**
	 * Method will fetch distribution driver by id and alias.
	 * 
	 * @param driverId
	 * @param driverTypeAlias
	 * @return responseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getDistributionDriverByIdAndType(int driverId, String driverTypeAlias) {

		ResponseObject responseObject = new ResponseObject();

		if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(driverTypeAlias)) {
			logger.debug("inside getDistributionDriverByIdAndType : Fetch configuration for FTP Distribution Driver");

			FTPDistributionDriver ftpDriver = driversDao.getFTPDistributionDriverById(driverId);

			if (ftpDriver != null) {
				logger.info("FTP Distribution driver found successfully.");
				responseObject.setSuccess(true);
				responseObject.setObject(ftpDriver);
			} else {
				logger.info("Failed to get FTP Distribution driver.");
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.SFTP_DRIVER_TYPE_NOT_FOUND);
			}
		} else if (EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(driverTypeAlias)) {

			SFTPDistributionDriver sftpDriver = driversDao.getSFTPDistributionDriverById(driverId);
			if (sftpDriver != null) {
				logger.info("SFTP Distribution driver found successfully.");
				responseObject.setSuccess(true);
				responseObject.setObject(sftpDriver);
			} else {
				logger.info("Failed to get SFTP Distribution driver.");
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.SFTP_DRIVER_TYPE_NOT_FOUND);
			}

		} else if (EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(driverTypeAlias)) {
			LocalDistributionDriver localDriver = driversDao.getLocalDistributionDriverById(driverId);
			if (localDriver != null) {
				logger.info("Local Distribution driver found successfully.");
				responseObject.setSuccess(true);
				responseObject.setObject(localDriver);
			} else {
				logger.info("Failed to get Local Distribution driver.");
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.SFTP_DRIVER_TYPE_NOT_FOUND);
			}
		} else if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(driverTypeAlias)) {
			DatabaseDistributionDriver databaseDriver = driversDao.getDatabaseDistributionDriverById(driverId);
			if (databaseDriver != null) {
				logger.info("Database Distribution driver found successfully.");
				responseObject.setSuccess(true);
				responseObject.setObject(databaseDriver);
			} else {
				logger.info("Failed to get Database Distribution driver.");
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.SFTP_DRIVER_TYPE_NOT_FOUND);
			}
		}
		return responseObject;

	}

	/**
	 * Check driver name is unique in case of update
	 * 
	 * @param serviceId
	 * @param driverName
	 * @return boolean
	 */
	@Transactional
	public boolean isDriverUniqueForUpdate(int serviceId, String driverName) {
		int count = driversDao.getDriverCount(driverName, serviceId);
		if(count > 1) {
			return false;
		} 
		return true;
	}

	/**
	 * Update Collection Driver configuration
	 * 
	 * @param collectionDriver
	 * @return ResponseObject
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_DRIVER, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Drivers.class,
			ignorePropList = "")
	@Override
	public ResponseObject updateCollectionDriverConfiguration(CollectionDriver collectionDriver) {

		ResponseObject responseObject = new ResponseObject();

		if (collectionDriver != null) {
			Service service = servicesService.getServiceandServerinstance(collectionDriver.getService().getId());

			if (service != null) {

				String collectionDriverType = collectionDriver.getDriverType().getAlias();
				if (EngineConstants.FTP_COLLECTION_DRIVER.equals(collectionDriverType)
						|| EngineConstants.SFTP_COLLECTION_DRIVER.equals(collectionDriverType)) {

					logger.debug("inside updateCollectionDriver : FTP Collection Driver Instance found");
					FTPCollectionDriver ftpCollectionDriver;
					if (EngineConstants.FTP_COLLECTION_DRIVER.equals(collectionDriverType)) {
						ftpCollectionDriver = (FTPCollectionDriver) collectionDriver;
					} else {
						ftpCollectionDriver = (SFTPCollectionDriver) collectionDriver;
					}
					ConnectionParameter connectionParam = null;

					if (ftpCollectionDriver.getFtpConnectionParams() != null) {
						connectionParam = ftpCollectionDriver.getFtpConnectionParams();
					}

					if (connectionParam != null) {
						connectionParam.setLastUpdatedDate(new Date());
						connectionParam.setLastUpdatedByStaffId(collectionDriver.getLastUpdatedByStaffId());
						if (connectionParam.getPassword() != null) {
							connectionParam.setPassword(encryptData(connectionParam.getPassword()));
						}
						connectionParamDao.merge(connectionParam);

						FileGroupingParameter fileGroupParam = ftpCollectionDriver.getFileGroupingParameter();
						if (fileGroupParam != null) {
							fileGroupParam.setLastUpdatedDate(new Date());
							fileGroupParam.setLastUpdatedByStaffId(collectionDriver.getLastUpdatedByStaffId());
							fileGroupingParamDao.merge(fileGroupParam);

							ftpCollectionDriver.setLastUpdatedDate(new Date());
							ftpCollectionDriver.setLastUpdatedByStaffId(collectionDriver.getLastUpdatedByStaffId());
							ftpCollectionDriver.setService(service);
							driversDao.merge(ftpCollectionDriver);

							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_SUCCESS);
							if (ftpCollectionDriver.getFtpConnectionParams().getPassword() != null) { // NOSONAR
								ftpCollectionDriver.getFtpConnectionParams().setPassword(
										decryptData(ftpCollectionDriver.getFtpConnectionParams().getPassword()));

							}
							responseObject.setObject(ftpCollectionDriver);
						} else {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
						}
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
					}
				} else if (EngineConstants.LOCAL_COLLECTION_DRIVER.equals(collectionDriver.getDriverType().getAlias())) {
					logger.debug("inside updateCollectionDriver : Local Collection Driver Instance found");

					LocalCollectionDriver localCollectionDriver = (LocalCollectionDriver) collectionDriver;

					FileGroupingParameter fileGroupParam = localCollectionDriver.getFileGroupingParameter();
					if (fileGroupParam != null) {
						fileGroupParam.setLastUpdatedDate(new Date());
						fileGroupParam.setLastUpdatedByStaffId(collectionDriver.getLastUpdatedByStaffId());
						fileGroupingParamDao.merge(fileGroupParam);

						localCollectionDriver.setLastUpdatedDate(new Date());
						localCollectionDriver.setLastUpdatedByStaffId(collectionDriver.getLastUpdatedByStaffId());
						localCollectionDriver.setService(service);
						driversDao.merge(localCollectionDriver);
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_SUCCESS);
						responseObject.setObject(localCollectionDriver);
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
					}
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
			}
		}

		return responseObject;
	}

	/**
	 * @param collectionDriver
	 * @return
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_DRIVER, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Drivers.class,
			ignorePropList = "")
	@Override
	public ResponseObject updateDistributionDriverConfiguration(DistributionDriver distributionDriver) {

		ResponseObject responseObject = new ResponseObject();

		if (distributionDriver != null) {

			Service service = servicesService.getServiceandServerinstance(distributionDriver.getService().getId());
			if (service != null) {

				String distributionDriverType = distributionDriver.getDriverType().getAlias();

				if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)
						|| EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)) {

					logger.debug("inside updateDistribution : FTP Distribution Driver Instance found");
					FTPDistributionDriver ftpDistributionDriver;
					if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)) {
						ftpDistributionDriver = (FTPDistributionDriver) distributionDriver;
					} else {
						ftpDistributionDriver = (SFTPDistributionDriver) distributionDriver;
					}
					
					if (ftpDistributionDriver.getControlFileSeq().getId() != 0)
					{
						SequenceManagement controlFileSeq = ControlFileDao.getControlFileSequenceBySequenceId(distributionDriver.getControlFileSeq().getId());
						if (controlFileSeq != null) {
							ftpDistributionDriver.getControlFileSeq().setCreatedByStaffId(controlFileSeq.getCreatedByStaffId());
							ftpDistributionDriver.getControlFileSeq().setCreatedDate(controlFileSeq.getCreatedDate());
							ftpDistributionDriver.getControlFileSeq().setLastsequpdatedDate(controlFileSeq.getLastsequpdatedDate());
						}
					} else {
						ftpDistributionDriver.getControlFileSeq().setCreatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
					}
					
					ftpDistributionDriver.getControlFileSeq().setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
					ftpDistributionDriver.getControlFileSeq().setLastUpdatedDate(new Date());
					ftpDistributionDriver.getControlFileSeq().setNextValue(distributionDriver.getControlFileSeq().getStartRange());
					
					ConnectionParameter connectionParam = null;
					if (ftpDistributionDriver.getFtpConnectionParams() != null) {
						connectionParam = ftpDistributionDriver.getFtpConnectionParams();
					}

					if (connectionParam != null) {
						if (connectionParam.getPassword() != null) {
							connectionParam.setPassword(encryptData(connectionParam.getPassword()));
						}
						connectionParam.setLastUpdatedDate(new Date());
						connectionParam.setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
						connectionParamDao.merge(connectionParam);
						
						ftpDistributionDriver.setLastUpdatedDate(new Date());
						ftpDistributionDriver.setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
						ftpDistributionDriver.setService(service);
						driversDao.merge(ftpDistributionDriver);
						
						// Distribution Drivers which are already added before Control File.
						DistributionDriver disDriver = driversDao.getFTPDistributionDriverById(distributionDriver.getId());
						ftpDistributionDriver.setControlFileSeq(disDriver.getControlFileSeq());
											
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_SUCCESS);
						if (ftpDistributionDriver.getFtpConnectionParams().getPassword() != null) {
							ftpDistributionDriver.getFtpConnectionParams().setPassword(
									decryptData(ftpDistributionDriver.getFtpConnectionParams().getPassword()));
						}
						responseObject.setObject(ftpDistributionDriver);
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
					}
				} else if (EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(distributionDriver.getDriverType().getAlias())) {

					logger.debug("Updating: Local distributioin driver configuration.");
					LocalDistributionDriver localDistributionDriver = (LocalDistributionDriver) distributionDriver;
					
					if (localDistributionDriver.getControlFileSeq().getId() != 0)
					{
						SequenceManagement controlFileSeq = ControlFileDao.getControlFileSequenceBySequenceId(distributionDriver.getControlFileSeq().getId());
						if (controlFileSeq != null) {
							localDistributionDriver.getControlFileSeq().setCreatedByStaffId(controlFileSeq.getCreatedByStaffId());
							localDistributionDriver.getControlFileSeq().setCreatedDate(controlFileSeq.getCreatedDate());
							localDistributionDriver.getControlFileSeq().setLastsequpdatedDate(controlFileSeq.getLastsequpdatedDate());
						}
					} else {
						localDistributionDriver.getControlFileSeq().setCreatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
					}
					
					localDistributionDriver.getControlFileSeq().setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
					localDistributionDriver.getControlFileSeq().setLastUpdatedDate(new Date());
					localDistributionDriver.getControlFileSeq().setNextValue(distributionDriver.getControlFileSeq().getStartRange());
					
					localDistributionDriver.setLastUpdatedDate(new Date());
					localDistributionDriver.setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
					localDistributionDriver.setService(service);
					driversDao.merge(localDistributionDriver);
					
					// Distribution Drivers which are already added before Control File.
					DistributionDriver disDriver = driversDao.getLocalDistributionDriverById(distributionDriver.getId());
					localDistributionDriver.setControlFileSeq(disDriver.getControlFileSeq());
					
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_SUCCESS);
					responseObject.setObject(localDistributionDriver);

				} else if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(distributionDriver.getDriverType().getAlias())) {

					logger.debug("Updating: Database distributioin driver configuration.");
					DatabaseDistributionDriver databaseDistributionDriver = (DatabaseDistributionDriver) distributionDriver;

					databaseDistributionDriver.setLastUpdatedDate(new Date());
					databaseDistributionDriver.setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
					databaseDistributionDriver.setService(service);
					driversDao.merge(databaseDistributionDriver);
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_SUCCESS);
					responseObject.setObject(databaseDistributionDriver);

				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
				}
			} else {
				logger.debug("Failed to update configuration driver type not found.");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
			}
		} else {
			logger.debug("Failed to update driver configuration due to object found null.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
		}

		return responseObject;
	}

	/**
	 * Update driver application order in database
	 * 
	 * @throws CloneNotSupportedException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject updateDriversApplicationOrder(String driverOrderList) throws CloneNotSupportedException {

		ResponseObject responseObject = new ResponseObject();
		JSONArray jOrderArray = new JSONArray(driverOrderList);
		int serviceId = 0;

		if (jOrderArray.length() > 0) {
			for (int index = 0; index < jOrderArray.length(); index++) {
				JSONObject driverDetail = jOrderArray.getJSONObject(index);
				int id = driverDetail.getInt(BaseConstants.DRIVER_ID);
				Drivers driver = driversDao.findByPrimaryKey(Drivers.class, id);
				serviceId = driver.getService().getId();

				Drivers dDrivers = (Drivers) driver.clone();
				dDrivers.setApplicationOrder(driverDetail.getInt("order"));
				DriversService driverServiceImpl = (DriversService) SpringApplicationContext.getBean("driversService"); // getting
																														// spring
																														// bean
																														// context
																														// for
																														// aop
																														// to
																														// call
																														// method
																														// from
																														// another.
				driverServiceImpl.updateDriversApplicationOrder(dDrivers, driverDetail);
			}

			ResponseObject collDriverListResponse = getDriversByServiceId(serviceId);
			if (collDriverListResponse.isSuccess())
				responseObject.setObject(collDriverListResponse.getObject());

			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.FTP_COLLECTION_DRIVER_REORDER_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FTP_COLLECTION_DRIVER_NO_DRIVER_LIST);
		}
		return responseObject;
	}

	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_DRIVER_APLICATION_ORDER, actionType = BaseConstants.UPDATE_ACTION,
			currentEntity = Drivers.class,
			ignorePropList = "noFileAlert,driverType,driverPathList,service,maxFileRange,minFileRange,fileSeqOrder,maxRetrycount,timeout,name")
	public ResponseObject updateDriversApplicationOrder(Drivers driver, JSONObject driverDetail) {
		ResponseObject responseObject = new ResponseObject();
		driversDao.merge(driver);
		responseObject.setSuccess(true);
		return responseObject;
	}

	/**
	 * Fetch Driver By Driver Id
	 */
	@Transactional(readOnly = true)
	@Override
	public Drivers getDriverById(int driverId) {
		return driversDao.findByPrimaryKey(Drivers.class, driverId);
	}

	/**
	 * Get Collection driver object with its dependents
	 */
	@Transactional
	@Override
	public List<CollectionDriver> getCollectionDriverDetails(List<CollectionDriver> collectionDriverList) {
		return driversDao.getAllCollectionDriverDetails(collectionDriverList);
	}

	/**
	 * Method will fetch total driver count by service instance id.
	 * 
	 * @param serviceInstanceId
	 * @return ResponseObject
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public long getDriversTotalCount(int serviceInstanceId) {
		Map<String, Object> driversConditions = driversDao.getDriversPaginatedList(serviceInstanceId);
		return driversDao.getQueryCount(Drivers.class, (List<Criterion>) driversConditions.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) driversConditions.get(BaseConstants.ALIASES));
	}

	/**
	 * Method will fetch DriverList
	 * 
	 * @param serviceInstanceId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List of Drivers
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Map<String, Object>> getDriversPaginatedList(int serviceInstanceId, int startIndex, int limit, String sidx, String sord) {
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row;
		Map<String, Object> conditionsAndAliases = driversDao.getDriversPaginatedList(serviceInstanceId);

		List<CollectionDriver> collectionDriverList = driversDao.getDriversPaginatedList(CollectionDriver.class,
				(List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);

		collectionDriverList = getCollectionDriverDetails(collectionDriverList);
		if (collectionDriverList != null && !collectionDriverList.isEmpty()) {
			for (CollectionDriver driverList : collectionDriverList) {
				row = new HashMap<>();

				row.put(BaseConstants.DRIVER_ID, driverList.getId());
				row.put(BaseConstants.ID, driverList.getId());
				row.put(BaseConstants.DRIVER_NAME, driverList.getName());
				row.put(BaseConstants.DRIVER_TYPE, driverList.getDriverType().getAlias());
				row.put(BaseConstants.DRIVER_TYPE_NAME, driverList.getDriverType().getType());

				if (EngineConstants.FTP_COLLECTION_DRIVER.equals(driverList.getDriverType().getAlias())
						|| EngineConstants.SFTP_COLLECTION_DRIVER.equals(driverList.getDriverType().getAlias())) {
					if (driverList instanceof FTPCollectionDriver) {

						FTPCollectionDriver ftpCollectionDriver;
						if (EngineConstants.FTP_COLLECTION_DRIVER.equals(driverList.getDriverType().getAlias())) {
							ftpCollectionDriver = (FTPCollectionDriver) driverList;
						} else {
							ftpCollectionDriver = (SFTPCollectionDriver) driverList;
						}

						if (ftpCollectionDriver.getFtpConnectionParams().getiPAddressHost() != null
								&& !"".equals(ftpCollectionDriver.getFtpConnectionParams().getiPAddressHost())) {
							row.put(BaseConstants.HOST, ftpCollectionDriver.getFtpConnectionParams().getiPAddressHost());
							row.put(BaseConstants.PORT, ftpCollectionDriver.getFtpConnectionParams().getPort());
						} else {
							row.put(BaseConstants.HOST, "-");
							row.put(BaseConstants.PORT, "-");
						}
					}
				} else if (EngineConstants.LOCAL_COLLECTION_DRIVER.equals(driverList.getDriverType().getAlias())) {
					LocalCollectionDriver localCollectionDriver = (LocalCollectionDriver) driverList;

					if (localCollectionDriver.getService().getServerInstance().getServer().getIpAddress() != null
							&& localCollectionDriver.getService().getServerInstance().getPort() > 0) {
						row.put(BaseConstants.HOST, localCollectionDriver.getService().getServerInstance().getServer().getIpAddress());
						row.put(BaseConstants.PORT, localCollectionDriver.getService().getServerInstance().getPort());
					} else {
						row.put(BaseConstants.HOST, "-");
						row.put(BaseConstants.PORT, "-");
					}
				}
				// Add SFTP and Local driver.
				row.put(BaseConstants.APPLICATION_ORDER, driverList.getApplicationOrder());
				row.put(BaseConstants.ENABLE, driverList.getStatus().toString().trim());
				rowList.add(row);

			}
		}
		return rowList;
	}

	/**
	 * Method will delete Driver and its dependents
	 * 
	 * @param driver
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_DRIVER, actionType = BaseConstants.DELETE_ACTION, currentEntity = Drivers.class,
			ignorePropList = "")
	public ResponseObject deleteDriverDetails(int driverId, int serviceId) {
		ResponseObject responseObject = new ResponseObject();

		List<Drivers> driverList = driversDao.getDriverByServiceId(serviceId);

		if (driverList != null && !driverList.isEmpty()) {
			int tempApplicationOrder = 0;
			for (int i = 0; i < driverList.size(); i++) {

				if (driverId == (driverList.get(i).getId()) && StateEnum.INACTIVE.equals(driverList.get(i).getStatus())) {
					tempApplicationOrder = driverList.get(i).getApplicationOrder();
					driverList.get(i).setApplicationOrder(-1);

					responseObject = importOrDeleteDriverConfig(driverList.get(i), false);
					if (!responseObject.isSuccess()) {
						responseObject.setResponseCode(ResponseCode.DRIVER_DELETE_FAIL);
						responseObject.setSuccess(false);
					}
				} else if(driverId == (driverList.get(i).getId()) && StateEnum.ACTIVE.equals(driverList.get(i).getStatus())) { 
					responseObject.setResponseCode(ResponseCode.DRIVER_DELETE_ACTIVE_FAIL);
					responseObject.setSuccess(false);
				} else {

					if (responseObject.isSuccess() && driverList.get(i).getApplicationOrder() > tempApplicationOrder) {
						driverList.get(i).setApplicationOrder(driverList.get(i).getApplicationOrder() - 1);
						driversDao.merge(driverList.get(i));
					}
				}
			}

			if (responseObject.isSuccess()) {
				responseObject.setResponseCode(ResponseCode.DRIVER_DELETE_SUCCESS);
				responseObject.setSuccess(true);
			} else if (responseObject.getResponseCode()==null) {								
				responseObject.setResponseCode(ResponseCode.DRIVER_DELETE_FAIL);
				responseObject.setSuccess(false);
			}
		} else {
			responseObject.setResponseCode(ResponseCode.DRIVER_DELETE_FAIL);
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	/**
	 * Change driver status
	 * 
	 * @throws CloneNotSupportedException
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_DRIVER_STATUS, actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = Drivers.class,
			ignorePropList = "driverPathList,driverType,noFileAlert,timeout,applicationOrder,fileSeqOrder,service")
	public ResponseObject updateDriverStatus(int id, String driverType, String driverStatus) throws CloneNotSupportedException {

		logger.debug("Going to update driver status for " + driverType + " and id " + id + " to status " + driverStatus);
		ResponseObject responseObject;

		responseObject = getDriverByTypeAndId(id, driverType);

		if (responseObject.isSuccess()) {

			Drivers drivers = (Drivers) responseObject.getObject();

			if (drivers != null) {
				if (StateEnum.ACTIVE.name().equals(driverStatus.trim())) {
					drivers.setStatus(StateEnum.ACTIVE);
				} else {
					drivers.setStatus(StateEnum.INACTIVE);
				}

				driversDao.merge(drivers);
				logger.info("Driver status has been updated successfully.");
				responseObject.setResponseCode(ResponseCode.DRIVER_UPDATE_SUCCESS);
				responseObject.setSuccess(true);

			} else {
				logger.info("Failed to update driver status.");
				responseObject.setResponseCode(ResponseCode.DRIVER_UPDATE_FAIL);
				responseObject.setSuccess(false);
			}
		} else {
			logger.info("Failed to update driver status.");
			responseObject.setResponseCode(ResponseCode.DRIVER_UPDATE_FAIL);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Validate driver parameter for import operation
	 * 
	 * @param driver
	 * @param driverImportErrorList
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public List<ImportValidationErrors> validateDriverForImport(Drivers driver, List<ImportValidationErrors> importErrorList) {

		driverValidator.validateDriverForImport(driver, importErrorList);

		logger.debug("Validate Driver's PathList");
		List<PathList> pathlists = driver.getDriverPathList();
		List<ImportValidationErrors> newimportErrorList = importErrorList;
		if (pathlists != null) {
			PathList pathList;
			for (int i = 0, size = pathlists.size(); i < size; i++) {
				pathList = pathlists.get(i);
				Drivers currentDriver = pathList.getDriver();
				pathList.setDriver(driver);
				if (StateEnum.ACTIVE.equals(pathList.getStatus())) {
					newimportErrorList = pathListService.validatePathListForImport(pathList, newimportErrorList);
				}
				pathList.setDriver(currentDriver);
			}
		}
		return newimportErrorList;
	}

	/**
	 * Iterate over driver dependents , change id and name for import operation
	 * 
	 * @param driver
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public void iterateOverDriverConfig(Drivers driver, boolean isImport) {

		if (driver instanceof CollectionDriver) {
			iterateCollectionDriverDependants(driver, isImport);
		} else if (driver instanceof DistributionDriver) {
			importOrDeleteFTPDistributionDriverDependents(driver, isImport);
			importOrDeleteDatabaseDisDriverDependents(driver, isImport);
		}

		pathListService.iterateDriverPathListDetails(driver, isImport);

	}
	 
	@Transactional(readOnly = true)
	@Override
	public void importDriverConfigAddAndKeepBothMode(Drivers exportedDriver, int importMode) {
		if (exportedDriver instanceof CollectionDriver) {
			importCollectionDriverAddMode(exportedDriver);
		} else if (exportedDriver instanceof DistributionDriver) {
			importDistributionDriverAddMode(exportedDriver);
		}
		pathListService.importDriverPathListAddAndKeepBothMode(exportedDriver, importMode);
	}
	
	@Transactional
	@Override
	public void importDriverForUpdateMode(Drivers dbDriver, Drivers exportedDriver, int importMode) {
		if (exportedDriver instanceof CollectionDriver
				&& dbDriver instanceof CollectionDriver) {
			/*if(importMode==BaseConstants.IMPORT_MODE_ADD) {
				importCollectionDriverAddMode((CollectionDriver) dbDriver, (CollectionDriver) exportedDriver);
			}else {
				importCollectionDriverUpdateMode((CollectionDriver) dbDriver, (CollectionDriver) exportedDriver);
			}*/
			if(importMode==BaseConstants.IMPORT_MODE_UPDATE){
				importCollectionDriverUpdateMode((CollectionDriver) dbDriver, (CollectionDriver) exportedDriver);
			}
		} else if (exportedDriver instanceof DistributionDriver
				&& dbDriver instanceof DistributionDriver) {
			/*if(importMode==BaseConstants.IMPORT_MODE_ADD) {
				importDistributionDriverAddMode((DistributionDriver) dbDriver, (DistributionDriver) exportedDriver);
			}else {
				importDistributionDriverUpdateMode((DistributionDriver) dbDriver, (DistributionDriver) exportedDriver);
			}*/
			if(importMode==BaseConstants.IMPORT_MODE_UPDATE) {
				importDistributionDriverUpdateMode((DistributionDriver) dbDriver, (DistributionDriver) exportedDriver);
			}
		}
		pathListService.importDriverPathListUpdateMode(dbDriver, exportedDriver, importMode);
	}

	/**
	 * Method will get distribution driver paginated list for jqgrid.
	 * 
	 * @param driverId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getDistributionDriverPaginatedList(int serviceInstanceId, int startIndex, int limit, String sidx, String sord) {

		logger.debug("Fetching distibution driver list.");
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row;
		Map<String, Object> conditionsAndAliases = driversDao.getDriversPaginatedList(serviceInstanceId);

		List<DistributionDriver> distributionDriverList = driversDao.getDistributionDriversPaginatedList(DistributionDriver.class,
				(List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);

		distributionDriverList = driversDao.getAllDistributionDriverDetails(distributionDriverList);
		if (distributionDriverList != null && !distributionDriverList.isEmpty()) {

			logger.info("Found " + distributionDriverList.size() + " distribution drivers.");
			for (DistributionDriver driverList : distributionDriverList) {
				row = new HashMap<>();

				row.put(BaseConstants.ID, driverList.getId());
				row.put(BaseConstants.DRIVER_NAME, driverList.getName());
				row.put("driverType", driverList.getDriverType().getAlias());
				row.put(BaseConstants.DRIVER_TYPE_NAME, driverList.getDriverType().getType());
				if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(driverList.getDriverType().getAlias())
						|| EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(driverList.getDriverType().getAlias())) {
					if (driverList instanceof FTPDistributionDriver) {

						FTPDistributionDriver ftpDistributionDriver;
						if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(driverList.getDriverType().getAlias())) {
							ftpDistributionDriver = (FTPDistributionDriver) driverList;
						} else {
							ftpDistributionDriver = (SFTPDistributionDriver) driverList;
						}

						if (ftpDistributionDriver.getFtpConnectionParams().getiPAddressHost() != null
								&& !"".equals(ftpDistributionDriver.getFtpConnectionParams().getiPAddressHost())) {
							row.put(BaseConstants.IPADDRESS, ftpDistributionDriver.getFtpConnectionParams().getiPAddressHost());
							row.put("port", ftpDistributionDriver.getFtpConnectionParams().getPort());
						} else {
							row.put(BaseConstants.IPADDRESS, "-");
							row.put("port", "-");
						}
					}
				} else if (EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(driverList.getDriverType().getAlias())) {

					row.put(BaseConstants.IPADDRESS, "-");
					row.put("port", "-");

				} else if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(driverList.getDriverType().getAlias())) {
					DatabaseDistributionDriver databaseDistributionDriver = (DatabaseDistributionDriver) driverList;

					if (databaseDistributionDriver.getService().getServerInstance().getServer().getIpAddress() != null
							&& databaseDistributionDriver.getService().getServerInstance().getPort() > 0) {
						row.put(BaseConstants.IPADDRESS, databaseDistributionDriver.getService().getServerInstance().getServer().getIpAddress());
						row.put("port", databaseDistributionDriver.getService().getServerInstance().getPort());
					} else {
						row.put(BaseConstants.IPADDRESS, "-");
						row.put("port", "-");
					}
				}

				row.put("pluginList", "Associated Plug-in List");
				row.put("applicationOrder", driverList.getApplicationOrder());
				row.put("enable", driverList.getStatus().toString().trim());
				rowList.add(row);

			}
		}
		return rowList;
	}

	/**
	 * Method will get all plug-in list by Distribution driver id.
	 * 
	 * @param driverId
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllDistributionPluginbyDriverId(int driverId) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to fetch plugin list for distribution driver id " + driverId);

		List<Object[]> driverPluginList = driversDao.getDistributionDriverComposerList(driverId);

		if (driverPluginList != null && !driverPluginList.isEmpty()) {
			logger.info(driverPluginList.size() + " plugin list found with all its detailed information.");
			JSONArray pluginArray = new JSONArray(driverPluginList);
			responseObject.setSuccess(true);
			responseObject.setObject(pluginArray);
		} else {
			logger.info("Failed to get plugin details for driver id " + driverId);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_COMPOSER_LIST);
		}
		return responseObject;

	}

	/**
	 * Method will create new FTP distribution driver.
	 * 
	 * @param ftpDistributionDriver
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_DRIVER, actionType = BaseConstants.CREATE_ACTION, currentEntity = Drivers.class,
			ignorePropList = "")
	public ResponseObject createDistributionDriver(DistributionDriver distributionDriver) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to create new distribution driver.");

		int driverCount = driversDao.getDriverCount(distributionDriver.getName(), getServiceId(distributionDriver.getService()));
		if (driverCount > 0) {
			logger.debug("Inside createDistributionDriver : duplicate driver name found:" + distributionDriver.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DRIVER_NAME);
		} else {
			Service service = servicesDao.findByPrimaryKey(Service.class, distributionDriver.getService().getId());
			DriverType driverType = driverTypeDao.getDriverTypeByAlias(distributionDriver.getDriverType().getAlias());

			if (driverType != null) {

				String distributionDriverType = distributionDriver.getDriverType().getAlias();
				DistributionDriver newDistributionDriver = null;

				ConnectionParameter connectionParameter = new ConnectionParameter();
				connectionParameter.setCreatedByStaffId(distributionDriver.getCreatedByStaffId());
				connectionParameter.setLastUpdatedByStaffId(distributionDriver.getCreatedByStaffId());
				connectionParameter.setCreatedDate(new Date());

				SequenceManagement controlFileSequence = new SequenceManagement();
				controlFileSequence.setCreatedByStaffId(distributionDriver.getCreatedByStaffId());
				controlFileSequence.setLastUpdatedByStaffId(distributionDriver.getCreatedByStaffId());
				controlFileSequence.setCreatedDate(new Date());
				controlFileSequence.setLastUpdatedDate(new Date());
				
				ControlFileParams controlFileParams = new ControlFileParams();
				
				if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)
						|| EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)) {

					if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)) {
						logger.debug("inside addCollectionDriver : FTP Collection Driver Found:" + distributionDriver.getName());
						newDistributionDriver = new FTPDistributionDriver();
						connectionParameter.setPort(21);
						((FTPDistributionDriver) newDistributionDriver).setFtpConnectionParams(connectionParameter);
						newDistributionDriver.setControlFileSeq(controlFileSequence);
						newDistributionDriver.setDriverControlFileParams(controlFileParams);
					} else {
						logger.debug("inside addCollectionDriver : SFTP Collection Driver Found:" + distributionDriver.getName());
						newDistributionDriver = new SFTPDistributionDriver();
						connectionParameter.setPort(22);
						((SFTPDistributionDriver) newDistributionDriver).setFtpConnectionParams(connectionParameter);
						newDistributionDriver.setControlFileSeq(controlFileSequence);
						newDistributionDriver.setDriverControlFileParams(controlFileParams);
					}

				} else if (EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(distributionDriver.getDriverType().getAlias())) {

					logger.debug("inside addCollectionDriver : Local Collection Driver Found");
					newDistributionDriver = new LocalDistributionDriver();
					newDistributionDriver.setControlFileSeq(controlFileSequence);
					newDistributionDriver.setDriverControlFileParams(controlFileParams);

				} else if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(distributionDriver.getDriverType().getAlias())) {

					logger.debug("inside addDistributionDriver : Database Distribution Driver Found");
					newDistributionDriver = new DatabaseDistributionDriver();
				}
				int totalDriverCount = driversDao.getDriverCountByServiceId(distributionDriver.getService().getId());

				logger.debug("inside addDistribution : Total Driver Count is ::" + totalDriverCount);
				logger.debug("inside addDistribution : Next Application Order is ::" + totalDriverCount);

				if (newDistributionDriver != null) {

					newDistributionDriver.setCreatedByStaffId(distributionDriver.getCreatedByStaffId());
					newDistributionDriver.setCreatedDate(distributionDriver.getCreatedDate());
					newDistributionDriver.setLastUpdatedByStaffId(distributionDriver.getCreatedByStaffId());
					newDistributionDriver.setLastUpdatedDate(distributionDriver.getCreatedDate());
					newDistributionDriver.setName(distributionDriver.getName());
					newDistributionDriver.setApplicationOrder(totalDriverCount);
					newDistributionDriver.setStatus(distributionDriver.getStatus());
					newDistributionDriver.setService(service);
					newDistributionDriver.setDriverType(driverType);

					responseObject = importOrDeleteFTPDistributionDriverDependents(newDistributionDriver, true);

					if (responseObject.isSuccess()) {

						logger.debug("creating new driver of " + distributionDriverType);

						if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)) {
							driversDao.save((FTPDistributionDriver) newDistributionDriver);
						} else if (EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)) {
							driversDao.save((SFTPDistributionDriver) newDistributionDriver);
						} else if (EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(distributionDriverType)) {
							driversDao.save((LocalDistributionDriver) responseObject.getObject());
						} else if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(distributionDriverType)) {
							driversDao.save((DatabaseDistributionDriver) responseObject.getObject());
						}

						if (newDistributionDriver.getId() != 0) {
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.DRIVER_ADD_SUCCESS);
						} else {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.DRIVER_ADD_FAIL);
						}
					} else {
						logger.debug("Fail to create Driver depedants");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DRIVER_ADD_FAIL);
					}
				} else {
					logger.debug("Failed to create driveras Driver type found null.");
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.DRIVER_ADD_FAIL);
				}
			} else {
				logger.debug("Failed to create driveras Driver type found null.");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DRIVER_ADD_FAIL);
			}
		}
		return responseObject;
	}

	/**
	 * It will update distribution driver basic details.
	 * 
	 * @param distributionDriver
	 */

	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_DRIVER, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Drivers.class,
			ignorePropList = "applicationOrder")
	public ResponseObject updateDistributionDriver(DistributionDriver distributionDriver) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to update Distribution Driver details.");
		if (isDriverUniqueForUpdate(getServiceId(distributionDriver.getService()), distributionDriver.getName())) {
			String distributionDriverType = distributionDriver.getDriverType().getAlias();
			if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)
					|| EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)) {

				logger.debug("inside updateCollectionDriver : FTP or SFTP Distribution Driver Instance found.");
				DistributionDriver ftpDistributionDriver = driversDao.getFTPDistributionDriverById(distributionDriver.getId());
				ftpDistributionDriver.setName(distributionDriver.getName());
				ftpDistributionDriver.setStatus(distributionDriver.getStatus());
				ftpDistributionDriver.setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
				ftpDistributionDriver.setLastUpdatedDate(new Date());

				if(ftpDistributionDriver instanceof SFTPDistributionDriver && 
						EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)){
					DriverType dtype = driverTypeDao.getDriverTypeByAlias(EngineConstants.FTP_DISTRIBUTION_DRIVER);
					ftpDistributionDriver.setDriverType(dtype);
					driversDao.updateDriverType(ftpDistributionDriver.getId(), BaseConstants.DRIVER_TYPE_FFTP_DISTRIBUTION_DRIVER);
					
				}else if(ftpDistributionDriver instanceof FTPDistributionDriver && 
						EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(distributionDriverType)){
					DriverType dtype = driverTypeDao.getDriverTypeByAlias(EngineConstants.SFTP_DISTRIBUTION_DRIVER);
					ftpDistributionDriver.setDriverType(dtype);
					driversDao.updateDriverType(ftpDistributionDriver.getId(), BaseConstants.DRIVER_TYPE_SFTP_DISTRIBUTION_DRIVER);
				}
				driversDao.merge(ftpDistributionDriver);

				logger.info("Driver details updated successfully.");
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.DRIVER_UPDATE_SUCCESS);
				responseObject.setObject(ftpDistributionDriver);

			} else if (EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(distributionDriver.getDriverType().getAlias())) {

				logger.debug("inside update distribution : Local Distribution Driver Instance found");
				LocalDistributionDriver localDistributionDriver = driversDao.getLocalDistributionDriverById(distributionDriver.getId());
				localDistributionDriver.setName(distributionDriver.getName());
				localDistributionDriver.setTimeout(distributionDriver.getTimeout());
				localDistributionDriver.setStatus(distributionDriver.getStatus());
				localDistributionDriver.setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
				localDistributionDriver.setLastUpdatedDate(new Date());

				driversDao.merge(localDistributionDriver);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.DRIVER_UPDATE_SUCCESS);
				responseObject.setObject(localDistributionDriver);
			} 
		} else {
			logger.debug("inside updateDistributionDriver : duplicate driver name found in update:" + distributionDriver.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DRIVER_NAME);
		}
		return responseObject;
	}

	
	/**
	 * Iterate over driver dependents , change id and name for import operation
	 * 
	 * @param exportedService
	 * @return
	 */
	@Transactional
	@Override
	public void iterateServiceDriverDetails(Service exportedService, boolean isImport) {
		ResponseObject responseObject = new ResponseObject();

		if (exportedService != null) {
			List<Drivers> drivers = exportedService.getMyDrivers();
			List<Drivers> driversList = new ArrayList<>();
			if (drivers != null && !drivers.isEmpty()) {
				Drivers driver;
				for (int i = 0; i < drivers.size(); i++) {
					driver = drivers.get(i);
					if (!StateEnum.DELETED.equals(driver.getStatus())) {
						driversList.add(driver);
						if (isImport) { // import call

							driver.setId(0);
							driver.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, driver.getName()));
							driver.setCreatedByStaffId(exportedService.getLastUpdatedByStaffId());
							driver.setCreatedDate(new Date());
							driver.setService(exportedService);
							driver.setApplicationOrder(i);
							driver.setLastUpdatedByStaffId(exportedService.getLastUpdatedByStaffId());

						} else { // delete call
							driver.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, driver.getName()));
							driver.setStatus(StateEnum.DELETED);
							driver.setLastUpdatedDate(new Date());
							driver.setApplicationOrder(-1);
						}

						driver.setLastUpdatedByStaffId(exportedService.getLastUpdatedByStaffId());
						driver.setLastUpdatedDate(new Date());

						iterateOverDriverConfig(driver, isImport);

					}
				}
				exportedService.setMyDrivers(driversList);

			} else {
				logger.debug("Driver not configured for service " + exportedService.getId());
				responseObject.setSuccess(true);
				responseObject.setObject(exportedService);
			}
		}
	}
	
	@Transactional
	@Override
	public void importServiceDriverAddAndKeepBothMode(Service exportedService, int importMode) {
		if(exportedService != null) {
			List<Drivers> drivers = exportedService.getMyDrivers();
			List<Drivers> driversList = new ArrayList<>();
			if(!CollectionUtils.isEmpty(drivers)) {
				int driverLength = drivers.size();
				int driverApplicationOrder=0;
				for(int i = driverLength-1; i >= 0; i--) {
					Drivers driver = drivers.get(i);
					if(driver != null && !StateEnum.DELETED.equals(driver.getStatus())) {
						saveDriverForImport(driver, exportedService, importMode);
						if (importMode != BaseConstants.IMPORT_MODE_OVERWRITE) { 
							driver.setApplicationOrder(driverApplicationOrder);
						}
						driverApplicationOrder++;
						driversList.add(driver);
					}
				}
				exportedService.setMyDrivers(driversList);
			}
		}
	}
	
	@Transactional
	@Override
	public void importServiceDriverUpdateMode(Service serviceDb, Service exportedService, int importMode) {
		Hibernate.initialize(serviceDb.getMyDrivers());
		List<Drivers> dbDrivers = serviceDb.getMyDrivers();
		if(exportedService != null) {
			List<Drivers> drivers = exportedService.getMyDrivers();
			if(drivers != null && !drivers.isEmpty()) {
				int driverLength = drivers.size();
				int driverApplicationOrder = 0;
				for(int i = driverLength-1; i >= 0; i--) {
					Drivers exportedDriver = drivers.get(i);
					if(exportedDriver != null) {
						if (!StateEnum.DELETED.equals(exportedDriver.getStatus())) {
							Drivers dbDriver = getDriverFromList(dbDrivers, exportedDriver.getName());
							if(dbDriver != null && importMode!=BaseConstants.IMPORT_MODE_ADD && !dbDriver.getDriverType().getAlias().equalsIgnoreCase(exportedDriver.getDriverType().getAlias())){
								dbDriver = null;
								exportedDriver.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedDriver.getName()));
							}
							if(dbDriver != null) {
								//update
								driverApplicationOrder = dbDriver.getApplicationOrder();
								importDriverForUpdateMode(dbDriver, exportedDriver, importMode);
								dbDriver.setApplicationOrder(driverApplicationOrder);
								dbDrivers.add(dbDriver);
							} else {
								//add
								saveDriverForImport(exportedDriver, serviceDb, importMode);
								dbDrivers.add(exportedDriver);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void saveDriverForImport(Drivers driver, Service service, int importMode){
		driver.setId(0);
		
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			driver.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, driver.getName()));
		}
		
		driver.setCreatedByStaffId(service.getLastUpdatedByStaffId());
		driver.setCreatedDate(EliteUtils.getDateForImport(false));
		driver.setService(service);
		if (importMode != BaseConstants.IMPORT_MODE_OVERWRITE) {
			int totalDriverCount;
			if (service.getId() > 0) {
				totalDriverCount = driversDao.getMaxApplicationOrderForDriver(service.getId());
			} else {
				totalDriverCount = driversDao.getTotalDriverCount();
			}

			if (totalDriverCount != -1) {
				driver.setApplicationOrder(++totalDriverCount);
			} else {
				driver.setApplicationOrder(++totalDriverCount);
			}
		}
	
		driver.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		driver.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		
		importDriverConfigAddAndKeepBothMode(driver, importMode);//NOSONAR
	}
	
	/**
	 * Fetch driver type using alias
	 * 
	 * @param driverTypeAlias
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject getDriverTypeByAlias(String driverTypeAlias) {
		ResponseObject responseObject = new ResponseObject();
		DriverType driverType = driverTypeDao.getDriverTypeByAlias(driverTypeAlias);
		if (driverType != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(driverType);
		}
		return responseObject;
	}

	/**
	 * To encrypt the password to hexadecimal
	 * 
	 * @param pwd
	 * @return
	 */
	public String encryptData(String pwd) {

		char[] chars = pwd.toCharArray();

		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		logger.debug(hex.toString());
		return hex.toString();

	}

	/**
	 * Decrypt password to ASCCII
	 * 
	 * @param pwd
	 * @return
	 */
	@Override
	public String decryptData(String pwd) {
		byte[] bytes = DatatypeConverter.parseHexBinary(pwd.trim());
		String decodedPwd = new String(bytes);
		logger.debug(decodedPwd);
		return decodedPwd;
	}

	/**
	 * Create Driver Attribute
	 * 
	 * @param driverAttribute
	 * @param driverId
	 * @param driverType
	 * @param staffId
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_DATABASE_DISTRIBUTION_DRIVER_ATTRIBUTE, actionType = BaseConstants.CREATE_ACTION,
			currentEntity = DatabaseDistributionDriverAttribute.class, ignorePropList = "")
	public ResponseObject createDriverAttributes(DatabaseDistributionDriverAttribute driverAttribute, int staffId) {

		ResponseObject responseObject = new ResponseObject();
		driverAttribute.setCreatedByStaffId(staffId);
		driverAttribute.setLastUpdatedByStaffId(staffId);
		driverAttribute.setCreatedDate(new Date());
		driverAttribute.setLastUpdatedDate(new Date());

		Drivers driver = driversDao.findByPrimaryKey(Drivers.class, driverAttribute.getDbDisDriver().getId());
		if (driver != null) {
			if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equalsIgnoreCase(driver.getDriverType().getAlias())) {
				DatabaseDistributionDriver dbDriver = (DatabaseDistributionDriver) driver;
				responseObject.setSuccess(true);
				responseObject.setObject(dbDriver);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_ATTRIBUTE_BY_ID);
		}
		if (isAttributeUniqueForUpdate(driverAttribute.getId(), driverAttribute.getDatabaseFieldName(), driverAttribute.getDbDisDriver().getId())) {
			if (responseObject.isSuccess()) {

				driverAttribute.setDbDisDriver((DatabaseDistributionDriver) responseObject.getObject());
				driverAttributeDao.save(driverAttribute);
				driversDao.save(driver);
				if (driverAttribute.getId() > 0) {
					responseObject.setSuccess(true);
					responseObject.setObject(driverAttribute);
					responseObject.setResponseCode(ResponseCode.DRIVER_ATTRIBUTE_ADD_SUCCESS);
				} else {
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.DRIVER_ATTRIBUTE_ADD_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.DRIVER_ATTRIBUTE_ADD_FAIL);
			}
		} else {
			logger.info("duplicate attribute Database field found:" + driverAttribute.getDatabaseFieldName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DRIVER_DUPLICATE_ATTRIBUTE_FOUND);
		}
		return responseObject;
	}

	/**
	 * Method will get attribute list count by driver id
	 * 
	 * @param driverId
	 * @return long
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public long getAttributeListCountByDriverId(int driverId) {
		Map<String, Object> attributeConditionsList = driverAttributeDao.getAttributeConditionList(driverId);
		return driverAttributeDao.getQueryCount(DatabaseDistributionDriverAttribute.class,
				(List<Criterion>) attributeConditionsList.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) attributeConditionsList.get(BaseConstants.ALIASES));
	}

	/**
	 * @param driverId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List<DatabaseDistributionDriverAttribute>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<DatabaseDistributionDriverAttribute> getPaginatedList(int driverId, int startIndex, int limit, String sidx, String sord) {

		Map<String, Object> attributeConditionsList = driverAttributeDao.getAttributeConditionList(driverId);
		return driverAttributeDao.getPaginatedList(DatabaseDistributionDriverAttribute.class,
				(List<Criterion>) attributeConditionsList.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) attributeConditionsList.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);
	}

	/**
	 * Prepare driver attribute map for jqgrid
	 * 
	 * @param resultList
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Map<String, Object>> getAttributeMap(List<DatabaseDistributionDriverAttribute> resultList) {

		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();

		if (resultList != null) {
			for (DatabaseDistributionDriverAttribute attributeList : resultList) {
				row = new HashMap<>();
				row.put("id", attributeList.getId());
				row.put("databaseFieldName", attributeList.getDatabaseFieldName());
				row.put("unifiedFieldName", attributeList.getUnifiedFieldName());
				row.put("dataType", attributeList.getDataType());
				row.put("defualtValue", attributeList.getDefualtValue());
				rowList.add(row);
			}
		}
		return rowList;

	}
	
	@Override
	@Transactional
	public ResponseObject uploadDriverAttributesFromCSV(File csvFile, int driverId, int staffId) {
		logger.debug("inside uploadDriverAttributesFromCSV");
		logger.debug("driverId : " + driverId);		
		ResponseObject responseObject = new ResponseObject();		
		String header =  null;
		String[] headers = null;
		String row = null;
		int hlength=0;
		int rlength=0;		
		boolean isUploadSuccess = true;		
		List<String> newAttributeList = new ArrayList<>();
		List<ImportValidationErrors> importErrorList = new ArrayList<>();		
		String recordFailureMessage = "CSV record attribute upload failed on Line number # ";
		String failureMessage = "CSV Record failed on Line number # ";
		try {			
			responseObject.setSuccess(true);
			//reading csv file with Files utility which returns line by line in List<String> type
			List<String> data = Files.readAllLines(Paths.get(csvFile.getAbsolutePath())); 
			if(data.size() <= 1){
	    		responseObject.setSuccess(false);
	    		responseObject.setResponseCode(ResponseCode.UPLOADED_ATRRIBUTE_FILE_EMPTY);
	    		return  responseObject;
	    	}
			for (int i=0;i<data.size();i++){
				if(i==0){
					header = data.get(i);			    				
					headers = header.split(BaseConstants.COMMA_SEPARATOR);
					hlength = headers.length;					
				}else{
					row = data.get(i);			
					List<String> rows = CSVUtils.parseLine(row, ',');
					rlength = rows.size();
					//if header length is not matching with csv record length then return error
					if(hlength!=rlength) {
						responseObject.setSuccess(false);
						responseObject.setObject(failureMessage+(i+1));
			    		responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_HEADER_MISTMATCH);
			    		deleteDriverAttributes(EliteUtils.getCSVString(newAttributeList), staffId);	
			    		return  responseObject;
					}else {				
						header = headers[0];
						//get appropriate composer object by plugin type and set csv row value by comparing header values
						DatabaseDistributionDriverAttribute dbDriverAttribute = new DatabaseDistributionDriverAttribute();
						DatabaseDistributionDriver dbDisDriver= new DatabaseDistributionDriver();
						dbDisDriver.setId(driverId);
						dbDriverAttribute.setDbDisDriver(dbDisDriver);
						
						for(int attrCnt = 0; attrCnt< headers.length; attrCnt++) {
							if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.DATA_TYPE.getName())) {
								dbDriverAttribute.setDataType(rows.get(attrCnt));
								if(!StringUtils.isEmpty(rows.get(attrCnt))) {
									try {
										DriverDataTypeEnum.valueOf(rows.get(attrCnt));
									}catch(IllegalArgumentException e) {
										logger.trace("IllegalArgumentException: ",e);
										isUploadSuccess = false;	
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Data type not available in the system.");
										break;										
									}
								}
							}else if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.DATABASE_FIELD_NAME.getName())) {
								dbDriverAttribute.setDatabaseFieldName(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.DEFAULT_VALUE.getName())) {
								dbDriverAttribute.setDefualtValue(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.UNIFIED_FIELD_NAME.getName())) {
								dbDriverAttribute.setUnifiedFieldName(rows.get(attrCnt));
								String regexVal=Regex.get(SystemParametersConstant.ATTR_UNIFIED_FIELD,BaseConstants.REGEX_MAP_CACHE);
								if (!StringUtils.isEmpty(rows.get(attrCnt)) && (rows.get(attrCnt).length()>100 ||match(regexVal, rows.get(attrCnt)))){
									isUploadSuccess = false;	
									setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"'  Unified field must begin with a letter of the alphabet and allows only Maximum 100 alphanumeric characters.");									
									break;
								}
							}else if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.ENABLE_PADDING.getName())) {
								String isPaddingEnable = rows.get(attrCnt); 
								if((isPaddingEnable !=null) && ("true".equalsIgnoreCase(isPaddingEnable) || "false".equalsIgnoreCase(isPaddingEnable))) {
									dbDriverAttribute.setPaddingEnable(Boolean.parseBoolean(rows.get(attrCnt)));
								}else{
									isUploadSuccess = false;
									setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+ rows.get(attrCnt)+" "+"' Invalid value for Enable Padding! Value must be either true or false");
									break;
								}
							}else if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.PADDING_LENGTH.getName())) {
								try {
									dbDriverAttribute.setLength(Integer.parseInt(rows.get(attrCnt)));	
								}catch(Exception e) {
									logger.trace("Exception  : ",e);
									isUploadSuccess = false;	
									setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) + "' Invalid value for Padding Length.");
									break;										
								}
							}else if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.TYPE.getName())) {
								if(!StringUtils.isEmpty(rows.get(attrCnt))) {
									try {
										dbDriverAttribute.setPaddingType(PositionEnum.valueOf(rows.get(attrCnt).toUpperCase()));
										PositionEnum.valueOf(rows.get(attrCnt));
									}catch(IllegalArgumentException e) {
										logger.trace("IllegalArgumentException  : ",e);
										isUploadSuccess = false;	
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Type not available in the system");
										break;										
									}
								}		
							}else if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.PADDING_CHARACTER.getName())) {
								dbDriverAttribute.setPaddingChar(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.PREFIX.getName())) {
								dbDriverAttribute.setPrefix(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumDbDriverAttributeHeader.SUFFIX.getName())) {
								dbDriverAttribute.setSuffix(rows.get(attrCnt));
							}else {
								responseObject.setSuccess(false);
					    		responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);
					    		responseObject.setObject(failureMessage+(i+1));					   
					    		isUploadSuccess=false;
					    		break;
							}
						}	
						if(!isUploadSuccess) {									
							deleteDriverAttributes(EliteUtils.getCSVString(newAttributeList), staffId);		
				    		break;
						}	
							driverValidator.validateDriverAttributeParameter(dbDriverAttribute, null, null, true, importErrorList);
							checkCSVRecordErrors(responseObject, importErrorList, i+1);
						
						//validation failed then return with errors
						if(!responseObject.isSuccess()) {
							deleteDriverAttributes(EliteUtils.getCSVString(newAttributeList), staffId);	
							responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);
							isUploadSuccess=false;
				    		break;
						}						
						//if validation passes then create driver attribute
						responseObject = createDriverAttributes(dbDriverAttribute, staffId);					
						//if any error while creating attribute do roll back - removing all added attribute prior to this attribute
						if(!responseObject.isSuccess()) {
							deleteDriverAttributes(EliteUtils.getCSVString(newAttributeList), staffId);	
							responseObject.setObject(failureMessage+(i+1));									    		
				    		isUploadSuccess=false;
				    		break;
						}
						//maintaining newly added attributes list for anytime rollback in future			
						DatabaseDistributionDriverAttribute newAttribute = (DatabaseDistributionDriverAttribute) responseObject.getObject();
						if (newAttribute.getId() > 0) {
							newAttributeList.add(String.valueOf(newAttribute.getId()));	
						}		
					}
				}
			}
			//if all attributes added successfully then return sucess message to controller
			if(isUploadSuccess) {
				responseObject = new ResponseObject();				
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.DRIVER_ATTR_UPLOAD_SUCCESS);
			}
	    }catch(FileNotFoundException e){			    	
	    	logger.trace("Lookup data file not found : ",e);
	    	responseObject.setSuccess(false);
	    	responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);	    		    	
	    }catch (IOException e) {			    
			logger.trace("Problem occurred while reading file : ",e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);			
		}catch (Exception e) {			    	
			logger.trace("Exception occurred while reading file : ",e);			
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);			
		}
		return responseObject;
	}
	private void setResponseObject(ResponseObject responseObject,String s){
		responseObject.setSuccess(false);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
		responseObject.setObject(s);
	}
	
	private void checkCSVRecordErrors(ResponseObject responseObject, List<ImportValidationErrors> importErrorList, int lineNo) {
		
		if (!importErrorList.isEmpty()) {
			logger.debug("Validation Fail for imported file");
			JSONArray finaljArray = new JSONArray();
			for (ImportValidationErrors errors : importErrorList) {
				JSONArray jArray = new JSONArray();
				jArray.put("Error on csv file line # "+lineNo +". " +errors.getErrorMessage());
				finaljArray.put(jArray);
			}
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
			responseObject.setObject(finaljArray);
		}
	}	
	/**
	 * Update Driver Attribute
	 * 
	 * @param driverAttribute
	 * @param driverId
	 * @param driverType
	 * @param staffId
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_DATABASE_DISTRIBUTION_DRIVER_ATTRIBUTE, actionType = BaseConstants.UPDATE_ACTION,
			currentEntity = DatabaseDistributionDriverAttribute.class, ignorePropList = "dbDisDriver")
	public ResponseObject updateDriverAttributes(DatabaseDistributionDriverAttribute driverAttribute, int staffId) {
		ResponseObject responseObject = new ResponseObject();

		driverAttribute.setCreatedByStaffId(staffId);
		driverAttribute.setLastUpdatedByStaffId(staffId);
		driverAttribute.setCreatedDate(new Date());
		driverAttribute.setLastUpdatedDate(new Date());

		if (isAttributeUniqueForUpdate(driverAttribute.getId(), driverAttribute.getDatabaseFieldName(), driverAttribute.getDbDisDriver().getId())) {
			if (driverAttribute.getId() > 0) {
				DatabaseDistributionDriverAttribute dbComposerAttribute = driverAttributeDao.findByPrimaryKey(
						DatabaseDistributionDriverAttribute.class, driverAttribute.getId());

				if (dbComposerAttribute != null) {
					dbComposerAttribute.setDatabaseFieldName(driverAttribute.getDatabaseFieldName());
					dbComposerAttribute.setUnifiedFieldName(driverAttribute.getUnifiedFieldName());
					dbComposerAttribute.setDataType(driverAttribute.getDataType());
					dbComposerAttribute.setDefualtValue(driverAttribute.getDefualtValue());
					/**
					 * MEDSUP-2029 Starts
					 */
					dbComposerAttribute.setPaddingEnable(driverAttribute.isPaddingEnable());	
					dbComposerAttribute.setLength(driverAttribute.getLength());	
					dbComposerAttribute.setPaddingType(driverAttribute.getPaddingType());	
					dbComposerAttribute.setPaddingChar(driverAttribute.getPaddingChar());	
					dbComposerAttribute.setPrefix(driverAttribute.getPrefix());	
					dbComposerAttribute.setSuffix(driverAttribute.getSuffix());
					/**
					 * MEDSUP-2029 Starts
					 */
					dbComposerAttribute.setLastUpdatedDate(new Date());
					dbComposerAttribute.setLastUpdatedByStaffId(staffId);
					dbComposerAttribute.setStatus(StateEnum.ACTIVE);

					driverAttributeDao.merge(dbComposerAttribute);
					driversDao.merge(dbComposerAttribute.getDbDisDriver());
					responseObject.setSuccess(true);
					responseObject.setObject(driverAttribute);
					responseObject.setResponseCode(ResponseCode.DRIVER_ATTRIBUTE_UPDATE_SUCCESS);

				} else {
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.DRIVER_ATTRIBUTE_UPDATE_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.DRIVER_ATTRIBUTE_UPDATE_FAIL);
			}
		} else {
			logger.info("duplicate attribute Database field found:" + driverAttribute.getDatabaseFieldName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DRIVER_DUPLICATE_ATTRIBUTE_FOUND);
		}
		return responseObject;

	}

	/**
	 * Delete Multiple driver Attributes
	 * 
	 * @param attributeId
	 * @param staffId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject deleteDriverAttributes(String attributeIds, int staffId) {
		ResponseObject responseObject = new ResponseObject();

		if (!StringUtils.isEmpty(attributeIds)) {
			String[] attributeIdList = attributeIds.split(",");

			for (int i = 0; i < attributeIdList.length; i++) {

				DriversService driversService = (DriversService) SpringApplicationContext.getBean("driversService");
				responseObject = driversService.deleteAttribute(Integer.parseInt(attributeIdList[i]), staffId);
			}

			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Method will delete selected attribute for any parser configuration.
	 * 
	 * @param attributeId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_DATABASE_DISTRIBUTION_DRIVER_ATTRIBUTE, actionType = BaseConstants.DELETE_ACTION,
			currentEntity = DatabaseDistributionDriverAttribute.class, ignorePropList = "")
	public ResponseObject deleteAttribute(int attributeId, int staffId) {
		ResponseObject responseObject = new ResponseObject();

		if (attributeId > 0) {
			DatabaseDistributionDriverAttribute driverAttribute = driverAttributeDao.findByPrimaryKey(DatabaseDistributionDriverAttribute.class,
					attributeId);
			if (driverAttribute != null) {
				int driverId = driverAttribute.getDbDisDriver().getId();
				List<DatabaseDistributionDriverAttribute> driverAttributeList = driverAttributeDao.getAllAttributeByDriverId(driverId);
				if (driverAttributeList != null && !driverAttributeList.isEmpty()) {
					for (int i = 0; i < driverAttributeList.size(); i++) {
						DatabaseDistributionDriverAttribute dbComposerAttribute = driverAttributeList.get(i);
						if (attributeId == (dbComposerAttribute.getId())) {
							dbComposerAttribute.setLastUpdatedDate(new Date());
							dbComposerAttribute.setLastUpdatedByStaffId(staffId);
							dbComposerAttribute.setStatus(StateEnum.DELETED);

							driverAttributeDao.merge(dbComposerAttribute);
							driversDao.merge(dbComposerAttribute.getDbDisDriver());
							responseObject.setSuccess(true);
							responseObject.setObject(driverAttribute);
							responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_SUCCESS);
						}
					}
				} else {
					logger.debug("Fail to fetch all attribute");
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Method will add or delete Database Distribution driver dependents.
	 * 
	 * @param driver
	 * @param isImport
	 * @return ResponseObject
	 */
	public ResponseObject importOrDeleteDatabaseDisDriverDependents(Drivers driver, boolean isImport) {
		ResponseObject responseObject;

		if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(driver.getDriverType().getAlias())) {
			DatabaseDistributionDriver databaseDistributionDriver = (DatabaseDistributionDriver) driver;
			responseObject = iterateDriversAttribute(driver, isImport);
			if (isImport) {
				responseObject.setSuccess(true);
				responseObject.setObject(databaseDistributionDriver);
			} else {
				databaseDistributionDriver.setLastUpdatedByStaffId(driver.getLastUpdatedByStaffId());
				responseObject.setSuccess(true);
				responseObject.setObject(databaseDistributionDriver);
			}
		} else {
			responseObject = new ResponseObject();
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	private ResponseObject iterateDriversAttribute(Drivers driver, boolean isImport) {
		ResponseObject responseObject = new ResponseObject();
		Date date = new Date();
		if (driver != null && EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(driver.getDriverType().getAlias())) {
			DatabaseDistributionDriver databaseDistributionDriver = (DatabaseDistributionDriver) driver;
			List<DatabaseDistributionDriverAttribute> attrList = databaseDistributionDriver.getAttributeList();
			if (attrList != null && !attrList.isEmpty()) {
				for (DatabaseDistributionDriverAttribute attribute : attrList) {
					if (isImport) {
						attribute.setId(0);
						attribute.setCreatedByStaffId(databaseDistributionDriver.getCreatedByStaffId());
						attribute.setCreatedDate(date);
						attribute.setLastUpdatedDate(date);
						attribute.setLastUpdatedByStaffId(databaseDistributionDriver.getCreatedByStaffId());
						attribute.setDbDisDriver(databaseDistributionDriver);
					}
				}
				databaseDistributionDriver.setAttributeList(attrList);
			} else {
				logger.debug("No attribute found for driver attribute " + databaseDistributionDriver.getName());
			}
		}
		return responseObject;
	}

	/**
	 * Method will check driver attribute name for unique source field name.
	 * 
	 * @param attributeId
	 * @param attributeName
	 * @param mappingId
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isAttributeUniqueForUpdate(int attributeId, String attributeName, int driverId) {
		DatabaseDistributionDriverAttribute driverAttribute = driverAttributeDao.checkUniqueAttributeNameForUpdate(driverId, attributeName);

		boolean isUnique;

		if (driverAttribute != null) {
			// If ID is same , then it is same attribute object
			if (attributeId == driverAttribute.getId()) {
				isUnique = true;
			} else { // It is another attribute object , but name is same
				isUnique = false;
			}
		} else { // No attribute found with same name
			isUnique = true;
		}
		return isUnique;
	}
	
	public int getServiceId(Service service) {
		if(service != null) {
			return service.getId();
		}
		return 0;
	}
	
	@Override
	public Drivers getDriverFromList(List<Drivers> driversList, String driverName) {
		if(!CollectionUtils.isEmpty(driversList)) {
			int length = driversList.size();
			for(int i = length-1; i >= 0; i--) {
				Drivers driver = driversList.get(i);
				if(driver != null && !driver.getStatus().equals(StateEnum.DELETED) && driver.getName().equalsIgnoreCase(driverName)) {
					return driversList.remove(i);
				}
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public List<DatabaseDistributionDriverAttribute> getAttributeListByDriverId(int driverId) {
        
		logger.debug("Fetching all attribute list for database distribution driver by driverId: " + driverId);
		
		return driverAttributeDao.getAllAttributeByDriverId(driverId);
	}


	
	/**
	 * this method is added for audit log for database distribution service
	 */
	
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_DRIVER, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Drivers.class,
			ignorePropList = "attributeList")
	@Override
	public ResponseObject updateDataBaseDistributionDriverConfiguration(DistributionDriver distributionDriver) {
		ResponseObject responseObject = new ResponseObject();

		if (distributionDriver != null) {
			Service service = servicesService.getServiceandServerinstance(distributionDriver.getService().getId());
			if (service != null) {
				logger.debug("Updating: Database distributioin driver configuration.");
				DatabaseDistributionDriver databaseDistributionDriver = (DatabaseDistributionDriver) distributionDriver;
				databaseDistributionDriver.setLastUpdatedDate(new Date());
				databaseDistributionDriver.setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
				databaseDistributionDriver.setService(service);
				driversDao.merge(databaseDistributionDriver);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_SUCCESS);
				responseObject.setObject(databaseDistributionDriver);

			} else {
				logger.debug("Failed to update driver configuration due to object found null.");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
			}
		} else {
			logger.debug("Failed to update driver configuration due to object found null.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FTP_DRIVER_CONFIG_UPDATE_FAIL);
		}
		return responseObject;
	}
	
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_DRIVER, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Drivers.class, ignorePropList = "attributeList,applicationOrder")
	@Override
	public ResponseObject updateDatabaseDistributionDriver(DistributionDriver distributionDriver) {

		ResponseObject responseObject = new ResponseObject();
		if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(distributionDriver.getDriverType().getAlias())) {
			logger.debug("inside update distribution : Database Distribution Driver Instance found");
			DatabaseDistributionDriver databaseDistributionDriver = driversDao
					.getDatabaseDistributionDriverById(distributionDriver.getId());
			databaseDistributionDriver.setName(distributionDriver.getName());
			databaseDistributionDriver.setTimeout(distributionDriver.getTimeout());
			databaseDistributionDriver.setStatus(distributionDriver.getStatus());
			databaseDistributionDriver.setLastUpdatedByStaffId(distributionDriver.getLastUpdatedByStaffId());
			databaseDistributionDriver.setLastUpdatedDate(new Date());
			driversDao.merge(databaseDistributionDriver);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DRIVER_UPDATE_SUCCESS);
			responseObject.setObject(databaseDistributionDriver);
		} else {
			logger.debug("inside updateDistributionDriver : duplicate driver name found in update:"
					+ distributionDriver.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DRIVER_NAME);
		}
		return responseObject;
	}
	private boolean match(String regex, String value){
		String newValue = "";
		if(value != null){
			newValue = value;
		}
    	Pattern pattern = Pattern.compile(regex,Pattern.UNICODE_CHARACTER_CLASS);
    	//logger.info("pattern: " + pattern);
    	Matcher matcher = pattern.matcher(newValue);
    	return !matcher.matches();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject testFtpSftpConnection(ConnectionParameter connectionParameter,int maxRetrycount,String ipAddress,int port,String driverType){
		ResponseObject responseObject = new ResponseObject();
		//JMX call 
		RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(ipAddress, port,3,5000,20);
		String crestelHome = serverMgmtRemoteJMXCall.getCrestelPEngineHome();
		if(crestelHome!=null) {
			if(connectionParameter.getiPAddressHost().contains(",")) {
				List<HostParameters> hosts=connectionParameter.getiPAddressHostList();
				String msg = "";
				Iterator<HostParameters> hostIterator=hosts.iterator();
				while(hostIterator.hasNext()) {
					String host=hostIterator.next().getiPAddressHost();
					if(host!=null && !host.isEmpty()) {
						if(!serverMgmtRemoteJMXCall.testFtpSftpConnection(host,connectionParameter.getPort(),connectionParameter.getUsername(),connectionParameter.getPassword(),connectionParameter.getKeyFileLocation(),maxRetrycount,driverType)){
							if(hostIterator.hasNext()) msg+=host + ","; else msg+=host;
						}
					}
				}
				if(msg!=null && !msg.isEmpty()) {
					responseObject.setMsg(msg);
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FTP_SFTP_TEST_CONNECTION_FAILURE);		
				}else {
					   responseObject.setSuccess(true);
					   responseObject.setResponseCode(ResponseCode.FTP_SFTP_TEST_CONNECTION_SUCCESS);					
				}
			}else {
				Boolean isConnected=serverMgmtRemoteJMXCall.testFtpSftpConnection(connectionParameter.getiPAddressHost(),connectionParameter.getPort(),connectionParameter.getUsername(),connectionParameter.getPassword(),connectionParameter.getKeyFileLocation(),maxRetrycount,driverType);
				if(isConnected){
				   responseObject.setSuccess(true);
				   responseObject.setResponseCode(ResponseCode.FTP_SFTP_TEST_CONNECTION_SUCCESS);
				}else{
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FTP_SFTP_TEST_CONNECTION_FAILURE);
				}
			}
						
		}else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.JMX_FAILURE);
		}
		return responseObject;
	}

}
