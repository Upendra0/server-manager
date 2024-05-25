/**
 * 
 */
package com.elitecore.sm.device.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.DecodeTypeEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.model.SearchDeviceMapping;


/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="deviceDao")
public class DeviceDaoImpl extends GenericDAOImpl<Device> implements DeviceDao {

	/**
	 * Method will return all Device list.
	 * @return List<DeviceType>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getAllDeviceList() {
		logger.info(">> getAllDefaulDevices ");
		Criteria criteria=getCurrentSession().createCriteria(Device.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		logger.info("<< getAllDefaulDevices ");
		return criteria.list();
	}

	/**
	 * Method will get Device by device Id.
	 * 
	 */
	@Override
	public Device getDevicebyId(int deviceId) {
		return  (Device) getCurrentSession().load(Device.class, deviceId);
	}
	
	/**
	 * Method will fetch all device by Device type and vendor Id. 
	 * (non-Javadoc)
	 * @see com.elitecore.sm.device.dao.DeviceDao#getAllDeviceByVendorAndDeviceType(int, int)
	 * @param deviceTypeId
	 * @param vendorId
	 * @return List<Device>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getAllDeviceByVendorAndDeviceType(int deviceTypeId,	int vendorId, String decodeType) {
		logger.info(">> getAllDeviceByVendorAndDeviceType ");
		Criteria criteria=getCurrentSession().createCriteria(Device.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("deviceType.id", deviceTypeId));
		criteria.add(Restrictions.eq("vendorType.id", vendorId));
		criteria.add(Restrictions.eq("decodeType", decodeType));
		criteria.addOrder(Order.asc("id"));
		logger.info("<< getAllDeviceByVendorAndDeviceType ");
		return criteria.list();
	}

	/**
	 * Method will fetch all device by Decode type. 
	 * (non-Javadoc)
	 * @see com.elitecore.sm.device.dao.DeviceDao#getAllDeviceByDecodeType(String)
	 * @param decodeType
	 * @return List<Device>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceType> getAllDeviceTypeIdsByDecodeType(String decodeType){
		logger.info(">> getAllDeviceTypeIdsByDecodeType ");
		Criteria criteria=getCurrentSession().createCriteria(Device.class,"device");
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("decodeType", decodeType));
		//criteria.addOrder(Order.asc("deviceType"));
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.distinct(Projections.property("deviceType")));
		criteria.setProjection(projections);
		logger.info("<< getAllDeviceTypeIdsByDecodeType ");
		return criteria.list();
	}
	
	
	/**
	 * Method will make a dynamic hql query base on selected search device parameters.
	 * @param deviceMapping
	 * @param actionType
	 * @return
	 */
	@Override
	public String getHQLBySearchParameters(SearchDeviceMapping deviceMapping, String actionType, String sidx, String sord) {
		
		logger.debug("Going to build dynamic query based on parameters " + deviceMapping);
		StringBuilder  hqlQuery = new StringBuilder();
		
		if(BaseConstants.HQL_QUERY_COUNT.equals(actionType)){
			hqlQuery.append("SELECT COUNT(*)  ");
		}else{
			//Please do not change order of select column fields as its used in front end.
			hqlQuery.append("SELECT device.id,device.name,device.decodeType,device.deviceType.name, device.vendorType.name,pa.id,pa.name,count(p.id),device.isPreConfigured,pa.mappingType, mappingtype.alias ");
		}

		if("UPSTREAM".equalsIgnoreCase(deviceMapping.getDecodeType())){
			hqlQuery.append("FROM Device as device LEFT OUTER JOIN device.parserMapping as pa ON pa.status='"+StateEnum.ACTIVE+"' "
					   + " LEFT OUTER JOIN pa.parserWrapper as p ON p.status='"+StateEnum.ACTIVE+"' LEFT OUTER JOIN pa.parserType as mappingtype WHERE device.status='"+StateEnum.ACTIVE+"' AND  device.decodeType='"+DecodeTypeEnum.UPSTREAM+"' ");
		}else{
			hqlQuery.append("FROM Device as device LEFT OUTER JOIN device.composerMapping as pa ON pa.status='"+StateEnum.ACTIVE+"' "
					   + " LEFT OUTER JOIN pa.composerWrapper as p ON p.status='"+StateEnum.ACTIVE+"' LEFT OUTER JOIN pa.composerType as mappingtype   WHERE device.status='"+StateEnum.ACTIVE+"' AND  device.decodeType='"+DecodeTypeEnum.DOWNSTREAM+"' ");
		}
		
				
		if (!StringUtils.isEmpty(deviceMapping.getDeviceName() )) {	
			logger.info("Adding device name search parameter in query.");
			hqlQuery.append(" AND upper(device.name) LIKE  '%"+(deviceMapping.getDeviceName().trim()).toUpperCase() + "%'");
		}
		
		if (!StringUtils.isEmpty(deviceMapping.getDeviceTypeName())) {		
			logger.info("Adding device type search parameter in query.");
			hqlQuery.append(" AND  upper(device.deviceType.name) LIKE  '%"+(deviceMapping.getDeviceTypeName().trim()).toUpperCase() + "%'");
		}

		if (!StringUtils.isEmpty(deviceMapping.getVendorTypeName())) {		
			logger.info("Adding vendor type search parameter in query.");
			hqlQuery.append(" AND  upper(device.vendorType.name) LIKE  '%"+(deviceMapping.getVendorTypeName().trim()).toUpperCase() + "%'");
		}
		
		if (!StringUtils.isEmpty(deviceMapping.getDecodeType()) && !"undefined".equals(deviceMapping.getDecodeType()) && !"0".equals(deviceMapping.getDecodeType())) {
			logger.info("Adding decode type search parameter in query.");	
			hqlQuery.append(" AND device.decodeType='"+deviceMapping.getDecodeType().trim()+"' ");
		}

		if (!StringUtils.isEmpty(deviceMapping.getMappingName())) {
			logger.info("Adding mapping search parameter in query.");
			hqlQuery.append(" AND upper(pa.name) LIKE  '%"+(deviceMapping.getMappingName().trim()).toUpperCase() + "%'");
		}
				
		hqlQuery.append(" GROUP BY device.id,device.name,device.decodeType,device.deviceType.name, device.vendorType.name,pa.id,pa.name,device.isPreConfigured,pa.mappingType,mappingtype.alias ORDER BY "+sidx+" "+sord+" ");
		
		logger.info("Final build hql query is " + hqlQuery.toString());
		
		return hqlQuery.toString();
	}
	
	
	@Override
	public Map<String, Object> getDeviceByDecodeType(SearchDeviceMapping searchDevice) {
		
		logger.debug(">> getDeviceByDecodeType in DeviceDaoImpl "  +searchDevice);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		if (!StringUtils.isEmpty(searchDevice.getDecodeType())) {
			//aliases.put("Device", "Device");
			conditionList.add(Restrictions.eq("decodeType", searchDevice.getDecodeType().trim() ).ignoreCase());
		}
		conditionList.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		returnMap.put("aliases", null);
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getServiceBySearchParameters in ServicesDaoImpl ");
		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getDevicesPaginatedList(Class<Device> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {

		List<Device> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);

		logger.debug("Sort column =" + sortColumn);

		if (("desc").equalsIgnoreCase(sortOrder)) {
				criteria.addOrder(Order.desc(sortColumn));
		} else if (("asc").equalsIgnoreCase(sortOrder)) {
				criteria.addOrder(Order.asc(sortColumn));
		}

		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getObjectListByQuery(String hqlQuery) {
		Query query = getCurrentSession().createQuery(hqlQuery);
		return query.list();
	}
	
	
	/**
	 * Method  will get  device count by device Name; 
	 * @param name
	 * @return count
	 */
	@Override
	public long getDeviceCountByName(String name,String decodeType) {
		logger.debug("Going to fetch device count for device " + name);
		
		Criteria criteria = getCurrentSession().createCriteria(Device.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (name != null) {
			criteria.add(Restrictions.eq("name", name).ignoreCase());
		} 
		if(decodeType != null){
			criteria.add(Restrictions.eq("decodeType", decodeType).ignoreCase());			
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}
	
	@Override
	public long getDeviceCountByDeviceId(Device device) {
		logger.debug("Going to fetch device count for device type " + device.getDeviceType().getId());
		
		Criteria criteria = getCurrentSession().createCriteria(Device.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		criteria.add(Restrictions.eq("deviceType.id", device.getDeviceType().getId()));
		criteria.add(Restrictions.ne("id", device.getId()));
		
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}
	
	@Override
	public long getDeviceCountByVendorId(Device device) {
		logger.debug("Going to fetch device count for vendor type " + device.getVendorType().getId());
		
		Criteria criteria = getCurrentSession().createCriteria(Device.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		criteria.add(Restrictions.eq("vendorType.id", device.getVendorType().getId()));
		criteria.add(Restrictions.ne("id", device.getId()));
		
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}

	/**
	 * Method will get device object by device name.
	 * @param name
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getDeviceCountByNameForUpdate(String name, String decodeType) {
		
		logger.debug("Going to fetch device count for device " + name);
		
		Criteria criteria = getCurrentSession().createCriteria(Device.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (name != null) {
			criteria.add(Restrictions.eq("name", name).ignoreCase());
		} 
		if(decodeType != null){
			criteria.add(Restrictions.eq("decodeType", decodeType).ignoreCase());
		}
		return criteria.list(); 
		
	}
	
	/**
	 * Method will fetch all device details like mapping list and all associated mapping entities.
	 * @param deviceId
	 * @return List<Object[]>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDeviceDetailsById(int deviceId, String decodeType) {
		logger.debug("Going to fetch device details for device id" + deviceId);
		
		StringBuilder hqlQueryString = new StringBuilder();
		
		//Please do not change order of select column fields as its used in front end.
		if(BaseConstants.UPSTREAM.equals(decodeType)){			
			hqlQueryString.append("SELECT device.id,device.name,device.decodeType,device.deviceType.name, device.vendorType.name,pa.id,pa.name,count(p.id),device.isPreConfigured,pa.mappingType FROM Device as device "); 
			hqlQueryString.append(" LEFT OUTER JOIN device.parserMapping as pa ON pa.status=?");
			hqlQueryString.append(" LEFT JOIN pa.parserWrapper as p  ON p.status=?");
			hqlQueryString.append(" WHERE device.status=? AND device.id=?");
			hqlQueryString.append(" GROUP BY device.id,device.name,device.decodeType,device.deviceType.name, device.vendorType.name,pa.id,pa.name, device.isPreConfigured, pa.mappingType ORDER BY device.id ASC");
		}else {
			hqlQueryString.append("SELECT device.id,device.name,device.decodeType,device.deviceType.name, device.vendorType.name,co.id,co.name,count(c.id),device.isPreConfigured,co.mappingType FROM Device as device "); 
			hqlQueryString.append(" LEFT OUTER JOIN device.composerMapping as co ON co.status=?");
			hqlQueryString.append(" LEFT JOIN co.composerWrapper as c  ON c.status=?");
			hqlQueryString.append(" WHERE device.status=? AND device.id=?");
			hqlQueryString.append(" GROUP BY device.id,device.name,device.decodeType,device.deviceType.name, device.vendorType.name,co.id,co.name, device.isPreConfigured, co.mappingType ORDER BY device.id ASC");
		}
		
		Query query = getCurrentSession().createQuery(hqlQueryString.toString());
		query.setString(0, String.valueOf(StateEnum.ACTIVE));
		query.setString(1, String.valueOf(StateEnum.ACTIVE));
		query.setString(2, String.valueOf(StateEnum.ACTIVE));
		query.setInteger(3,deviceId);
		
		return query.list();
	}
	
	/**
	 * Fetch dependents of device
	 * @param device
	 */
	@Override
	public void iterateOverDevice(Device device){
		if(device !=null){
			Hibernate.initialize(device.getDeviceType());
			Hibernate.initialize(device.getVendorType());
		}
	}

	/**
	 * Method will fetch Device details by device name.
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Device getDeviceByName(String name) {
		logger.debug("Fetching device by name  " + name);
		Criteria criteria = getCurrentSession().createCriteria(Device.class);
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("name", name.trim()).ignoreCase());
		List<Device> deviceList = criteria.list();
		if(deviceList != null && !deviceList.isEmpty()){
		 return	deviceList.get(0);
		}
		return null;
	}
	
	
	/**
	 * Method will find all device list with only id property by device type id.
	 * @param deviceTypeId
	 * @return List<Device>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAllDeviceIdsByDeviceTypeId(int deviceTypeId) {
		logger.debug("Fetching all device ids by device type id : " + deviceTypeId);
		Criteria criteria = getCurrentSession().createCriteria(Device.class);
		if(deviceTypeId != 0){
			criteria.add(Restrictions.eq("deviceType.id", deviceTypeId));	
		}
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("id").as("id"));
		criteria.setProjection(projections);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getAllDeviceByDecodeType(String decodeType){
		logger.info(">> getAllDeviceByDecodeType ");
		Criteria criteria=getCurrentSession().createCriteria(Device.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("decodeType", decodeType));
		criteria.addOrder(Order.asc("id"));
		logger.info("<< getAllDeviceByVendorAndDeviceType ");
		return criteria.list();
	}
	
	
}