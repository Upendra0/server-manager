/**
 * 
 */
package com.elitecore.sm.consolidationservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.consolidationservice.dao.DataConsolidationDao;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.util.EliteUtils;


/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service(value = "iDataConsolidation")
public class DataConsolidationServiceImpl implements IDataConsolidationService{

	@Autowired
	DataConsolidationDao dataConsolidationDao;
	
	@Autowired
	IConsolidationDefinitionService consolidationDefinitionService;
	
	@Override
	@Transactional
	public List<DataConsolidation> getDataConsolidationServicewiseCount(String name, int serviceId) {
		return this.dataConsolidationDao.getDataConsolidationServicewiseCount(name, serviceId);
	}

	@Override
	public void importDataConsolidationMappingUpdateMode(DataConsolidationMapping dbMapping, DataConsolidationMapping exportedMapping) {
		dbMapping.setDestPath(exportedMapping.getDestPath());
		dbMapping.setLogicalOperator(exportedMapping.getLogicalOperator());
		dbMapping.setProcessRecordLimit(exportedMapping.getProcessRecordLimit());
		dbMapping.setConditionList(exportedMapping.getConditionList());
		dbMapping.setCompressedOutput(exportedMapping.isCompressedOutput());
		dbMapping.setWriteOnlyConfiguredAttribute(exportedMapping.isWriteOnlyConfiguredAttribute());
		dbMapping.setFieldNameForCount(exportedMapping.getFieldNameForCount());
		dbMapping.setRecordSortingType(exportedMapping.getRecordSortingType());
		dbMapping.setRecordSortingField(exportedMapping.getRecordSortingField());
		dbMapping.setRecordSortingFieldType(exportedMapping.getRecordSortingFieldType());
		dbMapping.setFileName(exportedMapping.getFileName());
		dbMapping.setFileSequence(exportedMapping.isFileSequence());
		dbMapping.setMinSeqRange(exportedMapping.getMinSeqRange());
		dbMapping.setMaxSeqRange(exportedMapping.getMaxSeqRange());
	}
	
	@Override
	public void importDataConsolidationMappingAddAndKeepBothMode(DataConsolidationMapping exportedMapping, DataConsolidationPathList pathList, int importMode) {
		exportedMapping.setId(0);
		exportedMapping.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedMapping.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedMapping.setCreatedByStaffId(pathList.getCreatedByStaffId());
		exportedMapping.setLastUpdatedByStaffId(pathList.getLastUpdatedByStaffId());
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			exportedMapping.setMappingName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedMapping.getMappingName()));
		}
		exportedMapping.setDataConsPathList(pathList);
	}
	
	@Override
	public DataConsolidationMapping getDataConsolidationMappingFromList(List<DataConsolidationMapping> mappingList, String mappingName, String destPath) {
		if(!CollectionUtils.isEmpty(mappingList)) {
			int length = mappingList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidationMapping dataConsolidationMapping = mappingList.get(i);
				if(dataConsolidationMapping != null && !dataConsolidationMapping.getStatus().equals(StateEnum.DELETED)
						&& dataConsolidationMapping.getMappingName().equalsIgnoreCase(mappingName)
						&& dataConsolidationMapping.getDestPath().equalsIgnoreCase(destPath)) {
					return mappingList.remove(i);
				}
			}
		}
		return null;
	}
	
	@Override
	public void importServiceDataConsolidationUpdateMode(DataConsolidationService dbService, DataConsolidationService exportedService, int importMode) {
		List<DataConsolidation> dbDataConsolidationList = dbService.getConsolidation();
		List<DataConsolidation> exportedDataConsolidationList = exportedService.getConsolidation();
		
		if(!CollectionUtils.isEmpty(exportedDataConsolidationList)) {
			int length = exportedDataConsolidationList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidation exportedDataConsolidation = exportedDataConsolidationList.get(i);
				if(exportedDataConsolidation != null) {
					DataConsolidation dbDataConsolidation = consolidationDefinitionService.getDataConsolidationFromList(dbDataConsolidationList, exportedDataConsolidation.getConsName());
					if(dbDataConsolidation != null && importMode!=BaseConstants.IMPORT_MODE_ADD) {						
						consolidationDefinitionService.importDataConsolidationUpdateMode(dbDataConsolidation, exportedDataConsolidation, importMode);
						dbDataConsolidationList.add(dbDataConsolidation);
					}else if (dbDataConsolidation != null && importMode==BaseConstants.IMPORT_MODE_ADD){
						consolidationDefinitionService.importDataConsolidationAddMode(dbDataConsolidation, exportedDataConsolidation);						
						dbDataConsolidationList.add(dbDataConsolidation);
					} else {
						consolidationDefinitionService.importDataConsolidationAddAndKeepBothMode(exportedDataConsolidation, dbService, BaseConstants.IMPORT_MODE_UPDATE);
						dbDataConsolidationList.add(exportedDataConsolidation);
					}
				}
			}
		}
	}
	
	@Override
	public void importServiceDataConsolidationAddAndKeepBothMode(DataConsolidationService exportedService, int importMode) {
		List<DataConsolidation> exportedDataConsolidationList = exportedService.getConsolidation();
		if(!CollectionUtils.isEmpty(exportedDataConsolidationList)) {
			int length = exportedDataConsolidationList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidation exportedDataConsolidation = exportedDataConsolidationList.get(i);
				if(exportedDataConsolidation != null) {
					consolidationDefinitionService.importDataConsolidationAddAndKeepBothMode(exportedDataConsolidation, exportedService, importMode);
				}
			}
		}
	}
	
}