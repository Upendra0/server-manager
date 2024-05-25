package com.elitecore.sm.systemaudit.service;

import java.util.List;

import com.elitecore.sm.systemaudit.model.AuditActivity;

public interface AuditActivityService {

	public List<AuditActivity>  getAllAuditActivityBySubEntityId(int Id);
	public List<AuditActivity>  getAllAuditActivity();
}
