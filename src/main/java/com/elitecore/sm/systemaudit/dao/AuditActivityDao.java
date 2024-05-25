/**
 * 
 */
package com.elitecore.sm.systemaudit.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.systemaudit.model.AuditActivity;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface AuditActivityDao extends GenericDAO<AuditActivity>{

	public List<AuditActivity>  getAllAuditActivityBySubEntityId(int Id);
	
	public List<AuditActivity>  getAllAuditActivity();
	
}
