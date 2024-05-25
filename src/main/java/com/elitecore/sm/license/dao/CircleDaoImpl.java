package com.elitecore.sm.license.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.license.model.MappedDevicesInfoDTO;

/**
 * 
 * @author sterlite
 *
 */
@Repository(value = "circleDao")
public class CircleDaoImpl extends GenericDAOImpl<Circle> implements CircleDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Circle> getAllCirclesList() {
		logger.debug("Fetching server manager license details.");
		Criteria criteria = getCurrentSession().createCriteria(Circle.class);		
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));		
		criteria.addOrder(Order.asc("name"));
		return (List<Circle>) criteria.list();		
	}
	
	@Override
	public Circle getCircleById(int circleId){
		logger.debug("Fetching server manager circle details.");
		return  (Circle) getCurrentSession().load(Circle.class, circleId);		
	}
	
	@Override
	public int getCountByCircleName(String circleName){
		Criteria criteria = getCurrentSession().createCriteria(Circle.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("name", circleName));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Circle> getCircleListByName(String circleName) {

		Criteria criteria = getCurrentSession().createCriteria(Circle.class);
		criteria.add(Restrictions.eq("name", circleName));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		return (List<Circle>) criteria.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MappedDevicesInfoDTO> getMappedDevicesInfoByCircleId(int circleId) { 
		SQLQuery q = getCurrentSession().createSQLQuery( "select DISTINCT TBLTSERVICE.NAME as SERVICENAME, tbltserverinstance.NAME as SERVERNAME, " +
				"TBLMDEVICETYPE.NAME as DEVICETYPE, TBLMDEVICE.NAME as DEVICENAME FROM TBLTSERVERINSTANCE " + 
				"RIGHT JOIN TBLTSERVICE on TBLTSERVERINSTANCE.ID=TBLTSERVICE.SERVERINSTANCEID " + 
				"RIGHT JOIN TBLTPATHLIST on TBLTPATHLIST.SERVICEID=TBLTSERVICE.ID " + 
				"INNER JOIN TBLMDEVICE on TBLMDEVICE.ID=TBLTPATHLIST.PARENTDEVICE " + 
				"INNER JOIN TBLMDEVICETYPE on TBLMDEVICETYPE.ID=TBLMDEVICE.DEVICETYPEID  " + 
				"WHERE TBLTPATHLIST.CIRCLEID=? and TBLTPATHLIST.STATUS='ACTIVE' and DTYPE='PPL' ");
		q.setInteger(0, circleId); 
		List<Object[]> list = q.list(); 
		List<MappedDevicesInfoDTO> data = new ArrayList<>();
		MappedDevicesInfoDTO obj = null;
		if (list != null && !list.isEmpty()) {
			for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
				Object[] objects = iterator.next();
				String serviceName = (String) objects[0];
				String serverInstanceName = (String) objects[1];
				String deviceType = (String) objects[2];
				String deviceName = (String) objects[3];
				obj = new MappedDevicesInfoDTO(deviceName, null, deviceType, serviceName, serverInstanceName);
				data.add(obj);
			}
		}
		return data; 
	}
}