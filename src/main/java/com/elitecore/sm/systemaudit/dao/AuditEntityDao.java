package com.elitecore.sm.systemaudit.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.systemaudit.model.AuditEntity;

public interface AuditEntityDao extends GenericDAO<AuditEntity>{
	
	public List<AuditEntity>  getAllAuditEntity();
	
}
