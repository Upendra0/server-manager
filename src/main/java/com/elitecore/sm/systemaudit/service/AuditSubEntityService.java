/**
 * 
 */
package com.elitecore.sm.systemaudit.service;

import java.util.List;

import com.elitecore.sm.systemaudit.model.AuditSubEntity;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface AuditSubEntityService {

	public List<AuditSubEntity>  getAllAuditSubEntity();
	
	public List<AuditSubEntity>  getAllAuditSubEntityByEntityId(int Id);
}
