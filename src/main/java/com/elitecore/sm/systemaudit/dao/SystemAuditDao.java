/**
 * 
 */
package com.elitecore.sm.systemaudit.dao;

import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.systemaudit.model.SearchStaffAudit;
import com.elitecore.sm.systemaudit.model.SystemAudit;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface SystemAuditDao  extends GenericDAO<SystemAudit>{

	public Map<String, Object>   getStaffAuditBySearchParamters(SearchStaffAudit searchStaffAudit);
	
	public void addAuditDetails(SystemAudit systemAudit);
	
	public BaseModel getOldEntity(Class<?> clazz, Integer id);
	
	
}
