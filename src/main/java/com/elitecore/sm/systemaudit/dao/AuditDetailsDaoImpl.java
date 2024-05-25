package com.elitecore.sm.systemaudit.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.systemaudit.model.SystemAuditDetails;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="auditDetailsDao")
public class AuditDetailsDaoImpl extends GenericDAOImpl<SystemAuditDetails> implements AuditDetailsDao{

	/**
	 * Method will get all system audit details by system audit id.
	 * @param systermAuditId
	 * @return List<SystemAuditDetails>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SystemAuditDetails> getAuditDetailsBySystemAuditId(int systermAuditId) {
		logger.debug(">> getAuditDetailsBySystemAuditId  Parameter systermAuditId :: " + systermAuditId);
		
		Criteria criteria = getCurrentSession().createCriteria(SystemAuditDetails.class);
		criteria.add(Restrictions.eq("systemAudit.id", systermAuditId));
		criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		
		logger.debug("<< getAuditDetailsBySystemAuditId");
		return criteria.list();
	}


}
