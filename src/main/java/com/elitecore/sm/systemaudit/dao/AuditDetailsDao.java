package com.elitecore.sm.systemaudit.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.systemaudit.model.SystemAuditDetails;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface AuditDetailsDao extends GenericDAO<SystemAuditDetails>{

	public List<SystemAuditDetails>  getAuditDetailsBySystemAuditId(int systermAuditId);
}
