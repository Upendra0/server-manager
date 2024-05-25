package com.elitecore.sm.systemaudit.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.systemaudit.model.AuditEntity;

public interface AuditEntityService {

	public List<AuditEntity> getAllAuditEntity();
	
	public ResponseObject getAuditEntity(String entityType, int entityId);
	
}
