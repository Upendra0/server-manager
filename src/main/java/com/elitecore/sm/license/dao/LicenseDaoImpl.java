package com.elitecore.sm.license.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.LicenseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.license.model.HourlyCDRCount;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.util.Utilities;

/**
 * 
 * @author sterlite
 *
 */
@Repository(value = "licenseDao")
public class LicenseDaoImpl extends GenericDAOImpl<License> implements LicenseDao {

	/**
	 * Method will fetch server manager license details.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public License getServerManagerLicenseDetails() {
		logger.debug("Fetching server manager license details.");
		Criteria criteria = getCurrentSession().createCriteria(License.class);
		List<License> licenseList = (List<License>) criteria.list();
		if (licenseList != null && !licenseList.isEmpty()) {
			return licenseList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Method will get license details by smServer Id.
	 * 
	 * @param serverId
	 * @return
	 */
	public License getLicenseByServerId(String serverId, String applicationPath) {
		logger.debug("Fetching server manager license details.");
		Criteria criteria = getCurrentSession().createCriteria(License.class);
		criteria.add(Restrictions.eq("smServerId", serverId));
		if (applicationPath != null && !applicationPath.isEmpty())
			criteria.add(Restrictions.eq("applicationPath", applicationPath));
		List<License> licenseList = (List<License>) criteria.list();
		logger.info(licenseList);
		logger.info("getLicenseByServerId applicationPath ---- " + applicationPath);

		if (licenseList != null && !licenseList.isEmpty()) {
			return licenseList.get(0);
		} else {
			return getAndUpdateLicenseByServerId(serverId, applicationPath);
		}
	}

	public License getAndUpdateLicenseByServerId(String serverId, String applicationPath) {
		logger.debug("Fetching server manager license details.");
		Criteria criteria = getCurrentSession().createCriteria(License.class);
		criteria.add(Restrictions.eq("smServerId", serverId));
		List<License> licenseList = (List<License>) criteria.list();
		if (licenseList != null && !licenseList.isEmpty()) {
			License license = licenseList.get(0);
			license.setApplicationPath(applicationPath);
			update(license);
			return license;
		}
		return null;
	}

	/**
	 * Method will get all server instances by ipaddress/host.
	 * 
	 * @param ipAdress
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<License> getAllServerInstancesByHost(String ipAdress) {
		logger.debug("Fetching all server instances by ipadress " + ipAdress);
		Criteria criteria = getCurrentSession().createCriteria(License.class);
		criteria.add(Restrictions.eq("hostName", ipAdress));
		criteria.add(Restrictions.eq(ControllerConstants.COMPONENT_TYPE, LicenseConstants.LICENSE_ENGINE));
		return (List<License>) criteria.list();
	}

	/**
	 * Method will fetch license details by server instance id.
	 * 
	 * @param serverInstanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public License getLicenseDetailsByServerInstanceId(int serverInstanceId) {
		logger.debug("Fetching license detaisl by server instance id " + serverInstanceId);
		Criteria criteria = getCurrentSession().createCriteria(License.class);
		criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));
		criteria.add(Restrictions.eq(ControllerConstants.COMPONENT_TYPE, LicenseConstants.LICENSE_ENGINE));
		List<License> licenseList = (List<License>) criteria.list();
		if (licenseList != null && !licenseList.isEmpty()) {
			logger.info("License details found successfully for server instance id " + serverInstanceId);
			return licenseList.get(0);
		} else {
			logger.info("Failed to get license details for server instance id " + serverInstanceId);
			return null;
		}
	}

	/**
	 * Method will get all License where component type is serverManager.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public License getLicenseDetailsByComponentType() {

		logger.info("Fetching license details where componentType = serverManager");

		License license = null;

		Criteria criteria = getCurrentSession().createCriteria(License.class);
		criteria.add(Restrictions.eq(ControllerConstants.COMPONENT_TYPE, LicenseConstants.LICENSE_SM));

		List<License> licenseList = (List<License>) criteria.list();

		if (licenseList != null && !licenseList.isEmpty()) {
			license = licenseList.get(0);
			logger.info("Successfully retrieved License details where component type is ServerManager");
		} else {
			logger.info("No license details found where component type is ServerManager ");
		}
		return license;
	}

	/**
	 * Method will give count of number of rows where component type is engine.
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> getLicenseDetailsCount() {

		logger.debug("Fetching number of row count where component type = engine");
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();

		conditionList.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		conditionList.add(Restrictions.eq(ControllerConstants.COMPONENT_TYPE, LicenseConstants.LICENSE_ENGINE));

		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);

		logger.debug("<< getLicenseDetailsCount in LicenseDaoImpl ");
		return returnMap;
	}

	/**
	 * Method will get License data where component type is engine.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<License> getLicensePaginatedList(Class<License> classInstance, int offset, int limit, String sortColumn,
			String sortOrder) {

		logger.debug("fetching engine license details for aboutus grid");

		List<License> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance, "license");
		logger.debug("Sort column =" + sortColumn);

		criteria.createAlias("license.serverInstance", "serverInstance");
		criteria.createAlias("serverInstance.server", "server");
		criteria.add(Restrictions.ne("license.status", StateEnum.DELETED));
		criteria.add(Restrictions.eq("license.componentType", LicenseConstants.LICENSE_ENGINE));
		criteria.add(Restrictions.ne("serverInstance.status", StateEnum.DELETED));
		criteria.addOrder(Order.desc("server.ipAddress"));
		criteria.addOrder(Order.desc("server.utilityPort"));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		resultList = criteria.list();

		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public License getLicenseDetailsByInstanceId(int serverInstanceId) {
		logger.debug("Fetching server manger license details for server instance id  : " + serverInstanceId);
		Criteria criteria = getCurrentSession().createCriteria(License.class);
		criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));
		List<License> licenseList = (List<License>) criteria.list();
		logger.info(licenseList);
		if (licenseList != null && !licenseList.isEmpty()) {
			return licenseList.get(0);
		}
		return null;
	}

	/**
	 * Method will give all the details from versionHistory table for aboutUs page
	 * 
	 * @return query.list
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getVersionData() {
		logger.info(" Fetching data from virsionHistory table :");
		String sql = "SELECT * FROM TBLMVERSION WHERE modulename='" + BaseConstants.MODULE_NAME_SM
				+ "' ORDER BY installationdate DESC";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		return query.list();

	}

	/**
	 * Method will get all server instances by ipaddress/host.
	 * 
	 * @param ipAdress
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<License> getAllServerInstancesByHostContainerEnv(String ipAdress, int utilityPort,
			boolean isContainer) {
		logger.debug("Fetching all server instances by container env " + ipAdress);
		Criteria criteria = getCurrentSession().createCriteria(License.class);
		criteria.add(Restrictions.eq("hostName", ipAdress));
		criteria.add(Restrictions.eq(ControllerConstants.COMPONENT_TYPE, LicenseConstants.LICENSE_ENGINE));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<License>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public License getSMLicenseDetails() {
		logger.debug("Fetching server manager license details.");
		Criteria criteria = getCurrentSession().createCriteria(License.class);
		criteria.add(Restrictions.eq(ControllerConstants.COMPONENT_TYPE, LicenseConstants.LICENSE_SM));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.add(Restrictions.eq("applicationPath", LicenseUtility.TOMCAT_HOME));

		criteria.addOrder(Order.desc("id"));

		List<License> licenseList = (List<License>) criteria.list();
		if (licenseList != null && !licenseList.isEmpty()) {
			return licenseList.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getLicenseCount(Class<License> classInstance) {

		String hql = "select COUNT(*) "
				+ " from License as license inner join license.serverInstance as serverinstance "
				+ " inner join serverinstance.server server where license.status<>'DELETED' "
				+ " and license.componentType='ENGINE' and serverinstance.status<>'DELETED' ";

		return (Long) getCurrentSession().createQuery(hql).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Date, Long> getHourWiseTotalUtilizationMap() {
		logger.debug("Fetching Hour wise CDR Count summary by process date");
        Map<Date, Long> map = new HashMap<>();
		Query q = getCurrentSession().createQuery(
				"SELECT t.processEndDate , SUM(t.totalCDRCount) AS totalCDRCount FROM HourlyCDRCount t  GROUP BY t.processEndDate");
		List<Object[]> list = (List<Object[]>) q.list();
		for (Object[] obj : list) {
			Date date = (Date) obj[0];
			Long count = (Long) obj[1];
			map.put(date, count);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Date, Long> getHourWiseCurrentUtilizationMap() {
		logger.debug("Fetching Hour wise CDR Count summary by cdr date");
        Map<Date, Long> map = new HashMap<>();
		Query q = getCurrentSession().createQuery(
				"SELECT t.cdrDate , SUM(t.totalCDRCount) AS totalCDRCount FROM HourlyCDRCount t  GROUP BY t.cdrDate");
		List<Object[]> list = (List<Object[]>) q.list();
		for (Object[] obj : list) {
			Date date = (Date) obj[0];
			Long count = (Long) obj[1];
			map.put(date, count);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HourlyCDRCount> getHourlyCDRDCountByProcessDate(Date processDate) {
		logger.debug("Fetching Hourly CDR count details by process date");
		Criteria criteria = getCurrentSession().createCriteria(HourlyCDRCount.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.add(Restrictions.eq("processEndDate", processDate));
		criteria.addOrder(Order.desc("cdrDate"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public License getLicenseDetailsByCircleId(int circleId) {
		logger.debug("Fetching license details by circle id.");
		Criteria criteria = getCurrentSession().createCriteria(License.class);		
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.add(Restrictions.eq("circle.id", circleId));
		List<License> licenseList = (List<License>) criteria.list();
		if (licenseList != null && !licenseList.isEmpty()) {
			return licenseList.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getCurrentLicenseUtilizationByCircle(int circleId) {
		logger.debug("Fetching Hour wise CDR Count summary by cdr date");
		Query q = getCurrentSession().createQuery("SELECT SUM(t.totalCDRCount) AS totalCDRCount FROM HourlyCDRCount t "
				+ "WHERE t.circle.id=:circleId "
				+ "AND t.cdrDate = (SELECT MAX(a.cdrDate) as date FROM HourlyCDRCount a WHERE a.circle.id=:circleId)");		
		q.setParameter("circleId", circleId);
		List<Long> list = (List<Long>) q.list();
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Long> getCurrentLicenseUtilizationByCircleAndDevice(int circleId) {
		logger.debug("Fetching Hour wise CDR Count summary by cdr date");
        Map<String, Long> map = new HashMap<>();
        Query q = getCurrentSession().createQuery("SELECT t.deviceType, SUM(t.totalCDRCount) AS totalCDRCount FROM HourlyCDRCount t "
				+ "WHERE t.circle.id=:circleId "
				+ "AND t.cdrDate = (SELECT MAX(a.cdrDate) as date FROM HourlyCDRCount a WHERE a.circle.id=:circleId) "
				+ "GROUP BY t.deviceType");		
		q.setParameter("circleId", circleId);
		List<Object[]> list = (List<Object[]>) q.list();
		for (Object[] obj : list) {
			String device = (String) obj[0];
			Long count = (Long) obj[1];
			map.put(device, count);
		}
		return map;
	}
	
	/**
	 * Fetch License count based on circle id , to check license is applied to the circle or not
	 */
	@Override
	public boolean isLicenseAppliedToCircle(int circleId){
				
		Criteria criteria = getCurrentSession().createCriteria(License.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));		
		criteria.add(Restrictions.eq("circle.id", circleId));	
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue()>0?true:false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getMaxLicenseUtilizationByCircle(int circleId) {
		logger.debug("Fetching Latest Hour wise CDR Count summary by circle");
		Query q = getCurrentSession().createQuery(
				"SELECT SUM(t.totalCDRCount) AS totalCDRCount, t.processStartDate FROM HourlyCDRCount t "
				+ "WHERE t.circle.id=:circleId GROUP BY t.processStartDate");		
		q.setParameter("circleId", circleId);
		List<Object[]> list = (List<Object[]>) q.list();
		Long maxCount = 0l;
		for (Object[] obj : list) {			
			Long count = (Long) obj[0];
			if(count > maxCount)
				maxCount=count;
		}
		return maxCount;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Long> getMaxLicenseUtilizationByCircleAndDevice(int circleId) {
		logger.debug("Fetching Max CDR Count by scanning all process start date partition summary by circle and device type");
        Map<String, Long> map = new HashMap<>();
        Query q = getCurrentSession().createQuery(
				"SELECT t.deviceType, t.processStartDate, SUM(t.totalCDRCount) AS totalCDRCount FROM HourlyCDRCount t "
				+ "WHERE t.circle.id=:circleId GROUP BY t.deviceType, t.processStartDate");		
		q.setParameter("circleId", circleId);
		List<Object[]> list = (List<Object[]>) q.list();
		for (Object[] obj : list) {
			String device = (String) obj[0];
			Long count = (Long) obj[2];
			map.put(device, count);
			if(map.containsKey(device)) {
				Long oldValue = map.get(device);
				if(count>oldValue)
					map.put(device, count);
			}			
		}
		return map;
	}
	
}