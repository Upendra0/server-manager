package com.elitecore.sm.drivers.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriver;
import com.elitecore.sm.drivers.model.DistributionDriver;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.model.FTPCollectionDriver;
import com.elitecore.sm.drivers.model.FTPDistributionDriver;
import com.elitecore.sm.drivers.model.LocalCollectionDriver;
import com.elitecore.sm.drivers.model.LocalDistributionDriver;
import com.elitecore.sm.drivers.model.SFTPCollectionDriver;
import com.elitecore.sm.drivers.model.SFTPDistributionDriver;


/**
 * 
 * @author avani.panchal
 *
 */
public interface DriversDao extends GenericDAO<Drivers>{

	
	public int getDriverCount(String driverName, int serviceId);
	
	public int getDriverCountByServiceId(int serviceId);
	
	public List<Drivers> getDriverByServiceId(int serviceId);
	
	public FTPCollectionDriver getFTPCollectionDriverById(int driverId);
	
	public FTPDistributionDriver getFTPDistributionDriverById(int driverId);
	
	public SFTPCollectionDriver getSFTPCollectionDriverById(int driverId);
	
	public SFTPDistributionDriver getSFTPDistributionDriverById(int driverId);
	
	public List<Drivers> getDriverListByName(String driverName);

	public Map<String, Object> getDriversPaginatedList(int serviceInstanceId);
	
	public List<CollectionDriver> getDriversPaginatedList(Class<CollectionDriver> classInstance, List<Criterion> conditions, Map<String,String> aliases, int offset,int limit,String sortColumn,String sortOrder);
	
	public List<DistributionDriver> getDistributionDriversPaginatedList(Class<DistributionDriver> classInstance, List<Criterion> conditions, Map<String,String> aliases, int offset,int limit,String sortColumn,String sortOrder);
	
	public List<CollectionDriver> getAllCollectionDriverDetails(List<CollectionDriver> collectionDriverList);
	
	public List<DistributionDriver> getAllDistributionDriverDetails(List<DistributionDriver> distributionDriverList);
	
	public LocalCollectionDriver getLocalCollectionDriverById(int driverId);

	public LocalDistributionDriver getLocalDistributionDriverById(int driverId);
	
	public void irerateOverCollectionDriver(CollectionDriver driver);
	
	public List<Object[]>  getDistributionDriverComposerList(int driverId);
	
	public void irerateOverDistributionDriver(DistributionDriver driver);

	public DatabaseDistributionDriver getDatabaseDistributionDriverById(int driverId);
	
	public int getTotalDriverCount();
	
	public int getMaxApplicationOrderForDriver(int serviceId);
	
	public boolean updateDriverType(int driverId, String updatedDriverType);
	
}
