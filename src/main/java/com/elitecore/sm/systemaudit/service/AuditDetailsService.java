package com.elitecore.sm.systemaudit.service;

import java.util.List;
import java.util.Map;

import org.javers.core.diff.changetype.ValueChange;

import com.elitecore.sm.systemaudit.model.SystemAuditDetails;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface AuditDetailsService {

	public List<SystemAuditDetails> getAllModifiedProps(List<ValueChange> modifiedProps);
	
	public List<SystemAuditDetails>  getAuditDetailsBySystemAuditId(int systermAuditId);
	
	public List<SystemAuditDetails> getAllPropties(Map<String,List<?>> modifiedPropsList);
}
