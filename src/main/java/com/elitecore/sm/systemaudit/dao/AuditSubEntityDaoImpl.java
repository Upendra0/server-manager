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
import com.elitecore.sm.systemaudit.model.AuditSubEntity;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="auditSubEntityDao")
public class AuditSubEntityDaoImpl extends GenericDAOImpl<AuditSubEntity> implements AuditSubEntityDao{

	/**
	 * Method will fetch all Audit SubEntity
	 * @return List<AuditSubEntity>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditSubEntity> getAllAuditSubEntity() {
		logger.debug(">> getAllAuditSubEntity");

		Criteria criteria = getCurrentSession().createCriteria(AuditSubEntity.class);
		criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		
		logger.debug("<< getAllAuditSubEntity");
		return criteria.list();
	}
	
	/**
	 * Method will fetch all Audit SubEntity by Entity Id
	 * @param Id
	 * @return List<AuditSubEntity>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditSubEntity> getAllAuditSubEntityByEntityId(int Id) {
		logger.debug(">> getAllAuditSubEntityByEntityId  Parameter entityId :: " + Id);
		
		Criteria criteria = getCurrentSession().createCriteria(AuditSubEntity.class);
		criteria.add(Restrictions.eq("auditEntity.id", Id));
		criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		
		logger.debug("<< getAllAuditSubEntityByEntityId");
		return criteria.list();
	}

	
}
