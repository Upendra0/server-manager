/**
 * 
 */
package com.elitecore.sm.systemaudit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.systemaudit.dao.AuditActivityDao;
import com.elitecore.sm.systemaudit.model.AuditActivity;

/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value = "auditActivityService")
public class AuditActivityServiceImpl implements AuditActivityService {

	
	@Autowired
	private AuditActivityDao auditActivityDao;
	
	/**
	 * Method will fetch all audit activity by sub entity id.
	 * @param Id
	 * @return List<AuditActivity> 
	 */
	@Transactional
	@Override
	public List<AuditActivity> getAllAuditActivityBySubEntityId(int Id) {
		return auditActivityDao.getAllAuditActivityBySubEntityId(Id);
	}

	/**
	 * Method will fetch all audit activity by sub entity id.
	 * @return List<AuditActivity> 
	 */
	@Transactional
	@Override
	public List<AuditActivity> getAllAuditActivity() {
		return auditActivityDao.getAllAuditActivity();
	}
	
	
}
