package com.elitecore.sm.systemaudit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.systemaudit.dao.AuditSubEntityDao;
import com.elitecore.sm.systemaudit.model.AuditSubEntity;


/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value = "auditSubEntityService")
public class AuditSubEntityServiceImpl implements AuditSubEntityService{

	@Autowired
	private AuditSubEntityDao auditSubEntityDao;
	
	/**
	 * Method will fetch all Audit sub entity list.
	 * @return List<AuditSubEntity>
	 */
	@Transactional
	@Override
	public List<AuditSubEntity> getAllAuditSubEntity() {
		return auditSubEntityDao.getAllAuditSubEntity();
	}

	/**
	 * Method will fetch all sub entity list by entity Id.
	 * @param Id
	 * @return List<AuditSubEntity>
	 */
	@Transactional
	@Override
	public List<AuditSubEntity> getAllAuditSubEntityByEntityId(int Id) {
		return auditSubEntityDao.getAllAuditSubEntityByEntityId(Id);
	}

}
