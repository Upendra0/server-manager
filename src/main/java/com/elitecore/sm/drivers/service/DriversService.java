package com.elitecore.sm.drivers.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.model.ConnectionParameter;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriverAttribute;
import com.elitecore.sm.drivers.model.DistributionDriver;
import com.elitecore.sm.drivers.model.DriverCategory;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.services.model.Service;

/**
 * 
 * @author avani.panchal
 *
 */
public interface DriversService{
	
	public ResponseObject addDriver(Drivers driver);
	
	public ResponseObject updateCollectionDriver(CollectionDriver driver);
	
	public ResponseObject updateDistributionDriver(DistributionDriver driver);
	
	public ResponseObject importOrDeleteDriverConfig(Drivers driver,boolean isImport);
	
	public ResponseObject addDriverType(DriverType driverType);
	
	public ResponseObject getAllDriverTypeList(DriverCategory driverCategory);
	
	public ResponseObject addCollectionDriver(CollectionDriver collectionDriver);
	
	public ResponseObject getDriversByServiceId(int serviceId);
	
	public List<DriverType> getDriverTypeList(DriverCategory driverCategory);
	
	public ResponseObject getDriverByTypeAndId(int driverId,String driverTypeAlias);
	
	public ResponseObject updateCollectionDriverConfiguration(CollectionDriver collectionDriver);
	
	public ResponseObject updateDistributionDriverConfiguration(DistributionDriver collectionDriver);
	
	public ResponseObject updateDriversApplicationOrder(String driverOrderList) throws CloneNotSupportedException;
	
	public Drivers getDriverById(int driverId);
	
	public List<Map<String, Object>> getDriversPaginatedList(int serviceInstanceId,int startIndex, int limit,String sidx, String sord);

	public long getDriversTotalCount(int serviceInstanceId);
	
	public List<CollectionDriver> getCollectionDriverDetails(List<CollectionDriver> collectionDriverList);
	
	public ResponseObject deleteDriverDetails(int driverId,int serviceId);
	
	public ResponseObject updateDriverStatus(int driverId, String driverType , String driverStatus) throws CloneNotSupportedException;
	
	public List<ImportValidationErrors> validateDriverForImport(Drivers driver,List<ImportValidationErrors> driverImportErrorList);
	
	public void  iterateOverDriverConfig(Drivers driver,boolean isImport);
	
	public List<Map<String, Object>> getDistributionDriverPaginatedList(int serviceInstanceId,int startIndex, int limit,String sidx, String sord);
	
	public ResponseObject getAllDistributionPluginbyDriverId(int driverId);
	
	public ResponseObject getDistributionDriverByIdAndType(int driverId,String driverTypeAlias);
	
	public ResponseObject createDistributionDriver(DistributionDriver ftpDistributionDriver);
	
	public void iterateServiceDriverDetails(Service exportedService,boolean isImport );
	
	public ResponseObject getDriverTypeByAlias(String driverTypeAlias);
	
	public ResponseObject updateDriversApplicationOrder(Drivers driver, JSONObject driverDetail);

	public String decryptData(String pwd);
	
	public ResponseObject createDriverAttributes(DatabaseDistributionDriverAttribute driverAttribute, int staffId) ;
	
	public long getAttributeListCountByDriverId(int driverId);
	
	public List<DatabaseDistributionDriverAttribute> getPaginatedList(int driverId, int startIndex, int limit, String sidx, String sord);
	
	public List<Map<String, Object>> getAttributeMap(List<DatabaseDistributionDriverAttribute> resultList);
	
	public ResponseObject updateDriverAttributes(DatabaseDistributionDriverAttribute driverAttribute, int staffId);
	
	public ResponseObject deleteDriverAttributes(String attributeIds, int staffId) ;
	
	public ResponseObject deleteAttribute(int attributeId, int staffId) ;
	
	public void importDriverConfigAddAndKeepBothMode(Drivers exportedDriver, int importMode);
	
	public void importServiceDriverAddAndKeepBothMode(Service exportedService, int importMode);
	
	public void importDriverForUpdateMode(Drivers dbDriver, Drivers exportedDriver, int importMode) ;
	
	public void importServiceDriverUpdateMode(Service serviceDb, Service exportedService, int importMode);
	
	public void saveDriverForImport(Drivers driver, Service service, int importMode);
	
	public Drivers getDriverFromList(List<Drivers> driversList, String driverName);

	public List<DatabaseDistributionDriverAttribute> getAttributeListByDriverId(int driverId);

	public ResponseObject updateDataBaseDistributionDriverConfiguration(DistributionDriver driver);

	public ResponseObject updateDatabaseDistributionDriver(DistributionDriver distributionDriver); 
	
	public ResponseObject uploadDriverAttributesFromCSV(File csvFile, int driverId, int staffId);

	public ResponseObject testFtpSftpConnection(ConnectionParameter connectionParameter,int maxRetrycount,String ipAddress,int port,String driverType);

}
