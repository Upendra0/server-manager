package com.elitecore.sm.migration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.migration.dao.MigrationEntityMappingDao;
import com.elitecore.sm.migration.model.MigrationEntityMapping;

@Service(value="migrationEntityMappingService")
public class MigrationEntityMappingServiceImpl implements MigrationEntityMappingService {
	
	@Autowired
	private MigrationEntityMappingDao migrationEntityMappingDao;
	
	@Override
	@Transactional(readOnly=true)
	public ResponseObject getMigrationEntityMappingList() {
		ResponseObject responseObject = new ResponseObject();
		List<MigrationEntityMapping> entityMappings = migrationEntityMappingDao.getMigrationEntityMappingList();
		
		if(entityMappings != null && !entityMappings.isEmpty()) {
			responseObject.setObject(entityMappings);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
}
