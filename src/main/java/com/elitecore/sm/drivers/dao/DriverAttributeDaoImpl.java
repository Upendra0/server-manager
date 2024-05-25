package com.elitecore.sm.drivers.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriverAttribute;

/**
 * 
 * @author Chirag.Rathod
 *
 */
@Repository
public class DriverAttributeDaoImpl extends GenericDAOImpl<DatabaseDistributionDriverAttribute> implements DriverAttributeDao{
	
	@Override
	public Map<String, Object> getAttributeConditionList(int driverId) {
		
		logger.debug(">> getAttributeConditionList"  +driverId);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		conditionList.add(Restrictions.eq(BaseConstants.DBDISDRIVER_ID, driverId));
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseDistributionDriverAttribute> getAllAttributeByDriverId(int driverId) {
		Criteria criteria=getCurrentSession().createCriteria(DatabaseDistributionDriverAttribute.class);
		
		criteria.add(Restrictions.eq(BaseConstants.DBDISDRIVER_ID, driverId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		
		return (List<DatabaseDistributionDriverAttribute>)criteria.list();
	}
	
	/**
	 * Method will check attribute name while update attribute.
	 * @param mappingId
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DatabaseDistributionDriverAttribute checkUniqueAttributeNameForUpdate(int driverId, String name) {
		Criteria criteria=getCurrentSession().createCriteria(DatabaseDistributionDriverAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.DBDISDRIVER_ID, driverId));
		criteria.add(Restrictions.eq("databaseFieldName", name).ignoreCase());
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<DatabaseDistributionDriverAttribute> driverAttributeList = criteria.list();
		if(driverAttributeList != null && !driverAttributeList.isEmpty() ){
			return driverAttributeList.get(0);
		}else{
			return null;
		}
	}
}
