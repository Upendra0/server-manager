/**
 * 
 */
package com.elitecore.sm.systemaudit.service;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.systemaudit.model.AuditActivity;
import com.elitecore.sm.systemaudit.model.AuditUserDetails;
import com.elitecore.sm.systemaudit.model.SearchStaffAudit;
import com.elitecore.sm.systemaudit.model.SystemAudit;
import com.elitecore.sm.systemaudit.model.SystemAuditDetails;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface SystemAuditService {

	public long getTotalServiceInstancesCount(SearchStaffAudit searchStaffAudit);
	
	public List<SystemAudit> getPaginatedList(SearchStaffAudit service, int startIndex, int limit, String sidx, String sord);
	
	public void addAuditDetails(AuditUserDetails auditUserDetails, AuditActivity currentAction, List<SystemAuditDetails> modifiedPropsList, String actionType, String remarkMessage);

	public BaseModel getOldEntity(Class<?> clazz, Integer id);
	
	public ResponseObject checkEntityAndGetName(Object entity);
	
	public String setRemarks(Map<String,String> entityNameMap, String remarkMessage);
	
	
}
