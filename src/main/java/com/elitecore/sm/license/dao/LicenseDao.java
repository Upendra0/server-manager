package com.elitecore.sm.license.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.license.model.HourlyCDRCount;
import com.elitecore.sm.license.model.License;


public interface LicenseDao extends GenericDAO<License> {

	public  License getServerManagerLicenseDetails ();

	public License getLicenseByServerId(String serverId,String applicationPath);
	
	public List<License> getAllServerInstancesByHost(String ipAdress);
	
    public License getLicenseDetailsByServerInstanceId(int serverInstanceId);
	
    public License getLicenseDetailsByComponentType();
	
	public List<License> getLicensePaginatedList(Class<License> classInstance,int offset, int limit, String sortColumn, String sortOrder);

	public Map<String, Object> getLicenseDetailsCount();
	
	public License getLicenseDetailsByInstanceId(int serverInstanceId);
	
	public List<Object[]> getVersionData(); // To display version details for current license view from about us page.
	
	public List<License> getAllServerInstancesByHostContainerEnv(String ipAdress, int utilityPort, boolean isContainer);
	
	public  License getSMLicenseDetails();
	
	public Long getLicenseCount(Class<License> classInstance);
	
	public Map<Date, Long> getHourWiseTotalUtilizationMap();
	
	public Map<Date, Long> getHourWiseCurrentUtilizationMap();
	
	public List<HourlyCDRCount> getHourlyCDRDCountByProcessDate(Date processDate);
	
	public License getLicenseDetailsByCircleId(int circleId);
	
	public Long getCurrentLicenseUtilizationByCircle(int circleId);

	public Map<String, Long> getCurrentLicenseUtilizationByCircleAndDevice(int circleId);
	
	public boolean isLicenseAppliedToCircle(int circleId);
	
	public Long getMaxLicenseUtilizationByCircle(int circleId);
	
	public Map<String, Long> getMaxLicenseUtilizationByCircleAndDevice(int circleId);
}
