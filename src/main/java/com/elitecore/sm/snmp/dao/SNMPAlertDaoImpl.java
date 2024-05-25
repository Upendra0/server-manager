package com.elitecore.sm.snmp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertType;
import com.elitecore.sm.snmp.model.SNMPAlertTypeEnum;

@Repository(value="snmpAlertDao")
public class SNMPAlertDaoImpl extends GenericDAOImpl<SNMPAlert> implements SNMPAlertDao{

	
	/**
	 * Method will fetch Snmp Alert List
	 * @return Map
	 */
	@Override
	public Map<String, Object> getSnmpAlertPaginatedList() {
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditions = new ArrayList<>();

		
		conditions.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);

		return returnMap;
	}

	/**
	 * Get the list of all the Snmp Alerts
	 * @param classInstance
	 * @param conditions
	 * @param aliases
	 * @param offset
	 * @param limit
	 * @param sortColumn
	 * @param sortOrder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPAlert> getSnmpAlertPaginatedList(Class<SNMPAlert> classInstance, List<Criterion> conditions,
														 HashMap<String, String> aliases) {
		
		List<SNMPAlert> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);
	
		/*logger.debug("Sort column ="+sortColumn);
		
		if (("desc").equalsIgnoreCase(sortOrder)) {
			if (("name").equals(sortColumn)){
				criteria.addOrder(Order.desc("name"));
			}else if (("id").equals(sortColumn)){
				criteria.addOrder(Order.desc("id"));
			}else{
				criteria.addOrder(Order.desc(sortColumn));
			}
		} else if (("asc").equalsIgnoreCase(sortOrder)) {
			if (("name").equals(sortColumn)){
				criteria.addOrder(Order.asc("name"));
			}else if ("id".equals(sortColumn)){
				criteria.addOrder(Order.asc("id"));
			}else{
				criteria.addOrder(Order.asc(sortColumn));
			}	
		}*/

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

		//criteria.setFirstResult(offset);//first record is 2
		//criteria.setMaxResults(limit);//records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPAlert> getAlertsByCategory(SNMPAlertTypeEnum alertCategory) {
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlertType.class);
		criteria.add(Restrictions.eq("category", alertCategory));
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		List<SNMPAlertType> alertTypeList = (List<SNMPAlertType>) criteria.list();
		if(alertTypeList != null && alertTypeList.size() > 0){
			List<Integer> snmpTypeList = new ArrayList<>();
			for (SNMPAlertType alertType : alertTypeList) {
				snmpTypeList.add(alertType.getId());

			}
			criteria = getCurrentSession().createCriteria(SNMPAlert.class);
			Object[] snmpAlertIdArr = snmpTypeList.toArray();
			criteria.add(Restrictions.in("alertType.id", snmpAlertIdArr));
			criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
			return (List<SNMPAlert>) criteria.list();
		}
		else{
			return new ArrayList<SNMPAlert>();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.snmp.dao.SNMPAlertDao#getSnmpAlertList()
	 */
	@SuppressWarnings("unchecked")
	public List<SNMPAlert> getSnmpAlertList() {
		List<SNMPAlert> resultList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlert.class);
	
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.addOrder(Order.asc("name"));
		
		resultList = criteria.list();
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SNMPAlert getSnmpAlertByAlertId(String alertId) {
		List<SNMPAlert> resultList;
		SNMPAlert alertObj=null;
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlert.class);
		criteria.add(Restrictions.eq("alertId", alertId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		resultList = criteria.list();
		if(resultList!=null && !resultList.isEmpty()){
			alertObj=resultList.get(0);
		}
		return alertObj;
	}

}
