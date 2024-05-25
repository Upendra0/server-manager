/**
 * 
 */
package com.elitecore.sm.systemaudit.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.systemaudit.model.SearchStaffAudit;
import com.elitecore.sm.systemaudit.model.SystemAudit;


/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="systemAuditDao")
public class SystemAuditDaoImpl extends GenericDAOImpl<SystemAudit> implements SystemAuditDao{

	/**
	 * This method is used to get the System Audit Model list based on the offset and sort order
	 * 
	 * @param offset defines the starting row index 
	 * @param limit defines how many number of rows to be fetched.
	 * @param sortColumn defines which column have sort criteria
	 * @param sortOrder defines asc or desc order.
	 * @return List<SystemAudit>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SystemAudit> getPaginatedList(Class<SystemAudit> auditInstance,	List<Criterion> conditions, Map<String,String> aliases, int offset,int limit,String sortColumn,String sortOrder){
		
		List<SystemAudit> resultList ;
		Criteria criteria = getCurrentSession().createCriteria(auditInstance);
	
		logger.debug("Sort column ="+sortColumn);
		if(BaseConstants.DESC.equalsIgnoreCase(sortOrder)){
			if(BaseConstants.AUDITE_DATE.equals(sortColumn))
				{criteria.addOrder(Order.desc(BaseConstants.AUDITE_DATE));}
			else if(BaseConstants.STAFF_NAME.equals(sortColumn))
				{criteria.addOrder(Order.desc(BaseConstants.USER_NAME));}
			else{criteria.addOrder(Order.desc(sortColumn));	}
		}
		else if(BaseConstants.ASC.equalsIgnoreCase(sortOrder)){
			if(BaseConstants.AUDITE_DATE.equals(sortColumn))
				{criteria.addOrder(Order.asc(BaseConstants.AUDITE_DATE));}
			else if(BaseConstants.STAFF_NAME.equals(sortColumn))
				{criteria.addOrder(Order.asc(BaseConstants.USER_NAME));}
			else{criteria.addOrder(Order.asc(sortColumn));	}
		}
		
		if(conditions!=null){
			for(Criterion condition : conditions){
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		
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

	/**
	 * Method will fetch all staff detail list based on search parameters.
	 * @param searchStaffAudit
	 * @return Map of object.
	 */
	@Override
	public Map<String, Object> getStaffAuditBySearchParamters(SearchStaffAudit searchStaffAudit) {
		
		logger.debug(">> getStaffAuditBySearchParamters in SystemAuditDaoImpl "  +searchStaffAudit);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		if (!StringUtils.isEmpty(searchStaffAudit.getUserName())) {
			conditionList.add(Restrictions.like("userName", "%" + searchStaffAudit.getUserName().trim() + "%").ignoreCase());
		}
		
		if (searchStaffAudit.getEntityId() > 0) {
			conditionList.add(Restrictions.eq("systemAuditEntity.id", searchStaffAudit.getEntityId()));
		}

		if (searchStaffAudit.getSubEntityId() > 0) {
			conditionList.add(Restrictions.eq("systemAuditSubEntity.id", searchStaffAudit.getSubEntityId()));
		}

		if (searchStaffAudit.getAuditActivityId() > 0) {
			conditionList.add(Restrictions.eq("systemAuditActivity.id", searchStaffAudit.getAuditActivityId()));
		}
		
		if (!StringUtils.isEmpty(searchStaffAudit.getActionType()) && !"ALL".equals(searchStaffAudit.getActionType())) {
			conditionList.add(Restrictions.eq("actionType", searchStaffAudit.getActionType()));
		}
		
		if(!StringUtils.isEmpty(searchStaffAudit.getDurationFrom()) ){
			conditionList.add(Restrictions.ge(BaseConstants.AUDITE_DATE, searchStaffAudit.getDurationFrom()));
		}
			
		if(!StringUtils.isEmpty(searchStaffAudit.getDurationTo()) ){
			conditionList.add(Restrictions.le(BaseConstants.AUDITE_DATE, searchStaffAudit.getDurationTo()));
		} 
		
		conditionList.add(Restrictions.eq("status",StateEnum.ACTIVE));
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getStaffAuditBySearchParamters in SystemAuditDaoImpl ");
		return returnMap;
	}

	@Override
	public void addAuditDetails(SystemAudit systemAudit) {
		getCurrentSession().save(systemAudit);
	}

	/**
	 * Method will load entity from cache for given id. if not found from cache then it will find from database.
	 */
	@Override
	public BaseModel getOldEntity(Class<?> clazz, Integer id) {
		BaseModel entity =  (BaseModel) getCurrentSession().load(clazz, id);
		entity = initializeAndUnproxy(entity);
		return entity;
	}

	/**
	 * Method will convert hibernate proxy object to unproxy.
	 * @param entity
	 * @return Entity
	 */
	@SuppressWarnings("unchecked")
	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			throw new NullPointerException("Entity passed for initialization is null");
		}
		//Hibernate.initialize(entity);
		if (entity instanceof HibernateProxy) {		
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}
		return entity;
	}
}
