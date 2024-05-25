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
import com.elitecore.sm.systemaudit.model.AuditEntity;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="auditEntityDao")
public class AuditEntityDaoImpl extends GenericDAOImpl<AuditEntity> implements AuditEntityDao {

	/**
	 * Method will fetch all audit Master entity list.
	 * @return List of AuditEntity
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditEntity> getAllAuditEntity() {
		logger.debug(">> getAllAuditEntity");
		Criteria criteria = getCurrentSession().createCriteria(AuditEntity.class);
		criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		logger.debug("<< getAllAuditEntity");
		return criteria.list();
	}
}
