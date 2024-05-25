package com.elitecore.sm.license.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.license.model.HourlyCDRCount;
import com.elitecore.sm.license.model.License;

public interface LicenseService {

	public ResponseObject getServerManagerLicenseDetails();

	public ResponseObject applyFullLicense(File file, String productTypes, String repositoryPath) ;
	
	public boolean clearLicenseDetails(Map<String, String> licenseHashMap) throws SMException;

	public ResponseObject createTrialLicense(License license, String repositoryPath, String systemPath);

	public boolean updateLicenseDetails(Map<String, String> licenseHashMap);
	
	public ResponseObject checkLicenseDetails(String repositoryPath) throws SMException;
	
	public ResponseObject getLicenseByServerId(String serverId);
	
	public ResponseObject validateLicenseDetails(String repositoryPath);
	
	public ResponseObject applyEngineFullLicense(File file, String productTypes, String repositoryPath, int serverInstanceId) throws  IOException;
	
	public ResponseObject applyEngineFullLicense(byte[] fileData, int serverInstanceId) throws  IOException;
	
	public Map<String, String> licenseFullInfo(String serverInstanceIP, int port);
	
	public Map<String, String> serverIdDetails(String serverInstanceIP,int port);
	
	public ResponseObject getLicenseDetailsByServerInstance(int serverInstanceId);
	
	public int countLicenseDays(String endDate);
	
	public ResponseObject getLicenseDetailsByComponentType();
	
	public List<License> getPaginatedList(int startIndex, int limit, String sidx, String sord);
	
	public long getLicenseDetailsCount();
	
	public ResponseObject getEngineLicenseDetailsByInstance(int serverTypeId);
	
	public boolean checkSystemFile(String repositoryPath);
	
	public ResponseObject getLicenseDetailsByInstanceId(int serverInstanceId);
	
	public ResponseObject updateLicenseDetails(License license);
	
	public ResponseObject getVersionDetails();
	
	public ResponseObject upgradeEngineDefaultLicense(File file, String repositoryPath, int serverInstanceId) throws  IOException;
	
	public ResponseObject applyNewLicenseKeySM(String repositoryPath);
	
	public ResponseObject upgradeContainerLicense(File file, String repositoryPath, String hostIP, String containerLicPath);
	
	public int getLicensedContainerForHost(String repositoryPath , String containerLicPath);
	
	public Map<Date, Long> getHourWiseTotalUtilizationMap();
	
	public Map<Date, Long> getHourWiseCurrentUtilizationMap();
	
	public List<HourlyCDRCount> getHourlyCDRDCountByProcessDate(Date processDate);
	
	public long getMaxLicneseUtilizationCount(Map<Date, Long> map);
	
	public long getCurrentLicenseUtilizationCountFromMap(Map<Date, Long> map);
	
	public void updateLicenseUtilizationMap(List<HourlyCDRCount> list);

	public ResponseObject getCircleList();
	
	public ResponseObject getLicenseList();

	public ResponseObject getCircleDetailsById(int id);
	
	public ResponseObject getLicenseByCircleId(int circleId);
	
	public ResponseObject getLicenseUtilization(License license);
	
	public ResponseObject getLicenseUtilizationByCircle(License license);
		
	public ResponseObject getLicenseUtilizationByDevice(License license);
	
}
