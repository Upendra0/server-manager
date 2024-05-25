package com.elitecore.sm.drivers.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriver;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriverAttribute;
import com.elitecore.sm.drivers.model.DistributionDriver;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.model.FTPCollectionDriver;
import com.elitecore.sm.drivers.model.FTPDistributionDriver;
import com.elitecore.sm.drivers.model.LocalCollectionDriver;
import com.elitecore.sm.drivers.model.LocalDistributionDriver;
import com.elitecore.sm.drivers.model.SFTPCollectionDriver;
import com.elitecore.sm.drivers.model.SFTPDistributionDriver;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.services.dao.ServicesDao;


/**
 * 
 * @author avani.panchal
 *
 */
@Repository(value="driversDao")
public class DriversDaoImpl extends GenericDAOImpl<Drivers> implements DriversDao{
	
	@Autowired
	private ServicesDao servicesDao;
	
	@Autowired
	private PathListDao pathListDao;
	
	/**
	 * Mark service and server instance dirty , then update driver
	 */
	@Override
	public void update (Drivers driver){
		
		servicesDao.merge(driver.getService());
		
		getCurrentSession().merge(driver);
		
	}
	
	/**
	 * Mark service and server instance dirty , then save driver
	 */
	@Override
	public void save(Drivers driver){
		
		servicesDao.merge(driver.getService());
		
		getCurrentSession().save(driver);
	}
	
	/**
	 * Mark service and server instance dirty , then update driver
	 */
	@Override
	public void merge(Drivers driver){
		logger.debug("Going to mark dirty flag for service");
		logger.info("Service object is :: " + driver.getService().getId());
		//openCurrentSession().beginTransaction();
		getCurrentSession().merge(driver);
		servicesDao.merge(driver.getService());
	}
	
	/**
	 * Fetch Driver count based on name and service id, for check unique driver name per service wise
	 */
	@Override
	public int getDriverCount(String driverName, int serviceId){
				
		Criteria criteria = getCurrentSession().createCriteria(Drivers.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (driverName != null) {
			criteria.add(Restrictions.eq("name", driverName).ignoreCase());
		} 
		if(serviceId > 0) {
			criteria.add(Restrictions.eq("service.id", serviceId));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}
	
	/**
	 * Count number of driver in same service
	 */
	@Override
	public int getDriverCountByServiceId(int serviceId){
		
		Criteria criteria = getCurrentSession().createCriteria(Drivers.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (serviceId != 0) {
			criteria.add(Restrictions.eq("service.id", serviceId));
		} 
		
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}
	
	@Override
	public int getMaxApplicationOrderForDriver(int serviceId) {
		Criteria criteria = getCurrentSession().createCriteria(Drivers.class);
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.setProjection(Projections.max("applicationOrder"));
		Object object = criteria.uniqueResult();
		if(object != null) {
			return ((Number) object).intValue();
		}
		return -1;
	}
	
	
	@Override
	public int getTotalDriverCount() {
		Criteria criteria = getCurrentSession().createCriteria(Drivers.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	/**
	 * Fetch List Of Collection Driver
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Drivers> getDriverByServiceId(int serviceId){
		
		Criteria criteria =getCurrentSession().createCriteria(Drivers.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("service.id",serviceId));
		criteria.addOrder(Order.asc("applicationOrder"));
		
		 List<Drivers> driverList = (List<Drivers>)criteria.list();
		 for(Drivers driver:driverList){
			 driver.getDriverType().getAlias();
		}
		return (List<Drivers>)criteria.list();
		
	}

	/**
	 * Fetch FTP Collection Driver Dependents
	 * @param driverId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public FTPCollectionDriver getFTPCollectionDriverById(int driverId){
		
		FTPCollectionDriver ftpDriver=null;
		Criteria criteria=getCurrentSession().createCriteria(FTPCollectionDriver.class);
		criteria.add(Restrictions.eq("id", driverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<FTPCollectionDriver> ftpCollectionDriverList=(List<FTPCollectionDriver>)criteria.list();
		
		if(ftpCollectionDriverList!=null && !ftpCollectionDriverList.isEmpty()){
			ftpDriver=ftpCollectionDriverList.get(0);
			ftpDriver.getDriverType().getAlias();
			ftpDriver.getFileGroupingParameter().getGroupingType();
			ftpDriver.getFtpConnectionParams().getFileSeparator();
			ftpDriver.getMyFileFetchParams().getFileFetchIntervalMin();
			ftpDriver.getService().isEnableDBStats();
		}
		
		return ftpDriver;
	}
	
	
	/**
	 * Method will fetch ftp distribution driver by driver id.
	 * @param driverId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public FTPDistributionDriver getFTPDistributionDriverById(int driverId){
		
		logger.debug("Going to fetch FTP Distribution driver for driver id "  + driverId);
		FTPDistributionDriver ftpDriver = null;
		
		Criteria criteria = getCurrentSession().createCriteria(FTPDistributionDriver.class);
		criteria.add(Restrictions.eq("id", driverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<FTPDistributionDriver> ftpDistributionDriverList = (List<FTPDistributionDriver>)criteria.list();
		
		if(ftpDistributionDriverList != null && !ftpDistributionDriverList.isEmpty()){
			logger.info("FTP Distribution driver found successfully.");
			ftpDriver = ftpDistributionDriverList.get(0);
			ftpDriver.getDriverType().getAlias();
			ftpDriver.getFtpConnectionParams().getFileSeparator();
			ftpDriver.getService().isEnableDBStats();
		}else{
			logger.info("Failed to get FTP distribution driver.");
		}
		return ftpDriver;
	}
	
	
	/**
	 * Method will fetch ftp distribution driver by driver id.
	 * @param driverId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SFTPDistributionDriver getSFTPDistributionDriverById(int driverId){
		
		logger.debug("Going to fetch SFTP Distribution driver for driver id "  + driverId);
		SFTPDistributionDriver sftpDriver = null;
		
		Criteria criteria = getCurrentSession().createCriteria(SFTPDistributionDriver.class);
		criteria.add(Restrictions.eq("id", driverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<SFTPDistributionDriver> sftpDistributionDriverList = (List<SFTPDistributionDriver>)criteria.list();
		
		if(sftpDistributionDriverList != null && !sftpDistributionDriverList.isEmpty()){
			logger.info("FTP Distribution driver found successfully.");
			sftpDriver = sftpDistributionDriverList.get(0);
			sftpDriver.getDriverType().getAlias();
			sftpDriver.getFtpConnectionParams().getFileSeparator();
			sftpDriver.getService().isEnableDBStats();
		}else{
			logger.info("Failed to get SFTP distribution driver.");
		}
		
		return sftpDriver;
	}
	
	/**
	 * Method will fetch SFTP Collection Driver details .
	 * @param driverId
	 * @return SFTPCollectionDriver object
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SFTPCollectionDriver getSFTPCollectionDriverById(int driverId){
		
		SFTPCollectionDriver sftpDriver=null;
		Criteria criteria=getCurrentSession().createCriteria(SFTPCollectionDriver.class);
		criteria.add(Restrictions.eq("id", driverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<SFTPCollectionDriver> sftpCollectionDriverList = (List<SFTPCollectionDriver>)criteria.list();
		
		if(sftpCollectionDriverList != null && !sftpCollectionDriverList.isEmpty()){
			sftpDriver = sftpCollectionDriverList.get(0);
			sftpDriver.getDriverType().getAlias();
			sftpDriver.getFileGroupingParameter().getGroupingType();
			sftpDriver.getFtpConnectionParams().getFileSeparator();
			sftpDriver.getMyFileFetchParams().getFileFetchIntervalMin();
			sftpDriver.getService().isEnableDBStats();
		}
		
		return sftpDriver;
	}
	
	/**
	 * Method will fetch all collection driver details like ftpcollection drivers and its connection parameters etc.
	 * @param collectionDriverList
	 * @return List<CollectionDriver>
	 */
	@Override
	public List<CollectionDriver> getAllCollectionDriverDetails(List<CollectionDriver> collectionDriverList){
		if(!collectionDriverList.isEmpty()){
			for(CollectionDriver driver:collectionDriverList){
				if(EngineConstants.FTP_COLLECTION_DRIVER.equals(driver.getDriverType().getAlias()) || EngineConstants.SFTP_COLLECTION_DRIVER.equals(driver.getDriverType().getAlias())){
					FTPCollectionDriver ftpCollectionDriver = (FTPCollectionDriver) driver;

					ftpCollectionDriver.getFtpConnectionParams().getiPAddressHost();
					ftpCollectionDriver.getMyFileFetchParams().getFileFetchIntervalMin();
					
				}
			}
		}
		return collectionDriverList;
	}
	
	/**
	 * Method will fetch all collection driver details like ftpcollection drivers and its connection parameters etc.
	 * @param collectionDriverList
	 * @return List<CollectionDriver>
	 */
	@Override
	public List<DistributionDriver> getAllDistributionDriverDetails(List<DistributionDriver> distributionDriverList){
		if(!distributionDriverList.isEmpty()){
			for(DistributionDriver driver:distributionDriverList){
				if(EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(driver.getDriverType().getAlias()) || EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(driver.getDriverType().getAlias())){
					
					FTPDistributionDriver ftpDistributionDriver = (FTPDistributionDriver) driver;
					ftpDistributionDriver.getFtpConnectionParams().getiPAddressHost();
					
				}
			}
		}
		return distributionDriverList;
	}
	
	/**
	 * Fetch driver count and object by name 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Drivers> getDriverListByName(String driverName){
		
		Criteria criteria=getCurrentSession().createCriteria(Drivers.class);
		criteria.add(Restrictions.eq("name", driverName).ignoreCase());
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return (List<Drivers>)criteria.list();
	}

	/**
	 * Method will fetch all drivers by service instance id.
	 * @param servicInstanceId
	 * @return Map
	 */
	@Override
	public Map<String, Object> getDriversPaginatedList(int serviceInstanceId) {
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditions = new ArrayList<>();

		if (serviceInstanceId !=0) {
			aliases.put("service", "s");
			conditions.add(Restrictions.eq("s.id",serviceInstanceId));
		}
		conditions.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);

		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CollectionDriver> getDriversPaginatedList(Class<CollectionDriver> classInstance, List<Criterion> conditions,
														 Map<String, String> aliases, int offset, int limit,
														 String sortColumn, String sortOrder) {
		
		List<CollectionDriver> resultList = new ArrayList<>();
		Criteria criteria = getCurrentSession().createCriteria(classInstance);
	
		logger.debug("Sort column ="+sortColumn);
		
		if ("desc".equalsIgnoreCase(sortOrder)) {
			if ("descriminatorValue".equalsIgnoreCase(sortColumn)){
				criteria.createAlias("driverType", "driverType").addOrder(Order.desc("driverType.type"));
			}else if (sortColumn.equals("applicationOrder")){
				criteria.addOrder(Order.desc("applicationOrder"));
			}else{
				criteria.addOrder(Order.desc(sortColumn));
			}
		} else if (sortOrder.equalsIgnoreCase("asc")) {
			if (sortColumn.equals("descriminatorValue")){
				criteria.createAlias("driverType", "driverType").addOrder(Order.asc("driverType.type"));
			}else if (sortColumn.equals("applicationOrder")){
				criteria.addOrder(Order.asc("applicationOrder"));
			}else{
				criteria.addOrder(Order.asc(sortColumn));
			}	
		}

		if(conditions != null){
			for(Criterion condition : conditions){
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		if(aliases!=null){
			for (Entry<String, String> entry : aliases.entrySet()){
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);//first record is 2
		criteria.setMaxResults(limit);//records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DistributionDriver> getDistributionDriversPaginatedList(Class<DistributionDriver> classInstance, List<Criterion> conditions,
														 Map<String, String> aliases, int offset, int limit,
														 String sortColumn, String sortOrder) {
		
		Criteria criteria = getCurrentSession().createCriteria(classInstance);
	
		logger.debug("Sort column ="+sortColumn);
		
		if ("desc".equalsIgnoreCase(sortOrder)) {
			if ("descriminatorValue".equalsIgnoreCase(sortColumn)){

			criteria.createAlias("driverType", "driverType").addOrder(Order.desc("driverType.type"));
			
			}else if ("applicationOrder".equalsIgnoreCase(sortColumn)){
				criteria.addOrder(Order.desc("applicationOrder"));
			}else if (BaseConstants.DRIVER_NAME.equalsIgnoreCase(sortColumn)){
				criteria.addOrder(Order.desc("name"));
			}else{
				criteria.addOrder(Order.desc(sortColumn));
			}
		} else if ("asc".equalsIgnoreCase(sortOrder)) {
			if ("descriminatorValue".equalsIgnoreCase(sortColumn)){
				
			criteria.createAlias("driverType", "driverType").addOrder(Order.asc("driverType.type"));
			
			}else if ("applicationOrder".equalsIgnoreCase(sortColumn)){
				criteria.addOrder(Order.asc("applicationOrder"));
			}else if (BaseConstants.DRIVER_NAME.equals(sortColumn)){
				criteria.addOrder(Order.asc("name"));
			}else{
				criteria.addOrder(Order.asc(sortColumn));
			}	
		}

		if(conditions != null){
			for(Criterion condition : conditions){
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		if(aliases!=null){
			for (Entry<String, String> entry : aliases.entrySet()){
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);//first record is 2
		criteria.setMaxResults(limit);//records from 2 to (2+3) 5
		return criteria.list();
	}
	
	
	
	/**
	 * Fetch Local Collection Driver by driver id
	 */
	@SuppressWarnings("unchecked")
    @Override
	public LocalCollectionDriver getLocalCollectionDriverById(int driverId){
		
		LocalCollectionDriver localDriver=null;
		Criteria criteria=getCurrentSession().createCriteria(LocalCollectionDriver.class);
		criteria.add(Restrictions.eq("id", driverId));
		
		List<LocalCollectionDriver> localCollectionDriverList=(List<LocalCollectionDriver>)criteria.list();
		
		if(localCollectionDriverList!=null && !localCollectionDriverList.isEmpty()){
			localDriver=localCollectionDriverList.get(0);
			localDriver.getDriverType().getAlias();
			localDriver.getFileGroupingParameter().getGroupingType();
			localDriver.getService().isEnableDBStats();
		}
		return localDriver;

	}
	
	
	/**
	 * @see Method will get local distribution driver by driver id.
	 * @param driverId
	 * @return LocalDistributionDriver
	 */
	@SuppressWarnings("unchecked")
    @Override
	public LocalDistributionDriver getLocalDistributionDriverById(int driverId){
		logger.debug("Fetching local distribution driver for driver id : " + driverId);
		LocalDistributionDriver localDriver=null;
		Criteria criteria=getCurrentSession().createCriteria(LocalDistributionDriver.class);
		criteria.add(Restrictions.eq("id", driverId));
		
		List<LocalDistributionDriver> localDistributionDriverList=(List<LocalDistributionDriver>)criteria.list();
		
		if(localDistributionDriverList!=null && !localDistributionDriverList.isEmpty()){
			localDriver = localDistributionDriverList.get(0);
			localDriver.getDriverType().getAlias();
			localDriver.getService().isEnableDBStats();
		}
		return localDriver;
	}
	
	/**
	 * Iterate over collection driver and fetch its dependents
	 */
	@Override
	public void irerateOverCollectionDriver(CollectionDriver driver){
		
		if(driver!=null){
			
			Hibernate.initialize(driver.getFileGroupingParameter());
			
			if(driver.getDriverPathList()!=null){
				Hibernate.initialize(driver.getDriverPathList());
			}
			
			List<PathList> driverPathList = driver.getDriverPathList();
			if(driverPathList != null && !driverPathList.isEmpty()) {
				int pathListLength = driverPathList.size();
				for(int i = pathListLength-1; i >= 0; i--) {
					PathList path = driverPathList.get(i);
					if(path != null && StateEnum.DELETED.equals(path.getStatus())) {
						driverPathList.remove(i);
					}else {
						pathListDao.iterateOverCollectionPathList((CollectionDriverPathList)path);
					}
				}
			}
			driver.setDriverPathList(driverPathList);
			
			if(driver instanceof SFTPCollectionDriver){
				logger.debug("Going to fetch SFTPCollectionDriver , ftp connection param and file fetch param " );
				Hibernate.initialize(((SFTPCollectionDriver) driver).getFtpConnectionParams());
				Hibernate.initialize(((SFTPCollectionDriver) driver).getMyFileFetchParams());
			}else if (driver instanceof FTPCollectionDriver){
				logger.debug("Going to fetch FTPCollectionDriver , ftp connection param and file fetch param " );
				Hibernate.initialize(((FTPCollectionDriver) driver).getFtpConnectionParams());
				Hibernate.initialize(((FTPCollectionDriver) driver).getMyFileFetchParams());
			}
		}
	}
	
	/**
	 * Iterate over collection driver and fetch its dependants
	 * @param driver
	 * @return Drivers
	 */
	@Override
	public void irerateOverDistributionDriver(DistributionDriver driver){
		
		if(driver!=null){
			
			if(driver instanceof SFTPDistributionDriver){
				logger.debug("Going to fetch SFTPDistributionDriver , ftp connection param " );
				Hibernate.initialize(((SFTPDistributionDriver) driver).getFtpConnectionParams());
			}else if (driver instanceof FTPDistributionDriver){
				logger.debug("Going to fetch FTPDistributionDriver , ftp connection param " );
				Hibernate.initialize(((FTPDistributionDriver) driver).getFtpConnectionParams());
			}else if (driver instanceof DatabaseDistributionDriver){
				Hibernate.initialize(((DatabaseDistributionDriver) driver).getAttributeList());
				List<DatabaseDistributionDriverAttribute> driverAttributes=((DatabaseDistributionDriver) driver).getAttributeList();
				Iterator<DatabaseDistributionDriverAttribute> itr = driverAttributes.iterator();
				while(itr.hasNext()){
					DatabaseDistributionDriverAttribute driverAttr = itr.next();
					if(StateEnum.DELETED.equals(driverAttr.getStatus())){
						itr.remove();
					}
				}
				((DatabaseDistributionDriver) driver).setAttributeList(driverAttributes);
			}
			if(driver.getDriverPathList()!=null){
				List<PathList> distributionPathList=driver.getDriverPathList();
				Iterator<PathList> itr = distributionPathList.iterator();
				while(itr.hasNext()){
					PathList pathlist = itr.next();
					if(!StateEnum.DELETED.equals(pathlist.getStatus()) &&  pathlist instanceof DistributionDriverPathList){
					
							logger.debug("Going to fetch DistributionDriverPathList ,detail" );
							pathListDao.iterateOverDistributionPathList((DistributionDriverPathList)pathlist);
					
					}else{
						itr.remove();
					}
				}
				driver.setDriverPathList(distributionPathList);
			}
		}
	}
	
	/**
	 * Method will get All distribution driver plug-in list.
	 * @param driverId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDistributionDriverComposerList(int driverId) {
		
		StringBuilder hqlQueryString = new StringBuilder();
		
		//Please do not change order of select column fields as its used in front end.
		hqlQueryString.append("SELECT comp.id,comp.name,comp.composerType.type as type,pathlist.name, comp.composerType.alias as alias FROM Composer as comp "); 
		hqlQueryString.append("JOIN comp.myDistDrvPathlist as pathlist ON  pathlist.status=? ");
		hqlQueryString.append("JOIN  pathlist.driver as driver WHERE driver.id=? AND  comp.status=?  ORDER BY comp.id ");
		
		Query query = getCurrentSession().createQuery(hqlQueryString.toString());
		query.setString(0, String.valueOf(StateEnum.ACTIVE));
		query.setInteger(1,driverId);
		query.setString(2, String.valueOf(StateEnum.ACTIVE));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public DatabaseDistributionDriver getDatabaseDistributionDriverById(
			int driverId) {
		logger.debug("Fetching database distribution driver for driver id : " + driverId);
		DatabaseDistributionDriver databaseDriver=null;
		Criteria criteria=getCurrentSession().createCriteria(DatabaseDistributionDriver.class);
		criteria.add(Restrictions.eq("id", driverId));
		
		List<DatabaseDistributionDriver> databaseDistributionDriverList=(List<DatabaseDistributionDriver>)criteria.list();
		
		if(databaseDistributionDriverList!=null && !databaseDistributionDriverList.isEmpty()){
			databaseDriver = databaseDistributionDriverList.get(0);
			databaseDriver.getDriverType().getAlias();
			databaseDriver.getService().isEnableDBStats();
		}
		return databaseDriver;
	}
	
	/**
	 * Method will update driver type.
	 * @param driverId
	 * @param updated value
	 * @return
	 */
	@Override
	public boolean updateDriverType(int driverId, String updatedDriverType) {
		
		Query query = getCurrentSession().createSQLQuery(
			    "update TBLTDRIVER set DTYPE = :dType" + " where ID = :driverId");
			query.setParameter("dType", updatedDriverType);
			query.setParameter("driverId", driverId);
			query.executeUpdate();
			return true;
	}
	
}
