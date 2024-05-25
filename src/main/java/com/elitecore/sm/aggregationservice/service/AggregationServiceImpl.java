package com.elitecore.sm.aggregationservice.service;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationCondition;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.util.EliteUtils;


@org.springframework.stereotype.Service(value = "iDataAggregation")
public class AggregationServiceImpl implements IAggregationService{

	private Logger logger = Logger.getLogger(this.getClass().getName());

	
	@Autowired
	IAggregationDefinitionService aggDefinitionService;
	
	@Autowired
	IAggregationAttributeService aggAttributeService;

	@Autowired
	IAggregationKeyAttributeService aggKeyAttributeService;
	
	@Autowired
	IAggregationConditionService aggConditionService;
	
	@Override
	public void importServiceAggregationAddAndKeepBothMode(AggregationService exportedService, int importMode) {
		AggregationDefinition exportedDefinition = exportedService.getAggregationDefinition();
		if(exportedDefinition != null && !exportedDefinition.getStatus().equals(StateEnum.DELETED)){
			if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
				exportedDefinition.setAggDefName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedDefinition.getAggDefName()));
			}
			exportedDefinition.setId(0);
			exportedDefinition.setCreatedDate(EliteUtils.getDateForImport(false));
			exportedDefinition.setLastUpdatedDate(EliteUtils.getDateForImport(false));
			exportedDefinition.setCreatedByStaffId(exportedService.getCreatedByStaffId());
			exportedDefinition.setLastUpdatedByStaffId(exportedService.getLastUpdatedByStaffId());
			exportedDefinition.setAggDefName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedDefinition.getAggDefName()));
			
			List<AggregationCondition> exportedConditionList = exportedDefinition.getAggConditionList();
			if(exportedConditionList != null && !exportedConditionList.isEmpty()) {
				int length = exportedConditionList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationCondition exportedCondition = exportedConditionList.get(i);
					if(exportedCondition != null) {
						aggConditionService.importDataAggregationConditionAddAndKeepBothMode(exportedCondition, exportedDefinition, importMode);
					}
				}
			}
			
			List<AggregationKeyAttribute> exportedKeyAttrList = exportedDefinition.getAggKeyAttrList();
			if(exportedKeyAttrList != null && !exportedKeyAttrList.isEmpty()) {
				int length = exportedKeyAttrList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationKeyAttribute exportedKeyAttribute = exportedKeyAttrList.get(i);
					if(exportedKeyAttribute != null) {
						aggKeyAttributeService.importDataAggregationKeyAttributeAddAndKeepBothMode(exportedKeyAttribute, exportedDefinition, importMode);
					}
				}
			}
			
			List<AggregationAttribute> exportedAggAttrList = exportedDefinition.getAggAttrList();
			if(exportedAggAttrList != null && !exportedAggAttrList.isEmpty()) {
				int length = exportedAggAttrList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationAttribute exportedAggAttribute = exportedAggAttrList.get(i);
					if(exportedAggAttribute != null) {
						aggAttributeService.importDataAggregationAttributeAddAndKeepBothMode(exportedAggAttribute, exportedDefinition, importMode);
					}
				}
			}
			exportedService.setAggregationDefinition(exportedDefinition);
			exportedDefinition.setAggregationService(exportedService);
		}
	}
	
	@Override
	public void importServiceAggregationDefinitionUpdateMode(AggregationService dbService, AggregationService exportedService) {
		AggregationDefinition dbDefinition = dbService.getAggregationDefinition();
		AggregationDefinition exportedDefinition = exportedService.getAggregationDefinition();
		
		if(exportedDefinition != null){
			if(dbDefinition == null){
				dbDefinition = new AggregationDefinition();
				dbDefinition.setAggDefName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedDefinition.getAggDefName()));
			}
			dbDefinition.setNoOfPartition(exportedDefinition.getNoOfPartition());
			dbDefinition.setPartCDRField(exportedDefinition.getPartCDRField());
			dbDefinition.setfLegVal(exportedDefinition.getfLegVal());
			dbDefinition.setlLegVal(exportedDefinition.getlLegVal());
			dbDefinition.setUnifiedDateFiled(exportedDefinition.getUnifiedDateFiled());
			dbDefinition.setAggInterval(exportedDefinition.getAggInterval());
			dbDefinition.setOutputFileField(exportedDefinition.getOutputFileField());
			dbDefinition.setAggregationService(dbService);
			dbService.setAggregationDefinition(dbDefinition);
			List<AggregationAttribute> dbAttributeList = dbDefinition.getAggAttrList();
			List<AggregationAttribute> exportedAttributeList = exportedDefinition.getAggAttrList();
			
			if(!CollectionUtils.isEmpty(exportedAttributeList)) {
				int length = exportedAttributeList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationAttribute exportedAttribute = exportedAttributeList.get(i);
					if(exportedAttribute != null) {
						AggregationAttribute dbAttribute = aggAttributeService.getDataAggregationAttributeFromList(dbAttributeList, exportedAttribute.getOutputFieldName());
						if(dbAttribute != null) {
							logger.debug("going to update attribute : "+dbAttribute.getOutputFieldName());
							aggAttributeService.importDataAggregationAttributeUpdateMode(dbAttribute, exportedAttribute);
							dbAttributeList.add(dbAttribute);
						} else {
							logger.debug("going to add attribute : "+exportedAttribute.getOutputFieldName());
							aggAttributeService.importDataAggregationAttributeAddAndKeepBothMode(exportedAttribute, dbDefinition, BaseConstants.IMPORT_MODE_UPDATE);
							dbAttributeList.add(exportedAttribute);
						}
					}
				}
			}
			dbDefinition.setAggAttrList(dbAttributeList);
			
			List<AggregationKeyAttribute> dbKeyAttributeList = dbDefinition.getAggKeyAttrList();
			List<AggregationKeyAttribute> exportedKeyAttributeList = exportedDefinition.getAggKeyAttrList();
			
			if(!CollectionUtils.isEmpty(exportedKeyAttributeList)) {
				int length = exportedKeyAttributeList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationKeyAttribute exportedKeyAttribute = exportedKeyAttributeList.get(i);
					if(exportedKeyAttribute != null) {
						AggregationKeyAttribute dbKeyAttribute = aggKeyAttributeService.getDataAggregationKeyAttributeFromList(dbKeyAttributeList, exportedKeyAttribute.getFieldName());
						if(dbKeyAttribute == null) {
							logger.debug("going to add attribute : "+exportedKeyAttribute.getFieldName());
							aggKeyAttributeService.importDataAggregationKeyAttributeAddAndKeepBothMode(exportedKeyAttribute, dbDefinition, BaseConstants.IMPORT_MODE_UPDATE);
							dbKeyAttributeList.add(exportedKeyAttribute);
						}					
					}
				}
			}
			dbDefinition.setAggKeyAttrList(dbKeyAttributeList);
			
			List<AggregationCondition> dbConditionList = dbDefinition.getAggConditionList();
			List<AggregationCondition> exportedConditionList = exportedDefinition.getAggConditionList();
			
			if(!CollectionUtils.isEmpty(exportedConditionList)) {
				int length = exportedConditionList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationCondition exportedCondition = exportedConditionList.get(i);
					if(exportedCondition != null) {
						AggregationCondition dbCondition = aggConditionService.getDataConditionFromList(dbConditionList, exportedCondition.getCondExpression());
						if(dbCondition != null) {
							logger.debug("going to update attribute : "+dbCondition.getCondExpression());
							aggConditionService.importDataConditionUpdateMode(dbCondition, exportedCondition);
							dbConditionList.add(dbCondition);
						} else {
							logger.debug("going to add attribute : "+exportedCondition.getCondExpression());
							aggConditionService.importDataAggregationConditionAddAndKeepBothMode(exportedCondition, dbDefinition, BaseConstants.IMPORT_MODE_UPDATE);
							dbConditionList.add(exportedCondition);
						}
					}
				}
			}
			dbDefinition.setAggConditionList(dbConditionList);
		}
	}
	
	@Override
	public void importServiceAggregationDefinitionAddMode(AggregationService dbService, AggregationService exportedService) {
		AggregationDefinition dbDefinition = dbService.getAggregationDefinition();
		AggregationDefinition exportedDefinition = exportedService.getAggregationDefinition();
		
		if(exportedDefinition != null){
			/*if(dbDefinition == null){
				dbDefinition = new AggregationDefinition();
				dbDefinition.setAggDefName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedDefinition.getAggDefName()));
			}
			dbDefinition.setNoOfPartition(exportedDefinition.getNoOfPartition());
			dbDefinition.setPartCDRField(exportedDefinition.getPartCDRField());
			dbDefinition.setfLegVal(exportedDefinition.getfLegVal());
			dbDefinition.setlLegVal(exportedDefinition.getlLegVal());
			dbDefinition.setUnifiedDateFiled(exportedDefinition.getUnifiedDateFiled());
			dbDefinition.setAggInterval(exportedDefinition.getAggInterval());
			dbDefinition.setOutputFileField(exportedDefinition.getOutputFileField());
			dbDefinition.setAggregationService(dbService);
			dbService.setAggregationDefinition(dbDefinition);*/
			List<AggregationAttribute> dbAttributeList = dbDefinition.getAggAttrList();
			List<AggregationAttribute> exportedAttributeList = exportedDefinition.getAggAttrList();
			
			if(!CollectionUtils.isEmpty(exportedAttributeList)) {
				int length = exportedAttributeList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationAttribute exportedAttribute = exportedAttributeList.get(i);
					if(exportedAttribute != null) {
						AggregationAttribute dbAttribute = aggAttributeService.getDataAggregationAttributeFromList(dbAttributeList, exportedAttribute.getOutputFieldName());
						if(dbAttribute == null) {
							logger.debug("going to add attribute : "+exportedAttribute.getOutputFieldName());
							aggAttributeService.importDataAggregationAttributeAddAndKeepBothMode(exportedAttribute, dbDefinition, BaseConstants.IMPORT_MODE_UPDATE);
							dbAttributeList.add(exportedAttribute);
						}
					}
				}
			}
			dbDefinition.setAggAttrList(dbAttributeList);
			
			List<AggregationKeyAttribute> dbKeyAttributeList = dbDefinition.getAggKeyAttrList();
			List<AggregationKeyAttribute> exportedKeyAttributeList = exportedDefinition.getAggKeyAttrList();
			
			if(!CollectionUtils.isEmpty(exportedKeyAttributeList)) {
				int length = exportedKeyAttributeList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationKeyAttribute exportedKeyAttribute = exportedKeyAttributeList.get(i);
					if(exportedKeyAttribute != null) {
						AggregationKeyAttribute dbKeyAttribute = aggKeyAttributeService.getDataAggregationKeyAttributeFromList(dbKeyAttributeList, exportedKeyAttribute.getFieldName());
						if(dbKeyAttribute == null) {
							logger.debug("going to add attribute : "+exportedKeyAttribute.getFieldName());
							aggKeyAttributeService.importDataAggregationKeyAttributeAddAndKeepBothMode(exportedKeyAttribute, dbDefinition, BaseConstants.IMPORT_MODE_UPDATE);
							dbKeyAttributeList.add(exportedKeyAttribute);
						}					
					}
				}
			}
			dbDefinition.setAggKeyAttrList(dbKeyAttributeList);
			
			List<AggregationCondition> dbConditionList = dbDefinition.getAggConditionList();
			List<AggregationCondition> exportedConditionList = exportedDefinition.getAggConditionList();
			
			if(!CollectionUtils.isEmpty(exportedConditionList)) {
				int length = exportedConditionList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationCondition exportedCondition = exportedConditionList.get(i);
					if(exportedCondition != null) {
						AggregationCondition dbCondition = aggConditionService.getDataConditionFromList(dbConditionList, exportedCondition.getCondExpression());
						if(dbCondition == null) {							
							logger.debug("going to add attribute : "+exportedCondition.getCondExpression());
							aggConditionService.importDataAggregationConditionAddAndKeepBothMode(exportedCondition, dbDefinition, BaseConstants.IMPORT_MODE_UPDATE);
							dbConditionList.add(exportedCondition);
						}
					}
				}
			}
			dbDefinition.setAggConditionList(dbConditionList);
		}
	}
	
}