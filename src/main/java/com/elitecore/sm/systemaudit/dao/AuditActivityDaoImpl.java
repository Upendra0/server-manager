/**
 * 
 */
package com.elitecore.sm.systemaudit.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.systemaudit.model.AuditActivity;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="auditActivityDao")
public class AuditActivityDaoImpl extends GenericDAOImpl<AuditActivity> implements AuditActivityDao{

	/**
	 * Method will fetch all Audit Activity by sub entity Id
	 * @param Id
	 * @return List<AuditActivity>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditActivity> getAllAuditActivityBySubEntityId(int Id) {
		logger.debug(">> getAllAuditActivityBySubEntityId  Parameter entityId :: " + Id);
		
		Criteria criteria = getCurrentSession().createCriteria(AuditActivity.class);
		
		criteria.add(Restrictions.eq("auditSubEntity.id", Id));
		criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		
		logger.debug("<< getAllAuditActivityBySubEntityId");
		
		return criteria.list();
	}
	
	/**
	 * Method will fetch all Audit Activity list
	 * @return List<AuditActivity>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditActivity> getAllAuditActivity() {
		logger.debug(">> getAllAuditActivity ");
		
		Criteria criteria = getCurrentSession().createCriteria(AuditActivity.class);
		criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		
		logger.debug("<< getAllAuditActivity");
		List<AuditActivity> activityList = criteria.list();
		iterateAuditEntity(activityList);
		
		return activityList;
	}
	
	public void iterateAuditEntity(List<AuditActivity> activityList){
		for (int i = 0; i < activityList.size(); i++) {
			activityList.get(i).getAuditSubEntity().getAuditEntity().getId();
		}
	}
	
}
