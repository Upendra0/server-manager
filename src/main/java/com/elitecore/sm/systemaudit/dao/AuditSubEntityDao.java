package com.elitecore.sm.systemaudit.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.systemaudit.model.AuditSubEntity;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface AuditSubEntityDao extends GenericDAO<AuditSubEntity>{

	
	public List<AuditSubEntity>  getAllAuditSubEntity();
	
	public List<AuditSubEntity>  getAllAuditSubEntityByEntityId(int Id);
	
}
